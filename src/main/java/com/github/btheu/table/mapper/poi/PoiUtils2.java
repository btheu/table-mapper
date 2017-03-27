package com.github.btheu.table.mapper.poi;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;

import com.github.btheu.table.mapper.utils.DateUtils;
import com.github.btheu.table.mapper.utils.NumberUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author btheu
 *
 */
@Slf4j
public class PoiUtils2 {

    public static final DecimalFormat DF = new DecimalFormat("#");
    public static final NumberFormat NF = NumberFormat.getInstance(Locale.FRENCH);

    public static Date getDateValue(Cell cell, String defaultValue, String format) {
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return DateUtils.parse(defaultValue, format);
        }
        if (isAnyType(cell, Cell.CELL_TYPE_STRING, Cell.CELL_TYPE_FORMULA)) {
            return DateUtils.parse(getValueString(cell).trim(), format, defaultValue);
        } else {
            return cell.getDateCellValue();
        }

    }

    public static String toString(Cell cell) {

        StringBuilder sb = new StringBuilder();

        if (cell == null) {
            sb.append("null cell");
        } else {
            sb.append(cell.getSheet().getSheetName());

            sb.append(" [");
            sb.append(cell.getRowIndex() + 1);
            sb.append(",");
            sb.append(cell.getColumnIndex() + 1);
            sb.append("]");
            sb.append("(");
            sb.append(getValueString(cell));
            sb.append(")");
        }

        return sb.toString();
    }

    public static Integer getIntValue(Cell cell, String defaultValue) {
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return Integer.parseInt(defaultValue);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return (int) cell.getNumericCellValue();
        }

        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            String stringCellValue = cell.getStringCellValue().trim();

            return NumberUtils.parseInt(stringCellValue, defaultValue);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return (int) cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator().evaluate(cell)
                    .getNumberValue();
        }
        return null;
    }

    public static Long getLongValue(Cell cell, String defaultValue) {
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return Long.parseLong(defaultValue);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return Math.round(cell.getNumericCellValue());
        }

        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            String stringCellValue = cell.getStringCellValue().trim();

            return NumberUtils.parseLong(stringCellValue, defaultValue);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return (long) cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator().evaluate(cell)
                    .getNumberValue();
        }
        return null;
    }

    public static Double getDoubleValue(Cell cell, String defaultValue) {
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return Double.parseDouble(defaultValue);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue();
        }

        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            String stringCellValue = cell.getStringCellValue().trim();

            return NumberUtils.parseDouble(stringCellValue, defaultValue);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator().evaluate(cell)
                    .getNumberValue();
        }
        return null;
    }

    public static BigDecimal getBigDecimalValue(Cell cell, String defaultValue) {
        if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
            return new BigDecimal(defaultValue);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return new BigDecimal(cell.getNumericCellValue());
        }

        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            String stringCellValue = cell.getStringCellValue().trim();

            return NumberUtils.parseBigDecimal(stringCellValue, defaultValue);
        }

        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return new BigDecimal(cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator()
                    .evaluate(cell).getNumberValue());
        }
        return null;
    }

    public static boolean isAnyType(Cell cell, int... types) {
        for (int i : types) {
            if (cell.getCellType() == i) {
                return true;
            }
        }
        return false;
    }

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

}
