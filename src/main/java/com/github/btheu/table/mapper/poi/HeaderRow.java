package com.github.btheu.table.mapper.poi;

import org.apache.poi.ss.usermodel.Row;

import lombok.Data;

/**
 * 
 * @author theunissenb
 *
 */
@Data
public class HeaderRow {

    private Row row;
    private int cellIndexBegin;
    private int cellIndexEnd;

}
