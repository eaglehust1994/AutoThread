/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.quantityByDate;

import com.viettel.framework.service.common.DataConfig;
import com.viettel.mmserver.agent.MmJMXServerSec;
import java.util.ArrayList;
import org.apache.log4j.Logger;

//  @author hoanm1
public class ProcessManager {

    private static Logger logger = Logger.getLogger(ProcessManager.class);
    protected static String pro = "../conf/program.conf";
    public static String database;
    public static long interval;
    public static long maxBatchSize;
    public static int requestTimeout;
    public static int connectTimeout;
    public static int startDateContract;
    public static int startDateConstruction;
    public static int startDateDebt;
    public static int backDay;
    public static long hour; //Gio dau tien tien trinh duoc phep chay
    public static long minute;//Gio cuoi cung tien trinh duoc phep chay
    static ArrayList<ProcessThread> processList;

    public static void start() {
        try {
            DataConfig.init(pro);
            logger.info("*******************HA3 - doStart*********************");
            //giao thuc web service
            ProcessThread process;
            processList = new ArrayList<ProcessThread>();
            //doc thong tin cau hinh
            interval = DataConfig.getLongProperties("interval", 3600 * 24);
            startDateContract = DataConfig.getIntProperties("startDateContract", -1);
            startDateConstruction = DataConfig.getIntProperties("startDateConstruction", -1);
            startDateDebt = DataConfig.getIntProperties("startDateDebt", -1);
            maxBatchSize = DataConfig.getLongProperties("maxBatchSize", 1000);
            connectTimeout = DataConfig.getIntProperties("connectTimeout", 5 * 60000); //Default 5 minutes
            requestTimeout = DataConfig.getIntProperties("requestTimeout", 10 * 60000); //Default 10 minutes
            hour = DataConfig.getLongProperties("hour", -1); 
            minute = DataConfig.getLongProperties("minute", 0); 
            database = DataConfig.getStringProperties("database", "../conf/database.conf");

            //doc thong tin ws
            LogServerObject objWs = new LogServerObject();
            objWs.setLocalPart(DataConfig.getStringProperties("localPart"));
            objWs.setNamespace(DataConfig.getStringProperties("namespace"));
            objWs.setUrl(DataConfig.getStringProperties("url"));
            objWs.setUsername(DataConfig.getStringProperties("username"));
            objWs.setPassword(DataConfig.getStringProperties("password"));

            process = new ProcessThread(objWs, interval, maxBatchSize,hour,minute);
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
                ProcessThread process = processList.get(i);
                process.stop();
            }
        }
    }
}
