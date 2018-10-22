/*
 * Copyright YYYY Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.incidentSync;

import com.viettel.haframework.fw.ZkProcess;
import com.viettel.kpi.common.utils.DataConfig;
import com.viettel.mmserver.agent.MmJMXServerSec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author NhanND
 */
public class ProcessManager extends ZkProcess {

    private static long interval;
    private static String hourRun;
    private static String updateTime;
    private static Date startTime = null;
    private static Date endTime = null;
    private static boolean reload;
    protected static String pro = "../conf/program.conf";
    static ArrayList<ProcessThreadService> listProcess;
    private static ProcessManager manager;

    public static void startManager() {
        try {
            ProcessThreadService process;
            listProcess = new ArrayList<ProcessThreadService>();
            DataConfig.init(pro);
            interval = DataConfig.getLongProperties("interval", 3600000);
            hourRun = DataConfig.getStringProperties("hourRun");
            reload = DataConfig.getBoolProperties("reload", false);
            updateTime = DataConfig.getStringProperties("updateTime");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String strStartTime = DataConfig.getStringProperties("startTime");
            if (strStartTime != null && !strStartTime.isEmpty()) {
                try {
                    startTime = sdf.parse(strStartTime);
                } catch (Exception e) {
                    throw new Exception("Invalid startTime! Valid startTime: dd/MM/yyyy");
                }
            }
            String strEndTime = DataConfig.getStringProperties("endTime");
            if (strEndTime != null && !strEndTime.isEmpty()) {
                try {
                    endTime = sdf.parse(strEndTime);
                } catch (Exception e) {
                    throw new Exception("Invalid endTime! Valid endTime: dd/MM/yyyy");
                }
            }
            process = new ProcessThreadService(interval, updateTime, hourRun, 
                    reload, startTime, endTime);
            listProcess.add(process);
//            process.start();
            //HA_start
            manager = new ProcessManager();
            manager.start();
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
     */
    public static void stopManager() {
        MmJMXServerSec.getInstance().stop();
        if (listProcess != null && listProcess.size() > 0) {
            for (ProcessThreadService process : listProcess) {
                try {
                    process.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void doStart() {
        for (ProcessThreadService process : listProcess) {
            try {
                process.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doStop() {
        for (ProcessThreadService process : listProcess) {
            try {
                process.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
