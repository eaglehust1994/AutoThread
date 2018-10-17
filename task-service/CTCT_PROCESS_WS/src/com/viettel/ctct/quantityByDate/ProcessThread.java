/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.quantityByDate;

import com.viettel.framework.service.utils.DateTimeUtils;
import com.viettel.mmserver.base.ProcessThreadMX;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
        boolean bRunning = false;
        try {
            if (!useFixedHour) {
                UpdateProcess(myDbTask);
                bRunning = true;
            } else {
//                check ngay 1 run
                Date now = new Date(System.currentTimeMillis());
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                if (getCurrentHour() == hour && day == 1) {
                    //Kiểm tra phút
                    long currentMinute = getCurrentMinute();
                    if (currentMinute >= minuteRun) {
                        UpdateProcess(myDbTask);
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

    private void UpdateProcess(MyDbTask db) throws Exception {
        try {
            logger.info("Bat dau lay data quantity by date...");
            List<ConstructionTaskDailyDTO> lstPast = db.doSearchInPast(logger);
            List<ConstructionTaskDailyDTO> lstCT = new ArrayList<>();
            db.updateConfirm(lstPast, logger);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lstPast.size(); i++) {
                if (sb.indexOf(lstPast.get(i).getConstructionTaskId().toString()) < 0) {
                    sb.append(lstPast.get(i).getConstructionTaskId() + ",");
                    lstCT.add(lstPast.get(i));
                }                
            }
            for (int i = 0; i < lstCT.size(); i++) {
                db.updateConstructionTask(lstCT.get(i), logger);
            }
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
