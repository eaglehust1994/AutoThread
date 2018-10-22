/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.mobile.dl;

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
            logger.info("===========Start KPI Mobile============");

            //count
            if (!reload) {
                updateTime = DateTimeUtils.format(DateTimeUtils.add(new Date(), -1), "dd/MM/yyyy");
            }
            Date _updateTime = DateTimeUtils.parse(updateTime, "dd/MM/yyyy");
            logger.info("updateTime: " + updateTime);
//            db = new MyDbTask();
            List<KpiMobileCountDl> lst = db.getKpiMobileCount(_updateTime);
            if (!lst.isEmpty()) {
                if (reload) {
                    logger.info("Chay bu du lieu");
                    db.deleteKpiMobileCountDl(_updateTime);
                }
                db.insertKpiMobileCountDl(lst);
            }
            List<CataFuncMobile> lstCataFuncMobile = db.getListCataFuncMobile();
            if (reload) {
                logger.info("Chay bu du lieu");
                db.deleteKpiMobileDl(_updateTime);
            }
            for (CataFuncMobile cataFuncMobile : lstCataFuncMobile) {
                logger.info("=======================");
                logger.info("AppCode: " + cataFuncMobile.getAppCode());
                logger.info("Url: " + cataFuncMobile.getUrl());
                List<KpiMobileCountDl> lstCountDaily = db.getKpiMobileCountByDl(_updateTime, cataFuncMobile);
                if (!lstCountDaily.isEmpty()) {
                    Long total = db.getTotalDl(_updateTime, cataFuncMobile);
                    Double rank95 = total * 0.95;
//                    Double target = cataFuncMobile.getTarget();
                    KpiMobileDaily kpiDaily = db.getKpiMobileDl(lstCountDaily, rank95, total, cataFuncMobile, _updateTime);
                    db.insertKpiMobileDl(kpiDaily);
                } else {
                    logger.info("List Kpi Count Dl empty");
                }
            }
            //daily

            //monthly
            Calendar cal = Calendar.getInstance();
            if (reload) {
                cal.setTime(_updateTime);
            } else {
                cal.setTime(new Date());
            }
            cal.setTime(new Date());
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
                for (CataFuncMobile cataFuncMobile : lstCataFuncMobile) {
                    logger.info("=======================");
                    logger.info("AppCode: " + cataFuncMobile.getAppCode());
                    logger.info("FuncName: " + cataFuncMobile.getFuncName());
                    List<KpiMobileCountDl> lstCountMonthly = db.getKpiMobileCountByMonthly(_startDate, _endDate, cataFuncMobile);
                    if (!lstCountMonthly.isEmpty()) {
                        Long total = db.getTotalMonthly(_startDate, _endDate, cataFuncMobile);
                        Double rank95 = total * 0.95;
                        KpiMobileMonthly kpiMonthly = db.getKpiMobileMonthly(lstCountMonthly, rank95, total, month,
                                year, cataFuncMobile);
                        db.insertKpiMobileMonthly(kpiMonthly);
                    } else {
                        logger.info("List Kpi Count Monthly empty");
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
