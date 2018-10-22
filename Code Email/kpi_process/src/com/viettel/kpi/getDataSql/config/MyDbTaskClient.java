/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.config;

import com.viettel.kpi.common.utils.DbTask;
import com.viettel.kpi.getDataSql.common.CommonSvFtpColumnMapPK;
import com.viettel.kpi.getDataSql.common.MyParameters;
import com.viettel.kpi.service.common.DataTypes.eDataType;
import java.sql.*;
import java.util.*;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author qlmvt_dungnt44
 */
public class MyDbTaskClient extends DbTask {

    /**
     * Xóa dữ liệu trong bảng trước đó previousDayDel ngày.
     */
    public synchronized void deleteDataPrevious(Integer previousDayDel,
            String tableName, Timestamp startTimeRunning) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = " delete from  " + tableName + " a "
                + " where a.update_time < trunc(?) ";
        try {
            con = getConnectionPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(removeTimeTS(startTimeRunning, previousDayDel).getTime()));
            pstmt.execute();
            con.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
            close(con);
        }
    }

    /**
     * loai bo ngay gio va lui thoi gian di 1 khoang previousDayDel
     */
    private Timestamp removeTimeTS(Timestamp ts, Integer previousDayDel) {

        Date date = new Date(ts.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, 0 - previousDayDel);
        return new Timestamp(cal.getTimeInMillis());
    }

    /**
     * Xoa du lieu truoc khi chay Nếu là dữ liệu mức giờ thì có trường hour_id,
     * nếu là dữ liệu mức ngày thì chỉ có trường update_time.
     */
    public synchronized void deleteOldDataInManual(String tableName,
            Date timeReRunData, Date endTimeDelete, String logServerName, long queryId) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = " delete from  " + tableName + " a "
                + " where a.update_time >= ? and a.update_time < ? "
                + " and a.query_id = ? and a.log_server_id = ?";

        try {
            con = getConnectionPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(timeReRunData.getTime()));
            pstmt.setDate(2, new java.sql.Date(endTimeDelete.getTime()));
            pstmt.setLong(3, queryId);
            pstmt.setString(4, logServerName);

            pstmt.execute();
            con.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            close(con);
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }

        }
    }

    /**
     * Xoa du lieu truoc khi chay Nếu là dữ liệu mức giờ thì có trường hour_id,
     * nếu là dữ liệu mức ngày thì chỉ có trường update_time.
     */
    public synchronized void deleteContaintNewDataBeforeInsert(int dataLevel, String tableName, Timestamp timeReRunData,
            String logServerName, long queryId, List<String> listClientColName) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;

        //Check co du lieu hay khong
        String sql = " Select * from " + tableName + " a ";
        if (dataLevel == 1) {
            sql += " where (a.update_time + a.hour_id/24) >= ? ";
        } else {
            sql += " where a.update_time >= ? ";
        }
        if (!listClientColName.isEmpty()) {
            if (listClientColName.contains("query_id")) {
                sql += " and a.query_id = ? ";
            }
            if (listClientColName.contains("log_server_id")) {
                sql += " and a.log_server_id = ? ";
            }
        }

        sql += " and rownum <2";

        boolean hasData = false;

        try {
            con = getConnectionPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setTimestamp(1, timeReRunData);
            if (!listClientColName.isEmpty()) {
                int i = 2;
                if (listClientColName.contains("query_id")) {
                    pstmt.setLong(i++, queryId);
                }
                if (listClientColName.contains("log_server_id")) {
                    pstmt.setString(i, logServerName);
                }
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                hasData = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            con = getConnectionPool();
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }

        }
        if (hasData) {

            sql = " delete from  " + tableName + " a ";
            if (dataLevel == 1) {
                sql += " where (a.update_time + hour_id/24) >= ? ";
            } else {
                sql += " where a.update_time >= ? ";
            }
            if (!listClientColName.isEmpty()) {
                if (listClientColName.contains("query_id")) {
                    sql += " and a.query_id = ? ";
                }
                if (listClientColName.contains("log_server_id")) {
                    sql += " and a.log_server_id = ? ";
                }
            }
            try {
                con = getConnectionPool();
                pstmt = con.prepareStatement(sql);
                pstmt.setTimestamp(1, timeReRunData);
                if (!listClientColName.isEmpty()) {
                    int i = 2;
                    if (listClientColName.contains("query_id")) {
                        pstmt.setLong(i++, queryId);
                    }
                    if (listClientColName.contains("log_server_id")) {
                        pstmt.setString(i, logServerName);
                    }
                }
                pstmt.execute();
                con.commit();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            } finally {
                close(con);
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw ex;
                }

            }
        }
    }

    /**
     * Xoa du lieu truoc khi chay Nếu là dữ liệu mức giờ thì có trường hour_id,
     * nếu là dữ liệu mức ngày thì chỉ có trường update_time.
     */
    public synchronized void deleteOldData(int dataLevel, String tableName, Timestamp timeReRunData,
            Timestamp endTimeDelete, String logServerName, long queryId, List<String> listClientColName) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = " delete from  " + tableName + " a ";
        if (dataLevel == 1) {
            sql += " where (a.update_time + hour_id/24) >= ? and (a.update_time + hour_id/24) < ? ";
        } else {
            sql += " where a.update_time >= ? and a.update_time < ? ";
        }
        if (!listClientColName.isEmpty()) {
            if (listClientColName.contains("query_id")) {
                sql += " and a.query_id = ? ";
            }
            if (listClientColName.contains("log_server_id")) {
                sql += " and a.log_server_id = ? ";
            }
        }
        try {
            con = getConnectionPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setTimestamp(1, timeReRunData);
            pstmt.setTimestamp(2, endTimeDelete);
            if (!listClientColName.isEmpty()) {
                int i = 3;
                if (listClientColName.contains("query_id")) {
                    pstmt.setLong(i++, queryId);
                }
                if (listClientColName.contains("log_server_id")) {
                    pstmt.setString(i, logServerName);
                }
            }
            pstmt.execute();
            con.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            close(con);
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }

        }
    }

    /**
     * Lấy thông tin về khóa chính của bảng
     */
    public synchronized List<String> getColumnsPk(String tableName) throws Exception {
        List<String> columnsPk = new ArrayList<String>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        String query = " SELECT  a.column_name "
                + "FROM ALL_CONS_COLUMNS A, ALL_CONSTRAINTS C "
                + "WHERE C.TABLE_NAME = A.table_name AND C.CONSTRAINT_TYPE = 'P' and a.owner = c.owner "
                + "and A.CONSTRAINT_NAME = C.CONSTRAINT_NAME "
                + "and a.owner not in('SYS', 'SYSTEM', 'WMSYS','XDB', 'CTXSYS', 'DMSYS', "
                + "'SYSMAN', 'ORDSYS','DBSNMP', 'EXFSYS','OLAPSYS', 'MDSYS') and upper(a.table_name) = upper(?) "
                + "order by a.column_name";
        try {
            con = getConnectionPool();
            preStmt = con.prepareStatement(query);
            String shortTblName = tableName.toLowerCase().trim();
            if (shortTblName.contains(".")) {
                shortTblName = shortTblName.split("\\.")[1];
            }
            preStmt.setString(1, shortTblName);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String columnName = rs.getString("column_name");
                columnsPk.add(columnName.toUpperCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            close(con);
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw ex;
                }
            }
            if (preStmt != null) {
                try {
                    preStmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw ex;
                }
            }

        }
        return columnsPk;
    }

    /**
     * Insert dữ liệu vào bảng
     */
    public synchronized void insertData(String tableName, ArrayList<MyParameters> paramsList,
            Map newColumnMap, Map<String, eDataType> newColumnsDataType, String logServerName, long queryId, boolean b, List<String> listClientColName) throws Exception {

        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnectionPool();
            con.setAutoCommit(false);
            String columnListSql = " ";
            String valueListSql = " ";

            boolean hasInsertTime = false;
            boolean remoteHasLogServerId = false;
            boolean remoteHasQueryId = false;
            Set entries = newColumnMap.entrySet();
            Iterator it = entries.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                columnListSql = columnListSql + "," + entry.getKey();
                if (entry.getKey().toString().trim().equalsIgnoreCase("insert_time")) {
                    valueListSql = valueListSql + ",sysdate";
                    hasInsertTime = true;
                } else if (entry.getKey().toString().trim().equalsIgnoreCase("log_server_id")) {
                    remoteHasLogServerId = true;
                    valueListSql += ",'" + logServerName + "'";
                } else if (entry.getKey().toString().trim().equalsIgnoreCase("query_id")) {
                    remoteHasQueryId = true;
                    valueListSql += ",'" + queryId + "'";
                } else {
                    valueListSql = valueListSql + ",?";
                }
            }
            //Insert_time
            if (!hasInsertTime && listClientColName.contains("insert_time")) {
                columnListSql += ",INSERT_TIME ";
                valueListSql += ",sysdate ";
            }

            if (!remoteHasLogServerId && listClientColName.contains("log_server_id")) {
                columnListSql += ",LOG_SERVER_ID";
                valueListSql += ",'" + logServerName + "'";
            }

            if (!remoteHasQueryId && listClientColName.contains("query_id")) {
                columnListSql += ",QUERY_ID";
                valueListSql += ",'" + queryId + "'";
            }

            columnListSql += ") ";
            valueListSql += ") ";

            columnListSql = columnListSql.replaceFirst(",", "(");
            valueListSql = valueListSql.replaceFirst(",", "(");

            String strSql = " Insert Into "
                    + tableName
                    + columnListSql
                    + " Values "
                    + valueListSql;

            preStmt = con.prepareStatement(strSql);
            boolean logInvalidColumn = false;
            for (MyParameters params : paramsList) {
                boolean catchException = false;
                int i = 0;
                Iterator it2 = entries.iterator();
                while (it2.hasNext()) {
                    Map.Entry entry = (Map.Entry) it2.next();
                    if (!"insert_time".equalsIgnoreCase(entry.getKey().toString())
                            && !"log_server_id".equalsIgnoreCase(entry.getKey().toString())
                            && !"query_id".equalsIgnoreCase(entry.getKey().toString())) {
                        i++;
                        try {
                            Object value = params.getValue(entry.getKey().toString());

                            if (null != value) {
                                preStmt.setObject(i, value);
                            } else {
                                //Lấy loại dữ liệu
                                eDataType dataType = newColumnsDataType.get(entry.getKey().toString());
                                switch (dataType) {
                                    case NUM:
                                        preStmt.setNull(i, java.sql.Types.DOUBLE);
                                        break;
                                    case STR:
                                        preStmt.setNull(i, java.sql.Types.VARCHAR);
                                        break;
                                    case DATE:
                                        preStmt.setNull(i, java.sql.Types.TIMESTAMP);
                                        break;
                                    case INT:
                                        preStmt.setNull(i, java.sql.Types.INTEGER);
                                        break;
                                    default:
                                        preStmt.setNull(i, java.sql.Types.DOUBLE);
                                        break;
                                }
                            }

                        } catch (Exception e) {
                            if (!logInvalidColumn) {
                                e.printStackTrace();
                                catchException = true;
                            }
                        }
                    }
                }
//                System.out.println("--------------");
                if (catchException) {
                    logInvalidColumn = true;
                }
                preStmt.addBatch();
            }
            preStmt.executeBatch();
            if (b) {
                con.commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
            close(con);
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw ex;
                }
            }
            if (preStmt != null) {
                try {
                    preStmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw ex;
                }
            }

        }
    }

    public synchronized void insertDataFtp(String tableName, ArrayList<MyParameters> paramsList,
            Map newColumnMap, Map<String, eDataType> newColumnsDataType, String logServerName, long queryId, boolean b, List<String> listClientColName, List<String> listClientColNameIsNotNull, Logger logger) throws Exception {

        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnectionPool();
            con.setAutoCommit(false);
            String columnListSql = " ";
            String valueListSql = " ";

            boolean hasInsertTime = false;
            boolean remoteHasLogServerId = false;
            boolean remoteHasQueryId = false;
            Set entries = newColumnMap.entrySet();
            Iterator it = entries.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                CommonSvFtpColumnMapPK pk = (CommonSvFtpColumnMapPK) entry.getKey();
                columnListSql = columnListSql + "," + pk.getLocalColumn();
                if (pk.getLocalColumn().toString().trim().equalsIgnoreCase("insert_time")) {
                    valueListSql = valueListSql + ",sysdate";
                    hasInsertTime = true;
                } else if (pk.getLocalColumn().toString().trim().equalsIgnoreCase("log_server_id")) {
                    remoteHasLogServerId = true;
                    valueListSql += ",'" + logServerName + "'";
                } else if (pk.getLocalColumn().toString().trim().equalsIgnoreCase("query_id")) {
                    remoteHasQueryId = true;
                    valueListSql += ",'" + queryId + "'";
                } else {
                    valueListSql = valueListSql + ",?";
                }
            }
            //Insert_time
            if (!hasInsertTime && listClientColName.contains("insert_time")) {
                columnListSql += ",INSERT_TIME ";
                valueListSql += ",sysdate ";
            }

            if (!remoteHasLogServerId && listClientColName.contains("log_server_id")) {
                columnListSql += ",LOG_SERVER_ID";
                valueListSql += ",'" + logServerName + "'";
            }

            if (!remoteHasQueryId && listClientColName.contains("query_id")) {
                columnListSql += ",QUERY_ID";
                valueListSql += ",'" + queryId + "'";
            }

            columnListSql += ") ";
            valueListSql += ") ";

            columnListSql = columnListSql.replaceFirst(",", "(");
            valueListSql = valueListSql.replaceFirst(",", "(");

            String strSql = " Insert Into "
                    + tableName
                    + columnListSql
                    + " Values "
                    + valueListSql;

            preStmt = con.prepareStatement(strSql);
            boolean logInvalidColumn = false;
            for (MyParameters params : paramsList) {
                boolean catchException = false;
                int i = 0;
                int j = 0;
                Iterator it2 = entries.iterator();
                // Kiem tra cot khac null co gia tri null;
                boolean isColIsNull = false;
                while (it2.hasNext()) {
                    Map.Entry entry = (Map.Entry) it2.next();
                    CommonSvFtpColumnMapPK pk = (CommonSvFtpColumnMapPK) entry.getKey();
                    if (!"insert_time".equalsIgnoreCase(pk.getLocalColumn())
                            && !"log_server_id".equalsIgnoreCase(pk.getLocalColumn())
                            && !"query_id".equalsIgnoreCase(pk.getLocalColumn())) {
                        i++;
                        try {
                            Object value = params.getValue(pk.getLocalColumn());

                            if (null != value) {
                                preStmt.setObject(i, value);
                            } else {
                                j = j + 1;
                                //Lấy loại dữ liệu                                
                                eDataType dataType = newColumnsDataType.get(pk.getLocalColumn());
                                switch (dataType) {
                                    case NUM:
                                        preStmt.setNull(i, java.sql.Types.DOUBLE);
                                        break;
                                    case STR:
                                        preStmt.setNull(i, java.sql.Types.VARCHAR);
                                        break;
                                    case DATE:
                                        preStmt.setNull(i, java.sql.Types.TIMESTAMP);
                                        break;
                                    case INT:
                                        preStmt.setNull(i, java.sql.Types.INTEGER);
                                        break;
                                    default:
                                        preStmt.setNull(i, java.sql.Types.DOUBLE);
                                        break;
                                }
                                // Kiem tra truong IsNotNull;
                                if (listClientColNameIsNotNull.contains(pk.getLocalColumn().toLowerCase())) {
                                    logger.info("ignore gia tri cot null: " + pk.getLocalColumn());
                                    isColIsNull = true;
                                    break;
                                }
                            }

                        } catch (Exception e) {
                            if (!logInvalidColumn) {
                                e.printStackTrace();
                                catchException = true;
                            }
                        }
                    }
                }
//                System.out.println("--------------");
                // Neu tat ca cac cot null thi bo qua
                if (i == j || isColIsNull) {
                    continue;
                }
                if (catchException) {
                    logInvalidColumn = true;
                }
                preStmt.addBatch();
            }
            if (!paramsList.isEmpty()) {
                preStmt.executeBatch();
                // con.setAutoCommit(true);
                if (b) {
                    con.commit();
                }
            }
        } catch (Exception ex) {
            preStmt.clearBatch();
            ex.printStackTrace();
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw ex;
                }
            }
            if (preStmt != null) {
                try {
                    preStmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw ex;
                }
            }
            close(con);

        }
    }

    /**
     * Xoa du lieu trong ngay theo log server & query_id
     */
    public synchronized void deleteOldDataFtp(int dataLevel, String tableName,
            Date timeReRunData, Timestamp endTimeDelete, String logServerName, long queryId, List<String> listClientColName) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;

        String sql = " delete from  " + tableName + " a ";
        if (dataLevel == 1) {
            sql += " where a.update_time = ? ";// and a.update_time < ? ";
        } else {
            sql += " where trunc(a.update_time) = trunc(?) ";// and a.update_time < ? ";
        }
        if (!listClientColName.isEmpty()) {
            if (listClientColName.contains("query_id")) {
                sql += " and query_id = ? ";
            }
            if (listClientColName.contains("log_server_id")) {
                sql += " and log_server_id = ? ";
            }
            if (listClientColName.contains("hour_id")) {
                sql += " and hour_id = ? ";
            }
        }
        try {
            con = getConnectionPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setTimestamp(1, new java.sql.Timestamp(timeReRunData.getTime()));
//            pstmt.setDate(2, new jatimeReRunData.getTime()va.sql.Date(endTimeDelete.getTime()));
            if (!listClientColName.isEmpty()) {
                int i = 2;
                if (listClientColName.contains("query_id")) {
                    pstmt.setLong(i++, queryId);
                }
                if (listClientColName.contains("log_server_id")) {
                    pstmt.setString(i++, String.valueOf(logServerName));
                }
                if (listClientColName.contains("hour_id")) {
                    pstmt.setString(i, String.valueOf(timeReRunData.getHours()));
                }
            }

            pstmt.execute();
            con.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
            close(con);
        }
    }
}
