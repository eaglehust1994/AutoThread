/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.service.common;

/**
 *
 * @author qlmvt_dungnt44
 */
public class CommonLogServer {

    @Column(columnName="log_server_id")
    private long logServerId;
    @Column(columnName="log_server_name")
    private String logServerName;
    @Column(columnName="protocol")
    private String protocol;
    @Column(columnName="ip")
    private String ip;
    @Column(columnName="driver")
    private String driver;
    @Column(columnName="url")
    private String url;
    @Column(columnName="username")
    private String userName;
    @Column(columnName="password")
    private String password;
    @Column(columnName="vendor")
    private String vendor;
    @Column(columnName="status")
    private int status;

    public CommonLogServer(long logServerId, String logServerName, String protocol, String ip, String driver, String url, String userName, String password, String vendor, int status) {
        this.logServerId = logServerId;
        this.logServerName = logServerName;
        this.protocol = protocol;
        this.ip = ip;
        this.driver = driver;
        this.url = url;
        this.userName = userName;
        this.password = password;
        this.vendor = vendor;
        this.status = status;
    }

    public CommonLogServer() {
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getLogServerId() {
        return logServerId;
    }

    public void setLogServerId(long logServerId) {
        this.logServerId = logServerId;
    }

    public String getLogServerName() {
        return logServerName;
    }

    public void setLogServerName(String logServerName) {
        this.logServerName = logServerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
