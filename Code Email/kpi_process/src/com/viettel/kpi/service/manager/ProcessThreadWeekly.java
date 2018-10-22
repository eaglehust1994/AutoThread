/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.service.manager;

import com.viettel.kpi.service.common.LogServer;
import com.viettel.kpi.service.manager.Constants.Time;
import com.viettel.mmserver.base.ProcessThreadMX;
import java.util.Calendar;
import java.util.Date;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

/**
 *
 * @author qlmvt_minhht1
 */
public class ProcessThreadWeekly extends ProcessThreadMX {

    private LogServer logServer;
    private Job job;
    private boolean useFixedTime;
    private long interval;

    public ProcessThreadWeekly(LogServer logServer, Job job, long day, long hour) throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        super("Service." + logServer.getLogServerId());
        this.logServer = logServer;
        this.job = job;
        this.job.setLogger(logger);
        this.job.setDay(day);
        this.job.setHour(hour);
        this.useFixedTime = (1 <= day && day <= 7 && 0 <= hour && hour <= 23) ? true : false;
        this.interval = Time.ONE_HOUR;
        String MbeanName = "process:name=" + logServer.getLogServerId();
        registerAgent(MbeanName);
    }

    @Override
    protected void process() {
        buStartTime = new Date();
        long startTime = System.currentTimeMillis();
        logger.info("\r\n\r\n***** Begin Process *****");
        try {
            if ((!useFixedTime) || ((job.getHour() == getCurrentHour()) && (job.getDay() == getCurrentDay()))) {
                job.run(logServer, logger);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("process: " + ex.getMessage());
        } finally {
        }
        buStartTime = null;
        logger.info("Thread " + this.threadName + " sleep " + this.interval / 1000 + " seconds");
        long endTime = System.currentTimeMillis();
        try {
            long processTime = endTime - startTime;
            if (interval != 0) {
                processTime %= interval;
            }
            Thread.sleep(this.interval - processTime);
        } catch (InterruptedException e) {
        }        
    }

    public int getCurrentHour() {
        int currentMinute = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentMinute = now.get(Calendar.HOUR_OF_DAY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currentMinute;
    }

    public int getCurrentDay() {
        int currentMinute = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentMinute = now.get(Calendar.DAY_OF_WEEK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currentMinute;
    }
}
