/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.alarmNoc;

/**
 *
 * @author dungvv8
 */
public class CataAlarmConfig {

    private String alarmType;
    private String alarmRule;
    private Long alarmExceed;
    private Long alarmRuleValue;
    private Long alarmId;

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmRule() {
        return alarmRule;
    }

    public void setAlarmRule(String alarmRule) {
        this.alarmRule = alarmRule;
    }

    public Long getAlarmExceed() {
        return alarmExceed;
    }

    public void setAlarmExceed(Long alarmExceed) {
        this.alarmExceed = alarmExceed;
    }

    public Long getAlarmRuleValue() {
        return alarmRuleValue;
    }

    public void setAlarmRuleValue(Long alarmRuleValue) {
        this.alarmRuleValue = alarmRuleValue;
    }

    public Long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

}
