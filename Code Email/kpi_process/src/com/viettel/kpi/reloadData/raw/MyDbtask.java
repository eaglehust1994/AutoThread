/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.reloadData.raw;

import com.viettel.kpi.common.utils.DBUtil;
import com.viettel.kpi.common.utils.DbTask;
import com.viettel.kpi.getDataSql.common.CommonMapQueryLogServer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author os_sonnh
 */
public class MyDbtask extends DbTask {

    private static final Logger logger = Logger.getLogger(MyDbtask.class);

    public synchronized List<CommonMapQueryLogServer> getQueryList(Long logServerId, Long queryId) throws Exception {
        List<CommonMapQueryLogServer> queryList = new ArrayList<CommonMapQueryLogServer>();
        CommonMapQueryLogServer obj = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        logger.info("get query list");
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.*,f.log_server_name from");
        sql.append(" COMMON_MAP_QUERY_LOG_SERVER a");
        sql.append(" INNER JOIN common_log_server f");
        sql.append(" ON a.log_server_id = f.log_server_id");
        sql.append(" and f.status = 1");
        sql.append(" where a.query_id =? and a.log_server_id = ?");
        try {
            con = getConnectionPool();
            logger.info("get query list con " + con);
            preStmt = con.prepareStatement(sql.toString());
            logger.info("get query list preStmt " + preStmt);
            preStmt.setLong(1, queryId);
            preStmt.setLong(2, logServerId);

            rs = preStmt.executeQuery();
            logger.info("get query list rs " + rs);
            while (rs.next()) {
                obj = new CommonMapQueryLogServer();
                obj.setType(DBUtil.getString(rs.getObject("TYPE")));
                obj.setQueryId(DBUtil.getLong(rs.getObject("QUERY_ID")));
                obj.setLogServerId(DBUtil.getLong(rs.getObject("LOG_SERVER_ID")));
                obj.setLogServerName(DBUtil.getString(rs.getObject("log_server_name")));
                obj.setHourRunInDay(DBUtil.getInteger(rs.getObject("HOUR_RUN_IN_DAY")));
                obj.setIntervalHour(DBUtil.getInteger(rs.getObject("INTERVAL_HOUR")));
                obj.setEndTimeData(rs.getDate("END_TIME_DATA"));
                obj.setServiceTypeId(DBUtil.getInteger(rs.getObject("SERVICE_TYPE_ID")));
                obj.setModuleId(DBUtil.getInteger(rs.getObject("MODULE_ID")));
                obj.setEndTimeRun(rs.getDate("END_TIME_RUN"));
                obj.setTimeReturnNow(DBUtil.getInteger(rs.getObject("TIME_RETURN_NOW")));
                obj.setIsRunning(DBUtil.getInteger(rs.getObject("IS_RUNNING") == null ? 0 : rs.getObject("IS_RUNNING")));
                obj.setRunStepNext(DBUtil.getInteger(rs.getObject("RUN_STEP_NEXT") == null ? 0 : rs.getObject("RUN_STEP_NEXT")));
                queryList.add(obj);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (preStmt != null) {
                    preStmt.close();
                }
            } catch (Exception e) {
            }
            close(con);

        }

        return queryList;
    }
}
