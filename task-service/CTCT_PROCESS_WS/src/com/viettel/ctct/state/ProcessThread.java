/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.state;

import com.viettel.framework.service.utils.DateTimeUtils;
import com.viettel.mmserver.base.ProcessThreadMX;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

//  @author hoanm1
public class ProcessThread extends ProcessThreadMX {

    MyDbTask db;
    private long interval;
    private long maxBatchSize;
    private boolean useFixedHour;
    private long minuteRun;
    private long hour;
    String urlStr = "";
    int port;
    String namespace = "";
    String localPart = "";
    String username = "";
    String password = "";
    String database = "";

    /**
     *
     * @throws Exception
     */
    public ProcessThread(LogServerObject objWs, long interval, long maxBatchSize, long hour, long minute) throws Exception {
        super("Synchronous from tktu to bikt");
        this.interval = interval;
        this.maxBatchSize = maxBatchSize;
        urlStr = objWs.getUrl();
        namespace = objWs.getNamespace();
        localPart = objWs.getLocalPart();
        username = objWs.getUsername();
        password = objWs.getPassword();
        this.hour = hour;
        this.database = ProcessManager.database;
        String MbeanName = "process:name=SynTempFromTKTUToBikt";
        this.useFixedHour = (0 <= hour && hour <= 23) ? true : false;
        this.minuteRun = (0 <= minute && minute <= 59) ? minute : 0;
        registerAgent(MbeanName);
    }

    @Override
    protected void process() {
        //to do
        logger.info("=======================================================");
        logger.info("*****BEGIN GET SYNCHRONOUS FROM KTTS*****");
        logger.info("=======================================================");
        MyDbTask myDbTask = new MyDbTask();
        long startTime = System.currentTimeMillis();
        Date startDateContract = DateTimeUtils.add(new Date(), ProcessManager.startDateContract);
        Date startDateConstruction = DateTimeUtils.add(new Date(), ProcessManager.startDateConstruction);
        Date startDateDebt = DateTimeUtils.add(new Date(), ProcessManager.startDateDebt);
        boolean bRunning = false;
        try {
            if (!useFixedHour) {
                UpdateProcess(myDbTask, startDateContract,startDateConstruction,startDateDebt);
                bRunning = true;
            } else {
                if (getCurrentHour() == hour) {
                    //Kiểm tra phút
                    long currentMinute = getCurrentMinute();
                    if (currentMinute >= minuteRun) {
                        UpdateProcess(myDbTask, startDateContract,startDateConstruction,startDateDebt);
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

    private void UpdateProcess(MyDbTask db, Date startDateContract,Date startDateConstruction,Date startDateDebt) throws Exception {
        try {
            logger.info("Bat dau update trang thai hop dong...");
            db.updateStateDueContract(startDateContract, logger);
            db.updateStateOverDueContract(startDateContract, logger);
            logger.info("Ket thuc update trang thai hop dong...");
            logger.info("Bat dau update trang thai cong trinh...");
            db.updateStateDueConstruction(startDateConstruction, logger);
            db.updateStateOverDueConstruction(startDateConstruction, logger);
            logger.info("Ket thuc update trang thai cong trinh...");
            logger.info("Bat dau update trang thai cong viec...");
            db.updateStateDueConstructionTask(startDateConstruction, logger);
            db.updateStateOverDueConstructionTask(startDateConstruction, logger);
            logger.info("Ket thuc update trang thai cong viec...");
            logger.info("Bat dau lay du lieu cong no...");
            ArrayList<StockTotalForm> getlistProbType = db.getListDebt(startDateDebt);
            logger.info("Ket thuc lay du lieu cong no...");
            logger.info("Bat dau xoa du lieu cong no...");
            db.deleteStockTotal(logger, maxBatchSize);
            logger.info("Ket thuc xoa du lieu cong no...");
            logger.info("Bat dau them du lieu cong no...");
            db.insertStockTotal(getlistProbType, logger, maxBatchSize);
            logger.info("Ket thuc them du lieu cong no...");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("Sleep 10s to try connect WS KTTS....");
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
