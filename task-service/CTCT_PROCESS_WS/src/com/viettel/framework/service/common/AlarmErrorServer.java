/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.service.common;

import java.util.Date;

/**
 *
 * @author sonnh26
 */
public class AlarmErrorServer {

    private String name;
    private String ip;
    private Integer port;
    private String module;
    private String vendor;
    private String areaCode;
    private Integer vesion;
    private String type;
    private Date updateTime;
    private String errorDetail;
    private String path_error;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Integer getVesion() {
        return vesion;
    }

    public void setVesion(Integer vesion) {
        this.vesion = vesion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public String getPath_error() {
        return path_error;
    }

    public void setPath_error(String path_error) {
        this.path_error = path_error;
    }
    
}
