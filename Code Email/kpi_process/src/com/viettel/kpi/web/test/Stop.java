/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.test;

import org.quartz.SchedulerException;

/**
 *
 * @author Michael
 */
public class Stop {

    public static void main(String[] args) throws SchedulerException {
        JobManager.getInstance().Stop();
    }
}
