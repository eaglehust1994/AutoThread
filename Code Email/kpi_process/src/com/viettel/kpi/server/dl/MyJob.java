/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.server.dl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyJob implements Runnable {

//    private MyDbTask db;
    private String updateTime;
    private boolean reload;
    private static Logger logger = Logger.getLogger(MyJob.class);

    public MyJob() {
    }

    public MyJob(String updateTime, boolean reload) {
        this.updateTime = updateTime;
        this.reload = reload;
//        this.db = db;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start Kpi Server Dl============");

            //count
//            if (!reload) {
//                updateTime = DateTimeUtils.format(DateTimeUtils.add(new Date(), -1), "yyyyMMdd");
//            }
//            Date _updateTime = DateTimeUtils.parse(updateTime, "yyyyMMdd");
//            logger.info("updateTime: " + updateTime);
            String[] cmd2 = {"/bin/sh", "-c",
                "../kpi_server_ftp_upcode_0.1/kpi_server_ftp_upcode/kpi_server_ftp_upcode_run.sh"};
            executeCommands(cmd2);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
//            try {
//                if (db != null) {
//                    db.close();
//                }
//            } catch (Exception ex) {
//                logger.error(ex.getMessage(), ex);
//            }
        }
    }

    private String executeCommands(String[] command) {
        StringBuffer output = new StringBuffer();
        Process p;
        try {
            logger.info("command: " + command);
            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
//                output.append(line + "\n");
                logger.info(line);
            }
//            logger.info(line);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return output.toString();
    }
}
