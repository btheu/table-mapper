package com.github.btheu.table.mapper.poi;

import java.lang.reflect.Field;

import org.apache.poi.ss.usermodel.Cell;

import com.github.btheu.table.mapper.CellType;
import com.github.btheu.table.mapper.internal.Columns;
import com.github.btheu.table.mapper.internal.Columns.Entry;
import com.github.btheu.table.mapper.utils.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author btheu
 *
 */
@Slf4j
public class PoiMapper {

    public static <T> void map(final Columns columns, final T target, final Cell headerCell, final Cell valueCell) {

        try {
            for (Entry entry : columns.getColumns()) {
                if (entry.match(headerCell.getStringCellValue())) {
                    setValue(entry, target, valueCell);
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static <T> void setValue(Entry entry, final T target, final Cell valueCell)
            throws IllegalArgumentException, IllegalAccessException {

        Object value = safeEvaluate(entry, valueCell);

        Field field = entry.getField();

        ReflectionUtils.setValue(target, field, value);
    }

    /**
     * Handle type conversion and default value.
     * 
     * @param entry
     * @param valueCell
     * @return
     */
    private static Object safeEvaluate(Entry entry, Cell valueCell) {

        Object value;

        String defaultValue = entry.getDefaultValue();

        log.debug("{}\t{} {}", PoiUtils2.toString(valueCell), entry.getType().name(), entry.getName());

        CellType targetType = entry.getType();
        switch (targetType) {
        case DATE:
            value = PoiUtils2.getDateValue(valueCell, defaultValue, entry.getFormat());
            break;
        case INT:
            value = PoiUtils2.getIntValue(valueCell, defaultValue);
            break;
        case LONG:
            value = PoiUtils2.getLongValue(valueCell, defaultValue);
            break;
        case DOUBLE:
            value = PoiUtils2.getDoubleValue(valueCell, defaultValue);
            break;
        case BIG_DECIMAL:
            value = PoiUtils2.getBigDecimalValue(valueCell, defaultValue);
            break;
        case STRING:
            value = PoiUtils2.getValueString(valueCell);
            break;
        default:
            throw new RuntimeException("Not handled: " + targetType.name());
        }

        return value;
    }

}
