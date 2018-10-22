/*
 * Copyright YYYY Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.getDataSql.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author sondm2@viettel.com.vn
 * @since Apr,12,2010
 * @version 1.0
 */
public class DateTimeUtils {

    /**
     * .
     */
    public static final int CONST3 = 3;
    /**
     * .
     */
    public static final int CONST4 = 4;
    /**
     * .
     */
    public static final int CONST5 = 5;
    /**
     * .
     */
    public static final int CONST6 = 6;
    /**
     * .
     */
    public static final int CONST7 = 7;
    /**
     * .
     */
    public static final int CONST8 = 8;
    /**
     * .
     */
    public static final int CONST9 = 9;
    /**
     * .
     */
    public static final int CONST10 = 10;
    /**
     * .
     */
    public static final int CONST11 = 11;
    /**
     * .
     */
    public static final int CONST12 = 12;

    /**
     * private constructor
     */
    private DateTimeUtils() {
    }

    /**
     *
     * @param date to convert
     * @param pattern in converting
     * @return date
     */
    public static Date convertStringToTime(String date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(date);

        } catch (ParseException e) {
            System.out.println("Date ParseException, string value:" + date);
        }
        return null;
    }

    /**
     *
     * @param date to convert
     * @return String
     * @throws Exception if error
     */
    public static Date convertStringToDate(String date) throws Exception {
        String pattern = "dd/MM/yyyy HH:mm:ss";
        return convertStringToTime(date, pattern);
    }

    public static Date convertStringToDateStandard(String date) throws Exception {
        String pattern = "dd/MM/yyyy";
        return convertStringToTime(date, pattern);
    }
     public static Date convertStringToDateStandardNew(String date) throws Exception {
        String pattern = "yyyyMMdd";
        return convertStringToTime(date, pattern);
    }

    /**
     *
     * @param date to convert
     * @return String
     * @throws Exception if error
     */
    public static String convertDateToString(Date date, String fomatCell) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(fomatCell);
        if (date == null) {
            return "";
        }
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *
     * @return String
     * @throws Exception if error
     */
//    public static String getSysdate() throws Exception {
//        Calendar calendar = Calendar.getInstance();
//        return convertDateToString(calendar.getTime());
//    }

    /**
     *
     * @return String
     * @throws Exception if error
     */
    public static String getSysDateTime() throws Exception {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *
     * @param pattern to convert
     * @return String
     * @throws Exception if error
     */
    public static String getSysDateTime(String pattern) throws Exception {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *
     * @param date to convert
     * @return String
     * @throws Exception if error
     */
    public static Date convertStringToDateTime(String date) throws Exception {
        String pattern = "dd/MM/yyyy";
        return convertStringToTime(date, pattern);
    }

    /**
     *
     * @param date to convert
     * @return String
     * @throws Exception if error
     */
    public static String convertDateTimeToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            return dateFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     *
     * @param utilDate to convert
     * @return date
     */
    public static java.sql.Date convertToSqlDate(java.util.Date utilDate) {
        return new java.sql.Date(utilDate.getTime());
    }

    /**
     *
     * @param monthInput to parse
     * @return String
     */
    public static String parseDate(int monthInput) {
        String dateReturn = "01/01/";
        Calendar cal = Calendar.getInstance();
        switch (monthInput) {
            case 1:
                dateReturn = "01/01/";
                break;
            case 2:
                dateReturn = "01/02/";
                break;
            case CONST3:
                dateReturn = "01/03/";
                break;
            case CONST4:
                dateReturn = "01/04/";
                break;
            case CONST5:
                dateReturn = "01/05/";
                break;
            case CONST6:
                dateReturn = "01/06/";
                break;
            case CONST7:
                dateReturn = "01/07/";
                break;
            case CONST8:
                dateReturn = "01/08/";
                break;
            case CONST9:
                dateReturn = "01/09/";
                break;
            case CONST10:
                dateReturn = "01/10/";
                break;
            case CONST11:
                dateReturn = "01/11/";
                break;
            case CONST12:
                dateReturn = "01/12/";
                break;
        }
        return dateReturn + cal.get(Calendar.YEAR);
    }

    public static long getWeekOfYear(Date updateTime) {
        long w = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(updateTime);
        w = cal.get(Calendar.WEEK_OF_YEAR);
        return w;
    }

    public static long getMonthOfYear(Date updateTime) {
        long month = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(updateTime);
        month = cal.get(Calendar.MONTH) + 1;

        long week = getWeekOfYear(updateTime);
        if (week == 1 && updateTime.getDate() > 7) {
            month++;
            if (month > 12) {
                month = 1;
            }
        }
        return month;
    }

    public static long getYearNumber(Date updateTime) {
        long year = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(updateTime);
        year = cal.get(Calendar.YEAR);
        long month = getMonthOfYear(updateTime);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        if (currentMonth != 1 && month == 1) {
            year++;
        }
        return year;
    }

    /**
     * hàm trả về ngày đầu tiên của tuần, truyền vào số tuần trong năm và giá
     * trị năm
     *
     * @param week
     * @param year
     * @return
     */
    public static Date getStartDateFromWeekAndYear(int week, int year) {
        Date date;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week);
        date = cal.getTime();
        return date;
    }

    /**
     * hàm trả về ngày cuối cùng của tuần, truyền vào số tuần trong năm và giá
     * trị năm
     *
     * @param week
     * @param year
     * @return
     */
    public static Date getFinishDateFromWeekAndYear(int week, int year) {
        Date date;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.add(Calendar.DATE, 6);
        date = cal.getTime();

        return date;
    }

    public static Date getStartWeek(Date updateTime) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(updateTime);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }

    public static Date getFinishWeek(Date updateTime) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(updateTime);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return cal.getTime();
    }
}
