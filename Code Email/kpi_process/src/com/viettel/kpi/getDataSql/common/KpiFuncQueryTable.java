/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.common;

/**
 *
 * @author dongnd3
 */
public class KpiFuncQueryTable {

    private long query_id;
    private long group_id;
    private String table_name;
    private String load_type;
    private String sync_type;
    private String time_column;
    private long num_day_load_data;
    private long module_id;

    public long getModule_id() {
        return module_id;
    }

    public void setModule_id(long module_id) {
        this.module_id = module_id;
    }

    public KpiFuncQueryTable() {
    }

    public KpiFuncQueryTable(long query_id, long group_id, String table_name, String load_type, String sync_type, String time_column, long num_day_load_data, long module_id) {
        this.query_id = query_id;
        this.group_id = group_id;
        this.table_name = table_name;
        this.load_type = load_type;
        this.sync_type = sync_type;
        this.time_column = time_column;
        this.num_day_load_data = num_day_load_data;
        this.module_id = module_id;
    }

    public long getQuery_id() {
        return query_id;
    }

    public void setQuery_id(long query_id) {
        this.query_id = query_id;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getLoad_type() {
        return load_type;
    }

    public void setLoad_type(String load_type) {
        this.load_type = load_type;
    }

    public String getSync_type() {
        return sync_type;
    }

    public void setSync_type(String sync_type) {
        this.sync_type = sync_type;
    }

    public String getTime_column() {
        return time_column;
    }

    public void setTime_column(String time_column) {
        this.time_column = time_column;
    }

    public long getNum_day_load_data() {
        return num_day_load_data;
    }

    public void setNum_day_load_data(long num_day_load_data) {
        this.num_day_load_data = num_day_load_data;
    }

    
}
