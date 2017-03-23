package com.github.btheu.table.mapper.poi;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

import com.github.btheu.table.mapper.CellType;
import com.github.btheu.table.mapper.Column;
import com.github.btheu.table.mapper.utils.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author theunissenb
 *
 */
@Slf4j
public class PoiMapper {

    public static <T> void map(final T target, final Cell headerCell, final Cell valueCell) {

        try {
            List<Field> fields = ReflectionUtils.getAllFields(target.getClass());
            for (Field field : fields) {

                Column annotation = field.getAnnotation(Column.class);

                if (annotation != null && annotation.value().equalsIgnoreCase(headerCell.getStringCellValue())) {

                    setValue(field, target, valueCell);

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
