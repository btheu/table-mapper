package com.github.btheu.table.mapper.csv;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.github.btheu.table.mapper.internal.Columns;
import com.github.btheu.table.mapper.internal.Columns.Entry;
import com.github.btheu.table.mapper.internal.Parser;

public abstract class CSVWriter {

    private static final char CSV_SEPARATORS = ';';

    public static <T> void write(List<T> table, OutputStream os) {
        write(table, os, "ISO-8859-1");
    }

    public static <T> void write(List<T> table, OutputStream os, String charsetName) {

        try {
            Writer writer = new OutputStreamWriter(os, charsetName);

            if (!table.isEmpty()) {

                Columns columns = Parser.extractColumns(table.get(0).getClass());

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

    private static void writeHeader(Columns columns, Writer writer) {
        List<String> values = new ArrayList<String>();

        for (Entry entry : columns.getColumns()) {
            values.add(entry.getName());
        }

        CSVUtils.writeLine(writer, values, CSV_SEPARATORS, '"');
    }

    private static <T> void writeItem(Columns columns, T item, Writer writer) {
        List<String> values = new ArrayList<String>();

        try {
            for (Entry entry : columns.getColumns()) {

                Field field = entry.getField();

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
