/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.framework.scheduler;

import com.viettel.framework.service.common.DbTask;
import com.viettel.framework.service.common.LogServer;
import com.viettel.framework.service.manager.Job;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

/**
 *
 * @author qlmvt_MinhHT1
 */
public class MyJob extends Job {

    public DbTask db;

    public MyJob() {
    }

    public void initConnection() throws Exception {
        db = new MyDbTask();
        db.init(Start.dbFile);
    }

    public void releaseConnection() throws Exception {
//        db.close();
    }

    @Override
    public void run(LogServer logServer, Logger logger) {
        try {
            logger.info("===== BEGIN EXEC COMMAND =====");
            execCommand("../jar/run.bat", logger);
            logger.info("===== END EXEC COMMAND =====");
        } catch (Exception ex) {
            logger.error(ex.toString());
            ex.printStackTrace();
        }
    }

    public void execCommand(String command, Logger logger) {

        try {
            String endLine = System.getProperty("line.separator");
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(command);

            BufferedReader inPing = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine = "";
            while ((inputLine = inPing.readLine()) != null) {
//                logger.info(inputLine);
            }

            inPing.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("send Command: " + ex.getMessage());
        }
    }
}
