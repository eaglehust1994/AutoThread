/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.hl;

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
    private int hourRun;
    private boolean reload;
    private static Logger logger = Logger.getLogger(MyJob.class);

    public MyJob() {
    }

    public MyJob(String updateTime, int hourRun, boolean reload, MyDbTask db) {

        this.updateTime = updateTime;
        this.hourRun = hourRun;
        this.reload = reload;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start KPI Hourly============");

            //count
            if (!reload) {
                updateTime = DateTimeUtils.format(new Date(), "dd/MM/yyyy");
                hourRun = DateTimeUtils.getCurrentHour();
            }
            Date _updateTime = DateTimeUtils.parse(updateTime, "dd/MM/yyyy");
            logger.info("updateTime: " + updateTime);
            logger.info("hourRun: " + hourRun);

//            Calendar cal = Calendar.getInstance();

//            cal.setTime(_updateTime);
//            cal.set(Calendar.HOUR_OF_DAY, hourRun - 1);
//            cal.set(Calendar.MINUTE, 0);
//            cal.set(Calendar.SECOND, 0);
//            cal.set(Calendar.MILLISECOND, 0);
//            Date startTime = cal.getTime();
//            logger.info("startTime: " + startTime);
//
//            cal.setTime(_updateTime);
//            cal.set(Calendar.HOUR_OF_DAY, hourRun);
//            cal.set(Calendar.MINUTE, 0);
//            cal.set(Calendar.SECOND, 0);
//            cal.set(Calendar.MILLISECOND, 0);
//            Date endTime = cal.getTime();
//            logger.info("endTime: " + endTime);

            if (reload) {
//                logger.info("__________Bat dau chay bu ca ngay__________");
                logger.info("__________Bat dau chay bu theo gio__________");
//                for (int hourRun = 1; hourRun <= 24; hourRun++) {
                    logger.info("__________hourRun: " + hourRun);
                    logger.info("__________updateTime: " + _updateTime);

//                    cal.setTime(_updateTime);
//                    cal.set(Calendar.HOUR_OF_DAY, hourRun - 1);
//                    cal.set(Calendar.MINUTE, 0);
//                    cal.set(Calendar.SECOND, 0);
//                    cal.set(Calendar.MILLISECOND, 0);
//                    startTime = cal.getTime();
//                    logger.info("__________startTime: " + startTime);

//                    cal.setTime(_updateTime);
//                    cal.set(Calendar.HOUR_OF_DAY, hourRun);
//                    cal.set(Calendar.MINUTE, 0);
//                    cal.set(Calendar.SECOND, 0);
//                    cal.set(Calendar.MILLISECOND, 0);
//                    endTime = cal.getTime();
//                    logger.info("__________endTime: " + endTime);

                    List<KpiDataRaw> lst = db.getKpiDataRaw(_updateTime, hourRun - 1);
                    if (!lst.isEmpty()) {
                        logger.info("Delete kpi_data_raw before insert");
                        db.deleteKpiDataRaw(_updateTime, hourRun - 1);
                        db.insertKpiDataRaw(lst);
                    }
                    //Tong hop kpi web theo gio
                    List<KpiDataRawHl> lstHl = db.getKpiWebRawHl(_updateTime, hourRun - 1);
                    if (!lstHl.isEmpty()) {
                        logger.info("Delete kpi_data_raw_hl before insert");
                        db.deleteKpiWebRawHl(_updateTime, hourRun - 1);
                        db.insertKpiWebRawHl(_updateTime, lstHl);
                    }
//                }
            } else {
                //Tong hop 2 ban ghi thanh 1
                List<KpiDataRaw> lst = db.getKpiDataRaw(_updateTime, hourRun - 1);
                if (!lst.isEmpty()) {
                    if(db.existData(_updateTime, hourRun - 1)){
                        db.deleteKpiDataRaw(_updateTime, hourRun - 1);
                    }
                    db.insertKpiDataRaw(lst);
                }
                //Tong hop kpi web theo gio
                List<KpiDataRawHl> lstHl = db.getKpiWebRawHl(_updateTime, hourRun - 1);
                if (!lstHl.isEmpty()) {
                    db.deleteKpiWebRawHl(_updateTime, hourRun - 1);
                    db.insertKpiWebRawHl(_updateTime, lstHl);
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
