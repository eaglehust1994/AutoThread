/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.common.utils;

/**
 *
 * @author dungvv8
 */
public class Constants {

    public static final String SALT = "kpi";

    public static interface ALARM_TYPE {

        public static final String DAY = "DAY";
        public static final String WEEK = "WEEK";
        public static final String MONTH = "MONTH";

    }

    public static interface ALARM_RULE {

        public static final String DAYS_PER_WEEK = "DAYS_PER_WEEK";
        public static final String CONSECUTIVE_DAYS = "CONSECUTIVE_DAYS";
        public static final String CONSECUTIVE_WEEKS = "CONSECUTIVE_WEEKS";
        public static final String CONSECUTIVE_MONTHS = "CONSECUTIVE_MONTHS";
    }

    public static interface WS {

        public static final String REQUEST_TIMEOUT = "com.sun.xml.internal.ws.request.timeout";
        public static final String CONNECT_TIMEOUT = "com.sun.xml.internal.ws.connect.timeout";
    }

}
