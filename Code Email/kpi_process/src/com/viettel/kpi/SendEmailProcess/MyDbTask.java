/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.SendEmailProcess;

import com.viettel.kpi.common.utils.DBUtil;
import com.viettel.kpi.common.utils.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author sonnh26
 */
public class MyDbTask extends DbTask {

    private static final Logger logger = Logger.getLogger(MyDbTask.class);

    protected ArrayList<EmailSendFrom> getEmail(String typeEmail) throws Exception {
        ResultSet rs = null;
        ArrayList<EmailSendFrom> lst = new ArrayList<EmailSendFrom>();
        EmailSendFrom emailBo = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        String[] ipSplit = typeEmail.split("\\|");
        String fuction_code = ipSplit[0];
        String email = ipSplit[1];
        String hour_id = ipSplit[2];
        try {
            logger.info("=========== Get Info Email Send ============");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
//            sql.append("select a.email_id, a.sender_email_code, a.content, a.is_file,a.subject, a.log_server_id, a.path, a.file_name, a.is_send_email,a.error_msg ");
//            sql.append("from common_email a where a.is_send_email =0 and a.sender_email_code =?");
            sql.append("select a.email_id, a.sender_email_code, a.content, a.is_file,a.subject, a.log_server_id,"
                    + "  a.path, a.file_name, a.is_send_email,a.error_msg,b.email,b.hour_run from common_email a,cata_email b "
                    + " where a.type_report=b.type_report and a.type=b.type and b.type=1 and a.is_send_email =0 "
                    + " and a.sender_email_code =? and b.hour_run=? and b.email =?");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, fuction_code);
            pstmt.setString(2, hour_id);
            pstmt.setString(3, email);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                emailBo = new EmailSendFrom();
                emailBo.setEmailId(DBUtil.getInteger(rs.getObject("email_id")));
                emailBo.setSenderEmailCode(DBUtil.getString(rs.getObject("sender_email_code")));
                emailBo.setContent(rs.getClob("content"));
                emailBo.setIsFile(DBUtil.getInteger(rs.getObject("is_file")));
                emailBo.setSubject(DBUtil.getString(rs.getObject("subject")));
                emailBo.setLogServerId(DBUtil.getString(rs.getObject("log_server_id")));
                emailBo.setPath(DBUtil.getString(rs.getObject("path")));
                emailBo.setFileName(DBUtil.getString(rs.getObject("file_name")));
                emailBo.setIsSendEmail(DBUtil.getInteger(rs.getObject("is_send_email")));
                emailBo.setErrorMsg(DBUtil.getString(rs.getObject("error_msg")));
                //hoanm1_add_start
                emailBo.setEmail(DBUtil.getString(rs.getObject("email")));
                emailBo.setHour_run(DBUtil.getInteger(rs.getObject("hour_run")));
                //hoanm1_add_end
                lst.add(emailBo);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
            throw ex;
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
//hoanm1_add_start

    protected List<String> getlstMail(EmailSendFrom emailSendFrom) throws Exception {
        ResultSet rs = null;
        List<String> lst = new ArrayList<String>();
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            logger.info("=========== Get Info Email Send ============");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("select a.email from cata_email a,common_map_email b where a.type_report=b.type_report and a.type=b.type "
                    + " and b.type=1 and b.function_code=? and a.hour_run=? and a.email=?");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, emailSendFrom.getSenderEmailCode());
            pstmt.setLong(2, emailSendFrom.getHour_run());
            pstmt.setString(3, emailSendFrom.getEmail());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String columnCode = rs.getString("email").trim();
                lst.add(columnCode);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
            throw ex;
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
    //hoanm1_add_end

//    public List<String> getlstMail() throws Exception {
//        List<String> lst = new ArrayList<String>();
//        PreparedStatement preStmt = null;
//        ResultSet rs = null;
//        String strSql = " SELECT email from cata_email";
//        Connection connection = null;
//        try {
//            connection = getConnection();
//            preStmt = connection.prepareStatement(strSql);
//            rs = preStmt.executeQuery();
//            while (rs.next()) {
//                String columnCode = rs.getString("email").trim();
//                lst.add(columnCode);
//            }
//        } catch (Exception ex) {
//            throw ex;
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (preStmt != null) {
//                    preStmt.close();
//                }
//            } catch (Exception ex) {
//            }
//            try {
//                if (connection != null) {
//                    connection.close();
//                    connection = null;
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                throw ex;
//            }
//        }
//
//        return lst;
//    }
    protected ArrayList<String> getEmailList(String typeEmail) throws Exception {
        ResultSet rs = null;
        ArrayList<String> lst = new ArrayList<String>();
        EmailSendFrom emailBo = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            logger.info("===========Get Email List ============");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("select a.email ");
            sql.append("from cata_alarm_sms_email a where a.type_sms_email = ?");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, typeEmail);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lst.add(DBUtil.getString(rs.getObject("email")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
            throw ex;
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

    protected MscServer getLogServerID(String logServerID) throws Exception {
        ResultSet rs = null;
        MscServer msc = new MscServer();
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            logger.info("===========Get Server File ============");
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append("select a.ip , a.protocol , a.username , a.password , a.port ");
            sql.append("from common_log_server a where upper(a.log_server_name) =?");
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, logServerID.trim().toUpperCase());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                msc.setIp(DBUtil.getString(rs.getObject("ip")));
                msc.setProtocol(DBUtil.getString(rs.getObject("protocol")));
                msc.setUserName(DBUtil.getString(rs.getObject("username")));
                msc.setPassword(DBUtil.getString(rs.getObject("password")));
                msc.setPort(DBUtil.getInteger(rs.getObject("port")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex.getMessage(), ex);
            throw ex;
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
        return msc;
    }

    public void updateEmail(int emailID, String errMSG, Date startTime) throws Exception {
        PreparedStatement preStmt = null;
        Connection con = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" update common_email set is_send_email = 1 , error_msg = ? , start_time = ? where email_id=? ");
            preStmt = con.prepareStatement(sql.toString());
            preStmt.setString(1, errMSG);
            preStmt.setTimestamp(2, new Timestamp(startTime.getTime()));
            preStmt.setInt(3, emailID);
            preStmt.execute();
            this.commit(con);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (preStmt != null) {
                    preStmt.close();
                }
            } catch (Exception e) {
            }
            close(con);

        }
    }
}
