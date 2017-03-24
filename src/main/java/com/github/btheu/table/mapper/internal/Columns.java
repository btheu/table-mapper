package com.github.btheu.table.mapper.internal;

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

	public void add(Column column) {
		columns.add(new Entry(column));
	}

	public static class Entry {

		protected String name;

		protected Pattern namePattern;

		protected boolean regex;

		public Entry(Column column) {
			name = column.value();
			regex = column.regex();

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
