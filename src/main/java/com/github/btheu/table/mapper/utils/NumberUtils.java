package com.github.btheu.table.mapper.utils;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NumberUtils {

    public static int parseInt(String value, String defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.debug("Cant parse Int '" + value + "'. Trying with default value: " + defaultValue);
            return Integer.parseInt(defaultValue);
        }
    }

    public static Long parseLong(String value, String defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.debug("Cant parse Long '" + value + "'. Trying with default value: " + defaultValue);
            return Long.parseLong(defaultValue);
        }
    }

    public static Double parseDouble(String value, String defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            log.debug("Cant parse Double '" + value + "'. Trying with default value: " + defaultValue);
            return Double.parseDouble(defaultValue);
        }
    }

    public static BigDecimal parseBigDecimal(String value, String defaultValue) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            if (defaultValue == null) {
                throw new RuntimeException("Cant parse BigDecimal '" + value + "'", e);
            } else {
                log.debug("Cant parse BigDecimal '" + value + "'. Trying with default value: " + defaultValue);
                return new BigDecimal(defaultValue);
            }
        }
    }

}
