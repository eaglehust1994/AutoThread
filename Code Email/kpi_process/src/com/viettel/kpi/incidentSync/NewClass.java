/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.incidentSync;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author hainm24
 */
public class NewClass {

    public static void main(String[] args) throws Exception{
        Calendar startPreviousday = Calendar.getInstance();
        startPreviousday.add(Calendar.DAY_OF_MONTH, -1);
        startPreviousday.set(Calendar.HOUR_OF_DAY, 0);
        startPreviousday.set(Calendar.MINUTE, 0);
        startPreviousday.set(Calendar.SECOND, 0);
        Calendar sysdate = Calendar.getInstance();
        sysdate.set(Calendar.HOUR_OF_DAY, 0);
        sysdate.set(Calendar.MINUTE, 0);
        sysdate.set(Calendar.SECOND, 0);
        
        System.out.println(startPreviousday.getTime()+" "+sysdate.getTime());
        GregorianCalendar gcStartTime = new GregorianCalendar();
            gcStartTime.setTime(null);
            XMLGregorianCalendar xmStartTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcStartTime);
    }

}
