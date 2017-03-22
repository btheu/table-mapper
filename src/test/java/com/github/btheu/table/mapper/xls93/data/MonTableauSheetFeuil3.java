package com.github.btheu.table.mapper.xls93.data;

import java.math.BigDecimal;
import java.util.Date;

import com.github.btheu.table.mapper.CellType;
import com.github.btheu.table.mapper.Column;
import com.github.btheu.table.mapper.Sheet;

import lombok.Data;

@Data
@Sheet("Feuil3")
public class MonTableauSheetFeuil3 {

    @Column(value = "Numéro de rue", type = CellType.INT)
    private int numero;

    @Column("La rue")
    private String rue;

    @Column("La ville")
    private String ville;

    @Column(value = "Date création", type = CellType.DATE)
    private Date création;

    @Column(value = "Coût", type = CellType.BIG_DECIMAL)
    private BigDecimal cout;

}
