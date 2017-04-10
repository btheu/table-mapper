package com.github.btheu.table.mapper.poi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.github.btheu.table.mapper.SheetAll;
import com.github.btheu.table.mapper.internal.Columns;
import com.github.btheu.table.mapper.internal.Columns.Entry;
import com.github.btheu.table.mapper.internal.TableParser;
import com.github.btheu.table.mapper.poi.Header.HeaderCell;

import lombok.extern.slf4j.Slf4j;

/**
 * Classe Privée pour parser un fichier Excel
 * 
 * @author btheu
 *
 */
@Slf4j
public class PoiTableParser {

    /**
     * Size max of table header. Aka, column number max.
     */
    public static int HEADER_SIZE_MAX = 100;

    /**
     * The number of empty line telling that the table is ended.
     */
    public static int EMPTY_LINES_FOR_END = 10;

    public static <T> List<T> parse(Workbook workbook, Class<T> class1) {

        Columns columns = TableParser.parseClass(class1);

        return parse(workbook, columns);
    }

    private static <T> List<T> parse(Workbook workbook, Columns columns) {

        List<T> results = new ArrayList<T>();

        List<String> sheetNames = extractSheetsNames(workbook, columns.getDataClass());

        for (String sheetName : sheetNames) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' was not found in the WorkBook");
            }

            log.debug("Analysing: {}", sheetName);

            List<T> parse = parse(sheet, columns);

            results.addAll(parse);
        }

        return results;

    }

    private static <T> List<T> parse(Sheet sheet, Columns columns) {

        List<T> results = new ArrayList<T>();

        HeaderRow headerRow = findHeaderRow(sheet, columns);
        if (headerRow == null) {
            log.debug("[{}] have no table for [{}]", sheet.getSheetName(), columns.getDataClass().getSimpleName());
            return results;
        }

        int lineNumber = headerRow.getRow().getRowNum();

        int nbEmptyRow = 0;
        while (nbEmptyRow < EMPTY_LINES_FOR_END) {
            Row currentRow = sheet.getRow(++lineNumber);

            if (isEmptyRow(headerRow, currentRow)) {
                nbEmptyRow++;
            } else {
                nbEmptyRow = 0;

                T parse = parse(headerRow, currentRow, columns);

                results.add(parse);
            }
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    public static <T> T parse(Header header, Row currentRow, Columns columns) {

        try {
            T result = (T) columns.getDataClass().newInstance();

            for (HeaderCell headerCell : header) {

                PoiMapper.setValue(headerCell.getEntry(), result,
                        currentRow.getCell(headerCell.getHeaderCell().getColumnIndex()));

            }

            return result;

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public static <T> T parse(HeaderRow headerRow, Row currentRow, Columns columns) {

        try {
            T result = (T) columns.getDataClass().newInstance();

            for (int columnIndex = headerRow.getCellIndexBegin(); columnIndex <= headerRow
                    .getCellIndexEnd(); columnIndex++) {

                PoiMapper.map(columns, result, headerRow.getRow().getCell(columnIndex),
                        currentRow.getCell(columnIndex));
            }

            return result;

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isEmptyRow2(Header headerRow, Row currentRow) {
        if (currentRow == null) {
            return true;
        }

        for (HeaderCell headerCell : headerRow) {
            Cell currentCell = currentRow.getCell(headerCell.getHeaderCell().getColumnIndex());

            String value = PoiUtils.getValueString(currentCell);
            if (StringUtils.isNotBlank(value)) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    public static boolean isEmptyRow(HeaderRow headerRow, Row currentRow) {
        if (currentRow == null) {
            return true;
        }
        for (int i = headerRow.getCellIndexBegin(); i <= headerRow.getCellIndexEnd(); i++) {

            Cell cell = currentRow.getCell(i);

            String value = PoiUtils.getValueString(cell);
            if (StringUtils.isNotBlank(value)) {
                return false;
            }

        }
        return true;
    }

    public static Header findHeaderRow2(Sheet sheet, Columns columns) {

        for (int rowIndex = 0; rowIndex < HEADER_SIZE_MAX; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                for (int cellIndex = 0; cellIndex < HEADER_SIZE_MAX; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    if (isCellFromHeader(cell, columns)) {
                        int lastIndex = isHeaderRow(cell, columns);
                        if (lastIndex != -1) {
                            log.debug("Header found");

                            return new Header(cell, columns);
                        }
                    }
                }
            }
        }
        return null;

    }

    /**
     * Recherche une ligne dans le tableur correspondant aux colonnes attendues
     * par le POJO
     * 
     * @param sheet
     *            La feuille a parcourir
     * @param columns
     *            Le nom des colonnes reherchées
     * @return Un objet contenant les informations relatives à l'entete du
     *         tableau recherché, null si le tableau n'a pas été trouvé
     * @deprecated user findHeaderRow2
     */
    @Deprecated
    public static HeaderRow findHeaderRow(Sheet sheet, Columns columns) {
        for (int rowIndex = 0; rowIndex < HEADER_SIZE_MAX; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                for (int cellIndex = 0; cellIndex < HEADER_SIZE_MAX; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    if (isCellFromHeader(cell, columns)) {
                        int lastIndex = isHeaderRow(cell, columns);
                        if (lastIndex != -1) {
                            log.debug("Header found");

                            HeaderRow headerRow = new HeaderRow();
                            headerRow.setRow(cell.getRow());
                            headerRow.setCellIndexBegin(cell.getColumnIndex());
                            headerRow.setCellIndexEnd(lastIndex);
                            return headerRow;

                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 
     * @param firstCell
     * @param columns
     * @return The last column index cell of the table
     */
    private static int isHeaderRow(Cell firstCell, Columns columns) {

        int indexFirstCell = firstCell.getColumnIndex();
        int indexOfLastColumn = -1;

        for (Entry column : columns.getColumns()) {
            boolean found = false;
            for (int cellIndex = indexFirstCell; cellIndex < indexFirstCell + HEADER_SIZE_MAX; cellIndex++) {

                Cell current = firstCell.getRow().getCell(cellIndex);

                if (isCellFromHeader(current, column)) {
                    found = true;

                    indexOfLastColumn = Math.max(indexOfLastColumn, cellIndex);

                    break;
                }
            }
            if (!found) {
                return -1;
            }
        }
        return indexOfLastColumn;
    }

    private static boolean isCellFromHeader(Cell cell, Entry column) {
        return cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING && column.match(cell.getStringCellValue());
    }

    private static boolean isCellFromHeader(Cell cell, Columns columns) {
        return cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING && columns.match(cell.getStringCellValue());
    }

    public static <T> List<String> extractSheetsNames(Workbook workbook, Class<T> class1) {
        List<String> names = new ArrayList<String>();

        // Annotation @SheetAll
        SheetAll annotationSheetAll = class1.getAnnotation(SheetAll.class);

        if (annotationSheetAll != null) {
            // Toutes les feuilles
            int numberOfSheets = workbook.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                names.add(workbook.getSheetName(i));
            }
            return names;
        }

        // Annotation @Sheet
        com.github.btheu.table.mapper.Sheet annotationSheet = class1
                .getAnnotation(com.github.btheu.table.mapper.Sheet.class);

        if (annotationSheet == null) {
            // Par defaut, la premiere feuille si pas d'annotation
            names.addAll(Arrays.asList(workbook.getSheetName(0)));
        } else {
            names.addAll(Arrays.asList(annotationSheet.value()));
        }

        return names;
    }

}
