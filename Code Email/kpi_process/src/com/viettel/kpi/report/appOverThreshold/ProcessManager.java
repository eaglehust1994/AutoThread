/*
 * Copyright YYYY Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.report.appOverThreshold;

import com.viettel.kpi.common.utils.DataConfig;
import com.viettel.mmserver.agent.MmJMXServerSec;
import java.util.ArrayList;

/**
 *
 * @author dungvv8
 */
public class ProcessManager {

    private static int startDate;
    private static int endDate;
    private static long hourRun;
    private static String updateTime;
    private static boolean reload;
    protected static String pro = "../conf/program.conf";
    static ArrayList<ProcessThreadService> listProcess;

    public static void start() {
        try {
            ProcessThreadService process;
            listProcess = new ArrayList<ProcessThreadService>();
            DataConfig.init(pro);
            startDate = DataConfig.getIntProperties("startDate", 26);
            endDate = DataConfig.getIntProperties("endDate", 25);
            hourRun = DataConfig.getLongProperties("hourRun", 4L);
            reload = DataConfig.getBoolProperties("reload", false);
            updateTime = DataConfig.getStringProperties("updateTime");


            process = new ProcessThreadService(startDate,endDate, updateTime, hourRun, reload);
            listProcess.add(process);
            process.start();
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
        if (listProcess != null && listProcess.size() > 0) {
            for (int i = 0; i < listProcess.size(); i++) {
                ProcessThreadService process = listProcess.get(i);
            }
        }
    }
}
