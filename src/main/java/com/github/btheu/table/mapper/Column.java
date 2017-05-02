package com.github.btheu.table.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author btheu
 *
 */
@Inherited
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    public static final String NOT_OPTIONAL = "__NOT_OPTIONAL__";

    /**
     * Le nom de la colonne dans le tableau fourni en entrée
     * 
     * @return Le Nom de la colonne correspondante.
     */
    String value();

    /**
     * <p>
     * (optional) If missing or converts fails, this value will be used
     * <p>
     * this column is so mark as optional
     * 
     * @return the default value in case of missing or convert fail
     */
    String defaultValue() default NOT_OPTIONAL;

    /**
     * <p>
     * (optional) Define the data type for conversion
     * 
     * @return the value type expected
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
     * (optional) Indicate that the value is a regex
     * 
     * @return true if value() must be read as a regex
     */
    boolean regex() default false;

    /**
     * <p>
     * (optional) Indicate that the value is a optional
     * 
     * @return true if null value can be ignored when reading table
     */
    boolean optional() default true;
}
