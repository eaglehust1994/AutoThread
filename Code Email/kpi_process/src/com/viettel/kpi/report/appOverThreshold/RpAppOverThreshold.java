/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.report.appOverThreshold;

import java.util.Date;

/**
 *
 * @author dungvv8
 */
public class RpAppOverThreshold {

    private String appName;
    private Date toDate;
    private Date insertTime;
    private int year;
    private Date updateTime;
    private int month;
    private Date fromDate;
    private Long appId;
    private String dayOverThreshold;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getDayOverThreshold() {
        return dayOverThreshold;
    }

    public void setDayOverThreshold(String dayOverThreshold) {
        this.dayOverThreshold = dayOverThreshold;
    }
}
