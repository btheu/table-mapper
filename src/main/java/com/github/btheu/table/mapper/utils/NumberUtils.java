package com.github.btheu.table.mapper.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NumberUtils {

	public static int parseInt(String value, String defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			log.debug("Cant parse int '" + value + "'. Trying with default value: " + defaultValue);
			return Integer.parseInt(defaultValue);
		}
	}

}
