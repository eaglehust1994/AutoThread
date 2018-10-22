/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.lostData.rate;

import com.viettel.haframework.fw.ZkProcessMX;
import com.viettel.kpi.common.utils.DateTimeUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class ProcessThreadService extends ZkProcessMX {

    private MyDbTask db;
    private long interval;
    private String updateTime;
    private Long hourRun;
    private boolean reload;
    private String ipClient;
    private String urlClient;
    private String usernameClient;
    private String passwordClient;
    private static final Logger log = Logger.getLogger(ProcessThreadService.class);

    ProcessThreadService(long interval, String updateTime, long hourRun, boolean reload,
            String ipClient, String urlClient, String usernameClient, String passwordClient) {
        super("LostDataRate");
        this.interval = interval;
        this.updateTime = updateTime;
        this.hourRun = hourRun;
        this.reload = reload;
        this.ipClient = ipClient;
        this.urlClient = urlClient;
        this.usernameClient = usernameClient;
        this.passwordClient = passwordClient;
    }

    @Override
    public void process() {
        try {
            db = new MyDbTask();
            int currHour = DateTimeUtils.getCurrentHour();

            log.info("hour current: " + currHour);
            log.info("hour run: " + hourRun);
            if (hourRun != currHour) {
                log.info("Chua den thoi gian chay tien trinh");
            }
            if (hourRun == currHour) {
                MyJob job = new MyJob(updateTime, reload, ipClient, urlClient,usernameClient,passwordClient,db);
                job.run();
            }
        } catch (Exception ex) {
            log.info(ex.getMessage(), ex);
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
            } catch (Exception ex) {
                log.info(ex.getMessage(), ex);
            }
            try {
                long currMin = DateTimeUtils.getCurrentMinute();
                long sleepTime = (60 - currMin) * 60 * 1000;
                log.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                Thread.sleep(sleepTime);
            } catch (Exception e) {
            }
        }
    }
}
