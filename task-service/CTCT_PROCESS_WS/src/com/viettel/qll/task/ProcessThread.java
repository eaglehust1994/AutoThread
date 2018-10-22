/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.qll.task;


import com.google.common.collect.Lists;
import com.viettel.framework.service.utils.DateTimeUtils;
import javax.mail.Session;
import javax.mail.PasswordAuthentication;
import com.viettel.mmserver.base.ProcessThreadMX;
import static java.lang.Math.toIntExact;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 *
 * @author pm1_os38
 */
public class ProcessThread extends ProcessThreadMX {

//    LocalDate date = LocalDate.now();
//    java.sql.Date sqlDate = java.sql.Date.valueOf(date);

    MyDbSql db;
    private long interval;
    private long maxBatchSize;
    private boolean useFixedHour;
    private long minuteRun;
    private long hour;
    int port;
    String database = "";

    public ProcessThread(long interval, long maxBatchSize, long hour, long minute) throws Exception {
        super("");
        this.interval = interval;
        this.maxBatchSize = maxBatchSize;
        this.hour = hour;
        this.database = ProcessManager.database;
//        String MbeanName = "process:name=SynTempFromTKTUToBikt";
        this.useFixedHour = (0 <= hour && hour <= 23) ? true : false;
        this.minuteRun = (0 <= minute && minute <= 59) ? minute : 0;
//        registerAgent(MbeanName);
    }

    @Override
    protected void process() {
        //to do
        logger.info("=======================================================");
        logger.info("*****BEGIN GET SYNCHRONOUS FROM KTTS*****");
        logger.info("=======================================================");
        MyDbSql myDb = new MyDbSql();
        long startTime = System.currentTimeMillis();
        String dayRun = DateTimeUtils.format(DateTimeUtils.add(new Date(), ProcessManager.dayRemoveMer), "dd/MM/yyyy");
       
        boolean bRunning = false;
        try {
            
            insertTask(myDb,dayRun);
            sendWarningEmail(myDb,dayRun);
                bRunning = true;
           
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (bRunning) {
                try {
                    long endTime = System.currentTimeMillis();
                    long processTime = endTime - startTime;
                    if (interval != 0) {
                        processTime %= interval;
                    }
                    logger.info("Thread syn from WS TKTU sleep " + this.threadName + " sleep " + (interval - processTime) / 1000 + " seconds");
                    Thread.sleep(interval - processTime);
                } catch (InterruptedException ex) {
                    logger.error(ex.getMessage(), ex);
                    Logger.getLogger(ProcessThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    private long checkDayOfWeek(String day) throws Exception{
        long dayOfWeek=0;
        try {
            if("Mon".equals(day)){
                dayOfWeek = 2;               
            }
            if("Tue".equals(day)){
                dayOfWeek = 3;
            }
            if("Wed".equals(day)){
                dayOfWeek = 4;
            }
            if("Thu".equals(day)){
                dayOfWeek = 5;
            }
            if("Fri".equals(day)){
                dayOfWeek = 6;
            }
            if("Sat".equals(day)){
                dayOfWeek = 7;
            }
            if("Sun".equals(day)){
                dayOfWeek = 8;
            }
            
        } catch (Exception e) {
             logger.error(e.getMessage(), e);
        }
        
        return dayOfWeek;
    }
    
    
    private void insertTask(MyDbSql db, String dayRun) throws Exception{
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dayRun);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DateFormat dayOfWeek = new SimpleDateFormat("E"); 
       
        long day = checkDayOfWeek(dayOfWeek.format(date));
       
        calendar.add(Calendar.MONTH, (int) 1);
        long day1 = calendar.get(Calendar.DAY_OF_MONTH);
        long month = calendar.get(Calendar.MONTH);
        long week = calendar.get(Calendar.WEEK_OF_YEAR);
        
        try {
            logger.info("Lấy danh mục công việc mới");
            List<TaskGroupBO> lstTaskGroup = db.getInfoTaskGroup();
   
            List<TaskBO> listTask = Lists.newArrayList();
            for(TaskGroupBO objTaskGroup : lstTaskGroup){
                TaskBO newObj = new TaskBO();
                if(objTaskGroup.getIdTaskGroup()==null){
                    if(objTaskGroup.getPeriodic()==1 
                        && objTaskGroup.getStartTaskGroup()<= day
                        && objTaskGroup.getEndTaskGroup()>= day
                        && objTaskGroup.getCreateTaskCycle()==null){                           
                            newObj.setIdTaskGroup(objTaskGroup.getTaskGroupId());
                            newObj.setStatus((long)1);
                            newObj.setCreateTaskCycle(week);
                            newObj.setStartTime(date);
                            int culDate = toIntExact (objTaskGroup.getEndTaskGroup() - day);
                            calendar.add(Calendar.DATE,culDate);
                            newObj.setEndTime(calendar.getTime());
                            newObj.setNoteTask("");
                            listTask.add(newObj);
                    }
                    if(objTaskGroup.getPeriodic()==2
                       && objTaskGroup.getStartTaskGroup()<=day1
                       && objTaskGroup.getEndTaskGroup()>=day1 ){
                            newObj.setIdTaskGroup(objTaskGroup.getTaskGroupId());
                            newObj.setStatus((long)1);
                            newObj.setCreateTaskCycle(month);
                            newObj.setStartTime(date);
                            int culDate = toIntExact(objTaskGroup.getEndTaskGroup() - day1);
                            calendar.add(Calendar.DATE,culDate);
                            newObj.setEndTime(calendar.getTime());
                            newObj.setNoteTask("");
                            listTask.add(newObj);
                    }
                }else{
                    if(objTaskGroup.getCreateTaskCycle()!=week
                       && objTaskGroup.getPeriodic()==1 
                       && objTaskGroup.getStartTaskGroup()<= day
                       && objTaskGroup.getEndTaskGroup()>= day   ){
                        newObj.setIdTaskGroup(objTaskGroup.getTaskGroupId());
                            newObj.setStatus((long)1);
                            newObj.setCreateTaskCycle(week);
                            newObj.setStartTime(date);
                            int culDate = toIntExact (objTaskGroup.getEndTaskGroup() - day);
                            calendar.add(Calendar.DATE,culDate);
                            newObj.setEndTime(calendar.getTime());
                            newObj.setNoteTask("");
                            listTask.add(newObj);
                    }
                    if(objTaskGroup.getCreateTaskCycle()!=month
                       && objTaskGroup.getPeriodic()==2 
                       && objTaskGroup.getStartTaskGroup()<= day
                       && objTaskGroup.getEndTaskGroup()>= day   ){
                        newObj.setIdTaskGroup(objTaskGroup.getTaskGroupId());
                            newObj.setStatus((long)1);
                            newObj.setCreateTaskCycle(month);
                            newObj.setStartTime(date);
                            int culDate = toIntExact (objTaskGroup.getEndTaskGroup() - day);
                            calendar.add(Calendar.DATE,culDate);
                            newObj.setEndTime(calendar.getTime());
                            newObj.setNoteTask("");
                            listTask.add(newObj);
                    }
                }
            }
            logger.info("-----Tạo mới công việc-----");
            db.insertTask(listTask, logger, 100);
            
            
        } catch (Exception e) {
             logger.error(e.getMessage(), e);
        }
    }
    
    private void  sendWarningEmail(MyDbSql mySql,String dayRun) throws Exception{
        //https://docs.aws.amazon.com/ses/latest/DeveloperGuide/send-using-smtp-java.html
        
        //https://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dayRun);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DateFormat dayOfWeek = new SimpleDateFormat("E"); 
       
        long day = checkDayOfWeek(dayOfWeek.format(date));
       
        calendar.add(Calendar.MONTH, (int) 1);
        long day1 = calendar.get(Calendar.DAY_OF_MONTH);
        long month = calendar.get(Calendar.MONTH);
        long week = calendar.get(Calendar.WEEK_OF_YEAR);
        
        TaskGroupBO objWeek = new TaskGroupBO();
        objWeek.setCreateTaskCycle(week);
        objWeek.setStatus((long)1);
        objWeek.
        
        List<TaskGroupBO> listObj = mySql.checkTask();
        if(listObj.size()>0){
            for(TaskGroupBO obj : listObj){
                if(obj.getPeriodic()==1){
                    
                }
            }
        }
        System.out.println(listObj);
        
    }
    
    public int getCurrentHour() {
        int currentHour = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentHour = now.get(Calendar.HOUR_OF_DAY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currentHour;
    }

    public int getCurrentMinute() {
        int currentMinute = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentMinute = now.get(Calendar.MINUTE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currentMinute;
    }
}
