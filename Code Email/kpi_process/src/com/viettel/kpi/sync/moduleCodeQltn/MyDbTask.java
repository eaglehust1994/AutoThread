/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.sync.moduleCodeQltn;

import com.viettel.QltnGateproService.VApplicationDetailBO;
import com.viettel.kpi.common.utils.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyDbTask extends DbTask {

    private static final Logger logger = Logger.getLogger(MyDbTask.class);

    public synchronized void insertAppDetail(List<VApplicationDetailBO> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO cata_app_module_qltn \n");
            sql.append(" (app_code, app_name, module_code, module_name, module_id)\n");
            sql.append(" VALUES(?,?,?,?,?)");

//            logger.info(sql.toString());
            pstmt = con.prepareStatement(sql.toString());
            for (VApplicationDetailBO bo : lst) {

                //<editor-fold defaultstate="collapsed" desc="set param">
                if (bo.getAppGroupCode()!= null) {
                    pstmt.setString(1, bo.getAppGroupCode());
                } else {
                    pstmt.setNull(1, java.sql.Types.VARCHAR);
                }
                if (bo.getAppGroupName()!= null) {
                    pstmt.setString(2, bo.getAppGroupName());
                } else {
                    pstmt.setNull(2, java.sql.Types.VARCHAR);
                }
                if (bo.getAppCode()!= null) {
                    pstmt.setString(3, bo.getAppCode());
                } else {
                    pstmt.setNull(3, java.sql.Types.VARCHAR);
                }
                if (bo.getAppName()!= null) {
                    pstmt.setString(4, bo.getAppName());
                } else {
                    pstmt.setNull(4, java.sql.Types.VARCHAR);
                }
                if (bo.getAppId()!= null) {
                    pstmt.setLong(5, bo.getAppId());
                } else {
                    pstmt.setNull(5, java.sql.Types.NUMERIC);
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

    public synchronized void deleteAppDetail() {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE cata_app_module_qltn ");
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
