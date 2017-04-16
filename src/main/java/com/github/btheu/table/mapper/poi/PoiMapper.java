package com.github.btheu.table.mapper.poi;

import java.lang.reflect.Field;

import org.apache.poi.ss.usermodel.Cell;

import com.github.btheu.table.mapper.CellType;
import com.github.btheu.table.mapper.internal.TableData;
import com.github.btheu.table.mapper.internal.TableData.ColumnData;
import com.github.btheu.table.mapper.utils.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author btheu
 *
 */
@Slf4j
public class PoiMapper {

    public static <T> void map(final TableData columns, final T target, final Cell headerCell, final Cell valueCell) {

        try {
            for (ColumnData column : columns.getColumns()) {
                if (column.match(headerCell.getStringCellValue())) {
                    setValue(column, target, valueCell);
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static <T> void setValue(ColumnData column, final T target, final Cell valueCell)
            throws IllegalArgumentException, IllegalAccessException {

        Object value = safeEvaluate(column, valueCell);

        Field field = column.getField();

        ReflectionUtils.setValue(target, field, value);
    }

    /**
     * Handle type conversion and default value.
     * 
     * @param column
     * @param valueCell
     * @return
     */
    private static Object safeEvaluate(ColumnData column, Cell valueCell) {

        Object value;

        String defaultValue = column.getDefaultValue();

        if (log.isDebugEnabled()) {
            log.debug("{}\t{} {}", PoiUtils.toString(valueCell), column.getType().name(), column.getName());
        }

        CellType targetType = column.getType();
        switch (targetType) {
        case DATE:
            value = PoiUtils.getDateValue(valueCell, defaultValue, column.getFormat());
            break;
        case INT:
            value = PoiUtils.getIntValue(valueCell, defaultValue);
            break;
        case LONG:
            value = PoiUtils.getLongValue(valueCell, defaultValue);
            break;
        case DOUBLE:
            value = PoiUtils.getDoubleValue(valueCell, defaultValue);
            break;
        case BIG_DECIMAL:
            value = PoiUtils.getBigDecimalValue(valueCell, defaultValue);
            break;
        case STRING:
            value = PoiUtils.getValueString(valueCell);
            break;
        default:
            throw new RuntimeException("Not handled: " + targetType.name());
        }

        return value;
    }

}
