/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.lostData.rate;

import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.kpi.getDataSql.common.CommonLogServer;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyJob implements Runnable {

    private MyDbTask db;
    private String updateTime;
    private boolean reload;
    private String ipClient;
    private String urlClient;
    private String usernameClient;
    private String passwordClient;
    private static Logger logger = Logger.getLogger(MyJob.class);

    public MyJob() {
    }

    public MyJob(String updateTime, boolean reload,
            String ipClient, String urlClient, String usernameClient, String passwordClient, MyDbTask db) {
        this.updateTime = updateTime;
        this.reload = reload;
        this.ipClient = ipClient;
        this.urlClient = urlClient;
        this.usernameClient = usernameClient;
        this.passwordClient = passwordClient;
        this.db = db;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start LostData Rate============");

            //count
            if (!reload) {
                updateTime = DateTimeUtils.format(DateTimeUtils.add(new Date(), -1), "dd/MM/yyyy");
            }
            Date _updateTime = DateTimeUtils.parse(updateTime, "dd/MM/yyyy");
            logger.info("updateTime: " + updateTime);

//            db = new MyDbTask();
            MyDbTaskClient dbClient = new MyDbTaskClient();
            CommonLogServer logServerClient = new CommonLogServer(ipClient, "oracle.jdbc.OracleDriver",
                    urlClient, usernameClient, passwordClient);
            dbClient.connectOracle(logServerClient);

            Long countLogClient = db.getCountLogClient(_updateTime);
            logger.info("countLogClient: " + countLogClient);
            Long countLogServer = dbClient.getCountLogServer(_updateTime);
            logger.info("countLogServer: " + countLogServer);
            if (reload) {
                logger.info("Chay bu du lieu");
                db.deleteLostDataRate(_updateTime);
            }
            db.insertLostDataRate(_updateTime, countLogServer, countLogClient);

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
}
