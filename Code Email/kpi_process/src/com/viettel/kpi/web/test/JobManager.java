/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.test;

//import com.viettel.kpi.utils.EncryptDecryptUtils;
import com.viettel.kpi.common.utils.EncryptDecryptUtils;
import com.viettel.mmserver.base.ProcessThreadMX;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.SimpleTriggerImpl;

/**
 *
 * @author HuyND6@viettel.com.vn
 */
public class JobManager extends ProcessThreadMX {

    protected static JobManager instance = null;
    public static String runningTime = "";

    public static JobManager getInstance() {
        if (instance == null) {
            instance = new JobManager("Job Manager", "Tien trinh tong hop KPI", "KPI:thread=JobManager");
        }

        return instance;
    }
    private Scheduler scheduler;
    private boolean isLock = false;
    private boolean isAlive = true;
    public static Properties pro = null;

    public synchronized void setAlive() {
        isAlive = true;
    }

    public synchronized void disAlive() {
        isAlive = false;
    }

    public synchronized boolean checkAlive() {
        return isAlive;
    }

    public synchronized void lock() {
        isLock = true;
    }

    public synchronized void unLock() {
        isLock = false;
    }

    public synchronized boolean checkLock() {
        return isLock;
    }

    public void Stop() throws SchedulerException {

        disAlive();

        if (scheduler != null) {
            scheduler.shutdown();
            scheduler = null;
        }
    }

    public void Start() throws SchedulerException {
        if (scheduler == null) {
            this.createScheduler();
        }
        buStartTime = new Date();
        scheduler.start();
    }

    public static Properties loadConfigs() throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(".." + File.separator + "etc" + File.separator + "config.properties");
        Properties pro = new Properties();
        pro.load(fis);
        if (fis != null) {
            try {
                fis.close();
            } catch (Exception ex) {
            }
        }
        return pro;
    }

    protected void createScheduler() {
        try {
            // load thong tin DB
//            logger.info(" Get thong tin ma hoa Database ");
//            configDB = loadEncryptedDBConfig(".."+File.separator+"etc"+File.separator+"DBConfig");
//            if (configDB==null)
//            {
//                logger.error(" Loi lay thong tin ma hoa Database thanh cong");
//                return;
//            }
//            logger.info(" Get thong tin Database ");
//            
            pro = loadConfigs();
            String cronStr = pro.getProperty("CRON_STR");
//
            SchedulerFactory sf = new StdSchedulerFactory();
            scheduler = sf.getScheduler();

            logger.info("Create job for kpi ");
            JobDetail jobDetail = JobBuilder
                    .newJob(MyJob.class)
                    .withIdentity("myJob", Scheduler.DEFAULT_GROUP)
                    .build();

            logger.info("CRON_STR: " + cronStr);
            Trigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity("kpiTrigger", Scheduler.DEFAULT_GROUP)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronStr))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);


        } catch (Exception e) {
            logger.error("Error createScheduler: ", e);
        }
    }
    public static Map configDB = null;

    public Map loadEncryptedDBConfig(String filePath) {
        Map config = new HashMap();
        logger.info("Begin reading encrypted file: " + filePath);
        try {
            String decryptString = EncryptDecryptUtils.decryptFile(filePath);

            String[] properties = decryptString.split("\r\n");
            logger.info("Number of encrypted properties: " + properties.length);
            for (String property : properties) {
                String[] temp = property.split("=", 2);
                if (temp.length == 2) {
                    config.put(temp[0], temp[1]);
                    logger.info(String.format("Set property '%s' to Persistence", temp[0]));
                }
            }
        } catch (Exception e) {
            logger.error("Error read encrypted file : ", e); // hanm13
            return null;
        }

        logger.info("Done Reading encrypted file :" + filePath);
        return config;
    }

    protected JobManager(String threadName, String description, String MBeanName) {
        super(threadName, description);
        try {
            //Đăng ký tên Mbean
            registerAgent(MBeanName);
        } catch (Exception e) {
            logger.error("Error init registerAgent MM: ", e);
        }
    }

    @Override
    protected void process() {
        // Chú ý: muốn sử dụng được tính năng cảnh báo tiến trình treo, người lập trình phải gán thời điển bắt đầu của hàm process
        buStartTime = new Date();
        logger.info("Running");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            logger.error("Error Thread Sleep: ", ex);
        }
        //Khi thực hiện xong công việc, cần đặt lại buStartTime
        // buStartTime = null;
    }

    @Override
    public void start() {

        try {
            this.Start();
            super.start();
        } catch (SchedulerException ex) {
            logger.error("Error start Scheduler: ", ex);
        }
    }

    @Override
    public void stop() {
        try {

            Stop();
            super.stop();
        } catch (SchedulerException ex) {
            logger.error("Error stop Scheduler: ", ex);
        }
    }

    public Logger getLogger() {
        return logger;
    }
}
