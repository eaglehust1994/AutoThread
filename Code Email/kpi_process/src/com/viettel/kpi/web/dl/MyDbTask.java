/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.dl;

import com.viettel.kpi.common.utils.DBUtil;
import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.kpi.common.utils.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyDbTask extends DbTask {

    private static final Logger logger = Logger.getLogger(MyDbTask.class);

    public List<KpiDataRawDl> getKpiWebRawDl(String updateTime) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiDataRawDl> lst = new ArrayList<KpiDataRawDl>();
        KpiDataRawDl obj = null;
        try {
            logger.info("Get Kpi Web Raw Dl");
            con = getConnectionPool();
            logger.info("con " + con);

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.url_id ,c.url_pattern,COUNT(a.url_id) total_request,b.kpi,\n");
            sql.append("round(decode(COUNT(a.url_id),0,0,100*SUM(a.is_success)/COUNT(a.url_id)),2) succ_rate,\n");
            sql.append("round(100-decode(COUNT(a.url_id),0,0,100*SUM(a.is_success)/COUNT(a.url_id)),2) fail_rate,\n");
            sql.append("round(decode(COUNT(a.url_id),0,0,100*SUM(a.err_2x)/COUNT(a.url_id)),2) err_2x_rate,\n");
            sql.append("round(decode(COUNT(a.url_id),0,0,100*SUM(a.err_3x)/COUNT(a.url_id)),2) err_3x_rate,\n");
            sql.append("round(decode(COUNT(a.url_id),0,0,100*SUM(a.err_4x)/COUNT(a.url_id)),2) err_4x_rate,\n");
            sql.append("round(decode(COUNT(a.url_id),0,0,100*SUM(a.err_5x)/COUNT(a.url_id)),2) err_5x_rate,\n");
            sql.append("round(decode(COUNT(a.url_id),0,0,100*SUM(a.is_timeout)/COUNT(a.url_id)),2) timeout_rate,\n");
            sql.append("CASE WHEN b.kpi>c.target*1000 THEN 0 ELSE 1 END is_ok,\n");
            sql.append("round(decode(COUNT(a.url_id),0,0,100*SUM(a.is_exceed)/COUNT(a.url_id)),2) exceed_rate,\n");
            sql.append("COUNT(a.url_id) - SUM(a.is_timeout) no_sample,MAX(a.response_time) max_response_time,\n");
            sql.append("trunc(sysdate)-1 update_time,SYSDATE insert_time\n");
            sql.append("FROM \n");
            sql.append("(SELECT * FROM KPI_DATA_RAW PARTITION FOR (to_date('").append(updateTime).append("','dd/MM/yyyy'))\n");
            sql.append("WHERE 1=1 \n");
            sql.append(")a \n");
            sql.append("LEFT JOIN \n");
            sql.append("(SELECT DISTINCT a.url_id ,\n");
            sql.append("PERCENTILE_DISC(0.95) WITHIN GROUP (ORDER BY a.response_time ) OVER (PARTITION BY a.url_id)  kpi\n");
            sql.append("FROM KPI_DATA_RAW PARTITION FOR (to_date('").append(updateTime).append("','dd/MM/yyyy')) a\n");
            sql.append("WHERE 1=1 \n");
            sql.append(") b ON a.url_id = b.url_id\n");
            sql.append("LEFT JOIN \n");
            sql.append("CATA_URL c ON a.url_id = c.url_id\n");
            sql.append("WHERE a.response_time is not null\n");
            sql.append("GROUP BY a.url_id ,c.url_pattern, b.kpi,c.target");

            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);

            rs = pstmt.executeQuery();
            logger.info("rs " + rs);
            while (rs.next()) {
                obj = new KpiDataRawDl();
                obj.setUrlId(DBUtil.getLong(rs.getObject("url_id")));
                obj.setUrlPattern(DBUtil.getString(rs.getObject("url_pattern")));
                obj.setTotalRequest(DBUtil.getLong(rs.getObject("total_request")));
                obj.setKpi(DBUtil.getDouble(rs.getObject("kpi")));
                obj.setSuccRate(DBUtil.getDouble(rs.getObject("succ_rate")));
                obj.setFailRate(DBUtil.getDouble(rs.getObject("fail_rate")));
                obj.setErr2xRate(DBUtil.getDouble(rs.getObject("err_2x_rate")));
                obj.setErr3xRate(DBUtil.getDouble(rs.getObject("err_3x_rate")));
                obj.setErr4xRate(DBUtil.getDouble(rs.getObject("err_4x_rate")));
                obj.setErr5xRate(DBUtil.getDouble(rs.getObject("err_5x_rate")));
                obj.setTimeoutRate(DBUtil.getDouble(rs.getObject("timeout_rate")));
                obj.setIsOk(DBUtil.getLong(rs.getObject("is_ok")));
                obj.setExceedRate(DBUtil.getDouble(rs.getObject("exceed_rate")));
                obj.setNoSample(DBUtil.getLong(rs.getObject("no_sample")));
                obj.setMaxResponseTime(DBUtil.getLong(rs.getObject("max_response_time")));
                obj.setUpdateTime(DateTimeUtils.parse(updateTime, "dd/MM/yyyy"));
                obj.setInsertTime(rs.getTimestamp("insert_time"));
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

    public synchronized void insertKpiWebRawDl(String updateTime, List<KpiDataRawDl> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert KPI Data Raw Dl");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_data_raw_dl \n");
            sql.append(" (url_id, url_pattern, ip_lan, ip_wan, ip_server, \n");
            sql.append(" port_server, kpi, total_request, succ_rate, fail_rate, \n");
            sql.append(" timeout_rate, err_2x_rate, err_3x_rate, err_4x_rate, \n");
            sql.append(" err_5x_rate, is_ok, exceed_rate, max_response_time, \n");
            sql.append(" no_sample, insert_time, update_time) \n");
            sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            for (KpiDataRawDl obj : lst) {
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

                pstmt.setNull(3, java.sql.Types.VARCHAR);
                pstmt.setNull(4, java.sql.Types.VARCHAR);
                pstmt.setNull(5, java.sql.Types.VARCHAR);
                pstmt.setNull(6, java.sql.Types.VARCHAR);

                if (obj.getKpi() != null) {
                    pstmt.setDouble(7, obj.getKpi());
                } else {
                    pstmt.setNull(7, java.sql.Types.NUMERIC);
                }
                if (obj.getTotalRequest() != null) {
                    pstmt.setLong(8, obj.getTotalRequest());
                } else {
                    pstmt.setNull(8, java.sql.Types.NUMERIC);
                }
                if (obj.getSuccRate() != null) {
                    pstmt.setDouble(9, obj.getSuccRate());
                } else {
                    pstmt.setNull(9, java.sql.Types.NUMERIC);
                }
                if (obj.getFailRate() != null) {
                    pstmt.setDouble(10, obj.getFailRate());
                } else {
                    pstmt.setNull(10, java.sql.Types.NUMERIC);
                }
                if (obj.getTimeoutRate() != null) {
                    pstmt.setDouble(11, obj.getTimeoutRate());
                } else {
                    pstmt.setNull(11, java.sql.Types.NUMERIC);
                }
                if (obj.getErr2xRate() != null) {
                    pstmt.setDouble(12, obj.getErr2xRate());
                } else {
                    pstmt.setNull(12, java.sql.Types.NUMERIC);
                }
                if (obj.getErr3xRate() != null) {
                    pstmt.setDouble(13, obj.getErr3xRate());
                } else {
                    pstmt.setNull(13, java.sql.Types.NUMERIC);
                }
                if (obj.getErr4xRate() != null) {
                    pstmt.setDouble(14, obj.getErr4xRate());
                } else {
                    pstmt.setNull(14, java.sql.Types.NUMERIC);
                }
                if (obj.getErr5xRate() != null) {
                    pstmt.setDouble(15, obj.getErr5xRate());
                } else {
                    pstmt.setNull(15, java.sql.Types.NUMERIC);
                }
                if (obj.getIsOk() != null) {
                    pstmt.setLong(16, obj.getIsOk());
                } else {
                    pstmt.setNull(16, java.sql.Types.NUMERIC);
                }
                if (obj.getExceedRate() != null) {
                    pstmt.setDouble(17, obj.getExceedRate());
                } else {
                    pstmt.setNull(17, java.sql.Types.NUMERIC);
                }
                if (obj.getMaxResponseTime() != null) {
                    pstmt.setLong(18, obj.getMaxResponseTime());
                } else {
                    pstmt.setNull(18, java.sql.Types.NUMERIC);
                }
                if (obj.getNoSample() != null) {
                    pstmt.setLong(19, obj.getNoSample());
                } else {
                    pstmt.setNull(19, java.sql.Types.NUMERIC);
                }
                if (obj.getInsertTime() != null) {
                    pstmt.setTimestamp(20, new java.sql.Timestamp(obj.getInsertTime().getTime()));
                } else {
                    pstmt.setNull(20, java.sql.Types.TIMESTAMP);
                }
                if (obj.getUpdateTime() != null) {
                    pstmt.setDate(21, new java.sql.Date(obj.getUpdateTime().getTime()));
                } else {
                    pstmt.setNull(21, java.sql.Types.DATE);
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

    public synchronized void deleteKpiWebRawDl(String updateTime) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele KPI Data Raw Dl");

            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_data_raw_dl PARTITION FOR (to_date('").append(updateTime).append("','dd/MM/yyyy'))");

            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            
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
}
