/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.logServer.dl;

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

    public List<CataFuncLogServer> getListFuncName() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<CataFuncLogServer> lst = new ArrayList<CataFuncLogServer>();
        try {
            logger.info("Get List Function Name");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT a.app_code,a.func_name\n");
            sql.append("  FROM cata_func_log_server a");
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CataFuncLogServer obj = new CataFuncLogServer();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setFuncName(DBUtil.getString(rs.getObject("func_name")));
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

    public List<KpiLogServerRaw> getKpiLogServerRaw(Date updateTime, CataFuncLogServer cataFuncLogServer) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiLogServerRaw> lst = new ArrayList<KpiLogServerRaw>();
        KpiLogServerRaw obj = null;
        try {
            logger.info("Get Kpi Log Server Raw");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT a.app_code, a.func_name, a.insert_time, a.update_time,\n");
            sql.append(" a.duration, a.log_server_id, a.query_id, a.count\n");
            sql.append("  FROM kpi_log_server_raw PARTITION(DATA").append(partition).append(")a");
            sql.append("  WHERE trim(a.app_code)=? AND trim(a.func_name) = ? \n");
            sql.append("  ORDER BY a.duration");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, cataFuncLogServer.getAppCode());
            pstmt.setString(2, cataFuncLogServer.getFuncName());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiLogServerRaw();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setFuncName(DBUtil.getString(rs.getObject("func_name")));
                obj.setInsertTime(rs.getDate("insert_time"));
                obj.setUpdateTime(rs.getDate("update_time"));
                obj.setDuration(DBUtil.getDouble(rs.getObject("duration")));
                obj.setLogServerId(DBUtil.getString(rs.getObject("log_server_id")));
                obj.setQueryId(DBUtil.getLong(rs.getObject("query_id")));
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

    public Long getTotal(Date updateTime, CataFuncLogServer cataFuncLogServer) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Long total = 0l;
        try {
            logger.info("Get Total");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT sum(a.count) sum\n");
            sql.append("  FROM kpi_log_server_raw PARTITION(DATA").append(partition).append(")a");
            sql.append("  WHERE a.app_code=? AND a.func_name=? \n");
//            sql.append("  ORDER BY a.duration");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, cataFuncLogServer.getAppCode());
            pstmt.setString(2, cataFuncLogServer.getFuncName());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                total = DBUtil.getLong(rs.getLong("sum"));
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
        return total;
    }

    public KpiLogServerDl getKpiLogServerDl(List<KpiLogServerRaw> lst, Double rank95,Long total) {
        KpiLogServerDl obj = new KpiLogServerDl();
        long temp = 0l;
        try {
            logger.info("Get Log Server Daily");
            for (KpiLogServerRaw raw : lst) {
                temp = temp + raw.getCount();
                if (temp > rank95) {
                    obj.setAppCode(raw.getAppCode());
                    obj.setFuncName(raw.getFuncName());
                    obj.setInsertTime(new Date());
                    obj.setKpi(raw.getDuration());
                    obj.setLogServerId(raw.getLogServerId());
                    obj.setQueryId(raw.getQueryId());
                    obj.setUpdateTime(raw.getUpdateTime());
                    //dungvv8_20160119
                    obj.setTotal(total);
                    //dungvv8_20160119
                    break;
                }
            }
        } catch (Exception e) {
        }
        return obj;
    }

    public synchronized void insertKpiLogServerDl(KpiLogServerDl obj) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Insert KPI LogServer Daily");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_log_server_dl \n");
            sql.append(" (app_code,func_name,insert_time,update_time,\n");
            sql.append(" kpi,log_server_id,query_id,total) \n");
            sql.append(" VALUES(?,?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            if (obj.getAppCode() != null) {
                pstmt.setString(1, obj.getAppCode());
            } else {
                pstmt.setNull(1, java.sql.Types.VARCHAR);
            }
            if (obj.getFuncName() != null) {
                pstmt.setString(2, obj.getFuncName());
            } else {
                pstmt.setNull(2, java.sql.Types.VARCHAR);
            }
            if (obj.getInsertTime() != null) {
                pstmt.setTimestamp(3, new java.sql.Timestamp(obj.getInsertTime().getTime()));
            } else {
                pstmt.setNull(3, java.sql.Types.TIMESTAMP);
            }
            if (obj.getUpdateTime() != null) {
                pstmt.setTimestamp(4, new java.sql.Timestamp(obj.getUpdateTime().getTime()));
            } else {
                pstmt.setNull(4, java.sql.Types.TIMESTAMP);
            }
            if (obj.getKpi() != null) {
                pstmt.setDouble(5, obj.getKpi());
            } else {
                pstmt.setNull(5, java.sql.Types.NUMERIC);
            }
            if (obj.getLogServerId() != null) {
                pstmt.setString(6, obj.getLogServerId());
            } else {
                pstmt.setNull(6, java.sql.Types.VARCHAR);
            }
            if (obj.getQueryId() != null) {
                pstmt.setLong(7, obj.getQueryId());
            } else {
                pstmt.setNull(7, java.sql.Types.NUMERIC);
            }
            //dungvv8_20160119
            if (obj.getTotal()!= null) {
                pstmt.setLong(8, obj.getTotal());
            } else {
                pstmt.setNull(8, java.sql.Types.NUMERIC);
            }
            //dungvv8_20160119

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

    public synchronized void deleteKpiLogServerDl(Date updateTime, CataFuncLogServer cataFuncLogServer) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele KPI Log Server Dl");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_log_server_dl PARTITION(DATA").append(partition).append(") a");
            sql.append(" WHERE a.app_code=? AND a.func_name=?");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, cataFuncLogServer.getAppCode());
            pstmt.setString(2, cataFuncLogServer.getFuncName());
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

    public synchronized void deleteKpiLogServerDl(Date updateTime) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele KPI Log Server Dl");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_log_server_dl PARTITION(DATA").append(partition).append(") a");
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

    public synchronized void deleteKpiMobileMonthly(Integer month) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele KPI Log Server Monthly");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_log_server_monthly a");
            sql.append(" WHERE a.month = ? ");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setInt(1, month);
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

    public List<KpiLogServerRaw> getKpiLogServerCountByMonthly(Date startDate, Date endDate, CataFuncLogServer cataFuncLogServer) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiLogServerRaw> lst = new ArrayList<KpiLogServerRaw>();
        KpiLogServerRaw obj = null;
        try {
            logger.info("Get Kpi Log Server Count");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT a.app_code, a.func_name, a.duration, a.count,\n");
            sql.append(" a.update_time\n");
            sql.append("  FROM kpi_log_server_raw a");
            sql.append("  WHERE a.app_code = ? \n");
            sql.append("  AND a.func_name = ? \n");
            sql.append("  AND a.update_time >= ? \n");
            sql.append("  AND a.update_time < ? \n");
            sql.append("  ORDER BY a.duration");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, cataFuncLogServer.getAppCode());
            pstmt.setString(2, cataFuncLogServer.getFuncName());
            pstmt.setDate(3, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(4, new java.sql.Date(endDate.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiLogServerRaw();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setFuncName(DBUtil.getString(rs.getObject("func_name")));
                obj.setDuration(DBUtil.getDouble(rs.getObject("duration")));
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

    public KpiLogServerMonthly getKpiLogServerOtherByMonthly(Date startDate, Date endDate,
            CataFuncLogServer cataFuncLogServer, int month, int year) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
//        List<KpiLogServerRaw> lst = new ArrayList<KpiLogServerRaw>();
        KpiLogServerMonthly obj = null;
        try {
            logger.info("Get Kpi Log Server Count");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT a.app_code, a.func_name,\n");
            sql.append(" sum(succ_rate*total_trans)/sum(total_trans) kpi,\n");
            sql.append(" sum(total_trans) total\n");
            sql.append("  FROM kpi_log_server_other_dl a");
            sql.append("  WHERE a.app_code = ? \n");
            sql.append("  AND a.func_name = ? \n");
            sql.append("  AND a.update_time >= ? \n");
            sql.append("  AND a.update_time < ? \n");
            sql.append("  GROUP BY a.app_code, a.func_name");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, cataFuncLogServer.getAppCode());
            pstmt.setString(2, cataFuncLogServer.getFuncName());
            pstmt.setDate(3, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(4, new java.sql.Date(endDate.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiLogServerMonthly();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setFuncName(DBUtil.getString(rs.getObject("func_name")));
                obj.setKpi(DBUtil.getDouble(rs.getObject("kpi")));
                obj.setTotal(DBUtil.getLong(rs.getObject("total")));
                obj.setType("succ_rate");
                obj.setMonth(month);
                obj.setYear(year);
//                lst.add(obj);
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
        return obj;
    }

    public Long getTotalMonthly(Date startDate, Date endDate, CataFuncLogServer cataFuncLogServer) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Long total = 0l;
        try {
            logger.info("Get Total");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT sum(a.count) sum\n");
            sql.append("  FROM kpi_log_server_raw a");
            sql.append("  WHERE a.app_code = ? \n");
            sql.append("  AND a.func_name = ? \n");
            sql.append("  AND a.update_time >= ? \n");
            sql.append("  AND a.update_time < ? \n");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, cataFuncLogServer.getAppCode());
            pstmt.setString(2, cataFuncLogServer.getFuncName());
            pstmt.setDate(3, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(4, new java.sql.Date(endDate.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                total = DBUtil.getLong(rs.getLong("sum"));
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
        return total;
    }

    public KpiLogServerMonthly getKpiLogServerMonthly(List<KpiLogServerRaw> lst, Double rank95,
            Long total, Integer month, Integer year) {
        KpiLogServerMonthly obj = new KpiLogServerMonthly();
        long temp = 0l;
        try {
            logger.info("Get Kpi Log Server Monthly");
            for (KpiLogServerRaw countDl : lst) {
                temp = temp + countDl.getCount();
                if (temp > rank95) {
                    obj.setAppCode(countDl.getAppCode());
                    obj.setFuncName(countDl.getFuncName());
                    obj.setType("duration");
                    obj.setKpi(countDl.getDuration());
                    obj.setMonth(month);
                    obj.setYear(year);
                    obj.setTotal(total);
                    obj.setInsertTime(new Date());
                    break;
                }
            }
        } catch (Exception e) {
        }
        return obj;
    }

    public synchronized void insertKpiLogServerMonthly(KpiLogServerMonthly obj) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Insert KPI Log Server Monthly");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_log_server_monthly \n");
            sql.append(" (app_code,func_name,\n");
            sql.append(" kpi,month,total,insert_time,type,year) \n");
            sql.append(" VALUES(?,?,?,?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            if (obj.getAppCode() != null) {
                pstmt.setString(1, obj.getAppCode());
            } else {
                pstmt.setNull(1, java.sql.Types.VARCHAR);
            }
            if (obj.getFuncName() != null) {
                pstmt.setString(2, obj.getFuncName());
            } else {
                pstmt.setNull(2, java.sql.Types.VARCHAR);
            }
            if (obj.getKpi() != null) {
                pstmt.setDouble(3, obj.getKpi());
            } else {
                pstmt.setNull(3, java.sql.Types.NUMERIC);
            }
            if (obj.getMonth() != null) {
                pstmt.setLong(4, obj.getMonth());
            } else {
                pstmt.setNull(4, java.sql.Types.NUMERIC);
            }
            if (obj.getTotal() != null) {
                pstmt.setLong(5, obj.getTotal());
            } else {
                pstmt.setNull(5, java.sql.Types.NUMERIC);
            }
            pstmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));
            pstmt.setString(7, obj.getType());

            if (obj.getYear() != null) {
                pstmt.setLong(8, obj.getYear());
            } else {
                pstmt.setNull(8, java.sql.Types.NUMERIC);
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
}
