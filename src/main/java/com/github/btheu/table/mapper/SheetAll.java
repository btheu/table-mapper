package com.github.btheu.table.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Precise au parser d'agreger les tableaux de toutes les feuilles excel tant que le format des colonnes est correcte
 * 
 * @author theunissenb
 *
 */
@Inherited
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SheetAll {

}
