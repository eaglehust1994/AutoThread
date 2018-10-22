/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.sync.vsavtt;

import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyJob implements Runnable {

    private MyDbTask db;
    private MyDbTaskClient dbClient;
    private int idCNVT;
    private static Logger logger = Logger.getLogger(MyJob.class);

    public MyJob() {
    }

    public MyJob(int idCNVT, MyDbTask db, MyDbTaskClient dbClient) {

        this.idCNVT = idCNVT;
        this.db = db;
        this.dbClient = dbClient;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start Sync VSA VTT============");

            List<UsersVtt> lstUser = dbClient.getUserVtt();
            if (!lstUser.isEmpty()) {
                db.deleteUserVtt();
                db.insertUserVtt(lstUser);
            }

            List<DepartmentVtt> lstDept = dbClient.getDepartmentVtt();
            if (!lstDept.isEmpty()) {
                db.deleteDepartmentVtt();
                db.insertDepartmentVtt(lstDept);
            }

//            List<DepartmentVttOften> lstDeptOften = db.getDepartmentVttOften();
//            if (!lstDeptOften.isEmpty()) {
            db.deleteDepartmentVttOften();
            db.insertDepartmentVttOften(idCNVT);
//            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (db != null) {
                    db.close();
                    dbClient.close();
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
