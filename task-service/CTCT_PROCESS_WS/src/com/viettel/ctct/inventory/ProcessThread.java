/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.inventory;

import static com.viettel.ctct.inventory.ProcessManager.backDateIe;
import com.viettel.framework.service.utils.DateTimeUtils;
import com.viettel.mmserver.base.ProcessThreadMX;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
        Date startDateContract = DateTimeUtils.add(new Date(), ProcessManager.startDateContract);
        String startDate = DateTimeUtils.format(DateTimeUtils.add(new Date(), ProcessManager.backDay), "dd/MM/yyyy");
        int backDate = ProcessManager.backDay;

        boolean bRunning = false;
        try {
            if (!useFixedHour) {
                UpdateProcess(myDbTask, startDateContract,startDate,backDate);
                bRunning = true;
            } else {
                if (getCurrentHour() == hour) {
                    //Kiểm tra phút
                    long currentMinute = getCurrentMinute();
                    if (currentMinute >= minuteRun) {
                        UpdateProcess(myDbTask, startDateContract,startDate,backDate);
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

    private void UpdateProcess(MyDbTask db, Date startDateContract,String startDate,int backDate) throws Exception {
        try {
            ArrayList<StockDailyRemainObject> getlistProbType = db.getListStock();
            logger.info("Bat dau xoa du lieu trung ton kho...");
            db.deleteStockRemain(startDate,logger, maxBatchSize);
            logger.info("Ket thuc xoa du lieu trung ton kho...");
            logger.info("Bat dau them du lieu ton kho...");
            db.insertStockRemain(getlistProbType,backDate, logger, maxBatchSize);
            logger.info("Ket thuc them du lieu ton kho...");
            
            ArrayList<StockDailyImExObject> getlistStockImEx = db.getListStockImEx(startDate);
            logger.info("Bat dau xoa du lieu trung nhap xuat kho...");
            db.deleteStockDailyImEx(startDate,logger, maxBatchSize);
            logger.info("Ket thuc xoa du lieu trung nhap xuat kho...");
            logger.info("Bat dau them du lieu nhap xuat kho...");
            db.insertStockImEx(getlistStockImEx,backDate, logger, maxBatchSize);
            logger.info("Ket thuc them du lieu nhap xuat kho...");
            
            ArrayList<StockGoodsTotalRes> getListStockRes = db.getListStockRes();
            logger.info("Bat dau xoa du lieu kho...");
            db.deleteStockResponse(logger, maxBatchSize);
            logger.info("Ket thuc xoa du lieu kho...");
            logger.info("Bat dau them du lieu kho...");
            db.insertStockResponse(getListStockRes, logger, maxBatchSize);
            logger.info("Ket thuc them du lieu kho...");
            
            logger.info("Bat dau update truong INROAL =1...");
            db.updateInRoal(logger);
            logger.info("Ket thuc update truong INROAL =1....");
            
            logger.info("Bat dau update truong INROAL = null...");
            db.updateInRoalNull(logger);
            logger.info("Ket thuc update truong INROAL = null....");
            
            logger.info("Bat dau update truong INROAL =1 TH2 ...");
            db.updateInRoalTH2(logger);
            logger.info("Ket thuc update truong INROAL =1 TH2....");
            
            logger.info("Bat dau update truong INROAL = null TH2...");
            db.updateInRoalNullTH2(logger);
            logger.info("Ket thuc update truong INROAL = null TH2....");

            logger.info("Bat dau select test...");
            List<Integer> lstStock = db.selectAllStockFromKPIStorage(startDateContract, logger);
            logger.info("Bat dau lấy dữ liệu từ bảng MER_ENTITY theo status =1, ...");

            //KPI theo số lượng
            List<StockGoodsTotalBO> lstCheck = new ArrayList<>();
            for (int i = 0; i < lstStock.size(); i++) {
                logger.info("Bat dau xoa du lieu cong no...");
                List<StockGoodsTotalBO> lstCheck1 = new ArrayList();
                lstCheck1 = db.checkAmountQuota( logger, lstStock.get(i));
                for (int j = 0; j < lstCheck1.size(); j++) {
                    if (lstCheck1.get(j).getAmountQuota() - lstCheck1.get(j).getAmountRemain() >= (double) 0) {
                        lstCheck1.remove(lstCheck1.get(j));
                        j--;
                    }
                }
                lstCheck.addAll(lstCheck1);
            }
            logger.info("Bat dau xoa du lieu kpi...");
            db.deleteKpiStorageAmount(logger);

            logger.info("Ket thuc xoa du lieu kpi...");
            logger.info("Bat dau them du lieu cong no...");
            db.insertStockTotal(lstCheck, logger, maxBatchSize, startDateContract);
            logger.info("Ket thuc them du lieu cong no...");

            //KPI theo thời gian
            List<MerEntityBO> lstMer = db.selectAllStockFromMer(startDateContract, logger);
            List<Integer> timeQuota = db.selectKpiQuotaApp(buStartTime, logger);

            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            Date dayCurrent = new Date();
            dayCurrent = sdf.parse(sdf.format(dayCurrent));
            for (int k = 0; k < lstMer.size(); k++) {
                int count = (int) (timeQuota.get(0) - (dayCurrent.getTime() - lstMer.get(k).getImportDate().getTime()) / (1000 * 60 * 60 * 24));
                if (count >= 0) {
                    lstMer.remove(lstMer.get(k));
                    k--;
                }
            }
            logger.info("Ket thuc xoa du lieu kpi theo thời gian...");
            db.deleteKpiStorageTime(logger);
            logger.info("Bat dau them du lieu kpi theo thời gian...");
            if (lstMer.size() > 0) {
                db.insertKpiStorageTime(lstMer, logger, maxBatchSize, buStartTime, timeQuota.get(0));
            }
            logger.info("Ket thuc them du lieu kpi theo thời gian...");
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
