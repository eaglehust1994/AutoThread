/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.qll.task;


import com.viettel.qll.task.ProcessManager;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author hoangnh38
 */
public class Start {
     static {
        try {
            PropertyConfigurator.configure("../etc/log4j.cfg");
        } catch (Exception e) {
        }
    }
    
    public static void main (String [] args){
        ProcessManager.start();
    }
}
