/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.alarmNoc;

import com.viettel.kpi.common.utils.Constants.ALARM_RULE;
import com.viettel.kpi.common.utils.Constants.ALARM_TYPE;
import com.viettel.kpi.common.utils.DateTimeUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyJob implements Runnable {

    private MyDbTask db;
    private boolean reload;
    private static Logger logger = Logger.getLogger(MyJob.class);

    public MyJob() {
    }

    public MyJob(boolean reload, MyDbTask db) {
        this.reload = reload;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start Alarm NOC============");

            if (!reload) {
                logger.info("Chay binh thuong");
                Date now = DateTimeUtils.trunc(new Date());
                logger.info("Ngay hien tai: " + DateTimeUtils.format(now, "dd/MM/yyyy"));

                logger.info("Get cau hinh canh bao");
                List<CataAlarmConfig> lstAlarmConfig = db.getAlarmConfig();
                if (!lstAlarmConfig.isEmpty()) {
                    for (CataAlarmConfig alarmConfig : lstAlarmConfig) {

                        //<editor-fold defaultstate="collapsed" desc="DAYS_PER_WEEK">
                        if (ALARM_TYPE.DAY.equals(alarmConfig.getAlarmType())
                                && ALARM_RULE.DAYS_PER_WEEK.equals(alarmConfig.getAlarmRule())) {
                            logger.info("----------Canh bao ngay: ngay/tuan----------");
                            logger.info("Vuot nguong: " + alarmConfig.getAlarmExceed());
                            logger.info("So ngay/tuan: " + alarmConfig.getAlarmRuleValue());

                            List<KpiAlarmNoc> lst = new ArrayList<KpiAlarmNoc>();

                            logger.info("----------Canh bao ngay: AddonTDXL----------");
                            lst.addAll(db.getDaysPerWeekAddonTDXL(alarmConfig.getAlarmExceed(),
                                    alarmConfig.getAlarmRuleValue()));
//                            List<KpiAlarmNoc> lstAddonTDXL = db.getDaysPerWeekAddonTDXL(alarmConfig.getAlarmExceed(),
//                                    alarmConfig.getAlarmRuleValue());
//                            logger.info("lstAddonTDXL.size(): " + lstAddonTDXL.size());
//                            if (!lstAddonTDXL.isEmpty()) {
//                                logger.info("Insert kpi_alarm_noc");
//                                db.insertKpiAlarmNoc(lstAddonTDXL);
//                            }
                            logger.info("----------Canh bao ngay: AddonTLGD----------");
                            lst.addAll(db.getDaysPerWeekAddonTLGD(alarmConfig.getAlarmExceed(),
                                    alarmConfig.getAlarmRuleValue()));
                            
                            logger.info("----------Canh bao ngay: MobileTLGD----------");
                            logger.info("----------Canh bao ngay: LogserverTDXL----------");
                            logger.info("----------Canh bao ngay: LogserverTLGD----------");
                            
                            logger.info("lst.size(): " + lst.size());
                            if (!lst.isEmpty()) {
                                logger.info("Insert kpi_alarm_noc");
                                db.insertKpiAlarmNoc(lst);
                            }
                        }
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="CONSECUTIVE_DAYS">
                        if (ALARM_TYPE.DAY.equals(alarmConfig.getAlarmType())
                                && ALARM_RULE.CONSECUTIVE_DAYS.equals(alarmConfig.getAlarmRule())) {
                            logger.info("----------Canh bao ngay: ngay lien tiep----------");
                            logger.info("Vuot nguong: " + alarmConfig.getAlarmExceed());
                            logger.info("So ngay lien tiep: " + alarmConfig.getAlarmRuleValue());

                            List<KpiAlarmNoc> lst = new ArrayList<KpiAlarmNoc>();
                            
                            logger.info("----------Canh bao ngay: AddonTDXL----------");
                            lst.addAll(db.getConsecutiveDaysAddonTDXL(alarmConfig.getAlarmExceed(),
                                    alarmConfig.getAlarmRuleValue()));
//                            logger.info("lstTDXL.size(): " + lstTDXL.size());
//                            if (!lstTDXL.isEmpty()) {
//                                logger.info("Insert kpi_alarm_noc");
//                                db.insertKpiAlarmNoc(lstTDXL);
//                            }
                            logger.info("----------Canh bao ngay: AddonTLGD----------");
                            lst.addAll(db.getConsecutiveDaysAddonTLGD(alarmConfig.getAlarmExceed(),
                                    alarmConfig.getAlarmRuleValue()));
                            
                            logger.info("----------Canh bao ngay: MobileTLGD----------");
                            logger.info("----------Canh bao ngay: LogserverTDXL----------");
                            logger.info("----------Canh bao ngay: LogserverTLGD----------");
//                            hoanm1_comment_start
//                            logger.info("lstTLGD.size(): " + lstTLGD.size());
//                            if (!lstTLGD.isEmpty()) {
//                                logger.info("Insert kpi_alarm_noc");
//                                db.insertKpiAlarmNoc(lstTLGD);
//                            }
//                            hoanm1_comment_end
                        }
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="CONSECUTIVE_WEEKS">
                        if (ALARM_TYPE.WEEK.equals(alarmConfig.getAlarmType())
                                && ALARM_RULE.CONSECUTIVE_WEEKS.equals(alarmConfig.getAlarmRule())) {
                            logger.info("----------Canh bao tuan: tuan lien tiep----------");
                            logger.info("Vuot nguong: " + alarmConfig.getAlarmExceed());
                            Long n = alarmConfig.getAlarmRuleValue();
                            logger.info("So tuan lien tiep: " + n);
                            Map<String, KpiAlarmNoc> map = new HashMap<String, KpiAlarmNoc>();

                            for (int i = 0; i < n; i++) {
                                List<KpiAlarmNoc> lst = new ArrayList<KpiAlarmNoc>();
                                logger.info("----------Canh bao tuan: AddonTDXL----------");
                                lst.addAll(db.getConsecutiveWeeksAddonTDXL(alarmConfig.getAlarmExceed(), n, i));
                                logger.info("----------Canh bao tuan: AddonTLGD----------");
                                lst.addAll(db.getConsecutiveWeeksAddonTLGD(alarmConfig.getAlarmExceed(), n, i));
                                logger.info("----------Canh bao tuan: MobileTDXL----------");
                                lst.addAll(db.getConsecutiveWeeksMobileTDXL(alarmConfig.getAlarmExceed(), n, i));
                                logger.info("----------Canh bao tuan: LogserverTDXL----------");
                                lst.addAll(db.getConsecutiveWeeksLogserverTDXL(alarmConfig.getAlarmExceed(), n, i));
                                logger.info("----------Canh bao tuan: LogserverTLGD----------");
                                lst.addAll(db.getConsecutiveWeeksLogserverTLGD(alarmConfig.getAlarmExceed(), n, i));

                                for (KpiAlarmNoc obj : lst) {
                                    String key = obj.getId();
                                    if (map.containsKey(key)) {
                                        KpiAlarmNoc objValue = map.get(key);
                                        objValue.setContent(obj.getContent() + "," + objValue.getContent());
                                        objValue.setCount(objValue.getCount() + 1);
                                        map.put(objValue.getId(), objValue);
                                    } else {
                                        map.put(key, obj);
                                    }
                                }
                            }

                            List<KpiAlarmNoc> lstResult = new ArrayList<KpiAlarmNoc>();
                            for (Map.Entry<String, KpiAlarmNoc> entrySet : map.entrySet()) {
                                KpiAlarmNoc obj = entrySet.getValue();
                                if (Objects.equals(obj.getCount(), alarmConfig.getAlarmRuleValue())) {
                                    lstResult.add(obj);
                                }
                            }
                            logger.info("lstResult.size(): " + lstResult.size());
                            if (!lstResult.isEmpty()) {
                                logger.info("Insert kpi_alarm_noc");
                                db.insertKpiAlarmNoc(lstResult);
                            }
                        }
                        //</editor-fold>

                        //<editor-fold defaultstate="collapsed" desc="CONSECUTIVE_MONTHS">
                        if (ALARM_TYPE.MONTH.equals(alarmConfig.getAlarmType())
                                && ALARM_RULE.CONSECUTIVE_MONTHS.equals(alarmConfig.getAlarmRule())) {
                            logger.info("----------Canh bao thang: thang lien tiep----------");
                            logger.info("Vuot nguong: " + alarmConfig.getAlarmExceed());
                            logger.info("So ngay lien tiep: " + alarmConfig.getAlarmRuleValue());

                            logger.info("----------Canh bao thang: AddonTDXL----------");
//                            List<KpiAlarmNoc> lst = db.getConsecutiveMonthsAddonTDXL(alarmConfig.getAlarmExceed(),
//                                    alarmConfig.getAlarmRuleValue());
//                            if (!lst.isEmpty()) {
//                                logger.info("Insert kpi_alarm_noc");
//                                db.insertKpiAlarmNoc(lst);
//                            }
                        }
                        //</editor-fold>

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
