package com.viettel.kpi.common.utils;

import java.math.BigDecimal;

/**
 *
 * @author os_linhlh2
 */
public class DBUtil {
    public static Long getLong(Object obj){
        Long value = null;

        if(obj!=null) {
            value = Long.valueOf(obj.toString());
        }

        return value;
    }

    public static Integer getInteger(Object obj){
        Integer value = null;

        if(obj!=null) {
            value = Integer.valueOf(obj.toString());
        }

        return value;
    }

    public static Float getFloat(Object obj){
        Float value = null;

        if(obj!=null) {
            value = Float.valueOf(obj.toString());
        }

        return value;
    }
    
//    public static BigDecimal getBigDecimal(Object obj){
//        BigDecimal value = null;
//
//        if(obj!=null) {
//            value = BigDecimal.valueOf(Double.valueOf(obj.toString()));
//        }
//
//        return value;
//    }

    public static Double getDouble(Object obj){
        Double value = null;

        if(obj!=null) {
            value = Double.valueOf(obj.toString());
        }

        return value;
    }

    public static Short getShort(Object obj){
        Short value = null;

        if(obj!=null) {
            value = Short.valueOf(obj.toString());
        }

        return value;
    }

    public static String getString(Object obj){
        String value = null;

        if(obj!=null) {
            value = obj.toString();
        }

        return value;
    }
}
