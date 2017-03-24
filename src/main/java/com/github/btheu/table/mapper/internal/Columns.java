package com.github.btheu.table.mapper.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
		
		protected Pattern namePattern;

		/**
		 * The field, the column is mapped to
		 */
		protected Field field;
		
		protected boolean regex;


		public Entry(Field field, Column column) {
			this.field = field;
			name = column.value();
			regex = column.regex();

			defaultValue = column.defaultValue();
			
			if (regex) {
				namePattern = Pattern.compile(name);
			}
		}

		/**
		 * 
		 * @param columnName
		 * @return true if columnName match the column name or pattern
		 */
		public boolean match(String columnName) {
			if(regex){
				return namePattern.matcher(columnName).matches();
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
