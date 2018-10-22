package com.viettel.kpi.web.test;

import org.apache.log4j.PropertyConfigurator;
import org.quartz.SchedulerException;

/**
 *
 * @author michael
 */
public class Start {

    static {
        try {
            PropertyConfigurator.configure("../etc/log4j.cfg");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SchedulerException {
        // TODO code application logic here
        JobManager.getInstance().process();
    }
}
