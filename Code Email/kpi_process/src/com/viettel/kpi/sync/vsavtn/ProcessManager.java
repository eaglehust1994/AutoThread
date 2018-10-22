/*
 * Copyright YYYY Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.sync.vsavtn;

import com.viettel.haframework.fw.ZkProcess;
import static com.viettel.kpi.common.utils.Constants.SALT;
import com.viettel.kpi.common.utils.DataConfig;
import com.viettel.mmserver.agent.MmJMXServerSec;
import com.viettel.passprotector.PassProtector;
import java.util.ArrayList;

/**
 *
 * @author dungvv8
 */
public class ProcessManager extends ZkProcess {

    private static long interval;
    private static int hourRun;
    private static String wsVsaVtn;
    private static String userName;
    private static String password;
    protected static String pro = "../conf/program.conf";
    protected static int connectTimeout;
    protected static int requestTimeout;
    protected static int idTCT;
    static ArrayList<ProcessThreadService> listProcess;
    private static ProcessManager manager;

    public static void startManager() {
        try {
            ProcessThreadService process;
            listProcess = new ArrayList<ProcessThreadService>();
            DataConfig.init(pro);
            interval = DataConfig.getLongProperties("interval", 3600000);
            connectTimeout = DataConfig.getIntProperties("connectTimeout", 180000);
            requestTimeout = DataConfig.getIntProperties("requestTimeout", 300000);
            hourRun = DataConfig.getIntProperties("hourRun");
            hourRun = 15;
            wsVsaVtn = DataConfig.getStringProperties("wsVsaVtn");
//            userName = PassProtector.decrypt(DataConfig.getStringProperties("userName"), SALT);
//            password = PassProtector.decrypt(DataConfig.getStringProperties("password"), SALT);
            userName="1";
            password="1";
            idTCT = DataConfig.getIntProperties("idTCT");

            process = new ProcessThreadService(hourRun, wsVsaVtn, userName, password);
            listProcess.add(process);
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
                process.stop();
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
