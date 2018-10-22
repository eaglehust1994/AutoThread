/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.web.restartProcess;

import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.mmserver.base.ProcessThreadMX;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class ProcessThreadService extends ProcessThreadMX {

    private static final Logger log = Logger.getLogger(ProcessThreadService.class);
    private MyDbTask db;
    private long interval;
    private String updateTime;
    private int hourRun;
    private int minRun;
    private boolean reload;
    private boolean isRun;

    public ProcessThreadService(String updateTime, int hourRun, int minRun,long interval, boolean reload) {
        super("KPIHourly");
        this.updateTime = updateTime;
        this.hourRun = hourRun;
        this.minRun = minRun;
        this.reload = reload;
        this.interval = interval;
    }

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();
        try {
            logger.info("\r\n\r\n***** Begin Process *****");
            long currMin = DateTimeUtils.getCurrentMinute();
            db = new MyDbTask();
            logger.info("currMin: " + currMin);
            logger.info("minRun: " + minRun);
            logger.info("interval: " + interval);
            isRun = false;
            
            if (currMin >= minRun) {
                MyJob job = new MyJob(updateTime, hourRun, minRun, reload, db);
                job.run();
                isRun = true;
            } else {
                long sleepTime = (minRun - currMin) * 60 * 1000;
                logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                Thread.sleep(sleepTime);
            }
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
            } catch (Exception ex) {
                logger.info(ex.getMessage(), ex);
            }
            try {

            } catch (Exception ex) {
                logger.info(ex.getMessage(), ex);
            }
        }
        if (isRun) {
            try {
                long currMin = DateTimeUtils.getCurrentMinute();
                long sleepTime = (60 - currMin) * 60 * 1000;
                logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
            }
        }

    }
}
