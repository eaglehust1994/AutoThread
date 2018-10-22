/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.total.dl;

import com.viettel.kpi.common.utils.DateTimeUtils;
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
    private static Logger logger = Logger.getLogger(MyJob.class);

    public MyJob() {
    }

    public MyJob(String updateTime, boolean reload, MyDbTask db) {
        this.updateTime = updateTime;
        this.reload = reload;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start Report KPI Total Daily============");

            //count
            if (!reload) {
                updateTime = DateTimeUtils.format(DateTimeUtils.add(new Date(), -1), "dd/MM/yyyy");
            }
            Date _updateTime = DateTimeUtils.parse(updateTime, "dd/MM/yyyy");
            logger.info("updateTime: " + updateTime);

            //Tong hop kpi web theo ngày
            List<RpKpiToantrinhDl> lstToantrinhDl = db.getRpKpiToantrinhDl(updateTime);
            if (!lstToantrinhDl.isEmpty()) {
                db.deleteRpKpiToantrinhDl(updateTime);
                db.insertRpKpiToantrinhDl(updateTime, lstToantrinhDl);
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
