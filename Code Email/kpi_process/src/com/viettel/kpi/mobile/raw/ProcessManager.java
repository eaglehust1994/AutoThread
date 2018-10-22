/*
 * Copyright YYYY Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.mobile.raw;

import com.viettel.haframework.fw.ZkProcess;
import com.viettel.kpi.common.utils.DataConfig;
import com.viettel.mmserver.agent.MmJMXServerSec;
import java.util.ArrayList;

/**
 *
 * @author dungvv8
 */
public class ProcessManager extends ZkProcess {

    private static long interval;
    private static String startDate;
    private static String endDate;
    private static boolean reload;
    protected static String pro = "../conf/program.conf";
    static ArrayList<ProcessThreadService> listProcess;
    private static ProcessManager manager;
    public static void startManager() {
        try {
            ProcessThreadService process;
            listProcess = new ArrayList();
            DataConfig.init(pro);
            interval = DataConfig.getLongProperties("interval", 300000);
            reload = DataConfig.getBoolProperties("reload", false);
            startDate = DataConfig.getStringProperties("startTime");
            endDate = DataConfig.getStringProperties("endTime");

            process = new ProcessThreadService(interval, startDate,endDate, reload);
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
