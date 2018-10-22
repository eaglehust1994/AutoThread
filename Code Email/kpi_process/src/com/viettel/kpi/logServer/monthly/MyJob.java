/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.logServer.monthly;

import com.viettel.kpi.common.utils.DateTimeUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyJob implements Runnable {

    private MyDbTask db;
    private String updateTime;
    private boolean reload;
    private int startDate;
    private int endDate;
    private static Logger logger = Logger.getLogger(MyJob.class);

    public MyJob() {
    }

    public MyJob(String updateTime, boolean reload, int startDate, int endDate, MyDbTask db) {
        this.updateTime = updateTime;
        this.reload = reload;
        this.startDate = startDate;
        this.endDate = endDate;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start KPI Log Server Daily============");
            if (!reload) {
                updateTime = DateTimeUtils.format(DateTimeUtils.add(new Date(), -1), "dd/MM/yyyy");
            }
            Date _updateTime = DateTimeUtils.parse(updateTime, "dd/MM/yyyy");
            logger.info("updateTime : " + updateTime);
//             db = new MyDbTask();
            List<CataFuncLogServer> lst = db.getListFuncName();
//            for (CataFuncLogServer obj : lst) {
//                logger.info("=======================");
//                logger.info("appCode: " + obj.getAppCode());
//                logger.info("funcName: " + obj.getFuncName());
//                List<KpiLogServerRaw> lstKpiLogServerRaw = db.getKpiLogServerRaw(_updateTime, obj);
//                if (!lstKpiLogServerRaw.isEmpty()) {
//                    Long total = db.getTotal(_updateTime, obj);
//                    Double rank95 = total * 0.95;
//                    KpiLogServerDl kpiDl = db.getKpiLogServerDl(lstKpiLogServerRaw, rank95);
////                    if (reload) {
////                        logger.info("Chay bu du lieu");
//                    logger.info("Xoa truoc khi insert");
//                    db.deleteKpiLogServerDl(_updateTime, obj);
////                    }
//                    db.insertKpiLogServerDl(kpiDl);
//                } else {
//                    logger.info("List Kpi Log Server empty");
//                }
//            }

            //monthly
            Calendar cal = Calendar.getInstance();
            if (reload) {
                cal.setTime(_updateTime);
            } else {
                cal.setTime(new Date());
            }
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            logger.info("month: " + month);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            logger.info("day of month: " + dayOfMonth);

            if (dayOfMonth == endDate) {
                logger.info("Begin tong hop thang");
                cal.set(year, month - 2, startDate);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date _startDate = cal.getTime();
                logger.info("startDate: " + _startDate);

                cal.set(year, month - 1, endDate);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date _endDate = cal.getTime();
                logger.info("endDate: " + _endDate);

                if (reload) {
                    logger.info("Chay bu du lieu");
                    db.deleteKpiMobileMonthly(month);
                }
                for (CataFuncLogServer cataFuncLogServer : lst) {
                    logger.info("=======================");
                    logger.info("AppCode: " + cataFuncLogServer.getAppCode());
                    logger.info("FuncName: " + cataFuncLogServer.getFuncName());
                    List<KpiLogServerRaw> lstCountMonthly = db.getKpiLogServerCountByMonthly(_startDate, _endDate, cataFuncLogServer);
                    if (!lstCountMonthly.isEmpty()) {
                        Long total = db.getTotalMonthly(_startDate, _endDate, cataFuncLogServer);
                        Double rank95 = total * 0.95;
                        KpiLogServerMonthly kpiMonthly = db.getKpiLogServerMonthly(lstCountMonthly, rank95, total, month, year);
                        db.insertKpiLogServerMonthly(kpiMonthly);
                    } else {
                        logger.info("List Kpi Duration Monthly empty");
                    }

                    KpiLogServerMonthly otherMonthly = db.getKpiLogServerOtherByMonthly(_startDate, _endDate, cataFuncLogServer, month, year);
                    if (otherMonthly != null) {
                        db.insertKpiLogServerMonthly(otherMonthly);
                    } else {
                        logger.info("Kpi Other Monthly empty");
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
