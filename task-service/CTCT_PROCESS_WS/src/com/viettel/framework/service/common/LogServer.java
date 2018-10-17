/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.service.common;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author qlmvt_minhht1
 */
public class LogServer {

    private Map<String, Object> counters = new HashMap<String, Object>();
    private String logServerId;
    private String name;
    private int serverTypeId;
    private String url;
    private String ip;
    private int port;
    private String username;
    private String password;
    private String vendor;
    private String protocol;
    private String driver;
    private int enable;
    private String areaCode;
    private String module;

    public LogServer() {
    }

    public LogServer(String logServerId, String name, int serverTypeId, String url, String ip, int port, String username, String password, String vendor, String protocol, String driver, int enable) {
        this.logServerId = logServerId;
        this.name = name;
        this.serverTypeId = serverTypeId;
        this.url = url;
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.vendor = vendor;
        this.protocol = protocol;
        this.driver = driver;
        this.enable = enable;
    }
    //dungdv15_New_25/03/2013_start

    public LogServer(String logServerId, String name, int serverTypeId, String url, String ip, int port, String username, String password, String vendor, String protocol, String driver, int enable, String areaCode) {
        this.logServerId = logServerId;
        this.name = name;
        this.serverTypeId = serverTypeId;
        this.url = url;
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.vendor = vendor;
        this.protocol = protocol;
        this.driver = driver;
        this.enable = enable;
        this.areaCode = areaCode;
    }
//dungdv15_New_25/03/2013_start

    //sonnh26_R5236_14/03/2014_start
    public LogServer(String logServerId, String name, int serverTypeId, String url, String ip, int port, String username, String password, String vendor, String protocol, String driver, int enable, String areaCode, String module) {
        this.logServerId = logServerId;
        this.name = name;
        this.serverTypeId = serverTypeId;
        this.url = url;
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.vendor = vendor;
        this.protocol = protocol;
        this.driver = driver;
        this.enable = enable;
        this.areaCode = areaCode;
        this.module = module;
    }
//sonnh26_R5236_14/03/2014_end

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public void add(String counterName, Object value) {
        counters.put(counterName, value);
    }

    public Object getValue(String counterName) {
        return counters.get(counterName);
    }

    public void setValue(String counterName, Object value) {
        counters.remove(counterName);
        add(counterName, value);
    }

    public int size() {
        return counters.size();
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLogServerId(String logServerId) {
        this.logServerId = logServerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setServerTypeId(int serverTypeId) {
        this.serverTypeId = serverTypeId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDriver() {
        return driver;
    }

    public int getEnable() {
        return enable;
    }

    public String getIp() {
        return ip;
    }

    public String getLogServerId() {
        return logServerId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getServerTypeId() {
        return serverTypeId;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getVendor() {
        return vendor;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
