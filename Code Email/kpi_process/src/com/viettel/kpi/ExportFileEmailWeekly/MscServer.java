/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.ExportFileEmailWeekly;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 * @date Oct 24, 2012
 */
public class MscServer {

    private String mscCode;
    private String ip;
    private int port;
    private String userName;
    private String password;
    private String type;
    private int version;
    private String vendor;
    private String note;
    private Logger logger;
    //R3788_sonnh26_17062013_Start
    private String protocol;
    private Long logServerId;

    public Long getLogServerId() {
        return logServerId;
    }

    public void setLogServerId(Long logServerId) {
        this.logServerId = logServerId;
    }

    public String getProtocol() {
        return protocol;
    }
    private String time;

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    //R3788_sonnh26_17062013_End

    public MscServer() {
        //
    }

    public MscServer(String mscCode, String ip, int port, String userName, String password, String type, int version, String vendor, String note) {
        this.mscCode = mscCode;
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.type = type;
        this.vendor = vendor;
        this.version = version;
        this.note = note;
//        System.out.println("MscServer: " + mscCode + " - IP: " + ip + " - port: " + port + " - username: " + userName + ", password: " + password);
    }

    public MscServer(String mscCode, String ip, int port, String userName, String password, String type, int version, String vendor, String note, Logger logger) {
        this.mscCode = mscCode;
        this.ip = ip;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.type = type;
        this.vendor = vendor;
        this.version = version;
        this.note = note;
        this.logger = logger;

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMscCode() {
        return mscCode;
    }

    public void setMscCode(String mscCode) {
        this.mscCode = mscCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
