/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.scheduler;

import com.viettel.framework.service.common.DataConfig;
import com.viettel.framework.service.manager.ServiceManager;
import com.viettel.framework.service.common.LogServer;
import com.viettel.framework.service.manager.Job;
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
    private static final String CONFIG_FILE = "../conf/scheduler.program.conf";
    private static final int THREAD_SIZE = 1;
    public static String vendor;
    public static int serverTypeId;
    public static String dbFile;
    public static long hour;
    public static long minute;
    public static String fileJar;

    public static void main(String[] args) {
        try {
            DataConfig.init(CONFIG_FILE);
            vendor = DataConfig.getStringProperties("vendor");
            serverTypeId = DataConfig.getIntProperties("serverTypeId");
            dbFile = DataConfig.getStringProperties("dbFile");
            hour = DataConfig.getLongProperties("hour", -1);
            minute = DataConfig.getLongProperties("minute", 0);
            fileJar = DataConfig.getStringProperties("fileJar");
            
            ArrayList<LogServer> serverList = new ArrayList<LogServer>();
            LogServer mainThreadServer = new LogServer();
            mainThreadServer.setLogServerId("SCHEDULER_THREAD");
            mainThreadServer.setName("SCHEDULER_THREAD");
            serverList.add(mainThreadServer);

            ArrayList<Job> myJobList = new ArrayList<Job>();
            for (int i = 0; i < serverList.size(); i++) {
                myJobList.add(new MyJob());
            }
            ServiceManager.startDaily(serverList, myJobList, hour, minute);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}