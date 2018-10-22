/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.mobile.raw;

import com.viettel.kpi.common.utils.DateTimeUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyJob implements Runnable {

    private MyDbTask db;
    private String startDate;
    private String endDate;
    private boolean reload;
    private static Logger logger = Logger.getLogger(MyJob.class);

    public MyJob() {
    }

    public MyJob(String startDate, String endDate, boolean reload, MyDbTask db) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.reload = reload;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start KPI Mobile Raw============");

            if (!reload) {
                logger.info("Chay binh thuong");
//                updateTime = DateTimeUtils.format(DateTimeUtils.add(new Date(), -1), "dd/MM/yyyy");
//                Date now = DateTimeUtils.parse(updateTime, "dd/MM/yyyy");
                Date now = DateTimeUtils.trunc(new Date());
                logger.info("Ngay hien tai: " + DateTimeUtils.format(now, "dd/MM/yyyy"));

                logger.info("Tong hop 2 ban ghi thanh 1");
                logger.info("Tim kiem trong 2 ngay n, n-1 cac ban ghi co start va end");
                List<KpiMobileDataRaw> lst = new ArrayList<KpiMobileDataRaw>();
                lst.addAll(db.getKpiMobileRaw(now));
                lst.addAll(db.getKpiMobileRaw(DateTimeUtils.add(now, -1)));
                if (!lst.isEmpty()) {
                    logger.info("Insert kpi_mobile_raw");
                    db.insertKpiMobileRaw(lst);
                    logger.info("Delete kpi_mobile_raw_temp voi nhung ban ghi da gop");
                    db.deleteKpiMobileRawTemp(lst, now);
                    db.deleteKpiMobileRawTemp(lst, DateTimeUtils.add(now, -1));
                }
                logger.info("Tim kiem trong ngay n-2 cac ban ghi timeout");
                List<KpiMobileDataRaw> lstTimeout = db.getKpiMobileRawTimeout(DateTimeUtils.add(now, -2));
                if (!lstTimeout.isEmpty()) {
                    logger.info("Insert kpi_mobile_raw timeout");
                    db.insertKpiMobileRaw(lstTimeout);
                    logger.info("Delete kpi_mobile_raw_temp timeout");
                    db.deleteKpiMobileRawTemp(lstTimeout, DateTimeUtils.add(now, -2));
                }
            } else {
                logger.info("Chay bu");
                Date _startDate = DateTimeUtils.parse(startDate, "dd/MM/yyyy");
                logger.info("Ngay bat dau: " + startDate);
                Date _endDate = DateTimeUtils.parse(endDate, "dd/MM/yyyy");
                logger.info("Ngay ket thuc: " + endDate);

                logger.info("Tong hop 2 ban ghi thanh 1");
                logger.info("Tong hop lai tu ngay " + startDate + " den ngay " + endDate);
                while (_startDate.getTime() < _endDate.getTime()) {
                    logger.info("updateTime: " + DateTimeUtils.format(_startDate, "dd/MM/yyyy"));
                    List<KpiMobileDataRaw> lst = new ArrayList<KpiMobileDataRaw>();
                    logger.info("Tim kiem trong ngay " + DateTimeUtils.format(_startDate, "dd/MM/yyyy")
                            + " cac ban ghi co start va end");
                    lst.addAll(db.getKpiMobileRaw(_startDate));
                    if (!lst.isEmpty()) {
                        //khong can check vi insert da loai bo
//                        logger.info("Delete kpi_mobile_raw cac ban ghi truoc khi chay bu");
//                        db.deleteKpiMobileRaw(lst, _updateTime);
                        logger.info("Insert kpi_mobile_raw");
                        db.insertKpiMobileRaw(lst);
                        logger.info("Delete kpi_mobile_raw_temp voi nhung ban ghi da gop");
                        db.deleteKpiMobileRawTemp(lst, _startDate);
                        db.deleteKpiMobileRawTemp(lst, DateTimeUtils.add(_startDate, -1));
                    }
                    logger.info("Tim kiem trong ngay"
                            + DateTimeUtils.format(DateTimeUtils.add(_startDate, -2), "dd/MM/yyyy")
                            + "cac ban ghi timeout");
                    List<KpiMobileDataRaw> lstTimeout = db.getKpiMobileRawTimeout(DateTimeUtils.add(_startDate, -2));
                    if (!lstTimeout.isEmpty()) {
                        logger.info("Insert kpi_mobile_raw timeout");
                        db.insertKpiMobileRaw(lstTimeout);
                        logger.info("Delete kpi_mobile_raw_temp timeout");
                        db.deleteKpiMobileRawTemp(lstTimeout, DateTimeUtils.add(_startDate, -2));
                    }
                    _startDate = DateTimeUtils.add(_startDate, +1);
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
