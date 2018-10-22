/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.sync.vsavtn;

import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.mmserver.base.ProcessThreadMX;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class ProcessThreadService extends ProcessThreadMX {

    private static final Logger log = Logger.getLogger(ProcessThreadService.class);
    private MyDbTask db;
    private final int hourRun;
    private final String wsVsaVtn;
    private final String userName;
    private final String password;

    public ProcessThreadService(int hourRun, String wsVsaVtn, String userName, String password) {
        super("SyncVSAVTN");
        this.hourRun = hourRun;
        this.wsVsaVtn = wsVsaVtn;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public void process() {
        try {
            log.info("\r\n\r\n***** Begin Process *****");
            db = new MyDbTask();

            MyJob job = new MyJob(hourRun, wsVsaVtn, userName, password, db);
            job.run();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
            try {
                long currMin = DateTimeUtils.getCurrentMinute();
                long sleepTime = (60 - currMin) * 60 * 1000;
                logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                Thread.sleep(sleepTime);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

    }
}
