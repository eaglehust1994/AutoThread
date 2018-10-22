/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.service.manager;

import com.viettel.kpi.service.common.LogServer;
import org.apache.log4j.Logger;

/**
 *
 * @author qlmvt_minhht1
 */
public abstract class Job {

    protected long interval;
    protected long minute;
    protected long hour;
    protected long day;
    protected boolean lostData = true;
    protected Logger logger;

    public Job() {
    }

    public long getInterval() {
        return interval;
    }

    public long getDay() {
        return day;
    }

    public long getHour() {
        return hour;
    }

    public long getMinute() {
        return minute;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public void setMinute(long minute) {
        this.minute = minute;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setLostData(boolean lostData) {
        this.lostData = lostData;
    }

    public abstract void run(LogServer logServer, Logger logger);
}
