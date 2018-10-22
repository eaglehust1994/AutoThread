/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.common;

/**
 *
 * @author os_namndh
 */
public class CommonSvFtpColumnMapPK {

    private int fileId;
    private String localColumn;

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getLocalColumn() {
        return localColumn;
    }

    public void setLocalColumn(String localColumn) {
        this.localColumn = localColumn;
    }

    public CommonSvFtpColumnMapPK() {
    }

    public CommonSvFtpColumnMapPK(int fileId, String localColumn) {
        this.fileId = fileId;
        this.localColumn = localColumn;
    }
}
