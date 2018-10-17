/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.ctct;

import org.apache.log4j.PropertyConfigurator;

//  @author hoanm1
public class Start {

    static {
        try {
            PropertyConfigurator.configure("../etc/log4j.cfg");
        } catch (Exception ex) {
        }
    }

    public static void main(String[] args) {
        ProcessManager.start();
    }
}
