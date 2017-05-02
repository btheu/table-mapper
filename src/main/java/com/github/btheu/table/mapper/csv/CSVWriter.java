package com.github.btheu.table.mapper.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.github.btheu.table.mapper.internal.TableData;
import com.github.btheu.table.mapper.internal.TableData.ColumnData;
import com.github.btheu.table.mapper.internal.TableParser;

public abstract class CSVWriter {

    private static final char CSV_SEPARATORS = ';';

    public static <T> void write(List<T> table, OutputStream os) {
        write(table, os, "ISO-8859-1");
    }

    public static <T> void write(List<T> table, OutputStream os, String charsetName) {

        try {
            Writer writer = new OutputStreamWriter(os, charsetName);

            if (!table.isEmpty()) {

                TableData columns = TableParser.parseClass(table.get(0).getClass());

                writeHeader(columns, writer);

                for (T item : table) {
                    writeItem(columns, item, writer);
                }
            }

            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static void writeHeader(TableData columns, Writer writer) {
        List<String> values = new ArrayList<String>();

        for (ColumnData column : columns.getColumns()) {
            values.add(column.getName());
        }

        CSVUtils.writeLine(writer, values, CSV_SEPARATORS, '"');
    }

    private static <T> void writeItem(TableData columns, T item, Writer writer) {
        List<String> values = new ArrayList<String>();

        try {
            for (ColumnData column : columns.getColumns()) {

                Field field = column.getField();

                Object value = field.get(item);
                if (value == null) {
                    values.add("NULL");
                } else {
                    values.add(value.toString());
                }
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        CSVUtils.writeLine(writer, values, CSV_SEPARATORS, '"');
    }

}
