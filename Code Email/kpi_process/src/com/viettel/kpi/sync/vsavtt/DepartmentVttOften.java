package com.viettel.kpi.sync.vsavtt;

public class DepartmentVttOften {

    private Long deptId;
    private String deptName;
    private Long deptLevel;
    private Long parentId;

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getDeptLevel() {
        return deptLevel;
    }

    public void setDeptLevel(Long deptLevel) {
        this.deptLevel = deptLevel;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public DepartmentVttOften() {
    }

    public DepartmentVttOften(Long deptId) {
        this.deptId = deptId;
    }
}
