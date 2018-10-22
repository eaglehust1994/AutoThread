/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.report.appOverThreshold;

import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.mmserver.base.ProcessThreadMX;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class ProcessThreadService extends ProcessThreadMX {

    private MyDbTask db;
    private int startDate;
    private int endDate;
    private String updateTime;
    private Long hourRun;
    private boolean reload;
    private static final Logger logger = Logger.getLogger(ProcessThreadService.class);

    public ProcessThreadService(int startDate, int endDate, String updateTime, Long hourRun, boolean reload) {
        super("reportQA");
        this.startDate = startDate;
        this.endDate = endDate;
        this.updateTime = updateTime;
        this.hourRun = hourRun;
        this.reload = reload;
    }

    @Override
    public void process() {
        try {
            db = new MyDbTask();

            int currHour = DateTimeUtils.getCurrentHour();

            logger.info("hour current: " + currHour);
            logger.info("hour run: " + hourRun);
            if (hourRun != currHour) {
                logger.info("Chua den thoi gian chay tien trinh");
            }
            if (hourRun == currHour) {
                MyJob job = new MyJob(startDate, endDate, updateTime, reload,db);
                job.run();
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
                long currMin = DateTimeUtils.getCurrentMinute();
                long sleepTime = (60 - currMin) * 60 * 1000;
                logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                Thread.sleep(sleepTime);
            } catch (Exception e) {
            }
        }
    }
}
