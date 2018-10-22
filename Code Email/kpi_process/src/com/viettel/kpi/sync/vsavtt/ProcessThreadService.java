/*
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.kpi.sync.vsavtt;

import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.kpi.getDataSql.common.CommonLogServer;
import com.viettel.mmserver.base.ProcessThreadMX;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class ProcessThreadService extends ProcessThreadMX {

    private static final Logger log = Logger.getLogger(ProcessThreadService.class);
    private MyDbTask db;
    private MyDbTaskClient dbClient;
    private int hourRun;
    private int idCNVT;
    private String ip;
    private String username;
    private String password;
    private String url;

    public ProcessThreadService(int hourRun) {
        super("SyncVSAVTT");
        this.hourRun = hourRun;
    }

    public ProcessThreadService(int hourRun, String ip, String username, String password, String url,int idCNVT) {
        super("SyncVSAVTT");
        this.hourRun = hourRun;
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.url = url;
        this.idCNVT = idCNVT;
    }

    @Override
    public void process() {
        try {
            log.info("\r\n\r\n***** Begin Process *****");
            db = new MyDbTask();

            dbClient = new MyDbTaskClient();
            String driver = "oracle.jdbc.OracleDriver";
            CommonLogServer logServer = new CommonLogServer(ip, driver, url, username, password);
            dbClient.connectOracle(logServer);

            MyJob job = new MyJob(idCNVT, db, dbClient);
            job.run();

        } catch (Exception ex) {
            logger.info(ex.getMessage(), ex);
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
            } catch (Exception ex) {
                logger.info(ex.getMessage(), ex);
            }
            try {
                long currMin = DateTimeUtils.getCurrentMinute();
                long sleepTime = (60 - currMin) * 60 * 1000;
                logger.info("Thread " + this.threadName + " sleep " + sleepTime / 1000 + " seconds");
                Thread.sleep(sleepTime);
            } catch (Exception ex) {
                logger.info(ex.getMessage(), ex);
            }
        }

    }
}
