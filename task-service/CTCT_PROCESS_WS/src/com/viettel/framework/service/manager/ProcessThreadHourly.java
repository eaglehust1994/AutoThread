/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.service.manager;

import com.viettel.framework.service.common.LogServer;
import com.viettel.framework.service.manager.Constants.Time;
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
public class ProcessThreadHourly extends ProcessThreadMX {

    private LogServer logServer;
    private Job job;
    private boolean useFixedMinute;

    public ProcessThreadHourly(LogServer logServer, Job job, long minute) throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        super("Service." + logServer.getLogServerId());
        this.logServer = logServer;
        this.job = job;
        this.job.setLogger(logger);
        this.job.setMinute(minute);
        this.job.setInterval(Time.ONE_MINUTE);
        this.useFixedMinute = (0 <= minute && minute <= 59) ? true : false;
        String MbeanName = "process:name=" + logServer.getLogServerId();
        registerAgent(MbeanName);
    }

    @Override
    protected void process() {
        buStartTime = new Date();
        long startTime = System.currentTimeMillis();
        logger.info("\r\n\r\n***** Begin Process *****");
        logger.info("minute: " + job.getMinute());
        logger.info("useFixedMinute: " + useFixedMinute);
        try {
            if ((!useFixedMinute) || (getCurrentMinute() >= job.getMinute())) {
                job.run(logServer, logger);
                this.job.setInterval(Time.ONE_HOUR);
            } else {
                this.job.setInterval(Time.FIVE_MINUTE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("process: " + ex.getMessage());
        } finally {
        }
        buStartTime=null;
        long interval = this.job.getInterval();        
        long endTime = System.currentTimeMillis();
        try {
            long processTime = endTime - startTime;
            if (interval != 0) {
                processTime %= interval;
            }
            logger.info("Thread " + this.threadName + " sleep " + (interval - processTime) / 1000 + " seconds");
            buStartTime = null;
            Thread.sleep(interval - processTime);
        } catch (InterruptedException e) {
        }

    }

    public int getCurrentMinute() {
        int currentMinute = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentMinute = now.get(Calendar.MINUTE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currentMinute;
    }
}
