/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.viettel.framework.scheduler;

import com.viettel.framework.service.manager.ServiceManager;

/**
 *
 * @author qlmvt_MinhHT1
 */
public class Stop {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServiceManager.stop();
    }
}
