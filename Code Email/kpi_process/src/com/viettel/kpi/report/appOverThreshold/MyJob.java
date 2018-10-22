/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.report.appOverThreshold;

import com.viettel.kpi.common.utils.DateTimeUtils;
import java.util.ArrayList;
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
    private int startDate;
    private int endDate;
    private String updateTime;
    private boolean reload;
    private static Logger logger = Logger.getLogger(MyJob.class);

    public MyJob() {
    }

    public MyJob(int startDate, int endDate, String updateTime, boolean reload, MyDbTask db) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.updateTime = updateTime;
        this.reload = reload;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start Report QA============");
            if (!reload) {
                updateTime = DateTimeUtils.format(DateTimeUtils.add(new Date(), -1), "dd/MM/yyyy");
            }
            Date _updateTime = DateTimeUtils.parse(updateTime, "dd/MM/yyyy");
            logger.info("updateTime : " + updateTime);
//            MyDbTask db = new MyDbTask();
            int month = DateTimeUtils.getCurrentMonth();
            int year = DateTimeUtils.getCurrentYear();
            int currDay = DateTimeUtils.getCurrentDayOfMonth();
            if (currDay >= startDate) {
                month = month + 1;
            }
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, startDate);
            Date fromDate = cal.getTime();

            cal.set(year, month, endDate);
            Date toDate = cal.getTime();

            List<RpAppOverThreshold> lst = new ArrayList<RpAppOverThreshold>();
            cal.setTime(_updateTime);
            if (cal.get(Calendar.DAY_OF_MONTH) == startDate) {
                lst = db.getListFirst(_updateTime);

            } else {
                lst = db.getList(_updateTime);
            }
            if (!lst.isEmpty()) {
                for (RpAppOverThreshold obj : lst) {
                    obj.setFromDate(fromDate);
                    obj.setToDate(toDate);
                    obj.setMonth(month);
                    obj.setYear(year);
                    obj.setUpdateTime(_updateTime);
                    obj.setInsertTime(new Date());
                }
                if (reload) {
                    logger.info("Chay bu du lieu");
//                    db.deleteAppOverThreshold(_updateTime, obj);
                }
                db.insertAppOverThreshold(lst);
            } else {
                logger.info("List Kpi Log Server empty");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                db.close();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
