/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.incidentSync;

import com.viettel.business.webservice.KpiErrCntt;
import com.viettel.kpi.logServer.dl.*;
import com.viettel.kpi.common.utils.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public List<CataFuncLogServer> syncIncidents(List<KpiErrCntt> errCntts, Date startTime, Date endTime) throws SQLException {
        Connection con = null;
        PreparedStatement deleteStmt = null;
        PreparedStatement insertStmt = null;
        List<CataFuncLogServer> lst = new ArrayList<CataFuncLogServer>();
        try {
            con = getConnectionPool();
            con.setAutoCommit(false);
            //Delete old rows
            StringBuilder deleteSql = new StringBuilder("DELETE KPI_ERR_CNTT WHERE 1 = 1 ");
            ArrayList<Date> params = new ArrayList();
            if (startTime != null) {
                deleteSql.append("AND err_time >= ?");
                params.add(startTime);
            }
            if (endTime != null) {
                deleteSql.append("AND err_time <= ?");
                params.add(endTime);
            }
            deleteStmt = con.prepareStatement(deleteSql.toString());
            for (int i = 0; i < params.size(); i++) {
                deleteStmt.setTimestamp(i + 1, new java.sql.Timestamp(params.get(i).getTime()));
            }
            deleteStmt.executeUpdate();
            //add new rows
            insertStmt = con.prepareStatement(
                    "  Insert into KPI_ERR_CNTT ("
                    + " ID,SYSTEM_NAME,SYSTEM_LEVEL,SYSTEM_GROUP,ERR_CONTENT,CAUSE,"
                    + " ERR_GROUP,XL_UCTT,XL_TD,MANAGER_DEPARTMENT,RESPONSIBILITY_DEP,"
                    + " RESPONSIBILITY_HUMAN,MONTH_YEAR,ERR_TIME,RECEIVE_TIME,END_TIME,"
                    + " TIME_TOTAL,EFFECT_LEVEL,NUM_COMPLAIN,NOTE,CUSTOMER_OBJ,DATE_COMPLAIN,"
                    + " WEEK,PROPLEM,SOLUTION,TYPE,DOWNTIME,DOWNTIME_CNTT,DOWNTIME_VTNET,"
                    + " ERR_ALL,ERR_VTNET,ERR_CNTT,BREAKDOWN3,BREAKDOWN2,BREAKDOWN1,REPORT_TCT,REPORT_TD) "
                    + "values ("
                    + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                    + " ?,?,?,?,?,?,?,?,?)");
            for (int i = 0; i < errCntts.size(); i++) {
                KpiErrCntt errCntt = errCntts.get(i);
                insertStmt.setObject(1, errCntt.getId());
                insertStmt.setString(2, errCntt.getSystemName());
                insertStmt.setString(3, errCntt.getSystemLevel());
                insertStmt.setString(4, errCntt.getSystemGroup());
                insertStmt.setString(5, errCntt.getErrContent());
                insertStmt.setString(6, errCntt.getCause());
                insertStmt.setString(7, errCntt.getErrGroup());
                insertStmt.setString(8, errCntt.getXlUctt());
                insertStmt.setString(9, errCntt.getXlTd());
                insertStmt.setString(10, errCntt.getManagerDepartment());
                insertStmt.setString(11, errCntt.getResponsibilityDep());
                insertStmt.setString(12, errCntt.getResponsibilityHuman());
                insertStmt.setDate(13,
                        errCntt.getMonthYear() == null
                                ? null
                                : new java.sql.Date(errCntt.getMonthYear().toGregorianCalendar().getTime().getTime()));
                insertStmt.setDate(14,
                        errCntt.getErrTime() == null
                                ? null
                                : new java.sql.Date(errCntt.getErrTime().toGregorianCalendar().getTime().getTime()));
                insertStmt.setDate(15,
                        errCntt.getReceiveTime() == null
                                ? null
                                : new java.sql.Date(errCntt.getReceiveTime().toGregorianCalendar().getTime().getTime()));
                insertStmt.setDate(16,
                        errCntt.getEndTime() == null
                                ? null
                                : new java.sql.Date(errCntt.getEndTime().toGregorianCalendar().getTime().getTime()));
                insertStmt.setObject(17, errCntt.getTimeTotal());
                insertStmt.setString(18, errCntt.getEffectLevel());
                insertStmt.setObject(19, errCntt.getNumComplain());
                insertStmt.setString(20, errCntt.getNote());
                insertStmt.setString(21, errCntt.getCustomerObj());
                insertStmt.setDate(22,
                        errCntt.getDateComplain() == null
                                ? null
                                : new java.sql.Date(errCntt.getDateComplain().toGregorianCalendar().getTime().getTime()));
                insertStmt.setString(23, errCntt.getWeek());
                insertStmt.setString(24, errCntt.getProplem());
                insertStmt.setString(25, errCntt.getSolution());
                insertStmt.setObject(26, errCntt.getType());
                insertStmt.setObject(27, errCntt.getDowntime());
                insertStmt.setObject(28, errCntt.getDowntimeCntt());
                insertStmt.setObject(29, errCntt.getDowntimeVtnet());
                insertStmt.setObject(30, errCntt.getErrAll());
                insertStmt.setObject(31, errCntt.getErrVtnet());
                insertStmt.setObject(32, errCntt.getErrCntt());
                insertStmt.setObject(33, errCntt.getBreakdown3());
                insertStmt.setObject(34, errCntt.getBreakdown2());
                insertStmt.setObject(35, errCntt.getBreakdown1());
                insertStmt.setObject(36, errCntt.getReportTct());
                insertStmt.setObject(37, errCntt.getReportTd());
                insertStmt.addBatch();
                if ((i + 1) % 100 == 0) {
                    logger.info("INSERTED " + i + " rows!");
                    insertStmt.executeBatch();
                }
            }
            insertStmt.executeBatch();
            logger.info("INSERTED " + errCntts.size() + " rows!");
            con.commit();
        } catch (Exception ex) {
            if (con != null) {
                con.rollback();
            }
            logger.error(ex.getMessage(), ex);
        } finally {
            if (deleteStmt != null) {
                try {
                    deleteStmt.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            if (insertStmt != null) {
                try {
                    insertStmt.close();
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

}
