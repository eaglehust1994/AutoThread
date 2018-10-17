/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.inventory;

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

//    LocalDate date = LocalDate.now();
//    java.sql.Date sqlDate = java.sql.Date.valueOf(date);
    public ArrayList<StockDailyRemainObject> getListStock() throws Exception {
        ArrayList<StockDailyRemainObject> lstDebt = new ArrayList<StockDailyRemainObject>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String strSql = "with s as(SELECT STG.STOCK_ID, STG.GOODS_ID, STG.GOODS_STATE,SUM(ME.AMOUNT * ME.APPLY_PRICE) totalMoney,\n"
                + " SUM(ME.AMOUNT) amountMe  FROM STOCK_GOODS_TOTAL STG \n"
                + " INNER JOIN MER_ENTITY ME ON(ME.STOCK_ID = STG.STOCK_ID AND ME.GOODS_ID = STG.GOODS_ID AND ME.STATE = STG.GOODS_STATE AND ME.STATUS =4)\n"
                + " GROUP BY STG.STOCK_ID, STG.GOODS_ID, STG.GOODS_STATE)\n"
                + " SELECT DISTINCT ST.STOCK_ID stockId,\n"
                + " CS.CODE stockCode,\n"
                + " CS.NAME stockName,\n"
                + " ST.GOODS_ID goodsId,\n"
                + " ST.GOODS_CODE goodsCode,\n"
                + " ST.GOODS_IS_SERIAL goodsIsSerial,\n"
                + " ST.GOODS_NAME goodsName,\n"
                + " ST.GOODS_TYPE goodsType,\n"
                + " ST.GOODS_UNIT_NAME goodsUnitName,\n"
                + " ST.CHANGE_DATE remainDate,\n"
                + " ST.GOODS_STATE goodsState,\n"
                + " ST.GOODS_STATE_NAME goodsStateName,\n"
                + " s.totalMoney totalMoney,\n"
                + " ST.AMOUNT amount,\n"
                + " s.amountMe amountMe,\n"
                + " decode(S.amountMe,0,0,round((S.totalMoney/S.amountMe),0))AS totalPrice \n"
                + " FROM STOCK_GOODS_TOTAL ST JOIN CTCT_CAT_OWNER.CAT_STOCK CS ON  ST.STOCK_ID = CS.CAT_STOCK_ID \n"
                + " INNER JOIN S s ON(s.STOCK_ID = ST.STOCK_ID AND s.GOODS_ID = ST.GOODS_ID AND s.GOODS_STATE = ST.GOODS_STATE)";
        Connection connection = null;
        try {
            connection = getConnection();
            preStmt = connection.prepareStatement(strSql);
//            SimpleDateFormat dfCreate = new SimpleDateFormat("dd/MM/yyyy");
//            Date CreateFromDate = dfCreate.parse(startDateRemain);
//            preStmt.setTimestamp(1, new java.sql.Timestamp(CreateFromDate.getTime()));
            rs = preStmt.executeQuery();
            while (rs.next()) {
                StockDailyRemainObject frm = new StockDailyRemainObject();

                frm.setStockId(rs.getString("stockId"));
                frm.setStockCode(rs.getString("stockCode"));
                frm.setStockName(rs.getString("stockName"));
                frm.setGoodsId(rs.getString("goodsId"));
                frm.setGoodsCode(rs.getString("goodsCode"));
                frm.setGoodsIsSerial(rs.getString("goodsIsSerial"));
                frm.setGoodsName(rs.getString("goodsName"));
                frm.setGoodsType(rs.getString("goodsType"));
                frm.setGoodsUnitName(rs.getString("goodsUnitName"));
                frm.setRemainDate(rs.getDate("remainDate"));
                frm.setGoodsState(rs.getString("goodsState"));
                frm.setGoodsStateName(rs.getString("goodsStateName"));
                frm.setTotalMoney(rs.getString("totalMoney"));
                frm.setAmount(rs.getString("amount"));
                frm.setAmountMe(rs.getString("amountMe"));
                frm.setTotalPrice(rs.getString("totalPrice"));
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

    public ArrayList<StockDailyImExObject> getListStockImEx(String startTime) throws Exception {
        ArrayList<StockDailyImExObject> lstDebt = new ArrayList<StockDailyImExObject>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String strSql = "with s as (select s.stock_id,s.type, std.goods_code, std.goods_state , sum(std.amount_real) amountReal,sum(std.total_price) totalMoney \n"
                + "from stock_trans s inner join stock_trans_detail std on (s.stock_trans_id=std.stock_trans_id and s.status =2 \n"
                + " AND TRUNC(s.REAL_IE_TRANS_DATE) = ? ) \n"
                + "group by  s.stock_id, std.goods_code, std.goods_state,s.type)\n"
                + "SELECT DISTINCT ST.TYPE stockTransType,\n"
                + "ST.STOCK_ID stockId,\n"
                + "CS.CODE stockCode,\n"
                + "CS.NAME stockName,\n"
                + "SD.GOODS_TYPE goodsType,\n"
                + "SD.GOODS_TYPE_NAME goodsTypeName,\n"
                + "SD.GOODS_ID goodsId,\n"
                + "SD.GOODS_CODE goodsCode,\n"
                + "SD.GOODS_NAME goodsName,\n"
                + "SD.GOODS_IS_SERIAL goodsIsSerial,\n"
                + "SD.GOODS_STATE goodsState,\n"
                + "SD.GOODS_STATE_NAME goodsStateName,\n"
                + "SD.GOODS_UNIT_NAME goodsUnitName,\n"
                + "SD.GOODS_UNIT_ID goodsUnitId,\n"
                + "s.amountReal amountTotal,\n"
                + "s.totalMoney totalMoney \n"
                + "FROM STOCK_TRANS ST INNER JOIN STOCK_TRANS_DETAIL SD ON (ST.STOCK_TRANS_ID = SD.STOCK_TRANS_ID AND ST.STATUS =2 )\n"
                + "JOIN CTCT_CAT_OWNER.CAT_STOCK  CS ON ST.STOCK_ID = CS.CAT_STOCK_ID\n"
                + "JOIN S ON (s.stock_id =ST.STOCK_ID AND s.goods_code =SD.GOODS_CODE AND s.goods_state = SD.GOODS_STATE AND s.type=ST.TYPE)\n"
                + "WHERE  TRUNC(ST.REAL_IE_TRANS_DATE) = ? ";
        Connection connection = null;
        try {
            connection = getConnection();
            preStmt = connection.prepareStatement(strSql);
            SimpleDateFormat dfCreate = new SimpleDateFormat("dd/MM/yyyy");
            Date CreateFromDate = dfCreate.parse(startTime);
            preStmt.setTimestamp(1, new java.sql.Timestamp(CreateFromDate.getTime()));
            preStmt.setTimestamp(2, new java.sql.Timestamp(CreateFromDate.getTime()));
            rs = preStmt.executeQuery();

            while (rs.next()) {
                StockDailyImExObject frm = new StockDailyImExObject();

                frm.setStockTransType(rs.getString("stockTransType"));
                frm.setStockId(rs.getString("stockId"));
                frm.setStockCode(rs.getString("stockCode"));
                frm.setStockName(rs.getString("stockName"));
                frm.setGoodsType(rs.getString("goodsType"));
                frm.setGoodsTypeName(rs.getString("goodsTypeName"));
                frm.setGoodsId(rs.getString("goodsId"));
                frm.setGoodsCode(rs.getString("goodsCode"));
                frm.setGoodsName(rs.getString("goodsName"));
                frm.setGoodsIsSerial(rs.getString("goodsIsSerial"));
                frm.setGoodsState(rs.getString("goodsState"));
                frm.setGoodsStateName(rs.getString("goodsStateName"));
                frm.setGoodsUnitName(rs.getString("goodsUnitName"));
                frm.setGoodsUnitId(rs.getString("goodsUnitId"));
                frm.setAmountTotal(rs.getString("amountTotal"));
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

    public ArrayList<StockGoodsTotalRes> getListStockRes() throws Exception {
        ArrayList<StockGoodsTotalRes> lstDebt = new ArrayList<StockGoodsTotalRes>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String strSql = "WITH order_info as (SELECT ORD1.STOCK_ID, ODG1.GOODS_ID,ODG1.GOODS_STATE,  SUM(ODG1.AMOUNT) AMOUNT_ORDER\n"
                + "   FROM ORDER_GOODS ODG1\n"
                + "   JOIN ORDERS ORD1 ON (ORD1.SIGN_STATE ='3'\n"
                + "                        AND ODG1.ORDER_ID = ORD1.ORDER_ID\n"
                + "                        AND ORD1.STATUS = '1'\n"
                + "                        AND ORD1.TYPE = '2')\n"
                + "  group by ODG1.GOODS_STATE,ODG1.GOODS_ID,ORD1.STOCK_ID)\n"
                + "SELECT ST.STOCK_ID stockId,\n"
                + "       CS.CODE stockCode,\n"
                + "       CS.NAME stockName,\n"
                + "       ST.GOODS_ID goodsId,\n"
                + "       ST.GOODS_STATE goodsState,\n"
                + "       ST.GOODS_STATE_NAME goodsStateName,\n"
                + "       ST.GOODS_CODE goodsCode,\n"
                + "       ST.GOODS_NAME goodsName,\n"
                + "       ST.GOODS_TYPE goodsType,\n"
                + "       ST.GOODS_TYPE_NAME goodsTypeName,\n"
                + "       ST.GOODS_IS_SERIAL goodsIsSerial,\n"
                + "       ST.GOODS_UNIT_ID goodsUnitId,\n"
                + "       ST.GOODS_UNIT_NAME goodsUnitName,\n"
                + "       OI.AMOUNT_ORDER amountOrder,\n"
                + "       ST.AMOUNT amountRemain,\n"
                + "       (ST.AMOUNT - OI.AMOUNT_ORDER) amountIssue\n"
                + "from STOCK_GOODS_TOTAL st\n"
                + "inner join order_info oi on st.GOODS_ID = oi.GOODS_ID and oi.STOCK_ID = ST.STOCK_ID and oi.GOODS_STATE = st.GOODS_STATE\n"
                + "inner JOIN CAT_STOCK CS ON ST.STOCK_ID = CS.CAT_STOCK_ID";
        Connection connection = null;
        try {
            connection = getConnection();
            preStmt = connection.prepareStatement(strSql);

            rs = preStmt.executeQuery();
            while (rs.next()) {
                StockGoodsTotalRes frm = new StockGoodsTotalRes();

                frm.setStockId(rs.getString("stockId"));
                frm.setStockCode(rs.getString("stockCode"));
                frm.setStockName(rs.getString("stockName"));
                frm.setGoodsId(rs.getString("goodsId"));
                frm.setGoodsState(rs.getString("goodsState"));
                frm.setGoodsStateName(rs.getString("goodsStateName"));
                frm.setGoodsCode(rs.getString("goodsCode"));
                frm.setGoodsName(rs.getString("goodsName"));
                frm.setGoodsType(rs.getString("goodsType"));
                frm.setGoodsTypeName(rs.getString("goodsTypeName"));
                frm.setGoodsIsSerial(rs.getString("goodsIsSerial"));
                frm.setGoodsUnitId(rs.getString("goodsUnitId"));
                frm.setGoodsUnitName(rs.getString("goodsUnitName"));
                frm.setAmountOrder(rs.getString("amountOrder"));
                frm.setAmountRemain(rs.getString("amountRemain"));
                frm.setAmountIssue(rs.getString("amountIssue"));
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

    public synchronized void updateInRoal(Logger logger) {
        logger.info("UPDATE START!");
        String sql = " UPDATE STOCK_TRANS ST SET ST.IN_ROAL=1 WHERE ST.TYPE=2 \n"
                + "  AND ST.BUSINESS_TYPE=4 \n"
                + "  AND ST.STATUS=2 \n"
                + " AND NOT EXISTS(SELECT * FROM STOCK_TRANS ST1 WHERE ST1.STATUS=2 \n"
                + "AND ST1.TYPE=1 AND ST1.BUSINESS_TYPE=7 AND ST1.FROM_STOCK_TRANS_ID=ST1.STOCK_TRANS_ID)";

        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("UPDATE IN_ROAL SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE IN_ROAL FAIL!");
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
                    logger.info("UPDATE IN_ROAL FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE FINISH!");
    }

    public synchronized void updateInRoalNull(Logger logger) {
        logger.info("UPDATE START!");
        String sql = " UPDATE STOCK_TRANS ST SET ST.IN_ROAL=null WHERE ST.TYPE=2 \n"
                + " AND ST.BUSINESS_TYPE=4 \n"
                + " AND ST.STATUS=2"
                + " AND ST.IN_ROAL=1 \n"
                + " AND EXISTS(SELECT * FROM STOCK_TRANS ST1 WHERE ST1.STATUS=2 \n"
                + "AND ST1.TYPE=1 AND ST1.BUSINESS_TYPE=7 AND ST1.FROM_STOCK_TRANS_ID=ST1.STOCK_TRANS_ID)";

        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("UPDATE IN_ROAL NULL SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE IN_ROAL NULL FAIL!");
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
                    logger.info("UPDATE IN_ROAL NULL FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE FINISH!");
    }
    
    public synchronized void updateInRoalTH2(Logger logger) {
        logger.info("UPDATE START!");
        String sql = " UPDATE STOCK_TRANS ST SET ST.IN_ROAL=1 WHERE ST.BUSINESS_TYPE = 2 AND ST.CONFIRM IS NULL ";

        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("UPDATE IN_ROAL TH2 SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE IN_ROAL TH2 FAIL!");
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
                    logger.info("UPDATE IN_ROAL TH2 FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE FINISH!");
    }
    
    public synchronized void updateInRoalNullTH2(Logger logger) {
        logger.info("UPDATE START!");
        String sql = " UPDATE STOCK_TRANS ST SET ST.IN_ROAL=null WHERE ST.BUSINESS_TYPE = 2 AND ST.CONFIRM IS NOT NULL AND ST.IN_ROAL IS NOT NULL";

        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("UPDATE IN_ROAL NULL SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE IN_ROAL NULL FAIL!");
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
                    logger.info("UPDATE IN_ROAL NULL FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE FINISH!");
    }

    public synchronized void insertStockRemain(ArrayList<StockDailyRemainObject> lstDebt,int backDate, Logger logger, long maxBatchSize) {
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
            for (StockDailyRemainObject obj : lstDebt) {
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
//            pstmt.executeBatch();
//            logger.info("syn_stock_total INSERTED " + count + "/" + size);
//            con.setAutoCommit(false);
//            this.commit(con);
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

    public synchronized void insertStockImEx(ArrayList<StockDailyImExObject> lstDebt,int backDate, Logger logger, long maxBatchSize) {
        logger.info("INSERT START!");
        String sql = "INSERT INTO STOCK_DAILY_IMPORT_EXPORT (STOCK_DAILY_IMPORT_EXPORT_ID,\n"
                + "IE_DATE,\n"
                + "STOCK_TRANS_TYPE,\n"
                + "STOCK_ID,\n"
                + "STOCK_CODE,\n"
                + "STOCK_NAME,\n"
                + "GOODS_TYPE,\n"
                + "GOODS_TYPE_NAME,\n"
                + "GOODS_ID,\n"
                + "GOODS_CODE,\n"
                + "GOODS_NAME,\n"
                + "GOODS_IS_SERIAL,\n"
                + "GOODS_STATE,\n"
                + "GOODS_STATE_NAME,\n"
                + "GOODS_UNIT_NAME,\n"
                + "GOODS_UNIT_ID,\n "
                + "AMOUNT_TOTAL,\n "
                + "CREATE_DATE_TIME,\n"
                + "TOTAL_MONEY\n )"
                + "VALUES (STOCK_DAILY_IMPORT_EXPORT_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            int count = 0;
            int size = lstDebt.size();
            int group = 0;
            for (StockDailyImExObject obj : lstDebt) {
                int i = 1;
                try {
                    group++;
                    count++;
                    java.util.Date call = new Date();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(call);
                    cal.add(Calendar.DATE, backDate);
                    java.sql.Date ieDate = new java.sql.Date(cal.getTime().getTime());
                    pstmt.setDate(i++, ieDate);
                    pstmt.setString(i++, obj.getStockTransType());
                    pstmt.setString(i++, obj.getStockId());
                    pstmt.setString(i++, obj.getStockCode());
                    pstmt.setString(i++, obj.getStockName());
                    pstmt.setString(i++, obj.getGoodsType());
                    pstmt.setString(i++, obj.getGoodsTypeName());
                    pstmt.setString(i++, obj.getGoodsId());
                    pstmt.setString(i++, obj.getGoodsCode());
                    pstmt.setString(i++, obj.getGoodsName());
                    pstmt.setString(i++, obj.getGoodsIsSerial());
                    pstmt.setString(i++, obj.getGoodsState());
                    pstmt.setString(i++, obj.getGoodsStateName());
                    pstmt.setString(i++, obj.getGoodsUnitName());
                    pstmt.setString(i++, obj.getGoodsUnitId());
                    pstmt.setString(i++, obj.getAmountTotal());
                    
                    java.util.Date call1 = new Date();
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(call1);
                    cal1.add(Calendar.DATE, backDate + 1);
                    java.sql.Date createdDate= new java.sql.Date(cal1.getTime().getTime());
                    pstmt.setDate(i++, createdDate);
                    pstmt.setString(i++, obj.getTotalMoney());
                    pstmt.addBatch();
                    if (group == maxBatchSize) {
                        logger.info("STOCK_DAILY_IMPORT_EXPORT INSERTED " + count + "/" + size);
                        group = 0;
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                    if (group < maxBatchSize && count == size) {
                        logger.info("STOCK_DAILY_IMPORT_EXPORT INSERTED " + count + "/" + size);
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                } catch (Exception ex) {
                    logger.info("STOCK_DAILY_IMPORT_EXPORT INSERT INTO DB FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }
            logger.info("STOCK_DAILY_IMPORT_EXPORT INSERT INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("STOCK_DAILY_IMPORT_EXPORT INSERT INTO DB FAIL!");
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
                    logger.info("STOCK_DAILY_IMPORT_EXPORT INSERT INTO DB FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("INSERT FINISH!");
    }

    public synchronized void insertStockResponse(ArrayList<StockGoodsTotalRes> lstDebt, Logger logger, long maxBatchSize) {
        logger.info("INSERT START!");
        String sql = "INSERT INTO STOCK_GOODS_TOTAL_RESPONSE (STOCK_GOODS_TOTAL_RESPONSE_ID,\n"
                + "STOCK_ID,\n"
                + "STOCK_CODE,\n"
                + "STOCK_NAME,\n"
                + "GOODS_ID,\n"
                + "GOODS_STATE,\n"
                + "GOODS_STATE_NAME,\n"
                + "GOODS_CODE,\n"
                + "GOODS_NAME,\n"
                + "GOODS_TYPE,\n"
                + "GOODS_TYPE_NAME,\n"
                + "GOODS_IS_SERIAL,\n"
                + "GOODS_UNIT_ID,\n"
                + "GOODS_UNIT_NAME,\n"
                + "AMOUNT_REMAIN,\n"
                + "AMOUNT_ORDER,\n"
                + "AMOUNT_ISSUE,\n"
                + "CHANGE_DATE\n )"
                + "VALUES (STOCK_GOODS_TOTAL_RESPONSE_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            int count = 0;
            int size = lstDebt.size();
            int group = 0;
            for (StockGoodsTotalRes obj : lstDebt) {
                int i = 1;
                try {
                    group++;
                    count++;
                    pstmt.setString(i++, obj.getStockId());
                    pstmt.setString(i++, obj.getStockCode());
                    pstmt.setString(i++, obj.getStockName());
                    pstmt.setString(i++, obj.getGoodsId());
                    pstmt.setString(i++, obj.getGoodsState());
                    pstmt.setString(i++, obj.getGoodsStateName());
                    pstmt.setString(i++, obj.getGoodsCode());
                    pstmt.setString(i++, obj.getGoodsName());
                    pstmt.setString(i++, obj.getGoodsType());
                    pstmt.setString(i++, obj.getGoodsTypeName());
                    pstmt.setString(i++, obj.getGoodsIsSerial());
                    pstmt.setString(i++, obj.getGoodsUnitId());
                    pstmt.setString(i++, obj.getGoodsUnitName());
                    pstmt.setString(i++, obj.getAmountRemain());
                    pstmt.setString(i++, obj.getAmountOrder());
                    pstmt.setString(i++, obj.getAmountIssue());
                    java.util.Date rawDate = new Date();
                    java.sql.Date currDate = new java.sql.Date(rawDate.getTime());
                    pstmt.setDate(i++, currDate);
                    pstmt.addBatch();
                    if (group == maxBatchSize) {
                        logger.info("STOCK_GOODS_TOTAL_RESPONSE INSERTED " + count + "/" + size);
                        group = 0;
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                    if (group < maxBatchSize && count == size) {
                        logger.info("STOCK_GOODS_TOTAL_RESPONSE INSERTED " + count + "/" + size);
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                } catch (Exception ex) {
                    logger.info("STOCK_GOODS_TOTAL_RESPONSE INSERT INTO DB FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }
            logger.info("STOCK_GOODS_TOTAL_RESPONSE INSERT INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("STOCK_GOODS_TOTAL_RESPONSE INSERT INTO DB FAIL!");
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
                    logger.info("STOCK_GOODS_TOTAL_RESPONSE INSERT INTO DB FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("INSERT FINISH!");
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

    public void deleteStockDailyImEx(String startDate,Logger logger, long maxBatchSize) {
        logger.info("DELETE START!");
        String sql = " DELETE FROM STOCK_DAILY_IMPORT_EXPORT IE WHERE trunc(IE.IE_DATE) = ? ";
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
            logger.info("STOCK_DAILY_IMPORT_EXPORT DELETE INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("STOCK_DAILY_IMPORT_EXPORT DELETE INTO DB FAIL!");
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

    public void deleteStockResponse(Logger logger, long maxBatchSize) {
        logger.info("DELETE START!");
        String sql = " DELETE FROM STOCK_GOODS_TOTAL_RESPONSE ";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("STOCK_GOODS_TOTAL_RESPONSE DELETE INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("STOCK_GOODS_TOTAL_RESPONSE DELETE INTO DB FAIL!");
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

    public synchronized List selectAllStockFromKPIStorage(Date startTime, Logger logger) throws SQLException {
        List<Integer> arr = new ArrayList();
        logger.info("select START!");
        String sql = "select STOCK_ID from CONFIG_KPI_STORAGE";
        PreparedStatement pstmt = null;
        Connection con = null;
        ResultSet rs = null;
        int a = 0;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                arr.add(rs.getInt(1));
            }
            logger.info("SELECT ALL STOCK CONFIG KPI STORAGE SUCCESS!");
        } catch (Exception ex) {
            logger.info("SELECT ALL STOCK CONFIG KPI STORAGE FAIL!");
            logger.error(ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
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
                    logger.info("SELECT ALL STOCK CONFIG KPI STORAGE FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("SELECT FINISH!");
        return arr;
    }

    public synchronized List selectAllStockFromMer(Date startTime, Logger logger) throws SQLException {

        List<MerEntityBO> lst = new ArrayList<>();
        logger.info("select MER_ENTITY where status=1 and import_date >= now date START!");
        String sql = " select me.SERIAL serial,me.IMPORT_STOCK_TRANS_ID importStockTransId,me.GOODS_ID goodsId,me.GOODS_CODE goodsCode,\n"
                + " me.GOODS_NAME goodsName,me.STATE state,me.STATUS status,me.AMOUNT amount,\n"
                + " me.CAT_MANUFACTURER_ID catManufacetuerId,me.STOCK_ID stockId,\n"
                + " me.CNT_CONTRACT_ID cntContractId,me.SYS_GROUP_ID sysGroupId,\n"
                + " me.PROJECT_ID projectId,me.SHIPMENT_ID shipmentId,me.PART_NUMBER partNumber,\n"
                + " me.UNIT_PRICE unitPrice,me.APPLY_PRICE applyPrice,me.CAT_UNIT_ID catUnitId,\n"
                + " me.ORDER_ID orderId,me.CNT_CONTRACT_CODE cntConTractCode,me.IMPORT_DATE importDate,\n"
                + " me.UPDATED_DATE updatedDate,me.STOCK_CELL_ID stockCellId,me.STOCK_CELL_CODE stockCellCode,\n"
                + " me.CAT_PRODUCING_COUNTRY_ID catProducingCountryId,me.PARENT_MER_ENTITY_ID parentMerEntity,\n"
                + " me.CAT_UNIT_NAME catUnitName,sm.CODE shipmentCode,g.GOODS_TYPE goodsType,sm.PROJECT_CODE projectCode,\n"
                + " g.MANUFACTURER_NAME manufaceturerName,g.CAT_MANUFACTURER_ID manufaceturerId,g.CAT_PRODUCING_COUNTRY_ID producingCountryId,\n"
                + " g.PRODUCING_COUNTRY_NAME producingCountryName "
                + " from MER_ENTITY me left join SHIPMENT sm on sm.SHIPMENT_ID=me.SHIPMENT_ID\n"
                + " join CTCT_CAT_OWNER.GOODS g on g.GOODS_ID=me.GOODS_ID\n"
                + " where me.status ='4' and me.IMPORT_DATE is not null and me.STOCK_ID is not null";
        PreparedStatement pstmt = null;
        Connection con = null;
        ResultSet rs = null;
        int a = 0;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
//            pstmt.setDate(1, sqlDate);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MerEntityBO entityBO = new MerEntityBO();
                entityBO.setSerial(rs.getString("serial"));
                entityBO.setGoodsId(rs.getLong("goodsId"));
                entityBO.setImportStockTransId(rs.getString("importStockTransId"));
                entityBO.setGoodsCode(rs.getString("goodsCode"));
                entityBO.setGoodsName(rs.getString("goodsName"));
                entityBO.setState(rs.getString("state"));
                entityBO.setStatus(rs.getString("status"));
                entityBO.setAmount(rs.getDouble("amount"));
                entityBO.setCatManufacturerId(rs.getLong("catManufacetuerId"));
                entityBO.setStockId(rs.getString("stockId"));
                entityBO.setCntContractId(rs.getLong("cntContractId"));
                entityBO.setSysGroupId(rs.getLong("sysGroupId"));
                entityBO.setProjectId(rs.getLong("projectId"));
                entityBO.setShipmentId(rs.getLong("shipmentId"));
                entityBO.setPartNumber(rs.getString("partNumber"));
                entityBO.setUnitPrice(rs.getDouble("unitPrice"));
                entityBO.setApplyPrice(rs.getDouble("applyPrice"));
                entityBO.setManufacturerName(rs.getString("manufaceturerName"));
                entityBO.setProducingCountryName(rs.getString("producingCountryName"));
                entityBO.setCatUnitId(rs.getLong("catUnitId"));
                entityBO.setOrderId(rs.getLong("orderId"));
                entityBO.setCntContractCode(rs.getString("cntConTractCode"));
                entityBO.setImportDate(rs.getDate("importDate"));
                entityBO.setUpdatedDate(rs.getDate("updatedDate"));
                entityBO.setStockCellId(rs.getLong("stockCellId"));
                entityBO.setCatProducingCountryId(rs.getLong("catProducingCountryId"));
                entityBO.setParentMerEntityId(rs.getLong("parentMerEntity"));
                entityBO.setCatUnitName(rs.getString("catUnitName"));
                entityBO.setShipmentCode(rs.getString("shipmentCode"));
                entityBO.setGoodsType(rs.getLong("goodsType"));
                entityBO.setProjectCode(rs.getString("projectCode"));
                entityBO.setManufaceturerId(rs.getLong("manufaceturerId"));
                entityBO.setProducingCountryId(rs.getLong("producingCountryId"));
                lst.add(entityBO);
            }
            logger.info("SELECT ALL MER_ENTITY SUCCESS!");
        } catch (Exception ex) {
            logger.info("SELECT ALL MER_ENTITY FAIL!");
            logger.error(ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
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
                    logger.info("SELECT ALL MER_ENTITY FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("SELECT FINISH!");
        return lst;
    }

    public synchronized List selectKpiQuotaApp(Date startTime, Logger logger) throws SQLException {
//        MerEntityBO entityBO = new MerEntityBO();
        List<Integer> lst = new ArrayList<>();
        logger.info("select kpiQuota App START!");
        String sql = "select CODE code  from CTCT_CAT_OWNER.APP_PARAM where PAR_TYPE='KPI_STORAGE_TIME'";
        PreparedStatement pstmt = null;
        Connection con = null;
        ResultSet rs = null;
        int a = 0;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lst.add(rs.getInt("code"));
            }
            logger.info("SELECT kpiQuota SUCCESS!");
        } catch (Exception ex) {
            logger.info("SELECT kpiQuota FAIL!");
            logger.error(ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
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
                    logger.info("SELECT kpiQuota FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("SELECT FINISH!");
        return lst;
    }

    public synchronized List checkAmountQuota(Logger logger, int stockId) {
        //List Stock_goods_total
        List<StockGoodsTotalBO> arr = new ArrayList<StockGoodsTotalBO>();
        logger.info("select START!");
        String sql = "select sgt.STOCK_ID stockId,"
                + "sgt.GOODS_ID goodsId,"
                + "sgt.GOODS_STATE goodsState,"
                + "sgt.GOODS_STATE_NAME goodsStateName,"
                + "sgt.GOODS_CODE goodsCode,"
                + "sgt.GOODS_NAME goodsName,"
                + "sgt.GOODS_TYPE goodsType,"
                + "sgt.GOODS_IS_SERIAL goodsIsSerial,"
                + "sgt.GOODS_UNIT_ID goodsUnitId,"
                + "sgt.GOODS_UNIT_NAME goodsUnitName,"
                + "sgt.AMOUNT amountRemain,"
                + "sgt.CHANGE_DATE changeDate,"
                + "ckt.AMOUNT amountQuota,"
                + "sgt.GOODS_TYPE_NAME goodsTypeName,"
                + "sgt.STOCK_CODE stockCode,"
                + "sgt.STOCK_NAME stockName,"
                + " (CASE WHEN 1=1 then  sgt.AMOUNT - ckt.AMOUNT else 0 End) AS  amountKpi "
                + " from STOCK_GOODS_TOTAL  sgt  join  CONFIG_KPI_STORAGE ckt "
                + " on ckt.STOCK_ID=sgt.STOCK_ID where ckt.STOCK_ID =? and ckt.AMOUNT != sgt.AMOUNT and ckt.goods_id = sgt.goods_id ";
        PreparedStatement pstmt = null;
        Connection con = null;
        int a = 0;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, stockId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                StockGoodsTotalBO bO = new StockGoodsTotalBO();

                bO.setAmountRemain(rs.getDouble("amountRemain"));
                bO.setAmountKpi(rs.getDouble("amountKpi"));
                bO.setChangeDate(rs.getDate("changeDate"));
                bO.setGoodsCode(rs.getString("goodsCode"));
                bO.setGoodsId(rs.getLong("goodsId"));
                bO.setGoodsIsSerial(rs.getString("goodsIsSerial"));
                bO.setAmountQuota(rs.getDouble("amountQuota"));
                bO.setGoodsName(rs.getString("goodsName"));
                bO.setGoodsState(rs.getString("goodsState"));
                bO.setGoodsStateName(rs.getString("goodsStateName"));
                bO.setGoodsType(rs.getLong("goodsType"));
                bO.setGoodsTypeName(rs.getString("goodsTypeName"));
                bO.setGoodsUnitId(rs.getLong("goodsUnitId"));
                bO.setGoodsUnitName(rs.getString("goodsUnitName"));
                bO.setStockCode(rs.getString("stockCode"));
                bO.setStockId(rs.getLong("stockId"));
                bO.setStockName(rs.getString("stockName"));
                arr.add(bO);
            }
            logger.info("SELECT ALL STOCK CONFIG KPI STORAGE SUCCESS!");
        } catch (Exception ex) {
            logger.info("SELECT ALL STOCK CONFIG KPI STORAGE FAIL!");
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
                    logger.info("SELECT ALL STOCK CONFIG KPI STORAGE FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("SELECT FINISH!");
        return arr;
    }

    public void deleteKpiStorageAmount(Logger logger) {
        logger.info("DELETE START!");
        String sql = " delete from KPI_STORAGE_AMOUNT ";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("KPI_STORAGE_AMOUNT DELETE INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("KPI_STORAGE_AMOUNT DELETE INTO DB FAIL!");
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
        logger.info("DELETE FINISH!");
    }

    public void deleteKpiStorageTime(Logger logger) {
        logger.info("DELETE START!");
        String sql = " delete from KPI_STORAGE_TIME ";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("KPI_STORAGE_TIME DELETE INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("KPI_STORAGE_TIME DELETE INTO DB FAIL!");
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
        logger.info("DELETE FINISH!");
    }

    public synchronized void updateStateDueContract(Date startTime, Logger logger) {
        logger.info("insert START!");
        String sql = "insert into CONFIG_KPI_STORAGE "
                + " (CONFIG_KPI_STORAGE_ID,sys_user_id,stock_id,GOODS_ID,AMOUNT)"
                + " values (CONFIG_KPI_STORAGE_SEQ.nextval, 18627 , 11 , 32601 , 100 )";
//        String sql = " insert CONFIG_KPI_STORAGE set state=1 "
//                + " where START_TIME >=? and (end_time >= trunc(sysdate) or (end_time < trunc(sysdate) and status =4))";
        PreparedStatement pstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
//            pstmt.setDate(1, new java.sql.Date(startTime.getTime()));
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("INSERT CONFIG KPI STORAGE SUCCESS!");
        } catch (Exception ex) {
            logger.info("INSERT CONFIG KPI STORAGE FAIL!");
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
                    logger.info("INSERT CONFIG KPI STORAGE FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("INSERT FINISH!");
    }

    public synchronized void insertStockTotal(List<StockGoodsTotalBO> lstDebt, Logger logger, long maxBatchSize, Date startTime) {
        logger.info("INSERT START!");

        String sql = " insert into KPI_STORAGE_AMOUNT"
                + " (KPI_STORAGE_ID ,"
                + "STOCK_ID ,"
                + "GOODS_ID ,"
                + "GOODS_STATE ,"
                + "GOODS_STATE_NAME ,"
                + "GOODS_CODE ,"
                + "GOODS_NAME ,"
                + "GOODS_TYPE ,"
                + "GOODS_IS_SERIAL ,"
                + "GOODS_UNIT_ID ,"
                + "GOODS_UNIT_NAME ,"
                + "CHANGE_DATE ,"
                + "AMOUNT_REMAIN ,"
                + "AMOUNT_QUOTA ,"
                + "AMOUNT_KPI ,"
                + "GOODS_TYPE_NAME ,"
                + "STOCK_CODE ,"
                + "STOCK_NAME ) "
                + " values (KPI_STORAGE_AMOUNT_SEQ.nextval, ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?,?,?,?,?,?)";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            int count = 0;
            int size = lstDebt.size();
            int group = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            Date sqlDate = new Date();
            sqlDate = sdf.parse(sdf.format(sqlDate));
            for (StockGoodsTotalBO obj : lstDebt) {
                int i = 1;
                try {
                    group++;
                    count++;
                    pstmt.setLong(i++, obj.getStockId());
                    pstmt.setLong(i++, obj.getGoodsId());
                    pstmt.setString(i++, obj.getGoodsState());
                    pstmt.setString(i++, obj.getGoodsStateName());
                    pstmt.setString(i++, obj.getGoodsCode());
                    pstmt.setString(i++, obj.getGoodsName());
                    pstmt.setLong(i++, obj.getGoodsType());
                    pstmt.setString(i++, obj.getGoodsIsSerial());
                    pstmt.setLong(i++, obj.getGoodsUnitId());
                    pstmt.setString(i++, obj.getGoodsUnitName());
                    pstmt.setDate(i++, new java.sql.Date(sqlDate.getTime()));
                    pstmt.setDouble(i++, obj.getAmountRemain());
                    pstmt.setDouble(i++, obj.getAmountQuota());
                    pstmt.setDouble(i++, obj.getAmountKpi());
                    pstmt.setString(i++, obj.getGoodsTypeName());
                    pstmt.setString(i++, obj.getStockCode());
                    pstmt.setString(i++, obj.getStockName());
                    pstmt.addBatch();
                    if (group == maxBatchSize) {
                        logger.info("KPI_STORAGE_AMOUNT INSERTED " + count + "/" + size);
                        group = 0;
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                    if (group < maxBatchSize && count == size) {
                        logger.info("syn_stock_total INSERTED " + count + "/" + size);
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                } catch (Exception ex) {
                    logger.info("KPI_STORAGE_AMOUNT INSERT INTO DB FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }
//            pstmt.executeBatch();
//            logger.info("syn_stock_total INSERTED " + count + "/" + size);
//            con.setAutoCommit(false);
//            this.commit(con);
            logger.info("KPI_STORAGE_AMOUNT INSERT INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("KPI_STORAGE_AMOUNT INSERT INTO DB FAIL!");
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
                    logger.info("KPI_STORAGE_AMOUNT INSERT INTO DB FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("INSERT FINISH!");
    }

    public synchronized void insertKpiStorageTime(List<MerEntityBO> lstDebt, Logger logger, long maxBatchSize, Date startTime, int timeQuota) {
        logger.info("INSERT START!");

        String sql = " insert into KPI_STORAGE_TIME"
                + " (STOCK_GOODS_SERIAL_ID ,"
                + "STOCK_ID ,"
                + "GOODS_ID ,"
                + "GOODS_STATE ,"
                + "GOODS_STATE_NAME ,"
                + "GOODS_CODE ,"
                + "GOODS_NAME ,"
                + "GOODS_TYPE ,"
                + "GOODS_TYPE_NAME ,"
                + "GOODS_UNIT_ID ,"
                + "GOODS_UNIT_NAME ,"
                + "IMPORT_DATE ,"
                + "ORDER_ID ,"
                + "IMPORT_STOCK_TRANS_ID ,"
                + "STATUS ,"
                + "SERIAL ,"
                + "PRICE ,"
                + "CONTRACT_CODE,"
                + "SHIPMENT_CODE,"
                + "PROJECT_CODE,"
                + "PART_NUMBER,"
                + "MANUFACTURER_ID,"
                + "MANUFACTURER_NAME,"
                + "PRODUCER_CONTRY_ID,"
                + "PRODUCER_CONTRY_NAME,"
                + "CHANGE_DATE,"
                + "TIME_QUOTA,"
                + "TIME_STORAGE,"
                + "TIME_KPI,"
                + "AMOUNT ) "
                + " values (KPI_STORAGE_TIME_SEQ.nextval, ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?,?,?,?,?,?,? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?)";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            int count = 0;
            int size = lstDebt.size();
            int group = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            Date sqlDate = new Date();
            sqlDate = sdf.parse(sdf.format(sqlDate));
            for (MerEntityBO obj : lstDebt) {
                int i = 1;
                try {
                    group++;
                    count++;
                    pstmt.setString(i++, obj.getStockId());
                    pstmt.setLong(i++, obj.getGoodsId());
                    pstmt.setString(i++, obj.getState());
                    pstmt.setString(i++, "");
                    pstmt.setString(i++, obj.getGoodsCode());
                    pstmt.setString(i++, obj.getGoodsName());
                    pstmt.setLong(i++, obj.getGoodsType());//GOODS_TYPE
                    pstmt.setString(i++, "");//GOODS_TYPE_NAME
                    pstmt.setLong(i++, obj.getCatUnitId());
                    pstmt.setString(i++, obj.getCatUnitName());
                    if (obj.getImportDate() != null) {
                        java.sql.Date sqlDate1 = new java.sql.Date(obj.getImportDate().getTime());
                        pstmt.setDate(i++, sqlDate1);
                    } else {
                        pstmt.setDate(i++, null);
                    }
                    pstmt.setLong(i++, obj.getOrderId());
                    pstmt.setString(i++, obj.getImportStockTransId());//IMPORT_STOCK_TRANS_ID
                    pstmt.setString(i++, obj.getStatus());
                    pstmt.setString(i++, obj.getSerial());
                    pstmt.setDouble(i++, obj.getUnitPrice());
                    pstmt.setString(i++, obj.getCntContractCode());
                    pstmt.setString(i++, obj.getShipmentCode());
                    pstmt.setString(i++, obj.getProjectCode());
                    pstmt.setString(i++, obj.getPartNumber());
                    pstmt.setLong(i++, obj.getManufaceturerId());
                    pstmt.setString(i++, obj.getManufacturerName());
                    pstmt.setLong(i++, obj.getProducingCountryId());
                    pstmt.setString(i++, obj.getProducingCountryName());
//                    pstmt.setDate(i++, (java.sql.Date) sqlDate);
                    pstmt.setDate(i++, new java.sql.Date(sqlDate.getTime()));
                    pstmt.setInt(i++, timeQuota);
                    int timeStorage = (int) ((sqlDate.getTime() - obj.getUpdatedDate().getTime()) / (1000 * 60 * 60 * 24));
                    pstmt.setInt(i++, timeStorage);
                    pstmt.setInt(i++, (timeQuota - timeStorage));
                    pstmt.setDouble(i++, obj.getAmount());
                    pstmt.addBatch();
                    if (group == maxBatchSize) {
                        logger.info("KPI_STORAGE_TIME INSERTED " + count + "/" + size);
                        group = 0;
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                    if (group < maxBatchSize && count == size) {
                        logger.info("KPI_STORAGE_TIME INSERTED " + count + "/" + size);
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                } catch (Exception ex) {
                    logger.info("KPI_STORAGE_TIME INSERT INTO DB FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }
//            pstmt.executeBatch();
//            logger.info("syn_stock_total INSERTED " + count + "/" + size);
//            con.setAutoCommit(false);
//            this.commit(con);
            logger.info("KPI_STORAGE_TIME INSERT INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("KPI_STORAGE_TIME INSERT INTO DB FAIL!");
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
                    logger.info("KPI_STORAGE_TIME INSERT INTO DB FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("INSERT FINISH!");
    }

}
