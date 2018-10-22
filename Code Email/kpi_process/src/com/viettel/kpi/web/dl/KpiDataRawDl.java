package com.viettel.kpi.web.dl;

import java.util.Date;

public class KpiDataRawDl {

    private Long urlId;
    private String urlPattern;
    private Double kpi;
    private Long totalRequest;
    private Long maxResponseTime;
    private Double exceedRate;
    private Double succRate;
    private Double failRate;
    private Double err2xRate;
    private Double err3xRate;
    private Double err4xRate;
    private Double err5xRate;
    private Double timeoutRate;
    private Long isOk;
    private Long noSample;
    private Date updateTime;
    private Date insertTime;

    public KpiDataRawDl() {
    }

    public Double getFailRate() {
        return failRate;
    }

    public void setFailRate(Double failRate) {
        this.failRate = failRate;
    }

    public Double getErr2xRate() {
        return err2xRate;
    }

    public void setErr2xRate(Double err2xRate) {
        this.err2xRate = err2xRate;
    }

    public Double getErr3xRate() {
        return err3xRate;
    }

    public void setErr3xRate(Double err3xRate) {
        this.err3xRate = err3xRate;
    }

    public Double getErr4xRate() {
        return err4xRate;
    }

    public void setErr4xRate(Double err4xRate) {
        this.err4xRate = err4xRate;
    }

    public Double getErr5xRate() {
        return err5xRate;
    }

    public void setErr5xRate(Double err5xRate) {
        this.err5xRate = err5xRate;
    }

    public Double getTimeoutRate() {
        return timeoutRate;
    }

    public void setTimeoutRate(Double timeoutRate) {
        this.timeoutRate = timeoutRate;
    }

    public Long getIsOk() {
        return isOk;
    }

    public void setIsOk(Long isOk) {
        this.isOk = isOk;
    }

    public Long getNoSample() {
        return noSample;
    }

    public void setNoSample(Long noSample) {
        this.noSample = noSample;
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
