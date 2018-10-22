
package com.viettel.pnms.webservices;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.viettel.pnms.webservices package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetKpiSuCoCoDien_QNAME = new QName("http://webservices.pnms.viettel.com/", "getKpiSuCoCoDien");
    private final static QName _GetKpiMonthResponse_QNAME = new QName("http://webservices.pnms.viettel.com/", "getKpiMonthResponse");
    private final static QName _GetKpiNhietDoCaoResponse_QNAME = new QName("http://webservices.pnms.viettel.com/", "getKpiNhietDoCaoResponse");
    private final static QName _GetKpiSuCoCoDienResponse_QNAME = new QName("http://webservices.pnms.viettel.com/", "getKpiSuCoCoDienResponse");
    private final static QName _GetKpiSaiQuyTrinh_QNAME = new QName("http://webservices.pnms.viettel.com/", "getKpiSaiQuyTrinh");
    private final static QName _GetKpiMonth_QNAME = new QName("http://webservices.pnms.viettel.com/", "getKpiMonth");
    private final static QName _GetKpiNhietDoCao_QNAME = new QName("http://webservices.pnms.viettel.com/", "getKpiNhietDoCao");
    private final static QName _GetKpiSaiQuyTrinhResponse_QNAME = new QName("http://webservices.pnms.viettel.com/", "getKpiSaiQuyTrinhResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.viettel.pnms.webservices
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetKpiSaiQuyTrinh }
     * 
     */
    public GetKpiSaiQuyTrinh createGetKpiSaiQuyTrinh() {
        return new GetKpiSaiQuyTrinh();
    }

    /**
     * Create an instance of {@link GetKpiMonthResponse }
     * 
     */
    public GetKpiMonthResponse createGetKpiMonthResponse() {
        return new GetKpiMonthResponse();
    }

    /**
     * Create an instance of {@link GetKpiNhietDoCaoResponse }
     * 
     */
    public GetKpiNhietDoCaoResponse createGetKpiNhietDoCaoResponse() {
        return new GetKpiNhietDoCaoResponse();
    }

    /**
     * Create an instance of {@link GetKpiSuCoCoDienResponse }
     * 
     */
    public GetKpiSuCoCoDienResponse createGetKpiSuCoCoDienResponse() {
        return new GetKpiSuCoCoDienResponse();
    }

    /**
     * Create an instance of {@link GetKpiSuCoCoDien }
     * 
     */
    public GetKpiSuCoCoDien createGetKpiSuCoCoDien() {
        return new GetKpiSuCoCoDien();
    }

    /**
     * Create an instance of {@link GetKpiSaiQuyTrinhResponse }
     * 
     */
    public GetKpiSaiQuyTrinhResponse createGetKpiSaiQuyTrinhResponse() {
        return new GetKpiSaiQuyTrinhResponse();
    }

    /**
     * Create an instance of {@link GetKpiMonth }
     * 
     */
    public GetKpiMonth createGetKpiMonth() {
        return new GetKpiMonth();
    }

    /**
     * Create an instance of {@link GetKpiNhietDoCao }
     * 
     */
    public GetKpiNhietDoCao createGetKpiNhietDoCao() {
        return new GetKpiNhietDoCao();
    }

    /**
     * Create an instance of {@link KpiCoDienForm }
     * 
     */
    public KpiCoDienForm createKpiCoDienForm() {
        return new KpiCoDienForm();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetKpiSuCoCoDien }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.pnms.viettel.com/", name = "getKpiSuCoCoDien")
    public JAXBElement<GetKpiSuCoCoDien> createGetKpiSuCoCoDien(GetKpiSuCoCoDien value) {
        return new JAXBElement<GetKpiSuCoCoDien>(_GetKpiSuCoCoDien_QNAME, GetKpiSuCoCoDien.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetKpiMonthResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.pnms.viettel.com/", name = "getKpiMonthResponse")
    public JAXBElement<GetKpiMonthResponse> createGetKpiMonthResponse(GetKpiMonthResponse value) {
        return new JAXBElement<GetKpiMonthResponse>(_GetKpiMonthResponse_QNAME, GetKpiMonthResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetKpiNhietDoCaoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.pnms.viettel.com/", name = "getKpiNhietDoCaoResponse")
    public JAXBElement<GetKpiNhietDoCaoResponse> createGetKpiNhietDoCaoResponse(GetKpiNhietDoCaoResponse value) {
        return new JAXBElement<GetKpiNhietDoCaoResponse>(_GetKpiNhietDoCaoResponse_QNAME, GetKpiNhietDoCaoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetKpiSuCoCoDienResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.pnms.viettel.com/", name = "getKpiSuCoCoDienResponse")
    public JAXBElement<GetKpiSuCoCoDienResponse> createGetKpiSuCoCoDienResponse(GetKpiSuCoCoDienResponse value) {
        return new JAXBElement<GetKpiSuCoCoDienResponse>(_GetKpiSuCoCoDienResponse_QNAME, GetKpiSuCoCoDienResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetKpiSaiQuyTrinh }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.pnms.viettel.com/", name = "getKpiSaiQuyTrinh")
    public JAXBElement<GetKpiSaiQuyTrinh> createGetKpiSaiQuyTrinh(GetKpiSaiQuyTrinh value) {
        return new JAXBElement<GetKpiSaiQuyTrinh>(_GetKpiSaiQuyTrinh_QNAME, GetKpiSaiQuyTrinh.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetKpiMonth }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.pnms.viettel.com/", name = "getKpiMonth")
    public JAXBElement<GetKpiMonth> createGetKpiMonth(GetKpiMonth value) {
        return new JAXBElement<GetKpiMonth>(_GetKpiMonth_QNAME, GetKpiMonth.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetKpiNhietDoCao }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.pnms.viettel.com/", name = "getKpiNhietDoCao")
    public JAXBElement<GetKpiNhietDoCao> createGetKpiNhietDoCao(GetKpiNhietDoCao value) {
        return new JAXBElement<GetKpiNhietDoCao>(_GetKpiNhietDoCao_QNAME, GetKpiNhietDoCao.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetKpiSaiQuyTrinhResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.pnms.viettel.com/", name = "getKpiSaiQuyTrinhResponse")
    public JAXBElement<GetKpiSaiQuyTrinhResponse> createGetKpiSaiQuyTrinhResponse(GetKpiSaiQuyTrinhResponse value) {
        return new JAXBElement<GetKpiSaiQuyTrinhResponse>(_GetKpiSaiQuyTrinhResponse_QNAME, GetKpiSaiQuyTrinhResponse.class, null, value);
    }

}
