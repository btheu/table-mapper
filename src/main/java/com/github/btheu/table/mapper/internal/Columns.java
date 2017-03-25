package com.github.btheu.table.mapper.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.github.btheu.table.mapper.CellType;
import com.github.btheu.table.mapper.Column;

import lombok.Data;

/**
 * 
 * @author btheu
 *
 */
@Data
public class Columns {

	protected List<Entry> columns = new ArrayList<Entry>();

	public void add(Field field) {
		Column column = field.getAnnotation(Column.class);
        if (column != null) {
        	columns.add(new Entry(field, column));
        }
	}
	
	/**
	 * A column mapping definition
	 * 
	 * @author btheu
	 *
	 */
	@Data
	public static class Entry {

		protected String name;

		protected String defaultValue;
		
		protected String format;
		
		protected Pattern namePattern;

		protected boolean regex;
		
		/**
		 * The field, the column is mapped to
		 */
		protected Field field;

		protected CellType type;

		public Entry(Field field, Column column) {
			this.field = field;
			name = column.value();
			type = column.type();
			format = column.format();
			
			regex = column.regex();
			if (regex) {
				namePattern = Pattern.compile(name);
			}
			
			defaultValue = column.defaultValue();
			if(defaultValue.equals(Column.NOT_OPTIONAL)){
				defaultValue = null;
			}
		}

		/**
		 * 
		 * @param columnName
		 * @return true if columnName match the column name or pattern
		 */
		public boolean match(String columnName) {
			if(regex){
				boolean matches = namePattern.matcher(columnName).matches();
				
				return matches;
			}else{
				return name.equalsIgnoreCase(columnName);
			}
		}

	}

	/**
	 * 
	 * @param columnName
	 * @return true if columName match at least one column
	 */
	public boolean match(String columnName) {
		for (Entry entry : columns) {
			if(entry.match(columnName)){
				return true;
			}
		}
		return false;
	}



}
