/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.mobile.raw;

import com.viettel.kpi.common.utils.DBUtil;
import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.kpi.common.utils.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyDbTask extends DbTask {

    private static final Logger logger = Logger.getLogger(MyDbTask.class);

    public List<KpiMobileDataRaw> getKpiMobileRaw(Date now) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiMobileDataRaw> lst = new ArrayList<KpiMobileDataRaw>();
        KpiMobileDataRaw obj = null;
        try {
            String _now = DateTimeUtils.format(now, "dd/MM/yyyy");
            Date now_1 = DateTimeUtils.add(now, -1);
            String _now_1 = DateTimeUtils.format(now_1, "dd/MM/yyyy");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.user_name, a.app_name, a.date_time start_time,b.date_time end_time, b.response_code,\n");
            sql.append("       a.os_version, a.device_model, a.imei, a.network_type,\n");
            sql.append("       SYSDATE insert_time, a.url_request, a.request_id, a.latitude,\n");
            sql.append("       a.longitude, a.signal, a.app_version, a.error_description,\n");
            sql.append("       a.cell_id, a.lac_id, a.request_view,  \n");
            sql.append("       1000*((EXTRACT(DAY FROM b.date_time - a.date_time))*24*60*60+ \n");
            sql.append("       (EXTRACT(HOUR FROM b.date_time - a.date_time))*60*60+ \n");
            sql.append("       (EXTRACT(MINUTE FROM b.date_time - a.date_time))*60+ \n");
            sql.append("       (EXTRACT(SECOND FROM b.date_time - a.date_time))) duration,\n");
            sql.append("       a.download, a.upload, a.ram, a.cpu\n");
            sql.append("FROM \n");
            sql.append("(SELECT a.*\n");
            sql.append("FROM kpi_mobile_data_raw_temp PARTITION FOR(to_date('").append(_now).append("','dd/MM/yyyy')) a\n");
            sql.append("WHERE a.TYPE = 1\n");
//            sql.append("  UNION \n");
//            sql.append("SELECT a.*\n");
//            sql.append("FROM kpi_mobile_data_raw_temp  PARTITION FOR(to_date('").append(_now_1).append("','dd/MM/yyyy'))a\n");
//            sql.append("WHERE a.TYPE = 1");
            sql.append(") a\n");
            sql.append("INNER JOIN \n");
            sql.append("(SELECT a.*\n");
            sql.append("  FROM kpi_mobile_data_raw_temp PARTITION FOR(to_date('").append(_now).append("','dd/MM/yyyy'))a\n");
            sql.append("  WHERE a.TYPE = 2 AND a.date_time IS NOT NULL \n");
            sql.append("  UNION \n");
            sql.append("SELECT a.*\n");
            sql.append("FROM kpi_mobile_data_raw_temp PARTITION FOR(to_date('").append(_now_1).append("','dd/MM/yyyy'))a\n");
            sql.append("  WHERE a.TYPE = 2 AND a.date_time IS NOT NULL ) b ON a.request_id = b.request_id");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiMobileDataRaw();
                obj.setUserName(DBUtil.getString(rs.getObject("user_name")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setStartTime(new Date(rs.getTimestamp("start_time").getTime()));
                obj.setEndTime(new Date(rs.getTimestamp("end_time").getTime()));
                obj.setResponseCode(DBUtil.getString(rs.getObject("response_code")));
                obj.setOsVersion(DBUtil.getString(rs.getObject("os_version")));
                obj.setDeviceModel(DBUtil.getString(rs.getObject("device_model")));
                obj.setImei(DBUtil.getString(rs.getObject("imei")));
                obj.setNetworkType(DBUtil.getString(rs.getObject("network_type")));
                obj.setInsertTime(new Date());
                obj.setUrlRequest(DBUtil.getString(rs.getObject("url_request")));
                obj.setRequestID(DBUtil.getString(rs.getObject("request_id")));
                obj.setLatitude(DBUtil.getString(rs.getObject("latitude")));
                obj.setLongitude(DBUtil.getString(rs.getObject("longitude")));
                obj.setSignal(DBUtil.getString(rs.getObject("signal")));
                obj.setAppVersion(DBUtil.getString(rs.getObject("app_version")));
                obj.setErrorDescription(DBUtil.getString(rs.getObject("error_description")));
                obj.setCellId(DBUtil.getString(rs.getObject("cell_id")));
                obj.setLacId(DBUtil.getString(rs.getObject("lac_id")));
                obj.setRequestView(DBUtil.getString(rs.getObject("request_view")));
                obj.setDuration(DBUtil.getLong(rs.getObject("duration")));
                obj.setDownload(DBUtil.getLong(rs.getObject("download")));
                obj.setUpload(DBUtil.getLong(rs.getObject("upload")));
                obj.setRam(DBUtil.getString(rs.getObject("ram")));
                obj.setCpu(DBUtil.getString(rs.getObject("cpu")));
                lst.add(obj);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            try {
                close(con);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return lst;
    }

    public List<KpiMobileDataRaw> getKpiMobileRawTimeout(Date now) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiMobileDataRaw> lst = new ArrayList<KpiMobileDataRaw>();
        KpiMobileDataRaw obj = null;
        try {
            String _now = DateTimeUtils.format(now, "dd/MM/yyyy");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.user_name, a.app_name, a.date_time start_time,b.date_time end_time, a.response_code,\n");
            sql.append("       a.os_version, a.device_model, a.imei, a.network_type,\n");
            sql.append("       SYSDATE insert_time, a.url_request, a.request_id, a.latitude,\n");
            sql.append("       a.longitude, a.signal, a.app_version, a.error_description,\n");
            sql.append("       a.cell_id, a.lac_id, a.request_view, \n");
            sql.append("       1000*((EXTRACT(DAY FROM b.date_time - a.date_time))*24*60*60+ \n");
            sql.append("       (EXTRACT(HOUR FROM b.date_time - a.date_time))*60*60+ \n");
            sql.append("       (EXTRACT(MINUTE FROM b.date_time - a.date_time))*60+ \n");
            sql.append("       (EXTRACT(SECOND FROM b.date_time - a.date_time))) duration,\n");
            sql.append("       a.download, a.upload, a.ram, a.cpu \n");
            sql.append("FROM \n");
            sql.append("(SELECT a.*\n");
            sql.append("FROM kpi_mobile_data_raw_temp  PARTITION FOR(to_date('").append(_now).append("','dd/MM/yyyy'))a\n");
            sql.append("WHERE a.TYPE = 1) a\n");
            sql.append("LEFT JOIN \n");
            sql.append("(SELECT a.*\n");
            sql.append("FROM kpi_mobile_data_raw_temp PARTITION FOR(to_date('").append(_now).append("','dd/MM/yyyy'))a\n");
            sql.append("WHERE a.TYPE = 2 ) b ON a.request_id = b.request_id\n");
            sql.append("WHERE b.date_time IS NULL");

            pstmt = con.prepareStatement(sql.toString());
            logger.info(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiMobileDataRaw();
                obj.setUserName(DBUtil.getString(rs.getObject("user_name")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setStartTime(new Date(rs.getTimestamp("start_time").getTime()));
//                obj.setEndTime(new Date(rs.getTimestamp("end_time").getTime()));
                obj.setResponseCode(DBUtil.getString(rs.getObject("response_code")));
                obj.setOsVersion(DBUtil.getString(rs.getObject("os_version")));
                obj.setDeviceModel(DBUtil.getString(rs.getObject("device_model")));
                obj.setImei(DBUtil.getString(rs.getObject("imei")));
                obj.setNetworkType(DBUtil.getString(rs.getObject("network_type")));
                obj.setInsertTime(new Date());
                obj.setUrlRequest(DBUtil.getString(rs.getObject("url_request")));
                obj.setRequestID(DBUtil.getString(rs.getObject("request_id")));
                obj.setLatitude(DBUtil.getString(rs.getObject("latitude")));
                obj.setLongitude(DBUtil.getString(rs.getObject("longitude")));
                obj.setSignal(DBUtil.getString(rs.getObject("signal")));
                obj.setAppVersion(DBUtil.getString(rs.getObject("app_version")));
                obj.setErrorDescription(DBUtil.getString(rs.getObject("error_description")));
                obj.setCellId(DBUtil.getString(rs.getObject("cell_id")));
                obj.setLacId(DBUtil.getString(rs.getObject("lac_id")));
                obj.setRequestView(DBUtil.getString(rs.getObject("request_view")));
                obj.setDuration(DBUtil.getLong(rs.getObject("duration")));
                obj.setDownload(DBUtil.getLong(rs.getObject("download")));
                obj.setUpload(DBUtil.getLong(rs.getObject("upload")));
                obj.setRam(DBUtil.getString(rs.getObject("ram")));
                obj.setCpu(DBUtil.getString(rs.getObject("cpu")));
                lst.add(obj);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            try {
                close(con);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return lst;
    }

    public synchronized void insertKpiMobileRaw(List<KpiMobileDataRaw> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_mobile_data_raw \n");
            sql.append(" (id, user_name, app_name, start_time, end_time,\n");
            sql.append("       response_code, os_version, device_model, imei,\n");
            sql.append("       network_type, insert_time, url_request, request_id,\n");
            sql.append("       latitude, longitude, signal, app_version,\n");
            sql.append("       error_description, cell_id, lac_id, request_view,\n");
            sql.append("       duration, download, upload, ram, cpu) \n");
            sql.append(" VALUES(KPI_MOBILE_DATA_RAW_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,");
            sql.append("        ?,?,?,?,?,?,?,?,?,?,?,?,?)");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            for (KpiMobileDataRaw bo : lst) {

                //<editor-fold defaultstate="collapsed" desc="set param">
                if (bo.getUserName() != null) {
                    pstmt.setString(1, bo.getUserName());
                } else {
                    pstmt.setNull(1, java.sql.Types.VARCHAR);
                }
                if (bo.getAppName() != null) {
                    pstmt.setString(2, bo.getAppName());
                } else {
                    pstmt.setNull(2, java.sql.Types.VARCHAR);
                }
                if (bo.getStartTime() != null) {
                    pstmt.setTimestamp(3, new java.sql.Timestamp(bo.getStartTime().getTime()));
                } else {
                    pstmt.setNull(3, java.sql.Types.TIMESTAMP);
                }
                if (bo.getEndTime() != null) {
                    pstmt.setTimestamp(4, new java.sql.Timestamp(bo.getEndTime().getTime()));
                } else {
                    pstmt.setNull(4, java.sql.Types.TIMESTAMP);
                }
                if (bo.getResponseCode() != null) {
                    pstmt.setString(5, bo.getResponseCode());
                } else {
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                }
                if (bo.getOsVersion() != null) {
                    pstmt.setString(6, bo.getOsVersion());
                } else {
                    pstmt.setNull(6, java.sql.Types.VARCHAR);
                }
                if (bo.getDeviceModel() != null) {
                    pstmt.setString(7, bo.getDeviceModel());
                } else {
                    pstmt.setNull(7, java.sql.Types.VARCHAR);
                }
                if (bo.getImei() != null) {
                    pstmt.setString(8, bo.getImei());
                } else {
                    pstmt.setNull(8, java.sql.Types.VARCHAR);
                }
                if (bo.getNetworkType() != null) {
                    pstmt.setString(9, bo.getNetworkType());
                } else {
                    pstmt.setNull(9, java.sql.Types.VARCHAR);
                }
                if (bo.getInsertTime() != null) {
                    pstmt.setTimestamp(10, new java.sql.Timestamp(bo.getInsertTime().getTime()));
                } else {
                    pstmt.setNull(10, java.sql.Types.TIMESTAMP);
                }
                if (bo.getUrlRequest() != null) {
                    pstmt.setString(11, bo.getUrlRequest());
                } else {
                    pstmt.setNull(11, java.sql.Types.VARCHAR);
                }
                if (bo.getRequestID() != null) {
                    pstmt.setString(12, bo.getRequestID());
                } else {
                    pstmt.setNull(12, java.sql.Types.VARCHAR);
                }
                if (bo.getLatitude() != null) {
                    pstmt.setString(13, bo.getLatitude());
                } else {
                    pstmt.setNull(13, java.sql.Types.VARCHAR);
                }
                if (bo.getLongitude() != null) {
                    pstmt.setString(14, bo.getLongitude());
                } else {
                    pstmt.setNull(14, java.sql.Types.VARCHAR);
                }
                if (bo.getSignal() != null) {
                    pstmt.setString(15, bo.getSignal());
                } else {
                    pstmt.setNull(15, java.sql.Types.VARCHAR);
                }
                if (bo.getAppVersion() != null) {
                    pstmt.setString(16, bo.getAppVersion());
                } else {
                    pstmt.setNull(16, java.sql.Types.VARCHAR);
                }
                if (bo.getErrorDescription() != null) {
                    pstmt.setString(17, bo.getErrorDescription());
                } else {
                    pstmt.setNull(17, java.sql.Types.VARCHAR);
                }
                if (bo.getCellId() != null) {
                    pstmt.setString(18, bo.getCellId());
                } else {
                    pstmt.setNull(18, java.sql.Types.VARCHAR);
                }
                if (bo.getLacId() != null) {
                    pstmt.setString(19, bo.getLacId());
                } else {
                    pstmt.setNull(19, java.sql.Types.VARCHAR);
                }
                if (bo.getRequestView() != null) {
                    pstmt.setString(20, bo.getRequestView());
                } else {
                    pstmt.setNull(20, java.sql.Types.VARCHAR);
                }
                if (bo.getDuration() != null) {
                    pstmt.setLong(21, bo.getDuration());
                } else {
                    pstmt.setNull(21, java.sql.Types.NUMERIC);
                }
                if (bo.getDownload() != null) {
                    pstmt.setLong(22, bo.getDownload());
                } else {
                    pstmt.setNull(22, java.sql.Types.NUMERIC);
                }
                if (bo.getUpload() != null) {
                    pstmt.setLong(23, bo.getUpload());
                } else {
                    pstmt.setNull(23, java.sql.Types.NUMERIC);
                }
                if (bo.getRam() != null) {
                    pstmt.setString(24, bo.getRam());
                } else {
                    pstmt.setNull(24, java.sql.Types.VARCHAR);
                }
                if (bo.getCpu() != null) {
                    pstmt.setString(25, bo.getCpu());
                } else {
                    pstmt.setNull(25, java.sql.Types.VARCHAR);
                }
                //</editor-fold>

                i++;
                pstmt.addBatch();
                if (i >= 200) {
                    pstmt.executeBatch();
                    pstmt.clearBatch();
                    logger.info("InsertData(): " + i + "  records");
                    i = 0;
                }
            }
            pstmt.executeBatch();
            commit(con);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            try {
                close(con);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    public synchronized void deleteKpiMobileRawTemp(List<KpiMobileDataRaw> lst, Date now) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            String _now = DateTimeUtils.format(now, "dd/MM/yyyy");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE kpi_mobile_data_raw_temp PARTITION FOR(to_date('").append(_now).append("','dd/MM/yyyy')) ");
            sql.append("WHERE request_id = ? ");
            pstmt = con.prepareStatement(sql.toString());

            for (KpiMobileDataRaw bo : lst) {
                pstmt.setString(1, bo.getRequestID());
                i++;
                pstmt.addBatch();
                if (i >= 50) {
                    pstmt.executeBatch();
                    pstmt.clearBatch();
                    logger.info("DeteleData(): " + i + "  records");
                    i = 0;
                }
            }
            pstmt.executeBatch();

            commit(con);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            try {
                close(con);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
