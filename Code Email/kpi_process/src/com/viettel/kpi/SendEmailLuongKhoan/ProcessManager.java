/*
 * Copyright YYYY Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.SendEmailLuongKhoan;

import com.viettel.haframework.fw.ZkProcess;
import com.viettel.kpi.common.utils.DataConfig;
import com.viettel.mmserver.agent.MmJMXServerSec;
import java.util.ArrayList;

/**
 *
 * @author dungvv8
 */
public class ProcessManager extends ZkProcess {

    public static long interval;
    public static long hour; //Gio dau tien tien trinh duoc phep chay
    protected static String pro = "../conf/program.conf";
    static ArrayList<ProccessThreadService> synProcessList;
    //export
    public static String ipReloadData;
    public static int portReloadData;
    public static String pathFile;
    private static ProcessManager manager;

    public static void startManager() {
        try {
            ProccessThreadService synProcess;
            synProcessList = new ArrayList<ProccessThreadService>();
            DataConfig.init(pro);
            System.out.println(pro);
            interval = DataConfig.getLongProperties("interval", 120000L);
            hour = DataConfig.getLongProperties("hour", -1); 
            ipReloadData = DataConfig.getStringProperties("ipReloadData");
            portReloadData = DataConfig.getIntProperties("portReloadData");
            pathFile = DataConfig.getStringProperties("LOCAL_DIR");
            synProcess = new ProccessThreadService(interval,hour);
            synProcessList.add(synProcess);
            
//            synProcess.start();
            //HA_start
            manager = new ProcessManager();
            manager.doStart();
            //HA_end
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
    public static void stopManager() {
        MmJMXServerSec.getInstance().stop();
        if (synProcessList != null && synProcessList.size() > 0) {
            for (ProccessThreadService process : synProcessList) {
                process.stop();
            }
        }
    }

    @Override
    protected void doStart() {
        for (ProccessThreadService process : synProcessList) {
            try {
                process.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doStop() {
        for (ProccessThreadService process : synProcessList) {
            try {
                process.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
