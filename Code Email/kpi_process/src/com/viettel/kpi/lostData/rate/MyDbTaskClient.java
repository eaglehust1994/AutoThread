/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.lostData.rate;

import com.viettel.kpi.client.database.DbClient;
import com.viettel.kpi.common.utils.DBUtil;
import com.viettel.kpi.common.utils.DateTimeUtils;
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
public class MyDbTaskClient extends DbClient {

    private static final Logger log = Logger.getLogger(MyDbTask.class);

    public Long getCountLogServer(Date updateTime) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Long countLogClient = null;
        try {
            log.info("Get Count Log Client");
            String partition = DateTimeUtils.format(updateTime, "yyyyMMdd");
            con = getConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT count(DISTINCT id) countLogClient FROM search_sub_log.Performance_log SUBPARTITION(DATA").append(partition).append("_ap00) ")
                    .append(" WHERE id IS NOT NULL");
            log.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                countLogClient = DBUtil.getLong(rs.getObject("countLogClient"));
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
                con.close();
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        return countLogClient;
    }

}
