/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.common;

import com.viettel.kpi.service.common.DataTypes;
import com.viettel.kpi.service.common.DataTypes.eDataType;

/**
 *
 * @author qlmvt_dongnd3
 */
public class CommonSvFtpColumnMap {

    private int fileId;
    private String localColumn;
    private DataTypes.eDataType dataType;
    private String colType;
    private Long rowIdx;
    private Long colIdx;
    private String defaultValue;
    private String cellDataFormat;
    private String ignoreCharacter;
    private String blacklistValue;
    private int blacklistIgnoreInsert;

    public int getBlacklistIgnoreInsert() {
        return blacklistIgnoreInsert;
    }

    public void setBlacklistIgnoreInsert(int blacklistIgnoreInsert) {
        this.blacklistIgnoreInsert = blacklistIgnoreInsert;
    }

    public String getBlacklistValue() {
        return blacklistValue;
    }

    public void setBlacklistValue(String blacklistValue) {
        this.blacklistValue = blacklistValue;
    }

    public String getCellDataFormat() {
        return cellDataFormat;
    }

    public void setCellDataFormat(String cellDataFormat) {
        this.cellDataFormat = cellDataFormat;
    }

    public Long getColIdx() {
        return colIdx;
    }

    public void setColIdx(Long colIdx) {
        this.colIdx = colIdx;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public eDataType getDataType() {
        return dataType;
    }

    public void setDataType(eDataType dataType) {
        this.dataType = dataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getIgnoreCharacter() {
        return ignoreCharacter;
    }

    public void setIgnoreCharacter(String ignoreCharacter) {
        this.ignoreCharacter = ignoreCharacter;
    }

    public String getLocalColumn() {
        return localColumn;
    }

    public void setLocalColumn(String localColumn) {
        this.localColumn = localColumn;
    }

    public Long getRowIdx() {
        return rowIdx;
    }

    public void setRowIdx(Long rowIdx) {
        this.rowIdx = rowIdx;
    }

    public CommonSvFtpColumnMap() {
    }

    public CommonSvFtpColumnMap(int fileId, String localColumn,
            String colType, Long rowIdx, Long colIdx, String defaultValue,
            String cellDataFormat, String ignoreCharacter, String blacklistValue, int blacklistIgnoreInsert) {
        this.fileId = fileId;
        this.localColumn = localColumn;
        this.colType = colType;
        this.rowIdx = rowIdx;
        this.colIdx = colIdx;
        this.defaultValue = defaultValue;
        this.cellDataFormat = cellDataFormat;
        this.ignoreCharacter = ignoreCharacter;
        this.blacklistValue = blacklistValue;
        this.blacklistIgnoreInsert = blacklistIgnoreInsert;
    }
}
