package com.github.btheu.table.mapper.poi;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.github.btheu.table.mapper.Column;
import com.github.btheu.table.mapper.utils.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Classe Privée pour parser un fichier Excel
 * 
 * @author theunissenb
 *
 */
@Slf4j
public class PoiTableParser {

    public static <T> List<T> parse(Workbook workbook, Class<T> class1) {

        List<String> colonneNames = extractColumns(class1);

        return parse(workbook, class1, colonneNames);
    }

    private static <T> List<T> parse(Workbook workbook, Class<T> class1, List<String> colonneNames) {

        List<T> results = new ArrayList<T>();

        List<String> sheetNames = extractSheetsNames(workbook, class1);

        for (String sheetName : sheetNames) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' was not found in the WorkBook");
            }

            log.debug("Reading: {}", sheetName);

            List<T> parse = parse(sheet, class1, colonneNames);

            results.addAll(parse);
        }

        return results;

    }

    private static <T> List<T> parse(Sheet sheet, Class<T> class1, List<String> colonneNames) {

        List<T> results = new ArrayList<T>();

        HeaderRow headerRow = findHeaderRow(sheet, colonneNames);
        if (headerRow == null) {
            log.debug("[{}] have no table for [{}]", sheet.getSheetName(), class1.getSimpleName());
            return results;
        }

        Row currentRow = sheet.getRow(headerRow.getRow().getRowNum() + 1);

        int nbEmptyRow = 0;
        while (nbEmptyRow < 5) {

            if (isEmptyRow(headerRow, currentRow)) {
                nbEmptyRow++;
                continue;
            } else {
                nbEmptyRow = 0;
            }

            T parse = parse(headerRow, currentRow, class1, colonneNames);

            results.add(parse);

            currentRow = sheet.getRow(currentRow.getRowNum() + 1);
        }

        return results;
    }

    private static <T> T parse(HeaderRow headerRow, Row currentRow, Class<T> class1, List<String> colonneNames) {

        try {
            T result = class1.newInstance();

            for (int columnIndex = headerRow.getCellIndexBegin(); columnIndex <= headerRow
                    .getCellIndexEnd(); columnIndex++) {

                PoiMapper.map(result, headerRow.getRow().getCell(columnIndex), currentRow.getCell(columnIndex));
            }

            return result;

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isEmptyRow(HeaderRow headerRow, Row currentRow) {
        if (currentRow == null) {
            return true;
        }
        for (int i = headerRow.getCellIndexBegin(); i <= headerRow.getCellIndexEnd(); i++) {

            Cell cell = currentRow.getCell(i);

            String value = PoiUtils.getValueString(cell);
            if (!StringUtils.isEmpty(value)) {
                return false;
            }

        }
        return true;
    }

    /**
     * Recherche une ligne dans le tableur correspondant aux colonnes attendues par le POJO
     * 
     * @param sheet
     *            La feuille a parcourir
     * @param colonneNames
     *            Le nom des colonnes reherchées
     * @return Un objet contenant les informations relatives à l'entete du tableau recherché, null si le tableau n'a pas
     *         été trouvé
     */
    private static HeaderRow findHeaderRow(Sheet sheet, List<String> colonneNames) {
        for (int rowIndex = 0; rowIndex < 50; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            for (int cellIndex = 0; cellIndex < 50; cellIndex++) {
                if (row != null) {
                    Cell cell = row.getCell(cellIndex);
                    if (isCellFromHeader(cell, colonneNames)) {
                        int lastIndex = isHeaderRow(cell, colonneNames);
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
     * @param colonneNames
     * @return The last column index cell of the table
     */
    private static int isHeaderRow(Cell firstCell, List<String> colonneNames) {

        int indexFirstCell = firstCell.getColumnIndex();
        int indexOfLastColumn = -1;

        for (String colonneName : colonneNames) {
            boolean found = false;
            for (int cellIndex = indexFirstCell; cellIndex < indexFirstCell + 50; cellIndex++) {

                Cell current = firstCell.getRow().getCell(cellIndex);

                if (isCellFromHeader(current, colonneName)) {
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

    private static boolean isCellFromHeader(Cell cell, String colonneName) {
        return cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING
                && colonneName.equalsIgnoreCase(cell.getStringCellValue());
    }

    private static boolean isCellFromHeader(Cell cell, List<String> colonneNames) {
        return cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING
                && contains(colonneNames, cell.getStringCellValue());
    }

    private static boolean contains(Collection<String> array, String value) {
        for (String arrayValue : array) {
            if (arrayValue.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    private static <T> List<String> extractSheetsNames(Workbook workbook, Class<T> class1) {
        List<String> names = new ArrayList<String>();

        // Annotation @SheetAll
        com.github.btheu.table.mapper.SheetAll annotationSheetAll = class1
                .getAnnotation(com.github.btheu.table.mapper.SheetAll.class);

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

    private static <T> List<String> extractColumns(Class<T> class1) {

        List<String> names = new ArrayList<String>();

        List<Field> allFields = ReflectionUtils.getAllFields(class1);
        for (Field field : allFields) {
            Column annotation = field.getAnnotation(Column.class);
            if (annotation != null) {
                names.add(annotation.value());
            }
        }

        return names;
    }

}
