package com.github.btheu.table.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author theunissenb
 *
 */
@Inherited
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * Le nom de la colonne dans le tableau fourni en entrée
     * 
     * @return Le Nom de la colonne correspondante.
     */
    String value();

    CellType type() default CellType.STRING;

}
