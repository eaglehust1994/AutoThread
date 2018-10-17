/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.service.common;

import java.sql.Timestamp;

/**
 *
 * @author qlmvt_minhht1
 */
public class QueryObj {

    private int ID;
    private String description;
    private String content;
    private String tableName;
    private String logServerID;
    private String customizeClass;
    private String storedName;
    private Timestamp endTime;

    private Timestamp dataDate;

    private Integer previous_date_del;

    public QueryObj() {
    }

    public QueryObj(int ID, String content, String tableName, String logServerID, 
            String customizeClass, String storedName, Timestamp endTime, 
            Integer previous_date_del) {
        this.ID = ID;
        this.content = content;
        this.tableName = tableName;
        this.logServerID = logServerID;
        this.customizeClass = customizeClass;
        this.storedName = storedName;
        this.endTime = endTime;
        this.previous_date_del = previous_date_del;        
    }

    public int getID() {
        return ID;
    }

    public String getContent() {
        return content;
    }

    public String getLogServerID() {
        return logServerID;
    }

    public String getTableName() {
        return tableName;
    }

    public String getCustomizeClass() {
        return customizeClass;
    }

    public String getStoredName() {
        return storedName;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getDataDate() {
        return dataDate;
    }

    public void setDataDate(Timestamp dataDate) {
        this.dataDate = dataDate;
    }

    public Integer getPrevious_date_del() {
        return previous_date_del;
    }

    public void setPrevious_date_del(Integer previous_date_del) {
        this.previous_date_del = previous_date_del;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
