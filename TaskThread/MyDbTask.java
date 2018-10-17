/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.merHistory;

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

   public ArrayList<MerHistoryBO> getListMerEntity() throws Exception {
        ArrayList<MerHistoryBO> lstDebt = new ArrayList<MerHistoryBO>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String strSql = "SELECT MER_ENTITY_ID merEntityId,\n"+
                        "  SERIAL serial,\n" +
                        "  GOODS_ID goodsId,\n" +
                        "  GOODS_CODE goodsCode,\n" +
                        "  GOODS_NAME goodsName,\n" +
                        "  STATE state,\n" +
                        "  STATUS status,\n" +
                        "  AMOUNT amount,\n" +
                        "  CAT_MANUFACTURER_ID catManufacturerId,\n" +
                        "  STOCK_ID stockId,\n" +
                        "  CNT_CONTRACT_ID cntContractId,\n" +
                        "  SYS_GROUP_ID sysGroupId,\n" +
                        "  PROJECT_ID projectId,\n" +
                        "  SHIPMENT_ID shipmentId,\n" +
                        "  PART_NUMBER partNumber,\n" +
                        "  UNIT_PRICE unitPrice,\n" +
                        "  APPLY_PRICE applyPrice,\n" +
                        "  MANUFACTURER_NAME manufacturerName,\n" +
                        "  PRODUCING_COUNTRY_NAME producingCountryName,\n" +
                        "  CAT_UNIT_ID catUnitId,\n" +
                        "  ORDER_ID orderId,\n" +
                        "  CNT_CONTRACT_CODE cntContractCode,\n" +
                        "  IMPORT_DATE importDate,\n" +
                        "  UPDATED_DATE updatedDate,\n" +
                        "  STOCK_CELL_ID stockCellId,\n" +
                        "  STOCK_CELL_CODE stockCellCode,\n" +
                        "  CAT_PRODUCING_COUNTRY_ID catProducingCountryId,\n" +
                        "  PARENT_MER_ENTITY_ID parentMerEntityId,\n" +
                        "  CAT_UNIT_NAME catUnitName,\n" +
                        "  EXPORT_DATE exportDate,\n" +
                        "  IMPORT_STOCK_TRANS_ID importStockTransId"
                        + " FROM MER_ENTITY";
        Connection connection = null;
        try {
            connection = getConnection();
            preStmt = connection.prepareStatement(strSql);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                MerHistoryBO frm = new MerHistoryBO();
                frm.setMerEntityId(rs.getLong("merEntityId"));
                frm.setSerial(rs.getString("serial"));
                frm.setGoodsId(rs.getLong("goodsId"));
                frm.setGoodsCode(rs.getString("goodsCode"));
                frm.setGoodsName(rs.getString("goodsName"));
                frm.setState(rs.getString("state"));
                frm.setStatus(rs.getString("status"));
                frm.setAmount(rs.getString("amount"));
                frm.setCatManufacturerId(rs.getLong("catManufacturerId"));
                frm.setStockId(rs.getLong("stockId"));
                frm.setCntContractId(rs.getLong("cntContractId"));
                frm.setSysGroupId(rs.getLong("sysGroupId"));
                frm.setProjectId(rs.getLong("projectId"));
                frm.setShipmentId(rs.getLong("shipmentId"));
                frm.setPartNumber(rs.getString("partNumber"));
                frm.setUnitPrice(rs.getString("unitPrice"));
                frm.setApplyPrice(rs.getString("applyPrice"));
                frm.setManufacturerName(rs.getString("manufacturerName"));
                frm.setProducingCountryName(rs.getString("producingCountryName"));
                frm.setCatUnitId(rs.getLong("catUnitId"));
                frm.setOrderId(rs.getLong("orderId"));
                frm.setCntContractCode(rs.getString("cntContractCode"));
                frm.setImportDate(rs.getDate("importDate"));
                frm.setUpdatedDate(rs.getDate("updatedDate"));
                frm.setStockCellId(rs.getLong("stockCellId"));
                frm.setStockCellCode(rs.getString("stockCellCode"));
                frm.setCatProducingCountryId(rs.getLong("catProducingCountryId"));
                frm.setParentMerEntityId(rs.getLong("parentMerEntityId"));
                frm.setCatUnitName(rs.getString("catUnitName"));
                frm.setExportDate(rs.getDate("exportDate"));
                frm.setImportStockTransId(rs.getLong("importStockTransId"));
                
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
   
   public ArrayList<StockGoodsTotalBO> getListStockGoodsTotal() throws Exception {
        ArrayList<StockGoodsTotalBO> lstDebt = new ArrayList<StockGoodsTotalBO>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        String strSql = "SELECT\n" +
                        "STOCK_GOODS_TOTAL_ID stockGoodsTotalId,\n" +
                        "STOCK_ID stockId,\n" +
                        "GOODS_ID goodsId,\n" +
                        "GOODS_STATE goodsState,\n" +
                        "GOODS_STATE_NAME goodsStateName,\n" +
                        "GOODS_CODE goodsCode,\n" +
                        "GOODS_NAME goodsName,\n" +
                        "GOODS_TYPE goodsType,\n" +
                        "GOODS_IS_SERIAL goodsIsSerial,\n" +
                        "GOODS_UNIT_ID goodsUnitId,\n" +
                        "GOODS_UNIT_NAME goodsUnitName,\n" +
                        "AMOUNT amount,\n" +
                        "CHANGE_DATE changeDate,\n" +
                        "GOODS_TYPE_NAME goodsTypeName,\n" +
                        "AMOUNT_ISSUE amountIssue,\n" +
                        "STOCK_CODE stockCode,\n" +
                        "STOCK_NAME stockName FROM STOCK_GOODS_TOTAL";
        Connection connection = null;
        try {
            connection = getConnection();
            preStmt = connection.prepareStatement(strSql);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                StockGoodsTotalBO frm = new StockGoodsTotalBO();
                frm.setStockGoodsTotalId(rs.getLong("stockGoodsTotalId"));
                frm.setStockId(rs.getLong("stockId"));
                frm.setGoodsId(rs.getLong("goodsId"));
                frm.setGoodsState(rs.getString("goodsState"));
                frm.setGoodsStateName(rs.getString("goodsStateName"));
                frm.setGoodsCode(rs.getString("goodsCode"));
                frm.setGoodsName(rs.getString("goodsName"));
                frm.setGoodsType(rs.getString("goodsType"));
                frm.setGoodsIsSerial(rs.getString("goodsIsSerial"));
                frm.setGoodsUnitId(rs.getLong("goodsUnitId"));
                frm.setGoodsUnitName(rs.getString("goodsUnitName"));
                frm.setAmount(rs.getString("amount"));
                frm.setChangeDate(rs.getDate("changeDate"));
                frm.setGoodsTypeName(rs.getString("goodsTypeName"));
                frm.setAmountIssue(rs.getString("amountIssue"));
                frm.setStockCode(rs.getString("stockCode"));
                frm.setStockName(rs.getString("stockName"));
                
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
   
   public void deleteMerHistory(String backDateReport,Logger logger, long maxBatchSize) {
        logger.info("DELETE START!");
        String sql = " DELETE FROM MER_HISTORY RE WHERE trunc(RE.CREATED_DATE) = ? ";
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
            logger.info("MER_HISTORY DELETE INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("MER_HISTORY DELETE INTO DB FAIL!");
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
   
   public void deleteStockGoodsTotalHistory(String backDateReport,Logger logger, long maxBatchSize) {
        logger.info("DELETE START!");
        String sql = " DELETE FROM STOCK_GOODS_TOTAL_HISTORY ST WHERE trunc(ST.CREATED_DATE) = ? ";
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
            logger.info("STOCK_GOODS_TOTAL_HISTORY DELETE INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("STOCK_GOODS_TOTAL_HISTORY DELETE INTO DB FAIL!");
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
   public synchronized void insertMerHistory(ArrayList<MerHistoryBO> lstDebt,Logger logger, long maxBatchSize) {
        logger.info("INSERT START!");
        String sql = "INSERT INTO MER_HISTORY (MER_HISTORY_ID,\n" +
                    "MER_ENTITY_ID,\n" +
                    "SERIAL,\n" +
                    "GOODS_ID,\n" +
                    "GOODS_CODE,\n" +
                    "GOODS_NAME,\n" +
                    "STATE,\n" +
                    "AMOUNT,\n" +
                    "CAT_MANUFACTURER_ID,\n" +
                    "STOCK_ID,\n" +
                    "CNT_CONTRACT_ID,\n" +
                    "SYS_GROUP_ID,\n" +
                    "PROJECT_ID,\n" +
                    "SHIPMENT_ID,\n" +
                    "PART_NUMBER,\n" +
                    "UNIT_PRICE,\n" +
                    "APPLY_PRICE,\n" +
                    "MANUFACTURER_NAME,\n" +
                    "PRODUCING_COUNTRY_NAME,\n" +
                    "CAT_UNIT_ID,\n" +
                    "ORDER_ID,\n" +
                    "CNT_CONTRACT_CODE,\n" +
                    "IMPORT_DATE,\n" +
                    "UPDATED_DATE,\n" +
                    "STOCK_CELL_ID,\n" +
                    "STOCK_CELL_CODE,\n" +
                    "CAT_PRODUCING_COUNTRY_ID,\n" +
                    "PARENT_MER_ENTITY_ID,\n" +
                    "CAT_UNIT_NAME,\n" +
                    "EXPORT_DATE,\n" +
                    "IMPORT_STOCK_TRANS_ID,\n" +
                    "CREATED_DATE,\n" +
                    "STATUS)\n"
                + "VALUES (MER_HISTORY_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            int count = 0;
            int size = lstDebt.size();
            int group = 0;
            
            for (MerHistoryBO obj : lstDebt) {
                int i = 1;
                try {
                    group++;
                    count++;
                    
                    pstmt.setLong(i++, obj.getMerEntityId());
                    pstmt.setString(i++, obj.getSerial());
                    pstmt.setLong(i++, obj.getGoodsId());
                    pstmt.setString(i++, obj.getGoodsCode());
                    pstmt.setString(i++, obj.getGoodsName());
                    pstmt.setString(i++, obj.getState());
                    pstmt.setString(i++, obj.getAmount());
                    pstmt.setLong(i++, obj.getCatManufacturerId());
                    pstmt.setLong(i++, obj.getStockId());
                    pstmt.setLong(i++, obj.getCntContractId());
                    pstmt.setLong(i++, obj.getSysGroupId());
                    pstmt.setLong(i++, obj.getProjectId());
                    pstmt.setLong(i++, obj.getShipmentId());
                    pstmt.setString(i++, obj.getPartNumber());
                    pstmt.setString(i++, obj.getUnitPrice());
                    pstmt.setString(i++, obj.getApplyPrice());
                    pstmt.setString(i++, obj.getManufacturerName());
                    pstmt.setString(i++, obj.getProducingCountryName());
                    pstmt.setLong(i++, obj.getCatUnitId());
                    pstmt.setLong(i++, obj.getOrderId());
                    pstmt.setString(i++, obj.getCntContractCode());
                    pstmt.setDate(i++, (java.sql.Date) obj.getImportDate());
                    pstmt.setDate(i++, (java.sql.Date) obj.getUpdatedDate());
                    pstmt.setLong(i++, obj.getStockCellId());
                    pstmt.setString(i++, obj.getStockCellCode());
                    pstmt.setLong(i++, obj.getCatProducingCountryId());
                    pstmt.setLong(i++, obj.getParentMerEntityId());
                    pstmt.setString(i++, obj.getCatUnitName());
                    pstmt.setDate(i++, (java.sql.Date) obj.getExportDate());
                    pstmt.setLong(i++, obj.getImportStockTransId());
                    java.util.Date call1 = new Date();
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(call1);
                    cal1.add(Calendar.DATE, 0);
                    java.sql.Date createdDate= new java.sql.Date(cal1.getTime().getTime());
                    pstmt.setDate(i++, createdDate);
                    pstmt.setString(i++, obj.getStatus());
                    pstmt.addBatch();
                    if (group == maxBatchSize) {
                        logger.info("MER_HISTORY INSERTED " + count + "/" + size);
                        group = 0;
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                    if (group < maxBatchSize && count == size) {
                        logger.info("MER_HISTORY INSERTED " + count + "/" + size);
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                } catch (Exception ex) {
                    logger.info("MER_HISTORY INSERT INTO DB FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }
            logger.info("MER_HISTORY INSERT INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("MER_HISTORY INSERT INTO DB FAIL!");
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
                    logger.info("MER_HISTORY INSERT INTO DB FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("INSERT FINISH!");
    }
   
   public synchronized void insertStockGoodsTotalHistory(ArrayList<StockGoodsTotalBO> lstDebt,Logger logger, long maxBatchSize) {
        logger.info("INSERT START!");
        String sql = "INSERT INTO STOCK_GOODS_TOTAL_HISTORY (STOCK_GOODS_TOTAL_HISTORY_ID,\n" +
                    "STOCK_GOODS_TOTAL_ID,\n" +
                    "STOCK_ID,\n" +
                    "GOODS_ID,\n" +
                    "GOODS_STATE,\n" +
                    "GOODS_STATE_NAME,\n" +
                    "GOODS_CODE,\n" +
                    "GOODS_NAME,\n" +
                    "GOODS_TYPE,\n" +
                    "GOODS_IS_SERIAL,\n" +
                    "GOODS_UNIT_ID,\n" +
                    "GOODS_UNIT_NAME,\n" +
                    "AMOUNT,\n" +
                    "CHANGE_DATE,\n" +
                    "GOODS_TYPE_NAME,\n" +
                    "AMOUNT_ISSUE,\n" +
                    "STOCK_CODE,\n" +
                    "STOCK_NAME,\n" +
                    "CREATED_DATE)\n"
                + "VALUES (STOCK_GOODS_TOTAL_HISTORY_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            int count = 0;
            int size = lstDebt.size();
            int group = 0;
            
            for (StockGoodsTotalBO obj : lstDebt) {
                int i = 1;
                try {
                    group++;
                    count++;
                    
                    pstmt.setLong(i++, obj.getStockGoodsTotalId());
                    pstmt.setLong(i++, obj.getStockId());
                    pstmt.setLong(i++, obj.getGoodsId());
                    pstmt.setString(i++, obj.getGoodsState());
                    pstmt.setString(i++, obj.getGoodsStateName());
                    pstmt.setString(i++, obj.getGoodsCode());
                    pstmt.setString(i++, obj.getGoodsName());
                    pstmt.setString(i++, obj.getGoodsType());
                    pstmt.setString(i++, obj.getGoodsIsSerial());
                    pstmt.setLong(i++, obj.getGoodsUnitId());
                    pstmt.setString(i++, obj.getGoodsUnitName());
                    pstmt.setString(i++, obj.getAmount());
                    pstmt.setDate(i++, (java.sql.Date) obj.getChangeDate());
                    pstmt.setString(i++, obj.getGoodsTypeName());
                    pstmt.setString(i++, obj.getAmountIssue());
                    pstmt.setString(i++, obj.getStockCode());
                    pstmt.setString(i++, obj.getStockName());
                    java.util.Date call1 = new Date();
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(call1);
                    cal1.add(Calendar.DATE, 0);
                    java.sql.Date createdDate= new java.sql.Date(cal1.getTime().getTime());
                    pstmt.setDate(i++, createdDate);
                    
                    pstmt.addBatch();
                    if (group == maxBatchSize) {
                        logger.info("STOCK_GOODS_TOTAL_HISTORY INSERTED " + count + "/" + size);
                        group = 0;
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                    if (group < maxBatchSize && count == size) {
                        logger.info("STOCK_GOODS_TOTAL_HISTORY INSERTED " + count + "/" + size);
                        pstmt.executeBatch();
                        con.setAutoCommit(false);
                        this.commit(con);
                    }
                } catch (Exception ex) {
                    logger.info("STOCK_GOODS_TOTAL_HISTORY INSERT INTO DB FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }
            logger.info("STOCK_GOODS_TOTAL_HISTORY INSERT INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("MER_HISTORY INSERT INTO DB FAIL!");
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
                    logger.info("STOCK_GOODS_TOTAL_HISTORY INSERT INTO DB FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("INSERT FINISH!");
    }

}
