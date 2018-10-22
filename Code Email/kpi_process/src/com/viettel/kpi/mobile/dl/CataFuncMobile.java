/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.mobile.dl;

/**
 *
 * @author dungvv8
 */
public class CataFuncMobile {

    private Long urlId;
    private String appCode;
    private String funcName;
    private String url;
//    private Double target;

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

//    public Double getTarget() {
//        return target;
//    }
//
//    public void setTarget(Double target) {
//        this.target = target;
//    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public CataFuncMobile() {
    }
}
