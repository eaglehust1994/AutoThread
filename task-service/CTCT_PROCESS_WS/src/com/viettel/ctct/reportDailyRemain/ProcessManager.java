/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.reportDailyRemain;

import com.viettel.framework.service.common.DataConfig;
import com.viettel.mmserver.agent.MmJMXServerSec;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author hoangnh38
 */
public class ProcessManager {
    
    private static Logger logger = Logger.getLogger(com.viettel.ctct.reportDailyRemain.ProcessManager.class);
    protected static String pro = "../conf/program.conf";
    public static String database;
    public static long interval;
    public static long maxBatchSize;
    public static int requestTimeout;
    public static int connectTimeout;
    public static int startDateContract;
    public static int backDay;
    public static int backDayReport;
    public static int backDayRemain;
    public static int backDateIe;
    public static int backDateRemain;
    public static int dateConfigReport;
    public static long hour; //Gio dau tien tien trinh duoc phep chay
    public static long minute;//Gio cuoi cung tien trinh duoc phep chay
    static ArrayList<com.viettel.ctct.reportDailyRemain.ProcessThread> processList;
    
    public static void start() {
        try {
            DataConfig.init(pro);
            logger.info("*******************HA3 - doStart*********************");
            //giao thuc web service
            com.viettel.ctct.reportDailyRemain.ProcessThread process;
            processList = new ArrayList<com.viettel.ctct.reportDailyRemain.ProcessThread>();
            //doc thong tin cau hinh
            interval = DataConfig.getLongProperties("interval", 3600 * 24);
            startDateContract = DataConfig.getIntProperties("startDateContract", -1);
            maxBatchSize = DataConfig.getLongProperties("maxBatchSize", 1000);
            connectTimeout = DataConfig.getIntProperties("connectTimeout", 5 * 60000); //Default 5 minutes
            requestTimeout = DataConfig.getIntProperties("requestTimeout", 10 * 60000); //Default 10 minutes
            hour = DataConfig.getLongProperties("hour", -1); 
            minute = DataConfig.getLongProperties("minute", 0); 
            backDay = DataConfig.getIntProperties("backDay", 0); 
            backDayReport = DataConfig.getIntProperties("backDayReport"); 
            backDayRemain = DataConfig.getIntProperties("backDayRemain", 0); 
            backDateIe = DataConfig.getIntProperties("backDateIe", 0); 
            backDateRemain = DataConfig.getIntProperties("backDateRemain", 0);
            dateConfigReport = DataConfig.getIntProperties("dateConfigReport", 0);
            database = DataConfig.getStringProperties("database", "../conf/database.conf");

            process = new com.viettel.ctct.reportDailyRemain.ProcessThread( interval, maxBatchSize,hour,minute);
            processList.add(process);
            process.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void stop() {
        try {
            MmJMXServerSec.getInstance().stop();
        } catch (Exception e) {
        }
        if (processList != null && processList.size() > 0) {
            for (int i = 0; i < processList.size(); i++) {
                com.viettel.ctct.reportDailyRemain.ProcessThread process = processList.get(i);
                process.stop();
            }
        }
    }
}
