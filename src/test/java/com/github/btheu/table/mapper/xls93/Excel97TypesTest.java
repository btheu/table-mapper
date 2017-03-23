package com.github.btheu.table.mapper.xls93;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.github.btheu.table.mapper.TableMapper;
import com.github.btheu.table.mapper.xls93.data.MonTableauSheetFeuil4;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author theunissenb
 *
 */
@Slf4j
public class Excel97TypesTest {

    @Test
    public void dateTest1() {

        String classeur = "/Classeur97-2003.xls";
        log.info("Fichier : {} ", classeur);

        InputStream inputStream = Excel97TypesTest.class.getResourceAsStream(classeur);

        List<MonTableauSheetFeuil4> parseExcel = TableMapper.parseExcel(inputStream, MonTableauSheetFeuil4.class);

        TestCase.assertNotNull(parseExcel);
        TestCase.assertEquals(false, parseExcel.isEmpty());
        TestCase.assertEquals(6, parseExcel.size());
    }

}
