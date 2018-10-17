/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.reportDailyRemain;

import com.viettel.framework.service.common.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author hoangnh38
 */
public class MyDbTask extends DbTask{
    
    public ArrayList<StockDailyRemainBO> getListStock(String startDate, String remainDate) throws Exception {
        ArrayList<StockDailyRemainBO> lstDebt = new ArrayList<StockDailyRemainBO>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String strSql = "SELECT SD.STOCK_ID stockId,SD.STOCK_CODE stockCode,SD.STOCK_NAME stockName,SD.GOODS_ID goodsId,SD.GOODS_STATE goodsState,\n" +
                        "(CASE WHEN SD.GOODS_STATE=1 THEN 'Bình thường' ELSE 'Hỏng' END) goodsStateName ,\n"+
                        "SD.GOODS_CODE goodsCode,SD.GOODS_NAME goodsName,SD.GOODS_TYPE goodsType,SD.GOODS_IS_SERIAL goodsIsSerial,SD.GOODS_UNIT_NAME goodsUnitName,\n" +
                        " SUM(SD.AMOUNT) + SUM(SD.amountI) - SUM(SD.amountE) amount,\n" +
                        " SUM(SD.TOTAL_MONEY) + SUM(SD.totalMoneyI) - SUM(SD.totalMoneyE) totalMoney\n" +
                        " FROM\n" +
                        "(SELECT\n" +
                        "SD.STOCK_ID ,SD.STOCK_CODE ,SD.STOCK_NAME,SD.GOODS_ID,to_char(SD.GOODS_STATE)GOODS_STATE,\n" +
                        "SD.GOODS_CODE,SD.GOODS_NAME,to_char(SD.GOODS_TYPE)GOODS_TYPE,SD.GOODS_IS_SERIAL,SD.GOODS_UNIT_NAME,\n" +
                        "NVL((case when SD.STOCK_TRANS_TYPE=1 then SD.TOTAL_MONEY end ),0) totalMoneyI,\n" +
                        "NVL((case when SD.STOCK_TRANS_TYPE=2 then SD.TOTAL_MONEY end ),0) totalMoneyE,\n" +
                        "NVL((case when SD.STOCK_TRANS_TYPE=1 then SD.AMOUNT_TOTAL end ),0) amountI,\n" +
                        "NVL((case when SD.STOCK_TRANS_TYPE=2 then SD.AMOUNT_TOTAL end ),0) amountE,\n" +
                        "0 AMOUNT,0 TOTAL_MONEY\n" +
                        "FROM STOCK_DAILY_IMPORT_EXPORT SD WHERE TRUNC(SD.IE_DATE) = :ieDate\n" +
                        " UNION ALL\n" +
                        "SELECT\n" +
                        "SD.STOCK_ID ,SD.STOCK_CODE ,SD.STOCK_NAME,SD.GOODS_ID,to_char(SD.GOODS_STATE)GOODS_STATE,\n" +
                        "SD.GOODS_CODE,SD.GOODS_NAME,to_char(SD.GOODS_TYPE)GOODS_TYPE,SD.GOODS_IS_SERIAL,SD.GOODS_UNIT_NAME,\n" +
                        "0 totalMoneyI,\n" +
                        "0 totalMoneyE,\n" +
                        "0 amountI,\n" +
                        "0 amountE,\n" +
                        "NVL(sd.AMOUNT,0)AMOUNT,NVL(sd.TOTAL_MONEY,0)TOTAL_MONEY\n" +
                        "FROM STOCK_DAILY_REMAIN SD WHERE TRUNC(SD.REMAIN_DATE) = :remainDate\n" +
                        ") sd \n" +
                        "GROUP BY SD.STOCK_ID ,SD.STOCK_CODE ,SD.STOCK_NAME,SD.GOODS_ID,SD.GOODS_STATE,\n" +
                        "SD.GOODS_CODE,SD.GOODS_NAME,SD.GOODS_TYPE,SD.GOODS_IS_SERIAL,SD.GOODS_UNIT_NAME";
        Connection connection = null;
        try {
            connection = getConnection();
            preStmt = connection.prepareStatement(strSql);
             SimpleDateFormat dfCreate = new SimpleDateFormat("dd/MM/yyyy");
            Date CreateFromDate = dfCreate.parse(startDate);
            preStmt.setTimestamp(1, new java.sql.Timestamp(CreateFromDate.getTime()));
            
            Date CreateRemainDate = dfCreate.parse(remainDate);
            preStmt.setTimestamp(2, new java.sql.Timestamp(CreateRemainDate.getTime()));
            rs = preStmt.executeQuery();
            while (rs.next()) {
                StockDailyRemainBO frm = new StockDailyRemainBO();

                frm.setStockId(rs.getString("stockId"));
                frm.setStockCode(rs.getString("stockCode"));
                frm.setStockName(rs.getString("stockName"));
                frm.setGoodsId(rs.getString("goodsId"));
                frm.setGoodsState(rs.getString("goodsState"));
                frm.setGoodsStateName(rs.getString("goodsStateName"));
                frm.setGoodsCode(rs.getString("goodsCode"));
                frm.setGoodsName(rs.getString("goodsName"));
                frm.setGoodsType(rs.getString("goodsType"));
                frm.setGoodsIsSerial(rs.getString("goodsIsSerial"));
                frm.setGoodsUnitName(rs.getString("goodsUnitName"));
                frm.setAmount(rs.getString("amount"));
                frm.setTotalMoney(rs.getString("totalMoney"));
                
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
    
    public void deleteStockRemain(String startDate,Logger logger, long maxBatchSize) {
        logger.info("DELETE START!");
        String sql = " DELETE FROM STOCK_DAILY_REMAIN RE WHERE trunc(RE.REMAIN_DATE) = ?";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            SimpleDateFormat dfCreate = new SimpleDateFormat("dd/MM/yyyy");
            Date CreateFromDate = dfCreate.parse(startDate);
            pstmt.setTimestamp(1, new java.sql.Timestamp(CreateFromDate.getTime()));
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("STOCK_DAILY_REMAIN DELETE INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("STOCK_DAILY_REMAIN DELETE INTO DB FAIL!");
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
    
    public synchronized void insertStockRemain(ArrayList<StockDailyRemainBO> lstDebt,int backDate, Logger logger, long maxBatchSize) {
        logger.info("INSERT START!");
        String sql = "INSERT INTO STOCK_DAILY_REMAIN (STOCK_DAILY_REMAIN_ID,\n"
                + "REMAIN_DATE,\n"
                + "STOCK_ID,\n"
                + "STOCK_CODE,\n"
                + "STOCK_NAME,\n"
                + "GOODS_ID,\n"
                + "GOODS_STATE,\n"
                + "GOODS_STATE_NAME,\n"
                + "GOODS_CODE,\n"
                + "GOODS_NAME,\n"
                + "GOODS_TYPE,\n"
                + "GOODS_IS_SERIAL,\n"
                + "GOODS_UNIT_NAME,\n"
                + "AMOUNT,\n"
                + "TOTAL_PRICE,\n"
                + "CREATE_DATE_TIME,\n "
                + "TOTAL_MONEY,\n"
                + "AMOUNT_MER \n)"
                + "VALUES (STOCK_DAILY_REMAIN_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            int count = 0;
            int size = lstDebt.size();
            int group = 0;
            for (StockDailyRemainBO obj : lstDebt) {
                int i = 1;
                try {
                    group++;
                    count++;
                    java.util.Date call = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(call);
                    cal.add(Calendar.DATE, backDate);
                    java.sql.Date remainDate = new java.sql.Date(cal.getTime().getTime());
                    pstmt.setDate(i++, remainDate);
                    pstmt.setString(i++, obj.getStockId());
                    pstmt.setString(i++, obj.getStockCode());
                    pstmt.setString(i++, obj.getStockName());
                    pstmt.setString(i++, obj.getGoodsId());
                    pstmt.setString(i++, obj.getGoodsState());
                    pstmt.setString(i++, obj.getGoodsStateName());
                    pstmt.setString(i++, obj.getGoodsCode());
                    pstmt.setString(i++, obj.getGoodsName());
                    pstmt.setString(i++, obj.getGoodsType());
                    pstmt.setString(i++, obj.getGoodsIsSerial());
                    pstmt.setString(i++, obj.getGoodsUnitName());
                    pstmt.setString(i++, obj.getAmount());
                    pstmt.setString(i++, obj.getTotalPrice());
//                    var currDate = new Date();
//                    java.util.Date rawDate = new Date();
//                    java.sql.Date currDate = new java.sql.Date(rawDate.getTime());
//                    pstmt.setDate(i++, currDate);
                    java.util.Date call1 = new Date();
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(call1);
                    cal1.add(Calendar.DATE, backDate + 1);
                    java.sql.Date createdDate= new java.sql.Date(cal1.getTime().getTime());
                    pstmt.setDate(i++, createdDate);
                    pstmt.setString(i++, obj.getTotalMoney());
                    pstmt.setString(i++, obj.getAmountMe());
                    pstmt.addBatch();
                    if (group == maxBatchSize) {
                        logger.info("STOCK_DAILY_REMAIN INSERTED " + count + "/" + size);
                        group = 0;
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                    if (group < maxBatchSize && count == size) {
                        logger.info("STOCK_DAILY_REMAIN INSERTED " + count + "/" + size);
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                } catch (Exception ex) {
                    logger.info("STOCK_DAILY_REMAIN INSERT INTO DB FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }
            logger.info("STOCK_DAILY_REMAIN INSERT INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("STOCK_DAILY_REMAIN INSERT INTO DB FAIL!");
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
                    logger.info("STOCK_DAILY_REMAIN INSERT INTO DB FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("INSERT FINISH!");
    }
}
