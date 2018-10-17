/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.qll.task;

import com.google.common.collect.Lists;
import com.viettel.framework.service.common.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 */
public class MyDbSql extends DbTask {
    
    
    // lay du lieu moi bang TaskGroup
    public List<TaskGroupBO> getNewInfoTaskGroup ()throws Exception{
        
        List<TaskGroupBO> lstNewTaskGroup = Lists.newArrayList();
        PreparedStatement preStmt = null;
        ResultSet rs = null; 
        StringBuilder sql = new StringBuilder(" select t1.TASK_GROUP_ID taskGroupId ");
        sql.append(" ,t1.TASK_GROUP_NAME taskGroupName ");
        sql.append(" ,t1.TASK_GROUP_CONTENT taskGroupContent");
        sql.append(" ,t1.DEPARTMENT department");
        sql.append(" ,t1.PERIODIC periodic ");
        sql.append(" ,t1.END_TASK_GROUP endTaskGroup ");
        sql.append(" ,t1.START_TASK_GROUP startTaskGroup ");
        sql.append(" ,t1.WARNING_TASK_GROUP warningTaskGroup ");
        sql.append(" ,t1.WARNING_CYCLE warningCycle ");
        sql.append(" ,t1.WARNING_EMAIL warningEmail ");
        sql.append(" ,t1.DEPARTMENT_ID departmentId ");
        sql.append(" from TASK_GROUP t1 join TASK t2 on t1.TASK_GROUP_ID!=t2.TASK_ID ");
        
        Connection con = null;
        try {
            con = getConnection();
            preStmt = con.prepareStatement(sql.toString());
            rs = preStmt.executeQuery();
            while (rs.next()) {
                TaskGroupBO obj = new TaskGroupBO();
                obj.setTaskGroupId(rs.getLong("taskGroupId"));
                obj.setTaskGroupName(rs.getNString("taskGroupName"));
                obj.setTaskGroupContent(rs.getNString("taskGroupContent"));
                obj.setDepartment(rs.getNString("department"));
                obj.setPeriodic(rs.getLong("periodic"));
                obj.setEndTaskGroup(rs.getLong("endTaskGroup"));
                obj.setStartTaskGroup(rs.getLong("startTaskGroup"));
                obj.setWarningTaskGroup(rs.getLong("warningTaskGroup"));
                obj.setWarningCycle(rs.getLong("warningCycle"));
                obj.setWarningEmail(rs.getString("columnLabel"));
                obj.setDepartmentId(rs.getLong("departmentId"));
                lstNewTaskGroup.add(obj);
            }
            
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
            } catch (SQLException ex) {
            }
            try {
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (SQLException ex) {
                throw ex;
            }
        }
        return lstNewTaskGroup;
        
    }
    
    
    //lay du lieu TaskGroup theo Task
    
     public List<TaskGroupBO> getOldInfoTaskGroup ()throws Exception{
        
        List<TaskGroupBO> lstOldTaskGroup = Lists.newArrayList();
        PreparedStatement preStmt = null;
        ResultSet rs = null; 
        StringBuilder sql = new StringBuilder(" select t1.TASK_GROUP_ID taskGroupId ");
        sql.append(" ,t1.TASK_GROUP_NAME taskGroupName ");
        sql.append(" ,t1.TASK_GROUP_CONTENT taskGroupContent");
        sql.append(" ,t1.DEPARTMENT department");
        sql.append(" ,t1.PERIODIC periodic ");
        sql.append(" ,t1.END_TASK_GROUP endTaskGroup ");
        sql.append(" ,t1.START_TASK_GROUP startTaskGroup ");
        sql.append(" ,t1.WARNING_TASK_GROUP warningTaskGroup ");
        sql.append(" ,t1.WARNING_CYCLE warningCycle ");
        sql.append(" ,t1.WARNING_EMAIL warningEmail ");
        sql.append(" ,t1.DEPARTMENT_ID departmentId ");
        sql.append(" ,t2.STATUS status ");
        sql.append(" ,t2.END_TIME endTime ");
        sql.append(" ,t2.START_TIME startTime ");
        sql.append(" ,t2.CREATE_TASK_CYCLE createTaskCycle ");
        sql.append(" ,t2.NOTE_TASK noteTask ");
        sql.append(" from TASK_GROUP t1 join TASK t2 on t1.TASK_GROUP_ID=t2.ID_TASK_GROUP ");
        
        Connection con = null;
        try {
            con = getConnection();
            preStmt = con.prepareStatement(sql.toString());
            rs = preStmt.executeQuery();
            while (rs.next()) {
                TaskGroupBO obj = new TaskGroupBO();
                obj.setTaskGroupId(rs.getLong("taskGroupId"));
                obj.setTaskGroupName(rs.getNString("taskGroupName"));
                obj.setTaskGroupContent(rs.getNString("taskGroupContent"));
                obj.setDepartment(rs.getNString("department"));
                obj.setPeriodic(rs.getLong("periodic"));
                obj.setEndTaskGroup(rs.getLong("endTaskGroup"));
                obj.setStartTaskGroup(rs.getLong("startTaskGroup"));
                obj.setWarningTaskGroup(rs.getLong("warningTaskGroup"));
                obj.setWarningCycle(rs.getLong("warningCycle"));
                obj.setWarningEmail(rs.getString("columnLabel"));
                obj.setDepartmentId(rs.getLong("departmentId"));
                obj.setStatus(rs.getLong("status"));
                obj.setEndTime(rs.getDate("endTime"));
                obj.setStartTime(rs.getDate("startTime"));
                obj.setCreateTaskCycle(rs.getLong("createTaskCycle"));
                obj.setNoteTask(rs.getNString("noteTask"));
                lstOldTaskGroup.add(obj);
            }
            
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
            } catch (SQLException ex) {
            }
            try {
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (SQLException ex) {
                throw ex;
            }
        }
        return lstOldTaskGroup;
        
    }
    
  
    
    public synchronized void insertTask (List<TaskBO> lstObj,Logger logger){
        StringBuilder sql = new StringBuilder("");
        sql.append(" insert into TASK ( TASK_ID");
        sql.append(" ,ID_TASK_GROUP");
        sql.append(" ,STATUS");
        sql.append(" ,END_TIME");
        sql.append(" ,START_TIME");
        sql.append(" ,CREATE_TASK_CYCLE");
        sql.append(" ,NOTE_TASK)");
        sql.append(" VALUES(TASK_SEQ.nextval,?,?,?,?,?,?,?)");
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql.toString());
            
            
        } catch (Exception e) {
        }finally {
            if (con != null) {
                try {
                    close(con);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                    logger.info("MER_HISTORY INSERT INTO DB FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        
        
    }
}
