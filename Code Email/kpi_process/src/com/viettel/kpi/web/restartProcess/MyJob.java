/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.web.restartProcess;

import com.viettel.kpi.common.utils.DateTimeUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyJob implements Runnable {

    private MyDbTask db;

    private String updateTime;
    private int hourRun;
    private int minRun;
    private boolean reload;
    private static Logger logger = Logger.getLogger(MyJob.class);

    public MyJob() {
    }

    public MyJob(String updateTime, int hourRun, int minRun, boolean reload, MyDbTask db) {

        this.updateTime = updateTime;
        this.hourRun = hourRun;
        this.minRun = minRun;
        this.reload = reload;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start Restart Process============");

            //count
//            if (!reload) {
            updateTime = DateTimeUtils.format(new Date(), "dd/MM/yyyy");
            hourRun = DateTimeUtils.getCurrentHour();
//            }
            Date _updateTime = DateTimeUtils.parse(updateTime, "dd/MM/yyyy");
            logger.info("updateTime: " + updateTime);
            logger.info("hourRun: " + hourRun);

            logger.info("Kiem tra da co ban ghi raw insert chua");
            if (!db.existData(_updateTime, hourRun - 1)) {
                logger.info("Chua co ban ghi insert");
                logger.info("Restart tien trinh hl");

//                logger.info("Cd den thu muc bin");
//                String[] cmd = {"/bin/sh", "-c", "cd /u01/kpi/process_active/kpiWebHl/bin"};
//                executeCommands(cmd);
//
//                logger.info("pwd");
//                String[] cmd1 = {"/bin/sh", "-c", "pwd"};
//                executeCommands(cmd1);

                logger.info("Chay lenh restart");
                String[] cmd2 = {"/bin/sh", "-c", "/u01/kpi/process_active/kpiWebHl/bin/run restart"};
                executeCommands(cmd2);

            } else {
                logger.info("Da co ban ghi insert roi");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private String executeCommand(String command) {
        StringBuffer output = new StringBuffer();
        Process p;
        try {

            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            logger.info(line);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return output.toString();
    }

    private String executeCommands(String[] command) {
        StringBuffer output = new StringBuffer();
        Process p;
        try {

            p = Runtime.getRuntime().exec(command);
            p.waitFor();
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            logger.info(line);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return output.toString();
    }
}
