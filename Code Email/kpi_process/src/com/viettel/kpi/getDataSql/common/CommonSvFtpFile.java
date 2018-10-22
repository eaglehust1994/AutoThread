/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.common;

/**
 *
 * @author qlmvt_dongnd3
 */
public class CommonSvFtpFile {

    @Column(columnName = "file_id")
    private int fileId;
    @Column(columnName = "description")
    private String description;
    @Column(columnName = "remote_file_name")
    private String remoteFileName;
    @Column(columnName = "remote_name_format")
    private String remoteNameFormat;
    @Column(columnName = "remote_file_path")
    private String remoteFilePath;
    @Column(columnName = "remote_path_format")
    private String remotePathFormat;
    @Column(columnName = "file_type")
    private String fileType;
    @Column(columnName = "local_store_path")
    private String localStorePath;
    @Column(columnName = "local_path_format")
    private String localPathFormat;
    @Column(columnName = "table_name")
    private String tableName;
    @Column(columnName = "status")
    private int status;
    @Column(columnName = "data_level")
    private int dataLevel;
    @Column(columnName = "row_header")
    private int rowHeader;
    @Column(columnName = "detimiter")
    private String delimiter;
    @Column(columnName = "prev_date_del")
    private Integer prevDateDel;
    private String shortTableName;

    public String getShortTableName() {
        return shortTableName;
    }

    public void setShortTableName(String shortTableName) {
        this.shortTableName = shortTableName;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public int getDataLevel() {
        return dataLevel;
    }

    public void setDataLevel(int dataLevel) {
        this.dataLevel = dataLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getLocalPathFormat() {
        return localPathFormat;
    }

    public void setLocalPathFormat(String localPathFormat) {
        this.localPathFormat = localPathFormat;
    }

    public String getLocalStorePath() {
        return localStorePath;
    }

    public void setLocalStorePath(String localStorePath) {
        this.localStorePath = localStorePath;
    }

    public Integer getPrevDateDel() {
        return prevDateDel;
    }

    public void setPrevDateDel(Integer prevDateDel) {
        this.prevDateDel = prevDateDel;
    }

    public String getRemoteFileName() {
        return remoteFileName;
    }

    public void setRemoteFileName(String remoteFileName) {
        this.remoteFileName = remoteFileName;
    }

    public String getRemoteFilePath() {
        return remoteFilePath;
    }

    public void setRemoteFilePath(String remoteFilePath) {
        this.remoteFilePath = remoteFilePath;
    }

    public String getRemoteNameFormat() {
        return remoteNameFormat;
    }

    public void setRemoteNameFormat(String remoteNameFormat) {
        this.remoteNameFormat = remoteNameFormat;
    }

    public String getRemotePathFormat() {
        return remotePathFormat;
    }

    public void setRemotePathFormat(String remotePathFormat) {
        this.remotePathFormat = remotePathFormat;
    }

    public int getRowHeader() {
        return rowHeader;
    }

    public void setRowHeader(int rowHeader) {
        this.rowHeader = rowHeader;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
