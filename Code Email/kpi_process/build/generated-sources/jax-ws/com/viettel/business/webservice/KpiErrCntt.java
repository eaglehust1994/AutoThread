
package com.viettel.business.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for kpiErrCntt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="kpiErrCntt">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="breakdown1" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="breakdown2" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="breakdown3" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cause" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerObj" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateComplain" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="downtime" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="downtimeCntt" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="downtimeVtnet" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="effectLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="errAll" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="errCntt" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="errContent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="errVtnet" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="kpiDhml" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="managerDepartment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monthYear" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="note" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numComplain" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="proplem" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receiveTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="reportTct" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="reportTd" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="responsibilityDep" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="responsibilityHuman" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="solution" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="systemGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="systemLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="systemName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timeTotal" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="week" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="xlTd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="xlUctt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "kpiErrCntt", propOrder = {
    "breakdown1",
    "breakdown2",
    "breakdown3",
    "cause",
    "customerObj",
    "dateComplain",
    "downtime",
    "downtimeCntt",
    "downtimeVtnet",
    "effectLevel",
    "endTime",
    "errAll",
    "errCntt",
    "errContent",
    "errGroup",
    "errTime",
    "errVtnet",
    "id",
    "kpiDhml",
    "managerDepartment",
    "monthYear",
    "note",
    "numComplain",
    "proplem",
    "receiveTime",
    "reportTct",
    "reportTd",
    "responsibilityDep",
    "responsibilityHuman",
    "solution",
    "systemGroup",
    "systemLevel",
    "systemName",
    "timeTotal",
    "type",
    "week",
    "xlTd",
    "xlUctt"
})
public class KpiErrCntt {

    protected Long breakdown1;
    protected Long breakdown2;
    protected Long breakdown3;
    protected String cause;
    protected String customerObj;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateComplain;
    protected Double downtime;
    protected Double downtimeCntt;
    protected Double downtimeVtnet;
    protected String effectLevel;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar endTime;
    protected Long errAll;
    protected Long errCntt;
    protected String errContent;
    protected String errGroup;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar errTime;
    protected Long errVtnet;
    protected Long id;
    protected Long kpiDhml;
    protected String managerDepartment;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar monthYear;
    protected String note;
    protected Long numComplain;
    protected String proplem;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar receiveTime;
    protected Long reportTct;
    protected Long reportTd;
    protected String responsibilityDep;
    protected String responsibilityHuman;
    protected String solution;
    protected String systemGroup;
    protected String systemLevel;
    protected String systemName;
    protected Double timeTotal;
    protected Long type;
    protected String week;
    protected String xlTd;
    protected String xlUctt;

    /**
     * Gets the value of the breakdown1 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBreakdown1() {
        return breakdown1;
    }

    /**
     * Sets the value of the breakdown1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBreakdown1(Long value) {
        this.breakdown1 = value;
    }

    /**
     * Gets the value of the breakdown2 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBreakdown2() {
        return breakdown2;
    }

    /**
     * Sets the value of the breakdown2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBreakdown2(Long value) {
        this.breakdown2 = value;
    }

    /**
     * Gets the value of the breakdown3 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBreakdown3() {
        return breakdown3;
    }

    /**
     * Sets the value of the breakdown3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBreakdown3(Long value) {
        this.breakdown3 = value;
    }

    /**
     * Gets the value of the cause property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCause() {
        return cause;
    }

    /**
     * Sets the value of the cause property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCause(String value) {
        this.cause = value;
    }

    /**
     * Gets the value of the customerObj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerObj() {
        return customerObj;
    }

    /**
     * Sets the value of the customerObj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerObj(String value) {
        this.customerObj = value;
    }

    /**
     * Gets the value of the dateComplain property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateComplain() {
        return dateComplain;
    }

    /**
     * Sets the value of the dateComplain property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateComplain(XMLGregorianCalendar value) {
        this.dateComplain = value;
    }

    /**
     * Gets the value of the downtime property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDowntime() {
        return downtime;
    }

    /**
     * Sets the value of the downtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDowntime(Double value) {
        this.downtime = value;
    }

    /**
     * Gets the value of the downtimeCntt property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDowntimeCntt() {
        return downtimeCntt;
    }

    /**
     * Sets the value of the downtimeCntt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDowntimeCntt(Double value) {
        this.downtimeCntt = value;
    }

    /**
     * Gets the value of the downtimeVtnet property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDowntimeVtnet() {
        return downtimeVtnet;
    }

    /**
     * Sets the value of the downtimeVtnet property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDowntimeVtnet(Double value) {
        this.downtimeVtnet = value;
    }

    /**
     * Gets the value of the effectLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEffectLevel() {
        return effectLevel;
    }

    /**
     * Sets the value of the effectLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEffectLevel(String value) {
        this.effectLevel = value;
    }

    /**
     * Gets the value of the endTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndTime() {
        return endTime;
    }

    /**
     * Sets the value of the endTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndTime(XMLGregorianCalendar value) {
        this.endTime = value;
    }

    /**
     * Gets the value of the errAll property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getErrAll() {
        return errAll;
    }

    /**
     * Sets the value of the errAll property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setErrAll(Long value) {
        this.errAll = value;
    }

    /**
     * Gets the value of the errCntt property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getErrCntt() {
        return errCntt;
    }

    /**
     * Sets the value of the errCntt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setErrCntt(Long value) {
        this.errCntt = value;
    }

    /**
     * Gets the value of the errContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrContent() {
        return errContent;
    }

    /**
     * Sets the value of the errContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrContent(String value) {
        this.errContent = value;
    }

    /**
     * Gets the value of the errGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrGroup() {
        return errGroup;
    }

    /**
     * Sets the value of the errGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrGroup(String value) {
        this.errGroup = value;
    }

    /**
     * Gets the value of the errTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getErrTime() {
        return errTime;
    }

    /**
     * Sets the value of the errTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setErrTime(XMLGregorianCalendar value) {
        this.errTime = value;
    }

    /**
     * Gets the value of the errVtnet property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getErrVtnet() {
        return errVtnet;
    }

    /**
     * Sets the value of the errVtnet property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setErrVtnet(Long value) {
        this.errVtnet = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the kpiDhml property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getKpiDhml() {
        return kpiDhml;
    }

    /**
     * Sets the value of the kpiDhml property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setKpiDhml(Long value) {
        this.kpiDhml = value;
    }

    /**
     * Gets the value of the managerDepartment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManagerDepartment() {
        return managerDepartment;
    }

    /**
     * Sets the value of the managerDepartment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManagerDepartment(String value) {
        this.managerDepartment = value;
    }

    /**
     * Gets the value of the monthYear property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMonthYear() {
        return monthYear;
    }

    /**
     * Sets the value of the monthYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMonthYear(XMLGregorianCalendar value) {
        this.monthYear = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

    /**
     * Gets the value of the numComplain property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumComplain() {
        return numComplain;
    }

    /**
     * Sets the value of the numComplain property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumComplain(Long value) {
        this.numComplain = value;
    }

    /**
     * Gets the value of the proplem property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProplem() {
        return proplem;
    }

    /**
     * Sets the value of the proplem property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProplem(String value) {
        this.proplem = value;
    }

    /**
     * Gets the value of the receiveTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReceiveTime() {
        return receiveTime;
    }

    /**
     * Sets the value of the receiveTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReceiveTime(XMLGregorianCalendar value) {
        this.receiveTime = value;
    }

    /**
     * Gets the value of the reportTct property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getReportTct() {
        return reportTct;
    }

    /**
     * Sets the value of the reportTct property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setReportTct(Long value) {
        this.reportTct = value;
    }

    /**
     * Gets the value of the reportTd property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getReportTd() {
        return reportTd;
    }

    /**
     * Sets the value of the reportTd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setReportTd(Long value) {
        this.reportTd = value;
    }

    /**
     * Gets the value of the responsibilityDep property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponsibilityDep() {
        return responsibilityDep;
    }

    /**
     * Sets the value of the responsibilityDep property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponsibilityDep(String value) {
        this.responsibilityDep = value;
    }

    /**
     * Gets the value of the responsibilityHuman property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponsibilityHuman() {
        return responsibilityHuman;
    }

    /**
     * Sets the value of the responsibilityHuman property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponsibilityHuman(String value) {
        this.responsibilityHuman = value;
    }

    /**
     * Gets the value of the solution property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSolution() {
        return solution;
    }

    /**
     * Sets the value of the solution property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSolution(String value) {
        this.solution = value;
    }

    /**
     * Gets the value of the systemGroup property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemGroup() {
        return systemGroup;
    }

    /**
     * Sets the value of the systemGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemGroup(String value) {
        this.systemGroup = value;
    }

    /**
     * Gets the value of the systemLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemLevel() {
        return systemLevel;
    }

    /**
     * Sets the value of the systemLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemLevel(String value) {
        this.systemLevel = value;
    }

    /**
     * Gets the value of the systemName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * Sets the value of the systemName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemName(String value) {
        this.systemName = value;
    }

    /**
     * Gets the value of the timeTotal property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTimeTotal() {
        return timeTotal;
    }

    /**
     * Sets the value of the timeTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTimeTotal(Double value) {
        this.timeTotal = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setType(Long value) {
        this.type = value;
    }

    /**
     * Gets the value of the week property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeek() {
        return week;
    }

    /**
     * Sets the value of the week property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeek(String value) {
        this.week = value;
    }

    /**
     * Gets the value of the xlTd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXlTd() {
        return xlTd;
    }

    /**
     * Sets the value of the xlTd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXlTd(String value) {
        this.xlTd = value;
    }

    /**
     * Gets the value of the xlUctt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXlUctt() {
        return xlUctt;
    }

    /**
     * Sets the value of the xlUctt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXlUctt(String value) {
        this.xlUctt = value;
    }

}
