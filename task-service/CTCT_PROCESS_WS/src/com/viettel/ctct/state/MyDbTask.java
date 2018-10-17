/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.state;

import com.viettel.framework.service.common.DbTask;
import com.viettel.qldtktts.webservice.CatStationBO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import org.apache.log4j.Logger;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

//  @author hoanm1
public class MyDbTask extends DbTask {

    public synchronized void updateStateDueContract(Date startTime, Logger logger) {
        logger.info("UPDATE START!");
        String sql = " update cnt_contract set state=1 "
                + " where START_TIME >=? and (end_time >= trunc(sysdate) or (end_time < trunc(sysdate) and status =4))";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(startTime.getTime()));
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("UPDATE STATE DUE CONTRACT SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE STATE DUE CONTRACT FAIL!");
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
                    logger.info("UPDATE STATE DUE CONTRACT FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE FINISH!");
    }

    public synchronized void updateStateOverDueContract(Date startTime, Logger logger) {
        logger.info("UPDATE START!");
        String sql = " update cnt_contract set state=2 "
                + " where START_TIME >=? and (end_time < trunc(sysdate) and status !=4)";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(startTime.getTime()));
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("UPDATE STATE OVER DUE CONTRACT SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE STATE OVER DUE CONTRACT FAIL!");
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
                    logger.info("UPDATE STATE OVER DUE CONTRACT FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE FINISH!");
    }

    public synchronized void updateStateDueConstruction(Date startTime, Logger logger) {
        logger.info("UPDATE START!");
        String sql = " update construction set CONSTRUCTION_STATE=1 "
                + " where EXCPECTED_STARTING_DATE>=? and (EXCPECTED_COMPLETE_DATE >= trunc(sysdate) or"
                + " (EXCPECTED_COMPLETE_DATE < trunc(sysdate) and status =6))";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(startTime.getTime()));
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("UPDATE STATE DUE CONSTRUCTION SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE STATE DUE CONSTRUCTION FAIL!");
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
                    logger.info("UPDATE STATE DUE CONSTRUCTION FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE FINISH!");
    }

    public synchronized void updateStateOverDueConstruction(Date startTime, Logger logger) {
        logger.info("UPDATE START!");
        String sql = " update construction set CONSTRUCTION_STATE=2 "
                + " where EXCPECTED_STARTING_DATE>=? and (EXCPECTED_COMPLETE_DATE < trunc(sysdate) and status !=6)";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(startTime.getTime()));
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("UPDATE STATE OVER DUE CONSTRUCTION SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE STATE OVER DUE CONSTRUCTION FAIL!");
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
                    logger.info("UPDATE STATE OVER DUE CONSTRUCTION FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE FINISH!");
    }

    public synchronized void updateStateDueConstructionTask(Date startTime, Logger logger) {
        logger.info("UPDATE START!");
        String sql = " update CONSTRUCTION_TASK set COMPLETE_STATE=1 "
                + " where START_DATE >=? and (end_date >= trunc(sysdate) or (end_date < trunc(sysdate) and status not in(1,2)))";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(startTime.getTime()));
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("UPDATE STATE DUE CONSTRUCTION TASK SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE STATE DUE CONSTRUCTION TASK FAIL!");
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
                    logger.info("UPDATE STATE DUE CONSTRUCTION TASK FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE FINISH!");
    }

    public synchronized void updateStateOverDueConstructionTask(Date startTime, Logger logger) {
        logger.info("UPDATE START!");
        String sql = " update CONSTRUCTION_TASK set COMPLETE_STATE=2 "
                + " where START_DATE >=? and (end_date < trunc(sysdate) and status in (1,2))";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setDate(1, new java.sql.Date(startTime.getTime()));
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("UPDATE STATE OVER DUE CONSTRUCTION TASK SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE STATE OVER DUE CONSTRUCTION TASK FAIL!");
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
                    logger.info("UPDATE STATE OVER DUE CONSTRUCTION TASK FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE FINISH!");
    }

    public ArrayList<StockTotalForm> getListDebt(Date startTime) throws Exception {
        ArrayList<StockTotalForm> lstDebt = new ArrayList<StockTotalForm>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
//        String strSql = " select '1' type,b.goods_id goodsId,b.GOODS_NAME goodsName,b.Goods_code goodsCode,b.GOODS_UNIT_NAME goodsUnitName,b.goods_state goodsState,\n"
//                + " a.CONSTRUCTION_id constructionId,a.CONSTRUCTION_CODE constructionCode,a.last_shipper_id sysUserId,\n"
//                + " to_number((select Sys_group_id from construction cst where cst.CONSTRUCTION_ID=a.CONSTRUCTION_ID and cst.status !=0)) sysGroupId,b.SERIAL,\n"
//                + " sum(nvl(b.AMOUNT,0)- \n"
//                + " nvl((select nvl(cst_mer.QUANTITY,0)\n"
//                + " from construction_merchandise cst_mer where cst_mer.type=1 and cst_mer.CONSTRUCTION_ID=a.CONSTRUCTION_ID\n"
//                + " and cst_mer.GOODS_CODE=b.GOODS_CODE and nvl(cst_mer.SERIAL,1)=nvl(b.SERIAL,1) and cst_mer.mer_entity_id=b.mer_entity_id) ,0)-\n"
//                + " nvl((select nvl(cst_mer.REMAIN_COUNT,0)\n"
//                + " from construction_merchandise cst_mer  where cst_mer.type=1\n"
//                + " and cst_mer.CONSTRUCTION_ID=a.CONSTRUCTION_ID\n"
//                + " and cst_mer.GOODS_CODE=b.GOODS_CODE and nvl(cst_mer.SERIAL,1)=nvl(b.SERIAL,1) and cst_mer.mer_entity_id=b.mer_entity_id ),0)-\n"
//                + " nvl((select nvl(syn_detail.AMOUNT,0)\n"
//                + " from syn_stock_trans syn,\n"
//                + " syn_stock_trans_detail_serial syn_detail\n"
//                + " where syn.SYN_STOCK_TRANS_ID=syn_detail.SYN_STOCK_TRANS_ID and\n"
//                + " syn.type=1 and syn.STATUS=1 and syn.CONSTRUCTION_ID=a.CONSTRUCTION_ID and syn_detail.GOODS_CODE=b.GOODS_CODE \n"
//                + " and syn_detail.mer_entity_id=b.mer_entity_id and nvl(syn_detail.SERIAL,1)=nvl(b.SERIAL,1)),0)) amount\n"
//                + " from syn_stock_trans a,\n"
//                + " syn_stock_trans_detail_serial b,syn_stock_trans_detail c\n"
//                + " where a.SYN_STOCK_TRANS_ID=b.SYN_STOCK_TRANS_ID and b.syn_stock_trans_detail_id=c.syn_stock_trans_detail_id\n"
//                + " and a.confirm=1 and a.type=2\n"
//                + " and REAL_IE_TRANS_DATE >=?\n"
//                + " group by b.goods_id,b.GOODS_NAME,b.Goods_code,b.GOODS_UNIT_NAME,b.goods_state,\n"
//                + " a.CONSTRUCTION_id,a.CONSTRUCTION_CODE,a.last_shipper_id,b.SERIAL\n"
//                + " union all\n"
//                + " select \n"
//                + " '2' type,b.goods_id goodsId,b.GOODS_NAME goodsName,b.Goods_code goodsCode,b.GOODS_UNIT_NAME goodsUnitName,b.goods_state goodsState,\n"
//                + " a.CONSTRUCTION_id constructionId,(select code from construction cst where cst.CONSTRUCTION_ID=a.CONSTRUCTION_ID and cst.status !=0)constructionCode,\n"
//                + " a.last_shipper_id sysUserId,a.DEPT_RECEIVE_ID sysGroupId,c.SERIAL,\n"
//                + " sum(\n"
//                + " nvl(c.QUANTITY,0) -\n"
//                + " nvl((select asset_entity.QUANTITY\n"
//                + " from asset_management_request asset,ASSET_MANAGE_REQUEST_ENTITY asset_entity,mer_entity  where \n"
//                + " asset.asset_management_request_id=asset_entity.asset_management_request_id and asset_entity.mer_entity_id=mer_entity.mer_entity_id\n"
//                + " and asset.STATUS=3 and asset.CONSTRUCTION_ID=a.CONSTRUCTION_ID and mer_entity.GOODS_CODE=b.GOODS_CODE and \n"
//                + " nvl(mer_entity.SERIAL,1) = nvl(c.SERIAL,1) and mer_entity.mer_entity_id=c.mer_entity_id),0) - \n"
//                + " nvl((select nvl(cst_mer.QUANTITY,0)\n"
//                + " from construction_merchandise cst_mer where cst_mer.type=2 and cst_mer.CONSTRUCTION_ID=a.CONSTRUCTION_ID and\n"
//                + " cst_mer.GOODS_CODE=b.GOODS_CODE and nvl(cst_mer.SERIAL,1)=nvl(c.SERIAL,1) and cst_mer.mer_entity_id=c.mer_entity_id),0)) amount\n"
//                + " from stock_trans a,\n"
//                + " stock_trans_detail b,\n"
//                + " stock_trans_detail_serial c\n"
//                + " where a.stock_trans_id=b.stock_trans_id and b.stock_trans_detail_id=c.stock_trans_detail_id and \n"
//                + " a.confirm=1 and a.type = 2 \n"
//                + " and REAL_IE_TRANS_DATE >=?\n"
//                + " group by b.goods_id,b.GOODS_NAME,b.Goods_code,b.GOODS_UNIT_NAME,b.goods_state,\n"
//                + " a.CONSTRUCTION_id,a.last_shipper_id,a.DEPT_RECEIVE_ID,c.SERIAL";
        String strSql = "select '1' type,b.goods_id goodsId,b.GOODS_NAME goodsName,b.Goods_code goodsCode,b.GOODS_UNIT_NAME goodsUnitName,b.goods_state goodsState,\n"
                + "a.CONSTRUCTION_id constructionId,a.CONSTRUCTION_CODE constructionCode,a.last_shipper_id sysUserId,\n"
                + "to_number((select Sys_group_id from construction cst where cst.CONSTRUCTION_ID=a.CONSTRUCTION_ID and cst.status !=0)) sysGroupId,b.SERIAL,\n"
                + "(select prov.cat_province_id from construction cst,cat_station st,cat_province prov where \n"
                + "cst.CONSTRUCTION_ID=a.CONSTRUCTION_ID and cst.status !=0 and cst.cat_station_id=st.cat_station_id\n"
                + "and st.cat_province_id=prov.cat_province_id)provinceId,\n"
                + "(select prov.code from construction cst,cat_station st,cat_province prov where \n"
                + "cst.CONSTRUCTION_ID=a.CONSTRUCTION_ID and cst.status !=0 and cst.cat_station_id=st.cat_station_id\n"
                + "and st.cat_province_id=prov.cat_province_id)provinceCode,\n"
                + "\n"
                + "sum(nvl(b.AMOUNT,0)- \n"
                + " \n"
                + " nvl((select nvl(cst_mer.QUANTITY,0)\n"
                + "from construction_merchandise cst_mer where cst_mer.type=1 and cst_mer.CONSTRUCTION_ID=a.CONSTRUCTION_ID\n"
                + "and cst_mer.GOODS_CODE=b.GOODS_CODE and nvl(cst_mer.SERIAL,1)=nvl(b.SERIAL,1) and cst_mer.mer_entity_id=b.mer_entity_id) ,0)-\n"
                + "\n"
                + "--nvl((select nvl(cst_mer.REMAIN_COUNT,0)\n"
                + "--from construction_merchandise cst_mer  where cst_mer.type=1\n"
                + "--and cst_mer.CONSTRUCTION_ID=a.CONSTRUCTION_ID\n"
                + "--and cst_mer.GOODS_CODE=b.GOODS_CODE and nvl(cst_mer.SERIAL,1)=nvl(b.SERIAL,1) and cst_mer.mer_entity_id=b.mer_entity_id ),0)-\n"
                + "\n"
                + "nvl((select nvl(syn_detail.AMOUNT,0)\n"
                + "\n"
                + "from syn_stock_trans syn,\n"
                + "syn_stock_trans_detail_serial syn_detail\n"
                + "where syn.SYN_STOCK_TRANS_ID=syn_detail.SYN_STOCK_TRANS_ID and\n"
                + "syn.type=1 and syn.STATUS=1 and syn.CONSTRUCTION_ID=a.CONSTRUCTION_ID and syn_detail.GOODS_CODE=b.GOODS_CODE \n"
                + " and syn_detail.mer_entity_id=b.mer_entity_id and nvl(syn_detail.SERIAL,1)=nvl(b.SERIAL,1)),0)) amount,\n"
                + "\n"
                + " sum(nvl(b.AMOUNT*b.UNIT_PRICE,0)- \n"
                + " \n"
                + " nvl((select nvl(cst_mer.QUANTITY * b.UNIT_PRICE,0)\n"
                + "from construction_merchandise cst_mer where cst_mer.type=1 and cst_mer.CONSTRUCTION_ID=a.CONSTRUCTION_ID\n"
                + "and cst_mer.GOODS_CODE=b.GOODS_CODE and nvl(cst_mer.SERIAL,1)=nvl(b.SERIAL,1) and cst_mer.mer_entity_id=b.mer_entity_id) ,0)-\n"
                + "\n"
                + "--nvl((select nvl(cst_mer.REMAIN_COUNT,0)\n"
                + "--from construction_merchandise cst_mer  where cst_mer.type=1\n"
                + "--and cst_mer.CONSTRUCTION_ID=a.CONSTRUCTION_ID\n"
                + "--and cst_mer.GOODS_CODE=b.GOODS_CODE and nvl(cst_mer.SERIAL,1)=nvl(b.SERIAL,1) and cst_mer.mer_entity_id=b.mer_entity_id ),0)-\n"
                + "\n"
                + "nvl((select nvl(syn_detail.AMOUNT * b.UNIT_PRICE,0)\n"
                + "\n"
                + "from syn_stock_trans syn,\n"
                + "syn_stock_trans_detail_serial syn_detail\n"
                + "where syn.SYN_STOCK_TRANS_ID=syn_detail.SYN_STOCK_TRANS_ID and\n"
                + "syn.type=1 and syn.STATUS=1 and syn.CONSTRUCTION_ID=a.CONSTRUCTION_ID and syn_detail.GOODS_CODE=b.GOODS_CODE \n"
                + " and syn_detail.mer_entity_id=b.mer_entity_id and nvl(syn_detail.SERIAL,1)=nvl(b.SERIAL,1)),0)) price\n"
                + "\n"
                + "from syn_stock_trans a,\n"
                + "syn_stock_trans_detail_serial b,syn_stock_trans_detail c\n"
                + "where a.SYN_STOCK_TRANS_ID=b.SYN_STOCK_TRANS_ID and b.syn_stock_trans_detail_id=c.syn_stock_trans_detail_id\n"
                + "and a.confirm=1 and a.type=2\n"
                + "and REAL_IE_TRANS_DATE >= ?\n"
                + "group by b.goods_id,b.GOODS_NAME,b.Goods_code,b.GOODS_UNIT_NAME,b.goods_state,\n"
                + "a.CONSTRUCTION_id,a.CONSTRUCTION_CODE,a.last_shipper_id,b.SERIAL\n"
                + "\n"
                + "union all\n"
                + "select \n"
                + " '2' type,b.goods_id goodsId,b.GOODS_NAME goodsName,b.Goods_code goodsCode,b.GOODS_UNIT_NAME goodsUnitName,b.goods_state goodsState,\n"
                + "a.CONSTRUCTION_id constructionId,(select code from construction cst where cst.CONSTRUCTION_ID=a.CONSTRUCTION_ID and cst.status !=0)constructionCode,\n"
                + "a.last_shipper_id sysUserId,a.DEPT_RECEIVE_ID sysGroupId,c.SERIAL,\n"
                + "(select prov.cat_province_id from construction cst,cat_station st,cat_province prov where \n"
                + "cst.CONSTRUCTION_ID=a.CONSTRUCTION_ID and cst.status !=0 and cst.cat_station_id=st.cat_station_id\n"
                + "and st.cat_province_id=prov.cat_province_id)provinceId,\n"
                + "(select prov.code from construction cst,cat_station st,cat_province prov where \n"
                + "cst.CONSTRUCTION_ID=a.CONSTRUCTION_ID and cst.status !=0 and cst.cat_station_id=st.cat_station_id\n"
                + "and st.cat_province_id=prov.cat_province_id)provinceCode,\n"
                + "\n"
                + "\n"
                + "sum(\n"
                + "nvl(c.QUANTITY,0) -\n"
                + "\n"
                + "nvl((select asset_entity.QUANTITY\n"
                + "from asset_management_request asset,ASSET_MANAGE_REQUEST_ENTITY asset_entity,mer_entity  where \n"
                + " asset.asset_management_request_id=asset_entity.asset_management_request_id and asset_entity.mer_entity_id=mer_entity.mer_entity_id\n"
                + "and asset.STATUS=3 and asset.CONSTRUCTION_ID=a.CONSTRUCTION_ID and mer_entity.GOODS_CODE=b.GOODS_CODE and \n"
                + " nvl(mer_entity.SERIAL,1) = nvl(c.SERIAL,1) and mer_entity.mer_entity_id=c.mer_entity_id),0) - \n"
                + " \n"
                + " nvl((select nvl(cst_mer.QUANTITY,0)\n"
                + "from construction_merchandise cst_mer where cst_mer.type=2 and cst_mer.CONSTRUCTION_ID=a.CONSTRUCTION_ID and\n"
                + "cst_mer.GOODS_CODE=b.GOODS_CODE and nvl(cst_mer.SERIAL,1)=nvl(c.SERIAL,1) and cst_mer.mer_entity_id=c.mer_entity_id),0)) amount,\n"
                + "\n"
                + "sum(\n"
                + "nvl(c.QUANTITY *c.PRICE,0) -\n"
                + "\n"
                + "nvl((select asset_entity.QUANTITY *c.PRICE\n"
                + "from asset_management_request asset,ASSET_MANAGE_REQUEST_ENTITY asset_entity,mer_entity  where \n"
                + " asset.asset_management_request_id=asset_entity.asset_management_request_id and asset_entity.mer_entity_id=mer_entity.mer_entity_id\n"
                + "and asset.STATUS=3 and asset.CONSTRUCTION_ID=a.CONSTRUCTION_ID and mer_entity.GOODS_CODE=b.GOODS_CODE and \n"
                + " nvl(mer_entity.SERIAL,1) = nvl(c.SERIAL,1) and mer_entity.mer_entity_id=c.mer_entity_id),0) - \n"
                + " \n"
                + " nvl((select nvl(cst_mer.QUANTITY *c.PRICE,0)\n"
                + "from construction_merchandise cst_mer where cst_mer.type=2 and cst_mer.CONSTRUCTION_ID=a.CONSTRUCTION_ID and\n"
                + "cst_mer.GOODS_CODE=b.GOODS_CODE and nvl(cst_mer.SERIAL,1)=nvl(c.SERIAL,1) and cst_mer.mer_entity_id=c.mer_entity_id),0)) price\n"
                + "\n"
                + "from stock_trans a,\n"
                + "stock_trans_detail b,\n"
                + "stock_trans_detail_serial c\n"
                + "where a.stock_trans_id=b.stock_trans_id and b.stock_trans_detail_id=c.stock_trans_detail_id and \n"
                + " a.confirm=1 and a.type = 2 \n"
                + " and REAL_IE_TRANS_DATE >=?\n"
                + "group by b.goods_id,b.GOODS_NAME,b.Goods_code,b.GOODS_UNIT_NAME,b.goods_state,\n"
                + "a.CONSTRUCTION_id,a.last_shipper_id,a.DEPT_RECEIVE_ID,c.SERIAL";
        Connection connection = null;
        try {
            connection = getConnection();
            preStmt = connection.prepareStatement(strSql);
            preStmt.setDate(1, new java.sql.Date(startTime.getTime()));
            preStmt.setDate(2, new java.sql.Date(startTime.getTime()));
            rs = preStmt.executeQuery();
            while (rs.next()) {
                StockTotalForm frm = new StockTotalForm();
                frm.setType(rs.getString("type"));
                frm.setGoodsId(rs.getString("goodsId"));
                frm.setGoodsName(rs.getString("goodsName"));
                frm.setGoodsCode(rs.getString("goodsCode"));
                frm.setGoodsUnitName(rs.getString("goodsUnitName"));
                frm.setGoodsState(rs.getString("goodsState"));
                frm.setConstructionId(rs.getString("constructionId"));
                frm.setConstructionCode(rs.getString("constructionCode"));
                frm.setSysUserId(rs.getString("sysUserId"));
                frm.setSysGroupId(rs.getString("sysGroupId"));
                frm.setSERIAL(rs.getString("SERIAL"));
                frm.setAmount(rs.getString("amount"));
                frm.setProvinceId(rs.getString("provinceId"));
                frm.setProvinceCode(rs.getString("provinceCode"));
                frm.setPrice(rs.getString("price"));
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

    public synchronized void insertStockTotal(ArrayList<StockTotalForm> lstDebt, Logger logger, long maxBatchSize) {
        logger.info("INSERT START!");
        String sql = " insert into syn_stock_total "
                + " (syn_stock_total_id,type,goods_id,GOODS_NAME,Goods_code, GOODS_UNIT_NAME,goods_state,CONSTRUCTION_id,"
                + " CONSTRUCTION_CODE,sys_user_id,sys_group_id,SERIAL,amount,province_id,province_code,price)"
                + " values (syn_stock_total_seq.nextval, ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?,?,?,?)";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            int count = 0;
            int size = lstDebt.size();
            int group = 0;
            for (StockTotalForm obj : lstDebt) {
                int i = 1;
                try {
                    group++;
                    count++;
                    pstmt.setString(i++, obj.getType());
                    pstmt.setString(i++, obj.getGoodsId());
                    pstmt.setString(i++, obj.getGoodsName());
                    pstmt.setString(i++, obj.getGoodsCode());
                    pstmt.setString(i++, obj.getGoodsUnitName());
                    pstmt.setString(i++, obj.getGoodsState());
                    pstmt.setString(i++, obj.getConstructionId());
                    pstmt.setString(i++, obj.getConstructionCode());
                    pstmt.setString(i++, obj.getSysUserId());
                    pstmt.setString(i++, obj.getSysGroupId());
                    pstmt.setString(i++, obj.getSERIAL());
                    pstmt.setString(i++, obj.getAmount());
                    pstmt.setString(i++, obj.getProvinceId());
                    pstmt.setString(i++, obj.getProvinceCode());
                    pstmt.setString(i++, obj.getPrice());
                    pstmt.addBatch();
                    if (group == maxBatchSize) {
                        logger.info("syn_stock_total INSERTED " + count + "/" + size);
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
                    logger.info("syn_stock_total INSERT INTO DB FAIL!");
                    logger.error(ex.getMessage(), ex);
                }
            }
//            pstmt.executeBatch();
//            logger.info("syn_stock_total INSERTED " + count + "/" + size);
//            con.setAutoCommit(false);
//            this.commit(con);
            logger.info("syn_stock_total INSERT INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("syn_stock_total INSERT INTO DB FAIL!");
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
                    logger.info("syn_stock_total INSERT INTO DB FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("INSERT FINISH!");
    }

    public void deleteStockTotal(Logger logger, long maxBatchSize) {
        logger.info("DELETE START!");
        String sql = " delete from syn_stock_total ";
        PreparedStatement pstmt = null;
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("syn_stock_total DELETE INTO DB SUCCESS!");
        } catch (Exception ex) {
            logger.info("syn_stock_total DELETE INTO DB FAIL!");
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
        logger.info("KTTS FINISH!");
    }
}
