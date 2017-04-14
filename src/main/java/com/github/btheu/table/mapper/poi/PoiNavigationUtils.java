package com.github.btheu.table.mapper.poi;

import org.apache.poi.ss.usermodel.Row;

/**
 * 
 * @author btheu
 *
 */
public class PoiNavigationUtils {

    public static Row nextRowDown(Row row) {
        return row.getSheet().getRow(row.getRowNum() + 1);
    }

}
