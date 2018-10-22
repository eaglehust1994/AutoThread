package com.viettel.kpi.getDataSql.common;

import java.sql.Timestamp;

/**
 *
 * @author qlmvt_KhiemVK
 */
public class KpiFuncQuery {

    private long group_id;
    private long query_id;
    private String query_desc;
    private String query_gen;
    private String query_str;
    private String map_table;
    private boolean isInsert;
    private boolean isDelBfInsert;
    private String typeFunc;
    private String conditionTime;
    private Timestamp start_time;
    private Timestamp finish_time;
    private int isTimestenQuery;

    public KpiFuncQuery() {
    }

    public KpiFuncQuery(long group_id, long query_id, String query_desc) {
        this.group_id = group_id;
        this.query_id = query_id;
        this.query_desc = query_desc;
    }

    public KpiFuncQuery(long group_id, long query_id, String query_desc, String query_str) {
        this.group_id = group_id;
        this.query_id = query_id;
        this.query_desc = query_desc;
        this.query_str = query_str;
    }

    public KpiFuncQuery(long group_id, long query_id, String query_desc, String query_str, String map_table) {
        this.group_id = group_id;
        this.query_id = query_id;
        this.query_desc = query_desc;
        this.query_str = query_str;
        this.map_table = map_table;
    }

    public KpiFuncQuery(long group_id, long query_id, String query_desc, String query_gen, String query_str, String map_table, boolean isInsert, boolean isDelBfInsert,
            String typeFunc, String conditionTime) {
        this.group_id = group_id;
        this.query_id = query_id;
        this.query_desc = query_desc;
        this.query_gen = query_gen;
        this.query_str = query_str;
        this.map_table = map_table;
        this.isInsert = isInsert;
        this.isDelBfInsert = isDelBfInsert;
        this.typeFunc = typeFunc;
        this.conditionTime = conditionTime;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public String getMap_table() {
        return map_table;
    }

    public void setMap_table(String map_table) {
        this.map_table = map_table;
    }

    public String getQuery_desc() {
        return query_desc;
    }

    public void setQuery_desc(String query_desc) {
        this.query_desc = query_desc;
    }

    public long getQuery_id() {
        return query_id;
    }

    public void setQuery_id(long query_id) {
        this.query_id = query_id;
    }

    public String getQuery_str() {
        return query_str;
    }

    public void setQuery_str(String query_str) {
        this.query_str = query_str;
    }

    public String getQuery_gen() {
        return query_gen;
    }

    public void setQuery_gen(String query_gen) {
        this.query_gen = query_gen;
    }

    public boolean isIsDelBfInsert() {
        return isDelBfInsert;
    }

    public void setIsDelBfInsert(boolean isDelBfInsert) {
        this.isDelBfInsert = isDelBfInsert;
    }

    public boolean isIsInsert() {
        return isInsert;
    }

    public void setIsInsert(boolean isInsert) {
        this.isInsert = isInsert;
    }

    public String getTypeFunc() {
        return typeFunc;
    }

    public void setTypeFunc(String typeFunc) {
        this.typeFunc = typeFunc;
    }

    public Timestamp getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(Timestamp finish_time) {
        this.finish_time = finish_time;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public String getConditionTime() {
        return conditionTime;
    }

    public void setConditionTime(String conditionTime) {
        this.conditionTime = conditionTime;
    }

    public int getIsTimestenQuery() {
        return isTimestenQuery;
    }

    public void setIsTimestenQuery(int isTimestenQuery) {
        this.isTimestenQuery = isTimestenQuery;
    }
}
