package com.viettel.kpi.logServer.dl;

import java.util.Date;

/**
 *
 * @author dungvv8
 */
public class KpiLogServerDl {

    private Double kpi;
    //dungvv8_20160119
    private Long total;
    //dungvv8_20160119
    private String funcName;
    private Date insertTime;
    private String appCode;
    private Long queryId;
    private String logServerId;
    private Date updateTime;

    //dungvv8_20160119
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
    //dungvv8_20160119

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
