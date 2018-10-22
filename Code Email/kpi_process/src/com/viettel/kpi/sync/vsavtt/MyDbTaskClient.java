/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.sync.vsavtt;

import com.viettel.kpi.client.database.DbClient;
import com.viettel.kpi.common.utils.DBUtil;
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
public class MyDbTaskClient extends DbClient {

    private static final Logger logger = Logger.getLogger(MyDbTaskClient.class);

    public synchronized List<UsersVtt> getUserVtt() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<UsersVtt> lst = new ArrayList<UsersVtt>();
        UsersVtt obj = null;
        try {
            logger.info("Get User from vsa vtt");

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.user_id ,a.user_name,a.status,a.dept_id\n");
            sql.append("FROM bccs_vsa_v3.users a \n");

            pstmt = connection.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);

            rs = pstmt.executeQuery();
            logger.info("rs " + rs);
            while (rs.next()) {
                obj = new UsersVtt();
                obj.setUserId(DBUtil.getLong(rs.getObject("user_id")));
                obj.setUserName(DBUtil.getString(rs.getObject("user_name")));
                obj.setStatus(DBUtil.getLong(rs.getObject("status")));
                obj.setDeptId(DBUtil.getLong(rs.getObject("dept_id")));
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
//                close(connection);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return lst;
    }

    public synchronized List<DepartmentVtt> getDepartmentVtt() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<DepartmentVtt> lst = new ArrayList<DepartmentVtt>();
        DepartmentVtt obj = null;
        try {
            logger.info("Get Department from vsa vtt");

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT a.dept_id ,a.parent_id,a.status,a.dept_name,a.full_dept_name\n");
            sql.append("FROM bccs_vsa_v3.department a \n");

            pstmt = connection.prepareStatement(sql.toString());
            logger.info("pstmt " + pstmt);

            rs = pstmt.executeQuery();
            logger.info("rs " + rs);
            while (rs.next()) {
                obj = new DepartmentVtt();
                obj.setDeptId(DBUtil.getLong(rs.getObject("dept_id")));
                obj.setParentId(DBUtil.getLong(rs.getObject("parent_id")));
                obj.setStatus(DBUtil.getLong(rs.getObject("status")));
                obj.setDeptName(DBUtil.getString(rs.getObject("dept_name")));
                obj.setFullDeptName(DBUtil.getString(rs.getObject("full_dept_name")));
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
//                close(connection);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        return lst;
    }
}
