/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.frequently;

import com.viettel.kpi.common.utils.DataConfig;
import com.viettel.kpi.common.utils.DateTimeUtils;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyJob implements Runnable {

    private String updateTime;
    private MyDbTask db;
    private boolean reload;
    private static Logger logger = Logger.getLogger(MyJob.class);

    public MyJob() {
    }

    public MyJob(String updateTime, boolean reload,MyDbTask db) {
        this.updateTime = updateTime;
        this.reload = reload;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start KPI Frequently============");

            if (!reload) {
                updateTime = DateTimeUtils.format(DateTimeUtils.add(new Date(), -1), "dd/MM/yyyy");
            }
            Date _updateTime = DateTimeUtils.parse(updateTime, "dd/MM/yyyy");
            logger.info("updateTime: " + updateTime);
//            db = new MyDbTask();
            List<KpiDataWebFrequently> lst = db.getNumClient(_updateTime);
            if (!lst.isEmpty()) {
                if (reload) {
                    logger.info("Chay bu du lieu");
                    db.deleteKpiFrequently(_updateTime);
                }
                db.insertKpiFrequently(lst);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }finally {
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
