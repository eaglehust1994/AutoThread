package com.viettel.kpi.getDataSql.common;

/**
 *
 * @author qlmvt_KhiemVK
 */
public class LoggerLevel {
    public enum eLoggerLevel{
        eInfo,
        eWarning,
        eError,
        NONE;
    }

    public static String getLevel(eLoggerLevel level){
        switch(level){
            case eError:
                return "error";
            case eWarning:
                return "warning";
            case eInfo:
                return "info";
        }
        return "unknown";
    }
}
