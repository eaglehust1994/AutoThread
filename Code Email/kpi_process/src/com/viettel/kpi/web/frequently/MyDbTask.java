/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.frequently;

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

    public List<KpiDataWebFrequently> getNumClient(Date updateTime) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiDataWebFrequently> lst = new ArrayList<KpiDataWebFrequently>();
        KpiDataWebFrequently obj = null;
        try {
            logger.info("Get Num Client");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT e.app_id,e.app_name,d.func_group_id,d.func_group_name,\n");
            sql.append(" a.url_id,c.func_id,c.func_name, count(DISTINCT a.ip_lan) no_client\n");
            sql.append("  FROM ");
            sql.append("(SELECT * FROM kpi_data_raw PARTITION(DATA").append(partition).append("))a");
            sql.append("  LEFT JOIN cata_url b ON a.url_id = b.url_id\n");
            sql.append("  LEFT JOIN cata_func c ON c.func_id = b.func_id\n");
            sql.append("  LEFT JOIN cata_func_group d ON d.func_group_id = b.func_group_id\n");
            sql.append("  LEFT JOIN cata_app e ON e.app_id = b.app_id \n");
//            sql.append("  WHERE a.update_time=? \n");
            sql.append("  GROUP BY e.app_id,e.app_name,d.func_group_id,d.func_group_name,\n");
            sql.append("  a.url_id,c.func_id,c.func_name ");
            pstmt = con.prepareStatement(sql.toString());
//            pstmt.setTimestamp(1, new Timestamp(updateTime.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiDataWebFrequently();
                obj.setAppId(DBUtil.getLong(rs.getObject("app_id")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setFuncGroupId(rs.getLong("func_group_id"));
                obj.setFuncGroupName(DBUtil.getString(rs.getObject("func_group_name")));
                obj.setFuncId(DBUtil.getLong(rs.getObject("func_id")));
                obj.setFuncName(DBUtil.getString(rs.getObject("func_name")));
                obj.setUpdateTime(updateTime);
                obj.setUrlId(DBUtil.getLong(rs.getObject("url_id")));
                obj.setNoClient(DBUtil.getLong(rs.getObject("no_client")));
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

    public synchronized void insertKpiFrequently(List<KpiDataWebFrequently> lst){
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert KPI Frequently");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_data_web_frequently \n");
            sql.append(" (update_time,app_id,app_name,func_group_id,\n");
            sql.append(" func_group_name,func_id,func_name,url_id,no_client) \n");
            sql.append(" VALUES(?,?,?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            for (KpiDataWebFrequently obj : lst) {
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
                if (obj.getFuncGroupId() != null) {
                    pstmt.setLong(4, obj.getFuncGroupId());
                } else {
                    pstmt.setNull(4, java.sql.Types.NUMERIC);
                }
                if (obj.getFuncGroupName() != null) {
                    pstmt.setString(5, obj.getFuncGroupName());
                } else {
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                }
                if (obj.getFuncId() != null) {
                    pstmt.setLong(6, obj.getFuncId());
                } else {
                    pstmt.setNull(6, java.sql.Types.NUMERIC);
                }
                if (obj.getFuncName() != null) {
                    pstmt.setString(7, obj.getFuncName());
                } else {
                    pstmt.setNull(7, java.sql.Types.VARCHAR);
                }
                if (obj.getFuncId() != null) {
                    pstmt.setLong(8, obj.getUrlId());
                } else {
                    pstmt.setNull(8, java.sql.Types.NUMERIC);
                }
                if (obj.getNoClient() != null) {
                    pstmt.setLong(9, obj.getNoClient());
                } else {
                    pstmt.setNull(9, java.sql.Types.NUMERIC);
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
    
    public synchronized void deleteKpiFrequently(Date updateTime){
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele KPI Frequently");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_data_web_frequently PARTITION(DATA").append(partition).append(")");
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
