/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.ExportFileEmailMonthly;

import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.mmserver.base.ProcessThreadMX;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author dungvv8
 */
public class ProccessThreadService extends ProcessThreadMX {

//    DbTask db;
    MyClient client;
    public int hour;
    public int day;
    public long intervalSleep = 3600000L;
    public long interval;
    private Socket requestSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Boolean checkData;

    public Socket getRequestSocket() {
        return requestSocket;
    }

    public void setRequestSocket(Socket requestSocket) {
        this.requestSocket = requestSocket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public ProccessThreadService(Long interval) throws Exception {
        super("ExportFileEmail");
        this.interval = interval;
    }

    @Override
    protected void process() {
        try {
            client = new MyClient();
            Map<String, List<InformationIsFileForm>> mapInfomationIsFile = client.getInfomationIsFile();
            for (Map.Entry<String, List<InformationIsFileForm>> entry : mapInfomationIsFile.entrySet()) {
//                List<InformationIsFileForm> lst = entry.getValue();
//
//                Integer isRunHour = lst.get(0).getIsHourRun();
//                Integer isDayHour = lst.get(0).getIsDayRun();
//                Date timeRun = lst.get(0).getEndTimeRun();
//
//                String endTimeRun = DateTimeUtils.convertDateTimeToString(timeRun, "dd/MM/yyyy");
//                String timeCurent = DateTimeUtils.getSysDateTime("dd/MM/yyyy");
//
//                int hourCurrent = getCurrentHour();
//                int dayCurrentOfMonth = getCurrentDayOfMonth();
//                logger.info("Loai tien trinh gui Email: " + entry.getKey());
//                logger.info("current hour: " + hourCurrent);
//                logger.info("starthour: " + isRunHour);
//                if (isRunHour != hourCurrent) {
//                    logger.info("Chua den thoi gian chay tien trinh");
//                }
//                if (isDayHour != dayCurrentOfMonth) {
//                    logger.info("Chua den ngay cua thang chay tien trinh");
//                }
//                if ((isDayHour == dayCurrentOfMonth) && (isRunHour == hourCurrent)) {
//                    if (endTimeRun.equalsIgnoreCase(timeCurent)) {
//                        logger.info("Loai tien trinh gui Email: " + lst + " Da chay roi");
//                    } else {
//                        ProccessEmailThreadService thread = new ProccessEmailThreadService(lst, logger);
//                        thread.run();
//                    }
//                }
                  List<InformationIsFileForm> lst = entry.getValue();
                Integer isRunHour = lst.get(0).getHour_run();
                Integer isDayHour = lst.get(0).getDay_run();
                int hourCurrent = getCurrentHour();
                int dayCurrent = getCurrentDayOfMonth();
                logger.info("Loai tien trinh gui Email: " + entry.getKey());
                logger.info("current hour: " + hourCurrent);
                logger.info("starthour: " + isRunHour);
                if (isRunHour != hourCurrent) {
                    logger.info("Chua den thoi gian chay tien trinh");
                }
                if (isDayHour != dayCurrent) {
                    logger.info("Chua den ngay trong thang chay tien trinh");
                }
                if ((isDayHour == dayCurrent) && (isRunHour == hourCurrent)) {
                    ProccessEmailThreadService thread = new ProccessEmailThreadService(lst, logger);
                    thread.run();
                }
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
            try {
                logger.info("Sleep thread: " + interval + "Milisecon");
                Thread.sleep(interval);
            } catch (Exception e) {
            }
        }

    }

    public int getCurrentHour() {
        int currentHour = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentHour = now.get(Calendar.HOUR_OF_DAY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentHour;
    }

    public int getCurrentDayOfMonth() {
        int currentMinute = 0;
        try {
            Calendar now = Calendar.getInstance();
            currentMinute = now.get(Calendar.DAY_OF_MONTH);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return currentMinute;
    }
}
