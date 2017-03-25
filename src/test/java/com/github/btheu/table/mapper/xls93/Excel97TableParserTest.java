package com.github.btheu.table.mapper.xls93;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.github.btheu.table.mapper.TableMapper;
import com.github.btheu.table.mapper.xls93.data.MonTableauSheet1;
import com.github.btheu.table.mapper.xls93.data.MonTableauSheetAll;
import com.github.btheu.table.mapper.xls93.data.MonTableauSheetFeuil2et3;
import com.github.btheu.table.mapper.xls93.data.MonTableauSheetFeuil3;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author btheu
 *
 */
@Slf4j
public class Excel97TableParserTest {

    @Test
    public void testSheet1() {

        String classeur = "/Classeur97-2003.xls";
        log.info("Fichier : {} ", classeur);

        InputStream inputStream = Excel97TableParserTest.class.getResourceAsStream(classeur);

        List<MonTableauSheet1> parseExcel = TableMapper.parseExcel(inputStream, MonTableauSheet1.class);

        TestCase.assertNotNull(parseExcel);
        TestCase.assertEquals(false, parseExcel.isEmpty());
        TestCase.assertEquals(6, parseExcel.size());

    }

    @Test
    public void testSheetAll() {

        String classeur = "/Classeur97-2003.xls";
        log.info("Fichier : {} ", classeur);

        InputStream inputStream = Excel97TableParserTest.class.getResourceAsStream(classeur);

        List<MonTableauSheetAll> parseExcel = TableMapper.parseExcel(inputStream, MonTableauSheetAll.class);

        TestCase.assertNotNull(parseExcel);
        TestCase.assertEquals(false, parseExcel.isEmpty());
        TestCase.assertEquals(24, parseExcel.size());

        for (MonTableauSheetAll monTableauSheetAll : parseExcel) {
            log.info("{}", monTableauSheetAll);
        }

    }

    @Test
    public void testSheetFeuil3() {

        String classeur = "/Classeur97-2003.xls";
        log.info("Fichier : {} ", classeur);

        InputStream inputStream = Excel97TableParserTest.class.getResourceAsStream(classeur);

        List<MonTableauSheetFeuil3> parseExcel = TableMapper.parseExcel(inputStream, MonTableauSheetFeuil3.class);

        TestCase.assertNotNull(parseExcel);
        TestCase.assertEquals(false, parseExcel.isEmpty());
        TestCase.assertEquals(6, parseExcel.size());

    }

    @Test
    public void testSheetFeuil2et3() {

        String classeur = "/Classeur97-2003.xls";
        log.info("Fichier : {} ", classeur);

        InputStream inputStream = Excel97TableParserTest.class.getResourceAsStream(classeur);

        List<MonTableauSheetFeuil2et3> parseExcel = TableMapper.parseExcel(inputStream, MonTableauSheetFeuil2et3.class);

        TestCase.assertNotNull(parseExcel);
        TestCase.assertEquals(false, parseExcel.isEmpty());
        TestCase.assertEquals(12, parseExcel.size());

    }

}
