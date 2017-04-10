package com.github.btheu.table.mapper.xls93;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.github.btheu.table.mapper.TableMapper;
import com.github.btheu.table.mapper.xls93.data.MonTableauSheet1;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author btheu
 *
 */
@Slf4j
public class Excel97TableWriterTest {

    @Test
    public void testSheet1() {

        String classeur = "/Classeur97-2003.xls";
        log.info("Fichier : {} ", classeur);

        InputStream inputStream = Excel97TableWriterTest.class.getResourceAsStream(classeur);

        List<MonTableauSheet1> parseExcel = TableMapper.parseExcel(inputStream, MonTableauSheet1.class);

        inputStream = Excel97TableWriterTest.class.getResourceAsStream(classeur);

        TableMapper.writeExcel(inputStream, parseExcel);

    }

}
