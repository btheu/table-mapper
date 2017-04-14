package com.github.btheu.table.mapper.poi;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.github.btheu.table.mapper.internal.Columns;
import com.github.btheu.table.mapper.internal.TableParser;
import com.github.btheu.table.mapper.poi.Header.HeaderCell;
import com.github.btheu.table.mapper.utils.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Private class able to write pojos into poi file
 * 
 * @author btheu
 *
 */
@Slf4j
public class PoiTableWriter {

    public static <T> void write(Workbook wb, List<T> valueRows) {

        if (valueRows.isEmpty()) {
            log.debug("empty rows, nothing to do");
        } else {
            Columns columns = TableParser.parseClass(valueRows.get(0).getClass());

            write(wb, valueRows, columns);
        }

    }

    private static <T> void write(Workbook workbook, List<T> valueRows, Columns columns) {

        List<String> sheetNames = PoiTableParser.extractSheetsNames(workbook, columns.getDataClass());

        for (String sheetName : sheetNames) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' was not found in the WorkBook");
            }

            log.debug("Analysing: {}", sheetName);

            write(sheet, valueRows, columns);
        }

    }

    private static <T> void write(Sheet sheet, List<T> valueRows, Columns columns) {

        Header headerRow = PoiTableParser.findHeaderRow2(sheet, columns);
        if (headerRow == null) {
            log.debug("[{}] have no table for [{}]", sheet.getSheetName(), columns.getDataClass().getSimpleName());
        }

        RowIndex<T> index = new RowIndex<T>();

        int lineNumber = headerRow.getHeaderRow().getRowNum();

        // 1. Indexation of row with primary info
        int nbEmptyRow = 0;
        while (nbEmptyRow < PoiTableParser.EMPTY_LINES_FOR_END) {
            Row currentRow = sheet.getRow(++lineNumber);

            if (PoiTableParser.isEmptyRow2(headerRow, currentRow)) {
                nbEmptyRow++;
            } else {
                nbEmptyRow = 0;

                // Create data instance from table for hash map key creation
                T rowData = PoiTableParser.parse(headerRow, currentRow, columns);

                index.add(rowData, currentRow, headerRow);
            }
        }

        // 2. Update rows
        for (T valueRow : valueRows) {
            Row tuple = index.find(valueRow, headerRow);
            if (tuple == null) {
                log.warn("row not found for update, insertable not implemented. {}", valueRow);
            } else {
                log.debug("> update row");

                writeUpdate(tuple, headerRow, valueRow, columns);

                log.debug("< update row");
            }
        }
    }

    private static <T> void writeUpdate(Row tuple, Header headerRow, T valueRow, Columns columns) {

        for (HeaderCell headerCell : headerRow) {

            Cell cell = tuple.getCell(headerCell.getHeaderCell().getColumnIndex());

            Field field = headerCell.getEntry().getField();

            Object value = ReflectionUtils.getValue(valueRow, field);

            PoiUtils.setValue(cell, headerCell.getEntry().getType(), value);

        }

    }

    /**
     * 
     * @author NeoMcFly
     *
     * @param <T>
     *            the pojo type
     */
    public static class RowIndex<T> {

        protected Map<String, Row> index = new HashMap<String, Row>();

        public void add(T rowData, Row currentRow, Header headerRow) {

            String key = createHashKey(rowData, headerRow);

            index.put(key, currentRow);
        }

        public Row find(T rowData, Header headerRow) {

            String key = createHashKey(rowData, headerRow);

            return index.get(key);
        }

        /**
         * Build a hashKey from current row and primary key
         * 
         * @param row
         * @param headerRow
         * @return
         */
        private String createHashKey(T row, Header headerRow) {
            StringBuilder sb = new StringBuilder();
            for (HeaderCell headerCell : headerRow) {
                if (headerCell.getEntry().isPrimaryKey()) {

                    Field field = headerCell.getEntry().getField();

                    Object value = ReflectionUtils.getValue(row, field);

                    sb.append(value.toString());
                    sb.append("$$");
                }
            }
            return sb.toString();
        }

    }
}
