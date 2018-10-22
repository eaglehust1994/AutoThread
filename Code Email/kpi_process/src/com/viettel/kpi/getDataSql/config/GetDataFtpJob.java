/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.config;

import com.viettel.kpi.getDataSql.common.CommonLogServer;
import com.viettel.kpi.getDataSql.common.CommonMapQueryLogServer;
import com.viettel.kpi.getDataSql.common.CommonSvFtpColumnMap;
import com.viettel.kpi.getDataSql.common.CommonSvFtpColumnMapPK;
import com.viettel.kpi.getDataSql.common.CommonSvFtpFile;
import com.viettel.kpi.service.common.DataTypes.eDataType;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author qlmvt_dungnt44
 */
public class GetDataFtpJob extends DataJob {

    private CommonMapQueryLogServer mapQueryServer;
    private String dbFile;
    private String dbCommon;
    private Logger logger;
    private Date timeReRunData;
    private Integer numberHourReturnData;
    private MyDbTaskClient dbLocal;
    private MyDbTask db;
    private MyFtpClient ftpClient;
    private MySFtpClient sftpClient;
    private Timestamp startTime; // Thời gian bắt đầu lấy dữ liệu
    private Timestamp endTime; // Thời gian kết thúc lấy dữ liệu 
    private boolean isManual = false;
    private Integer module_id = null;
//
//    public void initConnection() throws Exception {
//        dbLocal = new MyDbTaskClient();
//        dbLocal.init(dbFile);
//        db = new MyDbTask();
//        db.init(dbCommon);
//    }
//
//    public void releaseConnection() throws Exception {
//        dbLocal.close();
//        db.close();
//    }

    public void initConnectionClient() throws Exception {
        dbLocal = new MyDbTaskClient();
        dbLocal.init(dbFile);
    }

    public void initConnectionCommon() throws Exception {
        db = new MyDbTask();
        db.init(dbCommon);
    }

    public void initClientConnection(CommonLogServer logServer) throws Exception {
        try {
            if (logServer.getProtocol().equals("FTP")) {
                ftpClient = new MyFtpClient();
                ftpClient.Connect(logServer);
            } else if (logServer.getProtocol().equals("SFTP")) {
                sftpClient = new MySFtpClient();
                sftpClient.Connect(logServer);
            }
        } catch (Exception ex) {
            String resul = db.getExceptionContent(ex);
            String path = db.getServerInfo();
            if (resul == null || resul.equals("")) {
                db.insertDataError(logServer, null, null, 2, "Fail to login ftp server:" + logServer.getIp(), path);
            } else {
                db.insertDataError(logServer, null, null, 2, resul, path);
            }
            throw ex;

        }
    }

    public void releaseClientConnection() throws Exception {
        if (ftpClient != null) {
            ftpClient.Disconnect();
        }
        if (sftpClient != null) {
            sftpClient.Disconnect();
        }
    }

    public GetDataFtpJob(CommonMapQueryLogServer mapQueryServer, String dbFile, Logger logger) {
        this.mapQueryServer = mapQueryServer;
        this.dbFile = dbFile;
        this.logger = logger;
    }

    public GetDataFtpJob(CommonMapQueryLogServer mapQueryServer, String dbFile, String dbCommon, Timestamp startTime, Timestamp endTime, Logger logger) {
        this.mapQueryServer = mapQueryServer;
        this.dbFile = dbFile;
        this.dbCommon = dbCommon;
        this.startTime = startTime;
        this.endTime = endTime;
        this.logger = logger;
        this.isManual = true;
    }

    @Override
    public void run() {
        boolean manualCheck = this.isManual;
        try {
            //bien nay dung de tinh toan thoi gian startTime va endTime
            int stepNext = mapQueryServer.getRunStepNext();
            if (stepNext == 0) {
                stepNext = 24;
            }

            //default data value: previous day
            Timestamp previousDay = new Timestamp((new Date()).getTime()
                    - 24 * 60 * 60 * 1000);

            //bien nay dung cho tien trinh lay bu du lieu
            boolean isUpdateEndData = false;
            //tinh toan thoi gian bat dau lay du lieu
            if (startTime == null) {
                //Neu end_time_data khac null thi start_time = end_time_data + run_step_next
                //Neu end_time_data == null thi start_time = trunc(now - 24)
                if (mapQueryServer.getEndTimeData() != null) {
                    startTime = new Timestamp(removeMinSecMil(mapQueryServer.getEndTimeData()).getTime()
                            + stepNext * 60 * 60 * 1000);
                } else {
                    startTime = new Timestamp(removeTime(new Date(previousDay.getTime())).getTime());
                }
            } else {
                isUpdateEndData = true;
            }

            //tinh toan thoi gian ket thuc lay du lieu
            if (endTime == null) {
                //Neu end_time_data khac null thi end_time = end_time_data + 2 * run_step_next
                //Neu end_time_data == null thi end_time = trunc(now - 24) + run_step_next
                if (mapQueryServer.getEndTimeData() != null) {
                    endTime = new Timestamp(removeMinSecMil(mapQueryServer.getEndTimeData()).getTime()
                            + stepNext * 2 * 60 * 60 * 1000);
                } else {
                    endTime = new Timestamp(removeTime(new Date(previousDay.getTime())).getTime()
                            + stepNext * 60 * 60 * 1000);
                }
            }
            if (dbCommon == null) {
                dbCommon = Start.dbFileCommon;
            }
            if (isManual == false) {
                dbFile = Start.dbFileCommon;
                module_id = mapQueryServer.getModuleId();
//                logger.info("Module_ID: " + module_id);
                if (module_id == -2) {
                    dbFile = Start.dbFileAccess2G;
                    logger.info("Module 2G");
                } else if (module_id == -3) {
                    dbFile = Start.dbFileAccess3G;
                    logger.info("Module 3G");
                } else if (module_id == -4) {
                    dbFile = Start.dbFileVasIn;
                    logger.info("Module VASIN");
                } else if (module_id == -6) {
                    dbFile = Start.dbFileRoaming;
                    logger.info("Module Roaming");
                } else if (module_id == -7) {
                    dbFile = Start.dbFileIsp;
                    logger.info("Module ISP");
                } else if (module_id == -8) {
                    dbFile = Start.dbFileTrans;
                    logger.info("Module Transmission");
                } else if (module_id == -9) {
                    dbFile = Start.dbFileInoc;
                    logger.info("Module Inoc");
                } else if (module_id == -15) {
                    dbFile = Start.dbFileTkvl;
                    logger.info("Module TKVL");
                } else if (module_id == -1) {
                    dbFile = Start.dbFileNss;
                    logger.info("Module NSS");
                }
            }

            int step = stepNext;
            Timestamp startTimeRunning = new Timestamp(new Date().getTime());
            boolean isHourLevelData = false;

            CommonLogServer logServer = null;
            CommonSvFtpFile svFtpFile = null;
            Map<String, eDataType> columnsDataType = new HashMap<String, eDataType>();
            Map<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> columnMap = new HashMap();
            List<Integer> listStep = new ArrayList<Integer>();
            try {
                initConnectionCommon();
                initConnectionClient();
                //lay thong tin tu common_log_server
                logServer = db.getLogServer(mapQueryServer.getLogServerId());//done

                //lay thong tin COMMON_SV_FTP_FILE
                svFtpFile = db.getCommonSvFtpFile(mapQueryServer.getQueryId());
                if (svFtpFile == null) {
                    logger.error("Khong co cau hinh file (hoac file status = 0");
                    return;
                }
                if (svFtpFile.getDataLevel() == 1) {
                    //neu du lieu muc gio
                    listStep = getStepRun(mapQueryServer.getIntervalHour(), stepNext);
                    isHourLevelData = true;
                }

                columnMap = db.getColumnMapFtp(mapQueryServer.getQueryId(), columnsDataType);
            } catch (Exception e) {
                //Timestamp updateTime = new Timestamp(mapQueryServer.getEndTimeData().getTime() + mapQueryServer.getIntervalHour() * 60 * 60 * 1000);
                logger.error(mapQueryServer.getQueryId() + ": " + e.toString());
                e.printStackTrace();
                throw e;
            }

            int stepIndex = 0;
            logger.info("is manual:" + isManual);
            Timestamp endTimeFix = new Timestamp(endTime.getTime());
            boolean checkRaw = false;
            if (isManual) {
                checkRaw = true;
                Start.maxBatchSize = com.viettel.kpi.reloadData.raw.Start.maxBatchSize;
            }
            boolean checkFirstRun = true;
            if (checkFirstRun) {
                db.updateIsRunning(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId(), true);
                checkFirstRun = false;
            }
            if (!checkRaw) {
                db.updateEndTimeRun(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId(), new Timestamp((new Date()).getTime()));
            }

//        while (isManual || isRunnable(startTimeRunning, endTime, mapQueryServer.getTimeReturnNow())) {
            while (isManual || isRunnable(startTimeRunning, endTime, startTime, endTimeFix, mapQueryServer.getTimeReturnNow(), checkRaw)) {

//                logger.info("end is while " + endTime);
//                logger.info("start is while " + startTime);
                if (isHourLevelData) {
                    if (stepIndex == listStep.size()) {
                        stepIndex = 0;
                    }
                    if (stepIndex == 0) {
                        step = listStep.get(stepIndex++);
                        endTime = new Timestamp(removeMinSecMil(new Date(startTime.getTime())).getTime()
                                + step * 60 * 60 * 1000);
                    }
                }
                try {
                    if (dbFile != null && !dbFile.equals("N/A")) {
//                        if (checkFirstRun) {
//                            initConnectionCommon();
//                            initConnectionClient();
////                            initClientConnection(logServer);
//                            checkFirstRun = false;
//                            // db.updateIsRunning(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId(), true);//done
//
//                        }
//                    initConnectionCommon();
                        //cap nhat end_time_data = startTime neu startTime > end_time_data
//                    if (mapQueryServer.getEndTimeData() == null) {
////                        db.updateEndTimeData(mapQueryServer, startTime);
//                    } else if (isUpdateEndData) {
//                        if (startTime.compareTo(mapQueryServer.getEndTimeData()) > 0) {
//                            db.updateEndTimeData(mapQueryServer, startTime);
//                        }
//                    }

                        //db.updateEndTimeRun(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId());
//                    db.updateIsRunning(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId(), true);

                        //cap nhat thoi gian chay lan cuoi
                        startTimeRunning = getStartRunningTime(mapQueryServer.getIntervalHour(), mapQueryServer.getHourRunInDay());
                        if (!checkRaw) {
                            db.updateEndTimeRun(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId(), new Timestamp((new Date()).getTime()));
                        }
                        String shortTblName = svFtpFile.getTableName().toLowerCase().trim();
                        if (shortTblName.contains(".")) {
                            shortTblName = shortTblName.split("\\.")[1];
                        }
                        svFtpFile.setShortTableName(shortTblName);

                        List<String> listClientColName = db.getListColumnName(shortTblName, logger);
                        List<String> listClientColNameIsNotNull = db.getListColumnNameIsNotNull(shortTblName, logger);
                        if (listClientColName.size() < 1) {
                            return;
                        }

//                    initConnectionClient();
                        //Xoa du lieu cu neu co thoi gian luu du lieu
                        if (svFtpFile.getPrevDateDel() != null) {
                            dbLocal.deleteDataPrevious(svFtpFile.getPrevDateDel(), svFtpFile.getTableName(),
                                    startTime);
                        }

                        // Xoa du lieu cu cua ngay/gio chay
                        if (Start.isDeleteData || isManual) {
                            try {
                                logger.info("Delete old data " + startTime);
                                dbLocal.deleteOldDataFtp(svFtpFile.getDataLevel(), svFtpFile.getTableName(),
                                        startTime, endTime, mapQueryServer.getLogServerName(), mapQueryServer.getQueryId(), listClientColName);
                                logger.info("End delete old data " + startTime);
                            } catch (Exception ex) {
                                java.util.logging.Logger.getLogger(GetDataJob.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            logger.info("isDelete data default= " + Start.isDeleteData);
                        }
                        // Băt đầu lấy dữ liệu
                        initClientConnection(logServer);
                        if (logServer.getProtocol().equals("FTP")) {

                            int totalRecord = ftpClient.parse(columnMap, svFtpFile, startTime, endTime,
                                    startTimeRunning, columnsDataType, db, dbLocal, logger, logServer, listClientColName, mapQueryServer, listClientColNameIsNotNull, checkRaw, module_id);
                        } else if (logServer.getProtocol().equals("SFTP")) {
                            int totalRecord = sftpClient.parse(columnMap, svFtpFile, startTime, endTime,
                                    startTimeRunning, columnsDataType, db, dbLocal, logger, logServer, listClientColName, mapQueryServer, listClientColNameIsNotNull, checkRaw);
                        }
                        try {
                            releaseClientConnection();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    logger.info("finish mapQueryServer.getQueryId(): " + totalRecord + " records");
                    }
                } catch (Exception e) {
                    logger.error(mapQueryServer.getQueryId() + ": " + e.toString());
                    e.printStackTrace();
                    if (e.toString().contains("Connection reset")) {
                        endTime = new Timestamp(removeMinSecMil(new Date(startTime.getTime())).getTime());
                        startTime = new Timestamp(removeMinSecMil(new Date(endTime.getTime())).getTime()
                                - step * 60 * 60 * 1000);
                        logger.error("Chay lai " + endTime + " do mat ket noi");
                    }
//                Start.mapRunning.put(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId());
                } finally {
                    if (isHourLevelData) {
//                    step = listStep.get(stepIndex++);
                        if (!checkRaw) {
                            startTime = new Timestamp(removeMinSecMil(new Date(endTime.getTime())).getTime());
                            endTime = new Timestamp(removeMinSecMil(new Date(startTime.getTime())).getTime()
                                    + step * 60 * 60 * 1000);
                        } else {
                            startTime = new Timestamp(removeMinSecMil(new Date(startTime.getTime())).getTime()
                                    + step * 60 * 60 * 1000);
                        }
                    } else {
                        if (!checkRaw) {
                            startTime = new Timestamp(removeMinSecMil(new Date(endTime.getTime())).getTime());
                            endTime = new Timestamp(removeMinSecMil(new Date(startTime.getTime())).getTime()
                                    + step * 60 * 60 * 1000);
                        } else {
                            startTime = new Timestamp(removeMinSecMil(new Date(startTime.getTime())).getTime()
                                    + step * 60 * 60 * 1000);
                        }
                    }
                }
                isManual = false;
            }

            if (!checkFirstRun) {
                try {
                    db.updateIsRunning(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (!manualCheck) {
                Start.decreaseThreadSizeUsed();
            } else {
                com.viettel.kpi.reloadData.raw.Start.decreaseThreadSizeUsed();
            }
        }
    }

    /**
     *
     */
    private Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(date.getTime()));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     *
     */
    private Date removeMinSecMil(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(date.getTime()));
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * stopping condition: endTime > currentTime
     *
     * @param startTime
     * @param endTime
     * @param mapQuery
     * @return
     */
    private boolean isRunnable(Timestamp currentTime, Timestamp endTime, Timestamp startTime, Timestamp endTimeFix, int timeReturn, boolean checkRaw) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTime.getTime());
        cal.add(Calendar.HOUR_OF_DAY, 0 - timeReturn);

        if (endTime.compareTo(new Timestamp(cal.getTimeInMillis())) > 0) {
            return false;
        }
        if (checkRaw) {
            if (startTime.compareTo(endTimeFix) >= 0) {
                return false;
            }
        }

        return true;
    }

    private int getHour(Timestamp time) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime());

        return cal.get(Calendar.HOUR_OF_DAY);
    }

    private Timestamp getStartRunningTime(int interval, int startHourCfg) {

        Timestamp currentTime = new Timestamp((new Date()).getTime());
        int currentHour = getHour(currentTime);
        if (currentHour > startHourCfg) {
            int period = currentHour - startHourCfg;
            int i = (int) Math.floor(period / interval);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(currentTime.getTime());
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.HOUR_OF_DAY, startHourCfg + i * interval);
            currentTime = new Timestamp(cal.getTimeInMillis());
        }

        return currentTime;
    }

    private static List<Integer> getStepRun(int intervalHour, int runStepNext) {

        //neu thoi gian chay theo gio ma duoc cau hinh = 0 thi set lai gia tri mac dinh
        if (intervalHour == 0) {
            intervalHour = 1;
        }

        List<Integer> listStep = new ArrayList<Integer>();
        if (intervalHour >= runStepNext) {
            listStep.add(runStepNext);
        } else {
            int modNumber = runStepNext % intervalHour;
            int step = (runStepNext - modNumber) / intervalHour;

            for (int i = 0; i < step; i++) {
                listStep.add(intervalHour);
            }

            if (modNumber != 0) {
                listStep.add(modNumber);
            }
        }

        return listStep;
    }
}
