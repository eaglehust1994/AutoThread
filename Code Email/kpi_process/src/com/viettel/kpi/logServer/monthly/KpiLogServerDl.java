package com.viettel.kpi.logServer.monthly;

import java.util.Date;

public class KpiLogServerDl {

    private Double kpi;
    private String funcName;
    private Date insertTime;
    private String appCode;
    private Long queryId;
    private String logServerId;
    private Date updateTime;

    public Double getKpi() {
        return kpi;
    }

    public void setKpi(Double kpi) {
        this.kpi = kpi;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public String getLogServerId() {
        return logServerId;
    }

    public void setLogServerId(String logServerId) {
        this.logServerId = logServerId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public KpiLogServerDl() {
    }
}