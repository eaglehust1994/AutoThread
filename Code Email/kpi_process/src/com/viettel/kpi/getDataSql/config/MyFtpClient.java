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
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.log4j.Logger;
import com.viettel.kpi.client.ftp.FtpClient;

/**
 *
 * @author qlmvt_dongnd3
 */
public class MyFtpClient extends FtpClient {

    /**
     * Thuc hien ham parse va goi ham insert du lieu
     */
    public synchronized int parse(Map<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> columnMap,
            CommonSvFtpFile svFtpFile, Timestamp startTime, Timestamp endTime, Timestamp startTimeRunning,
            Map<String, eDataType> columnsDataType, MyDbTask dbCommon,
            MyDbTaskClient dbLocal, Logger logger, CommonLogServer commonLogServer,
            List<String> listClientColumnName, CommonMapQueryLogServer mapQueryServer, List<String> listClientColNameIsNotNull, boolean checkRaw, Integer moduleID) throws Exception {

        logger.info("File ID    : " + svFtpFile.getFileId() + "; File Name  : " + svFtpFile.getRemoteFileName());
        logger.info("LogServer ID    : " + commonLogServer.getLogServerId() + "Table Name : " + svFtpFile.getTableName() + "Start Time : " + startTime);

        //B1: getFile from ftp
        Map<Timestamp, List<Map<String, InputStream>>> listIs = getFile(svFtpFile, startTime, endTime, logger, commonLogServer, moduleID, dbCommon);
        //B2: parse file & insert data
        Parser parser = new Parser();
        Timestamp endTimeData = null;
        if (mapQueryServer.getEndTimeData() != null) {
            endTimeData = new Timestamp(mapQueryServer.getEndTimeData().getTime());
        }
        for (Map.Entry<Timestamp, List<Map<String, InputStream>>> is : listIs.entrySet()) {
            if (is != null) {

                if (svFtpFile.getFileType().equalsIgnoreCase("xlsx")) {
                    if (parser.parseXlsx(is.getValue(), is.getKey(), columnMap, svFtpFile,
                            columnsDataType, dbCommon, dbLocal, logger, commonLogServer, startTimeRunning, listClientColumnName, listClientColNameIsNotNull)) {
                        endTimeData = checkTime(endTimeData, is.getKey());
                    } else {
                        endTimeData = null;
                    }
                } else if (svFtpFile.getFileType().equalsIgnoreCase("xls")) {
                    if (parser.parseXls(is.getValue(), is.getKey(), columnMap, svFtpFile,
                            columnsDataType, dbCommon, dbLocal, logger, commonLogServer, startTimeRunning, listClientColumnName, listClientColNameIsNotNull)) {
                        endTimeData = checkTime(endTimeData, is.getKey());
                    } else {
                        endTimeData = null;
                    }
                } else if (svFtpFile.getFileType().equalsIgnoreCase("csv")) {
                    if (parser.parseCsv(is.getValue(), is.getKey(), columnMap, svFtpFile,
                            columnsDataType, dbCommon, dbLocal, logger, commonLogServer, startTimeRunning, listClientColumnName, listClientColNameIsNotNull)) {
                        endTimeData = checkTime(endTimeData, is.getKey());
                    } else {
                        endTimeData = null;
                    }
                } else {
                    logger.error("File type khong dung dinh dang");
                }

                if (endTimeData != null) {
                    dbCommon.updateEndTimeData(mapQueryServer, endTimeData);
                }
                if (endTimeData != null && !checkRaw) {
                    dbCommon.updateEndTimeRun(mapQueryServer.getQueryId(), mapQueryServer.getLogServerId(), new Timestamp((new Date()).getTime()));
                }
            } else {
//                logger.error("Khong lay duoc file tu server FTP");
            }
        }

        return 0;
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

    /**
     * Thuc hien getfile tu FTP theo cau hinh Tu svFtpFile ==> Ten file (+
     * format neu co) + duong dan (+format neu no) Replace format theo ngay/gio
     * chay (tu start den end), ta se lay duoc mot danh sach cac file, tra ve
     * danh sach nay
     */
    private Map<Timestamp, List<Map<String, InputStream>>> getFile(CommonSvFtpFile svFtpFile, Timestamp startTime, Timestamp endTime, Logger logger, CommonLogServer commonLogServer, Integer moduleId, MyDbTask dbCommon) {

        List<Map<String, InputStream>> inputStream = null;
        Map<Timestamp, List<Map<String, InputStream>>> mapFile = new HashMap<Timestamp, List<Map<String, InputStream>>>();

        try {
            String remoteFileName = svFtpFile.getRemoteFileName();
            String remoteNameFormat = svFtpFile.getRemoteNameFormat();
            String remoteFilePath = svFtpFile.getRemoteFilePath();
            String remotePathFormat = svFtpFile.getRemotePathFormat();

            int period = (int) Math.floor((endTime.getTime() - startTime.getTime()) / (60 * 60 * 1000));

            if (period > 0) {
                Timestamp timestamp = new Timestamp(startTime.getTime());
                if (svFtpFile.getDataLevel() == 1) {
                    String oldFolder = null;
                    String oldFileName = null;
                    for (int i = 0; i < period; i++) {
                        timestamp = addHour(timestamp, i);

                        String folder = svFtpFile.getRemoteFilePath();
                        if (remotePathFormat != null && !"".equals(remotePathFormat.trim())) {
                            folder = remoteFilePath.replace(remotePathFormat, date2String(new Date(timestamp.getTime()), remotePathFormat));
                        } else {
                            folder = remoteFilePath.replace("yyyyMMdd", date2String(new Date(timestamp.getTime()), "yyyyMMdd"));
                        }

                        String fileName = svFtpFile.getRemoteFileName();
                        if (remoteNameFormat != null && !"".equals(remoteNameFormat.trim())) {
                            fileName = remoteFileName.replace(remoteNameFormat, date2String(new Date(timestamp.getTime()), remoteNameFormat));
                        } else {
                            fileName = remoteFileName.replace("yyyyMMdd", date2String(new Date(timestamp.getTime()), "yyyyMMdd"));
                        }

                        if (folder != null && fileName != null
                                && oldFolder != null && oldFileName != null
                                && folder.equalsIgnoreCase(oldFolder)
                                && fileName.equalsIgnoreCase(oldFileName)) {
                            continue;
                        }
                        oldFolder = folder.trim();
                        oldFileName = fileName.trim();
                        String localStore = svFtpFile.getLocalStorePath();

                        inputStream = getFileStream(folder, fileName, logger, localStore, timestamp, svFtpFile.getLocalPathFormat(), true, commonLogServer);
                        if (inputStream == null || inputStream.isEmpty()) {
                            dbCommon.writelogLostData(fileName, moduleId, commonLogServer.getLogServerName(), startTime);
                        }
                        mapFile.put(timestamp, inputStream);
                    }
                } else {

                    boolean check = true;
                    Timestamp timestampAdd = new Timestamp(timestamp.getTime());
                    String folder = svFtpFile.getRemoteFilePath();
                    if (remotePathFormat != null && !"".equals(remotePathFormat.trim())) {
                        if (remotePathFormat.contains("+")) {
                            check = false;
                            String[] addDate = remotePathFormat.split("+");
                            String n = "";
                            try {
                                n = addDate[1].trim();
                                remotePathFormat = addDate[0].trim();
                                if (!n.equals("")) {
                                    timestampAdd = new Timestamp(timestamp.getTime());
                                    timestampAdd.setDate(timestampAdd.getDate() + Integer.parseInt(n));
                                    folder = remoteFilePath.replace(remotePathFormat, date2String(new Date(timestampAdd.getTime()), remotePathFormat));
                                }

                            } catch (Exception e) {
                            }


                        } else {
                            folder = remoteFilePath.replace(remotePathFormat, date2String(new Date(timestamp.getTime()), remotePathFormat));
                        }
                        if (folder.charAt(folder.length() - 1) == '/') {
                            folder = folder.substring(0, folder.length() - 1);
                        }
                    } else {
                        folder = remoteFilePath.replace("yyyyMMdd", date2String(new Date(timestamp.getTime()), "yyyyMMdd"));
                        if (folder.charAt(folder.length() - 1) == '/') {
                            folder = folder.substring(0, folder.length() - 1);
                        }
                    }
                    String fileName = svFtpFile.getRemoteFileName();
                    if (remoteNameFormat != null && !"".equals(remoteNameFormat.trim())) {
                        if (remoteNameFormat.contains("+")) {
                            check = false;
                            String[] addDate = remoteNameFormat.split("\\+");
                            String n = "";
                            try {
                                n = addDate[1].trim();
                                remoteNameFormat = addDate[0].trim();
                                if (!n.equals("")) {
                                    timestampAdd = new Timestamp(timestamp.getTime());
                                    timestampAdd.setDate(timestampAdd.getDate() + Integer.parseInt(n));
                                    fileName = remoteFileName.replace(remoteNameFormat, date2String(new Date(timestampAdd.getTime()), remoteNameFormat));
                                }

                            } catch (Exception e) {
                            }


                        } else {
                            fileName = remoteFileName.replace(remoteNameFormat, date2String(new Date(timestamp.getTime()), remoteNameFormat));
                        }
                    } else {
                        fileName = remoteFileName.replace("yyyyMMdd", date2String(new Date(timestamp.getTime()), "yyyyMMdd"));
                    }
                    String localStore = svFtpFile.getLocalStorePath();
                    if (check) {
                        inputStream = getFileStream(folder, fileName, logger, localStore, timestamp, svFtpFile.getLocalPathFormat(), true, commonLogServer);
                    } else {
                        inputStream = getFileStream(folder, fileName, logger, localStore, timestampAdd, svFtpFile.getLocalPathFormat(), true, commonLogServer);
                    }
                    if (inputStream == null || inputStream.isEmpty()) {
                        dbCommon.writelogLostData(fileName, moduleId, commonLogServer.getLogServerName(), startTime);
                    }
                    mapFile.put(timestamp, inputStream);
                }
//                logger.info("So luong file lay duoc: " + mapFile.size());
            } else {
                logger.error("Thoi gian ket thuc truoc thoi gian bat dau !!!");
            }
        } catch (Exception ex) {
            logger.error("Khong thuc hien lay duoc file FTP: " + ex.getMessage(), ex);
            ex.printStackTrace();
        } finally {
        }

        return mapFile;
    }

    private Timestamp addHour(Timestamp time, int hour) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime());
        cal.add(Calendar.HOUR_OF_DAY, hour);

        return new Timestamp(cal.getTimeInMillis());
    }

    public String date2String(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }
}
