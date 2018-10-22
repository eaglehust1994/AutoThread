/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.common;

import java.util.Date;

/**
 *
 * @author qlmvt_dungnt44
 */
public class CommonMapQueryLogServer {

    @Column(columnName = "query_id")
    private long queryId;
    @Column(columnName = "log_server_id")
    private long logServerId;
    @Column(columnName = "hour_run_in_day")
    private int hourRunInDay;
    @Column(columnName = "end_time_data")
    private Date endTimeData;
    @Column(columnName = "end_time_run")
    private Date endTimeRun;
    @Column(columnName = "service_type_id")
    private int serviceTypeId;
    @Column(columnName = "module_id")
    private int moduleId;
    @Column(columnName = "time_return_now")
    private int timeReturnNow;
    @Column(columnName = "interval_hour")
    private int intervalHour;
    @Column(columnName = "type")
    private String type;
    @Column(columnName = "is_running")
    private int isRunning;
    @Column(columnName = "run_step_next")
    private int runStepNext;
    @Column(columnName = "status")
    private int status;
    @Column(columnName = "query_name")
    private String queryName;
    @Column(columnName = "log_server_name")
    private String logServerName;
    @Column(columnName = "true_end_time_run")
    private Date trueEndTimeRun;
    @Column(columnName = "data_level")
    private int dataLevel;

    public int getDataLevel() {
        return dataLevel;
    }

    public void setDataLevel(int dataLevel) {
        this.dataLevel = dataLevel;
    }

    public Date getTrueEndTimeRun() {
        return trueEndTimeRun;
    }

    public void setTrueEndTimeRun(Date trueEndTimeRun) {
        this.trueEndTimeRun = trueEndTimeRun;
    }

    public String getLogServerName() {
        return logServerName;
    }

    public void setLogServerName(String logServerName) {
        this.logServerName = logServerName;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRunStepNext() {
        return runStepNext;
    }

    public void setRunStepNext(int runStepNext) {
        this.runStepNext = runStepNext;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(int isRunning) {
        this.isRunning = isRunning;
    }

    public int getIntervalHour() {
        return intervalHour;
    }

    public void setIntervalHour(int intervalHour) {
        this.intervalHour = intervalHour;
    }

    public Date getEndTimeData() {
        return endTimeData;
    }

    public void setEndTimeData(Date endTimeData) {
        this.endTimeData = endTimeData;
    }

    public Date getEndTimeRun() {
        return endTimeRun;
    }

    public void setEndTimeRun(Date endTimeRun) {
        this.endTimeRun = endTimeRun;
    }

    public int getHourRunInDay() {
        return hourRunInDay;
    }

    public void setHourRunInDay(int hourRunInDay) {
        this.hourRunInDay = hourRunInDay;
    }

    public long getLogServerId() {
        return logServerId;
    }

    public void setLogServerId(long logServerId) {
        this.logServerId = logServerId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public long getQueryId() {
        return queryId;
    }

    public void setQueryId(long queryId) {
        this.queryId = queryId;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public int getTimeReturnNow() {
        return timeReturnNow;
    }

    public void setTimeReturnNow(int timeReturnNow) {
        this.timeReturnNow = timeReturnNow;
    }

    public CommonMapQueryLogServer(long queryId, long logServerId, int hourRunInDay, Date endTimeData, Date endTimeRun, int serviceTypeId, int moduleId, int timeReturnNow, String queryName, String logServerName) {
        this.queryId = queryId;
        this.logServerId = logServerId;
        this.hourRunInDay = hourRunInDay;
        this.endTimeData = endTimeData;
        this.endTimeRun = endTimeRun;
        this.serviceTypeId = serviceTypeId;
        this.moduleId = moduleId;
        this.timeReturnNow = timeReturnNow;
        this.queryName = queryName;
        this.logServerName = logServerName;
    }

    public CommonMapQueryLogServer() {
    }
}
