package com.viettel.kpi.getDataSql.common;

import com.viettel.kpi.service.common.DataTypes.eDataType;

/**
 *
 * @author qlmvt_KhiemVK
 */
public class KpiColumnMap {

    private long query_id;
    private long kpi_id;
    private String kpi_desc;
    private String target_column;
    private String column_code;
    private boolean is_target_key;
    private String query_condition;
    private eDataType data_type;

    public KpiColumnMap() {
    }

    public KpiColumnMap(long query_id, long kpi_id, String kpi_desc) {
        this.query_id = query_id;
        this.kpi_id = kpi_id;
        this.kpi_desc = kpi_desc;
    }

    public String getColumn_code() {
        return column_code;
    }

    public void setColumn_code(String column_code) {
        this.column_code = column_code;
    }

    public eDataType getData_type() {
        return data_type;
    }

    public void setData_type(eDataType data_type) {
        this.data_type = data_type;
    }

    public boolean isIs_target_key() {
        return is_target_key;
    }

    public void setIs_target_key(boolean is_target_key) {
        this.is_target_key = is_target_key;
    }

    public String getKpi_desc() {
        return kpi_desc;
    }

    public void setKpi_desc(String kpi_desc) {
        this.kpi_desc = kpi_desc;
    }

    public long getKpi_id() {
        return kpi_id;
    }

    public void setKpi_id(long kpi_id) {
        this.kpi_id = kpi_id;
    }

    public String getQuery_condition() {
        return query_condition;
    }

    public void setQuery_condition(String query_condition) {
        this.query_condition = query_condition;
    }

    public long getQuery_id() {
        return query_id;
    }

    public void setQuery_id(long query_id) {
        this.query_id = query_id;
    }

    public String getTarget_column() {
        return target_column;
    }

    public void setTarget_column(String target_column) {
        this.target_column = target_column;
    }
}
