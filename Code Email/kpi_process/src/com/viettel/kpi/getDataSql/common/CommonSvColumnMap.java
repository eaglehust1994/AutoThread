/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.kpi.getDataSql.common;

/**
 *
 * @author qlmvt_dungnt44
 */
public class CommonSvColumnMap {
    private long queryId;
    private String localColumn;
    private String remoteColumn;
    private String dataType;

    public CommonSvColumnMap(long queryId, String localColumn, String remoteColumn, String dataType) {
        this.queryId = queryId;
        this.localColumn = localColumn;
        this.remoteColumn = remoteColumn;
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getLocalColumn() {
        return localColumn;
    }

    public void setLocalColumn(String localColumn) {
        this.localColumn = localColumn;
    }

    public long getQueryId() {
        return queryId;
    }

    public void setQueryId(long queryId) {
        this.queryId = queryId;
    }

    public String getRemoteColumn() {
        return remoteColumn;
    }

    public void setRemoteColumn(String remoteColumn) {
        this.remoteColumn = remoteColumn;
    }

    

}
