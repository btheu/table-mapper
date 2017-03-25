package com.github.btheu.table.mapper.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {

	public static Date parse(String valueString, String format) {
		SimpleDateFormat SFD = new SimpleDateFormat(format);
		return parseWithRuntimeException(SFD, valueString);
	}

	public static Date parse(String valueString, String format, String defaultValue) {
		SimpleDateFormat SFD = new SimpleDateFormat(format);
		try {
			return SFD.parse(valueString);
		} catch (ParseException e) {
			log.debug("Cant parse date '" + valueString + "' with format: '" + format + "'. Trying with default value: "
					+ defaultValue);
			return parseWithRuntimeException(SFD, defaultValue);
		}
	}

	private static Date parseWithRuntimeException(SimpleDateFormat sfd, String valueString) {
		try {
			return sfd.parse(valueString);
		} catch (ParseException e) {
			throw new RuntimeException(
					"Cant parse date '" + valueString + "' with format: '" + sfd.getNumberFormat() + "'", e);
		}
	}

}
