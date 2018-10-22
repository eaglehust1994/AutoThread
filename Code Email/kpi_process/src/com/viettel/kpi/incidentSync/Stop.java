/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.incidentSync;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

/**
 *
 * @author NhanND
 */
public class Stop {

    /**
     * @param ts
     * @throws javax.management.MalformedObjectNameException
     * @throws javax.management.InstanceAlreadyExistsException
     * @throws javax.management.MBeanRegistrationException
     * @throws javax.management.NotCompliantMBeanException
     */
    public static void main(String ts[]) throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        ProcessManager.stopManager();
    }
}
