
package com.viettel.business.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.6-1b01 
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "IncidentCnttWebserviceImplService", targetNamespace = "http://webservice.business.viettel.com/", wsdlLocation = "http://10.58.65.133:8457/ksclm/incidentCnttservice?wsdl")
public class IncidentCnttWebserviceImplService
    extends Service
{

    private final static URL INCIDENTCNTTWEBSERVICEIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException INCIDENTCNTTWEBSERVICEIMPLSERVICE_EXCEPTION;
    private final static QName INCIDENTCNTTWEBSERVICEIMPLSERVICE_QNAME = new QName("http://webservice.business.viettel.com/", "IncidentCnttWebserviceImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://10.58.65.133:8457/ksclm/incidentCnttservice?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        INCIDENTCNTTWEBSERVICEIMPLSERVICE_WSDL_LOCATION = url;
        INCIDENTCNTTWEBSERVICEIMPLSERVICE_EXCEPTION = e;
    }

    public IncidentCnttWebserviceImplService() {
        super(__getWsdlLocation(), INCIDENTCNTTWEBSERVICEIMPLSERVICE_QNAME);
    }

    public IncidentCnttWebserviceImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), INCIDENTCNTTWEBSERVICEIMPLSERVICE_QNAME, features);
    }

    public IncidentCnttWebserviceImplService(URL wsdlLocation) {
        super(wsdlLocation, INCIDENTCNTTWEBSERVICEIMPLSERVICE_QNAME);
    }

    public IncidentCnttWebserviceImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, INCIDENTCNTTWEBSERVICEIMPLSERVICE_QNAME, features);
    }

    public IncidentCnttWebserviceImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public IncidentCnttWebserviceImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns IncidentCnttWebservice
     */
    @WebEndpoint(name = "IncidentCnttWebserviceImplPort")
    public IncidentCnttWebservice getIncidentCnttWebserviceImplPort() {
        return super.getPort(new QName("http://webservice.business.viettel.com/", "IncidentCnttWebserviceImplPort"), IncidentCnttWebservice.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IncidentCnttWebservice
     */
    @WebEndpoint(name = "IncidentCnttWebserviceImplPort")
    public IncidentCnttWebservice getIncidentCnttWebserviceImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://webservice.business.viettel.com/", "IncidentCnttWebserviceImplPort"), IncidentCnttWebservice.class, features);
    }

    private static URL __getWsdlLocation() {
        if (INCIDENTCNTTWEBSERVICEIMPLSERVICE_EXCEPTION!= null) {
            throw INCIDENTCNTTWEBSERVICEIMPLSERVICE_EXCEPTION;
        }
        return INCIDENTCNTTWEBSERVICEIMPLSERVICE_WSDL_LOCATION;
    }

}