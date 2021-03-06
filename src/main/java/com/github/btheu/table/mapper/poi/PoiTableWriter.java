package com.github.btheu.table.mapper.poi;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.github.btheu.table.mapper.internal.TableData;
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
            TableData tableData = TableParser.parseClass(valueRows.get(0).getClass());

            write(wb, valueRows, tableData);
        }

    }

    private static <T> void write(Workbook workbook, List<T> valueRows, TableData columns) {

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

    private static <T> void write(Sheet sheet, List<T> valueRows, TableData columns) {

        Header headerRow = PoiTableParser.findHeaderRow(sheet, columns);
        if (headerRow == null) {
            log.debug("[{}] have no table for [{}]", sheet.getSheetName(), columns.getDataClass().getSimpleName());
        }

        RowIndex<T> index = new RowIndex<T>();

        int lineNumber = headerRow.getHeaderRow().getRowNum();

        // 1. Indexation of row with primary info
        int nbEmptyRow = 0;
        while (nbEmptyRow <= sheet.getLastRowNum()) {
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

        List<T> rows4inserts = new ArrayList<T>();

        // 2. Update rows
        for (T valueRow : valueRows) {
            Row tuple = index.find(valueRow, headerRow);
            if (tuple == null) {

                rows4inserts.add(valueRow);

            } else {
                log.debug("> update row {}", valueRow);

                writeUpdate(tuple, headerRow, valueRow, columns);

                log.debug("< update row");
            }
        }

        writeInsert(rows4inserts, headerRow, columns);
    }

    private static <T> void writeInsert(List<T> rows4inserts, Header headerRow, TableData columns) {
        Row tuple = findFirstEmptyRow(headerRow, headerRow.getHeaderRow());

        for (T valueRow : rows4inserts) {
            log.debug("> insert row {}", valueRow);

            writeUpdate(tuple, headerRow, valueRow, columns);

            log.debug("< insert row {}", valueRow);

            tuple = findFirstEmptyRow(headerRow, tuple);
        }

    }

    private static Row findFirstEmptyRow(Header headerRow, Row row) {

        Row nextRow = PoiNavigationUtils.nextRowDown(row);
        int index = row.getRowNum() + 1;

        while (!PoiTableParser.isEmptyRow2(headerRow, nextRow)) {
            index = nextRow.getRowNum() + 1;
            nextRow = PoiNavigationUtils.nextRowDown(nextRow);
        }

        if (nextRow == null) {
            nextRow = headerRow.getHeaderRow().getSheet().createRow(index);
            log.debug("create row {}", nextRow.getRowNum() + 1);
        }
        return nextRow;
    }

    private static <T> void writeUpdate(Row tuple, Header headerRow, T valueRow, TableData columns) {

        log.debug("write L{} {}", tuple.getRowNum() + 1, valueRow);

        for (HeaderCell headerCell : headerRow) {

            Cell cell = tuple.getCell(headerCell.getHeaderCell().getColumnIndex());
            if (cell == null) {
                cell = tuple.createCell(headerCell.getHeaderCell().getColumnIndex());
                log.debug("create cell {}", PoiUtils.toString(cell));
            }

            Field field = headerCell.getColumn().getField();

            Object value = ReflectionUtils.getValue(valueRow, field);

            PoiUtils.setValue(cell, headerCell.getColumn().getType(), value);

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
         * FIXME btheu makes better createHashKey (lombok like)
         * 
         * Build a hashKey from current row and primary key
         * 
         * @param row
         * @param headerRow
         * @return
         */
        private String createHashKey(T row, Header headerRow) {
            StringBuilder sb = new StringBuilder();
            for (HeaderCell headerCell : headerRow) {
                if (headerCell.getColumn().isPrimaryKey()) {

                    Field field = headerCell.getColumn().getField();

                    Object value = ReflectionUtils.getValue(row, field);
                    if (value == null) {
                        sb.append("null");
                    } else {
                        sb.append(value.toString());
                    }
                    sb.append("$$");
                }
            }
            return sb.toString();
        }

    }
}
