/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.sync.moduleCodeQltn;

import com.viettel.QltnGateproService.ModuleBO;
import com.viettel.QltnGateproService.QltnGateproService_PortType;
import com.viettel.QltnGateproService.QltnGateproService_ServiceLocator;
import com.viettel.QltnGateproService.Response;
import com.viettel.QltnGateproService.VApplicationDetailBO;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class MyJob implements Runnable {

    private MyDbTask db;

    private static Logger logger = Logger.getLogger(MyJob.class);

    public MyJob() {
    }

    public MyJob(MyDbTask db) {
        this.db = db;
    }

    @Override
    public void run() {
        try {
            logger.info("===========Start sync App Module Qltn============");
            Response res = getListModuleCodeBySearch("", "", null, null);
            Long totalRows = res.getTotalRows();
            logger.info("totalRows: " + totalRows);
            Long rowPerPage = 100l;
            logger.info("rowPerPage: " + rowPerPage);
            Long page = totalRows / rowPerPage;
            logger.info("page: " + page);

//            List<VApplicationDetailBO> lst = new ArrayList<VApplicationDetailBO>();
            logger.info("delete before insert ");
            db.deleteAppDetail();

            logger.info("insert App Module Qltn");
            int n = 0;
            for (int i = 0; i <= page; i++) {
                logger.info("get data page[" + i + "]");
                Response response = getListModuleCodeBySearch("", "", Long.valueOf(i), rowPerPage);
                List<VApplicationDetailBO> lstPerPage = getApplicationDetailBO(response);
                logger.info("so ban ghi tren page[" + i + "]: " + lstPerPage.size());
//                lst.addAll(lstPerPage);
                db.insertAppDetail(lstPerPage);
                n = n + lstPerPage.size();
            }
            logger.info("So ban ghi dong bo: " + n);

            logger.info("===========Finish sync App Module Qltn============");
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

    private static List<VApplicationDetailBO> getApplicationDetailBO(Response response) {
        List<VApplicationDetailBO> lst = new ArrayList<VApplicationDetailBO>();
        try {
            ModuleBO[] lstModuleBO = response.getLstModuleBO();
            for (ModuleBO bo : lstModuleBO) {
                lst.add(bo.getApplicationDetailBO());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return lst;
    }

    private static Response getListModuleCodeBySearch(String moduleCode, String serviceCode,
            Long pageFirst, Long pageRowCount) {
        Response response = null;
        try {
            QltnGateproService_ServiceLocator service = new QltnGateproService_ServiceLocator();
            QltnGateproService_PortType port = service.getQltnGateproServicePort();

//            Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
//            requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, Start.requestTimeout);
//            requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, Start.connectTimeout);
            response = port.getListModuleCodeBySearch(moduleCode, serviceCode, pageFirst, pageRowCount);

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return response;
    }
}
