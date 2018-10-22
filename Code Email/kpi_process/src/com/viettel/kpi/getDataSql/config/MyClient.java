/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.config;

import com.viettel.kpi.client.database.DbClient;
import com.viettel.kpi.getDataSql.common.CommonMapQueryLogServer;
import com.viettel.kpi.getDataSql.common.CommonSvQuery;
import com.viettel.kpi.getDataSql.common.MyParameters;
import com.viettel.kpi.service.common.DataTypes.eDataType;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author qlmvt_MinhHT1
 */
public class MyClient extends DbClient {

//    public List<String> getRemoteColumns(List<String> lstColumnLocal, Map columnMap) {
//        List<String> lstRemoteColumns = new ArrayList<String>();
//        for (String strColumn : lstColumnLocal) {
//            String remoteColumn = (String) columnMap.get(strColumn);
//            if (remoteColumn != null && !remoteColumn.trim().equals("")) {
//                lstRemoteColumns.add(remoteColumn.toUpperCase());
//            }
//        }
//        return lstRemoteColumns;
//    }
    public synchronized int getData(CommonSvQuery svQuery, Timestamp startTime, Timestamp endTime,
            Map columnMap, Map<String, eDataType> columnsDataType, MyDbTask dbCommon,
            MyDbTaskClient db, Logger logger, CommonMapQueryLogServer mapQueryLogServer,
            Timestamp startTimeRunning, List<String> listClientColName) throws Exception {

        ArrayList<MyParameters> paramsList = new ArrayList<MyParameters>();
        PreparedStatement pre_stmt = null;
        ResultSet rs = null;
        Connection con = null;
        int totalRecord = 0;
        List lstColumsPk = db.getColumnsPk(svQuery.getShortTableName());
        int countRun = 0;
        countRun = dbCommon.getCountRun(mapQueryLogServer.getLogServerId(), mapQueryLogServer.getQueryId(), startTime);
        dbCommon.writelog(startTime, new Timestamp((new Date()).getTime()), mapQueryLogServer.getQueryId(),
                mapQueryLogServer.getLogServerId(), countRun,
                "Bat dau chay queryId " + mapQueryLogServer.getQueryId() + " o server " + mapQueryLogServer.getLogServerId(), "Info", null);
        String logQueryId = com.viettel.kpi.reloadData.raw.Start.logQueryId;
        List lstLogQueryId = Arrays.asList(logQueryId.split(","));
        logger.info("logQueryId: " + logQueryId);
        // 1 là giờ: 0 là 30 phut: 2 là 1 ngày
        try {
            if (svQuery.getDataLevel() == 2) {
                //tinh toan count_run
                //vi chay du lieu theo ngay nen update_time fai set la ngay startTime
//                countRun = dbCommon.getCountRun(mapQueryLogServer.getLogServerId(), mapQueryLogServer.getQueryId(), removeTimeTS(startTime));
//                if (countRun == 1) {
//                    dbCommon.writelog(startTime, startTimeRunning, mapQueryLogServer.getQueryId(),
//                            mapQueryLogServer.getLogServerId(), countRun,
//                            "Bat dau chay " + mapQueryLogServer.getQueryId() + " o server " + mapQueryLogServer.getLogServerId(), "Info", null);
//                }

//                List<String> mapCheckPk = new ArrayList<String>();
                HashMap<String, String> hmCheckPk = new HashMap<String, String>();
                //<editor-fold defaultstate="collapsed" desc="Lấy dữ liệu mức ngày">
                pre_stmt = null;
//                con = getConnection();
                logger.info("Query: " + svQuery.getQueryText());
                pre_stmt = connection.prepareStatement(svQuery.getQueryText().toString());
                logger.info("pre_stmt: " + pre_stmt);
                String[] temps = (svQuery.getQueryText() + " ").split("\\?");
                for (int i = 1; i < temps.length; i++) {
                    if (i % 2 == 1) {
                        pre_stmt.setTimestamp(i, startTime);
                    } else {
                        pre_stmt.setTimestamp(i, endTime);
                    }
                }
                rs = pre_stmt.executeQuery();
                logger.info("rs: " + rs);
                logger.info("finish executeQuery(), begin map value.....");
                Map newColumnMap = columnMap;
                Map<String, eDataType> newColumnsDataType = columnsDataType;
//
//                // customize Parameter
//                if (svQuery.getCustomerClass() != null) {
//                    newColumnMap = Invoker.changeColumnMap(svQuery.getCustomerClass(), columnMap);
//                    newColumnsDataType = Invoker.changeColumnDataType(svQuery.getCustomerClass(), columnsDataType);
//                }
                int count = 0;
                Object objRemove = null;
                while (rs.next()) {
                    //Remove
                    if (objRemove != null) {
                        columnMap.remove(objRemove);
                        objRemove = null;
                    }
                    MyParameters param = new MyParameters();
                    Set entries = columnMap.entrySet();
                    Iterator it = entries.iterator();
                    // Giá trị khóa chính dùng để kiểm tra trong hashMap
                    String strPkValue = "";
                    while (it.hasNext()) {
                        Map.Entry entry = (Map.Entry) it.next();
                        Object value = new Object();
                        try {
                            value = rs.getObject(entry.getValue().toString());
                            //dungvv8_log_query_06012015
                            if (lstLogQueryId.contains(String.valueOf(mapQueryLogServer.getQueryId()))) {
                                logger.info("entry.getValue().toString() :" + entry.getValue().toString());
                                logger.info("value :" + value);
                            }
                            if (lstColumsPk.contains(entry.getKey().toString().toUpperCase())) {
                                strPkValue += value.toString().toUpperCase() + ";;";
                            }
                            if (value instanceof Date) {
                                value = rs.getTimestamp(entry.getValue().toString());
                            }
                            param.add(entry.getKey().toString().toUpperCase(), value);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (null != entry.getValue()) {
                                logger.error(svQuery.getTableName() + ": rs.getObject + " + entry.getValue().toString() + ": " + e.getMessage());
                                //Remove
                                objRemove = entry.getKey();
                            } else {
                                logger.error(svQuery.getTableName() + ": rs.getObject + : " + e.getMessage());
                            }
                        }
                    }
                    if (svQuery.getCustomerClass() != null) {
                        callCustomizeClass(svQuery.getCustomerClass(), dbCommon, startTime, endTime, mapQueryLogServer.getLogServerId(), logger);
                    }
                    /*
                     * kiêm tra xem khoa đã tồn tại hay chưa?? Nếu đã tồn tại
                     * thì bỏ qua không insert bản ghi này vào nữa Nếu bảng
                     * không có khóa chính thì insert toàn bộ bản ghi
                     */
                    if (!strPkValue.trim().equals("")) {
                        if (hmCheckPk.containsKey(strPkValue)) {
                            logger.debug("Trung key:" + strPkValue);
                            continue;
                        } else {
                            hmCheckPk.put(strPkValue, "1");
                            paramsList.add(param);
                        }
                    } else {
                        paramsList.add(param);
                    }

                    if (paramsList.size() >= Start.maxBatchSize) {
                        try {
                            logger.info("insert lan thu : " + (++count));
                            logger.info("So ban ghi them moi  : " + paramsList.size());
                            totalRecord += paramsList.size();
//                        logServices.write(Start.serviceCode, Long.valueOf(query.getID())," Số bản ghi: " + paramsList.size() , startTime, 2);
                            db.insertData(svQuery.getTableName(), paramsList, newColumnMap, newColumnsDataType, mapQueryLogServer.getLogServerName(), mapQueryLogServer.getQueryId(), true, listClientColName);
                            logger.info("End insert data");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Timestamp now = new Timestamp(new Date().getTime());
                            dbCommon.updateLog(startTime, startTimeRunning, now, mapQueryLogServer.getQueryId(),
                                    mapQueryLogServer.getLogServerId(), countRun, "insert " + count + " failed: " + e.getMessage(), "Error");
                            logger.error("insert " + count + " failed: " + e.getMessage());
                            throw e;
                        } finally {
                            paramsList.clear();
                            logger.info("Start add data to paramsList");
                        }

                    }
                }

                // insert dữ liệu lần cuối cùng
                try {
                    logger.info("insert lan cuoi: ");
                    logger.info("So ban ghi them moi  : " + paramsList.size());
                    db.insertData(svQuery.getTableName(), paramsList, newColumnMap, newColumnsDataType, mapQueryLogServer.getLogServerName(), mapQueryLogServer.getQueryId(), true, listClientColName);
                    totalRecord += paramsList.size();
                    logger.info("TOTAL RECORD  : " + totalRecord);
                    //ghi log vao bang common_sv_log_services
                    Timestamp now = new Timestamp(new Date().getTime());
                    if (totalRecord > 0) {
                        dbCommon.updateLog(startTime, startTimeRunning, now, mapQueryLogServer.getQueryId(),
                                mapQueryLogServer.getLogServerId(), countRun, "Tổng số bản ghi insert là: " + totalRecord, "Info");
                    } else {
                        dbCommon.updateLog(startTime, startTimeRunning, now, mapQueryLogServer.getQueryId(),
                                mapQueryLogServer.getLogServerId(), countRun, "Tien trinh chay khong co du lieu,tong so ban ghi = 0", "Warning");
                    }
                    // Goi lop
                    if (svQuery.getCustomerClass() != null) {
                        callCustomizeClass(svQuery.getCustomerClass(), dbCommon, startTime, endTime, mapQueryLogServer.getLogServerId(), logger);
                    }
                    if (svQuery.getStoredName() != null) {
                        callStoredname(svQuery.getStoredName(), startTime, endTime, mapQueryLogServer.getLogServerId(), db, logger);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Timestamp now = new Timestamp(new Date().getTime());
                    dbCommon.updateLog(startTime, startTimeRunning, now, mapQueryLogServer.getQueryId(),
                            mapQueryLogServer.getLogServerId(), countRun, "insert lan cuoi failed: " + e.getMessage(), "Error");
                    logger.error("insert lan cuoi failed: " + e.getMessage());
                    throw e;
                } finally {
                    paramsList.clear();
                }
                //    //</editor-fold>
            } else if (svQuery.getDataLevel() == 1) {
                //<editor-fold defaultstate="collapsed" desc="Lấy dữ liệu mức giờ">
                // Hiện tại đang để mặc định khoảng thời gian là 2h một lần lấy dữ liệu;
                Timestamp startTimeData = new Timestamp(startTime.getTime());
                Timestamp endTimeData = null;
                pre_stmt = null;
                pre_stmt = connection.prepareStatement(svQuery.getQueryText());

                List<Integer> lstStepRun = getStepRun(Start.number_hour_get_data, mapQueryLogServer.getRunStepNext());
                for (Integer step : lstStepRun) {
                    totalRecord = 0;
                    HashMap<String, String> hmCheckPk = new HashMap<String, String>();

                    //tinh toan lai startTime theo startTime ban dau
                    endTimeData = new Timestamp(startTimeData.getTime());
//                    logger.info("Begin get data hourly - queryID: " + svQuery.getQueryName() + " " + startTimeData.toString());

                    //tinh toan count_run dua tren startTime va endTime                                        
                    String[] temps = (svQuery.getQueryText() + " ").split("\\?");
                    // Truyền tham số giờ
                    int startHourData = getHourFromTS(startTimeData);
                    for (int i = 1; i < temps.length; i++) {
                        if (i % 4 == 1) {
                            pre_stmt.setTimestamp(i, removeTimeTS(startTimeData));
                        } else if (i % 4 == 2) {
                            pre_stmt.setTimestamp(i, removeTimeAdd1TS(endTimeData));
                        } else if (i % 4 == 3) {
                            pre_stmt.setInt(i, startHourData);
                        } else {
                            pre_stmt.setInt(i, startHourData + step);// Sua Start.number_hour_get_data ==> step                            
                        }
                    }
                    rs = pre_stmt.executeQuery();
//                    logger.info("finish executeQuery(), begin map value.....");
                    Map newColumnMap = columnMap;
                    Map<String, eDataType> newColumnsDataType = columnsDataType;
                    int count = 0;
                    Object objRemove = null;
                    while (rs.next()) {
                        //Remove
                        if (objRemove != null) {
                            columnMap.remove(objRemove);
                            objRemove = null;
                        }
                        MyParameters param = new MyParameters();
                        Set entries = columnMap.entrySet();
                        Iterator it = entries.iterator();
                        // Giá trị khóa chính dùng để kiểm tra trong hashMap
                        String strPkValue = "";
                        while (it.hasNext()) {
                            Map.Entry entry = (Map.Entry) it.next();
                            Object value = new Object();
                            try {
                                value = rs.getObject(entry.getValue().toString());
                                if (lstColumsPk.contains(entry.getKey().toString().toUpperCase())) {
                                    strPkValue += value.toString().toUpperCase() + ";;";
                                }
                                if (value instanceof Date) {
                                    value = rs.getTimestamp(entry.getValue().toString());
                                }
                                param.add(entry.getKey().toString().toUpperCase(), value);
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (null != entry.getValue()) {
                                    logger.error(svQuery.getTableName() + ": rs.getObject + " + entry.getValue().toString() + ": " + e.getMessage());
                                    //Remove
                                    objRemove = entry.getKey();
                                } else {
                                    logger.error(svQuery.getTableName() + ": rs.getObject + : " + e.getMessage());
                                }
                            }
                        }
                        /*
                         * kiêm tra xem khoa đã tồn tại hay chưa?? Nếu đã tồn
                         * tại thì bỏ qua không insert bản ghi này vào nữa Nếu
                         * bảng không có khóa chính thì insert toàn bộ bản ghi
                         */
                        if (!strPkValue.trim().equals("")) {
                            if (hmCheckPk.containsKey(strPkValue)) {
                                logger.debug("Trung key:" + strPkValue);
                                continue;
                            } else {
                                hmCheckPk.put(strPkValue, "1");
                                paramsList.add(param);
                            }
                        } else {
                            paramsList.add(param);
                        }

                        if (paramsList.size() >= Start.maxBatchSize) {
                            try {
                                logger.info("insert lan thu : " + (++count));
                                logger.info("So ban ghi them moi  : " + paramsList.size());
                                totalRecord += paramsList.size();
//                        logServices.write(Start.serviceCode, Long.valueOf(query.getID())," Số bản ghi: " + paramsList.size() , startTime, 2);
                                db.insertData(svQuery.getTableName(), paramsList, newColumnMap, newColumnsDataType, mapQueryLogServer.getLogServerName(), mapQueryLogServer.getQueryId(), true, listClientColName);
                                logger.info("End insert data");
                            } catch (Exception e) {
                                e.printStackTrace();
                                Timestamp now = new Timestamp(new Date().getTime());
                                dbCommon.updateLog(startTime, startTimeRunning, now, mapQueryLogServer.getQueryId(),
                                        mapQueryLogServer.getLogServerId(), countRun, "insert " + count + " failed: " + e.getMessage(), "Error");
                                logger.error("insert " + count + " failed: " + e.getMessage());
                                throw e;
                            } finally {
                                paramsList.clear();
                                logger.info("Start add data to paramsList");
                            }

                        }
                    }
                    rs.close();

                    // insert dữ liệu lần cuối cùng
                    try {
                        logger.info("insert lan cuoi: ");
                        logger.info("So ban ghi them moi  : " + paramsList.size());
                        db.insertData(svQuery.getTableName(), paramsList, newColumnMap, newColumnsDataType, mapQueryLogServer.getLogServerName(), mapQueryLogServer.getQueryId(), true, listClientColName);
                        totalRecord += paramsList.size();
                        logger.info("TOTAL RECORD  : " + totalRecord);

                        //ghi log vao bang common_sv_log_services
                        Timestamp now = new Timestamp(new Date().getTime());
                        if (totalRecord > 0) {
                            dbCommon.updateLog(startTime, startTimeRunning, now, mapQueryLogServer.getQueryId(),
                                    mapQueryLogServer.getLogServerId(), countRun, "Tổng số bản ghi insert là: " + totalRecord, "Info");
                        } else {
                            dbCommon.updateLog(startTime, startTimeRunning, now, mapQueryLogServer.getQueryId(),
                                    mapQueryLogServer.getLogServerId(), countRun, "Tien trinh chay khong co du lieu,tong so ban ghi = 0", "Warning");
                        }
                        // Goi lop
                        if (svQuery.getCustomerClass() != null) {
                            callCustomizeClass(svQuery.getCustomerClass(), dbCommon, startTime, endTime, mapQueryLogServer.getLogServerId(), logger);
                        }
                        if (svQuery.getStoredName() != null) {
                            callStoredname(svQuery.getStoredName(), startTime, endTime, mapQueryLogServer.getLogServerId(), db, logger);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Timestamp now = new Timestamp(new Date().getTime());
                        dbCommon.updateLog(startTime, startTimeRunning, now, mapQueryLogServer.getQueryId(),
                                mapQueryLogServer.getLogServerId(), countRun, "insert lan cuoi failed: " + e.getMessage(), "Error");
                        logger.error("insert lan cuoi failed: " + e.getMessage());
                        throw e;
                    } finally {
                        paramsList.clear();
                    }

                    startTimeData = new Timestamp(endTimeData.getTime());
                }
                //</editor-fold>
            }
        } catch (Exception ex) {
            Timestamp now = new Timestamp(new Date().getTime());
            dbCommon.updateLog(startTime, startTimeRunning, now, mapQueryLogServer.getQueryId(),
                    mapQueryLogServer.getLogServerId(), countRun, ex.toString(), "Error");
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (pre_stmt != null) {
                    pre_stmt.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return totalRecord;
    }

    /**
     * Gọi hàm thuộc lớp sau khi lấy dữ liệu xong
     */
    private String callCustomizeClass(String className, MyDbTask dbCommon, Timestamp startTime, Timestamp endTime, Long logServerId, Logger logger) {

        try {
            logger.info("Call Customize Class : " + className.trim());
            logger.info("Connection : " + dbCommon);
            logger.info("startTime : " + startTime);
            logger.info("endTime : " + endTime);
            logger.info("Log Server ID : " + logServerId);
            Class[] argTypes = new Class[1];
            argTypes[0] = Map.class;
            Method mainMethod = Class.forName(className).getDeclaredMethod("start", argTypes);
            Object[] argListForInvokedMain = new Object[1];
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("update_time", startTime);

            argListForInvokedMain[0] = paramMap;
            return (String) mainMethod.invoke(null, argListForInvokedMain);
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Xác định số bước nhảy của hàm. một bước nhảy lớn nhất là bằng
     * number_hour_get_data. Ví dụ: Nếu number_hour_get_data = 4, intervalHour =
     * 6 => Trả về list<4,2>;
     */
    private static List<Integer> getStepRun(int number_hour_get_data, int intervalHour) {

        //neu thoi gian chay theo gio ma duoc cau hinh = 0 thi set lai gia tri mac dinh
        if (number_hour_get_data == 0) {
            number_hour_get_data = 1;
        }
        List<Integer> listStep = new ArrayList<Integer>();
        if (number_hour_get_data >= intervalHour) {
            listStep.add(intervalHour);
        } else {
            int modNumber = intervalHour % number_hour_get_data;
            int step = (intervalHour - modNumber) / number_hour_get_data;

            for (int i = 0; i < step; i++) {
                listStep.add(number_hour_get_data);
            }

            if (modNumber != 0) {
                listStep.add(modNumber);
            }
        }

        return listStep;
    }

    /**
     * Gọi stored khi chay xong lay dữ liệu
     */
    private void callStoredname(String storeName, Timestamp startTime, Timestamp endTime, Long LogServerId, MyDbTaskClient db, Logger logger) {
        try {
            logger.info("Call Stored Name : " + storeName.trim());
            logger.info("startTime : " + startTime);
            logger.info("endTime : " + endTime);
            logger.info("LogServerId : " + LogServerId);
            db.executeStored(storeName, startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Timestamp removeTimeTS(Timestamp ts) {

        Date date = new Date(ts.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTime().getTime());
    }

    private Timestamp removeTimeAdd1TS(Timestamp ts) {

        Date date = new Date(ts.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return new Timestamp(cal.getTime().getTime());
    }

    private int getHourFromTS(Timestamp ts) {

        Date date = new Date(ts.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.HOUR_OF_DAY);
    }

}
