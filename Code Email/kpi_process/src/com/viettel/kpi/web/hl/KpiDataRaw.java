/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.hl;

import java.util.Date;

/**
 *
 * @author dungvv8
 */
public class KpiDataRaw {

    private Long id;
    private Long urlId;
    private String url;
    private String ipLan;
    private String ipWan;
    private String ipServer;
    private String portServer;
    private String responseStatus;
    private Date updateTime;
    private Date insertTime;
    private Long responseTime;
    private Long isSuccess;
    private Long err2x;
    private Long err3x;
    private Long err4x;
    private Long err5x;
    private Long isTimeout;
    private Long isExceed;
    private Date startTime;
    private Date endTime;
    private String contentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIpLan() {
        return ipLan;
    }

    public void setIpLan(String ipLan) {
        this.ipLan = ipLan;
    }

    public String getIpWan() {
        return ipWan;
    }

    public void setIpWan(String ipWan) {
        this.ipWan = ipWan;
    }

    public String getIpServer() {
        return ipServer;
    }

    public void setIpServer(String ipServer) {
        this.ipServer = ipServer;
    }

    public String getPortServer() {
        return portServer;
    }

    public void setPortServer(String portServer) {
        this.portServer = portServer;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
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

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public Long getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Long isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Long getErr2x() {
        return err2x;
    }

    public void setErr2x(Long err2x) {
        this.err2x = err2x;
    }

    public Long getErr3x() {
        return err3x;
    }

    public void setErr3x(Long err3x) {
        this.err3x = err3x;
    }

    public Long getErr4x() {
        return err4x;
    }

    public void setErr4x(Long err4x) {
        this.err4x = err4x;
    }

    public Long getErr5x() {
        return err5x;
    }

    public void setErr5x(Long err5x) {
        this.err5x = err5x;
    }

    public Long getIsTimeout() {
        return isTimeout;
    }

    public void setIsTimeout(Long isTimeout) {
        this.isTimeout = isTimeout;
    }

    public Long getIsExceed() {
        return isExceed;
    }

    public void setIsExceed(Long isExceed) {
        this.isExceed = isExceed;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

}
