/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.getDataSql.common;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author os_NamNDH
 * @version 1.0
 * @since since_text
 */
public class CommonUtil {

    private static org.apache.log4j.Logger logger;

    public CommonUtil(org.apache.log4j.Logger logger) {
        CommonUtil.logger = logger;
    }

    public static List getDynaListObj(String className, ResultSet rs)
            throws Exception {
        Object obj;
        List<String> colNames = new ArrayList<String>();
        //get list column names from result set
        ResultSetMetaData metadata = rs.getMetaData();
        int colCount = metadata.getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            colNames.add(metadata.getColumnLabel(i).toLowerCase());
        }

        //retrieve list dynamic object into list of form
        ArrayList<Object> results = new ArrayList<Object>();
        Field f;
        while (rs.next()) {
            //cast object into specific class through class name
            obj = Class.forName(className).newInstance();

            for (String colName : colNames) {
                f = obj.getClass().getDeclaredField(colName);
                if (f != null) {
                    f.setAccessible(true);
                    f.set(obj, rs.getObject(colName) != null
                            ? castObj(rs.getObject(colName), f) : null);
                }
            }

            results.add(obj);
        }
        return results;
    }

    public static ArrayList getDynaListObjByAnnotation(String className, ResultSet rs)
            throws Exception {
        Object obj;
        List<String> colNames = new ArrayList<String>();
        //get list column names from result set
        ResultSetMetaData metadata = rs.getMetaData();
        int colCount = metadata.getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            colNames.add(metadata.getColumnLabel(i).toLowerCase());
        }

        Map<String, String> mapAnnotation = new HashMap<String, String>();
        Object tempObj = Class.forName(className).newInstance();
        for (Field field : tempObj.getClass().getDeclaredFields()) {
            Column col;
            field.setAccessible(true);
            if ((col = field.getAnnotation(Column.class)) != null) {
                mapAnnotation.put(col.columnName().toLowerCase().trim(), field.getName());
            }
        }

        //retrieve list dynamic object into list of form
        ArrayList<Object> results = new ArrayList<Object>();
        Field f;
        int i = 1;
        while (rs.next()) {
            //cast object into specific class through class name
            obj = Class.forName(className).newInstance();

            for (String colName : colNames) {
                String fieldName = mapAnnotation.get(colName);
                if (fieldName != null) {
                    f = obj.getClass().getDeclaredField(fieldName);
                    if (f != null) {
                        f.setAccessible(true);
                        Object rsValue;
                        if (f.getType().equals(java.util.Date.class)) {
                            rsValue = (Object) rs.getTimestamp(colName);
                        } else {
                            rsValue = (Object) rs.getObject(colName);
                        }
                        f.set(obj, castObj(rsValue, f));
                    }
                }
            }
            //System.out.println(className + "---" + i++);
            results.add(obj);
        }
        //System.out.println("fetched done!");
        return results;
    }

    private static Object castObj(Object obj, Field f) throws ParseException, SQLException {

        Object object = null;

        if (obj != null) {
            if (f.getType().equals(int.class)) {
                object = Integer.valueOf(obj.toString()).intValue();
            } else if (f.getType().equals(long.class)) {
                object = Long.valueOf(obj.toString()).longValue();
            } else if (f.getType().equals(java.lang.Long.class)) {
                object = Long.valueOf(obj.toString());
            } else if (f.getType().equals(java.lang.Integer.class)) {
                object = Integer.valueOf(obj.toString());
            } else if (f.getType().equals(java.util.Date.class)) {
                if (obj.getClass().equals(oracle.sql.TIMESTAMP.class)) {
                    oracle.sql.TIMESTAMP newObj = (oracle.sql.TIMESTAMP) obj;
                    object = new Date(newObj.timestampValue().getTime());
                } else if (obj.getClass().equals(java.sql.Timestamp.class)) {
                    java.sql.Timestamp newObj = (java.sql.Timestamp) obj;
                    object = new Date(newObj.getTime());
                } else if (obj.getClass().equals(java.sql.Date.class)) {
                    java.sql.Date newObj = (java.sql.Date) obj;
                    object = new Date(newObj.getTime());
                }
            } else if (f.getType().equals(java.lang.String.class)) {
                if (obj instanceof java.sql.Clob) {
                    java.sql.Clob clob = (java.sql.Clob) obj;
                    //chu y: neu du lieu trong clob lon thi khong dung ham hay
                    //khi do phai dung streaming de doc clob ra
                    object = clob.getSubString(1, (int) clob.length());
                } else if (obj.getClass().equals(oracle.sql.TIMESTAMP.class)) {
                    oracle.sql.TIMESTAMP newObj = (oracle.sql.TIMESTAMP) obj;
                    Date date = new Date(newObj.timestampValue().getTime());
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    object = (String) formatter.format(date);
                } else if (obj.getClass().equals(java.sql.Timestamp.class)) {
                    java.sql.Timestamp newObj = (java.sql.Timestamp) obj;
                    Date date = new Date(newObj.getTime());
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    object = (String) formatter.format(date);
                } else {
                    object = String.valueOf(obj.toString());
                }
            }
        } else {
            if (f.getType().equals(int.class)) {
                object = 0;
            } else if (f.getType().equals(long.class)) {
                object = 0l;
            }
        }

        return object;
    }

    /*
     * remove leading whitespace
     */
    public static String ltrim(String source) {
        return source.replaceAll("^\\s+", "");
    }

    /*
     * remove trailing whitespace
     */
    public static String rtrim(String source) {
        return source.replaceAll("\\s+$", "");
    }

    /*
     * replace multiple whitespaces between words with single blank
     */
    public static String itrim(String source) {
        return source.replaceAll("\\s{2,}", " ");
    }

    /*
     * remove all superfluous whitespaces in source string
     */
    public static String trim(String source) {
        return itrim(ltrim(rtrim(source)));
    }

    public static String lrtrim(String source) {
        return ltrim(rtrim(source));
    }
}
