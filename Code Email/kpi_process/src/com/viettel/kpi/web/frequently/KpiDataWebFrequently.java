/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.frequently;

import java.util.Date;

/**
 *
 * @author dungvv8
 */
public class KpiDataWebFrequently {

    private String appName;
    private String funcGroupName;
    private String funcName;
    private Long funcId;
    private Long funcGroupId;
    private Long urlId;
    private Date updateTime;
    private Long noClient;
    private Long appId;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getFuncGroupName() {
        return funcGroupName;
    }

    public void setFuncGroupName(String funcGroupName) {
        this.funcGroupName = funcGroupName;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public Long getFuncId() {
        return funcId;
    }

    public void setFuncId(Long funcId) {
        this.funcId = funcId;
    }

    public Long getFuncGroupId() {
        return funcGroupId;
    }

    public void setFuncGroupId(Long funcGroupId) {
        this.funcGroupId = funcGroupId;
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getNoClient() {
        return noClient;
    }

    public void setNoClient(Long noClient) {
        this.noClient = noClient;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
