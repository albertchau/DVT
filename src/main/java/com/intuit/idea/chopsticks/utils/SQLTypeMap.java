package com.intuit.idea.chopsticks.utils;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/15/15
 * ************************************
 */

import java.sql.Types;

/**
 * Converts database types to Java class types.
 */
public class SQLTypeMap {
    /**
     * Translates a data getType from an integer (java.sql.Types value) to a string
     * that represents the corresponding class.
     *
     * @param type The java.sql.Types value to toClass to its corresponding class.
     * @return The class that corresponds to the given java.sql.Types
     * value, or Object.class if the getType has no known mapping.
     */
    public static Class<? extends Comparable> toClass(int type) {
        Class<? extends Comparable> result = null;

        switch (type) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                result = String.class;
                break;

            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.ROWID:
                result = java.math.BigDecimal.class;
                break;

            case Types.BIT:
            case Types.BOOLEAN:
                result = Boolean.class;
                break;

            case Types.TINYINT:
                result = Byte.class;
                break;

            case Types.SMALLINT:
                result = Short.class;
                break;

            case Types.INTEGER:
                result = Integer.class;
                break;

            case Types.BIGINT:
                result = Long.class;
                break;

            case Types.REAL:
            case Types.FLOAT:
                result = Float.class;
                break;

            case Types.DOUBLE:
                result = Double.class;
                break;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
//                result = Byte[].class; todo
                break;

            case Types.DATE:
                result = java.sql.Date.class;
                break;

            case Types.TIME:
            case Types.TIME_WITH_TIMEZONE:
                result = java.sql.Time.class;
                break;

            case Types.TIMESTAMP:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                result = java.sql.Timestamp.class;
                break;

            case Types.NULL:
            case Types.OTHER:
            case Types.JAVA_OBJECT:
            case Types.DISTINCT:
            case Types.STRUCT:
            case Types.BLOB:
            case Types.CLOB:
            case Types.REF:
            case Types.DATALINK:
            case Types.NCLOB:
            case Types.SQLXML:
            case Types.REF_CURSOR:
//                result = Object.class; todo
                break;
            case Types.ARRAY:
//                result = Object[].class; todo
                break;
        }

        return result;
    }
}