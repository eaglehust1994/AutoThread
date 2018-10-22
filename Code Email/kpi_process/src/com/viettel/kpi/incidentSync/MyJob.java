/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.incidentSync;

import com.viettel.business.webservice.IncidentCnttObj;
import com.viettel.business.webservice.KpiErrCntt;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyJob implements Runnable {

    private String updateTime;
    private boolean reload;
    private static Logger logger = Logger.getLogger(MyJob.class);
    private Date startTime;
    private Date endTime;

    public MyJob() {
    }

    public MyJob(String updateTime, boolean reload, Date startTime, Date endTime) {
        this.updateTime = updateTime;
        this.reload = reload;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start sync incident Daily============");
            //default: sync data the day before
            if (startTime == null && endTime == null) {
                Calendar startPreviousday = Calendar.getInstance();
                startPreviousday.add(Calendar.DAY_OF_MONTH, -1);
                startPreviousday.set(Calendar.HOUR_OF_DAY, 0);
                startPreviousday.set(Calendar.MINUTE, 0);
                startPreviousday.set(Calendar.SECOND, 0);
                Calendar endPreviousDay = Calendar.getInstance();
                endPreviousDay.add(Calendar.DAY_OF_MONTH, -1);
                endPreviousDay.set(Calendar.HOUR_OF_DAY, 23);
                endPreviousDay.set(Calendar.MINUTE, 59);
                endPreviousDay.set(Calendar.SECOND, 59);
                startTime = startPreviousday.getTime();
                endTime = endPreviousDay.getTime();
            }
            XMLGregorianCalendar xmlStartTime = null;
            if (startTime != null) {
                GregorianCalendar gcStartTime = new GregorianCalendar();
                gcStartTime.setTime(startTime);
                xmlStartTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcStartTime);
            } else {
                throw new Exception("startTime not null");
            }
            //
//            XMLGregorianCalendar xmlEndTime = null;
            if (endTime == null) {
                endTime = new Date();
            }
            GregorianCalendar gcEndTime = new GregorianCalendar();
            gcEndTime.setTime(endTime);
            XMLGregorianCalendar xmlEndTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcEndTime);
            //
            IncidentCnttObj incidentCnttObj = getListIncidentCnttByTime(
                    "MEpwqqY2xx0sesmHpFBuZg==",
                    "EwZdQFSaDymx5wzE3K9OuQ==",
                    xmlStartTime,
                    xmlEndTime);
            List<KpiErrCntt> errCntts = incidentCnttObj.getListIncident();
            logger.info("Get List of incidents from " + startTime + " to " + endTime + ": " + errCntts.size() + " incidents");
            MyDbTask db = new MyDbTask();
            db.syncIncidents(errCntts, startTime, endTime);
            logger.info("===========Finish sync incident Daily============");
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private static com.viettel.business.webservice.IncidentCnttObj getListIncidentCnttByTime(java.lang.String user, java.lang.String password, javax.xml.datatype.XMLGregorianCalendar fromDate, javax.xml.datatype.XMLGregorianCalendar toDate) {
        com.viettel.business.webservice.IncidentCnttWebserviceImplService service = new com.viettel.business.webservice.IncidentCnttWebserviceImplService();
        com.viettel.business.webservice.IncidentCnttWebservice port = service.getIncidentCnttWebserviceImplPort();
        return port.getListIncidentCnttByTime(user, password, fromDate, toDate);
    }

}
