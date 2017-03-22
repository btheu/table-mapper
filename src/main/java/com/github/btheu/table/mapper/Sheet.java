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
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Sheet {

    /**
     * Le nom de la ou des feuilles dans le cas d'excel ou open office.
     * 
     * @return les noms des feuilles de travail
     */
    String[] value();

    /**
     * Le nom de la ou des feuilles à exclure dans le cas d'excel ou open office.
     * 
     * @return les noms des feuilles de travail à exclure
     */
    // TODO exclude
    // String[] exclude();

}
