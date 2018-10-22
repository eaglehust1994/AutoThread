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
public class ProcessThreadDaily extends ProcessThreadMX {

    private LogServer logServer;
    private Job job;
    private boolean useFixedHour;
    private long interval;
    private long minuteRun = 0;

    public ProcessThreadDaily(LogServer logServer, Job job, long hour) throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        super("Service." + logServer.getLogServerId());
        this.logServer = logServer;
        this.job = job;
        this.job.setLogger(logger);
        this.job.setHour(hour);
        this.useFixedHour = (0 <= hour && hour <= 23) ? true : false;
        this.interval = Time.ONE_HOUR;
        String MbeanName = "process:name=" + logServer.getLogServerId();
        registerAgent(MbeanName);
    }

    public ProcessThreadDaily(LogServer logServer, Job job, long hourRun, long minuteRun) throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        super("Service." + logServer.getLogServerId());
        this.logServer = logServer;
        this.job = job;
        this.job.setLogger(logger);
        this.job.setHour(hourRun);
        this.useFixedHour = (0 <= hourRun && hourRun <= 23) ? true : false;
        this.interval = Time.ONE_HOUR;
        this.minuteRun = (0 <= minuteRun && minuteRun <= 59) ? minuteRun : 0 ;
        String MbeanName = "process:name=" + logServer.getLogServerId();
        registerAgent(MbeanName);
    }

    @Override
    protected void process() {
        buStartTime = new Date();
        long startTime = System.currentTimeMillis();
        logger.info("\r\n\r\n***** Begin Process *****");
        boolean bRunning = false;
        try {
            if (!useFixedHour && getCurrentHour()>=4){
                job.setLostData(true);
                job.run(logServer, logger);
                bRunning = true;
            }else{
                if(job.getHour() == getCurrentHour()){
                    //Kiểm tra phút
                    long currentMinute = getCurrentMinute();
                    if(currentMinute>=minuteRun){
                        job.setLostData(true);
                        job.run(logServer, logger);
                        bRunning = true;
                    } else{
                        //Nghỉ đến phút chạy
                        long sleepTime = (minuteRun-currentMinute) * 60 * 1000;
                        logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                        buStartTime = null;
                        Thread.sleep(sleepTime);                        
                    }
                }else{
                    long currentMinute = getCurrentMinute();
                    long sleepTime = 0;
                    if(currentMinute>minuteRun){
                        //Nghỉ hết giờ hiện tại
                        sleepTime = (60-currentMinute) * 60 * 1000;
                    } else if(currentMinute<minuteRun){
                        //Nghỉ hết giờ hiện tại
                        sleepTime = (minuteRun-currentMinute) * 60 * 1000;
                    } else{
                        //Nghi tu 0h -> gio bat dau chay, khong chay bu
                        if(job.lostData && getCurrentHour()>=job.getHour()){
                            job.setLostData(true);
                            job.run(logServer, logger);
                            bRunning = true;
                        }else{
                            //Nghỉ hết giờ, chờ đến chạy
                            sleepTime = interval;
                        }
                    }
                    if(!bRunning){
                        logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                        buStartTime = null;
                        Thread.sleep(sleepTime);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("process: " + ex.getMessage());
        } finally {
        }
        //Thực hiện xong, sleep 1h
        buStartTime = null;
        if(bRunning){
            long endTime = System.currentTimeMillis();
            try {
                long processTime = endTime - startTime;
                if (interval != 0) {
                    processTime %= interval;
                }
                buStartTime = null;
                logger.info("Thread " + this.threadName + " sleep " + (this.interval - processTime) / 1000 + " seconds");
                Thread.sleep(this.interval - processTime);
            } catch (InterruptedException e) {
            }
        }
        
    }

    public int getCurrentHour() {
        int currentHour = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentHour = now.get(Calendar.HOUR_OF_DAY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currentHour;
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
