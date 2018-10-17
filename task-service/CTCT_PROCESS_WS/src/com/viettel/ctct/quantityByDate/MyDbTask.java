/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct.quantityByDate;

import com.viettel.framework.service.common.DbTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//  @author hoanm1
public class MyDbTask extends DbTask {

//    hungnx 20180714 end
    public synchronized List<ConstructionTaskDailyDTO> doSearchInPast(Logger logger) {
        logger.info("search task daily in last month!");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        cal.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        String date = sdf.format(cal.getTime());
        StringBuilder stringBuilder = new StringBuilder("SELECT CTD.CONSTRUCTION_TASK_DAILY_ID constructionTaskDailyId, CTD.AMOUNT amount, CTD.CAT_TASK_ID catTaskId"
                + ", CTD.CONSTRUCTION_TASK_ID constructionTaskId, CTD.WORK_ITEM_ID workItemId "
                + ", CT.STATUS statusConstructionTask, C.AMOUNT amountConstruction, ct.path path"
                + " FROM CONSTRUCTION_TASK_DAILY ctd ,CONSTRUCTION_TASK ct, CONSTRUCTION c  "
                + " where TO_CHAR(ctd.created_date, 'MM/yyyy') = '" + date + "'"
                + " and CTD.CONSTRUCTION_TASK_ID = CT.CONSTRUCTION_TASK_ID and C.CONSTRUCTION_ID = CT.CONSTRUCTION_ID and CTD.CONFIRM =0");
        PreparedStatement pstmt = null;
        Connection con = null;
        ResultSet rs = null;
        List<ConstructionTaskDailyDTO> lst = new ArrayList<ConstructionTaskDailyDTO>();
        try {
            con = getConnection();
            pstmt = con.prepareStatement(stringBuilder.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ConstructionTaskDailyDTO obj = new ConstructionTaskDailyDTO();
                obj.setWorkItemId(rs.getLong("workItemId"));
                obj.setConstructionTaskDailyId(rs.getLong("constructionTaskDailyId"));
                obj.setCatTaskId(rs.getLong("catTaskId"));
                obj.setAmount(rs.getDouble("amount"));
                obj.setConstructionTaskId(rs.getLong("constructionTaskId"));
                obj.setAmountConstruction(rs.getDouble("amountConstruction"));
                obj.setStatusConstructionTask(rs.getString("statusConstructionTask"));
                obj.setPath(rs.getString("path"));
                lst.add(obj);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
            if (con != null) {
                try {
                    close(con);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return lst;
    }

    public synchronized void updateConfirm(List<ConstructionTaskDailyDTO> lst, Logger logger) {
        PreparedStatement pstmt = null;
        StringBuilder stringBuilder = new StringBuilder("UPDATE CONSTRUCTION_TASK_DAILY ctd set CTD.CONFIRM =? ");
        stringBuilder.append(" where ctd.CONSTRUCTION_TASK_DAILY_ID in (");
        for (int i = 0; i < lst.size(); i++) {
            stringBuilder.append(lst.get(i).getConstructionTaskDailyId());
            if (i < lst.size() - 1) {
                stringBuilder.append(",");
            }
        }     
        stringBuilder.append(")");
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(stringBuilder.toString());
            pstmt.setString(1, "2");
//            pstmt.setString(2, strId);
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("UPDATE confirm SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE confirm FAIL!");
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
                    logger.info("UPDATE confirm FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE FINISH!");
    }

    public synchronized void updateConstructionTask(ConstructionTaskDailyDTO criteria, Logger logger) {
        PreparedStatement pstmt = null;
        StringBuilder stringBuilder = new StringBuilder("UPDATE CONSTRUCTION_TASK CT set ");
        double percent = 0;
        stringBuilder.append(" CT.STATUS =?");
        if (criteria.getAmountConstruction() != null) {
            stringBuilder.append(", CT.COMPLETE_PERCENT =?");
            double amountDaily = getTotalAmountDaily(criteria, logger);
            if (null != criteria.getAmountConstruction()) {
                percent = 100*(amountDaily / criteria.getAmountConstruction()) ;
            }
        }
        stringBuilder.append(" where CT.CONSTRUCTION_TASK_ID =?");
        Connection con = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(stringBuilder.toString());
            if (percent < 100) {
                 pstmt.setString(1, "2");
            } else {
                 pstmt.setString(1, "4");
            }
            pstmt.setLong(3, criteria.getConstructionTaskId());
            if (criteria.getAmountConstruction() != null) {
                pstmt.setDouble(2, percent);
            }
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);
            logger.info("UPDATE COMPLETE_PERCENT SUCCESS!");
        } catch (Exception ex) {
            logger.info("UPDATE COMPLETE_PERCENT FAIL!");
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
                    logger.info("UPDATE constr task FINISH!");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        logger.info("UPDATE COMPLETE_PERCENT FINISH!");
        //            update complete percent workitem, construction
        String[] lstPath = criteria.getPath().split("/");
        String sysGroupId = lstPath[1];
        String constructionId = lstPath[2];
        String workItemId = lstPath[3];
        updateWorkItemConstructionTask(workItemId, constructionId, logger);
        updateSysGroupTask(sysGroupId, logger);
    }

    public synchronized void updateWorkItemConstructionTask(String workItemId,
            String constructionId, Logger logger) {
        StringBuilder sql = new StringBuilder("UPDATE CONSTRUCTION_TASK c1 SET "
                + " c1.COMPLETE_PERCENT = ROUND((SELECT AVG(nvl(c2.COMPLETE_PERCENT,0)) FROM CONSTRUCTION_TASK c2 WHERE c2.PARENT_ID = ?),2),"
                + " c1.START_DATE =( SELECT min(c3.START_DATE) From CONSTRUCTION_TASK c3 WHERE c3.PARENT_ID = ?),"
                + " c1.END_DATE =( SELECT max(c4.END_DATE) From CONSTRUCTION_TASK c4 WHERE c4.PARENT_ID = ?)"
                + " Where c1.CONSTRUCTION_TASK_ID = ? ");
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmtCons = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, Long.parseLong(workItemId));
            pstmt.setLong(2, Long.parseLong(workItemId));
            pstmt.setLong(3, Long.parseLong(workItemId));
            pstmt.setLong(4, Long.parseLong(workItemId));
            pstmt.execute();
//            update construction compete percent
            StringBuilder sqlConstructionTask = new StringBuilder(
                    " UPDATE CONSTRUCTION_TASK c1 SET "
                    + " c1.COMPLETE_PERCENT = ROUND((SELECT AVG(nvl(c2.COMPLETE_PERCENT,0)) FROM CONSTRUCTION_TASK c2 WHERE c2.PARENT_ID = ?),2),"
                    + " c1.START_DATE =( SELECT min(c3.START_DATE) From CONSTRUCTION_TASK c3 WHERE c3.PARENT_ID = ?),"
                    + " c1.END_DATE =( SELECT max(c4.END_DATE) From CONSTRUCTION_TASK c4 WHERE c4.PARENT_ID = ?)"
                    + " Where c1.CONSTRUCTION_TASK_ID = ? ");
            pstmtCons = con.prepareStatement(sqlConstructionTask.toString());
            pstmtCons.setLong(1, Long.parseLong(constructionId));
            pstmtCons.setLong(2, Long.parseLong(constructionId));
            pstmtCons.setLong(3, Long.parseLong(constructionId));
            pstmtCons.setLong(4, Long.parseLong(constructionId));
//          pstmtCons.setLong(2, Long.parseLong(workItemId));
            pstmtCons.execute();
            con.setAutoCommit(false);
            this.commit(con);
        } catch (Exception ex) {
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
    }

    public synchronized void updateSysGroupTask(String sysId, Logger logger) {
        StringBuilder sqlUnitTask = new StringBuilder(
                " UPDATE CONSTRUCTION_TASK c1 SET "
                + " c1.COMPLETE_PERCENT = ROUND((SELECT AVG(nvl(c2.COMPLETE_PERCENT,0)) FROM CONSTRUCTION_TASK c2 WHERE c2.PARENT_ID = ?),2),"
                + " c1.START_DATE =( SELECT min(c3.START_DATE) From CONSTRUCTION_TASK c3 WHERE c3.PARENT_ID = ?),"
                + " c1.END_DATE =( SELECT max(c4.END_DATE) From CONSTRUCTION_TASK c4 WHERE c4.PARENT_ID = ?)"
                + " Where c1.CONSTRUCTION_TASK_ID = ? ");

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sqlUnitTask.toString());
            pstmt.setLong(1, Long.parseLong(sysId));
            pstmt.setLong(2, Long.parseLong(sysId));
            pstmt.setLong(3, Long.parseLong(sysId));
            pstmt.setLong(4, Long.parseLong(sysId));
            pstmt.execute();
            con.setAutoCommit(false);
            this.commit(con);

        } catch (Exception ex) {
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
    }

    public synchronized Double getTotalAmountDaily(ConstructionTaskDailyDTO criteria, Logger logger) {
        StringBuilder sql = new StringBuilder(" SELECT nvl(SUM(a.AMOUNT), 0) amount FROM CONSTRUCTION_TASK_DAILY a where a.confirm in(0,1)");
        if (null != criteria.getCatTaskId()) {
            sql.append(" and a.CAT_TASK_ID = :catTaskId");
        }
        if (null != criteria.getWorkItemId()) {
            sql.append(" and a.WORK_ITEM_ID = :workItemId");
        }
        double total = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, criteria.getCatTaskId());
            pstmt.setLong(2, criteria.getWorkItemId());

            rs = pstmt.executeQuery();
            while (rs.next()) {
                total = rs.getDouble("amount");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
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
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        return total;
    }

//    hungnx 20180714 end
}
