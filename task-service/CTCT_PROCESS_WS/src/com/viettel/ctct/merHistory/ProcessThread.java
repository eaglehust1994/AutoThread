/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.merHistory;

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
        String dayRemoveMer = DateTimeUtils.format(DateTimeUtils.add(new Date(), ProcessManager.dayRemoveMer), "dd/MM/yyyy");

        boolean bRunning = false;
        try {
            if (!useFixedHour) {
                UpdateProcess(myDbTask,dayRemoveMer);
                bRunning = true;
            } else {
                if (getCurrentHour() == hour) {
                    //Kiểm tra phút
                    long currentMinute = getCurrentMinute();
                    if (currentMinute >= minuteRun) {
                        UpdateProcess(myDbTask,dayRemoveMer);
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
                    Logger.getLogger(ProcessThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void UpdateProcess(MyDbTask db, String dayRemoveMer) throws Exception {
        try {
            logger.info("Tim kiem ban ghi trong merEntity");
            ArrayList<MerHistoryBO> getListMerEntity = db.getListMerEntity();
            logger.info("Lay ra duoc" + getListMerEntity + "ban ghi");
            logger.info("Bat dau xoa du lieu merentity history...");
            db.deleteMerHistory(dayRemoveMer,logger, maxBatchSize);
            logger.info("Ket thuc xoa du lieu merentity history...");
            logger.info("Bat dau them du lieu merentity history...");
            db.insertMerHistory(getListMerEntity, logger, maxBatchSize);
            logger.info("Ket thuc them du lieu merentity history...");
            
            logger.info("Tim kiem ban ghi trong stockGoodsTotal");
            ArrayList<StockGoodsTotalBO> getListStockGoodsTotal = db.getListStockGoodsTotal();
            logger.info("Lay ra duoc" + getListStockGoodsTotal + "ban ghi");
            logger.info("Bat dau xoa du lieu merentity history...");
            db.deleteStockGoodsTotalHistory(dayRemoveMer,logger, maxBatchSize);
            logger.info("Ket thuc xoa du lieu merentity history...");
            logger.info("Bat dau them du lieu merentity history...");
            db.insertStockGoodsTotalHistory(getListStockGoodsTotal, logger, maxBatchSize);
            logger.info("Ket thuc them du lieu merentity history...");
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
