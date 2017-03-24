package com.github.btheu.table.mapper.poi;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;

import com.github.btheu.table.mapper.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author btheu
 *
 */
@Slf4j
public class PoiUtils {

    private static final DecimalFormat DF = new DecimalFormat("#");
    private static final NumberFormat  NF = NumberFormat.getInstance(Locale.FRENCH);

    public static String getValueString(Cell cell) {
        if (cell == null) {
            return null;
        }
        int cellType = cell.getCellType();
        switch (cellType) {
        case Cell.CELL_TYPE_BLANK:
            return "";
        case Cell.CELL_TYPE_BOOLEAN:
            return Boolean.toString(cell.getBooleanCellValue());
        case Cell.CELL_TYPE_ERROR:
            return "error";
        case Cell.CELL_TYPE_FORMULA:
            return cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator().evaluate(cell)
                    .getStringValue();
        case Cell.CELL_TYPE_NUMERIC:
            return DF.format(cell.getNumericCellValue());
        case Cell.CELL_TYPE_STRING:
            return cell.getStringCellValue();
        default:
            log.error("Type unknown : '{}'", cellType);
        }

        return null;
    }

    public static Cell getCellWithColumnOffsetX(Cell cell, int columnOffset) {
        return cell.getRow().getCell(cell.getColumnIndex() + columnOffset);
    }

    public static Double getDoubleValue(Cell currentCell) {

        if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return currentCell.getNumericCellValue();
        }
        if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
            String stringCellValue = currentCell.getStringCellValue();
            if (stringCellValue.trim().isEmpty()) {
                return null;
            }
            try {
                return NF.parse(stringCellValue).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {

            return currentCell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator()
                    .evaluate(currentCell).getNumberValue();
        }

        return null;
    }

    public static Long getLongValue(Cell currentCell) {
        if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return Math.round(currentCell.getNumericCellValue());
        }
        if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
            String stringCellValue = currentCell.getStringCellValue();
            if (stringCellValue.trim().isEmpty()) {
                return null;
            }
            return Long.parseLong(currentCell.getStringCellValue());
        }
        if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {

            return (long) currentCell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator()
                    .evaluate(currentCell).getNumberValue();
        }

        return null;
    }

    public static Integer getIntValue(Cell currentCell) {
        if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return (int) currentCell.getNumericCellValue();
        }
        if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
            String stringCellValue = currentCell.getStringCellValue();
            if (stringCellValue.trim().isEmpty()) {
                return null;
            }
            return Integer.parseInt(currentCell.getStringCellValue());
        }
        if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {

            return (int) currentCell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator()
                    .evaluate(currentCell).getNumberValue();
        }
        return null;
    }

    public static BigDecimal getBigDecimalValue(Cell currentCell) {
        if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return new BigDecimal(currentCell.getNumericCellValue());
        }
        if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
            String stringCellValue = currentCell.getStringCellValue();
            if (stringCellValue.trim().isEmpty()) {
                return null;
            }
            return new BigDecimal(stringCellValue);
        }
        if (currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {

            return new BigDecimal(currentCell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator()
                    .evaluate(currentCell).getNumberValue());
        }
        return null;
    }

    public static Date getDateValue(Cell valueCell, String format) {
        if (valueCell.getCellType() == Cell.CELL_TYPE_STRING) {

            return DateUtils.parse(getValueString(valueCell), format);
        } else {

            return valueCell.getDateCellValue();
        }

    }
}
