package com.github.btheu.table.mapper.xls93;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
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
    public void testSheet1() throws FileNotFoundException {

        String classeur = "/Classeur97-2003.xls";
        log.info("Fichier : {} ", classeur);

        InputStream inputStream = Excel97TableWriterTest.class.getResourceAsStream(classeur);

        List<MonTableauSheet1> parseExcel = TableMapper.parseExcel(inputStream, MonTableauSheet1.class);

        inputStream = Excel97TableWriterTest.class.getResourceAsStream(classeur);

        for (MonTableauSheet1 monTableauSheet1 : parseExcel) {
            log.info("{}", monTableauSheet1.getNumero());
            log.info("{}", monTableauSheet1.getCout());
            if (monTableauSheet1.getNumero() == 3) {
                monTableauSheet1.setRue("Ma rue");
                monTableauSheet1.setVille("Bordeaux");
                monTableauSheet1.setCreation(new Date());
                monTableauSheet1.setCout(new BigDecimal("1235644668745486754"));
            }
        }

        TableMapper.writeExcel(inputStream, parseExcel, new FileOutputStream(new File("out.xls")));

    }

}
