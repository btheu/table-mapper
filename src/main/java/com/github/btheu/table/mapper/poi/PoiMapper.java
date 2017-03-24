package com.github.btheu.table.mapper.poi;

import java.lang.reflect.Field;

import org.apache.poi.ss.usermodel.Cell;

import com.github.btheu.table.mapper.CellType;
import com.github.btheu.table.mapper.Column;
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
				if(entry.match(headerCell.getStringCellValue())){
					setValue(entry.getField(), target, valueCell);
					break;
				}
			}
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static <T> void setValue(Field field, final T target, final Cell valueCell)
            throws IllegalArgumentException, IllegalAccessException {

        if (valueCell == null) {
            ReflectionUtils.setValue(target, field, null);
            return;
        }

        Column annotation = field.getAnnotation(Column.class);

        CellType type = annotation.type();
        try {
            switch (type) {
            case DATE:
                ReflectionUtils.setValue(target, field, PoiUtils.getDateValue(valueCell, annotation.format()));
                break;
            case INT:
                ReflectionUtils.setValue(target, field, PoiUtils.getIntValue(valueCell));
                break;
            case LONG:
                ReflectionUtils.setValue(target, field, PoiUtils.getLongValue(valueCell));
                break;
            case DOUBLE:
                ReflectionUtils.setValue(target, field, PoiUtils.getDoubleValue(valueCell));
                break;
            case BIG_DECIMAL:
                ReflectionUtils.setValue(target, field, PoiUtils.getBigDecimalValue(valueCell));
                break;
            case STRING:
                ReflectionUtils.setValue(target, field, PoiUtils.getValueString(valueCell));
                break;
            default:
                throw new RuntimeException("Not handled: " + type.name());

            }
        } catch (NumberFormatException e) {
            log.error(valueCell.getSheet().getSheetName() + " " + valueCell.getColumnIndex() + ":"
                    + valueCell.getRowIndex() + " => '" + PoiUtils.getValueString(valueCell) + "', type: "
                    + valueCell.getCellType());
            throw new RuntimeException(e.getMessage(), e);

        } catch (IllegalStateException e) {
            log.error(valueCell.getSheet().getSheetName() + " " + valueCell.getColumnIndex() + ":"
                    + valueCell.getRowIndex() + " => '" + PoiUtils.getValueString(valueCell) + "', type: "
                    + valueCell.getCellType());
            throw new IllegalStateException(e);
        }

    }

}
