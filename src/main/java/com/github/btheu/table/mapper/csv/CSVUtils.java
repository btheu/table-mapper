package com.github.btheu.table.mapper.csv;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVUtils {

    protected static final char DEFAULT_SEPARATOR = ';';

    public static void writeLine(Writer w, List<String> values) {
        writeLine(w, values, DEFAULT_SEPARATOR, ' ');
    }

    public static void writeLine(Writer w, List<String> values, char separators) {
        writeLine(w, values, separators, ' ');
    }

    // https://tools.ietf.org/html/rfc4180
    private static String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }

    public static void writeLine(Writer w, List<String> values, char separators, char customQuote) {

        log.debug("write line {}", values);
        
        try {
            boolean first = true;

            // default customQuote is empty

            if (separators == ' ') {
                separators = DEFAULT_SEPARATOR;
            }

            StringBuilder sb = new StringBuilder();
            for (String value : values) {
                if (!first) {
                    sb.append(separators);
                }
                if (customQuote == ' ') {
                    sb.append(followCVSformat(value));
                } else {
                    sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
                }

                first = false;
            }
            sb.append("\n");
            w.append(sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

}