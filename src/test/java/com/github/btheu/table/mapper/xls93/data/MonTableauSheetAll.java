package com.github.btheu.table.mapper.xls93.data;

import java.math.BigDecimal;
import java.util.Date;

import com.github.btheu.table.mapper.CellType;
import com.github.btheu.table.mapper.Column;
import com.github.btheu.table.mapper.SheetAll;

import lombok.Data;

@Data
@SheetAll
public class MonTableauSheetAll {

    @Column(value = "Numéro de rue", type = CellType.INT)
    private int numero;

    @Column("La rue")
    private String rue;

    @Column(value = "La ville", defaultValue = "Paris")
    private String ville;

    @Column(value = "Date création", type = CellType.DATE, format = "yyyy/MM/dd")
    private Date création;

    @Column(value = "Coût", type = CellType.BIG_DECIMAL, defaultValue = "1001")
    private BigDecimal cout;

}
