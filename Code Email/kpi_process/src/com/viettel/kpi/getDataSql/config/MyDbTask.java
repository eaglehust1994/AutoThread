/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.getDataSql.config;

import com.viettel.kpi.common.utils.DBUtil;
import com.viettel.kpi.common.utils.DbTask;
import com.viettel.kpi.getDataSql.common.CommonLogServer;
import com.viettel.kpi.getDataSql.common.CommonMapQueryLogServer;
import com.viettel.kpi.getDataSql.common.CommonSvFtpColumnMap;
import com.viettel.kpi.getDataSql.common.CommonSvFtpColumnMapPK;
import com.viettel.kpi.getDataSql.common.CommonSvFtpFile;
import com.viettel.kpi.getDataSql.common.CommonSvQuery;
import com.viettel.kpi.getDataSql.common.CommonUtil;
import com.viettel.kpi.service.common.AlarmErrorServer;
import com.viettel.kpi.service.common.DataTypes;
import com.viettel.kpi.service.common.DataTypes.eDataType;
import com.viettel.kpi.service.common.LogServer;
import com.viettel.kpi.service.common.MscServerCommon;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author qlmvt_MinhHT1
 */
public class MyDbTask extends DbTask {

    public synchronized List<String> getListColumnName(String tableName, Logger logger) throws Exception {

        List<String> listCol = new ArrayList<String>();
        Connection con = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String strSql = " select a.column_name from all_tab_columns a "
                + " where lower(a.table_name) = ?";

        try {
            con = getConnectionPool();

            preStmt = con.prepareStatement(strSql);
            preStmt.setString(1, tableName);
            rs = preStmt.executeQuery();

            while (rs.next()) {
                listCol.add(rs.getString("column_name").toLowerCase().trim());
            }
            if (listCol.size() < 1) {
                logger.error("Bang : " + tableName + " khong ton tai");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
                close(con);
            } catch (Exception ex) {
                throw ex;
            }
        }
        return listCol;
    }

    public synchronized List<String> getListColumnNameIsNotNull(String tableName, Logger logger) throws Exception {

        List<String> listCol = new ArrayList<String>();
        Connection con = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String strSql = " select a.column_name from all_tab_columns a "
                + " where lower(a.table_name) = ? "
                + "   and a.nullable = 'N'";

        try {
            con = getConnectionPool();

            preStmt = con.prepareStatement(strSql);
            preStmt.setString(1, tableName);
            rs = preStmt.executeQuery();

            while (rs.next()) {
                listCol.add(rs.getString("column_name").toLowerCase().trim());
            }
//            if (listCol.size() < 1) {
//                logger.error("Bang : " + tableName + " khong ton tai");
//            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
                close(con);
            } catch (Exception ex) {
                throw ex;
            }
        }
        return listCol;
    }

    /**
     * Lấy map remoteColumn-LocalColumn , map localColumn-DataType;
     */
    public synchronized Map<String, String> getColumnMap(long query_id, Map<String, eDataType> columnsDataType) throws Exception {

        Map<String, String> columnMap = new HashMap<String, String>();

        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        String strSql = " SELECT a.local_column, a.remote_column, a.data_type "
                + " FROM COMMON_SV_COLUMN_MAP a "
                + " WHERE a.query_id = ?"
                + " AND a.local_column is not null"
                + " AND a.remote_column is not null";
        try {
            con = getConnectionPool();
            preStmt = con.prepareStatement(strSql);
            preStmt.setLong(1, query_id);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                String localColumn = rs.getString("local_column").toUpperCase().trim();
                String remoteColumn = rs.getString("remote_column").toUpperCase().trim();
                Object data_type = rs.getObject("data_type");
                if (data_type != null) {
                    columnsDataType.put(localColumn, DataTypes.getDataType(data_type.toString()));
                } else {
                    columnsDataType.put(localColumn, eDataType.NONE);
                }
                columnMap.put(localColumn, remoteColumn);
            }

        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
            } catch (Exception ex) {
                throw ex;
            }
        }
        close(con);
        return columnMap;
    }
    /*
     * Cập nhật thời gian chạy lần cuối cho query, thời gian trunc tới mức giờ.
     */

    public synchronized void updateEndTimeRun(long queryId, long logServerId, Timestamp startTimeRunning) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = " update common_map_query_log_server a "
                + " set a.end_time_run = ? "
                + " where a.query_id = ? and log_server_id = ?";
        try {
            con = getConnectionPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setTimestamp(1, new java.sql.Timestamp(startTimeRunning.getTime()));
            pstmt.setLong(2, queryId);
            pstmt.setLong(3, logServerId);

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
                close(con);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

    }
    /*
     * Cập nhật trạng thái running cho query
     */

    public synchronized void updateIsRunning(long queryId, long logServerId, boolean b) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = " update common_map_query_log_server a "
                + " set a.is_running = ? "
                + " where a.query_id = ? and log_server_id = ?";
        try {
            con = getConnectionPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, b ? 1 : 0);
            pstmt.setLong(2, queryId);
            pstmt.setLong(3, logServerId);

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
                close(con);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }

        }
    }
    /*
     * Lấy thông tin về danh mục map query va log_server isRunning = 1;
     * serviceTypeId = serviceTypeId
     */

    public synchronized ArrayList<CommonMapQueryLogServer> getMapQueryServerList(int serviceTypeId) throws Exception {

        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        String strSql = " select * from ( "
                + " SELECT a.query_id, a.log_server_id, a.hour_run_in_day, a.interval_hour,"
                + " a.end_time_data, a.service_type_id, a.module_id, a.run_step_next,"
                + " a.time_return_now, a.is_running, a.type, "
                + " case when nvl(b.data_level,c.data_level) = 1 then trunc(a.end_time_run, 'HH24')"
                + "      when nvl(b.data_level,c.data_level) = 2 then trunc(a.end_time_run)"
                + "     else a.end_time_run end end_time_run, a.end_time_run true_end_time_run, "
                + " case when c.query_name is null then b.description"
                + "     else c.query_name end query_name,"
                + " f.log_server_name,"
                + " nvl(b.data_level,c.data_level) data_level, "
                + " nvl(b.status,c.status) status "
                + " FROM common_map_query_log_server a "
                + " LEFT JOIN common_sv_ftp_file b "
                + " ON b.file_id = a.query_id "
                + " LEFT JOIN common_sv_query c "
                + " ON a.query_id = c.query_id "
                + " INNER JOIN common_log_server f "
                + " ON a.log_server_id = f.log_server_id and f.status = 1 "
                + " where a.service_type_id = ? "
                + " and (a.is_running = 0 or a.is_running is null) "
                + " ) d where d.status =1 ";
        ArrayList<CommonMapQueryLogServer> results = new ArrayList<CommonMapQueryLogServer>();

        try {
            con = getConnectionPool();

            preStmt = con.prepareStatement(strSql);
            preStmt.setInt(1, serviceTypeId);
            rs = preStmt.executeQuery();

            results = CommonUtil.getDynaListObjByAnnotation(CommonMapQueryLogServer.class.getName(), rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
                close(con);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;

            }

        }

        return results;
    }

    /**
     * Lấy logServer trong bảng COMMON_LOG_SERVER
     */
    public synchronized CommonLogServer getLogServer(long logServerId) throws Exception {

        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        String strSql = " SELECT a.log_server_id, a.log_server_name, a.protocol, a.ip, a.driver,"
                + " a.url, a.username, a.password, a.vendor, a.status, a.port"
                + " FROM COMMON_LOG_SERVER a"
                + " where a.log_server_id = ?";
        ArrayList<CommonLogServer> results = new ArrayList<CommonLogServer>();

        try {
            con = getConnectionPool();

            preStmt = con.prepareStatement(strSql);
            preStmt.setLong(1, logServerId);
            rs = preStmt.executeQuery();

            results = CommonUtil.getDynaListObjByAnnotation(CommonLogServer.class.getName(), rs);
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
                close(con);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;

            }
        }

        return !results.isEmpty() ? results.get(0) : null;
    }

    /**
     * Lấy cấu hình query trong bảng COMMON_SV_QUERY
     */
    public CommonSvQuery getCommonSvQuery(long queryId) throws Exception {

        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        String strSql = " SELECT a.query_id, a.query_name, a.query_text, a.table_name,"
                + " a.data_level, a.previous_day_del, a.customer_class,"
                + " a.stored_name, a.status"
                + " FROM COMMON_SV_QUERY a"
                + " where a.query_id = ? and a.status = 1";
        ArrayList<CommonSvQuery> results = new ArrayList<CommonSvQuery>();

        try {
            con = getConnectionPool();

            preStmt = con.prepareStatement(strSql);
            preStmt.setLong(1, queryId);
            rs = preStmt.executeQuery();

            results = CommonUtil.getDynaListObjByAnnotation(CommonSvQuery.class.getName(), rs);
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
                close(con);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;

            }
        }

        return !results.isEmpty() ? results.get(0) : null;
    }

    /**
     * Lay ra cau hinh trong bang COMMON_SV_FTP_COLUMN_MAP
     */
    public synchronized Map<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> getColumnMapFtp(
            long queryId, Map<String, eDataType> columnsDataType) throws Exception {

        Map<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap> columnMap = new HashMap<CommonSvFtpColumnMapPK, CommonSvFtpColumnMap>();

        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        String strSql = " SELECT a.file_id, a.local_column, a.data_type, a.col_type, a.row_idx,"
                + " a.col_idx, a.default_value, a.cell_data_format,"
                + " a.ignore_character, a.blacklist_value, a.blacklist_ignore_insert"
                + " FROM common_sv_ftp_column_map a "
                + " WHERE a.file_id = ?"
                + " AND a.local_column is not null";
        try {
            con = getConnectionPool();
            preStmt = con.prepareStatement(strSql);
            preStmt.setLong(1, queryId);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                int fileId = rs.getInt("file_id");
                String localColumn = rs.getString("local_column").toUpperCase().trim();
                CommonSvFtpColumnMapPK pk = new CommonSvFtpColumnMapPK(fileId, localColumn);

                String colType = rs.getString("col_type") != null ? rs.getString("col_type").trim() : null;
                Long rowIdx = rs.getLong("row_idx");
                Long colIdx = rs.getLong("col_idx");
                String defaultValue = rs.getString("default_value") != null ? rs.getString("default_value").trim() : null;
                String cellDataFormat = rs.getString("cell_data_format") != null ? rs.getString("cell_data_format").trim() : null;
                String ignoreCharacter = rs.getString("ignore_character") != null ? rs.getString("ignore_character").trim() : null;
                String blacklistValue = rs.getString("blacklist_value") != null ? rs.getString("blacklist_value").trim() : null;
                int blacklistIgnoreInsert = rs.getInt("blacklist_ignore_insert");

                CommonSvFtpColumnMap commonSvFtpColumnMap = new CommonSvFtpColumnMap(fileId, localColumn,
                        colType, rowIdx, colIdx, defaultValue, cellDataFormat, ignoreCharacter,
                        blacklistValue, blacklistIgnoreInsert);
                Object data_type = rs.getObject("data_type");
                if (data_type != null) {
                    columnsDataType.put(localColumn, DataTypes.getDataType(data_type.toString()));
                    commonSvFtpColumnMap.setDataType(DataTypes.getDataType(data_type.toString()));
                } else {
                    columnsDataType.put(localColumn, eDataType.NONE);
                    commonSvFtpColumnMap.setDataType(eDataType.NONE);
                }

                columnMap.put(pk, commonSvFtpColumnMap);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
                close(con);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        return columnMap;
    }

    /**
     * Lay file cau hinh trong bang COMMON_SV_FTP_FILE
     */
    public synchronized CommonSvFtpFile getCommonSvFtpFile(long queryId) throws Exception {

        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        String strSql = " SELECT a.file_id, a.description, a.remote_file_name,"
                + " a.remote_name_format, a.remote_file_path, a.remote_path_format,"
                + " a.file_type, a.local_store_path, a.local_path_format,"
                + " a.table_name, a.status, a.data_level, a.row_header, a.detimiter,"
                + " a.prev_date_del"
                + " FROM common_sv_ftp_file a"
                + " where a.file_id = ? and a.status = 1";
        ArrayList<CommonSvFtpFile> results = new ArrayList<CommonSvFtpFile>();

        try {
            con = getConnectionPool();

            preStmt = con.prepareStatement(strSql);
            preStmt.setLong(1, queryId);
            rs = preStmt.executeQuery();

            results = CommonUtil.getDynaListObjByAnnotation(CommonSvFtpFile.class.getName(), rs);
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
                close(con);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;

            }
        }

        return !results.isEmpty() ? results.get(0) : null;
    }

    public synchronized void updateEndTimeData(CommonMapQueryLogServer mapQueryServer, Timestamp startTime) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = " update common_map_query_log_server a "
                + " set a.end_time_data = ? "
                + " where a.query_id = ? and log_server_id = ?";
        try {
            con = getConnectionPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setTimestamp(1, startTime);
            pstmt.setLong(2, mapQueryServer.getQueryId());
            pstmt.setLong(3, mapQueryServer.getLogServerId());

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
        }
        close(con);
    }

    /**
     * Ghi log trong bảng COMMON_SV_LOG_SERVICES
     */
    public synchronized void writelog(Timestamp updateTime, Timestamp startTime,
            long queryId, long logServerId, int countRun, String content, String logLevel, Timestamp endTime) throws Exception {
//        PreparedStatement preStmt = null;
//        Connection con = null;
//        String strSql = " Insert into COMMON.COMMON_SV_LOG_SERVICES"
//                + " (update_time, query_id, log_server_id, count_run, content,"
//                + " log_level, start_time, end_time) "
//                + " values(?,?,?,?,?,?,?,?) ";
//        try {
////            con= getConnectionPool();
//            con = getConnectionPool();
//            preStmt = con.prepareStatement(strSql);
//            preStmt.setTimestamp(1, updateTime);
//            preStmt.setLong(2, queryId);
//            preStmt.setLong(3, logServerId);
//            preStmt.setInt(4, countRun);
//            preStmt.setString(5, content);
//            preStmt.setString(6, logLevel);
//            preStmt.setTimestamp(7, startTime);
//            preStmt.setTimestamp(8, endTime);
//            preStmt.execute();
//            con.commit();
//        } catch (Exception ex) {
//            throw ex;
//        } finally {
//            if (preStmt != null) {
//                try {
//                    preStmt.close();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                    throw ex;
//                }
//            }
//            close(con);
//        }
    }

    /**
     * Cập nhật log trong bảng COMMON_SV_LOG_SERVICES
     */
    public synchronized void updateLog(Timestamp updateTime, Timestamp startTimeRunning, Timestamp finishTime,
            long queryId, long logServerId, int countRun, String content, String logLevel) throws Exception {
//        PreparedStatement preStmt = null;
//        Connection con = null;
//        String strSql = " update COMMON.COMMON_SV_LOG_SERVICES a set "
//                + " a.content = ?, a.log_level = ?, a.end_time = ?"
//                + " where a.update_time = ? and a.query_id = ? "
//                + " and a.log_server_id = ? and a.count_run = ? ";
//        try {
//            con = getConnectionPool();
//
//            preStmt = con.prepareStatement(strSql);
//            preStmt.setString(1, content);
//            preStmt.setString(2, logLevel);
////            preStmt.setTimestamp(3, startTimeRunning);
//            preStmt.setTimestamp(3, finishTime);
//
//            preStmt.setTimestamp(4, updateTime);
//            preStmt.setLong(5, queryId);
//            preStmt.setLong(6, logServerId);
//            preStmt.setInt(7, countRun);
//
//            preStmt.execute();
//            con.commit();
//        } catch (Exception ex) {
//            throw ex;
//        } finally {
//            if (preStmt != null) {
//                try {
//                    preStmt.close();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                    throw ex;
//                }
//            }
//            close(con);
//        }
    }

    /**
     * Lay so lan chay cua tien trinh( mac dinh bang 1) Trong bảng
     * COMMON_SV_LOG_SERVICES
     */
//    public int getCountRun(CommonMapQueryLogServer mapQueryServer, Timestamp startTime, Timestamp endTime) throws Exception {
//
//        int countRun = 1;
//
//        PreparedStatement preStmt = null;
//        ResultSet rs = null;
//        String strSql = " SELECT count_run"
//                + " FROM COMMON_SV_LOG_SERVICES a"
//                + " where a.log_server_id = ? and a.query_id = ? "
//                + " and a.update_time >= ? and a.update_time < ?";
//
//        try {
//            con= getConnectionPool();
//
//            preStmt = connection.prepareStatement(strSql);
//            preStmt.setLong(1, mapQueryServer.getLogServerId());
//            preStmt.setLong(2, mapQueryServer.getQueryId());
//            preStmt.setDate(3, new java.sql.Date(startTime.getTime()));
//            preStmt.setDate(4, new java.sql.Date(endTime.getTime()));
//            rs = preStmt.executeQuery();
//
//            while (rs.next()) {
//                countRun = rs.getInt("count_run");
//                break;
//            }
//        } catch (Exception ex) {
//            throw ex;
//        } finally {
//            if (rs != null) {
//                rs.close();
//            }
//            if (preStmt != null) {
//                preStmt.close();
//            }
//        }
//
//        return countRun;
//    }
//
//    /**
//     * Lay so lan chay cua tien trinh( mac dinh bang 1) Trong bảng
//     * COMMON_SV_LOG_SERVICES
//     */
//    public int getCountRun(CommonMapQueryLogServer mapQueryServer, Timestamp startTime) throws Exception {
//
//        int countRun = 0;
//
//        PreparedStatement preStmt = null;
//        ResultSet rs = null;
//        String strSql = " SELECT count_run"
//                + " FROM COMMON_SV_LOG_SERVICES a"
//                + " where a.log_server_id = ? and a.query_id = ? "
//                + " and a.update_time = ?";
//
//        try {
//            con= getConnectionPool();
//
//            preStmt = connection.prepareStatement(strSql);
//            preStmt.setLong(1, mapQueryServer.getLogServerId());
//            preStmt.setLong(2, mapQueryServer.getQueryId());
//            preStmt.setDate(3, new java.sql.Date(startTime.getTime()));
//            rs = preStmt.executeQuery();
//
//            while (rs.next()) {
//                countRun = rs.getInt("count_run") + 1;
//                break;
//            }
//        } catch (Exception ex) {
//            throw ex;
//        } finally {
//            if (rs != null) {
//                rs.close();
//            }
//            if (preStmt != null) {
//                preStmt.close();
//            }
//        }
//
//        return countRun;
//    }
    public int getCountRun(long logServerId, long fileId, Timestamp startTime) throws Exception {

        int countRun = 1;

        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        String strSql = " SELECT max(count_run) count_run"
                + " FROM COMMON_SV_LOG_SERVICES a"
                + " where a.log_server_id = ? and a.query_id = ? "
                + " and a.update_time = ?";

        try {
            con = getConnectionPool();

            preStmt = con.prepareStatement(strSql);
            preStmt.setLong(1, logServerId);
            preStmt.setLong(2, fileId);
            preStmt.setTimestamp(3, startTime);
            rs = preStmt.executeQuery();

            while (rs.next()) {
                countRun = rs.getInt("count_run") + 1;
                break;
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
                close(con);
            } catch (Exception e) {
                throw e;
            }

        }

        return countRun;
    }

    public int getServiceType(String serviceTypeCode) throws Exception {

        int count = 0;
        Connection con = null;

        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String strSql = " select service_type_id count from common_service_type a where a.service_type_code=? ";

        try {
            con = getConnectionPool();
            preStmt = con.prepareStatement(strSql);
            preStmt.setString(1, serviceTypeCode);
            rs = preStmt.executeQuery();

            while (rs.next()) {
                count = rs.getInt("count");
                break;
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
                close(con);
            } catch (Exception e) {
                throw e;
            }
        }

        return count;
    }

    public CommonMapQueryLogServer getCommonLogServer(Long _queryId, Long _logServerId) throws SQLException, Exception {

        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        String strSql = " select * from ( "
                + " SELECT a.query_id, a.log_server_id, a.hour_run_in_day, a.interval_hour,"
                + " a.end_time_data, a.service_type_id, a.module_id, a.end_time_run,"
                + " a.time_return_now, a.is_running, a.type, a.run_step_next,"
                + " nvl(b.status,c.status) status "
                + " FROM common_map_query_log_server a "
                + " LEFT JOIN common_sv_ftp_file b "
                + " ON b.file_id = a.query_id "
                + " LEFT JOIN common_sv_query c "
                + " ON a.query_id = c.query_id "
                + " where a.query_id = ? "
                + " and a.service_type_id = ? "
                + " ) d where d.status =1 ";
        ArrayList<CommonMapQueryLogServer> results = new ArrayList<CommonMapQueryLogServer>();

        try {
            con = getConnectionPool();
            preStmt = con.prepareStatement(strSql);
            preStmt.setLong(1, _queryId);
            preStmt.setLong(2, _logServerId);
            rs = preStmt.executeQuery();

            results = CommonUtil.getDynaListObjByAnnotation(CommonMapQueryLogServer.class.getName(), rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
                close(con);
            } catch (Exception e) {
                throw e;
            }
        }

        return results.get(0);
    }

    public synchronized void resetIsRunning(String serviceTypeCode) throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = " update common_map_query_log_server a "
                + " set a.is_running = 0 where a.service_type_id = (select service_type_id from common_service_type where service_type_code = ? ) ";

        try {
            con = getConnectionPool();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, serviceTypeCode);
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
                close(con);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

    }

    public Long getSequenceValue(String sequenceName) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Connection con = null;
        String sql = " Select " + sequenceName + ".nextval  from dual";
        Long longValue = null;
        try {
            con = getConnectionPool();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                longValue = rs.getLong(1);
            }
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
                close(con);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
        return longValue;
    }

    public void writelogLostData(String fileName, Integer module, String logserverId, Timestamp time) {

        //java.util.Date updateTime = DateTimeUtils.convertStringToDateStandardNew(time);
        PreparedStatement preStmt = null;
        Connection con = null;
        String strSql = " Insert into common.common_lost_file_data"
                + " (update_time, file_name, module_id, log_server_id, SERVER_TYPE_CODE)"
                + " values(?,?,?,?,?) ";
        try {
//            con= getConnectionPool();
            con = getConnectionPool();
            preStmt = con.prepareStatement(strSql);

            preStmt.setTimestamp(1, time);
            preStmt.setString(2, fileName);
            preStmt.setInt(3, module);
            preStmt.setString(4, logserverId);
            preStmt.setString(5, Start.serviceTypeCode);
            preStmt.execute();
            con.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (preStmt != null) {
                try {
                    preStmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                close(con);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(MyDbTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public void insertDataError(CommonLogServer commonLogServer, LogServer logServer, MscServerCommon mscServer, int type, String errorDetail, String path) throws Exception {
        AlarmErrorServer alarmErrorServer = new AlarmErrorServer();
        List<AlarmErrorServer> lstAlarmErrorServer = new ArrayList<AlarmErrorServer>();

//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        conn = getConnectionPool();
        if (type == 1) {
            // thuc hien lay thogn tin tu bang common.common_sv_log_servers
            try {
                if (logServer != null) {
                    alarmErrorServer.setName(logServer.getLogServerId());
                    alarmErrorServer.setIp(logServer.getIp());
                    alarmErrorServer.setPort(logServer.getPort());
                    alarmErrorServer.setModule(logServer.getModule());
                    alarmErrorServer.setVendor(logServer.getVendor());
                    alarmErrorServer.setAreaCode(logServer.getAreaCode());
                    alarmErrorServer.setErrorDetail(errorDetail);
                    alarmErrorServer.setPath_error(path);
                    lstAlarmErrorServer.add(alarmErrorServer);
                    insertAlamrServerNode(lstAlarmErrorServer);
                }

            } catch (Exception e) {
            }

        } else if (type == 2) {
            // thuc hien lay thogn tin tu bang common.common_log_server
            if (commonLogServer != null) {
                lstAlarmErrorServer = getCommonLogServer(commonLogServer.getLogServerId(), errorDetail, path);
                insertAlamrServerNode(lstAlarmErrorServer);
            }
        } else if (type == 3) {
            // thuc hien lay thogn tin tu bang nss.cata_msc
            //dungvv8_cmt_01092015
//            lstAlarmErrorServer = getCataMsc(mscServer.getMscCode(), mscServer.getType(), errorDetail, path);
//            insertAlamrServerNode(lstAlarmErrorServer);
        }

    }

    public void insertAlamrServerNode(List<AlarmErrorServer> alarmErrorServer) {
        try {
            int count = 0;
            Connection conn = null;
            PreparedStatement pstmt = null;
            conn = getConnectionError();

            String sql = " INSERT INTO  common.common_alarm_node_server "
                    + " ( id, name, ip, port, module, vendor, area_code,"
                    + " vesion, type, update_time, insert_time, error_detail,path_error,"
                    + " flag_sms, flag_email)"
                    + " VALUES (common.common_alarm_node_server_sq.nextval,?,?,?,?,?,?,?,?,TRUNC(sysdate),sysdate,?,?,0,0)";
            pstmt = conn.prepareStatement(sql);
            for (AlarmErrorServer alarmErrorServerBo : alarmErrorServer) {
                pstmt.setString(1, alarmErrorServerBo.getName() != null ? alarmErrorServerBo.getName() : "");
                pstmt.setString(2, alarmErrorServerBo.getIp() != null ? alarmErrorServerBo.getIp() : "");
                if (alarmErrorServerBo.getPort() != null) {
                    pstmt.setInt(3, alarmErrorServerBo.getPort());
                } else {
                    pstmt.setNull(3, Types.INTEGER);
                }
                pstmt.setString(4, alarmErrorServerBo.getModule() != null ? alarmErrorServerBo.getModule() : "");
                pstmt.setString(5, alarmErrorServerBo.getVendor() != null ? alarmErrorServerBo.getVendor() : "");
                pstmt.setString(6, alarmErrorServerBo.getAreaCode() != null ? alarmErrorServerBo.getAreaCode() : "");
                if (alarmErrorServerBo.getVesion() != null) {
                    pstmt.setInt(7, alarmErrorServerBo.getVesion());
                } else {
                    pstmt.setNull(7, Types.INTEGER);
                }
                pstmt.setString(8, alarmErrorServerBo.getType() != null ? alarmErrorServerBo.getType() : "");
                pstmt.setString(9, alarmErrorServerBo.getErrorDetail());
                pstmt.setString(10, alarmErrorServerBo.getPath_error());
                pstmt.addBatch();
                count++;
                if (count >= 500) {
                    pstmt.executeBatch();
                    pstmt.clearBatch();
                    count = 0;
                }
            }
            if (count > 0) {
                pstmt.executeBatch();
                pstmt.clearBatch();

            }
            commit(conn);
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Exception e) {
            }
            close(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<AlarmErrorServer> getCommonLogServer(Long logServerId, String errorDeatail, String path) throws Exception {
        List<AlarmErrorServer> list = new ArrayList<AlarmErrorServer>();
        PreparedStatement pstmt = null;
        Connection con = null;
        ResultSet result = null;
        AlarmErrorServer bo = null;
        try {
            con = getConnectionError();
            String sql = " select DISTINCT(e.module_name) module_name,d.* from (select c.module_id, b.* from "
                    + " (select a.* from common_log_server a where a.log_server_id = ?)b "
                    + " left JOIN common_map_query_log_server c on b.log_server_id = c.log_server_id)d"
                    + " left join sv_kpi_module e on e.module_id = d.module_id  order by d.log_server_id ";
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, logServerId);
            result = pstmt.executeQuery();

            while (result.next()) {
                try {
                    bo = new AlarmErrorServer();

                    bo.setModule(DBUtil.getString(result.getObject("module_name")));
                    bo.setName(DBUtil.getString(result.getObject("log_server_name")));
                    bo.setErrorDetail(errorDeatail);
                    bo.setIp(DBUtil.getString(result.getObject("ip")));
                    bo.setPath_error(path);
                    bo.setPort(DBUtil.getInteger(result.getObject("port")));
                    bo.setVendor(DBUtil.getString(result.getObject("vendor")));
                    list.add(bo);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {

            try {
                if (result != null) {
                    result.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            close(con);
        }
        return list;

    }

    public List<AlarmErrorServer> getCataMsc(String mscCode, String type, String errorDeatail, String path) throws Exception {
        List<AlarmErrorServer> list = new ArrayList<AlarmErrorServer>();
        PreparedStatement pstmt = null;
        Connection con = null;
        ResultSet result = null;
        AlarmErrorServer bo = null;
        try {
            con = getConnectionError();
            String sql = " select b.area_code,a.* from cata_msc a join nss_cata_node b on a.msc_code=b.node_code where a.msc_code=? and a.type=? ";

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, mscCode);
            pstmt.setString(2, type);
            result = pstmt.executeQuery();

            while (result.next()) {
                try {
                    bo = new AlarmErrorServer();

                    bo.setModule("NSS");
                    bo.setName(DBUtil.getString(result.getObject("msc_code")));
                    bo.setErrorDetail(errorDeatail);
                    bo.setIp(DBUtil.getString(result.getObject("ip")));
                    bo.setPath_error(path);
                    bo.setPort(DBUtil.getInteger(result.getObject("port")));
                    bo.setVendor(DBUtil.getString(result.getObject("vendor")));
                    bo.setVesion(DBUtil.getInteger(result.getObject("version")));
                    bo.setType(DBUtil.getString(result.getObject("type")));
                    bo.setAreaCode(DBUtil.getString(result.getObject("area_code")));
                    list.add(bo);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {

            try {
                if (result != null) {
                    result.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            close(con);
        }
        return list;

    }

    public String getExceptionContent(Exception ex) {
        StringBuilder result = new StringBuilder();

        try {
            result = new StringBuilder(ex.getMessage());
            StringWriter sw = new StringWriter();
            new Throwable("").printStackTrace(new PrintWriter(sw));
            result.append(System.getProperty("line.separator"));
            if (sw.toString().length() <= 900) {
                result.append(sw.toString());
            } else {
                result.append(sw.toString().subSequence(0, 900));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public String getServerInfo() {
        String info = "";
        try {
            InetAddress thisIp = InetAddress.getLocalHost();
            info = "IP: " + thisIp.toString() + System.getProperty("line.separator") + " URL: " + System.getProperty("user.dir");
            if (info.length() > 450) {
                info = info.substring(0, 450);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return info;
    }
}
