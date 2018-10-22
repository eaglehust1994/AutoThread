/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.hl;

import com.viettel.kpi.common.utils.DBUtil;
import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.kpi.common.utils.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    public List<KpiDataRaw> getKpiDataRaw(Date updateTime, int hour) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiDataRaw> lst = new ArrayList<KpiDataRaw>();
        KpiDataRaw obj = null;
        try {
            logger.info("Get Kpi Web Raw");
            if (hour < 0) {
                hour = 23;
                updateTime = DateTimeUtils.add(updateTime, -1);
                logger.info("hour :" + hour);
                logger.info("updateTime :" + updateTime);
            }
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            logger.info("con :" + con);
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.id, a.url_id ,a.url, a.ip_lan, a.ip_wan ,a.ip_server, a.port_server , \n");
            sql.append("b.response_status , a.insert_time update_time,sysdate insert_time, \n");
            sql.append("CASE WHEN (b.total_time_long >= a.insert_time_long) THEN (b.total_time_long - a.insert_time_long) ELSE 0 END response_time, \n");
            sql.append("CASE WHEN (b.response_status = '200' OR b.response_status = '302' OR b.response_status = '(Cache)') THEN 1 ELSE 0 END is_success, \n");
            sql.append("CASE WHEN (SUBSTR(b.response_status,0,1)='2' AND b.response_status != '200') THEN 1 ELSE 0 END err_2x, \n");
            sql.append("CASE WHEN SUBSTR(b.response_status,0,1)='3' THEN 1 ELSE 0 END err_3x, \n");
            sql.append("CASE WHEN SUBSTR(b.response_status,0,1)='4' THEN 1 ELSE 0 END err_4x, \n");
            sql.append("CASE WHEN SUBSTR(b.response_status,0,1)='5' THEN 1 ELSE 0 END err_5x, \n");
            sql.append("CASE WHEN(b.total_time_long - a.insert_time_long) < c.timeout*1000 THEN 0 ELSE 1 END is_timeout, \n");
            sql.append("CASE WHEN(b.total_time_long - a.insert_time_long) < c.target*1000 THEN 0 ELSE 1 END is_exceed, \n");
            sql.append("a.client_time start_time, \n");
            sql.append("b.total_time_long,b.content_type \n");
            sql.append("FROM \n");
            sql.append("(SELECT a.id, a.url_id,a.ip_lan1  ip_lan, a.ip_wan, a.url, a.insert_time,a.insert_time_long, \n");
            sql.append("a.type, a.response_status, a.ip_server, a.port_server,a.client_time \n");
            sql.append("FROM KPI_DATA_RAW_TEMP PARTITION(DATA").append(partition).append(") a \n");
            sql.append("WHERE a.TYPE = 1 \n");
            sql.append("AND a.insert_time_long IS NOT NULL \n");
            sql.append("AND a.insert_time_long > 0 \n");
            sql.append("AND to_char(insert_time,'HH24') = ? \n");
//            sql.append("AND a.insert_time >= ? \n");
//            sql.append("AND a.insert_time < ? \n");
            sql.append(") a \n");
            sql.append("LEFT JOIN \n");
            sql.append("(SELECT a.id, a.url,a.insert_time_long, a.type, a.response_status,a.total_time_long,a.content_type \n");
            sql.append("FROM KPI_DATA_RAW_TEMP PARTITION(DATA").append(partition).append(") a \n");
            sql.append("WHERE a.TYPE = 2 \n");
            sql.append("AND a.total_time_long IS NOT NULL \n");
            sql.append("AND a.total_time_long > 0 \n");
            sql.append("AND to_char(insert_time,'HH24') = ? \n");
//            sql.append("AND a.insert_time >= ? \n");
//            sql.append("AND a.insert_time < ? \n");
            sql.append(") b \n");
            sql.append("ON a.id = b.id AND a.url=b.url \n");
            sql.append("LEFT JOIN CATA_URL c ON a.url_id = c.url_id \n");
            sql.append("WHERE 1=1 \n");
            sql.append("AND a.id != 1 ");
            sql.append("AND b.total_time_long IS NOT NULL ");

//            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt :" + pstmt);
            pstmt.setInt(1, hour);
            pstmt.setInt(2, hour);
//            pstmt.setTimestamp(1, new Timestamp(startTime.getTime()));
//            pstmt.setTimestamp(2, new Timestamp(endTime.getTime()));
//            pstmt.setTimestamp(3, new Timestamp(startTime.getTime()));
//            pstmt.setTimestamp(4, new Timestamp(endTime.getTime()));
            rs = pstmt.executeQuery();
            logger.info("rs :" + rs);
            while (rs.next()) {
                obj = new KpiDataRaw();
                obj.setId(DBUtil.getLong(rs.getObject("id")));
                obj.setUrlId(DBUtil.getLong(rs.getObject("url_id")));
                obj.setIpLan(DBUtil.getString(rs.getObject("ip_lan")));
                obj.setIpWan(DBUtil.getString(rs.getObject("ip_wan")));
                obj.setUrl(DBUtil.getString(rs.getObject("url")));
                obj.setIpServer(DBUtil.getString(rs.getObject("ip_server")));
                obj.setPortServer(DBUtil.getString(rs.getObject("port_server")));
                obj.setResponseStatus(DBUtil.getString(rs.getObject("response_status")));
                obj.setUpdateTime(rs.getTimestamp("update_time"));
                obj.setInsertTime(rs.getTimestamp("insert_time"));
                obj.setResponseTime(DBUtil.getLong(rs.getObject("response_time")));
                obj.setIsSuccess(DBUtil.getLong(rs.getObject("is_success")));
                obj.setErr2x(DBUtil.getLong(rs.getObject("err_2x")));
                obj.setErr3x(DBUtil.getLong(rs.getObject("err_3x")));
                obj.setErr4x(DBUtil.getLong(rs.getObject("err_4x")));
                obj.setErr5x(DBUtil.getLong(rs.getObject("err_5x")));
                obj.setIsTimeout(DBUtil.getLong(rs.getObject("is_timeout")));
                obj.setIsExceed(DBUtil.getLong(rs.getObject("is_exceed")));
                obj.setStartTime(rs.getTimestamp("start_time"));
                try {
                    obj.setEndTime(new Date(DBUtil.getLong(rs.getObject("total_time_long"))));
                } catch (Exception ex) {
                    logger.info("total_time_long :" + rs.getObject("total_time_long"));
                    logger.error(ex.getMessage(), ex);
                }
                obj.setContentType(DBUtil.getString(rs.getObject("content_type")));
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

    public synchronized void insertKpiDataRaw(List<KpiDataRaw> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert KPI Web Raw");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_data_raw \n");
            sql.append(" (id, url_id, ip_wan, ip_lan, url, insert_time, \n");
            sql.append(" update_time, response_time, response_status, ip_server, \n");
            sql.append(" port_server, is_success, err_2x, err_3x, err_4x, \n");
            sql.append(" err_5x, is_timeout, is_exceed, start_time, end_time,content_type) \n");
            sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            for (KpiDataRaw obj : lst) {
                if (obj.getId() != null) {
                    pstmt.setLong(1, obj.getId());
                } else {
                    pstmt.setNull(1, java.sql.Types.NUMERIC);
                }
                if (obj.getUrlId() != null) {
                    pstmt.setLong(2, obj.getUrlId());
                } else {
                    pstmt.setNull(2, java.sql.Types.NUMERIC);
                }
                if (obj.getIpWan() != null) {
                    pstmt.setString(3, obj.getIpWan());
                } else {
                    pstmt.setNull(3, java.sql.Types.VARCHAR);
                }
                if (obj.getIpLan() != null) {
                    pstmt.setString(4, obj.getIpLan());
                } else {
                    pstmt.setNull(4, java.sql.Types.VARCHAR);
                }
                if (obj.getUrl() != null) {
                    pstmt.setString(5, obj.getUrl());
                } else {
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                }
                if (obj.getInsertTime() != null) {
                    pstmt.setTimestamp(6, new Timestamp(obj.getInsertTime().getTime()));
                } else {
                    pstmt.setNull(6, java.sql.Types.TIMESTAMP);
                }
                if (obj.getUpdateTime() != null) {
                    pstmt.setTimestamp(7, new Timestamp(obj.getUpdateTime().getTime()));
                } else {
                    pstmt.setNull(7, java.sql.Types.TIMESTAMP);
                }
                if (obj.getResponseTime() != null) {
                    pstmt.setLong(8, obj.getResponseTime());
                } else {
                    pstmt.setNull(8, java.sql.Types.NUMERIC);
                }
                if (obj.getResponseStatus() != null) {
                    pstmt.setString(9, obj.getResponseStatus());
                } else {
                    pstmt.setNull(9, java.sql.Types.VARCHAR);
                }
                if (obj.getIpServer() != null) {
                    pstmt.setString(10, obj.getIpServer());
                } else {
                    pstmt.setNull(10, java.sql.Types.VARCHAR);
                }
                if (obj.getPortServer() != null) {
                    pstmt.setString(11, obj.getPortServer());
                } else {
                    pstmt.setNull(11, java.sql.Types.VARCHAR);
                }
                if (obj.getIsSuccess() != null) {
                    pstmt.setLong(12, obj.getIsSuccess());
                } else {
                    pstmt.setNull(12, java.sql.Types.NUMERIC);
                }
                if (obj.getErr2x() != null) {
                    pstmt.setLong(13, obj.getErr2x());
                } else {
                    pstmt.setNull(13, java.sql.Types.NUMERIC);
                }
                if (obj.getErr3x() != null) {
                    pstmt.setLong(14, obj.getErr3x());
                } else {
                    pstmt.setNull(14, java.sql.Types.NUMERIC);
                }
                if (obj.getErr4x() != null) {
                    pstmt.setLong(15, obj.getErr4x());
                } else {
                    pstmt.setNull(15, java.sql.Types.NUMERIC);
                }
                if (obj.getErr5x() != null) {
                    pstmt.setLong(16, obj.getErr5x());
                } else {
                    pstmt.setNull(16, java.sql.Types.NUMERIC);
                }
                if (obj.getIsTimeout() != null) {
                    pstmt.setLong(17, obj.getIsTimeout());
                } else {
                    pstmt.setNull(17, java.sql.Types.NUMERIC);
                }
                if (obj.getIsExceed() != null) {
                    pstmt.setLong(18, obj.getIsExceed());
                } else {
                    pstmt.setNull(18, java.sql.Types.NUMERIC);
                }
                if (obj.getStartTime() != null) {
                    pstmt.setTimestamp(19, new Timestamp(obj.getStartTime().getTime()));
                } else {
                    pstmt.setNull(19, java.sql.Types.TIMESTAMP);
                }
                if (obj.getEndTime() != null) {
                    pstmt.setTimestamp(20, new Timestamp(obj.getEndTime().getTime()));
                } else {
                    pstmt.setNull(20, java.sql.Types.TIMESTAMP);
                }
                if (obj.getContentType() != null) {
                    pstmt.setString(21, obj.getContentType());
                } else {
                    pstmt.setNull(21, java.sql.Types.VARCHAR);
                }
                i++;
                pstmt.addBatch();
                if (i >= 1000) {
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

    public synchronized void deleteKpiDataRaw(Date updateTime, int hour) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele KPI Web Raw");
            if (hour < 0) {
                hour = 23;
                updateTime = DateTimeUtils.add(updateTime, -1);
                logger.info("hour :" + hour);
                logger.info("updateTime :" + updateTime);
            }
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");

            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_data_raw PARTITION(DATA").append(partition).append(")");
            sql.append(" WHERE to_char(update_time,'HH24') = ? ");

            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            
            pstmt.setInt(1, hour);
            pstmt.executeQuery();
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

    public List<KpiDataRawHl> getKpiWebRawHl(Date updateTime, int hour) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiDataRawHl> lst = new ArrayList<KpiDataRawHl>();
        KpiDataRawHl obj = null;
        try {
            if (hour < 0) {
                hour = 23;
                updateTime = DateTimeUtils.add(updateTime, -1);
                logger.info("hour :" + hour);
                logger.info("updateTime :" + updateTime);
            }
            logger.info("Get Kpi Web Raw Hl");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.url_id ,c.url_pattern, COUNT(a.url_id) total_request,b.kpi, \n");
            sql.append("round(decode(COUNT(a.url_id),0,0,100*SUM(a.is_success)/COUNT(a.url_id)),2) succ_rate, \n");
            sql.append("round(decode(COUNT(a.url_id),0,0,100*SUM(a.is_exceed)/COUNT(a.url_id)),2) exceed_rate, \n");
            sql.append("MAX(a.response_time) max_response_time, \n");
            sql.append("trunc(a.start_time) update_time,to_char(a.start_time,'HH24') hour,SYSDATE insert_time \n");
            sql.append("FROM \n");
            sql.append("(SELECT * \n");
            sql.append("FROM KPI_DATA_RAW PARTITION(DATA").append(partition).append(") a  \n");
            sql.append("WHERE to_char(start_time,'HH24') = ? \n");
            sql.append("AND trunc(a.start_time) = ? \n");
            sql.append(")a \n");
            sql.append("LEFT JOIN \n");
            sql.append("(SELECT DISTINCT a.url_id, PERCENTILE_DISC(0.95) WITHIN GROUP (ORDER BY a.response_time ) OVER (PARTITION BY a.url_id)  kpi \n");
            sql.append("FROM KPI_DATA_RAW PARTITION(DATA").append(partition).append(") a  \n");
            sql.append("WHERE to_char(a.start_time,'HH24') = ? \n");
            sql.append("AND trunc(a.start_time) = ? \n");
            sql.append(") b ON a.url_id = b.url_id \n");
            sql.append("LEFT JOIN \n");
            sql.append("CATA_URL c ON a.url_id = c.url_id \n");
            sql.append("WHERE a.response_time is not null \n");
            sql.append("GROUP BY a.url_id ,c.url_pattern, b.kpi,c.target,trunc(a.start_time), to_char(a.start_time,'HH24') \n");

//            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            
            pstmt.setInt(1, hour);
            pstmt.setDate(2, new java.sql.Date(updateTime.getTime()));
            pstmt.setInt(3, hour);
            pstmt.setDate(4, new java.sql.Date(updateTime.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiDataRawHl();
                obj.setUrlId(DBUtil.getLong(rs.getObject("url_id")));
                obj.setUrlPattern(DBUtil.getString(rs.getObject("url_pattern")));
                obj.setTotalRequest(DBUtil.getLong(rs.getObject("total_request")));
                obj.setKpi(DBUtil.getDouble(rs.getObject("kpi")));
                obj.setSuccRate(DBUtil.getDouble(rs.getObject("succ_rate")));
                obj.setExceedRate(DBUtil.getDouble(rs.getObject("exceed_rate")));
                obj.setMaxResponseTime(DBUtil.getLong(rs.getObject("max_response_time")));
                obj.setUpdateTime(rs.getDate("update_time"));
                obj.setHour(DBUtil.getLong(rs.getObject("hour")));
                obj.setInsertTime(rs.getDate("insert_time"));
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

    public synchronized void insertKpiWebRawHl(Date updateTime, List<KpiDataRawHl> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert KPI Data Raw Hl");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_data_raw_hl \n");
            sql.append(" (url_id, url_pattern, kpi, total_request, succ_rate, \n");
            sql.append(" exceed_rate, max_response_time, insert_time, \n");
            sql.append(" update_time, hour) \n");
            sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            
            for (KpiDataRawHl obj : lst) {
                if (obj.getUrlId() != null) {
                    pstmt.setLong(1, obj.getUrlId());
                } else {
                    pstmt.setNull(1, java.sql.Types.NUMERIC);
                }
                if (obj.getUrlPattern() != null) {
                    pstmt.setString(2, obj.getUrlPattern());
                } else {
                    pstmt.setNull(2, java.sql.Types.VARCHAR);
                }
                if (obj.getKpi() != null) {
                    pstmt.setDouble(3, obj.getKpi());
                } else {
                    pstmt.setNull(3, java.sql.Types.NUMERIC);
                }
                if (obj.getTotalRequest() != null) {
                    pstmt.setLong(4, obj.getTotalRequest());
                } else {
                    pstmt.setNull(4, java.sql.Types.NUMERIC);
                }
                if (obj.getSuccRate() != null) {
                    pstmt.setDouble(5, obj.getSuccRate());
                } else {
                    pstmt.setNull(5, java.sql.Types.NUMERIC);
                }
                if (obj.getExceedRate() != null) {
                    pstmt.setDouble(6, obj.getExceedRate());
                } else {
                    pstmt.setNull(6, java.sql.Types.NUMERIC);
                }
                if (obj.getMaxResponseTime() != null) {
                    pstmt.setLong(7, obj.getMaxResponseTime());
                } else {
                    pstmt.setNull(7, java.sql.Types.NUMERIC);
                }
                if (obj.getInsertTime() != null) {
                    pstmt.setTimestamp(8, new Timestamp(new Date().getTime()));
                } else {
                    pstmt.setNull(8, java.sql.Types.TIMESTAMP);
                }
                if (obj.getUpdateTime() != null) {
//                    logger.info("updateTime: " + obj.getUpdateTime());
                    //check if vuot qua partition
                    if (obj.getUpdateTime().getTime() <= new Date().getTime()) {
                        pstmt.setTimestamp(9, new Timestamp(obj.getUpdateTime().getTime()));
                    } else {
                        pstmt.setTimestamp(9, new Timestamp(updateTime.getTime()));
                    }
                } else {
                    pstmt.setNull(9, java.sql.Types.TIMESTAMP);
                }
                if (obj.getHour() != null) {
                    pstmt.setLong(10, obj.getHour());
                } else {
                    pstmt.setNull(10, java.sql.Types.NUMERIC);
                }
                i++;
                pstmt.addBatch();
                if (i >= 1000) {
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

    public synchronized void deleteKpiWebRawHl(Date updateTime, int hour) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            if (hour < 0) {
                hour = 23;
                updateTime = DateTimeUtils.add(updateTime, -1);
                logger.info("hour :" + hour);
                logger.info("updateTime :" + updateTime);
            }
            logger.info("Detele KPI Data Raw Hl");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");

            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_data_raw_hl PARTITION(DATA").append(partition).append(")");
            sql.append(" WHERE hour = ? ");

            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            
            pstmt.setInt(1, hour);
            pstmt.executeQuery();
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

    public boolean existData(Date updateTime, int hour) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isExistData = false;
        long total;
        try {
            logger.info("Check Data KPI Data Raw ");
            if (hour < 0) {
                hour = 23;
                updateTime = DateTimeUtils.add(updateTime, -1);
                logger.info("hour :" + hour);
                logger.info("updateTime :" + updateTime);
            }
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");

            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT count(*) total FROM kpi_data_raw PARTITION(DATA").append(partition).append(")");
            sql.append(" WHERE to_char(update_time,'HH24') = ? ");

            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            
            pstmt.setInt(1, hour);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                total = DBUtil.getLong(rs.getLong("total"));
                if (total > 0) {
                    isExistData = true;
                }
            }
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
        return isExistData;
    }

}
