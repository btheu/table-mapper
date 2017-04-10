package com.github.btheu.table.mapper.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.github.btheu.table.mapper.CellType;
import com.github.btheu.table.mapper.Column;
import com.github.btheu.table.mapper.Id;

import lombok.Data;

/**
 * 
 * @author btheu
 * 
 */
// TODO BTHEU rename Columns to TableData
// TODO BTHEU rename Entry to ColumnData
@Data
public class Columns {

    protected List<Entry> primaries = new ArrayList<Entry>();

    protected List<Entry> columns = new ArrayList<Entry>();

    protected Class<?> dataClass;

    public Columns(Class<?> clazz) {
        this.dataClass = clazz;
    }

    public void add(Field field) {

        Id aId = field.getAnnotation(Id.class);

        Column aColumn = field.getAnnotation(Column.class);
        if (aColumn != null) {
            Entry entry = new Entry(field, aColumn);

            if (aId != null) {
                entry.setPrimaryKey(true);
                primaries.add(entry);
            }

            columns.add(entry);
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

        protected boolean primaryKey;

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
            if (defaultValue.equals(Column.NOT_OPTIONAL)) {
                defaultValue = null;
            }
        }

        /**
         * 
         * @param columnName
         * @return true if columnName match the column name or pattern
         */
        public boolean match(String columnName) {
            if (regex) {
                boolean matches = namePattern.matcher(columnName).matches();

                return matches;
            } else {
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
            if (entry.match(columnName)) {
                return true;
            }
        }
        return false;
    }

}
