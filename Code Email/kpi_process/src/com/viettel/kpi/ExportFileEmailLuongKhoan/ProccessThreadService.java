/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.ExportFileEmailLuongKhoan;

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
    public long hour;
    public int day;
    public long intervalSleep = 3600000L;
    public long interval;
    public String pathFile;
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

    public ProccessThreadService(Long interval,Long hour,String pathFile) throws Exception {
        super("ExportFileEmail");
        this.interval = interval;
        this.hour = hour;
        this.pathFile=pathFile;
    }

    @Override
    protected void process() {
        try {
            client = new MyClient();
                int hourCurrent = getCurrentHour();
                logger.info("current hour: " + hourCurrent);
                logger.info("starthour: " + hour);
                if (hour != hourCurrent) {
                    logger.info("Chua den thoi gian chay tien trinh");
                }
               if (hour == hourCurrent) {
//                    ProccessEmailThreadService thread = new ProccessEmailThreadService(lst, logger);
                    ProccessEmailThreadService thread = new ProccessEmailThreadService(logger,pathFile);
                    thread.run();
                }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (client != null) {
                    client.close();
                    logger.info("Da chay xong tien trinh");
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
}
