package com.github.btheu.table.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.github.btheu.table.mapper.csv.CSVTableParser;
import com.github.btheu.table.mapper.poi.PoiTableParser;

import lombok.extern.slf4j.Slf4j;

/**
 * Public class TableMapper
 * 
 * @author btheu
 *
 */
@Slf4j
public class TableMapper {

    /**
     * Parse un tableau Excel
     * 
     * @param inputStream
     *            Le flux du fichier Excel
     * @param targetClass
     *            La classe du POJO correspondant à une ligne du tableau Excel
     * @return La liste des objets parsés dans le tableau Excel
     */
    public static <T> List<T> parseExcel(InputStream inputStream, Class<T> targetClass) {
        try {
            Workbook wb = WorkbookFactory.create(inputStream);

            List<T> parse = PoiTableParser.parse(wb, targetClass);

            wb.close();

            return parse;
        } catch (InvalidFormatException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * Parse un fichier CSV
     * 
     * @param inputStream
     *            Le flux du fichier CSV
     * @param targetClass
     *            La classe du POJO correspondant à une ligne du tableau CSV
     * @return La liste des objets parsés dans le tableau CSV
     */
    public static <T> List<T> parseCSV(InputStream inputStream, Class<T> targetClass) {
        return CSVTableParser.parse(inputStream, targetClass);
    }

}
