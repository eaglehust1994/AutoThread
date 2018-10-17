/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.qll.task;


import com.viettel.framework.service.utils.DateTimeUtils;
import com.viettel.mmserver.base.ProcessThreadMX;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pm1_os38
 */
public class ProcessThread extends ProcessThreadMX {

//    LocalDate date = LocalDate.now();
//    java.sql.Date sqlDate = java.sql.Date.valueOf(date);

    MyDbSql db;
    private long interval;
    private long maxBatchSize;
    private boolean useFixedHour;
    private long minuteRun;
    private long hour;
    int port;
    String database = "";

    public ProcessThread(long interval, long maxBatchSize, long hour, long minute) throws Exception {
        super("");
        this.interval = interval;
        this.maxBatchSize = maxBatchSize;
        this.hour = hour;
        this.database = ProcessManager.database;
//        String MbeanName = "process:name=SynTempFromTKTUToBikt";
        this.useFixedHour = (0 <= hour && hour <= 23) ? true : false;
        this.minuteRun = (0 <= minute && minute <= 59) ? minute : 0;
//        registerAgent(MbeanName);
    }

    @Override
    protected void process() {
        //to do
        logger.info("=======================================================");
        logger.info("*****BEGIN GET SYNCHRONOUS FROM KTTS*****");
        logger.info("=======================================================");
        MyDbSql myDbTask = new MyDbSql();
        long startTime = System.currentTimeMillis();
        String dayRemoveMer = DateTimeUtils.format(DateTimeUtils.add(new Date(), ProcessManager.dayRemoveMer), "dd/MM/yyyy");

        boolean bRunning = false;
        try {
     
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (bRunning) {
                try {
                    long endTime = System.currentTimeMillis();
                    long processTime = endTime - startTime;
                    if (interval != 0) {
                        processTime %= interval;
                    }
                    logger.info("Thread syn from WS TKTU sleep " + this.threadName + " sleep " + (interval - processTime) / 1000 + " seconds");
                    Thread.sleep(interval - processTime);
                } catch (InterruptedException ex) {
                    logger.error(ex.getMessage(), ex);
                    Logger.getLogger(ProcessThread.class.getName()).log(Level.SEVERE, null, ex);
                }
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
