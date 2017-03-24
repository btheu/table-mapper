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
	 * Handle type conversion and default value .
	 * 
	 * @param entry
	 * @param valueCell
	 * @return
	 */
	private static Object safeEvaluate(Entry entry, Cell valueCell) {

		if (valueCell == null) {
			return null;
		}

		Object value;

		CellType type = entry.getType();
		try {
			switch (type) {
			case DATE:
				value = PoiUtils.getDateValue(valueCell, entry.getFormat());
				break;
			case INT:
				value = PoiUtils.getIntValue(valueCell);
				break;
			case LONG:
				value = PoiUtils.getLongValue(valueCell);
				break;
			case DOUBLE:
				value = PoiUtils.getDoubleValue(valueCell);
				break;
			case BIG_DECIMAL:
				value = PoiUtils.getBigDecimalValue(valueCell);
				break;
			case STRING:
				value = PoiUtils.getValueString(valueCell);
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

		return value;
	}

}
