/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.sync.vsavtn;

import com.viettel.kpi.sync.vsavtt.*;
import com.viettel.kpi.common.utils.DbTask;
import com.viettel.vsaadmin.service.DepartmentBO;
import com.viettel.vsaadmin.service.UserInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyDbTask extends DbTask {
    
    private static final Logger logger = Logger.getLogger(MyDbTask.class);
    
    public synchronized void syncUserVtn(List<UserInfo> lstUser) {
        Connection con = null;
        PreparedStatement pstmt = null;
//        int i = 0;
        try {
            logger.info("Sync users_vtn");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" MERGE INTO users_vtn a \n");
            sql.append(" USING (SELECT ? user_id,? user_name,? status,? dept_id FROM dual) b \n");
            sql.append(" ON (a.user_id = b.user_id ) \n");
            sql.append(" WHEN MATCHED THEN \n");
            sql.append(" UPDATE SET a.user_name = b.user_name,a.status = b.status,a.dept_id = b.dept_id \n");
            sql.append(" WHEN NOT MATCHED THEN \n");
            sql.append(" INSERT (a.user_id, a.user_name, a.status, a.dept_id) \n");
            sql.append(" VALUES (b.user_id, b.user_name, b.status, b.dept_id) \n");
            pstmt = con.prepareStatement(sql.toString());
            for (UserInfo obj : lstUser) {
                if (obj.getUserId() != null) {
                    pstmt.setLong(1, obj.getUserId());
                } else {
                    pstmt.setNull(1, java.sql.Types.NUMERIC);
                }
                if (obj.getUserName() != null) {
                    pstmt.setString(2, obj.getUserName());
                } else {
                    pstmt.setNull(2, java.sql.Types.VARCHAR);
                }
                if (obj.getStatus() != null) {
                    pstmt.setLong(3, obj.getStatus());
                } else {
                    pstmt.setNull(3, java.sql.Types.NUMERIC);
                }
                if (obj.getDeptId() != null) {
                    pstmt.setLong(4, obj.getDeptId());
                } else {
                    pstmt.setNull(4, java.sql.Types.NUMERIC);
                }
//                i++;
                pstmt.execute();
            }
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
    
    public synchronized void syncDepartmentVtn(List<DepartmentBO> lstDept) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Sync department_vtn");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" MERGE INTO department_vtn a \n");
            sql.append(" USING (SELECT ? dept_id,? parent_id,? status,? dept_name,? full_dept_name FROM dual) b \n");
            sql.append(" ON (a.dept_id = b.dept_id ) \n");
            sql.append(" WHEN MATCHED THEN \n");
            sql.append(" UPDATE SET a.parent_id = b.parent_id,a.status = b.status, \n");
            sql.append(" a.dept_name = b.dept_name,a.full_dept_name = b.full_dept_name \n");
            sql.append(" WHEN NOT MATCHED THEN \n");
            sql.append(" INSERT (a.dept_id, a.parent_id, a.status, a.dept_name, a.full_dept_name) \n");
            sql.append(" VALUES (b.dept_id, b.parent_id, b.status, b.dept_name, b.full_dept_name) \n");
            pstmt = con.prepareStatement(sql.toString());
            for (DepartmentBO obj : lstDept) {
                if (obj.getDeptId() != null) {
                    pstmt.setLong(1, obj.getDeptId());
                } else {
                    pstmt.setNull(1, java.sql.Types.NUMERIC);
                }
                if (obj.getParentId() != null) {
                    pstmt.setLong(2, obj.getParentId());
                } else {
                    pstmt.setNull(2, java.sql.Types.NUMERIC);
                }
                if (obj.getStatus() != null) {
                    pstmt.setLong(3, obj.getStatus());
                } else {
                    pstmt.setNull(3, java.sql.Types.NUMERIC);
                }
                if (obj.getDeptName() != null) {
                    pstmt.setString(4, obj.getDeptName());
                } else {
                    pstmt.setNull(4, java.sql.Types.VARCHAR);
                }
                if (obj.getFullDeptName() != null) {
                    pstmt.setString(5, obj.getFullDeptName());
                } else {
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                }
                pstmt.execute();
            }
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
    
    public synchronized List<DepartmentVttOften> getDepartmentVttOften() throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<DepartmentVttOften> lst = new ArrayList<DepartmentVttOften>();
        DepartmentVttOften obj = null;
        try {
            logger.info("Get Department_Vtt_Often");
            con = getConnectionPool();
            logger.info("con " + con);
            
            StringBuilder sql = new StringBuilder();
            
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);
            
            rs = pstmt.executeQuery();
            logger.info("rs " + rs);
            while (rs.next()) {
                obj = new DepartmentVttOften();
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
    
    public synchronized void deleteDepartmentVttOften() {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele department_vtt_often");
            con = getConnectionPool();
            
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE department_vtt_often");
            
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
    
    public synchronized void insertDepartmentVttOften(List<DepartmentVttOften> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert Department_Vtt_Often");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            pstmt = con.prepareStatement(sql.toString());
            for (DepartmentVttOften obj : lst) {
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
    
    public synchronized void insertDepartmentVtnOften(int idTCT) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Insert Department_vtn_often");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO department_vtn_often \n");
            sql.append(" SELECT dept_id,parent_id,dept_name,LEVEL dept_level,full_dept_name \n");
            sql.append(" FROM department_vtn \n");
            sql.append(" START WITH  dept_id = ? \n");
            sql.append(" CONNECT BY PRIOR dept_id = parent_id");
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt: " + pstmt);
            pstmt.setLong(1, idTCT);
            
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
