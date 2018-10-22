/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.ipDeploy;

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

    public List<KpiDataWebIpDeploy> getIpDeploy(Date updateTime) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiDataWebIpDeploy> lst = new ArrayList<KpiDataWebIpDeploy>();
        KpiDataWebIpDeploy obj = null;
        try {
            logger.info("Get IP Deploy");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT DISTINCT e.app_id,e.app_name,a.ip_lan\n");
            sql.append("  FROM ");
            sql.append("(SELECT * FROM kpi_data_raw PARTITION(DATA").append(partition).append("))a");
            sql.append("  LEFT JOIN cata_url b ON a.url_id = b.url_id\n");
            sql.append("  LEFT JOIN cata_func c ON c.func_id = b.func_id\n");
            sql.append("  LEFT JOIN cata_func_group d ON d.func_group_id = b.func_group_id\n");
            sql.append("  LEFT JOIN cata_app e ON e.app_id = b.app_id \n");
//            sql.append("  WHERE a.update_time=? \n");
            pstmt = con.prepareStatement(sql.toString());
//            pstmt.setTimestamp(1, new Timestamp(updateTime.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiDataWebIpDeploy();
                obj.setAppId(DBUtil.getLong(rs.getObject("app_id")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setIpLan(DBUtil.getString(rs.getObject("ip_lan")));
                obj.setUpdateTime(updateTime);
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

    public synchronized void insertIpDeploy(List<KpiDataWebIpDeploy> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert IP Deploy");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_data_web_ip_deploy \n");
            sql.append(" (update_time,app_id,app_name,ip_lan)\n");
            sql.append(" VALUES(?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            for (KpiDataWebIpDeploy obj : lst) {
                if (obj.getUpdateTime() != null) {
                    pstmt.setTimestamp(1, new java.sql.Timestamp(obj.getUpdateTime().getTime()));
                } else {
                    pstmt.setNull(1, java.sql.Types.TIMESTAMP);
                }
                if (obj.getAppId() != null) {
                    pstmt.setLong(2, obj.getAppId());
                } else {
                    pstmt.setNull(2, java.sql.Types.NUMERIC);
                }
                if (obj.getAppName() != null) {
                    pstmt.setString(3, obj.getAppName());
                } else {
                    pstmt.setNull(3, java.sql.Types.VARCHAR);
                }
                if (obj.getIpLan() != null) {
                    pstmt.setString(4, obj.getIpLan());
                } else {
                    pstmt.setNull(4, java.sql.Types.VARCHAR);
                }
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

    public synchronized void deleteIpDeploy(Date updateTime) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele IP Deploy");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_data_web_ip_deploy PARTITION(DATA").append(partition).append(")");
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
