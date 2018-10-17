/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.scheduler;

import com.viettel.framework.service.common.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author qlmvt_MinhHT1
 */
public class MyDbTask extends DbTask {

    public synchronized void insertData(String tableName, ArrayList<MyParameters> paramsList, Map columnMap) throws Exception {
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getConnection();
//            assureConnection();
//            this.setAutoCommit(false);
            String columnListSql = " ";
            String valueListSql = " ";

            boolean hasInsertTime = false;
            Set entries = columnMap.entrySet();
            Iterator it = entries.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                columnListSql = columnListSql + "," + entry.getKey();
                if (entry.getKey().toString().trim().equals("insert_time")) {
                    valueListSql = valueListSql + ",sysdate";
                    hasInsertTime = true;
                } else {
                    valueListSql = valueListSql + ",?";
                }
            }

            // update_time
            columnListSql += ",UPDATE_TIME ";
            valueListSql += ",trunc(sysdate) ";

            //Insert_time
            if (!hasInsertTime) {
                columnListSql += ",INSERT_TIME) ";
                valueListSql += ",sysdate) ";
            }

            columnListSql = columnListSql.replaceFirst(",", "(");
            valueListSql = valueListSql.replaceFirst(",", "(");

            String strSql = " Insert Into "
                    + tableName
                    + columnListSql
                    + " Values "
                    + valueListSql;

            preStmt = con.prepareStatement(strSql);
            boolean logInvalidColumn = false;
            for (MyParameters params : paramsList) {
                boolean catchException = false;
                int i = 0;
                Iterator it2 = entries.iterator();
                while (it2.hasNext()) {
                    i++;
                    Map.Entry entry = (Map.Entry) it2.next();
                    try {
                        Object value = params.getValue(entry.getKey().toString());
                        if (null != value) {
                            preStmt.setObject(i, value);
                        } else {
                            if (entry.getKey().toString().equalsIgnoreCase("FPDCH")
                                    || entry.getKey().toString().equalsIgnoreCase("SPDCH")
                                    || entry.getKey().toString().equalsIgnoreCase("CHGR")) {
                                preStmt.setNull(i, java.sql.Types.DOUBLE);
                            } else if (entry.getKey().toString().equalsIgnoreCase("LAC_ID")) {
                                preStmt.setNull(i, java.sql.Types.INTEGER);
                            } else {
                                preStmt.setNull(i, java.sql.Types.VARCHAR);
                            }

                        }
                    } catch (Exception e) {
                        if (!logInvalidColumn) {
                            e.printStackTrace();
                            catchException = true;
                        }
                        System.out.println(e.getMessage());
                    }
                }
//                System.out.println("--------------");
                if (catchException) {
                    logInvalidColumn = true;
                }
                preStmt.addBatch();
            }
            preStmt.executeBatch();
            this.commit(con);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
//            this.setAutoCommit(true);
            if (preStmt != null) {
                preStmt.close();
            }
            if(con != null) {
                con.close();
            }
        }
    }
}
