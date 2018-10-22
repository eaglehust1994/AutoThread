/*
 * Copyright YYYY Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.SendEmailProcess;

import com.viettel.kpi.common.utils.DataConfig;
import com.viettel.mmserver.agent.MmJMXServerSec;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class ProcessManager {

    public static String database;
    public static long interval;
    public static String processId;
    public static long hour;
    protected static String pro = "../conf/program.conf";
    static ArrayList<ProccessThreadService> synProcessList;
    //Socket
    public static int port;
    public static int threadMax;
    public static int maxBatchSize;
    public static int numberEnd;
    public static Logger log;

    public static void start() {
        try {

//            ProccessThreadService synProcess;
            synProcessList = new ArrayList<ProccessThreadService>();
            DataConfig.init(pro);
            port = DataConfig.getIntProperties("port", 9997);
            threadMax = DataConfig.getIntProperties("threadMax");
            maxBatchSize = DataConfig.getIntProperties("maxBatchSize", 500);
            //ĐIỀN TYPE EMAIL VÀO TRƯỜNG TRUYỀN VÀO TYPE_EMAIL
            //synProcess = new ProccessThreadService("traffic2G");
            //  synProcessList.add(synProcess);
            //  synProcess.run();
            
            SocketServer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //--------------------------------------------------------------------------
    /**
     * Stop tien trinh
     *
     * @author: TienBV2
     * @date: 30/11/2012
     * @param: N/A
     * @return:N/A
     * @throws: N/A
     */
    public static void stop() {
        MmJMXServerSec.getInstance().stop();
        if (synProcessList != null && synProcessList.size() > 0) {
            for (int i = 0; i < synProcessList.size(); i++) {
                ProccessThreadService process = synProcessList.get(i);
            }
        }
    }
}
