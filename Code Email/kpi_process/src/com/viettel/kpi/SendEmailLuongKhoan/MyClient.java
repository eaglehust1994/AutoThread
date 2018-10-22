/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.SendEmailLuongKhoan;

import com.viettel.kpi.common.utils.DBUtil;
import com.viettel.kpi.common.utils.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyClient extends DbTask {

    private static Logger logger = Logger.getLogger(DbTask.class);

    public Map<String, List<InformationIsFileForm>> getInfomationIsFile() throws Exception {
        ResultSet rs = null;
        logger.info("===getInfomationIsFile===");

        List<InformationIsFileForm> lstEmailConfig = new ArrayList<InformationIsFileForm>();
        Map<String, List<InformationIsFileForm>> mapInfomationConfig = new HashMap<String, List<InformationIsFileForm>>();
        InformationIsFileForm emailConfigForm = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnectionPool();
            logger.info("con: " + con);
            StringBuilder sql = new StringBuilder();
//            sql.append(" SELECT function_code,type_sms_email_code,body_email,fomat_body_email,");
//            sql.append(" is_file,subject,fomat_subject,path_file,sever_type_code,fomat_path_file,");
//            sql.append(" file_name,fomat_file_name,is_hour_run,end_time_run,is_body_email");
//            sql.append(" FROM common_map_email a");
//            sql.append(" where end_time_run= trunc(sysdate-1) and type=1");
//            hoanm1_add_09062016
            sql.append(" SELECT a.function_code,a.type_sms_email_code,a.body_email,a.fomat_body_email,a.is_file,"
                    + " a.subject,a.fomat_subject,a.path_file,a.sever_type_code,a.fomat_path_file,"
                    + " a.file_name,a.fomat_file_name,a.is_hour_run,a.end_time_run,a.is_body_email,"
                    + " a.type_report,a.type, b.email,b.hour_run,b.day_run FROM common_map_email a,"
                    + " cata_email b where a.type_report=b.type_report and a.type=b.type and b.type=1");
//            hoanm1_add_09062016
            pstmt = con.prepareStatement(sql.toString());
            logger.info("pstmt: " + pstmt);
            rs = pstmt.executeQuery();
            logger.info("rs: " + rs);
            int count = 1;
            while (rs.next()) {
                if (count != 1) {
                    String typeSmsCode = DBUtil.getString(rs.getObject("type_sms_email_code"));
                    if (mapInfomationConfig.get(typeSmsCode) == null) {
                        lstEmailConfig = new ArrayList<InformationIsFileForm>();
                    } else {
                        lstEmailConfig = mapInfomationConfig.get(typeSmsCode);
                    }
                }
                emailConfigForm = new InformationIsFileForm();
                emailConfigForm.setFunctionCode(DBUtil.getString(rs.getObject("function_code")));
                emailConfigForm.setTypeSmsEmailCode(DBUtil.getString(rs.getObject("type_sms_email_code")));
                emailConfigForm.setBodyEmail(DBUtil.getString(rs.getObject("body_email")));
                emailConfigForm.setFomatBodyEmail(DBUtil.getString(rs.getObject("fomat_body_email")));
                emailConfigForm.setIsFile(DBUtil.getInteger(rs.getObject("is_file")));
                emailConfigForm.setSubject(DBUtil.getString(rs.getObject("subject")));
                emailConfigForm.setFomatSubject(DBUtil.getString(rs.getObject("fomat_subject")));
                emailConfigForm.setPathFile(DBUtil.getString(rs.getObject("path_file")));
                emailConfigForm.setFomatPathFile(DBUtil.getString(rs.getObject("fomat_path_file")));
                emailConfigForm.setIsBodyEmail(DBUtil.getString(rs.getObject("is_body_email")));
                emailConfigForm.setLogServerId(DBUtil.getString(rs.getObject("sever_type_code")));
                emailConfigForm.setFileName(DBUtil.getString(rs.getObject("file_name")));
                emailConfigForm.setFomatFileName(DBUtil.getString(rs.getObject("fomat_file_name")));
                emailConfigForm.setIsHourRun(DBUtil.getInteger(rs.getObject("is_hour_run")));
                emailConfigForm.setEndTimeRun(rs.getDate("end_time_run"));
                //            hoanm1_add_09062016
                emailConfigForm.setType_report(DBUtil.getString(rs.getObject("type_report")));
                emailConfigForm.setType(DBUtil.getString(rs.getObject("type")));
                emailConfigForm.setEmail(DBUtil.getString(rs.getObject("email")));
                emailConfigForm.setHour_run(DBUtil.getInteger(rs.getObject("hour_run")));
                emailConfigForm.setDay_run(DBUtil.getInteger(rs.getObject("day_run")));
                //            hoanm1_add_09062016
                lstEmailConfig.add(emailConfigForm);
                mapInfomationConfig.put(emailConfigForm.getFunctionCode() + emailConfigForm.getEmail() + emailConfigForm.getHour_run(), lstEmailConfig);
                count++;
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
        return mapInfomationConfig;
    }

    public List<EmailConfigForm> getFunction(String functionCode, String sheetName) throws Exception {
        ResultSet rs = null;
        System.out.println("===GetData===");

        List<EmailConfigForm> lstEmailConfig = new ArrayList<EmailConfigForm>();
        EmailConfigForm emailConfigForm = null;
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" select b.function_code, b.query_string_sql,b.sheet_name,c.function_title,");
            sql.append(" a.column_code,a.column_name,a.column_style,column_index,a.column_name_l1,a.column_name_l2");
            sql.append(" from ADM_FUNC_QUERY b ,ADM_FUNC c,adm_func_query_column a");
            sql.append(" where a.function_code=b.function_code");
            sql.append(" and a.function_code=c.function_code");
            sql.append(" and a.query_id = b.query_id");
            sql.append(" and a.is_display=1");
            sql.append(" and upper(b.function_code)= ?");
            if (sheetName != null) {
                sql.append(" and b.sheet_name= ?");
            }
            sql.append(" order by a.column_index");

            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, functionCode.toUpperCase().trim());
            if (sheetName != null) {
                pstmt.setString(2, sheetName.trim());
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                emailConfigForm = new EmailConfigForm();
                emailConfigForm.setFunctionCode(DBUtil.getString(rs.getObject("function_code")));
                emailConfigForm.setFunctionTitle(DBUtil.getString(rs.getObject("function_title")));
                emailConfigForm.setQueryStringsql(rs.getClob("query_string_sql"));
                emailConfigForm.setSheetName(DBUtil.getString(rs.getObject("sheet_name")));
                emailConfigForm.setColumnCode(DBUtil.getString(rs.getObject("column_code")));
                emailConfigForm.setColumnName(DBUtil.getString(rs.getObject("column_name")));
                emailConfigForm.setColumnStyle(DBUtil.getString(rs.getObject("column_style")));
                emailConfigForm.setColumnIndex(DBUtil.getInteger(rs.getObject("column_index")));
//                hoanm1_add_start
                emailConfigForm.setColumnNameL1(DBUtil.getString(rs.getObject("column_name_l1")));
                emailConfigForm.setColumnNameL2(DBUtil.getString(rs.getObject("column_name_l2")));
//                hoanm1_add_end
                lstEmailConfig.add(emailConfigForm);
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
        return lstEmailConfig;
    }

    public List<String> getSheetName(String functionCode) throws Exception {
        ResultSet rs = null;
        System.out.println("===GetData===");

        List<String> lst = new ArrayList<String>();
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" select b.sheet_name from ADM_FUNC_QUERY b");
            sql.append(" where upper(b.function_code)= ?");
            sql.append(" ORDER BY query_index");

            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, functionCode.toUpperCase().trim());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lst.add(DBUtil.getString(rs.getObject("sheet_name")));
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

//    public String getSenderEmail(String senderEmail) throws Exception {
//        ResultSet rs = null;
//        String emailConfig = "";
//        System.out.println("===getSenderEmail===");
//
//        PreparedStatement pstmt = null;
//        Connection con = null;
//        try {
//            con = getConnectionPool();
//            StringBuilder sql = new StringBuilder();
//            sql.append(" select sender_email_code from common_email where upper(sender_email_code)=?");
//
//            pstmt = con.prepareStatement(sql.toString());
//            pstmt.setString(1, senderEmail.toUpperCase().trim());
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                emailConfig = DBUtil.getString(rs.getObject("sender_email_code"));
//
//            }
//        } catch (Exception ex) {
//            logger.error(ex.getMessage(), ex);
//        } finally {
//            if (rs != null) {
//                rs.close();
//            }
//            if (pstmt != null) {
//                try {
//                    pstmt.close();
//                } catch (Exception ex) {
//                    logger.error(ex.getMessage(), ex);
//                }
//            }
//            try {
//                close(con);
//            } catch (Exception ex) {
//                logger.error(ex.getMessage(), ex);
//            }
//        }
//        return emailConfig;
//    }
    //hoanm1_add
    public String getSenderEmail(String senderEmail) throws Exception {
        ResultSet rs = null;
        String emailConfig = "";
        System.out.println("===getSenderEmail===");

        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" select sender_email_code from common_email where upper(sender_email_code)=?");

            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, senderEmail.toUpperCase().trim());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                emailConfig = DBUtil.getString(rs.getObject("sender_email_code"));

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
        return emailConfig;
    }
//hoanm1_add

    public List<String> getEmail(String senderEmail) throws Exception {
        ResultSet rs = null;
        String email = "";
        System.out.println("===getSenderEmail===");
        List<String> lstMail = new ArrayList<String>();
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnectionPool();
            StringBuilder sql = new StringBuilder();
            sql.append(" select email from cata_Alarm_sms_Email where upper(type_sms_email)=?");

            pstmt = con.prepareStatement(sql.toString());
            pstmt.setString(1, senderEmail.toUpperCase().trim());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                email = DBUtil.getString(rs.getObject("email"));
                lstMail.add(email);
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
        return lstMail;
    }

    public synchronized void insertDataWsToDb(ObjectEmail objectMail, Logger log)
            throws Exception {
        if (objectMail != null) {
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnectionPool();
                StringBuilder sql = new StringBuilder(" INSERT INTO ");
                sql.append(" common_Email");
                sql.append(" (email_id, sender_email_code,content, "
                        + " is_file,subject,log_server_id,path,file_name,is_send_email,error_msg,start_time)");
                sql.append(" VALUES (common_Email_seq.nextval,?,?,?,?,?,?,?,0,null,sysdate)");

                pstmt = con.prepareStatement(sql.toString());

                if (objectMail.getSenderEmailCode() != null) {
                    try {

                        pstmt.setString(1, objectMail.getSenderEmailCode());
                        pstmt.setString(2, objectMail.getContent());
                        // pstmt.setString(3, objectMail.getEmail());
                        pstmt.setInt(3, objectMail.getIsFile());
                        pstmt.setString(4, objectMail.getSubject());
                        pstmt.setString(5, objectMail.getLogServerId());
                        pstmt.setString(6, objectMail.getPath());
                        pstmt.setString(7, objectMail.getFileName());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pstmt.execute();
                    this.commit(con);
                    log.info("INSERT INTO DB common_Email SUCCESS!");
                }

            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                log.info("INSERT INTO DB common_Email FAIL!");
            } finally {
                close(con);
                log.info("INSERT INTO DB common_Email FINISH!");
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                } catch (Exception e) {
                }
            }
        }

    }

    public void updateCommonEmail(ObjectEmail objectMail, Logger logger)
            throws Exception {
        if ((objectMail != null)) {
            String sql = " UPDATE common_email SET content = ?,is_file=?"
                    + " ,subject =?,log_server_id=?,path=?,file_name=?,is_send_email=?,start_time=sysdate"
                    + " where upper(sender_email_code) = ? ";
            PreparedStatement pstmt = null;
            Connection con = null;
            try {
                con = getConnectionPool();
                pstmt = con.prepareStatement(sql);
                try {
                    pstmt.setString(1, objectMail.getContent().trim());
                    pstmt.setInt(2, objectMail.getIsFile());
                    pstmt.setString(3, objectMail.getSubject());
                    pstmt.setString(4, objectMail.getLogServerId());
                    pstmt.setString(5, objectMail.getPath());
                    pstmt.setString(6, objectMail.getFileName());
                    pstmt.setInt(7, 0);
                    pstmt.setString(8, objectMail.getSenderEmailCode().toUpperCase());

                } catch (Exception ex) {
                    logger.info("UPDATE INTO common_Email FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
                pstmt.execute();
                this.commit(con);
                logger.info("UPDATE INTO DB common_Email SUCCESS!");
            } catch (Exception ex) {
                logger.info("UPDATE INTO DB common_Email FAIL!");
                logger.error(ex.getMessage(), ex);
            } finally {
                if (pstmt != null) {
                    try {
                        pstmt.close();
                        logger.info("UPDATE INTO DB common_Email FINISH!");
                    } catch (Exception ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                }
                close(con);
            }
        } else {
            logger.info("UPDATE INTO DB common_Email FINISH!");
        }
    }

    public void updateCommonMapEmail(String typeSmsEmailCode, List<InformationIsFileForm> lstUpdate, Logger logger)
            throws Exception {
        String sql = " UPDATE common_map_email SET end_time_run = trunc(sysdate)"
                + " where upper(function_code)=?";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnectionPool();
            pstmt = con.prepareStatement(sql);
            int size = lstUpdate.size();
            int group = 0;
            for (InformationIsFileForm obj : lstUpdate) {
                int i = 1;
                try {
                    group++;
//                    pstmt.setString(i++, typeSmsEmailCode.trim().toUpperCase());
                    pstmt.setString(i++, obj.getFunctionCode().trim().toUpperCase());
                    pstmt.addBatch();
                    if (group == 50) {
                        logger.info("UPDATED " + group + "/" + size);
                        group = 0;
                        pstmt.executeBatch();
                        this.commit(con);
                    }
                } catch (Exception ex) {
                    logger.info("UPDATE INTO common_map_email FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }
            pstmt.executeBatch();
            this.commit(con);
            logger.info("UPDATE INTO DB common_map_email SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE INTO DB common_map_email FAIL!");
            logger.error(ex.getMessage(), ex);
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                    logger.info("UPDATE INTO DB common_map_email FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            close(con);
        }

    }

    protected MscServer getLogServerID(String logServerID, Logger logger) throws Exception {
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
            logger.error(ex.getMessage(), ex);
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
}
