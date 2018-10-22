/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.config;

import com.viettel.kpi.common.utils.DataConfig;
import com.viettel.kpi.service.common.LogServer;
import com.viettel.kpi.service.manager.ServiceManager;
import java.util.ArrayList;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author qlmvt_MinhHT1
 */
public class Start {

    static {
        try {
            PropertyConfigurator.configure("../etc/log4j.cfg");
        } catch (Exception ex) {
        }
    }
    /**
     * @param args the command line arguments
     */
    private static final String CONFIG_FILE = "../conf/program.conf";
    private static final int THREAD_SIZE = 1;
    //dungvv8_check_log_06012016
    public static String logQueryId;
    public static String vendor;
    public static String serviceTypeCode;
    public static long maxBatchSize;
    public static int maxThreadSize;
    public static int number_hour_get_data = 2;// Giờ lớn nhất khi chia ra
    private static long interval;
    public static String dbFileCommon;
    public static String dbFileAccess2G;
    public static String dbFileAccess3G;
    public static String dbFileVasIn;
    public static String dbFileIsp;
    public static String dbFileTrans;
    public static String dbFileInoc;
    public static String dbFileRoaming;
    public static String dbFileNss;
    public static int threadSizeInUsed = 0;
    public static int serviceTypeId;
//    public static HashMap<Long, Long> mapRunning = new HashMap<Long, Long>();
    public static String dbFileTkvl;
    public static boolean isDeleteData = false;

    public static void main(String[] args) {
        try {
//            String timeZone = DataConfig.getStringProperties("TimeZone");
//            if(timeZone != null && !"".equals(timeZone.trim())) {
//                TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
//            }
//            mapRunning = new HashMap<Long, Long>();
            DataConfig.init(CONFIG_FILE);
            //dungvv8_check_log_06012016
            logQueryId = DataConfig.getStringProperties("logQueryId");
            serviceTypeCode = DataConfig.getStringProperties("serviceTypeCode");
            maxBatchSize = DataConfig.getLongProperties("maxBatchSize", 1000);
            number_hour_get_data = DataConfig.getIntProperties("number_hour_get_data", 2);
            interval = DataConfig.getLongProperties("interval", 2 * 60 * 1000);
            dbFileCommon = DataConfig.getStringProperties("dbFileCommon");
            dbFileAccess2G = DataConfig.getStringProperties("dbFileAccess2G");
            dbFileAccess3G = DataConfig.getStringProperties("dbFileAccess3G");
            dbFileVasIn = DataConfig.getStringProperties("dbFileVasIn");
            dbFileIsp = DataConfig.getStringProperties("dbFileIsp");
            dbFileTrans = DataConfig.getStringProperties("dbFileTrans");
            dbFileRoaming = DataConfig.getStringProperties("dbFileRoaming");
            dbFileTkvl = DataConfig.getStringProperties("dbFileTkvl");
            dbFileInoc = DataConfig.getStringProperties("dbFileInoc");
            dbFileNss = DataConfig.getStringProperties("dbFileNss");
            maxThreadSize = DataConfig.getIntProperties("maxThreadSize");
            isDeleteData = DataConfig.getBoolProperties("isDeleteData", false);
            MyDbTask db = new MyDbTask();
            MyJob myJob = new MyJob();
            ArrayList<LogServer> serverList = new ArrayList<LogServer>();
            LogServer logServer = new LogServer();
            logServer.setLogServerId("Main Thread Service");
            logServer.setName("Main Thread Service");
            serverList.add(logServer);
//            db.init(dbFileCommon);
            db.resetIsRunning(serviceTypeCode);
//            db.close();
//            ArrayList<Job> myJobList = new ArrayList<Job>();
//            for (int i = 0; i < serverList.size(); i++) {
//                myJobList.add(new MyJob());
//            }
            ServiceManager.start(serverList, myJob, interval);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static synchronized void decreaseThreadSizeUsed() {
        threadSizeInUsed--;
    }

    static synchronized void increaseThreadSizeUsed() {
        threadSizeInUsed++;
    }
}
