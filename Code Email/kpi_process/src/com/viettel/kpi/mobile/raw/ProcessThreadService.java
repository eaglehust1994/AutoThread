/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.mobile.raw;

import com.viettel.haframework.fw.ZkProcessMX;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class ProcessThreadService extends ZkProcessMX {

    private MyDbTask db;
    private long interval;
    private String startDate;
    private String endDate;
    private boolean reload;
    private static final Logger logger = Logger.getLogger(ProcessThreadService.class);

    public ProcessThreadService(Long interval, String startDate, String endDate, boolean reload) {
        super("KPIMobileRaw");
        this.interval = interval;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reload = reload;
    }

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();
        try {
            db = new MyDbTask();
//            int currMinute = DateTimeUtils.getCurrentMinute();
//            logger.info("hour current: " + currHour);
//            logger.info("hour run: " + hourRun);
//            if (hourRun != currHour) {
//                logger.info("Chua den thoi gian chay tien trinh");
//            }
//            if (hourRun == currHour) {
//            if (currMinute % 5 == 0) {
            MyJob job = new MyJob(startDate,endDate, reload, db);
            job.run();
//            }
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
                long endTime = System.currentTimeMillis();
                long processTime = endTime - startTime;
                logger.info("processTime: " + processTime / 1000 + " seconds");
//                long currMin = DateTimeUtils.getCurrentMinute();
                long sleepTime = interval - processTime;
                logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (Exception e) {
            }
        }
    }
}
