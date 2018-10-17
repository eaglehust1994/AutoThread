/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.reportDailyRemain;

import com.viettel.ctct.reportDailyRemain.MyDbTask;
import com.viettel.ctct.reportDailyRemain.ProcessManager;
import com.viettel.framework.service.utils.DateTimeUtils;
import com.viettel.mmserver.base.ProcessThreadMX;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hoangnh38
 */
public class ProcessThread extends ProcessThreadMX{
    

//    LocalDate date = LocalDate.now();
//    java.sql.Date sqlDate = java.sql.Date.valueOf(date);

    MyDbTask db;
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
        MyDbTask myDbTask = new MyDbTask();
        long startTime = System.currentTimeMillis();
        String startDate = DateTimeUtils.format(DateTimeUtils.add(new Date(), ProcessManager.dateConfigReport), "dd/MM/yyyy");
        String remainDate = DateTimeUtils.format(DateTimeUtils.add(new Date(), ProcessManager.dateConfigReport - 1), "dd/MM/yyyy");
        int backDate = ProcessManager.dateConfigReport;

        boolean bRunning = false;
        try {
            if (!useFixedHour) {
                UpdateProcess(myDbTask,startDate,backDate,remainDate);
                bRunning = true;
            } else {
                if (getCurrentHour() == hour) {
                    //Kiểm tra phút
                    long currentMinute = getCurrentMinute();
                    if (currentMinute >= minuteRun) {
                        UpdateProcess(myDbTask,startDate,backDate,remainDate);
                        bRunning = true;
                    } else {
                        //Nghỉ đến phút chạy
                        long sleepTime = (minuteRun - currentMinute) * 60 * 1000;
                        logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                        Thread.sleep(sleepTime);
                    }
                } else {
                    long currentMinute = getCurrentMinute();
                    //Nghỉ hết giờ hiện tại
                    long sleepTime = (60 - currentMinute) * 60 * 1000;
                    logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                    Thread.sleep(sleepTime);
                }
            }
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
                    Logger.getLogger(com.viettel.ctct.report.ProcessThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void UpdateProcess(MyDbTask db, String startDate, int backDate,String remainDate) throws Exception {
        try {
            ArrayList<StockDailyRemainBO> getlistProbType = db.getListStock(startDate,remainDate);
            logger.info("Bat dau xoa du lieu trung ton kho...");
            db.deleteStockRemain(startDate,logger, maxBatchSize);
            logger.info("Ket thuc xoa du lieu trung ton kho...");
            logger.info("Bat dau them du lieu ton kho...");
            db.insertStockRemain(getlistProbType,backDate, logger, maxBatchSize);
            logger.info("Ket thuc them du lieu ton kho...");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("Sleep 10s to try connect WS....");
            Thread.sleep(10000);
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
