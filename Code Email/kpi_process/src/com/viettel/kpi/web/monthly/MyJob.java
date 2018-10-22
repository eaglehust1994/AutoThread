/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.monthly;

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
            logger.info("===========Start KPI Frequently============");

            //count
            if (!reload) {
                updateTime = DateTimeUtils.format(DateTimeUtils.add(new Date(), -1), "dd/MM/yyyy");
            }
            Date _updateTime = DateTimeUtils.parse(updateTime, "dd/MM/yyyy");
            logger.info("updateTime: " + updateTime);
            db = new MyDbTask();
            List<KpiWebCountDl> lst = db.getKpiWebCount(_updateTime);
            if (!lst.isEmpty()) {
                if (reload) {
                    logger.info("Chay bu du lieu");
                    db.deleteKpiCountDl(_updateTime);
                }
                db.insertKpiCountDl(lst);
            }

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

                List<CataUrl> lstCataUrl = db.getListCataUrl();
                if (reload) {
                    logger.info("Chay bu du lieu");
                    logger.info("month: " + month);
                    logger.info("year: " + year);
                    db.deleteKpiWebMonthly(month, year);
                }
                for (CataUrl cataUrl : lstCataUrl) {
                    logger.info("=======================");
                    logger.info("AppId: " + cataUrl.getAppId());
                    logger.info("UrlId: " + cataUrl.getUrlId());
                    List<KpiWebCountDl> lstKpiWebCount = db.getKpiWebCount(_startDate, _endDate, cataUrl);
                    if (!lstKpiWebCount.isEmpty()) {
                        List<Long> list = db.getTotal(_startDate, _endDate, cataUrl);

                        Long total = list.get(0);
                        logger.info("total: " + total);
                        Long totalSucc = list.get(1);
                        logger.info("totalSucc: " + totalSucc);

                        Double rank95 = total * 0.95;
                        KpiWebMonthly kpiMonthly = db.getKpiWebMonthly(lstKpiWebCount, rank95,
                                total, totalSucc, month, year);
                        db.insertKpiWebMonthly(kpiMonthly);
                    } else {
                        logger.info("List Kpi Count Dl empty");
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
