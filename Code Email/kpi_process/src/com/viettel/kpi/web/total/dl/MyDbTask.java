/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.total.dl;

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

    public List<RpKpiToantrinhDl> getRpKpiToantrinhDl(String updateTime) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<RpKpiToantrinhDl> lst = new ArrayList<RpKpiToantrinhDl>();
        RpKpiToantrinhDl obj = null;
        try {
//            String partition = DateTimeUtils.format(DateTimeUtils.parse(updateTime, "dd/MM/yyyy"),"yyyyMMdd");
            logger.info("Get Report Kpi Total Dl");
            con = getConnectionPool();
            logger.info("con " + con);

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.url_id ,a.app_code,COUNT(a.url_id) total,b.kpi_server,c.kpi_client,\n");
            sql.append("to_date('").append(updateTime).append("','dd/MM/yyyy') update_time,SYSDATE insert_time\n");
            sql.append("FROM \n");
            sql.append("(SELECT * FROM rp_kpi_toantrinh PARTITION FOR (to_date('").append(updateTime).append("','dd/MM/yyyy'))\n");
//            sql.append("(SELECT * FROM rp_kpi_toantrinh PARTITION(DATA").append(partition).append(")\n");
            sql.append("WHERE 1=1 \n");
            sql.append(")a \n");
            sql.append("LEFT JOIN \n");
            sql.append("(SELECT DISTINCT a.url_id ,\n");
            sql.append("PERCENTILE_DISC(0.95) WITHIN GROUP (ORDER BY a.server_duration) OVER (PARTITION BY a.url_id)  kpi_server\n");
            sql.append("FROM rp_kpi_toantrinh PARTITION FOR (to_date('").append(updateTime).append("','dd/MM/yyyy')) a\n");
//            sql.append("FROM rp_kpi_toantrinh PARTITION(DATA").append(partition).append(") a\n");
            sql.append("WHERE 1=1 \n");
            sql.append(") b ON a.url_id = b.url_id\n");
            sql.append("LEFT JOIN \n");
            sql.append("(SELECT DISTINCT a.url_id ,\n");
            sql.append("PERCENTILE_DISC(0.95) WITHIN GROUP (ORDER BY a.duration) OVER (PARTITION BY a.url_id)  kpi_client\n");
            sql.append("FROM rp_kpi_toantrinh PARTITION FOR (to_date('25/02/2016','dd/MM/yyyy')) a\n");
            sql.append("WHERE 1=1 \n");
            sql.append(") c ON a.url_id = c.url_id\n");
            sql.append("WHERE a.server_duration is not null\n");
            sql.append("GROUP BY a.url_id ,a.app_code, b.kpi_server, c.kpi_client");

            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);

            rs = pstmt.executeQuery();
            logger.info("rs " + rs);
            while (rs.next()) {
                obj = new RpKpiToantrinhDl();
                obj.setUrlId(DBUtil.getLong(rs.getObject("url_id")));
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setTotal(DBUtil.getLong(rs.getObject("total")));
                obj.setKpiServer(DBUtil.getDouble(rs.getObject("kpi_server")));
                obj.setKpiClient(DBUtil.getDouble(rs.getObject("kpi_client")));
                obj.setUpdateTime(rs.getDate("update_time"));
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

    public synchronized void insertRpKpiToantrinhDl(String updateTime, List<RpKpiToantrinhDl> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert Report Kpi Total Dl");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO rp_kpi_toantrinh_dl \n");
            sql.append(" (url_id, app_code, kpi_server, kpi_client, total, update_time, insert_time) \n");
            sql.append(" VALUES(?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            for (RpKpiToantrinhDl obj : lst) {
                if (obj.getUrlId() != null) {
                    pstmt.setLong(1, obj.getUrlId());
                } else {
                    pstmt.setNull(1, java.sql.Types.NUMERIC);
                }
                if (obj.getAppCode() != null) {
                    pstmt.setString(2, obj.getAppCode());
                } else {
                    pstmt.setNull(2, java.sql.Types.VARCHAR);
                }
                if (obj.getKpiServer() != null) {
                    pstmt.setDouble(3, obj.getKpiServer());
                } else {
                    pstmt.setNull(3, java.sql.Types.NUMERIC);
                }
                if (obj.getKpiClient() != null) {
                    pstmt.setDouble(4, obj.getKpiClient());
                } else {
                    pstmt.setNull(4, java.sql.Types.NUMERIC);
                }
                if (obj.getTotal() != null) {
                    pstmt.setLong(5, obj.getTotal());
                } else {
                    pstmt.setNull(5, java.sql.Types.NUMERIC);
                }
                if (obj.getUpdateTime() != null) {
                    pstmt.setDate(6, new java.sql.Date(obj.getUpdateTime().getTime()));
                } else {
                    pstmt.setNull(6, java.sql.Types.DATE);
                }
                if (obj.getInsertTime() != null) {
                    pstmt.setTimestamp(7, new java.sql.Timestamp(obj.getInsertTime().getTime()));
                } else {
                    pstmt.setNull(7, java.sql.Types.TIMESTAMP);
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

    public synchronized void deleteRpKpiToantrinhDl(String updateTime) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
//            String partition = DateTimeUtils.format(DateTimeUtils.parse(updateTime, "dd/MM/yyyy"),"yyyyMMdd");
            logger.info("Detele Report Kpi Total Dl");

            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE rp_kpi_toantrinh_dl PARTITION FOR (to_date('").append(updateTime).append("','dd/MM/yyyy'))");
//            sql.append(" DELETE rp_kpi_toantrinh_dl PARTITION(DATA").append(partition).append(")");

            pstmt = con.prepareStatement(sql.toString());
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
