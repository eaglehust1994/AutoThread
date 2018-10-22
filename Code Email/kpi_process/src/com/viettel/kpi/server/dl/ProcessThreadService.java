/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.server.dl;

import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.mmserver.base.ProcessThreadMX;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class ProcessThreadService extends ProcessThreadMX {

    private static final Logger log = Logger.getLogger(ProcessThreadService.class);
//    private MyDbTask db;
    private String updateTime;
    private boolean reload;
    private int hourRun;

    public ProcessThreadService(String updateTime, int hourRun, boolean reload) {
        super("KPIDaily");
        this.updateTime = updateTime;
        this.hourRun = hourRun;
        this.reload = reload;
    }

    @Override
    public void process() {
        try {
            log.info("\r\n\r\n***** Begin Process *****");
//            db = new MyDbTask();
            int currHour = DateTimeUtils.getCurrentHour();

            logger.info("hour current: " + currHour);
            logger.info("hour run: " + hourRun);
            if (hourRun != currHour) {
                logger.info("Chua den thoi gian chay tien trinh");
            }
            if (hourRun == currHour) {
                MyJob job = new MyJob(updateTime, reload);
                job.run();
            }
        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
        } finally {
//            try {
//                if (db != null) {
//                    db.close();
//                }
//            } catch (Exception ex) {
//                logger.info(ex.getMessage(), ex);
//            }
            try {
                long currMin = DateTimeUtils.getCurrentMinute();
                long sleepTime = (60 - currMin) * 60 * 1000;
                logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                Thread.sleep(sleepTime);
            } catch (Exception ex) {
                logger.info(ex.getMessage(), ex);
            }
        }

    }
}
