/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.test;

import java.util.Date;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author dungvv8
 */
public class MyJob implements Job {

    private Logger logger = Logger.getLogger(getClass());

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        if (JobManager.getInstance().checkLock() == true) {
            logger.info("An other process is running.....");
            return;
        }
        logger.info("Start a new process");
        JobManager.getInstance().lock();
        try {
            process();
        } catch (Exception ex) {
            logger.error("Error execute process.", ex);
        }
        JobManager.getInstance().unLock();
        logger.info("Finish process");
    }

    public void process() throws Exception {
        System.out.println(new Date());
    }
}
