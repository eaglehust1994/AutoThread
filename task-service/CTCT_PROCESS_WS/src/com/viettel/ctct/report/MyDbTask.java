/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.report;

import com.viettel.framework.service.common.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author pm1_os38
 */
public class MyDbTask extends DbTask {

   public ArrayList<ReportBO> getListReport(String backDateReport) throws Exception {
        ArrayList<ReportBO> lstDebt = new ArrayList<ReportBO>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String strSql = "with tbl as( select a.GOODS_ID,a.GOODS_CODE,a.GOODS_NAME,a.GOODS_STATE, a.STOCK_ID,a.GOODS_UNIT_NAME,\n"
                + " sum(a.AMOUNT_MER)  amount,sum(a.TOTAL_MONEY) totalMoney, \n"
                + " sum((select sum(AMOUNT_TOTAL) from STOCK_DAILY_IMPORT_EXPORT b  where TRUNC(b.ie_date) = ? and a.stock_id=b.stock_id and STOCK_TRANS_TYPE=1 and a.GOODS_ID=b.GOODS_ID and a.GOODS_STATE=b.GOODS_STATE)) amountTotalImport,\n"
                + " sum((select sum(AMOUNT_TOTAL) from STOCK_DAILY_IMPORT_EXPORT b where TRUNC(b.ie_date) = ?  and a.stock_id=b.stock_id and STOCK_TRANS_TYPE=2 and a.GOODS_ID=b.GOODS_ID and a.GOODS_STATE=b.GOODS_STATE)) amountTotalExport,\n"
                + " sum((select sum(TOTAL_MONEY) from STOCK_DAILY_IMPORT_EXPORT b where TRUNC(b.ie_date) = ?  and a.stock_id=b.stock_id and STOCK_TRANS_TYPE=1  and a.GOODS_ID=b.GOODS_ID and a.GOODS_STATE=b.GOODS_STATE))moneyTotalIm,\n"
                + " sum((select sum(TOTAL_MONEY) from STOCK_DAILY_IMPORT_EXPORT b where TRUNC(b.ie_date) = ?  and a.stock_id=b.stock_id and STOCK_TRANS_TYPE=2 and a.GOODS_ID=b.GOODS_ID and a.GOODS_STATE=b.GOODS_STATE))moneyTotalEx \n"
                + " from STOCK_DAILY_REMAIN a where trunc(remain_date)= ? \n"
                + " group by a.GOODS_ID,a.GOODS_CODE,a.GOODS_NAME,a.GOODS_STATE, a.STOCK_ID, a.GOODS_UNIT_NAME \n"
                + " union all \n"
                + " select a.GOODS_ID,a.GOODS_CODE,a.GOODS_NAME,a.GOODS_STATE, a.STOCK_ID,a.GOODS_UNIT_NAME, \n"
                + " sum ((select sum(AMOUNT_MER) from STOCK_DAILY_REMAIN b where trunc(b.remain_date)= ?  and a.stock_id=b.stock_id  and a.GOODS_ID !=b.GOODS_ID and a.GOODS_STATE !=b.GOODS_STATE))amount,\n"
                + " sum ((select sum(TOTAL_MONEY) from STOCK_DAILY_REMAIN b where trunc(b.remain_date)= ?  and a.stock_id=b.stock_id  and a.GOODS_ID !=b.GOODS_ID and a.GOODS_STATE !=b.GOODS_STATE))totalMoney,\n"
                + " sum(case when a.STOCK_TRANS_TYPE=1 then AMOUNT_TOTAL else 0 end) amountTotalImport, sum(case when a.STOCK_TRANS_TYPE=2 then AMOUNT_TOTAL else 0 end )amountTotalExport,\n"
                + " sum( case when a.STOCK_TRANS_TYPE=1 then TOTAL_MONEY else 0 end) moneyTotalIm,\n"
                + " sum( case when a.STOCK_TRANS_TYPE=2 then TOTAL_MONEY else 0 end) moneyTotalEx  from STOCK_DAILY_IMPORT_EXPORT a \n"
                + " where TRUNC(a.ie_date) = ? \n"
                + " group by a.GOODS_ID,a.GOODS_CODE,a.GOODS_NAME,a.GOODS_STATE, a.STOCK_ID, a.GOODS_UNIT_NAME ) \n"
                + " , tbl1 as( select  a.GOODS_ID goodsId,a.GOODS_CODE goodsCode,a.GOODS_NAME goodsName, cat.NAME stockName,\n"
                + " a.GOODS_STATE goodsState, a.STOCK_ID stockId, a.GOODS_UNIT_NAME goodsUnitName,\n"
                + " max(nvl(amount,0))amount,\n"
                + " max(nvl(totalMoney,0))totalMoney,\n"
                + " max(nvl(amountTotalImport,0))amountTotalImport,\n"
                + " max(nvl(amountTotalExport,0))amountTotalExport,\n"
                + " max(nvl(moneyTotalIm,0))moneyTotalIm,\n"
                + " max(nvl(moneyTotalEx,0))moneyTotalEx, \n"
                + " NVL((SELECT SD.AMOUNT_MER FROM STOCK_DAILY_REMAIN SD  WHERE TRUNC(REMAIN_DATE)= ? AND SD.GOODS_ID = a.GOODS_ID AND SD.STOCK_ID = a.STOCK_ID AND SD.GOODS_STATE = a.GOODS_STATE),0) as amountFinal,\n"
                + " NVL((SELECT SD.TOTAL_MONEY FROM STOCK_DAILY_REMAIN SD WHERE TRUNC(REMAIN_DATE)= ? AND SD.GOODS_ID = a.GOODS_ID AND SD.STOCK_ID = a.STOCK_ID AND SD.GOODS_STATE = a.GOODS_STATE),0) as moneyFinal \n"
                + " from tbl a  left join cat_stock cat on a.stock_id = cat.cat_stock_id  where 1=1 \n"
                + " group by a.GOODS_ID,a.GOODS_CODE,a.GOODS_NAME,a.GOODS_STATE, a.STOCK_ID,a.GOODS_UNIT_NAME,cat.NAME) \n"
                + " select 1 type,0 goodsId, null goodsCode,null goodsName,null stockName,null goodsState,0 stockId,null goodsUnitName, \n"
                + " sum(amount)amount,sum(totalMoney)totalMoney,\n"
                + " sum(amountTotalImport)amountTotalImport,\n"
                + " sum(amountTotalExport)amountTotalExport,\n"
                + " sum(moneyTotalIm)moneyTotalIm,\n"
                + " sum(moneyTotalEx)moneyTotalEx,\n"
                + " sum(amountFinal)amountFinal,\n"
                + " sum(moneyFinal)moneyFinal from tbl1 \n"
                + " union all\n"
                + " select 2 type, goodsId, goodsCode,goodsName,stockName,goodsState,stockId,goodsUnitName,  amount,\n"
                + " totalMoney,amountTotalImport,amountTotalExport,moneyTotalIm,moneyTotalEx,amountFinal,moneyFinal  from tbl1";
        Connection connection = null;
        try {
            connection = getConnection();
            preStmt = connection.prepareStatement(strSql);
            SimpleDateFormat dfCreate = new SimpleDateFormat("dd/MM/yyyy");
            Date CreateFromDate = dfCreate.parse(backDateReport);
            preStmt.setTimestamp(1, new java.sql.Timestamp(CreateFromDate.getTime()));
            preStmt.setTimestamp(2, new java.sql.Timestamp(CreateFromDate.getTime()));
            preStmt.setTimestamp(3, new java.sql.Timestamp(CreateFromDate.getTime()));
            preStmt.setTimestamp(4, new java.sql.Timestamp(CreateFromDate.getTime()));
            preStmt.setTimestamp(5, new java.sql.Timestamp(CreateFromDate.getTime()));
            preStmt.setTimestamp(6, new java.sql.Timestamp(CreateFromDate.getTime()));
            preStmt.setTimestamp(7, new java.sql.Timestamp(CreateFromDate.getTime()));
            preStmt.setTimestamp(8, new java.sql.Timestamp(CreateFromDate.getTime()));
            preStmt.setTimestamp(9, new java.sql.Timestamp(CreateFromDate.getTime()));
            preStmt.setTimestamp(10, new java.sql.Timestamp(CreateFromDate.getTime()));
            rs = preStmt.executeQuery();
            while (rs.next()) {
                ReportBO frm = new ReportBO();
                frm.setStockId(rs.getLong("stockId"));
                frm.setStockName(rs.getString("stockName"));
                frm.setGoodsId(rs.getLong("goodsId"));
                frm.setGoodsCode(rs.getString("goodsCode"));
                frm.setGoodsName(rs.getString("goodsName"));
                frm.setGoodsState(rs.getString("goodsState"));
                frm.setGoodsUnitName(rs.getString("goodsUnitName"));
                frm.setAmount(rs.getString("amount"));
                frm.setAmountTotalImport(rs.getString("amountTotalImport"));
                frm.setAmountTotalExport(rs.getString("amountTotalExport"));
                frm.setAmountFinal(rs.getString("amountFinal"));
                frm.setTotalMoney(rs.getString("totalMoney"));
                frm.setMoneyTotalIm(rs.getString("moneyTotalIm"));
                frm.setMoneyTotalEx(rs.getString("moneyTotalEx"));
                frm.setMoneyFinal(rs.getString("moneyFinal"));
                frm.setType(rs.getString("type"));
                lstDebt.add(frm);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preStmt != null) {
                    preStmt.close();
                }
            } catch (Exception ex) {
            }
            try {
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
        return lstDebt;
    }
   
   public void deleteReport(String backDateReport,Logger logger, long maxBatchSize) {
        logger.info("DELETE START!");
        String sql = " DELETE FROM REPORT_IN_PERIOD RE WHERE trunc(RE.REPORT_DATE) = ? ";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            SimpleDateFormat dfCreate = new SimpleDateFormat("dd/MM/yyyy");
            Date CreateFromDate = dfCreate.parse(backDateReport);
            pstmt.setTimestamp(1, new java.sql.Timestamp(CreateFromDate.getTime()));
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("REPORT_IN_PERIOD DELETE INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("REPORT_IN_PERIOD DELETE INTO DB FAIL!");
            logger.error(ex.getMessage(), ex);
        } finally {
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
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("WMS FINISH!");
    }
   
   public synchronized void insertReport(ArrayList<ReportBO> lstDebt,int backDayReport,Logger logger, long maxBatchSize) {
        logger.info("INSERT START!");
        String sql = "INSERT INTO REPORT_IN_PERIOD (PERIOD_ID,\n"
                + "REPORT_DATE,\n"
                + "STOCK_ID,\n"
                + "STOCK_NAME,\n"
                + "GOODS_ID,\n"
                + "GOODS_CODE,\n"
                + "GOODS_NAME,\n"
                + "GOODS_STATE,\n"
                + "GOODS_UNIT_NAME,\n"
                + "AMOUNT,\n"
                + "AMOUNT_TOTAL_IMPORT,\n"
                + "AMOUNT_TOTAL_EXPORT,\n"
                + "AMOUNT_FINAL,\n" 
                + "TOTAL_MONEY,\n"
                + "MONEY_TOTAL_IMPORT,\n "
                + "MONEY_TOTAL_EXPORT,\n"
                + "MONEY_FINAL, \n"
                + "TYPE, \n"
                + "CREATED_DATE \n)"
                + "VALUES (REPORT_IN_PERIOD_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            int count = 0;
            int size = lstDebt.size();
            int group = 0;
            
            for (ReportBO obj : lstDebt) {
                int i = 1;
                try {
                    group++;
                    count++;
                    java.util.Date call = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(call);
                    cal.add(Calendar.DATE, backDayReport);
                    java.sql.Date reportDate = new java.sql.Date(cal.getTime().getTime());
                    pstmt.setDate(i++, reportDate);
                    pstmt.setLong(i++, obj.getStockId());
                    pstmt.setString(i++, obj.getStockName());
                    pstmt.setLong(i++, obj.getGoodsId());
                    pstmt.setString(i++, obj.getGoodsCode());
                    pstmt.setString(i++, obj.getGoodsName());
                    pstmt.setString(i++, obj.getGoodsState());
                    pstmt.setString(i++, obj.getGoodsUnitName());
                    pstmt.setString(i++, obj.getAmount());
                    pstmt.setString(i++, obj.getAmountTotalImport());
                    pstmt.setString(i++, obj.getAmountTotalExport());
                    pstmt.setString(i++, obj.getAmountFinal());
                    pstmt.setString(i++, obj.getTotalMoney());
                    pstmt.setString(i++, obj.getMoneyTotalIm());
                    pstmt.setString(i++, obj.getMoneyTotalEx());
                    pstmt.setString(i++, obj.getMoneyFinal());
                    pstmt.setString(i++, obj.getType());
                    java.util.Date call1 = new Date();
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(call1);
                    cal1.add(Calendar.DATE, backDayReport + 1);
                    java.sql.Date createdDate= new java.sql.Date(cal1.getTime().getTime());
                    pstmt.setDate(i++, createdDate);
                    pstmt.addBatch();
                    if (group == maxBatchSize) {
                        logger.info("REPORT_IN_PERIOD INSERTED " + count + "/" + size);
                        group = 0;
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                    if (group < maxBatchSize && count == size) {
                        logger.info("REPORT_IN_PERIOD INSERTED " + count + "/" + size);
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                } catch (Exception ex) {
                    logger.info("REPORT_IN_PERIOD INSERT INTO DB FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }
            logger.info("REPORT_IN_PERIOD INSERT INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("REPORT_IN_PERIOD INSERT INTO DB FAIL!");
            logger.error(ex.getMessage(), ex);
        } finally {
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
                    logger.info("REPORT_IN_PERIOD INSERT INTO DB FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("INSERT FINISH!");
    }

}
