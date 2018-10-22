/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.SendEmailProcessMonthly;

import java.sql.Clob;
import java.util.Date;

/**
 *
 * @author ngocdm4
 */
public class EmailSendFrom {

    private int emailId;
    private String senderEmailCode;
    private Clob content;
    private int isFile;
    private String subject;
    private String logServerId;
    private String path;
    private String fileName;
    private int isSendEmail;
    private String errorMsg;
    private Date startTime;
    //hoanm1_add
    private String email;
    private Integer hour_run;
    private Integer day_run;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getHour_run() {
        return hour_run;
    }

    public void setHour_run(Integer hour_run) {
        this.hour_run = hour_run;
    }

    public Integer getDay_run() {
        return day_run;
    }

    public void setDay_run(Integer day_run) {
        this.day_run = day_run;
    }

    public int getEmailId() {
        return emailId;
    }

    public void setEmailId(int emailId) {
        this.emailId = emailId;
    }

    public String getSenderEmailCode() {
        return senderEmailCode;
    }

    public void setSenderEmailCode(String senderEmailCode) {
        this.senderEmailCode = senderEmailCode;
    }

    public Clob getContent() {
        return content;
    }

    public void setContent(Clob content) {
        this.content = content;
    }

    public int getIsFile() {
        return isFile;
    }

    public void setIsFile(int isFile) {
        this.isFile = isFile;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getLogServerId() {
        return logServerId;
    }

    public void setLogServerId(String logServerId) {
        this.logServerId = logServerId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getIsSendEmail() {
        return isSendEmail;
    }

    public void setIsSendEmail(int isSendEmail) {
        this.isSendEmail = isSendEmail;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

}
