/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.service.manager;

import com.viettel.framework.service.common.LogServer;
import com.viettel.mmserver.base.ProcessThreadMX;
import java.util.ArrayList;
import java.util.Date;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

/**
 *
 * @author qlmvt_minhht1
 */
public class ProcessThread extends ProcessThreadMX {

    private static long threadCount = 0;
    private long threadID;
    private ArrayList<LogServer> serverList;
    private Job job;

    public ProcessThread(ArrayList<LogServer> serverList, Job job, long interval) throws MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        super("Framework.Thread" + ++threadCount);
        this.serverList = serverList;
        this.job = job;
        this.job.setInterval(interval);
        String MbeanName = "process:name=" + threadCount;
        registerAgent(MbeanName);
    }

    @Override
    protected void process() {
        buStartTime = new Date();
        long startTime = System.currentTimeMillis();
        logger.info("\r\n\r\n***** Begin Process *****");
        try {
            if (serverList != null && serverList.size() > 0) {
                for (LogServer logServer : serverList) {
                    job.setLogger(logger);
                    job.run(logServer, logger);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("process: " + ex.getMessage());
        } finally {
        }

        long interval = this.job.getInterval();
        logger.info("Thread " + this.threadName + " sleep " + interval / 1000 + " seconds");
        long endTime = System.currentTimeMillis();
        try {
            long processTime = endTime - startTime;
            if (interval != 0) {
                processTime %= interval;
            }
            buStartTime = null;
            Thread.sleep(interval - processTime);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
      //  buStartTime = null;
    }
}
