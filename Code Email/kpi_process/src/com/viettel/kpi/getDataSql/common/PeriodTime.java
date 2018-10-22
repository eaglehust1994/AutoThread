package com.viettel.kpi.getDataSql.common;

/**
 *
 * @author qlmvt_KhiemVK
 */
public class PeriodTime {

    public enum ePeriodTime {
        eDaily,
        eHourly,
        NONE;
    }

    public static ePeriodTime getPeriodTime(String periodTime) {
        if (periodTime == null) {
            return ePeriodTime.NONE;
        }
        if (periodTime.trim().equalsIgnoreCase("DAILY")) {
            return ePeriodTime.eDaily;
        }
        if (periodTime.trim().equalsIgnoreCase("HOURLY")) {
            return ePeriodTime.eHourly;
        }
        return ePeriodTime.NONE;
    }

    public static String ToString(ePeriodTime periodTime) {
        switch (periodTime) {
            case eDaily:
                return "Daily";
            case eHourly:
                return "Hourly";
            default:
                return "";
        }
    }

    public static String getTruncTime(ePeriodTime periodTime){
        switch (periodTime) {
            case eDaily:
                return "dd";
            case eHourly:
                return "hh24";
            default:
                return "dd";
        }
    }
}
