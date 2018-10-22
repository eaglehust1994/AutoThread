/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.lostData.rate;

import com.viettel.kpi.common.utils.DBUtil;
import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.kpi.common.utils.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyDbTask extends DbTask {

    private static final Logger log = Logger.getLogger(MyDbTask.class);

    public Long getCountLogClient(Date updateTime) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Long countLogServer = null;
        try {
            log.info("Get Count Log Server");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            log.info("con: " + con);
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT count(distinct id) countLogServer FROM kpi_data_raw PARTITION(DATA").append(partition).append(") ")
                    .append("WHERE url_id IN (SELECT url_id FROM cata_url WHERE app_id = 20)");

            pstmt = con.prepareStatement(sql.toString());
            log.info("pstmt: " + pstmt);
            rs = pstmt.executeQuery();
            log.info("rs: " + rs);
            while (rs.next()) {
                countLogServer = DBUtil.getLong(rs.getObject("countLogServer"));
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
            try {
                close(con);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return countLogServer;
    }

    public synchronized void deleteLostDataRate(Date updateTime) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            log.info("Detele LOST DATA RATE Dl");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE rp_kpi_lost_data_rate_dl WHERE update_time = ?");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setDate(1, new java.sql.Date(updateTime.getTime()));
            pstmt.executeQuery();
            commit(con);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
            try {
                close(con);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

    public synchronized void insertLostDataRate(Date updateTime,
            Long countLogServer, Long countLogClient) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            log.info("Insert LOST DATA RATE Dl");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO rp_kpi_lost_data_rate_dl \n");
            sql.append(" (app_code,COUNT_DATA_CLIENT,\n");
            sql.append(" COUNT_DATA_SERVER,LOST_DATA_RATE,UPDATE_TIME) \n");
            sql.append(" VALUES(?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, "CC");
            pstmt.setLong(2, countLogClient);
            pstmt.setLong(3, countLogServer);
            double rate = Math.round(100d * 100 * (countLogServer - countLogClient) / countLogServer) / 100d;
            log.info("rate: " + rate);
            pstmt.setDouble(4, rate);
            pstmt.setDate(5, new java.sql.Date(updateTime.getTime()));

            pstmt.executeQuery();
            commit(con);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }
            try {
                close(con);
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }

}
