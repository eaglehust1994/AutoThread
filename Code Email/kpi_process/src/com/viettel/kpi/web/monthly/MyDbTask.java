/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.monthly;

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

    public List<KpiWebCountDl> getKpiWebCount(Date updateTime) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiWebCountDl> lst = new ArrayList<KpiWebCountDl>();
        KpiWebCountDl obj = null;
        try {
            logger.info("Get Kpi Count From Raw");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT  c.app_id,c.app_name,a.url_id,b.url_name,a.duration,a.COUNT,a.total_succ \n");
            sql.append("FROM \n");
            sql.append("(SELECT url_id , round(response_time/1000,2) duration,COUNT(*) count,SUM(IS_SUCCESS) total_succ  \n");
            sql.append("FROM kpi_data_raw PARTITION(DATA").append(partition).append(") ");
            sql.append("WHERE response_time > 0 \n");
            sql.append("GROUP BY url_id,round(response_time/1000,2)) a \n");
            sql.append("LEFT JOIN cata_url b ON a.url_id = b.url_id \n");
            sql.append("LEFT JOIN cata_app c ON b.app_id = c.app_id ");

            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
//            pstmt.setTimestamp(1, new Timestamp(updateTime.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiWebCountDl();
                obj.setAppId(DBUtil.getLong(rs.getObject("app_id")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setUrlId(DBUtil.getLong(rs.getObject("url_id")));
                obj.setUrlName(DBUtil.getString(rs.getObject("url_name")));
                obj.setDuration(DBUtil.getDouble(rs.getObject("duration")));
                obj.setCount(DBUtil.getLong(rs.getObject("count")));
                obj.setTotalSucc(DBUtil.getLong(rs.getObject("total_succ")));
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

    public synchronized void insertKpiCountDl(List<KpiWebCountDl> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert KPI Count");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_web_count_dl \n");
            sql.append(" (app_id,app_name,url_id,url_name,\n");
            sql.append(" duration,count,update_time,total_succ) \n");
            sql.append(" VALUES(?,?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            for (KpiWebCountDl obj : lst) {
                if (obj.getAppId() != null) {
                    pstmt.setLong(1, obj.getAppId());
                } else {
                    pstmt.setNull(1, java.sql.Types.NUMERIC);
                }
                if (obj.getAppName() != null) {
                    pstmt.setString(2, obj.getAppName());
                } else {
                    pstmt.setNull(2, java.sql.Types.VARCHAR);
                }
                if (obj.getUrlId() != null) {
                    pstmt.setLong(3, obj.getUrlId());
                } else {
                    pstmt.setNull(3, java.sql.Types.NUMERIC);
                }
                if (obj.getUrlName() != null) {
                    pstmt.setString(4, obj.getUrlName());
                } else {
                    pstmt.setNull(4, java.sql.Types.VARCHAR);
                }
                if (obj.getDuration() != null) {
                    pstmt.setDouble(5, obj.getDuration());
                } else {
                    pstmt.setNull(5, java.sql.Types.NUMERIC);
                }
                if (obj.getCount() != null) {
                    pstmt.setLong(6, obj.getCount());
                } else {
                    pstmt.setNull(6, java.sql.Types.NUMERIC);
                }
                if (obj.getUpdateTime() != null) {
                    pstmt.setDate(7, new java.sql.Date(obj.getUpdateTime().getTime()));
                } else {
                    pstmt.setNull(7, java.sql.Types.DATE);
                }
                if (obj.getTotalSucc() != null) {
                    pstmt.setLong(8, obj.getTotalSucc());
                } else {
                    pstmt.setNull(8, java.sql.Types.NUMERIC);
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

    public synchronized void deleteKpiCountDl(Date updateTime) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele KPI Count");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_web_count_dl PARTITION(DATA").append(partition).append(")");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.executeQuery();
            logger.info("pstmt " + pstmt);
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

    public List<CataUrl> getListCataUrl() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<CataUrl> lst = new ArrayList<CataUrl>();
        try {
            logger.info("Get List App");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT a.url_id, a.url_name, a.url_pattern, a.status,\n");
            sql.append("       a.response_error_pattern, a.timeout, a.target, a.app_id,\n");
            sql.append("       a.func_group_id, a.func_id, a.url_param, a.post_data\n");
            sql.append("  FROM cata_url a");
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CataUrl obj = new CataUrl();
                obj.setAppId(DBUtil.getLong(rs.getObject("app_id")));
                obj.setUrlId(DBUtil.getLong(rs.getObject("url_id")));
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

    public List<KpiWebCountDl> getKpiWebCount(Date startDate, Date endDate, CataUrl cataUrl) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiWebCountDl> lst = new ArrayList<KpiWebCountDl>();
        KpiWebCountDl obj = null;
        try {
            logger.info("Get Kpi Count");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT a.app_id, a.app_name, a.url_id, a.url_name, a.duration, a.count,a.total_succ,\n");
            sql.append(" a.update_time\n");
            sql.append("  FROM kpi_web_count_dl a");
            sql.append("  WHERE a.app_id = ? \n");
            sql.append("  AND a.url_id = ? \n");
            sql.append("  AND a.update_time >= ? \n");
            sql.append("  AND a.update_time < ? \n");
            sql.append("  ORDER BY a.duration");
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            pstmt.setLong(1, cataUrl.getAppId());
            pstmt.setLong(2, cataUrl.getUrlId());
            pstmt.setDate(3, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(4, new java.sql.Date(endDate.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiWebCountDl();
                obj.setAppId(DBUtil.getLong(rs.getObject("app_id")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setUrlId(DBUtil.getLong(rs.getObject("url_id")));
                obj.setUrlName(DBUtil.getString(rs.getObject("url_name")));
                obj.setDuration(DBUtil.getDouble(rs.getObject("duration")));
                obj.setTotalSucc(DBUtil.getLong(rs.getObject("total_succ")));
                obj.setCount(DBUtil.getLong(rs.getObject("count")));
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

    public List<Long> getTotal(Date startDate, Date endDate, CataUrl cataUrl) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Long> list = new ArrayList<Long>();
//        Long total = 0l;
        try {
            logger.info("Get Total");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT sum(a.count) total,sum(a.total_succ) total_succ\n");
            sql.append("  FROM kpi_web_count_dl a");
            sql.append("  WHERE a.app_id = ? \n");
            sql.append("  AND a.url_id = ? \n");
            sql.append("  AND a.update_time >= ? \n");
            sql.append("  AND a.update_time < ? \n");
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            pstmt.setLong(1, cataUrl.getAppId());
            pstmt.setLong(2, cataUrl.getUrlId());
            pstmt.setDate(3, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(4, new java.sql.Date(endDate.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Long total = DBUtil.getLong(rs.getLong("total"));
                list.add(total);
                Long totalSucc = DBUtil.getLong(rs.getLong("total_succ"));
                list.add(totalSucc);
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
        return list;
    }

    public KpiWebMonthly getKpiWebMonthly(List<KpiWebCountDl> lst, Double rank95,
            Long total,Long totalSucc, Integer month, Integer year) {
        KpiWebMonthly obj = new KpiWebMonthly();
        long temp = 0l;
        try {
            logger.info("Get Kpi Monthly");
            for (KpiWebCountDl countDl : lst) {
                temp = temp + countDl.getCount();
                if (temp > rank95) {
                    obj.setAppId(countDl.getAppId());
                    obj.setAppName(countDl.getAppName());
                    obj.setUrlId(countDl.getUrlId());
                    obj.setUrlName(countDl.getUrlName());
                    obj.setKpi(countDl.getDuration());
                    obj.setMonth(month);
                    obj.setYear(year);
                    obj.setTotal(total);
                    obj.setTotalSucc(totalSucc);
                    obj.setInsertTime(new Date());
                    break;
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return obj;
    }

    public synchronized void insertKpiWebMonthly(KpiWebMonthly obj) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Insert KPI Monthly");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_web_monthly \n");
            sql.append(" (app_id,app_name,url_id,url_name,\n");
            sql.append(" kpi,month,total,insert_time,year,total_succ) \n");
            sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            if (obj.getAppId() != null) {
                pstmt.setLong(1, obj.getAppId());
            } else {
                pstmt.setNull(1, java.sql.Types.NUMERIC);
            }
            if (obj.getAppName() != null) {
                pstmt.setString(2, obj.getAppName());
            } else {
                pstmt.setNull(2, java.sql.Types.VARCHAR);
            }
            if (obj.getUrlId() != null) {
                pstmt.setLong(3, obj.getUrlId());
            } else {
                pstmt.setNull(3, java.sql.Types.NUMERIC);
            }
            if (obj.getUrlName() != null) {
                pstmt.setString(4, obj.getUrlName());
            } else {
                pstmt.setNull(4, java.sql.Types.VARCHAR);
            }
            if (obj.getKpi() != null) {
                pstmt.setDouble(5, obj.getKpi());
            } else {
                pstmt.setNull(5, java.sql.Types.NUMERIC);
            }
            if (obj.getMonth() != null) {
                pstmt.setLong(6, obj.getMonth());
            } else {
                pstmt.setNull(6, java.sql.Types.NUMERIC);
            }
            if (obj.getTotal() != null) {
                pstmt.setLong(7, obj.getTotal());
            } else {
                pstmt.setNull(7, java.sql.Types.NUMERIC);
            }
            pstmt.setTimestamp(8, new java.sql.Timestamp(new Date().getTime()));
            if (obj.getYear() != null) {
                pstmt.setLong(9, obj.getYear());
            } else {
                pstmt.setNull(9, java.sql.Types.NUMERIC);
            }
            if (obj.getTotalSucc()!= null) {
                pstmt.setLong(10, obj.getTotalSucc());
            } else {
                pstmt.setNull(10, java.sql.Types.NUMERIC);
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

    public synchronized void deleteKpiWebMonthly(Integer month,Integer year) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele KPI Monthly");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_web_monthly a");
            sql.append(" WHERE a.month = ? AND year = ? ");
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
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
