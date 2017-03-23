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

    /**
     * (optional)
     * 
     */
    CellType type() default CellType.STRING;

    /**
     * (optional) Add information for conversion. Supported types:
     * <ul>
     * <li><strong>date:</strong> Date pattern compliant with
     * java.text.SimpleDateFormat
     * </ul>
     * 
     * @return the format
     */
    String format() default "";

    /**
     * <p>
     * Indicate that the value is a regex
     * 
     * @return true if value() must be read as a regex
     */
    boolean regex() default false;
}
