/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.kpi.sync.vsavtn;

import com.viettel.kpi.common.utils.Constants.WS;
import com.viettel.vsaadmin.service.Actor;
import com.viettel.vsaadmin.service.Response;
import com.viettel.vsaadmin.service.VsaadminService;
import com.viettel.vsaadmin.service.VsaadminServiceService;
import java.net.URL;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.apache.log4j.Logger;

/**
 *
 * @author dungvv8
 */
public class VsaVtnServiceUtil {

    private URL url;
    private QName qName;
    VsaadminService portService;
    private static final Logger log = Logger.getLogger(VsaVtnServiceUtil.class);

    public VsaVtnServiceUtil() {
        URL baseUrl;
        baseUrl = VsaadminServiceService.class.getResource(".");
        try {
            url = new URL(baseUrl, "http://10.58.71.48:8280/vsaadminv3/VsaadminService?wsdl");
            qName = new QName("http://service.vsaadmin.viettel.com/", "VsaadminServiceService");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public VsaVtnServiceUtil(String wsVsaVtn) {
        try {
            URL baseUrl = VsaadminServiceService.class.getResource(".");
            url = new URL(baseUrl, wsVsaVtn);
            qName = new QName("http://service.vsaadmin.viettel.com/", "VsaadminServiceService");
            log.info("===========Begin connect WS VSA VTN============");
            VsaadminServiceService service = new VsaadminServiceService(url, qName);
            log.info("service: " + service);
            portService = service.getVsaadminServicePort();
            log.info("portService: " + portService);

            Map<String, Object> requestContext = ((BindingProvider) portService).getRequestContext();
            requestContext.put(WS.REQUEST_TIMEOUT, ProcessManager.requestTimeout);
            requestContext.put(WS.CONNECT_TIMEOUT, ProcessManager.connectTimeout);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            ex.printStackTrace();
        }
    }

    public Response getUserInfoBySyncDate(Actor actor, String userName, String syncDate) {
        Response result = null;
        try {
            result = portService.getUserInfoBySyncDate(actor, userName, syncDate);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            ex.printStackTrace();
        }
        return result;
    }

    public Response getDepartmentTree(Actor actor, String syncDate, String deptCode) {
        Response result = null;
        try {
            result = portService.getDepartmentTree(actor, syncDate, deptCode);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            ex.printStackTrace();
        }
        return result;
    }
}
