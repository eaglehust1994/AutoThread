/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.mobile.dl;

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

    public List<KpiMobileCountDl> getKpiMobileCount(Date updateTime) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiMobileCountDl> lst = new ArrayList<KpiMobileCountDl>();
        KpiMobileCountDl obj = null;
        try {
            logger.info("Get Kpi Count From Raw");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
//            sql.append("SELECT app_name app_code, url_request func_name,round(duration/1000,2) duration,\n");
//            sql.append("count(*) COUNT\n");
//            sql.append("FROM kpi_mobile_data_raw PARTITION(DATA").append(partition).append(") ");
//            sql.append("WHERE 1=1 \n");
//            sql.append("AND ((app_name = 'VSMART' AND response_code = '200') OR ");
//            sql.append("(app_name = 'MBCCS' AND response_code in ('0','1','TOKEN_INVALID'))) \n");
//            sql.append("AND url_request IS NOT NULL \n");
//            sql.append("AND duration IS NOT NULL \n");
//            sql.append("AND request_id IN (SELECT DISTINCT request_id FROM kpi_mobile_data_raw PARTITION(DATA").append(partition).append(")) ");
//            sql.append("GROUP BY app_name,url_request,round(duration/1000,2) \n");
            //dungvv8_11042016
            sql.append("SELECT app_name app_code, url_request func_name,round(duration/1000,2) duration,count(*) COUNT\n");
            sql.append("FROM\n");
            sql.append("(SELECT DISTINCT app_name,url_request,duration,request_id,response_code\n");
            sql.append("FROM kpi_mobile_data_raw PARTITION FOR(to_date('").append(partition).append("','yyyyMMdd'))) \n");
            sql.append("WHERE 1=1 \n");
            sql.append("AND ((app_name = 'VSMART' AND response_code = '200') OR ");
            sql.append("(app_name = 'MBCCS' AND response_code in ('0','1','TOKEN_INVALID'))) \n");
            sql.append("AND url_request IS NOT NULL \n");
            sql.append("AND duration IS NOT NULL \n");
            sql.append("GROUP BY app_name,url_request,round(duration/1000,2) \n");
            //dungvv8_11042016

            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiMobileCountDl();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setFuncName(DBUtil.getString(rs.getObject("func_name")));
                obj.setDuration(DBUtil.getDouble(rs.getObject("duration")));
                obj.setCount(DBUtil.getLong(rs.getObject("count")));
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

    public synchronized void insertKpiMobileCountDl(List<KpiMobileCountDl> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert KPI Mobile Count Dl");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_mobile_count_dl \n");
            sql.append(" (app_code,func_name,\n");
            sql.append(" duration,count,update_time) \n");
            sql.append(" VALUES(?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            for (KpiMobileCountDl obj : lst) {
                if (obj.getAppCode() != null) {
                    pstmt.setString(1, obj.getAppCode());
                } else {
                    pstmt.setNull(1, java.sql.Types.NUMERIC);
                }
                if (obj.getFuncName() != null) {
                    pstmt.setString(2, obj.getFuncName());
                } else {
                    pstmt.setNull(2, java.sql.Types.VARCHAR);
                }
                if (obj.getDuration() != null) {
                    pstmt.setDouble(3, obj.getDuration());
                } else {
                    pstmt.setNull(3, java.sql.Types.NUMERIC);
                }
                if (obj.getCount() != null) {
                    pstmt.setLong(4, obj.getCount());
                } else {
                    pstmt.setNull(4, java.sql.Types.NUMERIC);
                }
                if (obj.getUpdateTime() != null) {
                    pstmt.setDate(5, new java.sql.Date(obj.getUpdateTime().getTime()));
                } else {
                    pstmt.setNull(5, java.sql.Types.DATE);
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

    public synchronized void deleteKpiMobileCountDl(Date updateTime) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele KPI Mobile Count Dl");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_mobile_count_dl PARTITION(DATA").append(partition).append(")");
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

    public List<CataFuncMobile> getListCataFuncMobile() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<CataFuncMobile> lst = new ArrayList<CataFuncMobile>();
        try {
            logger.info("Get List CataFuc Mobile");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT b.app_code, a.url_name func_name,a.url_pattern url, a.url_id \n");
            sql.append("  FROM cata_url a");
            sql.append("  LEFT JOIN cata_app b ON a.app_id = b.app_id");
            sql.append("  WHERE b.app_code IN ('MBCCS','VSMART')");
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CataFuncMobile obj = new CataFuncMobile();
                obj.setUrlId(DBUtil.getLong(rs.getObject("url_id")));
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setFuncName(DBUtil.getString(rs.getObject("func_name")));
                obj.setUrl(DBUtil.getString(rs.getObject("url")));
//                obj.setTarget(DBUtil.getDouble(rs.getObject("target")));
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

    public List<KpiMobileCountDl> getKpiMobileCountByMonthly(Date startDate, Date endDate, CataFuncMobile cataFuncMobile) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiMobileCountDl> lst = new ArrayList<KpiMobileCountDl>();
        KpiMobileCountDl obj = null;
        try {
            logger.info("Get Kpi Mobile Count");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT a.app_code, a.func_name, a.duration, a.count,\n");
            sql.append(" a.update_time\n");
            sql.append("  FROM kpi_mobile_count_dl a");
            sql.append("  WHERE a.app_code = ? \n");
            sql.append("  AND a.func_name = ? \n");
            sql.append("  AND a.update_time >= ? \n");
            sql.append("  AND a.update_time < ? \n");
            sql.append("  ORDER BY a.duration");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, cataFuncMobile.getAppCode());
            pstmt.setString(2, cataFuncMobile.getUrl());
            pstmt.setDate(3, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(4, new java.sql.Date(endDate.getTime()));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiMobileCountDl();
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

    public Long getTotalMonthly(Date startDate, Date endDate, CataFuncMobile cataFuncMobile) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Long total = 0l;
        try {
            logger.info("Get Total");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT sum(a.count) sum\n");
            sql.append("  FROM kpi_mobile_count_dl a");
            sql.append("  WHERE a.app_code = ? \n");
            sql.append("  AND a.func_name = ? \n");
            sql.append("  AND a.update_time >= ? \n");
            sql.append("  AND a.update_time < ? \n");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, cataFuncMobile.getAppCode());
            pstmt.setString(2, cataFuncMobile.getUrl());
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

    public KpiMobileMonthly getKpiMobileMonthly(List<KpiMobileCountDl> lst, Double rank95,
            Long total, Integer month, Integer year, CataFuncMobile cataFuncMobile) {
        KpiMobileMonthly obj = new KpiMobileMonthly();
        long temp = 0l;
        try {
            logger.info("Get Kpi Mobile Monthly");
            for (KpiMobileCountDl countDl : lst) {
                temp = temp + countDl.getCount();
                if (temp > rank95) {
                    obj.setAppCode(countDl.getAppCode());
                    obj.setFuncName(countDl.getFuncName());
                    obj.setKpi(countDl.getDuration());
                    obj.setMonth(month);
                    obj.setTotal(total);
                    obj.setInsertTime(new Date());
                    obj.setUrlId(cataFuncMobile.getUrlId());
                    obj.setYear(year);
                    break;
                }
            }
        } catch (Exception e) {
        }
        return obj;
    }

    public synchronized void insertKpiMobileMonthly(KpiMobileMonthly obj) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Insert KPI Mobile Monthly");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_mobile_monthly \n");
            sql.append(" (app_code,func_name,\n");
            sql.append(" kpi,month,total,insert_time,url_id,year) \n");
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
            if (obj.getUrlId() != null) {
                pstmt.setLong(7, obj.getUrlId());
            } else {
                pstmt.setNull(7, java.sql.Types.NUMERIC);
            }
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

    public synchronized void deleteKpiMobileMonthly(Integer month) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele KPI Mobile Monthly");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_mobile_monthly a");
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

    public List<KpiMobileCountDl> getKpiMobileCountByDl(Date updateTime, CataFuncMobile cataFuncMobile) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiMobileCountDl> lst = new ArrayList<KpiMobileCountDl>();
        KpiMobileCountDl obj = null;
        try {
            logger.info("Get Kpi Mobile Count Dl");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT a.app_code, a.func_name, a.duration, a.count,\n");
            sql.append(" a.update_time\n");
            sql.append("  FROM kpi_mobile_count_dl PARTITION(DATA").append(partition).append(") a");
            sql.append("  WHERE a.app_code = ? \n");
            sql.append("  AND a.func_name = ? \n");
            sql.append("  ORDER BY a.duration");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, cataFuncMobile.getAppCode());
            pstmt.setString(2, cataFuncMobile.getUrl());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiMobileCountDl();
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

    public Long getTotalDl(Date updateTime, CataFuncMobile cataFuncMobile) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Long total = 0l;
        try {
            logger.info("Get Total Dl");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT sum(a.count) sum\n");
            sql.append("  FROM kpi_mobile_count_dl PARTITION(DATA").append(partition).append(") a");
            sql.append("  WHERE a.app_code = ? \n");
            sql.append("  AND a.func_name = ? \n");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, cataFuncMobile.getAppCode());
            pstmt.setString(2, cataFuncMobile.getUrl());
//            pstmt.setDate(3, new java.sql.Date(startDate.getTime()));
//            pstmt.setDate(4, new java.sql.Date(endDate.getTime()));
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

    public KpiMobileDaily getKpiMobileDl(List<KpiMobileCountDl> lst, Double rank95, Long total, CataFuncMobile cataFuncMobile, Date updateTime) {
        KpiMobileDaily obj = new KpiMobileDaily();
        long temp = 0l;
        try {
            logger.info("Get Kpi Mobile Dl");
            for (KpiMobileCountDl countDl : lst) {
                temp = temp + countDl.getCount();
                if (temp > rank95) {
                    obj.setAppCode(countDl.getAppCode());
                    obj.setFuncName(countDl.getFuncName());
                    obj.setKpi(countDl.getDuration());
                    obj.setTotal(total);
                    obj.setInsertTime(new Date());
                    obj.setUpdateTime(updateTime);
                    obj.setUrlId(cataFuncMobile.getUrlId());
                    break;
                }
            }
        } catch (Exception e) {
        }
        return obj;
    }

    public synchronized void insertKpiMobileDl(KpiMobileDaily obj) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Insert KPI Mobile Dl");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_mobile_dl \n");
            sql.append(" (app_code,func_name,\n");
            sql.append(" kpi,total,update_time,insert_time,url_id) \n");
            sql.append(" VALUES(?,?,?,?,?,?,?)");
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
            if (obj.getTotal() != null) {
                pstmt.setLong(4, obj.getTotal());
            } else {
                pstmt.setNull(4, java.sql.Types.NUMERIC);
            }
            if (obj.getUpdateTime() != null) {
                pstmt.setDate(5, new java.sql.Date(obj.getUpdateTime().getTime()));
            } else {
                pstmt.setNull(5, java.sql.Types.NUMERIC);
            }
            pstmt.setTimestamp(6, new java.sql.Timestamp(new Date().getTime()));

            if (obj.getUrlId() != null) {
                pstmt.setLong(7, obj.getUrlId());
            } else {
                pstmt.setNull(7, java.sql.Types.NUMERIC);
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

    public synchronized void deleteKpiMobileDl(Date updateTime) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele KPI Mobile Dl");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE kpi_mobile_dl PARTITION(DATA").append(partition).append(") a");
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
