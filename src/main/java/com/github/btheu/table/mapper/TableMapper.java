package com.github.btheu.table.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.github.btheu.table.mapper.csv.CSVTableParser;
import com.github.btheu.table.mapper.poi.PoiTableParser;
import com.github.btheu.table.mapper.poi.PoiTableWriter;

import lombok.extern.slf4j.Slf4j;

/**
 * Public class TableMapper
 * 
 * @author btheu
 *
 */
@Slf4j
public class TableMapper {

    public static Workbook openExcelDocument(InputStream inputStream) {
        try {
            Workbook document = WorkbookFactory.create(inputStream);

            return document;
        } catch (InvalidFormatException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static <T> List<T> parseExcel(Workbook document, Class<T> targetClass) {
        return PoiTableParser.parse(document, targetClass);
    }

    /**
     * <p>
     * Parse specified sheets for data
     * <p>
     * {@link Sheet} and {@link SheetAll} annotations will be ignored
     * 
     * @param inputStream
     *            the document
     * @param targetClass
     *            the pojo target class
     * @param sheets
     *            names of sheets from which data will be parsed
     * @return Parsed data filled in pojos
     */
    public static <T> List<T> parseExcel(InputStream inputStream, Class<T> targetClass, String... sheets) {

        Workbook document = openExcelDocument(inputStream);

        return PoiTableParser.parse(document, targetClass, sheets);
    }

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
        Workbook document = openExcelDocument(inputStream);

        return parseExcel(document, targetClass);
    }

    public static <T> void writeExcel(InputStream inputStream, List<T> rows, OutputStream outputStream) {
        try {
            Workbook document = WorkbookFactory.create(inputStream);

            writeExcel(document, rows, outputStream);

        } catch (InvalidFormatException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 
     * @param document
     *            the excel document
     * @param rows
     *            pojos filled to write into the docuement
     * @param outputStream
     */
    public static <T> void writeExcel(Workbook document, List<T> rows, OutputStream outputStream) {
        try {
            PoiTableWriter.write(document, rows);

            document.write(outputStream);

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
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
