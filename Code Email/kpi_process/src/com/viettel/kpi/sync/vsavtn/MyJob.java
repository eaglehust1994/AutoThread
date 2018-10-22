/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.sync.vsavtn;

import com.viettel.kpi.common.utils.DateTimeUtils;
import com.viettel.vsaadmin.service.Actor;
import com.viettel.vsaadmin.service.DepartmentBO;
import com.viettel.vsaadmin.service.Response;
import com.viettel.vsaadmin.service.UserInfo;
import com.viettel.vsaadmin.service.Users;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyJob implements Runnable {
    
    private MyDbTask db;
    private int hourRun;
    private String wsVsaVtn;
    private String userName;
    private String password;
    private static Logger logger = Logger.getLogger(MyJob.class);
    
    public MyJob() {
    }
    
    public MyJob(int hourRun, String wsVsaVtn, String userName, String password, MyDbTask db) {
        this.hourRun = hourRun;
        this.wsVsaVtn = wsVsaVtn;
        this.userName = userName;
        this.password = password;
        this.db = db;
    }
    
    @Override
    public void run() {
        try {
            logger.info("===========Start Sync VSA VTN============");
            List<UserInfo> lstUser = new ArrayList<UserInfo>();
            List<DepartmentBO> lstDept = new ArrayList<DepartmentBO>();
            
            getDataFromWs(lstUser, lstDept);
            
            db.syncUserVtn(lstUser);
            db.syncDepartmentVtn(lstDept);
            db.insertDepartmentVtnOften(ProcessManager.idTCT);
            
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
                logger.info("===========Finish Sync VSA VTN============");
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
    
    public void getDataFromWs(List<UserInfo> lstUser, List<DepartmentBO> lstDept) throws InterruptedException {
        int reconnect = 1;
        boolean checkWs = true;
        Response response;
        List<Object> lst = new ArrayList<Object>();
        String syncDate = DateTimeUtils.format(DateTimeUtils.add(new Date(), -1), "dd/MM/yyyy");
        VsaVtnServiceUtil service = new VsaVtnServiceUtil(wsVsaVtn);
        Actor actor = new Actor();
        actor.setUserName(userName);
        actor.setPassword(password);
        
        while ((reconnect <= 3 && !checkWs) || reconnect == 1) {
            try {
                reconnect = reconnect + 1;
                checkWs = true;

                //<editor-fold defaultstate="collapsed" desc="user">
                logger.info("get List User tu ngay " + syncDate + " den ngay hien tai");
                response = service.getUserInfoBySyncDate(actor, "", syncDate);
                lst = response.getValues();
                for (Object obj : lst) {
                    lstUser.add((UserInfo) obj);
                }
                logger.info("lstUser size: " + lstUser.size());
                //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="dept">
                logger.info("get List Dept tu ngay " + syncDate + " den ngay hien tai");
                response = service.getDepartmentTree(actor, syncDate, "");
                lst = response.getValues();
                for (Object obj : lst) {
                    lstDept.add((DepartmentBO) obj);
                }
                logger.info("lstDept size: " + lstDept.size());
                //</editor-fold>
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                reconnect = reconnect + 1;
                checkWs = false;
                logger.info("Sleep 10s to try connect WS VSA....");
                Thread.sleep(10000);
                
            }
        }
    }
}
