package com.github.btheu.table.mapper.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author theunissenb
 *
 */
public class ReflectionUtils {

    public static List<Field> getAllFields(Class<?> clazz) {

        List<Field> res = new ArrayList<Field>();

        Class<?> index = clazz;
        while (index != Object.class) {
            res.addAll(Arrays.asList(index.getDeclaredFields()));

            index = index.getSuperclass();

        }

        return res;
    }

    public static void setValue(Object target, Field field, Object value)
            throws IllegalArgumentException, IllegalAccessException {

        field.setAccessible(true);

        field.set(target, value);
    }

}
