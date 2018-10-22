/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.ExportFileEmailWeekly;

import java.sql.Clob;

/**
 *
 * @author sonnh26
 */
public class EmailConfigForm {

    private String columnCode;
    private String columnName;
    private String columnStyle;
    private Integer columnIndex;
    private Clob queryStringsql;
    private String functionTitle;
    private String functionCode;
    private String sheetName;
    private String columnNameL1;
    private String columnNameL2;

    public String getColumnNameL2() {
        return columnNameL2;
    }

    public void setColumnNameL2(String columnNameL2) {
        this.columnNameL2 = columnNameL2;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnStyle() {
        return columnStyle;
    }

    public void setColumnStyle(String columnStyle) {
        this.columnStyle = columnStyle;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public Clob getQueryStringsql() {
        return queryStringsql;
    }

    public void setQueryStringsql(Clob queryStringsql) {
        this.queryStringsql = queryStringsql;
    }

    public String getFunctionTitle() {
        return functionTitle;
    }

    public void setFunctionTitle(String functionTitle) {
        this.functionTitle = functionTitle;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getColumnNameL1() {
        return columnNameL1;
    }

    public void setColumnNameL1(String columnNameL1) {
        this.columnNameL1 = columnNameL1;
    }

}
