package com.viettel.kpi.web.total.dl;

import java.util.Date;

public class RpKpiToantrinhDl {

    private Long urlId;
    private String appCode;
    private Long total;
    private Double kpiServer;
    private Double kpiClient;
    private Date updateTime;
    private Date insertTime;

    public RpKpiToantrinhDl() {
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Double getKpiServer() {
        return kpiServer;
    }

    public void setKpiServer(Double kpiServer) {
        this.kpiServer = kpiServer;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Double getKpiClient() {
        return kpiClient;
    }

    public void setKpiClient(Double kpiClient) {
        this.kpiClient = kpiClient;
    }

}
