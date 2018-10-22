/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.server.dl;

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
        ProcessManager.startManager();
    }
}
