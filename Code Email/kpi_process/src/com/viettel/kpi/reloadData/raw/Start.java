package com.viettel.kpi.reloadData.raw;

import com.viettel.kpi.common.utils.DataConfig;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Khiemvk
 */
public class Start {

    static {
        try {
            PropertyConfigurator.configure("../etc/log4j.cfg");
        } catch (Exception ex) {
        }
    }
    private static final String CONFIG_FILE = "../conf/program.conf";
    //dungvv8_check_log_06012016
    public static String logQueryId;
    public static String dbFileCommon;
    public static String dbFileAccess2G;
    public static String dbFileAccess3G;
    public static String dbFileVasIn;
    public static String dbFileIsp;
    public static String dbFileTrans;
    public static String dbFileInoc;
    public static String dbFileRoaming;
    public static String dbFileNss;
    public static int port = 9999;
    public static int threadMax = 1;
    public static int numberUserCallMax = 3;
    public static int maxBatchSize = 1000;
    public static int number_hour_get_data;
    public static int threadSizeInUsed = 0;

    public static void main(String arg[]) {
        try {
            DataConfig.init(CONFIG_FILE);
            //dungvv8_check_log_06012016
            logQueryId = DataConfig.getStringProperties("logQueryId");
            dbFileAccess2G = DataConfig.getStringProperties("dbFileAccess2G");
            dbFileAccess3G = DataConfig.getStringProperties("dbFileAccess3G");
            dbFileInoc = DataConfig.getStringProperties("dbFileINOC");
            dbFileRoaming = DataConfig.getStringProperties("dbFileRoaming");
            dbFileCommon = DataConfig.getStringProperties("dbFileCommon");
            dbFileVasIn = DataConfig.getStringProperties("dbFileVasIn");
            dbFileIsp = DataConfig.getStringProperties("dbFileIsp");
            dbFileTrans = DataConfig.getStringProperties("dbFileTrans");
            dbFileInoc = DataConfig.getStringProperties("dbFileInoc");
            dbFileNss = DataConfig.getStringProperties("dbFileNss");
            port = DataConfig.getIntProperties("port", 9008);
            threadMax = DataConfig.getIntProperties("threadMax", 10);
            numberUserCallMax = DataConfig.getIntProperties("numberUserCallMax", 10);
            number_hour_get_data = DataConfig.getIntProperties("number_hour_get_data", 2);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SocketServer.start();
    }

    public static synchronized void decreaseThreadSizeUsed() {
        threadSizeInUsed--;
    }

    public static synchronized void increaseThreadSizeUsed() {
        threadSizeInUsed++;
    }
}
