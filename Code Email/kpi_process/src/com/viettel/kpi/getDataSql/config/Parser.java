/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.config;

import au.com.bytecode.opencsv.CSVReader;
import com.viettel.kpi.getDataSql.common.CommonLogServer;
import com.viettel.kpi.getDataSql.common.CommonSvFtpColumnMap;
import com.viettel.kpi.getDataSql.common.CommonSvFtpColumnMapPK;
import com.viettel.kpi.getDataSql.common.CommonSvFtpFile;
import com.viettel.kpi.getDataSql.common.MyParameters;
import com.viettel.kpi.service.common.DataTypes;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author os_namndh
 */
public class Parser {

    private int rowHeader = 0;

    /**
     *
     * @param fileStream
     * @param updateTime
     * @param columnMap
     * @param svFtpFile
     * @param columnsDataType
     * @param dbLocal
     * @param logger
     * @param commonLogServer
     * @param startTimeRunning
     */
    public boolean parseXlsx(List<Map<String, InputStream>> lstFileStream, Timestamp updateTime,
            Map<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> columnMap,
            CommonSvFtpFile svFtpFile, Map<String, DataTypes.eDataType> columnsDataType,
            MyDbTask dbCommon, MyDbTaskClient dbLocal, Logger logger, CommonLogServer commonLogServer,
            Timestamp startTimeRunning, List<String> listClientColumnName, List<String> listClientColumnNameIsNotNull) throws Exception {

        boolean check = false;
        rowHeader = svFtpFile.getRowHeader();
        long logServerId = commonLogServer.getLogServerId();
        String logServerName = commonLogServer.getLogServerName();
        int totalRecords = 0;
        ArrayList<MyParameters> paramsList = new ArrayList<MyParameters>();
        XSSFWorkbook workbook = null;
        List<String> lstColumsPk = dbLocal.getColumnsPk(svFtpFile.getTableName());
        int countRun = 0;
        countRun = dbCommon.getCountRun(commonLogServer.getLogServerId(), svFtpFile.getFileId(), updateTime);
        if (lstFileStream == null || lstFileStream.isEmpty()) {
//                logger.error("Khong lay duoc du lieu tu FTP");
            dbCommon.writelog(updateTime, new Timestamp((new Date()).getTime()), svFtpFile.getFileId(),
                    commonLogServer.getLogServerId(), countRun, "Khong lay duoc du lieu tu FTP", "Warning", new Timestamp((new Date()).getTime()));
//                logger.error("Khong lay duoc du lieu tu FTP");
            return check;
        }
        String strInfo = "";
        for (int ii = 0; ii < lstFileStream.size(); ii++) {
            Map<String, InputStream> mapListFileStream = lstFileStream.get(ii);
            InputStream fileStream = null;
            //String filename = null;
            for (Map.Entry<String, InputStream> entry : mapListFileStream.entrySet()) {
                fileStream = entry.getValue();
                //filename = entry.getKey();
            }
            try {

                if (ii == 0) {
                    dbCommon.writelog(updateTime, new Timestamp((new Date()).getTime()), svFtpFile.getFileId(),
                            commonLogServer.getLogServerId(), countRun, "Bat dau chay " + svFtpFile.getFileId() + " o server " + commonLogServer.getLogServerName(), "Info", null);
                }
                workbook = (XSSFWorkbook) WorkbookFactory.create(fileStream);
                workbook.setMissingCellPolicy(XSSFRow.CREATE_NULL_AS_BLANK);



                if (workbook != null) {
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    int rowCount = sheet.getLastRowNum();

                    //lay ra cac cot mac dinh tu file excel hoac gia tri default
                    MyParameters paramFix = new MyParameters();
                    for (Map.Entry<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> entry : columnMap.entrySet()) {
                        CommonSvFtpColumnMapPK columnMapPK = entry.getKey();
                        CommonSvFtpColumnMap columnMapObj = entry.getValue();

                        //day la loai column du lieu: FIX, DEFAULT, NORMAL, MERGE
                        String colType = columnMapObj.getColType();

                        String value = null;
                        if (colType.equalsIgnoreCase("FIX")) {
                            //neu gia tri cot la cot duoc fix san gia tri o 1 cell trong file
                            XSSFRow row = sheet.getRow(columnMapObj.getRowIdx().intValue());
                            row.getCell(columnMapObj.getColIdx().intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                            value = row.getCell(columnMapObj.getColIdx().intValue()).toString().trim();
                        } else if (colType.equalsIgnoreCase("DEFAULT")) {
                            //neu la kieu cot la default thi lay tu default_value
//                        value = columnMapObj.getDefaultValue();
                        } else if (colType.equalsIgnoreCase("SEQUENCE")) {
                            Long sequenceValue = dbCommon.getSequenceValue(columnMapObj.getDefaultValue());
                            if (sequenceValue != null) {
                                value = sequenceValue.toString();
                            }
                        }

                        boolean isAble2Insert = true;
                        //check black list
                        //neu gia tri co tu nam trong black list thi ko insert
                        if (columnMapObj.getBlacklistValue() != null && value != null) {
                            String[] temps = columnMapObj.getBlacklistValue().split("\\;");
                            for (String str : temps) {
                                if (value.toLowerCase().contains(str.toLowerCase())) {
                                    if (columnMapObj.getBlacklistIgnoreInsert() == 1) {
                                        isAble2Insert = false;
                                    } else {
                                        value = null;
                                    }
                                    break;
                                }
                            }
                        }

                        //replace cac ki tu nam trong ignore_character
                        if (columnMapObj.getIgnoreCharacter() != null && value != null) {
                            String[] temps = columnMapObj.getIgnoreCharacter().split("\\;");
                            for (String str : temps) {
                                if (value.toLowerCase().contains(str.toLowerCase())) {
                                    value = value.replace(str, "");
                                }
                            }
                        }

                        if (isAble2Insert) {
                            paramFix.add(columnMapPK.getLocalColumn(), getObjectValue(svFtpFile, columnMapObj, updateTime, value, logger));
                        }
                    }

                    //doc lan luot tung dong trong file excel
                    //neu col_type = Normal hoac Merge thi se co xu ly tuong ung
                    XSSFRow oldRow = null;
                    Map<String, MyParameters> mapParams = new HashMap();
                    for (int i = rowHeader; i <= rowCount; i++) {
                        try {
                            MyParameters parameter = new MyParameters();
                            boolean isAble2Insert = true;

                            for (Map.Entry<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> entry : columnMap.entrySet()) {
                                CommonSvFtpColumnMapPK columnMapPK = entry.getKey();
                                CommonSvFtpColumnMap columnMapObj = entry.getValue();

                                String colType = columnMapObj.getColType();
                                Long colNo = columnMapObj.getColIdx();
                                if (colNo == null) {
                                    colNo = -1L;
                                }
                                //bien value luu gia tri cua cell tuong ung
                                String value = null;

                                if (colType.equalsIgnoreCase("NORMAL")) {
                                    if (!colNo.equals(-1L)) {
                                        //neu la cot NORMAL thi get theo (colNo,i)
                                        XSSFRow row = sheet.getRow(i);
                                        if (columnMapObj.getDataType().toString().equals("DATE")) {
                                            try {
                                                Date dateTime = row.getCell(colNo.intValue()).getDateCellValue();
                                                String cellFomat = columnMapObj.getCellDataFormat();
                                                value = DateTimeUtils.convertDateToString(dateTime, cellFomat);
                                            } catch (Exception e) {
                                                row.getCell(colNo.intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                                                value = row.getCell(colNo.intValue()).toString().trim();
                                            }

                                        } else {
                                            row.getCell(colNo.intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                                            value = row.getCell(colNo.intValue()).toString().trim();
                                        }

                                    } else {
                                        logger.error("Chua cau hinh col_idx cho cot: " + columnMapPK.getLocalColumn());
                                    }
                                } else if (colType.equalsIgnoreCase("MERGE")) {
                                    if (!colNo.equals(-1L)) {
                                        //neu la cot merge thi lay gia tri cua dong dau tien cua cot merge
                                        XSSFRow row = sheet.getRow(i);
                                        row.getCell(colNo.intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                                        value = row.getCell(colNo.intValue()).toString().trim();
                                        if (value != null && value.length() == 0) {
                                            //oldRowNo luu cac vi tri dong truoc do
                                            int oldRowNo = i;

                                            //duyet nguoc lai cac dong truoc do de lay gia tri
                                            do {
                                                oldRow = sheet.getRow(--oldRowNo);
                                            } while (oldRow != null
                                                    && oldRowNo > rowHeader
                                                    && oldRow.getCell(colNo.intValue()).toString().length() == 0);

                                            //ko lay noi dung dong header
                                            if (oldRowNo > rowHeader) {
                                                value = oldRow.getCell(colNo.intValue()).toString().trim();
                                            }
                                        }
                                    } else {
                                        logger.error("Chua cau hinh col_idx cho cot: " + columnMapPK.getLocalColumn());
                                    }
                                } else if (colType.equalsIgnoreCase("TIMESTAMPT")) {
                                    if (!colNo.equals(-1L)) {
                                        XSSFRow row = sheet.getRow(i);
                                        if (columnMapObj.getDataType().toString().equals("DATE")) {
                                            try {
                                                row.getCell(colNo.intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                                                Long temp = Long.valueOf(row.getCell(colNo.intValue()).toString().trim());
                                                String cellFomat = columnMapObj.getCellDataFormat();
                                                value = DateTimeUtils.convertDateToString(new Date(temp), cellFomat);
                                            } catch (Exception e) {
                                                row.getCell(colNo.intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                                                value = row.getCell(colNo.intValue()).toString().trim();
                                            }

                                        } else {
                                            row.getCell(colNo.intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                                            value = row.getCell(colNo.intValue()).toString().trim();
                                        }

                                    } else {
                                        logger.error("Chua cau hinh col_idx cho cot: " + columnMapPK.getLocalColumn());
                                    }
                                } else {
                                    //neu la cac gia tri mac dinh: FIX hoac DEFAULT
                                    //thi them cot vao dong du lieu can insert
                                    parameter.setValue(columnMapPK.getLocalColumn(), paramFix.getValue(columnMapPK.getLocalColumn()));
                                    continue;
                                }

                                //check black list
                                //neu gia tri co tu nam trong black list thi ko insert
                                if (columnMapObj.getBlacklistValue() != null && value != null) {
                                    String[] temps = columnMapObj.getBlacklistValue().split("\\;");
                                    for (String str : temps) {
                                        if (value.toLowerCase().contains(str.toLowerCase())) {
                                            if (columnMapObj.getBlacklistIgnoreInsert() == 1) {
                                                isAble2Insert = false;
                                            } else {
                                                value = null;
                                            }
                                            break;
                                        }
                                    }
                                }

                                //replace cac ki tu nam trong ignore_character
                                if (columnMapObj.getIgnoreCharacter() != null && value != null) {
                                    String[] temps = columnMapObj.getIgnoreCharacter().split("\\;");
                                    for (String str : temps) {
                                        if (value.toLowerCase().contains(str.toLowerCase())) {
                                            value = value.replace(str, "");
                                        }
                                    }
                                }

                                //set gia tri cho cot tuong ung
                                Object objValue = getObjectValue(svFtpFile, columnMapObj, updateTime, value, logger);
                                parameter.setValue(columnMapPK.getLocalColumn(), objValue);
                            }

                            //check co bi trung khoa chinh hay khong
                            if (isAble2Insert) {
                                if (checkDuplicate(mapParams, parameter, lstColumsPk)) {
                                    paramsList.add(parameter);
                                }
                            }

                            //kiem tra xem danh sach cac ban ghi da lon hon batch chua
                            //lon hon hoac bang thi se insert du lieu vao database
                            if (paramsList.size() >= Start.maxBatchSize) {
                                logger.info("insert number record  : " + paramsList.size());
                                totalRecords += paramsList.size();
                                dbLocal.insertDataFtp(svFtpFile.getTableName(), paramsList, columnMap, columnsDataType, logServerName, svFtpFile.getFileId(), true, listClientColumnName, listClientColumnNameIsNotNull, logger);
                                paramsList.clear();
                            }
                        } catch (Exception ex) {
                            logger.error(ex.toString());
                            ex.printStackTrace();
                            paramsList.clear();
                        }
                    }

                    try {
                        if (paramsList.size() > 0) {
                            totalRecords += paramsList.size();
                            logger.info("insert lan cuoi: " + paramsList.size());
                            dbLocal.insertDataFtp(svFtpFile.getTableName(), paramsList, columnMap, columnsDataType, logServerName, svFtpFile.getFileId(), true, listClientColumnName, listClientColumnNameIsNotNull, logger);
                            paramsList.clear();
                        }
                        logger.info("TOTAL RECORDS  : " + totalRecords);

                        //ghi log vao bang common_sv_log_services                                           

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("insert lan cuoi failed: " + e.getMessage());
                    } finally {
                        paramsList.clear();
                    }
                } else {
//                    Timestamp now = new Timestamp(new Date().getTime());
//                    dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
//                            commonLogServer.getLogServerId(), countRun, "Khong lay duoc du lieu tu file excel!", "Error");
                    logger.error("Khong lay duoc du lieu tu file excel!!");
                }
            } catch (Exception e) {
                logger.error(e.toString());
                try {
                    Timestamp now = new Timestamp(new Date().getTime());
                    dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                            commonLogServer.getLogServerId(), countRun, e.toString(), "Error");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                try {
                    if (fileStream != null) {
                        fileStream.close();
                    }
                } catch (Exception e) {
                    logger.error(e.toString());
                    e.printStackTrace();
                }
            }
        }
        Timestamp now = new Timestamp(new Date().getTime());
        if (totalRecords > 0) {
            check = true;
            dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                    commonLogServer.getLogServerId(), countRun, "Tổng số bản ghi insert là: " + totalRecords, "Info");
        } else {
            dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                    commonLogServer.getLogServerId(), countRun, "Tien trinh chay khong co du lieu,tong so ban ghi = 0 ", "Warning");
        }
        return check;
    }

    /**
     *
     * @param fileStream
     * @param updateTime
     * @param columnMap
     * @param svFtpFile
     * @param columnsDataType
     * @param dbLocal
     * @param logger
     * @param commonLogServer
     * @param startTimeRunning
     */
    public boolean parseXls(List<Map<String, InputStream>> listFileStream, Timestamp updateTime,
            Map<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> columnMap,
            CommonSvFtpFile svFtpFile, Map<String, DataTypes.eDataType> columnsDataType,
            MyDbTask dbCommon, MyDbTaskClient dbLocal, Logger logger, CommonLogServer commonLogServer,
            Timestamp startTimeRunning, List<String> listClientColumnName, List<String> listClientColumnNameIsNotNull) throws Exception {

        boolean check = false;
        rowHeader = svFtpFile.getRowHeader();
        long logServerId = commonLogServer.getLogServerId();
        String logServerName = commonLogServer.getLogServerName();
        int totalRecords = 0;
        ArrayList<MyParameters> paramsList = new ArrayList<MyParameters>();
        HSSFWorkbook workbook = null;

        List<String> lstColumsPk = dbLocal.getColumnsPk(svFtpFile.getTableName());
        int countRun = 0;
//        Bắt đầu chạy + query_name ở server + log_server_name
        countRun = dbCommon.getCountRun(commonLogServer.getLogServerId(), svFtpFile.getFileId(), updateTime);
        if (listFileStream == null || listFileStream.isEmpty()) {
//                logger.error("Khong lay duoc du lieu tu FTP");
            dbCommon.writelog(updateTime, new Timestamp((new Date()).getTime()), svFtpFile.getFileId(),
                    commonLogServer.getLogServerId(), countRun, "Khong lay duoc du lieu tu FTP", "Warning", new Timestamp((new Date()).getTime()));

            return check;
        }
        for (int ii = 0; ii < listFileStream.size(); ii++) {
            Map<String, InputStream> mapListFileStream = listFileStream.get(ii);
            InputStream fileStream = null;
            //String filename = null;
            for (Map.Entry<String, InputStream> entry : mapListFileStream.entrySet()) {
                fileStream = entry.getValue();
                //filename = entry.getKey();
            }
            try {
                if (ii == 0) {
                    dbCommon.writelog(updateTime, new Timestamp((new Date()).getTime()), svFtpFile.getFileId(),
                            commonLogServer.getLogServerId(), countRun, "Bat dau chay " + svFtpFile.getFileId() + " o server " + commonLogServer.getLogServerName(), "Info", null);
                }
                workbook = new HSSFWorkbook(fileStream);
                workbook.setMissingCellPolicy(HSSFRow.CREATE_NULL_AS_BLANK);

                if (workbook != null) {
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    int rowCount = sheet.getLastRowNum();

                    //lay ra cac cot mac dinh tu file excel hoac gia tri default
                    MyParameters paramFix = new MyParameters();
                    for (Map.Entry<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> entry : columnMap.entrySet()) {
                        CommonSvFtpColumnMapPK columnMapPK = entry.getKey();
                        CommonSvFtpColumnMap columnMapObj = entry.getValue();

                        String colType = columnMapObj.getColType();

                        String value = null;
                        if (colType.equalsIgnoreCase("FIX")) {
                            //neu gia tri cot la cot duoc fix san gia tri o 1 cell trong file
                            HSSFRow row = sheet.getRow(columnMapObj.getRowIdx().intValue());
                            row.getCell(columnMapObj.getColIdx().intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                            value = row.getCell(columnMapObj.getColIdx().intValue()).toString().trim();
                        } else if (colType.equalsIgnoreCase("DEFAULT")) {
                            //neu la kieu cot la default thi lay tu default_value
//                        value = columnMapObj.getDefaultValue();
                        } else if (colType.equalsIgnoreCase("SEQUENCE")) {
                            Long sequenceValue = dbCommon.getSequenceValue(columnMapObj.getDefaultValue());
                            if (sequenceValue != null) {
                                value = sequenceValue.toString();
                            }
                        }

                        boolean isAble2Insert = true;
                        //check black list
                        //neu gia tri co tu nam trong black list thi ko insert
                        if (columnMapObj.getBlacklistValue() != null && value != null) {
                            String[] temps = columnMapObj.getBlacklistValue().split("\\;");
                            for (String str : temps) {
                                if (value.toLowerCase().contains(str.toLowerCase())) {
                                    if (columnMapObj.getBlacklistIgnoreInsert() == 1) {
                                        isAble2Insert = false;
                                    } else {
                                        value = null;
                                    }
                                    break;
                                }
                            }
                        }

                        //replace cac ki tu nam trong ignore_character
                        if (columnMapObj.getIgnoreCharacter() != null && value != null) {
                            String[] temps = columnMapObj.getIgnoreCharacter().split("\\;");
                            for (String str : temps) {
                                if (value.toLowerCase().contains(str.toLowerCase())) {
                                    value = value.replace(str, "");
                                }
                            }
                        }

                        if (isAble2Insert) {
                            paramFix.add(columnMapPK.getLocalColumn(), getObjectValue(svFtpFile, columnMapObj, updateTime, value, logger));
                        }
                    }

                    //doc lan luot tung dong trong file excel
                    //neu col_type = Normal hoac Merge thi se co xu ly tuong ung
                    HSSFRow oldRow = null;
                    Map<String, MyParameters> mapParams = new HashMap();
                    for (int i = rowHeader; i <= rowCount; i++) {
                        try {
                            MyParameters parameter = new MyParameters();
                            boolean isAble2Insert = true;

                            for (Map.Entry<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> entry : columnMap.entrySet()) {
                                CommonSvFtpColumnMapPK columnMapPK = entry.getKey();
                                CommonSvFtpColumnMap columnMapObj = entry.getValue();

                                String colType = columnMapObj.getColType();
                                Long colNo = columnMapObj.getColIdx();
                                if (colNo == null) {
                                    colNo = -1L;
                                }
                                //bien value luu gia tri cua cell tuong ung
                                String value = null;

                                if (colType.equalsIgnoreCase("NORMAL")) {
                                    if (!colNo.equals(-1L)) {
                                        //neu la cot NORMAL thi get theo (colNo,i)
                                        HSSFRow row = sheet.getRow(i);

                                        if (columnMapObj.getDataType().toString().equals("DATE")) {
                                            try {
                                                Date dateTime = row.getCell(colNo.intValue()).getDateCellValue();
                                                String cellFomat = columnMapObj.getCellDataFormat();
                                                value = DateTimeUtils.convertDateToString(dateTime, cellFomat);
                                            } catch (Exception e) {
                                                row.getCell(colNo.intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                                                value = row.getCell(colNo.intValue()).toString().trim();
                                            }

                                        } else {
                                            row.getCell(colNo.intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                                            value = row.getCell(colNo.intValue()).toString().trim();
                                        }

                                    } else {
                                        logger.error("Chua cau hinh col_idx cho cot: " + columnMapPK.getLocalColumn());
                                    }
                                } else if (colType.equalsIgnoreCase("MERGE")) {
                                    if (!colNo.equals(-1L)) {
                                        //neu la cot merge thi lay gia tri cua dong dau tien cua cot merge
                                        HSSFRow row = sheet.getRow(i);
                                        row.getCell(colNo.intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                                        value = row.getCell(colNo.intValue()).toString().trim();
                                        if (value != null && value.length() == 0) {
                                            //oldRowNo luu cac vi tri dong truoc do
                                            int oldRowNo = i;

                                            //duyet nguoc lai cac dong truoc do de lay gia tri
                                            do {
                                                oldRow = sheet.getRow(--oldRowNo);
                                            } while (oldRow != null
                                                    && oldRowNo > rowHeader
                                                    && oldRow.getCell(colNo.intValue()).toString().length() == 0);

                                            //ko lay noi dung dong header
                                            if (oldRowNo > rowHeader) {
                                                value = oldRow.getCell(colNo.intValue()).toString().trim();
                                            }
                                        }
                                    } else {
                                        logger.error("Chua cau hinh col_idx cho cot: " + columnMapPK.getLocalColumn());
                                    }
                                } else if (colType.equalsIgnoreCase("TIMESTAMPT")) {
                                    if (!colNo.equals(-1L)) {
                                        HSSFRow row = sheet.getRow(i);
                                        if (columnMapObj.getDataType().toString().equals("DATE")) {
                                            try {
                                                row.getCell(colNo.intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                                                Long temp = Long.valueOf(row.getCell(colNo.intValue()).toString().trim());
                                                String cellFomat = columnMapObj.getCellDataFormat();
                                                value = DateTimeUtils.convertDateToString(new Date(temp), cellFomat);
                                            } catch (Exception e) {
                                                row.getCell(colNo.intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                                                value = row.getCell(colNo.intValue()).toString().trim();
                                            }

                                        } else {
                                            row.getCell(colNo.intValue()).setCellType(HSSFCell.CELL_TYPE_STRING);
                                            value = row.getCell(colNo.intValue()).toString().trim();
                                        }

                                    } else {
                                        logger.error("Chua cau hinh col_idx cho cot: " + columnMapPK.getLocalColumn());
                                    }
                                } else {
                                    //neu la cac gia tri mac dinh: FIX hoac DEFAULT
                                    //thi them cot vao dong du lieu can insert
                                    parameter.setValue(columnMapPK.getLocalColumn(), paramFix.getValue(columnMapPK.getLocalColumn()));
                                    continue;
                                }

                                //check black list
                                //neu gia tri co tu nam trong black list thi ko insert
                                if (columnMapObj.getBlacklistValue() != null && value != null) {
                                    String[] temps = columnMapObj.getBlacklistValue().split("\\;");
                                    for (String str : temps) {
                                        if (value.toLowerCase().contains(str.toLowerCase())) {
                                            if (columnMapObj.getBlacklistIgnoreInsert() == 1) {
                                                isAble2Insert = false;
                                            } else {
                                                value = null;
                                            }
                                            break;
                                        }
                                    }
                                }

                                //replace cac ki tu nam trong ignore_character
                                if (columnMapObj.getIgnoreCharacter() != null && value != null) {
                                    String[] temps = columnMapObj.getIgnoreCharacter().split("\\;");
                                    for (String str : temps) {
                                        if (value.toLowerCase().contains(str.toLowerCase())) {
                                            value = value.replace(str, "");
                                        }
                                    }
                                }

                                //set gia tri cho cot tuong ung
                                Object objValue = getObjectValue(svFtpFile, columnMapObj, updateTime, value, logger);
                                parameter.setValue(columnMapPK.getLocalColumn(), objValue);
                            }

                            //check co bi trung khoa chinh hay khong
                            if (isAble2Insert) {
                                if (checkDuplicate(mapParams, parameter, lstColumsPk)) {
                                    paramsList.add(parameter);
                                }
                            }

                            //kiem tra xem danh sach cac ban ghi da lon hon batch chua
                            //lon hon hoac bang thi se insert du lieu vao database
                            if (paramsList.size() >= Start.maxBatchSize) {
                                logger.info("insert number record  : " + paramsList.size());
                                totalRecords += paramsList.size();
                                dbLocal.insertDataFtp(svFtpFile.getTableName(), paramsList, columnMap, columnsDataType, logServerName, svFtpFile.getFileId(), true, listClientColumnName, listClientColumnNameIsNotNull, logger);
                                paramsList.clear();
                            }
                        } catch (Exception ex) {
                            paramsList.clear();
                            logger.error(ex.toString());
//                        ex.printStackTrace();
                            //throw ex;
                        }
                    }

                    try {
                        totalRecords += paramsList.size();
                        logger.info("insert lan cuoi: " + paramsList.size());
                        dbLocal.insertDataFtp(svFtpFile.getTableName(), paramsList, columnMap, columnsDataType, logServerName, svFtpFile.getFileId(), true, listClientColumnName, listClientColumnNameIsNotNull, logger);
                        paramsList.clear();
                        logger.info("TOTAL RECORDS  : " + totalRecords);

                        //ghi log vao bang common_sv_log_services
//                    Timestamp now = new Timestamp(new Date().getTime());

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("insert lan cuoi failed: " + e.getMessage());
                    } finally {
                        paramsList.clear();
                    }
                } else {
                    Timestamp now = new Timestamp(new Date().getTime());
                    dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                            commonLogServer.getLogServerId(), countRun, "Khong lay duoc du lieu tu file excel!", "Error");
                }

            } catch (Exception e) {
                logger.error(e.toString());
                try {
                    Timestamp now = new Timestamp(new Date().getTime());
                    dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                            commonLogServer.getLogServerId(), countRun, e.toString(), "Error");


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                try {
                    if (fileStream != null) {
                        fileStream.close();
                    }
                } catch (Exception e) {
                    logger.error(e.toString());
                    e.printStackTrace();
                }
            }
        }
        if (totalRecords > 0) {
            check = true;
            Timestamp now = new Timestamp(new Date().getTime());
            dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                    commonLogServer.getLogServerId(), countRun, "Tổng số bản ghi insert là: " + totalRecords, "Info");
        } else {
            Timestamp now = new Timestamp(new Date().getTime());
            dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                    commonLogServer.getLogServerId(), countRun, "Tien trinh chay khong co du lieu,tong so ban ghi = 0", "Info");
        }
        return check;
    }

    /**
     *
     * @param fileStream
     * @param updateTime
     * @param columnMap
     * @param svFtpFile
     * @param columnsDataType
     * @param dbLocal
     * @param logger
     * @param commonLogServer
     * @param startTimeRunning
     */
    public boolean parseCsv(List<Map<String, InputStream>> listFileStream, Timestamp updateTime,
            Map<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> columnMap,
            CommonSvFtpFile svFtpFile, Map<String, DataTypes.eDataType> columnsDataType,
            MyDbTask dbCommon, MyDbTaskClient dbLocal, Logger logger, CommonLogServer commonLogServer,
            Timestamp startTimeRunning, List<String> listClientColumnName, List<String> listClientColumnNameIsNotNull) throws Exception {
        boolean check = false;
        rowHeader = svFtpFile.getRowHeader();
        long logServerId = commonLogServer.getLogServerId();
        String logServerName = commonLogServer.getLogServerName();
        int totalRecords = 0;
        ArrayList<MyParameters> paramsList = new ArrayList<MyParameters>();

        List<String> lstColumsPk = dbLocal.getColumnsPk(svFtpFile.getTableName());
        int countRun = 0;
        countRun = dbCommon.getCountRun(commonLogServer.getLogServerId(), svFtpFile.getFileId(), updateTime);
        //o day dung opencsv de doc file CSV
        dbCommon.writelog(updateTime, new Timestamp((new Date()).getTime()), svFtpFile.getFileId(),
                commonLogServer.getLogServerId(), countRun, " ", "Info", null);
        CSVReader reader = null;
        //  logger.info("Vao day roi");
        if (listFileStream != null) {
            for (int ii = 0; ii < listFileStream.size(); ii++) {
                //logger.info("Bat dau for List");
                Map<String, InputStream> mapListFileStream = listFileStream.get(ii);
                InputStream fileStream = null;
                String filename = null;
                for (Map.Entry<String, InputStream> entry : mapListFileStream.entrySet()) {
                    fileStream = entry.getValue();
                    filename = entry.getKey();
                }
                try {
                    if (fileStream != null) {
//                if (countRun == 1) {                   
//                }
                        Character delimiter = ',';
                        Character delimiterTab = '\t';
                        if (svFtpFile.getDelimiter() != null && !"".equals(svFtpFile.getDelimiter())) {
                            delimiter = svFtpFile.getDelimiter().toCharArray()[0];
                            if (svFtpFile.getDelimiter().length() == 2) {
                                delimiterTab = svFtpFile.getDelimiter().toCharArray()[1];
                            }
                        }
                        if (delimiterTab.equals('t')) {
                            reader = new CSVReader(new InputStreamReader(fileStream), '\t');
                        } else {
                            reader = new CSVReader(new InputStreamReader(fileStream), delimiter);
                        }

                        if (reader != null) {
                            //doc tat ca du lieu tu file csv va dua vao trong list
                            List<String[]> listCSVData = reader.readAll();

                            if (listCSVData != null && !listCSVData.isEmpty()) {
                                //lay ra cac cot mac dinh tu file excel hoac gia tri default
                                MyParameters paramFix = new MyParameters();
                                for (Map.Entry<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> entry : columnMap.entrySet()) {
                                    CommonSvFtpColumnMapPK columnMapPK = entry.getKey();
                                    CommonSvFtpColumnMap columnMapObj = entry.getValue();

                                    String colType = columnMapObj.getColType();

                                    String value = null;
                                    if (colType.equalsIgnoreCase("FIX")) {
                                        //neu gia tri cot la cot duoc fix san gia tri o 1 cell trong file
                                        if (columnMapObj.getRowIdx().intValue() < listCSVData.size()) {
                                            String row[] = listCSVData.get(columnMapObj.getRowIdx().intValue());
                                            if (columnMapObj.getColIdx().intValue() < row.length) {
                                                //dungvv8 code them phan cau hinh lay du lieu tu file_name
                                                if (columnMapObj.getDefaultValue().toString().trim().equalsIgnoreCase("FILE_NAME")) {
                                                    String regex = columnMapObj.getCellDataFormat().trim();
                                                    value = getValueInBlock(filename, regex, 1).trim();

                                                } else {
                                                    value = row[columnMapObj.getColIdx().intValue()].toString().trim();
                                                }
                                            } else {
                                                logger.error("Gia tri col_idx: " + columnMapObj.getColIdx().intValue()
                                                        + " > so cot co trong file CSV cua fileId: " + svFtpFile.getFileId());
                                            }
                                        } else {
                                            logger.error("Gia tri row_idx: " + columnMapObj.getRowIdx().intValue()
                                                    + " > so dong co trong file CSV cua fileId: " + svFtpFile.getFileId());
                                        }
                                    } else if (colType.equalsIgnoreCase("DEFAULT")) {
                                        //neu la kieu cot la default thi lay tu default_value
//                                value = columnMapObj.getDefaultValue();
                                    }

                                    boolean isAble2Insert = true;
                                    //check black list
                                    //neu gia tri co tu nam trong black list thi ko insert
                                    if (columnMapObj.getBlacklistValue() != null && value != null) {
                                        String[] temps = columnMapObj.getBlacklistValue().split("\\;");
                                        for (String str : temps) {
                                            if (value.toLowerCase().contains(str.toLowerCase())) {
                                                if (columnMapObj.getBlacklistIgnoreInsert() == 1) {
                                                    isAble2Insert = false;
                                                } else {
                                                    value = null;
                                                }
                                                break;
                                            }
                                        }
                                    }

                                    //replace cac ki tu nam trong ignore_character
                                    if (columnMapObj.getIgnoreCharacter() != null && value != null) {
                                        String[] temps = columnMapObj.getIgnoreCharacter().split("\\;");
                                        for (String str : temps) {
                                            if (value.toLowerCase().contains(str.toLowerCase())) {
                                                value = value.replace(str, "");
                                            }
                                        }
                                    }

                                    if (isAble2Insert) {
                                        paramFix.add(columnMapPK.getLocalColumn(), getObjectValue(svFtpFile, columnMapObj, updateTime, value, logger));
                                    }
                                }

                                //doc du lieu tung row va xu ly
                                String oldRow[] = null;
                                Map<String, MyParameters> mapParams = new HashMap();
                                if (rowHeader < listCSVData.size()) {
                                    for (int i = rowHeader; i < listCSVData.size(); i++) {
                                        try {
                                            //paramsList = new ArrayList<MyParameters>();
                                            MyParameters parameter = new MyParameters();
                                            boolean isAble2Insert = true;

                                            for (Map.Entry<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> entry : columnMap.entrySet()) {
                                                CommonSvFtpColumnMapPK columnMapPK = entry.getKey();
                                                CommonSvFtpColumnMap columnMapObj = entry.getValue();

                                                String colType = columnMapObj.getColType();
                                                Long colNo = columnMapObj.getColIdx();
                                                if (colNo == null) {
                                                    colNo = -1L;
                                                }
                                                //bien value luu gia tri cua cell tuong ung
                                                String value = null;

                                                if (colType.equalsIgnoreCase("NORMAL")) {
                                                    if (!colNo.equals(-1L)) {
                                                        //neu la cot NORMAL thi get theo (colNo,i)
                                                        String row[] = listCSVData.get(i);
                                                        try {
                                                            if (colNo.intValue() < row.length) {
                                                                value = row[colNo.intValue()].toString().trim();
                                                            } else {
                                                                logger.error("Gia tri cot " + colNo.intValue() + "> gia tri cot trong file CSV");
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        logger.error("Chua cau hinh col_idx cho cot: " + columnMapPK.getLocalColumn());
                                                    }
                                                } else if (colType.equalsIgnoreCase("MERGE")) {
                                                    if (!colNo.equals(-1L)) {
                                                        //neu la cot merge thi lay gia tri cua dong dau tien cua cot merge
                                                        String row[] = listCSVData.get(i);
                                                        value = row[colNo.intValue()].toString().trim();
                                                        if (value != null && value.length() == 0) {
                                                            //oldRowNo luu cac vi tri dong truoc do
                                                            int oldRowNo = i;

                                                            //duyet nguoc lai cac dong truoc do de lay gia tri
                                                            do {
                                                                oldRow = listCSVData.get(--oldRowNo);
                                                            } while (oldRow != null
                                                                    && oldRowNo > rowHeader
                                                                    && oldRow[colNo.intValue()].toString().length() == 0);

                                                            //ko lay noi dung dong header
                                                            if (oldRowNo > rowHeader) {
                                                                value = oldRow[colNo.intValue()].toString().trim();
                                                            }
                                                        }
                                                    } else {
                                                        logger.error("Chua cau hinh col_idx cho cot: " + columnMapPK.getLocalColumn());
                                                    }
                                                } else if (colType.equalsIgnoreCase("TIMESTAMPT")) {
                                                    if (!colNo.equals(-1L)) {
                                                        //neu la cot NORMAL thi get theo (colNo,i)
                                                        String row[] = listCSVData.get(i);
                                                        try {
                                                            if (colNo.intValue() < row.length) {
                                                                value = row[colNo.intValue()].toString().trim();
                                                                Long temp = Long.valueOf(value);
                                                                String cellFomat = columnMapObj.getCellDataFormat();
                                                                value = DateTimeUtils.convertDateToString(new Date(temp), cellFomat);
                                                            } else {
                                                                logger.error("Gia tri cot " + colNo.intValue() + "> gia tri cot trong file CSV");
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        logger.error("Chua cau hinh col_idx cho cot: " + columnMapPK.getLocalColumn());
                                                    }
                                                } else if (colType.equalsIgnoreCase("SEQUENCE")) {
                                                    Long sequenceValue = dbCommon.getSequenceValue(columnMapObj.getDefaultValue());
                                                    if (sequenceValue != null) {
                                                        value = sequenceValue.toString();
                                                    }
                                                } else {
                                                    //neu la cac gia tri mac dinh: FIX hoac DEFAULT
                                                    //thi them cot vao dong du lieu can insert
                                                    parameter.setValue(columnMapPK.getLocalColumn(), paramFix.getValue(columnMapPK.getLocalColumn()));
                                                    continue;
                                                }

                                                //check black list
                                                //neu gia tri co tu nam trong black list thi ko insert
                                                if (columnMapObj.getBlacklistValue() != null && value != null) {
                                                    String[] temps = columnMapObj.getBlacklistValue().split("\\;");
                                                    for (String str : temps) {
                                                        if (value.toLowerCase().contains(str.toLowerCase())) {
                                                            if (columnMapObj.getBlacklistIgnoreInsert() == 1) {
                                                                isAble2Insert = false;
                                                            } else {
                                                                value = null;
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }

                                                //replace cac ki tu nam trong ignore_character
                                                if (columnMapObj.getIgnoreCharacter() != null && value != null) {
                                                    String[] temps = columnMapObj.getIgnoreCharacter().split("\\;");
                                                    for (String str : temps) {
                                                        if (value.toLowerCase().contains(str.toLowerCase())) {
                                                            value = value.replace(str, "");
                                                        }
                                                    }
                                                }

                                                //set gia tri cho cot tuong ung
                                                Object objValue = getObjectValue(svFtpFile, columnMapObj, updateTime, value, logger);
                                                parameter.setValue(columnMapPK.getLocalColumn(), objValue);
                                            }

                                            //check co bi trung khoa chinh hay khong
                                            if (isAble2Insert) {
                                                if (checkDuplicate(mapParams, parameter, lstColumsPk)) {
                                                    paramsList.add(parameter);
                                                }
                                            }

                                            //kiem tra xem danh sach cac ban ghi da lon hon batch chua
                                            //lon hon hoac bang thi se insert du lieu vao database
                                            if (paramsList.size() >= Start.maxBatchSize) {

                                                logger.info("insert number record  : " + paramsList.size());
                                                totalRecords += paramsList.size();
                                                dbLocal.insertDataFtp(svFtpFile.getTableName(), paramsList, columnMap, columnsDataType, logServerName, svFtpFile.getFileId(), true, listClientColumnName, listClientColumnNameIsNotNull, logger);
                                                paramsList.clear();
                                            }
                                        } catch (Exception ex) {
                                            logger.error(ex.toString());
                                            ex.printStackTrace();
                                            paramsList.clear();
                                        }
                                    }

                                    try {
                                        totalRecords += paramsList.size();
                                        //  logger.info("insert lan cuoi: " + paramsList.size());
                                        dbLocal.insertDataFtp(svFtpFile.getTableName(), paramsList, columnMap, columnsDataType, logServerName, svFtpFile.getFileId(), true, listClientColumnName, listClientColumnNameIsNotNull, logger);
                                        paramsList.clear();
                                        logger.info("TOTAL RECORDS  : " + totalRecords);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        logger.error("insert lan cuoi failed: " + e.getMessage());
                                    } finally {
                                        paramsList.clear();
                                    }
                                } else {
                                    logger.error("Can check lai cau hinh row_header, fileId: " + svFtpFile.getFileId());
                                    Timestamp now = new Timestamp(new Date().getTime());
                                    dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                                            commonLogServer.getLogServerId(), countRun, "Can check lai cau hinh row_header, fileId: " + svFtpFile.getFileId(), "Error");
                                }
                            } else {
                                logger.error("Khong co du lieu trong file CSV, fileId: " + svFtpFile.getFileId());
                                Timestamp now = new Timestamp(new Date().getTime());
                                dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                                        commonLogServer.getLogServerId(), countRun, "Khong co du lieu trong file CSV, fileId: " + svFtpFile.getFileId(), "Error");
                            }
                        } else {
                            logger.error("Khong doc duoc file, fileId: " + svFtpFile.getFileId());
                            Timestamp now = new Timestamp(new Date().getTime());
                            dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                                    commonLogServer.getLogServerId(), countRun, "Khong doc duoc file, fileId: " + svFtpFile.getFileId(), "Error");
                        }
                    } else {
//                logger.error("Khong lay duoc file tu server, fileId: " + svFtpFile.getFileId());
//                        dbCommon.writelog(updateTime, new Timestamp((new Date()).getTime()), svFtpFile.getFileId(),
//                                commonLogServer.getLogServerId(), countRun, "Khong lay duoc file tu server, fileId: " + svFtpFile.getFileId(), "Warning", new Timestamp((new Date()).getTime()));
                        logger.warn("1234 Khong lay duoc file tu server, fileId: " + svFtpFile.getFileId());
                        Timestamp now = new Timestamp(new Date().getTime());
                        dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                                commonLogServer.getLogServerId(), countRun, "Khong lay duoc file tu server, fileId: " + svFtpFile.getFileId(), "Warning");

                    }
                } catch (Exception e) {
                    logger.error(e.toString());
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception ex) {
                            logger.error("Khong the thuc hien reader.close(): " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                    if (fileStream != null) {
                        try {
                            fileStream.close();
                        } catch (IOException ex) {
                            logger.error("Khong the thuc hien fileStream.close(): " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } else {
            Timestamp now = new Timestamp(new Date().getTime());
            dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                    commonLogServer.getLogServerId(), countRun, "Khong doc duoc file, fileId: " + svFtpFile.getFileId(), "Error");
        }
        //ghi log vao bang common_sv_log_services
        Timestamp now = new Timestamp(new Date().getTime());
        if (totalRecords > 0) {
            check = true;
            dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                    commonLogServer.getLogServerId(), countRun, "Tổng số bản ghi insert là: " + totalRecords, "Info");
        } else {
            dbCommon.updateLog(updateTime, startTimeRunning, now, svFtpFile.getFileId(),
                    commonLogServer.getLogServerId(), countRun, "Tien trinh chay khong co du lieu,tong so ban ghi = 0 ", "Warning");
        }
        return check;
    }

    public boolean checkDuplicate(Map<String, MyParameters> paramList, MyParameters param, List<String> listPrimaryKey) {

        if (listPrimaryKey.isEmpty()) {
            return true;
        }
        String key = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        for (String primaryKey : listPrimaryKey) {
            Object value = param.getValue(primaryKey);
            if (value != null) {
                if (value instanceof Date) {
                    key += sdf.format((Date) value) + "|";
                } else {
                    key += value.toString() + "|";
                }
            }
        }

        if (paramList.containsKey(key)) {
            return false;
        } else {
            paramList.put(key, param);
            return true;
        }
    }

    public Object getObjectValue(CommonSvFtpFile fileConfig, CommonSvFtpColumnMap map,
            Timestamp updateTime, String strValue, Logger logger) {

        try {
            if (strValue == null && map.getColType().equalsIgnoreCase("SEQUENCE")) {
                return null;
            }
            if (strValue == null && map.getDefaultValue() != null) {
                String colType = map.getColType();
                strValue = map.getDefaultValue().trim();

                if ((colType.equalsIgnoreCase("FIX") || colType.equalsIgnoreCase("DEFAULT"))) {
                    if (strValue.equalsIgnoreCase("DAY")) {
                        return truncTime(updateTime);
                    } else if (strValue.toUpperCase().contains("DAY+")) {
                        String temp = strValue.substring(4);
                        return new Timestamp(truncTime(updateTime).getTime() + Long.parseLong(temp));
                    } else if (strValue.equalsIgnoreCase("HOUR")) {
                        return getHour(updateTime);
                    } else if (strValue.toUpperCase().contains("HOUR+")) {
                        String temp = strValue.substring(4);
                        return getHour(updateTime) + Integer.parseInt(temp);
                    }
                }
            } else if (strValue == null || "".equals(strValue)) {
                return null;
            } else {
                strValue = strValue.trim();
            }

            DataTypes.eDataType dataType = map.getDataType();
            switch (dataType) {
                case NUM:
                    return Double.valueOf(strValue.replace(",", "").replace("\uFFFD", "").replace("'", ""));
                case STR:
                    return strValue;
                case DATE:
                    String cellFormat = "dd/MM/yyyy";
                    if (map.getCellDataFormat() != null) {
                        cellFormat = map.getCellDataFormat();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat(cellFormat);
                    Date dDate = sdf.parse(strValue);
                    Timestamp tTime = new Timestamp(dDate.getTime());

                    return tTime;
                case INT:
                    return Integer.valueOf(strValue);
                default:
                    return strValue;
            }
        } catch (Exception ex) {
            logger.error("Data field " + map.getLocalColumn() + " error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    private Timestamp truncTime(Timestamp ts) {

        Date date = new Date(ts.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTimeInMillis());
    }

    private String getValueInBlock(String candidate, String pattern, int index) {
        String value = "";


        try {
            Pattern p = Pattern.compile(pattern, Pattern.MULTILINE);
            Matcher m;
            m = p.matcher(candidate);


            while (m.find()) {
                try {
                    value = m.group(index).trim();


                } catch (Exception ex) {
                    ex.printStackTrace();


                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);


        }
        return value;


    }

    private int getHour(Timestamp time) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime());

        return cal.get(Calendar.HOUR_OF_DAY);
    }
}
