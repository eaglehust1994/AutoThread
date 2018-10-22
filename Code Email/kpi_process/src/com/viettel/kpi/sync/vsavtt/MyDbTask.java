/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.sync.vsavtt;

import com.viettel.kpi.common.utils.DbTask;
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

    public synchronized void deleteUserVtt() {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele users_vtt");

            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE users_vtt");

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

    public synchronized void insertUserVtt(List<UsersVtt> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert users_vtt");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO users_vtt \n");
            sql.append(" (user_id, user_name, status, dept_id) \n");
            sql.append(" VALUES(?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            for (UsersVtt obj : lst) {
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

    public synchronized void deleteDepartmentVtt() {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Detele department_vtt");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE department_vtt");

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

    public synchronized void insertDepartmentVtt(List<DepartmentVtt> lst) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            logger.info("Insert department_vtt");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO department_vtt \n");
            sql.append(" (dept_id, parent_id, status, dept_name, full_dept_name) \n");
            sql.append(" VALUES(?,?,?,?,?)");
            pstmt = con.prepareStatement(sql.toString());
            for (DepartmentVtt obj : lst) {
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
                if (obj.getFullDeptName()!= null) {
                    pstmt.setString(5, obj.getFullDeptName());
                } else {
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
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

    public synchronized void insertDepartmentVttOften(int idCNVT) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            logger.info("Insert Department_Vtt_Often");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" INSERT INTO department_vtt_often \n");
            sql.append(" SELECT dept_id,parent_id,dept_name,LEVEL dept_level,full_dept_name \n");
            sql.append(" FROM department_vtt \n");
            sql.append(" START WITH  dept_id = ? \n");
            sql.append(" CONNECT BY PRIOR dept_id = parent_id");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, idCNVT);
            
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
