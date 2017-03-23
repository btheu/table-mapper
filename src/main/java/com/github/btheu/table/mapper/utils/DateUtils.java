package com.github.btheu.table.mapper.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date parse(String valueString, String format) {
        try {
            SimpleDateFormat SFD = new SimpleDateFormat(format);
            return SFD.parse(valueString);
        } catch (ParseException e) {
            throw new RuntimeException("Cant parse date '" + valueString + "' with format: '" + format + "'", e);
        }
    }

}
