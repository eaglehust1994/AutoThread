/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.config;

import com.viettel.kpi.getDataSql.common.CommonLogServer;
import com.viettel.kpi.getDataSql.common.CommonMapQueryLogServer;
import com.viettel.kpi.getDataSql.common.CommonSvQuery;
import com.viettel.kpi.service.common.DataTypes.eDataType;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author qlmvt_dungnt44
 */
public class GetDataJob extends DataJob {

    private CommonMapQueryLogServer mapQueryServer;
    private String dbFile;// Đường dẫn db file tương ứng với module lấy dữ liệu
    private String dbCommon; // Đường dẫn db file common
    private Logger logger;
    private Timestamp startTime; // Thời gian bắt đầu lấy dữ liệu
    private Timestamp endTime; // Thời gian kết thúc lấy dữ liệu 
    private Integer numberHourReturnData;
    private MyDbTaskClient dbClientConn; // Lấy dữ liệu, insert, update dữ liệu tương ứng với module.
    private MyDbTask dbCommonConn; // db lấy dữ liệu ở common
    private MyClient client;//client lấy dữ liệu từ OSS
    private boolean isManual = false;

    public void initConnectionClient() throws Exception {
        dbClientConn = new MyDbTaskClient();
        dbClientConn.init(dbFile);

    }

    public void initConnectionCommon() throws Exception {
        dbCommonConn = new MyDbTask();
        dbCommonConn.init(dbCommon);
    }

    public void initClientConnection(CommonLogServer logServer) throws Exception {
        try {
            client = new MyClient();
            if (logServer.getProtocol().trim().equalsIgnoreCase("SYBASE")) {
                client.connectSybase(logServer);
            } else if (logServer.getProtocol().trim().equalsIgnoreCase("ORACLE")) {
                client.connectOracle(logServer);
            } else if (logServer.getProtocol().trim().equalsIgnoreCase("MYSQL")) {
                client.connectMySQL(logServer);
            } else if (logServer.getProtocol().trim().equalsIgnoreCase("SQLSERVER")) {
                client.connectSQLServer(logServer);
            } else if (logServer.getProtocol().trim().equalsIgnoreCase("PostgreSQL")) {
                client.connectPostgreSQL(logServer);
            }
        } catch (Exception ex) {
//            String resul = dbCommonConn.getExceptionContent(ex);
//            String path = dbCommonConn.getServerInfo();
//            if (resul == null || resul.equals("")) {
//                dbCommonConn.insertDataError(logServer, null, null, 2, "LOST_CONNECTION", path);
//            } else {
//                dbCommonConn.insertDataError(logServer, null, null, 2, resul, path);
//            }
//            throw ex;
            logger.error(ex.getMessage(), ex);
        }
    }

    public void releaseClientConnection() throws Exception {
        try {
            if (client != null) {
                client.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public GetDataJob(CommonMapQueryLogServer mapQueryServer, String dbFile, Logger logger) {
        this.mapQueryServer = mapQueryServer;
        this.dbFile = dbFile;
        this.logger = logger;
    }

    public GetDataJob(CommonMapQueryLogServer mapQueryServer, String dbFile, String dbCommon, Timestamp startTime, Timestamp endTime, Logger logger, long maxBatchSize) {
        this.mapQueryServer = mapQueryServer;
        this.dbFile = dbFile;
        this.dbCommon = dbCommon;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isManual = true;
        this.logger = logger;
        Start.maxBatchSize = maxBatchSize;
    }

    @Override
    public void run() {
        logger.info("==================Start Process ==========");
        boolean manualCheck = this.isManual;
        try {
            boolean checkRaw = false;
            if (isManual) {
                checkRaw = true;
            }
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
                int module_id = mapQueryServer.getModuleId();
//                logger.info("Module_ID: " + module_id);
                if (module_id == -2 || module_id == -12 || module_id == -11) {
                    dbFile = Start.dbFileAccess2G;
                    logger.info("Module 2G");
                } else if (module_id == -3 || module_id == -10) {
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
//                    dbFile = Start.dbFileNss;
//                    logger.info("Module NSS");
                    dbFile = Start.dbFileCommon;
                    logger.info("Module KPI");
                }

            }

            int step = stepNext;
            Timestamp startTimeRunning = new Timestamp(new Date().getTime());
            boolean isHourLevelData = false;

            CommonLogServer logServer = null;
            CommonSvQuery svQuery = null;
            Map<String, eDataType> columnsDataType = new HashMap<String, eDataType>();
            Map columnMap = new HashMap();
//        List<Integer> listStep = new ArrayList<Integer>();
            List<String> listClientColName = new ArrayList();
            try {
                //init username, password toi database NPMS
                initConnectionCommon();
                initConnectionClient();

                //lay thong tin tu common_log_server
                logServer = dbCommonConn.getLogServer(mapQueryServer.getLogServerId());//done
                //lay thong tin tu common_sv_query
                svQuery = dbCommonConn.getCommonSvQuery(mapQueryServer.getQueryId());//done
                String shortTblName = svQuery.getTableName().toLowerCase().trim();
                if (shortTblName.contains(".")) {
                    shortTblName = shortTblName.split("\\.")[1];
                }
                svQuery.setShortTableName(shortTblName);
                listClientColName = dbCommonConn.getListColumnName(shortTblName, logger);
                if (svQuery.getDataLevel() == 1) {
                    //neu du lieu muc gio
//                listStep = getStepRun(mapQueryServer.getIntervalHour(), stepNext);
                    isHourLevelData = true;
                }

                columnMap = dbCommonConn.getColumnMap(mapQueryServer.getQueryId(), columnsDataType);//done
            } catch (Exception e) {
                //Timestamp updateTime = new Timestamp(mapQueryServer.getEndTimeData().getTime() + mapQueryServer.getIntervalHour() * 60 * 60 * 1000);
                logger.error(mapQueryServer.getQueryId() + ": " + e.toString());
                e.printStackTrace();
                throw e;
            }

            boolean checkFirstRun = true;
            //cap nhat trang thai khi dang chay la = 1
            if (checkFirstRun) {
                //Init ket noi toi database client

                checkFirstRun = false;
                logger.info("Update is running query_id: " + mapQueryServer.getQueryId() + ", log_id: " + mapQueryServer.getLogServerId() + " = 1");
                dbCommonConn.updateIsRunning(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId(), true);//done   

            }

            //cap nhat thoi gian chay lan cuoi
            startTimeRunning = getStartRunningTime(mapQueryServer.getIntervalHour(), mapQueryServer.getHourRunInDay());
            if (!checkRaw) {
                dbCommonConn.updateEndTimeRun(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId(), new Timestamp((new Date()).getTime()));
            }

            try {
//        int stepIndex = 0;            
                logger.info("is manual:" + isManual);
                //Khong phai la manual thi moi xoa du lieu cu            
                if (!isManual) {
                    //<editor-fold defaultstate="collapsed" desc="delete old data">

                    if (svQuery.getPreviousDayDel() != null) {
                        try {
                            logger.info("data level: " + svQuery.getDataLevel());
                            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                            if (svQuery.getDataLevel() == 1) {//Neu la du lieu muc gio thi xoa luc 23h (1 là giờ: 0 là 30 phut: 2 là 1 ngày)

                                //Du lieu muc gio thi chi xoa khi vao ban dem
                                if (hour == 23) {
                                    logger.info("Delete previous data: older than " + svQuery.getPreviousDayDel() + " days...");
                                    dbClientConn.deleteDataPrevious(svQuery.getPreviousDayDel(),
                                            svQuery.getTableName(), new Timestamp((new Date()).getTime()));//done                            
                                }
                            } else {
                                if (svQuery.getDataLevel() == 2) {//Muc ngay thi chi xoa luc bang thoi gian chay lan dau
                                    if (hour == mapQueryServer.getHourRunInDay()) {
                                        logger.info("Delete previous data: older than " + svQuery.getPreviousDayDel() + " days...");
                                        dbClientConn.deleteDataPrevious(svQuery.getPreviousDayDel(),
                                                svQuery.getTableName(), new Timestamp((new Date()).getTime()));//done                            
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(GetDataJob.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    //</editor-fold>
                    if (Start.isDeleteData) {
                        try {
                            logger.info("Delete data: newer than " + startTime + "...");
                            dbClientConn.deleteContaintNewDataBeforeInsert(svQuery.getDataLevel(), svQuery.getTableName(), startTime,
                                    mapQueryServer.getLogServerName(), mapQueryServer.getQueryId(), listClientColName);//done                            
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(GetDataJob.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    //Neu la Manual thi xoa du lieu trong khoang chay                    
                    logger.info("Delete exist data: between " + startTime + " and " + endTime);
                    dbClientConn.deleteOldDataInManual(svQuery.getTableName(), startTime, endTime,
                            mapQueryServer.getLogServerName(), mapQueryServer.getQueryId());//done
                }
//        Timestamp endTimeFix = new Timestamp(endTime.getTime());

                Timestamp endTimeDataUpdate = null;
                if (mapQueryServer.getEndTimeData() != null) {
                    endTimeDataUpdate = new Timestamp(mapQueryServer.getEndTimeData().getTime());
                }

                initClientConnection(logServer);
                if (logServer != null && svQuery != null) {
                    while (isManual || isRunnable(startTimeRunning, endTime, startTime, mapQueryServer.getTimeReturnNow(), checkRaw)) {
                        try {
                            if (dbFile != null) {
                                // Băt đầu lấy dữ liệu
//                                logger.info("----------------------------------------");
                                logger.info(" Query ID    : " + svQuery.getQueryId() + " LogServer ID    : " + mapQueryServer.getLogServerId());
                                logger.info(" Table Name : " + svQuery.getTableName());
                                logger.info(" Start time: " + startTime);
                                logger.info(" End time: " + endTime);
//                                logger.info("----------------------------------------");
                                int totalRecord = client.getData(svQuery, startTime, endTime, columnMap, columnsDataType,
                                        dbCommonConn, dbClientConn, logger, mapQueryServer, startTimeRunning, listClientColName);

                                logger.info("totalRecord " + totalRecord);
//                                if (totalRecord > 0) {
                                endTimeDataUpdate = checkTime(endTimeDataUpdate, startTime);
                                if (endTimeDataUpdate.compareTo(startTime) == 0) {
                                    dbCommonConn.updateEndTimeData(mapQueryServer, startTime);
                                    if (!checkRaw) {
                                        dbCommonConn.updateEndTimeRun(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId(), new Timestamp((new Date()).getTime()));
                                    }
                                }
//                                    else {
//                                    }
//                                }
                            }
                        } catch (Exception e) {
                            //Timestamp updateTime = new Timestamp(mapQueryServer.getEndTimeData().getTime() + mapQueryServer.getIntervalHour() * 60 * 60 * 1000);
                            logger.error(mapQueryServer.getQueryId() + ": " + e.toString());
                            e.printStackTrace();
                            if (e.toString().contains("Connection reset")) {
                                endTime = new Timestamp(removeMinSecMil(new Date(startTime.getTime())).getTime());
                                startTime = new Timestamp(removeMinSecMil(new Date(endTime.getTime())).getTime()
                                        - step * 60 * 60 * 1000);
                                logger.error("Chay lai " + endTime + " do mat ket noi");
                            }
                        } finally {
                            if (isHourLevelData) {
                                if (checkRaw) {
                                    startTime = new Timestamp(removeMinSecMil(new Date(startTime.getTime())).getTime()
                                            + step * 60 * 60 * 1000);
                                } else {
                                    startTime = new Timestamp(removeMinSecMil(new Date(endTime.getTime())).getTime());
                                    endTime = new Timestamp(removeMinSecMil(new Date(startTime.getTime())).getTime()
                                            + step * 60 * 60 * 1000);
                                }

                            } else {
                                if (checkRaw) {
                                    startTime = new Timestamp(removeMinSecMil(new Date(startTime.getTime())).getTime()
                                            + step * 60 * 60 * 1000);
                                } else {
                                    startTime = new Timestamp(removeMinSecMil(new Date(endTime.getTime())).getTime());
                                    endTime = new Timestamp(removeMinSecMil(new Date(startTime.getTime())).getTime()
                                            + step * 60 * 60 * 1000);
                                }
                            }
                        }
                        isManual = false;
                    }

                }
            } catch (Exception ex) {
                logger.error(ex);
                ex.printStackTrace();
            } finally {
                releaseClientConnection();
            }

            if (!checkFirstRun) {
                logger.info("Update is running query_id: " + mapQueryServer.getQueryId() + ", log_id: " + mapQueryServer.getLogServerId() + " = 0");
                dbCommonConn.updateIsRunning(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId(), false);//done
            }

        } catch (Exception ex) {
            logger.info(ex);
            ex.printStackTrace();
        } finally {
            if (!manualCheck) {
                Start.decreaseThreadSizeUsed();
            } else {
                com.viettel.kpi.reloadData.raw.Start.decreaseThreadSizeUsed();
            }
            logger.info("==================End Process==========");
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
    private boolean isRunnable(Timestamp currentTime, Timestamp endTime, Timestamp startTime, int timeReturn, boolean checkRaw) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentTime.getTime());
        cal.add(Calendar.HOUR_OF_DAY, 0 - timeReturn);

        if (endTime.compareTo(new Timestamp(cal.getTimeInMillis())) > 0) {
            return false;
        }
        if (checkRaw) {
            if (startTime.compareTo(endTime) >= 0) {
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

    private Timestamp checkTime(Timestamp endTimeData, Timestamp endTimeDataNew) {
        Timestamp temp = null;
        if (endTimeData != null) {
            if (endTimeDataNew.compareTo(endTimeData) > 0) {
                temp = endTimeDataNew;
            } else {
                temp = endTimeData;
            }
        } else {
            temp = endTimeDataNew;
        }
        return temp;
    }
}
