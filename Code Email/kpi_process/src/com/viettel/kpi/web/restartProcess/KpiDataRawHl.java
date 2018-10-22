package com.viettel.kpi.web.restartProcess;

import java.util.Date;

public class KpiDataRawHl {

    private Long urlId;
    private String urlPattern;
    private Double kpi;
    private Long totalRequest;
    private Long maxResponseTime;
    private Double exceedRate;
    private Double succRate;
    private Date updateTime;
    private Long hour;
    private Date insertTime;

    public KpiDataRawHl() {
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Double getKpi() {
        return kpi;
    }

    public void setKpi(Double kpi) {
        this.kpi = kpi;
    }

    public Long getHour() {
        return hour;
    }

    public void setHour(Long hour) {
        this.hour = hour;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getTotalRequest() {
        return totalRequest;
    }

    public void setTotalRequest(Long totalRequest) {
        this.totalRequest = totalRequest;
    }

    public Double getSuccRate() {
        return succRate;
    }

    public void setSuccRate(Double succRate) {
        this.succRate = succRate;
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public Long getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Long maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public Double getExceedRate() {
        return exceedRate;
    }

    public void setExceedRate(Double exceedRate) {
        this.exceedRate = exceedRate;
    }

}
