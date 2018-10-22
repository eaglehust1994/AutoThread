/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.sync.moduleCodeQltn;

import com.viettel.kpi.common.utils.DataConfig;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author qlmvt_KhangNT2
 */
public class Start {
    
    public static long interval;
    public static long hourRun;
    public static long connectTimeout;
    public static long requestTimeout;
    protected static String pro = "../conf/program.conf";
    static {
        try {
            PropertyConfigurator.configure("../etc/log4j.cfg");
        } catch (Exception ex) {
        }
    }

    /**
     * @param args the command line arguments
     * @throws javax.management.MalformedObjectNameException
     * @throws javax.management.InstanceAlreadyExistsException
     * @throws javax.management.MBeanRegistrationException
     * @throws javax.management.NotCompliantMBeanException
     */
    public static void main(String[] args) throws MalformedObjectNameException,
            InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        DataConfig.init(pro);
        interval = DataConfig.getLongProperties("interval", 3600000);
        hourRun = DataConfig.getLongProperties("hourRun", 1);
        connectTimeout = DataConfig.getLongProperties("connectTimeout", 60000);
        requestTimeout = DataConfig.getLongProperties("requestTimeout", 60000);
        ProcessManager.startManager();
    }
}
