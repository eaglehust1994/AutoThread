/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.alarmNoc;

import java.util.Date;

/**
 *
 * @author dungvv8
 */
public class KpiAlarmNoc {

    private String appCode;
    private String appName;
    private String operatingDepartment;
    private String operatingCenter;
    private String measureSolution;
    private String funcName;
    private Date startTime;
    private String alarmName;
    private String content;
    private String alarmType;
    private String target;

    private String id;
    private Long count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getOperatingDepartment() {
        return operatingDepartment;
    }

    public void setOperatingDepartment(String operatingDepartment) {
        this.operatingDepartment = operatingDepartment;
    }

    public String getOperatingCenter() {
        return operatingCenter;
    }

    public void setOperatingCenter(String operatingCenter) {
        this.operatingCenter = operatingCenter;
    }

    public String getMeasureSolution() {
        return measureSolution;
    }

    public void setMeasureSolution(String measureSolution) {
        this.measureSolution = measureSolution;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

}
