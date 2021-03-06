package com.github.btheu.table.mapper.xls93.data;

import java.math.BigDecimal;
import java.util.Date;

import com.github.btheu.table.mapper.CellType;
import com.github.btheu.table.mapper.Column;
import com.github.btheu.table.mapper.Id;

import lombok.Data;

@Data
// Pas de @Sheet => Chargement uniquement sur la premiere feuille
public class MonTableauSheet1 {

    @Id
    @Column(value = "Numéro de rue", type = CellType.INT)
    private int numero;

    @Column("La rue")
    private String rue;

    @Column("La ville")
    private String ville;

    @Column(value = "Date création", type = CellType.DATE)
    private Date creation;

    @Column(value = "Coût", type = CellType.BIG_DECIMAL)
    private BigDecimal cout;

}
