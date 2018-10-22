/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.alarmNoc;

import com.viettel.kpi.mobile.raw.*;
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

    public List<CataAlarmConfig> getAlarmConfig() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<CataAlarmConfig> lst = new ArrayList<CataAlarmConfig>();
        CataAlarmConfig obj = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.alarm_id, a.alarm_type, a.alarm_exceed, a.alarm_rule, a.alarm_rule_value\n");
            sql.append("  FROM cata_alarm_config a");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new CataAlarmConfig();
                obj.setAlarmId(DBUtil.getLong(rs.getObject("alarm_id")));
                obj.setAlarmType(DBUtil.getString(rs.getObject("alarm_type")));
                obj.setAlarmExceed(DBUtil.getLong(rs.getObject("alarm_exceed")));
                obj.setAlarmRule(DBUtil.getString(rs.getObject("alarm_rule")));
                obj.setAlarmRuleValue(DBUtil.getLong(rs.getObject("alarm_rule_value")));
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

    //<editor-fold defaultstate="collapsed" desc="DAYS_PER_WEEK">
    public List<KpiAlarmNoc> getDaysPerWeekAddonTDXL(Long alarmExceed, Long alarmRuleValue) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiAlarmNoc> lst = new ArrayList<KpiAlarmNoc>();
        KpiAlarmNoc obj = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,'addon' measure_solution,\n");
            sql.append("a.app_name||','||a.url_name||','||'ngày,").append(alarmRuleValue).append("/7,TDXL' alarm_name,\n");
            sql.append("'ngày,").append(alarmRuleValue).append("/7,TDXL' alarm_type,a.target,trunc(SYSDATE) start_time,\n");
            sql.append("a.operating_department||','||a.operating_center||',addon,'||\n");
            sql.append("listagg(to_char(a.update_time,'dd/MM')||'('||a.kpi||','||a.total_request||')', ',') \n");
            sql.append("within GROUP (ORDER BY update_time) content\n");
            sql.append("FROM\n");
            sql.append("(SELECT decode(a.target,0,0,(c.kpi-a.target)/a.target) compare,\n");
            sql.append("b.app_code,b.app_name,a.url_name,b.operating_department,\n");
            sql.append("b.operating_center,c.update_time,a.target,\n");
            sql.append("c.total_request,c.kpi\n");
            sql.append("FROM \n");
            sql.append("(SELECT * FROM cata_url WHERE target IS NOT NULL AND alarm = 1) a\n");
            sql.append("LEFT JOIN cata_app b ON a.app_id = b.app_id\n");
            sql.append("LEFT JOIN  \n");
            sql.append("(SELECT * FROM kpi_data_raw_dl  \n");
            sql.append("WHERE update_time <= trunc(sysdate-1)\n");
            sql.append("AND update_time >= trunc(sysdate-7)\n");
            sql.append("AND total_request > 100) c ON a.url_id = c.url_id\n");
            sql.append("WHERE c.kpi IS NOT NULL AND a.target IS NOT NULL ) a\n");
            sql.append("WHERE 1=1\n");
            sql.append("HAVING COUNT(CASE WHEN compare > ?/100 THEN 1 ELSE 0 END)> ?\n");
            sql.append("GROUP BY a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,a.target");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, alarmExceed);
            pstmt.setLong(2, alarmRuleValue);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiAlarmNoc();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setFuncName(DBUtil.getString(rs.getObject("url_name")));
                obj.setOperatingDepartment(DBUtil.getString(rs.getObject("operating_department")));
                obj.setOperatingCenter(DBUtil.getString(rs.getObject("operating_center")));
                obj.setMeasureSolution(DBUtil.getString(rs.getObject("measure_solution")));
                obj.setAlarmName(DBUtil.getString(rs.getObject("alarm_name")));
                obj.setAlarmType(DBUtil.getString(rs.getObject("alarm_type")));
                obj.setTarget(DBUtil.getString(rs.getObject("target")));
                obj.setStartTime(rs.getDate("start_time"));
                obj.setContent(DBUtil.getString(rs.getObject("content")));

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

    public List<KpiAlarmNoc> getDaysPerWeekAddonTLGD(Long alarmExceed, Long alarmRuleValue) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiAlarmNoc> lst = new ArrayList<KpiAlarmNoc>();
        KpiAlarmNoc obj = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,'addon' measure_solution,\n");
            sql.append("a.app_name||','||a.url_name||','||'ngày,").append(alarmRuleValue).append("/7,TLGD' alarm_name,\n");
            sql.append("'ngày,").append(alarmRuleValue).append("/7,TLGD' alarm_type,a.target,trunc(SYSDATE) start_time,\n");
            sql.append("a.operating_department||','||a.operating_center||',addon,'||\n");
            sql.append("listagg(to_char(a.update_time,'dd/MM')||'('||a.kpi||','||a.total_request||')', ',') \n");
            sql.append("within GROUP (ORDER BY update_time) content\n");
            sql.append("FROM\n");
            sql.append("(SELECT decode(a.target,0,0,(c.kpi-a.target)/a.target) compare,\n");
            sql.append("b.app_code,b.app_name,a.url_name,b.operating_department,\n");
            sql.append("b.operating_center,c.update_time,a.target,\n");
            sql.append("c.total_request,c.kpi\n");
            sql.append("FROM \n");
            sql.append("(SELECT url_id,app_id,url_name,target_success_rate target FROM cata_url WHERE target_success_rate IS NOT NULL AND alarm = 1) a\n");
            sql.append("LEFT JOIN cata_app b ON a.app_id = b.app_id\n");
            sql.append("LEFT JOIN  \n");
            sql.append("(SELECT url_id,update_time,total_request,succ_rate kpi FROM kpi_data_raw_dl  \n");
            sql.append("WHERE update_time <= trunc(sysdate-1)\n");
            sql.append("AND update_time >= trunc(sysdate-7)\n");
            sql.append("AND total_request > 100) c ON a.url_id = c.url_id\n");
            sql.append("WHERE c.kpi IS NOT NULL AND a.target IS NOT NULL ) a\n");
            sql.append("WHERE 1=1\n");
            sql.append("HAVING COUNT(CASE WHEN compare > ?/100 THEN 1 ELSE 0 END)> ?\n");
            sql.append("GROUP BY a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,a.target");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, alarmExceed);
            pstmt.setLong(2, alarmRuleValue);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiAlarmNoc();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setFuncName(DBUtil.getString(rs.getObject("url_name")));
                obj.setOperatingDepartment(DBUtil.getString(rs.getObject("operating_department")));
                obj.setOperatingCenter(DBUtil.getString(rs.getObject("operating_center")));
                obj.setMeasureSolution(DBUtil.getString(rs.getObject("measure_solution")));
                obj.setAlarmName(DBUtil.getString(rs.getObject("alarm_name")));
                obj.setAlarmType(DBUtil.getString(rs.getObject("alarm_type")));
                obj.setTarget(DBUtil.getString(rs.getObject("target")));
                obj.setStartTime(rs.getDate("start_time"));
                obj.setContent(DBUtil.getString(rs.getObject("content")));

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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSECUTIVE_DAYS">
    public List<KpiAlarmNoc> getConsecutiveDaysAddonTDXL(Long alarmExceed, Long alarmRuleValue) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiAlarmNoc> lst = new ArrayList<KpiAlarmNoc>();
        KpiAlarmNoc obj = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,'addon' measure_solution,\n");
            sql.append("a.app_name||','||a.url_name||','||'ngày,").append(alarmRuleValue).append("/7,TDXL' alarm_name,\n");
            sql.append("'ngày,").append(alarmRuleValue).append("/7,TDXL' alarm_type,a.target,trunc(SYSDATE) start_time,\n");
            sql.append("a.operating_department||','||a.operating_center||',addon,'||\n");
            sql.append("listagg(to_char(a.update_time,'dd/MM')||'('||a.kpi||','||a.total_request||')', ',') \n");
            sql.append("within GROUP (ORDER BY update_time) content\n");
            sql.append("FROM\n");
            sql.append("(SELECT decode(a.target,0,0,(c.kpi-a.target)/a.target) compare,\n");
            sql.append("b.app_code,b.app_name,a.url_name,b.operating_department,\n");
            sql.append("b.operating_center,c.update_time,a.target,\n");
            sql.append("c.total_request,c.kpi\n");
            sql.append("FROM \n");
            sql.append("(SELECT url_id,app_id,url_name,target_success_rate target FROM cata_url WHERE target IS NOT NULL AND alarm = 1) a\n");
            sql.append("LEFT JOIN cata_app b ON a.app_id = b.app_id\n");
            sql.append("LEFT JOIN  \n");
            sql.append("(SELECT url_id,update_time,total_request,succ_rate kpi FROM kpi_data_raw_dl  \n");
            sql.append("WHERE update_time <= trunc(sysdate-1)\n");
            sql.append("AND update_time >= trunc(sysdate-?)\n");
            sql.append("AND total_request > 100) c ON a.url_id = c.url_id\n");
            sql.append("WHERE c.kpi IS NOT NULL AND a.target IS NOT NULL ) a\n");
            sql.append("WHERE 1=1\n");
            sql.append("HAVING COUNT(CASE WHEN compare > ?/100 THEN 1 ELSE 0 END)= ?\n");
            sql.append("GROUP BY a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,a.target");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, alarmRuleValue);
            pstmt.setLong(2, alarmExceed);
            pstmt.setLong(3, alarmRuleValue);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiAlarmNoc();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setFuncName(DBUtil.getString(rs.getObject("url_name")));
                obj.setOperatingDepartment(DBUtil.getString(rs.getObject("operating_department")));
                obj.setOperatingCenter(DBUtil.getString(rs.getObject("operating_center")));
                obj.setMeasureSolution(DBUtil.getString(rs.getObject("measure_solution")));
                obj.setAlarmName(DBUtil.getString(rs.getObject("alarm_name")));
                obj.setAlarmType(DBUtil.getString(rs.getObject("alarm_type")));
                obj.setTarget(DBUtil.getString(rs.getObject("target")));
                obj.setStartTime(rs.getDate("start_time"));
                obj.setContent(DBUtil.getString(rs.getObject("content")));

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

    public List<KpiAlarmNoc> getConsecutiveDaysAddonTLGD(Long alarmExceed, Long alarmRuleValue) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiAlarmNoc> lst = new ArrayList<KpiAlarmNoc>();
        KpiAlarmNoc obj = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,'addon' measure_solution,\n");
            sql.append("a.app_name||','||a.url_name||','||'ngày_").append(alarmRuleValue).append("/7,TLGD' alarm_name,\n");
            sql.append("'ngày,").append(alarmRuleValue).append("/7,TLGD' alarm_type,a.target,trunc(SYSDATE) start_time,\n");
            sql.append("a.operating_department||','||a.operating_center||',addon,'||\n");
            sql.append("listagg(to_char(a.update_time,'dd/MM')||'('||a.kpi||','||a.total_request||')', ',') \n");
            sql.append("within GROUP (ORDER BY update_time) content\n");
            sql.append("FROM\n");
            sql.append("(SELECT decode(a.target,0,0,(c.kpi-a.target)/a.target) compare,\n");
            sql.append("b.app_code,b.app_name,a.url_name,b.operating_department,\n");
            sql.append("b.operating_center,c.update_time,a.target,\n");
            sql.append("c.total_request,c.kpi \n");
            sql.append("FROM \n");
            sql.append("(SELECT url_id,app_id,url_name,target_success_rate target FROM cata_url WHERE target_success_rate IS NOT NULL AND alarm = 1) a\n");
            sql.append("LEFT JOIN cata_app b ON a.app_id = b.app_id\n");
            sql.append("LEFT JOIN  \n");
            sql.append("(SELECT update_time,url_id,total_request,succ_rate kpi FROM kpi_data_raw_dl  \n");
            sql.append("WHERE update_time <= trunc(sysdate-1)\n");
            sql.append("AND update_time >= trunc(sysdate-?)\n");
            sql.append("AND total_request > 100) c ON a.url_id = c.url_id\n");
            sql.append("WHERE c.kpi IS NOT NULL AND a.target IS NOT NULL ) a\n");
            sql.append("WHERE 1=1\n");
            sql.append("HAVING COUNT(CASE WHEN compare > ?/100 THEN 1 ELSE 0 END)= ?\n");
            sql.append("GROUP BY a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,a.target");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, alarmRuleValue);
            pstmt.setLong(2, alarmExceed);
            pstmt.setLong(3, alarmRuleValue);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiAlarmNoc();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setFuncName(DBUtil.getString(rs.getObject("url_name")));
                obj.setOperatingDepartment(DBUtil.getString(rs.getObject("operating_department")));
                obj.setOperatingCenter(DBUtil.getString(rs.getObject("operating_center")));
                obj.setMeasureSolution(DBUtil.getString(rs.getObject("measure_solution")));
                obj.setAlarmName(DBUtil.getString(rs.getObject("alarm_name")));
                obj.setAlarmType(DBUtil.getString(rs.getObject("alarm_type")));
                obj.setTarget(DBUtil.getString(rs.getObject("target")));
                obj.setStartTime(rs.getDate("start_time"));
                obj.setContent(DBUtil.getString(rs.getObject("content")));

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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="CONSECUTIVE_WEEKS">
    public List<KpiAlarmNoc> getConsecutiveWeeksAddonTDXL(Long alarmExceed, Long alarmRuleValue, int index) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiAlarmNoc> lst = new ArrayList<KpiAlarmNoc>();
        KpiAlarmNoc obj = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,'addon' measure_solution,\n");
            sql.append("a.app_name||','||a.url_name||',tuan,").append(alarmRuleValue).append(" tuan lien tiep,TDXL' alarm_name,\n");
            sql.append("'tuan,").append(alarmRuleValue).append(" tuan lien tiep,TDXL' alarm_type,a.target,trunc(SYSDATE) start_time,\n");
            sql.append("a.operating_department||','||a.operating_center||',addon,'||\n");
            sql.append("'tuan_n('||a.kpi_week||','||a.total_request||')' content,a.url_id\n");
            sql.append("FROM\n");
            sql.append("(SELECT decode(a.target,0,0,(c.kpi_week-a.target)/a.target) compare,\n");
            sql.append("b.app_code,b.app_name,a.url_name,b.operating_department,\n");
            sql.append("b.operating_center,c.update_time,a.target,\n");
            sql.append("c.total_request,c.kpi_week,c.url_id\n");
            sql.append("FROM \n");
            sql.append("(SELECT * FROM cata_url WHERE target IS NOT NULL AND alarm = 1) a\n");
            sql.append("LEFT JOIN cata_app b ON a.app_id = b.app_id\n");
            sql.append("LEFT JOIN  \n");
            sql.append("(SELECT url_id,update_time,SUM(total_request) total_request,avg(kpi) kpi_week FROM kpi_data_raw_dl  \n");
            sql.append("WHERE update_time <= trunc(sysdate-(7*?+1))\n");
            sql.append("AND update_time >= trunc(sysdate-(7*?+7))\n");
            sql.append("HAVING SUM(total_request) > 100\n");
            sql.append("GROUP BY url_id,update_time\n");
            sql.append(") c ON a.url_id = c.url_id\n");
            sql.append("WHERE c.kpi_week IS NOT NULL AND a.target IS NOT NULL ) a\n");
            sql.append("WHERE 1=1\n");
            sql.append("AND decode(a.target,0,0,(a.kpi_week-a.target)/a.target) > ?/100");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, index);
            pstmt.setLong(2, index);
            pstmt.setLong(3, alarmExceed);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiAlarmNoc();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setFuncName(DBUtil.getString(rs.getObject("url_name")));
                obj.setOperatingDepartment(DBUtil.getString(rs.getObject("operating_department")));
                obj.setOperatingCenter(DBUtil.getString(rs.getObject("operating_center")));
                obj.setMeasureSolution(DBUtil.getString(rs.getObject("measure_solution")));
                obj.setAlarmName(DBUtil.getString(rs.getObject("alarm_name")));
                obj.setAlarmType(DBUtil.getString(rs.getObject("alarm_type")));
                obj.setTarget(DBUtil.getString(rs.getObject("target")));
                obj.setStartTime(rs.getDate("start_time"));
                obj.setContent(DBUtil.getString(rs.getObject("content")));
                obj.setId(DBUtil.getString(rs.getObject("measure_solution"))
                        + DBUtil.getString(rs.getObject("alarm_type"))
                        + DBUtil.getLong(rs.getObject("url_id"))
                );
                obj.setCount(1L);

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

    public List<KpiAlarmNoc> getConsecutiveWeeksAddonTLGD(Long alarmExceed, Long alarmRuleValue, int index) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiAlarmNoc> lst = new ArrayList<KpiAlarmNoc>();
        KpiAlarmNoc obj = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,'addon' measure_solution,\n");
            sql.append("a.app_name||','||a.url_name||',tuan,").append(alarmRuleValue).append(" tuan lien tiep,TDXL' alarm_name,\n");
            sql.append("'tuan,").append(alarmRuleValue).append(" tuan lien tiep,TDXL' alarm_type,a.target,trunc(SYSDATE) start_time,\n");
            sql.append("a.operating_department||','||a.operating_center||',addon,'||\n");
            sql.append("'tuan_n('||a.kpi_week||','||a.total_request||')' content,a.url_id\n");
            sql.append("FROM\n");
            sql.append("(SELECT decode(a.target,0,0,(c.kpi_week-a.target)/a.target) compare,\n");
            sql.append("b.app_code,b.app_name,a.url_name,b.operating_department,\n");
            sql.append("b.operating_center,c.update_time,a.target,\n");
            sql.append("c.total_request,c.kpi_week,c.url_id\n");
            sql.append("FROM \n");
            sql.append("(SELECT url_id,app_id,url_name,target_success_rate target FROM cata_url WHERE target_success_rate IS NOT NULL AND alarm = 1) a\n");
            sql.append("LEFT JOIN cata_app b ON a.app_id = b.app_id\n");
            sql.append("LEFT JOIN  \n");
            sql.append("(SELECT url_id,update_time,SUM(total_request) total_request,avg(succ_rate) kpi_week FROM kpi_data_raw_dl  \n");
            sql.append("WHERE update_time <= trunc(sysdate-(7*?+1))\n");
            sql.append("AND update_time >= trunc(sysdate-(7*?+7))\n");
            sql.append("HAVING SUM(total_request) > 100\n");
            sql.append("GROUP BY url_id,update_time\n");
            sql.append(") c ON a.url_id = c.url_id\n");
            sql.append("WHERE c.kpi_week IS NOT NULL AND a.target IS NOT NULL ) a\n");
            sql.append("WHERE 1=1\n");
            sql.append("AND decode(a.target,0,0,(a.kpi_week-a.target)/a.target) > ?/100");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, index);
            pstmt.setLong(2, index);
            pstmt.setLong(3, alarmExceed);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiAlarmNoc();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setFuncName(DBUtil.getString(rs.getObject("url_name")));
                obj.setOperatingDepartment(DBUtil.getString(rs.getObject("operating_department")));
                obj.setOperatingCenter(DBUtil.getString(rs.getObject("operating_center")));
                obj.setMeasureSolution(DBUtil.getString(rs.getObject("measure_solution")));
                obj.setAlarmName(DBUtil.getString(rs.getObject("alarm_name")));
                obj.setAlarmType(DBUtil.getString(rs.getObject("alarm_type")));
                obj.setTarget(DBUtil.getString(rs.getObject("target")));
                obj.setStartTime(rs.getDate("start_time"));
                obj.setContent(DBUtil.getString(rs.getObject("content")));
                obj.setId(DBUtil.getString(rs.getObject("measure_solution"))
                        + DBUtil.getString(rs.getObject("alarm_type"))
                        + DBUtil.getLong(rs.getObject("url_id"))
                );
                obj.setCount(1L);

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

    public List<KpiAlarmNoc> getConsecutiveWeeksMobileTDXL(Long alarmExceed, Long alarmRuleValue, int index) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiAlarmNoc> lst = new ArrayList<KpiAlarmNoc>();
        KpiAlarmNoc obj = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,'addon' measure_solution,\n");
            sql.append("a.app_name||','||a.url_name||',tuan,").append(alarmRuleValue).append(" tuan lien tiep,TDXL' alarm_name,\n");
            sql.append("'tuan,").append(alarmRuleValue).append(" tuan lien tiep,TDXL' alarm_type,a.target,trunc(SYSDATE) start_time,\n");
            sql.append("a.operating_department||','||a.operating_center||',addon,'||\n");
            sql.append("'tuan_n('||a.kpi_week||','||a.total_request||')' content,a.url_id\n");
            sql.append("FROM\n");
            sql.append("(SELECT decode(a.target,0,0,(c.kpi_week-a.target)/a.target) compare,\n");
            sql.append("b.app_code,b.app_name,a.url_name,b.operating_department,\n");
            sql.append("b.operating_center,c.update_time,a.target,\n");
            sql.append("c.total_request,c.kpi_week,c.url_id\n");
            sql.append("FROM \n");
            sql.append("(SELECT * FROM cata_url WHERE target IS NOT NULL AND alarm = 1) a\n");
            sql.append("LEFT JOIN cata_app b ON a.app_id = b.app_id\n");
            sql.append("LEFT JOIN  \n");
            sql.append("(SELECT url_id,update_time,SUM(total) total_request,avg(kpi) kpi_week FROM kpi_mobile_dl  \n");
            sql.append("WHERE update_time <= trunc(sysdate-(7*?+1))\n");
            sql.append("AND update_time >= trunc(sysdate-(7*?+7))\n");
            sql.append("HAVING SUM(total) > 100\n");
            sql.append("GROUP BY url_id,update_time\n");
            sql.append(") c ON a.url_id = c.url_id\n");
            sql.append("WHERE c.kpi_week IS NOT NULL AND a.target IS NOT NULL ) a\n");
            sql.append("WHERE 1=1\n");
            sql.append("AND decode(a.target,0,0,(a.kpi_week-a.target)/a.target) > ?/100");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, index);
            pstmt.setLong(2, index);
            pstmt.setLong(3, alarmExceed);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiAlarmNoc();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setFuncName(DBUtil.getString(rs.getObject("url_name")));
                obj.setOperatingDepartment(DBUtil.getString(rs.getObject("operating_department")));
                obj.setOperatingCenter(DBUtil.getString(rs.getObject("operating_center")));
                obj.setMeasureSolution(DBUtil.getString(rs.getObject("measure_solution")));
                obj.setAlarmName(DBUtil.getString(rs.getObject("alarm_name")));
                obj.setAlarmType(DBUtil.getString(rs.getObject("alarm_type")));
                obj.setTarget(DBUtil.getString(rs.getObject("target")));
                obj.setStartTime(rs.getDate("start_time"));
                obj.setContent(DBUtil.getString(rs.getObject("content")));
                obj.setId(DBUtil.getString(rs.getObject("measure_solution"))
                        + DBUtil.getString(rs.getObject("alarm_type"))
                        + DBUtil.getLong(rs.getObject("url_id"))
                );
                obj.setCount(1L);

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

    public List<KpiAlarmNoc> getConsecutiveWeeksLogserverTDXL(Long alarmExceed, Long alarmRuleValue, int index) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiAlarmNoc> lst = new ArrayList<KpiAlarmNoc>();
        KpiAlarmNoc obj = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,'addon' measure_solution,\n");
            sql.append("a.app_name||','||a.url_name||',tuan,").append(alarmRuleValue).append(" tuan lien tiep,TDXL' alarm_name,\n");
            sql.append("'tuan,").append(alarmRuleValue).append(" tuan lien tiep,TDXL' alarm_type,a.target,trunc(SYSDATE) start_time,\n");
            sql.append("a.operating_department||','||a.operating_center||',addon,'||\n");
            sql.append("'tuan_n('||a.kpi_week||','||a.total_request||')' content\n");
            sql.append("FROM\n");
            sql.append("(SELECT decode(a.target,0,0,(c.kpi_week-a.target)/a.target) compare,\n");
            sql.append("b.app_code,b.app_name,a.url_name,b.operating_department,\n");
            sql.append("b.operating_center,c.update_time,a.target,\n");
            sql.append("c.total_request,c.kpi_week\n");
            sql.append("FROM \n");
            sql.append("(SELECT app_code,func_name,target_response_time target,func_real_name url_name FROM cata_func_log_server WHERE target_response_time IS NOT NULL AND alarm = 1) a\n");
            sql.append("LEFT JOIN cata_app b ON a.app_code = b.app_code\n");
            sql.append("LEFT JOIN  \n");
            sql.append("(SELECT app_code,func_name,update_time,SUM(total) total_request,avg(kpi) kpi_week FROM kpi_log_server_dl  \n");
            sql.append("WHERE update_time <= trunc(sysdate-(7*?+1))\n");
            sql.append("AND update_time >= trunc(sysdate-(7*?+7))\n");
            sql.append("HAVING SUM(total) > 100\n");
            sql.append("GROUP BY app_code,func_name,update_time\n");
            sql.append(") c ON a.app_code = c.app_code AND a.func_name=c.func_name  \n");
            sql.append("WHERE c.kpi_week IS NOT NULL AND a.target IS NOT NULL ) a\n");
            sql.append("WHERE 1=1\n");
            sql.append("AND decode(a.target,0,0,(a.kpi_week-a.target)/a.target) > ?/100");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, index);
            pstmt.setLong(2, index);
            pstmt.setLong(3, alarmExceed);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiAlarmNoc();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setFuncName(DBUtil.getString(rs.getObject("url_name")));
                obj.setOperatingDepartment(DBUtil.getString(rs.getObject("operating_department")));
                obj.setOperatingCenter(DBUtil.getString(rs.getObject("operating_center")));
                obj.setMeasureSolution(DBUtil.getString(rs.getObject("measure_solution")));
                obj.setAlarmName(DBUtil.getString(rs.getObject("alarm_name")));
                obj.setAlarmType(DBUtil.getString(rs.getObject("alarm_type")));
                obj.setTarget(DBUtil.getString(rs.getObject("target")));
                obj.setStartTime(rs.getDate("start_time"));
                obj.setContent(DBUtil.getString(rs.getObject("content")));
                obj.setId(DBUtil.getString(rs.getObject("measure_solution"))
                        + DBUtil.getString(rs.getObject("alarm_type"))
                        + DBUtil.getString(rs.getObject("app_code"))
                        + DBUtil.getString(rs.getObject("url_name")));
                obj.setCount(1L);

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

    public List<KpiAlarmNoc> getConsecutiveWeeksLogserverTLGD(Long alarmExceed, Long alarmRuleValue, int index) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<KpiAlarmNoc> lst = new ArrayList<KpiAlarmNoc>();
        KpiAlarmNoc obj = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.app_code,a.app_name,a.url_name,a.operating_department,a.operating_center,'addon' measure_solution,\n");
            sql.append("a.app_name||','||a.url_name||',tuan,").append(alarmRuleValue).append(" tuan lien tiep,TDXL' alarm_name,\n");
            sql.append("'tuan,").append(alarmRuleValue).append(" tuan lien tiep,TDXL' alarm_type,a.target,trunc(SYSDATE) start_time,\n");
            sql.append("a.operating_department||','||a.operating_center||',addon,'||\n");
            sql.append("'tuan_n('||a.kpi_week||','||a.total_request||')' content\n");
            sql.append("FROM\n");
            sql.append("(SELECT decode(a.target,0,0,(c.kpi_week-a.target)/a.target) compare,\n");
            sql.append("b.app_code,b.app_name,a.url_name,b.operating_department,\n");
            sql.append("b.operating_center,c.update_time,a.target,\n");
            sql.append("c.total_request,c.kpi_week\n");
            sql.append("FROM \n");
            sql.append("(SELECT app_code,func_name,target_response_time target,func_real_name url_name FROM cata_func_log_server WHERE target_response_time IS NOT NULL AND alarm = 1) a\n");
            sql.append("LEFT JOIN cata_app b ON a.app_code = b.app_code\n");
            sql.append("LEFT JOIN  \n");
            sql.append("(SELECT app_code,func_name,update_time,SUM(total_trans) total_request,avg(succ_rate) kpi_week FROM kpi_log_server_other_dl  \n");
            sql.append("WHERE update_time <= trunc(sysdate-(7*?+1))\n");
            sql.append("AND update_time >= trunc(sysdate-(7*?+7))\n");
            sql.append("HAVING SUM(total_trans) > 100\n");
            sql.append("GROUP BY app_code,func_name,update_time\n");
            sql.append(") c ON a.app_code = c.app_code AND a.func_name=c.func_name  \n");
            sql.append("WHERE c.kpi_week IS NOT NULL AND a.target IS NOT NULL ) a\n");
            sql.append("WHERE 1=1\n");
            sql.append("AND decode(a.target,0,0,(a.kpi_week-a.target)/a.target) > ?/100");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, index);
            pstmt.setLong(2, index);
            pstmt.setLong(3, alarmExceed);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                obj = new KpiAlarmNoc();
                obj.setAppCode(DBUtil.getString(rs.getObject("app_code")));
                obj.setAppName(DBUtil.getString(rs.getObject("app_name")));
                obj.setFuncName(DBUtil.getString(rs.getObject("url_name")));
                obj.setOperatingDepartment(DBUtil.getString(rs.getObject("operating_department")));
                obj.setOperatingCenter(DBUtil.getString(rs.getObject("operating_center")));
                obj.setMeasureSolution(DBUtil.getString(rs.getObject("measure_solution")));
                obj.setAlarmName(DBUtil.getString(rs.getObject("alarm_name")));
                obj.setAlarmType(DBUtil.getString(rs.getObject("alarm_type")));
                obj.setTarget(DBUtil.getString(rs.getObject("target")));
                obj.setStartTime(rs.getDate("start_time"));
                obj.setContent(DBUtil.getString(rs.getObject("content")));
                obj.setId(DBUtil.getString(rs.getObject("measure_solution"))
                        + DBUtil.getString(rs.getObject("alarm_type"))
                        + DBUtil.getString(rs.getObject("app_code"))
                        + DBUtil.getString(rs.getObject("url_name")));
                obj.setCount(1L);

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
    //</editor-fold>
    
    public synchronized void insertKpiAlarmNoc(List<KpiAlarmNoc> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO kpi_alarm_noc \n");
            sql.append(" (app_code, app_name, func_name, operating_department, operating_center,\n");
            sql.append("       mesure_solution, alarm_name, content, alarm_type, start_time, target, id) \n");
            sql.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,kpi_alarm_noc_seq.nextval)");

            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            for (KpiAlarmNoc bo : lst) {

                //<editor-fold defaultstate="collapsed" desc="set param">
                if (bo.getAppCode() != null) {
                    pstmt.setString(1, bo.getAppCode());
                } else {
                    pstmt.setNull(1, java.sql.Types.VARCHAR);
                }
                if (bo.getAppName() != null) {
                    pstmt.setString(2, bo.getAppName());
                } else {
                    pstmt.setNull(2, java.sql.Types.VARCHAR);
                }
                if (bo.getStartTime() != null) {
                    pstmt.setTimestamp(3, new java.sql.Timestamp(bo.getStartTime().getTime()));
                } else {
                    pstmt.setNull(3, java.sql.Types.TIMESTAMP);
                }
                if (bo.getFuncName() != null) {
                    pstmt.setString(4, bo.getFuncName());
                } else {
                    pstmt.setNull(4, java.sql.Types.VARCHAR);
                }
                if (bo.getOperatingDepartment() != null) {
                    pstmt.setString(5, bo.getOperatingDepartment());
                } else {
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                }
                if (bo.getOperatingCenter() != null) {
                    pstmt.setString(6, bo.getOperatingCenter());
                } else {
                    pstmt.setNull(6, java.sql.Types.VARCHAR);
                }
                if (bo.getMeasureSolution() != null) {
                    pstmt.setString(7, bo.getMeasureSolution());
                } else {
                    pstmt.setNull(7, java.sql.Types.VARCHAR);
                }
                if (bo.getAlarmName() != null) {
                    pstmt.setString(8, bo.getAlarmName());
                } else {
                    pstmt.setNull(8, java.sql.Types.VARCHAR);
                }
                if (bo.getContent() != null) {
                    pstmt.setString(9, bo.getContent());
                } else {
                    pstmt.setNull(9, java.sql.Types.VARCHAR);
                }
                if (bo.getStartTime() != null) {
                    pstmt.setTimestamp(10, new java.sql.Timestamp(bo.getStartTime().getTime()));
                } else {
                    pstmt.setNull(10, java.sql.Types.TIMESTAMP);
                }
                if (bo.getTarget() != null) {
                    pstmt.setString(11, bo.getTarget());
                } else {
                    pstmt.setNull(11, java.sql.Types.VARCHAR);
                }
                //</editor-fold>

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

    public synchronized void deleteKpiMobileRawTemp(List<KpiMobileDataRaw> lst, Date now) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            String _now = DateTimeUtils.format(now, "dd/MM/yyyy");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE kpi_mobile_data_raw_temp PARTITION FOR(to_date('").append(_now).append("','dd/MM/yyyy')) ");
            sql.append("WHERE request_id = ? ");
            pstmt = con.prepareStatement(sql.toString());

            for (KpiMobileDataRaw bo : lst) {
                pstmt.setString(1, bo.getRequestID());
                i++;
                pstmt.addBatch();
                if (i >= 50) {
                    pstmt.executeBatch();
                    pstmt.clearBatch();
                    logger.info("DeteleData(): " + i + "  records");
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
}
