package com.github.btheu.table.mapper.poi;

import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.github.btheu.table.mapper.internal.Columns;
import com.github.btheu.table.mapper.internal.TableParser;

import lombok.extern.slf4j.Slf4j;

/**
 * Private class able to write pojos into poi file
 * 
 * @author btheu
 *
 */
@Slf4j
public class PoiTableWriter {

    public static <T> void write(Workbook wb, List<T> rows) {

        if (rows.isEmpty()) {
            log.debug("empty rows, nothing to do");
        } else {
            Columns columns = TableParser.parseClass(rows.get(0).getClass());

            write(wb, rows, columns);
        }

    }

    private static <T> void write(Workbook workbook, List<T> rows, Columns columns) {

        List<String> sheetNames = PoiTableParser.extractSheetsNames(workbook, columns.getDataClass());

        for (String sheetName : sheetNames) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' was not found in the WorkBook");
            }

            log.debug("Analysing: {}", sheetName);

            write(sheet, rows, columns);
        }

    }

    private static <T> void write(Sheet sheet, List<T> rows, Columns columns) {
        // TODO Auto-generated method stub
    }

}
