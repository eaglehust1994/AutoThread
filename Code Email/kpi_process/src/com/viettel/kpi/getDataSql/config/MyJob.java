/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.config;

import com.viettel.kpi.getDataSql.common.CommonMapQueryLogServer;
import com.viettel.kpi.service.common.LogServer;
import com.viettel.kpi.service.manager.Job;
//import com.viettel.writeLog.LogServices;
import java.sql.Timestamp;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author qlmvt_MinhHT1
 */
public class MyJob extends Job {

    private MyDbTask db;
    private MyClient client;
    private String dbFile;
//    private LogServices logServices;
    private int queryId;
    private Timestamp runTimestamp;

    public MyJob() {
        this.dbFile = Start.dbFileCommon;
    }

    public void initConnection() throws Exception {
        db = new MyDbTask();
        db.init(dbFile);
    }

    public void releaseConnection() throws Exception {
        db.close();
    }

//    public void initClientConnection(LogServer logServer) throws Exception {
//        client = new MyClient();
//        if (logServer.getProtocol().trim().equalsIgnoreCase("SYBASE")) {
//            client.connectSybase(logServer);
//        }
//        if (logServer.getProtocol().trim().equalsIgnoreCase("ORACLE")) {
//            client.connectOracle(logServer);
//        }
//    }
//    public void releaseClientConnection() throws Exception {
//        client.close();
//    }
    @Override
    public void run(LogServer logServer, Logger logger) {
//        System.out.println("===============Running================");
        try {
            db = new MyDbTask();
//            initConnection();
//            initClientConnection(logServer);
            // 1 get Query List and Table List
            int serviceTypeId = db.getServiceType(Start.serviceTypeCode);
            if (serviceTypeId == 0) {
                logger.error("Service Type" + Start.serviceTypeCode + " khong ton tai trong bang common_service_type");
                return;
            }
            Start.serviceTypeId = serviceTypeId;
//            logger.info("begin getQueryList");
            ArrayList<CommonMapQueryLogServer> queryList = db.getMapQueryServerList(Start.serviceTypeId);
            logger.info("QueryList.size : " + queryList.size());
//            Set set = Start.mapRunning.entrySet();
//// Get an iterator
//            Iterator i = set.iterator();
//// Display elements
//            while (i.hasNext()) {
//                logger.info("chui vao day roi nay");
//                Map.Entry me = (Map.Entry) i.next();
//                Long _queryId = (Long) me.getKey();
//                Long _logServerId = (Long) me.getValue();
//                CommonMapQueryLogServer logServerReconnec = db.getCommonLogServer(_queryId,_logServerId);
//                queryList.add(logServerReconnec);
//                Start.mapRunning.remove(_queryId);
//            }
            // Kiem tra xem query nao du dk để chạy:
            for (CommonMapQueryLogServer query : queryList) {
                String type = query.getType();
                try {
                    //Tạo thread để chạy ở đây.
                    // Kiểm tra dk chạy với if(true)
                    Date now = new Date();
                    if (validateRuntime(query, now)) {
                        while (true) {
                            if (Start.threadSizeInUsed < Start.maxThreadSize) {
                                if (type != null) {
                                    if (type.equals("1")) {
                                        GetDataJob getDataJob = new GetDataJob(query, dbFile, logger);
                                        Thread thread = new Thread(getDataJob);
                                        thread.setName("Server(" + query.getLogServerName() + "):Query(" + query.getQueryName() + ")");
                                        thread.start();
                                        Start.increaseThreadSizeUsed();
                                        logger.info("-----threadSizeInUsed: " + Start.threadSizeInUsed);
                                        break;
                                    } else if (type.equals("2")) {
                                        GetDataFtpJob getDataFtpJob = new GetDataFtpJob(query, dbFile, logger);
                                        Thread thread = new Thread(getDataFtpJob);
                                        thread.setName("Server(" + query.getLogServerName() + "):Query(" + query.getQueryName() + ")");
                                        thread.start();
                                        Start.increaseThreadSizeUsed();
                                        logger.info("-----threadSizeInUsed: " + Start.threadSizeInUsed);
                                        break;
                                    } else {
                                        break;
                                    }
                                }
                            } else {
                                // Slep 10s de doi xem con Thread nao da giai phong chua?
                                Thread.sleep(10 * 1000);
                            }
                        }
                    }
                } catch (Exception e1) {
                    //Mat du lieu
                    e1.printStackTrace();
//                    logServices.write(Start.serviceCode, Long.valueOf(query.getID()), "Error is: " + e1.getMessage(), runTimestamp, 3);
                }
            }
        } catch (Exception ex) {
            //Mat du lieu
            setLostData(true);
            logger.error(ex.toString());
            ex.printStackTrace();
        } finally {
            try {
                releaseConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateRuntime(CommonMapQueryLogServer queryLogServer, Date now) {
        Date endTimeRun = queryLogServer.getEndTimeRun();
        Date trueEndTimeRun = queryLogServer.getTrueEndTimeRun();
        int hourRunInDay = queryLogServer.getHourRunInDay();
        int intervalHour = queryLogServer.getIntervalHour();
        Date endTimeData = queryLogServer.getEndTimeData();
        Calendar calNow = Calendar.getInstance();
        calNow.setTime(now);
        int hourNow = calNow.get(Calendar.HOUR_OF_DAY);
        if (endTimeRun == null) {
            //neu chua chay lan nao
            //thi chi can gio hien tai lon hon gio cau hinh chay
            if (hourNow >= hourRunInDay) {
                return true;
            }
        } else {
            //neu da co lan chay truoc do
            //gio hien tai phai lon hon gio cau hinh
            //va nam sau thoi gian chay ke tiep
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(endTimeRun);
            calEnd.add(Calendar.HOUR_OF_DAY, intervalHour);
            Date nextEndTimeRun = calEnd.getTime();
            if (hourNow >= hourRunInDay && now.compareTo(nextEndTimeRun) >= 0) {
                return true;
            }
            //==> Kiem tra neu data_level = 2 (muc ngay), cho phep 1 gio chay lay bu du lieu 1 lan tu dong
            //Neu end_time_data = null ==> Kiem tra gio chay cuoi cung voi gio hien tai, neu >1h thi se tu chay lai
            // Chay toi da 4 lan
            if (queryLogServer.getDataLevel() == 2) {
                if (endTimeData == null) {
                    if (hourNow >= hourRunInDay && (now.getTime() - trueEndTimeRun.getTime()) > 3600000 && hourNow < (hourRunInDay + 4)) {
                        return true;
                    }
                } else {
                    //Neu end_time_data != null ==> kiem tra end_time_data da la ngay cuoi cung chua?
                    //Neu chua thi Kiem tra gio chay cuoi cung voi gio hien tai, neu >1h thi se tu chay lai
                    // Chay toi da 4 lan
                    if ((endTimeRun.getTime() - endTimeData.getTime()) > 86400000) {
                        if (hourNow >= hourRunInDay && (now.getTime() - trueEndTimeRun.getTime()) > 3600000 && hourNow < (hourRunInDay + 4)) {
                            return true;
                        }
                    }
                }
            }

        }

        return false;
    }
}
