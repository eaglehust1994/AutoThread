/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.common;

/**
 *
 * @author qlmvt_dungnt44
 */
public class CommonSvQuery {

    @Column(columnName="query_id")
    private int queryId;
    @Column(columnName="query_name")
    private String queryName;
    @Column(columnName="query_text")
    private String queryText;
    @Column(columnName="table_name")
    private String tableName;
    @Column(columnName="data_level")
    private int dataLevel;
    @Column(columnName="previous_day_del")
    private Integer previousDayDel;
    @Column(columnName="customer_class")
    private String customerClass;
    @Column(columnName="stored_name")
    private String storedName;
    @Column(columnName="status")
    private int status;
    private String shortTableName;

    public String getShortTableName() {
        return shortTableName;
    }

    public void setShortTableName(String shortTableName) {
        this.shortTableName = shortTableName;
    }

    public CommonSvQuery(int queryId, String queryName, String queryText, String tableName, int dataLevel, Integer previousDayDel, String customerClass, String storedName, int status) {
        this.queryId = queryId;
        this.queryName = queryName;
        this.queryText = queryText;
        this.tableName = tableName;
        this.dataLevel = dataLevel;
        this.previousDayDel = previousDayDel;
        this.customerClass = customerClass;
        this.storedName = storedName;
        this.status = status;
    }

    public CommonSvQuery() {
    }

    public String getCustomerClass() {
        return customerClass;
    }

    public void setCustomerClass(String customerClass) {
        this.customerClass = customerClass;
    }

    public int getDataLevel() {
        return dataLevel;
    }

    public void setDataLevel(int dataLevel) {
        this.dataLevel = dataLevel;
    }

    public Integer getPreviousDayDel() {
        return previousDayDel;
    }

    public void setPreviousDayDel(Integer previousDayDel) {
        this.previousDayDel = previousDayDel;
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStoredName() {
        return storedName;
    }

    public void setStoredName(String storedName) {
        this.storedName = storedName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
