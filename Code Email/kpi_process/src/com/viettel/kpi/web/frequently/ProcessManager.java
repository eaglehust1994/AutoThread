/*
 * Copyright YYYY Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.web.frequently;

import com.viettel.haframework.fw.ZkProcess;
import com.viettel.kpi.common.utils.DataConfig;
import com.viettel.mmserver.agent.MmJMXServerSec;
import com.viettel.mmserver.base.ProcessThreadMX;
import java.util.ArrayList;

/**
 *
 * @author dungvv8
 */
public class ProcessManager extends ZkProcess {

    private static long interval;
    private static long hourRun;
    private static String updateTime;
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
            hourRun = DataConfig.getLongProperties("hourRun", 3L);
            reload = DataConfig.getBoolProperties("reload", false);
//            if (reload) {
            updateTime = DataConfig.getStringProperties("updateTime");
//            } else {
//                updateTime = DateTimeUtils.format(DateTimeUtils.add(new Date(), -1), "dd/MM/yyyy");
//            }

            process = new ProcessThreadService(interval, updateTime, hourRun, reload);
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
     * @param: N/A
     * @return:N/A
     * @throws: N/A
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
        for (ProcessThreadMX process : listProcess) {
            try {
                process.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doStop() {
        for (ProcessThreadMX process : listProcess) {
            try {
                process.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
