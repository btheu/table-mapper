package com.github.btheu.table.mapper.internal;

import java.lang.reflect.Field;
import java.util.List;

import com.github.btheu.table.mapper.utils.ReflectionUtils;

public class TableParser {

    public static <T> Columns parseClass(Class<T> class1) {
        Columns columns = new Columns(class1);

        List<Field> allFields = ReflectionUtils.getAllFields(class1);
        for (Field field : allFields) {
            columns.add(field);
        }

        return columns;
    }

}
