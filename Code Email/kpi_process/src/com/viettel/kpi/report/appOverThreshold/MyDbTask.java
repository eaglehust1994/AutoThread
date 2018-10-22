/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.report.appOverThreshold;

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

    public List<RpAppOverThreshold> getListFirst(Date updateTime) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<RpAppOverThreshold> lst = new ArrayList<RpAppOverThreshold>();
        try {
            logger.info("Get List App Over Threshold First");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT   DISTINCT a.app_id,a.app_name , b.day_over_threshold\n");
            sql.append("  FROM   cata_app a \n");
            sql.append("  LEFT JOIN \n");
            sql.append("  (SELECT  DISTINCT a.app_id , TO_CHAR (b.update_time, 'dd/MM') day_over_threshold\n");
            sql.append("  FROM cata_url a\n");
            sql.append("  LEFT JOIN kpi_data_raw_dl PARTITION (DATA").append(partition).append(") b\n");
            sql.append("  ON a.url_id = b.url_id\n");
            sql.append("  WHERE  b.is_ok =0) b\n");
            sql.append("  ON a.app_id = b.app_id");

            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RpAppOverThreshold obj = new RpAppOverThreshold();
                obj.setAppId(DBUtil.getLong(rs.getObject("app_id")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setDayOverThreshold(DBUtil.getString(rs.getObject("day_over_threshold")));
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

    public List<RpAppOverThreshold> getList(Date updateTime) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<RpAppOverThreshold> lst = new ArrayList<RpAppOverThreshold>();
        try {
            logger.info("Get List App Over Threshold");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT a.app_id,a.app_name,\n");
            sql.append("CASE WHEN b.day_over_threshold IS NULL THEN a.day_over_threshold\n");
            sql.append("WHEN a.day_over_threshold IS NULL THEN b.day_over_threshold\n");
            sql.append("ELSE  b.day_over_threshold || ',' || a.day_over_threshold \n");
            sql.append(" END day_over_threshold\n");
            sql.append("FROM \n");
            sql.append("(SELECT   DISTINCT a.app_id,a.app_name , b.day_over_threshold\n");
            sql.append("  FROM   cata_app a \n");
            sql.append("  LEFT JOIN \n");
            sql.append("  (SELECT  DISTINCT a.app_id , TO_CHAR (b.update_time, 'dd/MM') day_over_threshold\n");
            sql.append("  FROM cata_url a\n");
            sql.append("  LEFT JOIN kpi_data_raw_dl PARTITION (DATA").append(partition).append(") b\n");
            sql.append("  ON a.url_id = b.url_id\n");
            sql.append("  WHERE  b.is_ok =0) b\n");
            sql.append("  ON a.app_id = b.app_id) a\n");
            sql.append("  LEFT JOIN \n");
            sql.append("  (SELECT a.app_id,a.app_name,a.day_over_threshold FROM rp_app_over_threshold a\n");
            sql.append("  WHERE a.update_time = ?) b\n");
            sql.append("  ON a.app_id = b.app_id");

            pstmt = con.prepareStatement(sql.toString());
            pstmt.setDate(1, new java.sql.Date(updateTime.getTime() - 24 * 60 * 60 * 1000));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                RpAppOverThreshold obj = new RpAppOverThreshold();
                obj.setAppId(DBUtil.getLong(rs.getObject("app_id")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setDayOverThreshold(DBUtil.getString(rs.getObject("day_over_threshold")));
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

    public synchronized void insertOrUpdateAppOverThreshold(RpAppOverThreshold obj) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Insert Or Update App Over Threshold");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" MERGE INTO rp_app_over_threshold USING dual ON ( month = ? AND year = ? AND app_id = ? )\n");
            sql.append(" WHEN MATCHED THEN UPDATE SET day_over_threshold = day_over_threshold || ',' || ?\n");
            sql.append(" WHEN NOT MATCHED THEN INSERT \n");
            sql.append("     (month, app_name, day_over_threshold, from_date,\n");
            sql.append("       to_date, update_time, insert_time, year, app_id)\n");
            sql.append("  VALUES (?,?,?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, obj.getMonth());
            pstmt.setInt(2, obj.getYear());
            if (obj.getAppId() != null) {
                pstmt.setLong(3, obj.getAppId());
            } else {
                pstmt.setNull(3, java.sql.Types.NUMERIC);
            }
            if (obj.getDayOverThreshold() != null) {
                pstmt.setString(4, obj.getDayOverThreshold());
            } else {
                pstmt.setNull(4, java.sql.Types.TIMESTAMP);
            }
            pstmt.setInt(5, obj.getMonth());
            if (obj.getAppName() != null) {
                pstmt.setString(6, obj.getAppName());
            } else {
                pstmt.setNull(6, java.sql.Types.VARCHAR);
            }
            if (obj.getDayOverThreshold() != null) {
                pstmt.setString(7, obj.getDayOverThreshold());
            } else {
                pstmt.setNull(7, java.sql.Types.VARCHAR);
            }
            if (obj.getFromDate() != null) {
                pstmt.setDate(7, new java.sql.Date(obj.getFromDate().getTime()));
            } else {
                pstmt.setNull(7, java.sql.Types.DATE);
            }
            if (obj.getToDate() != null) {
                pstmt.setDate(8, new java.sql.Date(obj.getToDate().getTime()));
            } else {
                pstmt.setNull(8, java.sql.Types.DATE);
            }
            if (obj.getUpdateTime() != null) {
                pstmt.setDate(9, new java.sql.Date(obj.getUpdateTime().getTime()));
            } else {
                pstmt.setNull(9, java.sql.Types.DATE);
            }

            pstmt.setTimestamp(9, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setInt(10, obj.getYear());
            if (obj.getAppId() != null) {
                pstmt.setLong(11, obj.getAppId());
            } else {
                pstmt.setNull(11, java.sql.Types.NUMERIC);
            }

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

    public synchronized void updateAppOverThreshold(List<RpAppOverThreshold> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert Or Update App Over Threshold");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE rp_app_over_threshold\n");
            sql.append(" SET day_over_threshold = day_over_threshold || ',' || ?\n");
            sql.append("  WHERE month = ? AND year = ? AND app_id = ? \n");
            pstmt = con.prepareStatement(sql.toString());
            for (RpAppOverThreshold obj : lst) {
                if (obj.getDayOverThreshold() != null) {
                    pstmt.setString(1, obj.getDayOverThreshold());
                } else {
                    pstmt.setNull(1, java.sql.Types.VARCHAR);
                }
                pstmt.setInt(2, obj.getMonth());
                pstmt.setInt(3, obj.getYear());
                if (obj.getAppId() != null) {
                    pstmt.setLong(4, obj.getAppId());
                } else {
                    pstmt.setNull(4, java.sql.Types.NUMERIC);
                }
                i++;
                pstmt.addBatch();
                if (i >= 100) {
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

    public synchronized void insertAppOverThreshold(List<RpAppOverThreshold> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert App Over Threshold");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO rp_app_over_threshold\n");
            sql.append("     (month, app_name, day_over_threshold, from_date,\n");
            sql.append("       to_date, update_time, insert_time, year, app_id)\n");
            sql.append("  VALUES (?,?,?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            for (RpAppOverThreshold obj : lst) {

                pstmt.setInt(1, obj.getMonth());
                if (obj.getAppName() != null) {
                    pstmt.setString(2, obj.getAppName());
                } else {
                    pstmt.setNull(2, java.sql.Types.VARCHAR);
                }
                if (obj.getDayOverThreshold() != null) {
                    pstmt.setString(3, obj.getDayOverThreshold());
                } else {
                    pstmt.setNull(3, java.sql.Types.VARCHAR);
                }
                if (obj.getFromDate() != null) {
                    pstmt.setDate(4, new java.sql.Date(obj.getFromDate().getTime()));
                } else {
                    pstmt.setNull(4, java.sql.Types.DATE);
                }
                if (obj.getToDate() != null) {
                    pstmt.setDate(5, new java.sql.Date(obj.getToDate().getTime()));
                } else {
                    pstmt.setNull(5, java.sql.Types.DATE);
                }
                if (obj.getUpdateTime() != null) {
                    pstmt.setDate(6, new java.sql.Date(obj.getUpdateTime().getTime()));
                } else {
                    pstmt.setNull(6, java.sql.Types.DATE);
                }

                pstmt.setTimestamp(7, new java.sql.Timestamp(new Date().getTime()));
                pstmt.setInt(8, obj.getYear());
                if (obj.getAppId() != null) {
                    pstmt.setLong(9, obj.getAppId());
                } else {
                    pstmt.setNull(9, java.sql.Types.NUMERIC);
                }

                i++;
                pstmt.addBatch();
                if (i >= 100) {
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
//    public synchronized void deleteKpiLogServerDl(Date updateTime, RpAppOverThreshold cataFuncLogServer) {
//        Connection con = null;
//        PreparedStatement pstmt = null;
//        try {
//            logger.info("Detele KPI Log Server Dl");
//            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
//            con = getConnectionPool();
//            StringBuilder sql = new StringBuilder();
//            sql.append(" DELETE kpi_log_server_dl PARTITION(DATA").append(partition).append(") a");
//            sql.append(" WHERE a.app_code=? AND a.func_name=?");
//            pstmt = con.prepareStatement(sql.toString());
//            pstmt.setString(1, cataFuncLogServer.getAppCode());
//            pstmt.setString(2, cataFuncLogServer.getFuncName());
//            pstmt.executeQuery();
//            commit(con);
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//        } finally {
//            if (pstmt != null) {
//                try {
//                    pstmt.close();
//                } catch (Exception ex) {
//                    logger.error(ex.getMessage(), ex);
//                }
//            }
//            try {
//                close(con);
//            } catch (Exception ex) {
//                logger.error(ex.getMessage(), ex);
//            }
//        }
//    }
}
