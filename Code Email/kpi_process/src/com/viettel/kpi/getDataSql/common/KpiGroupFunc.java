package com.viettel.kpi.getDataSql.common;

import com.viettel.kpi.getDataSql.common.PeriodTime.ePeriodTime;
import java.sql.Timestamp;

/**
 *
 * @author qlmvt_KhiemVK
 */
public class KpiGroupFunc {

    private long group_id;
    private String group_name;
    private long group_id_parent;
    private ePeriodTime periodTime;
    private int module_id;
    private Integer parallel;
    private Timestamp start_time;
    private Timestamp finish_time;
    //Rxxxx_New_TienBV2_05122012_Start
    private String vendor;

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    //Rxxxx_New_TienBV2_05122012_End

    public KpiGroupFunc() {
    }

    public KpiGroupFunc(long group_id, String group_name) {
        this.group_id = group_id;
        this.group_name = group_name;
    }

    public Integer getParallel() {
        return parallel;
    }

    public void setParallel(Integer parallel) {
        this.parallel = parallel;
    }

    public KpiGroupFunc(long group_id, String group_name, long group_id_parent) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.group_id_parent = group_id_parent;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public long getGroup_id_parent() {
        return group_id_parent;
    }

    public void setGroup_id_parent(long group_id_parent) {
        this.group_id_parent = group_id_parent;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public ePeriodTime getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(ePeriodTime periodTime) {
        this.periodTime = periodTime;
    }

    public int getModule_id() {
        return module_id;
    }

    public void setModule_id(int module_id) {
        this.module_id = module_id;
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
}
