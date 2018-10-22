
package com.viettel.pnms.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for kpiCoDienForm complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="kpiCoDienForm">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="alarm_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cabinet_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="district_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="district_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="end_time" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fault_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="kpi_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="kpi_value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="kv_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="kv_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="level_type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nation_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="node_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="occured_time" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="province_code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="province_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reason_final_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="station_code_nims" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="target" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type_station" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="update_time" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vendor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="year_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "kpiCoDienForm", propOrder = {
    "alarmId",
    "cabinetName",
    "description",
    "districtCode",
    "districtName",
    "endTime",
    "faultName",
    "kpiName",
    "kpiValue",
    "kvCode",
    "kvName",
    "levelType",
    "nation",
    "nationId",
    "nodeCode",
    "occuredTime",
    "provinceCode",
    "provinceName",
    "reasonFinalName",
    "stationCodeNims",
    "target",
    "typeStation",
    "updateTime",
    "vendor",
    "yearId"
})
public class KpiCoDienForm {

    @XmlElement(name = "alarm_id")
    protected String alarmId;
    @XmlElement(name = "cabinet_name")
    protected String cabinetName;
    protected String description;
    @XmlElement(name = "district_code")
    protected String districtCode;
    @XmlElement(name = "district_name")
    protected String districtName;
    @XmlElement(name = "end_time")
    protected String endTime;
    @XmlElement(name = "fault_name")
    protected String faultName;
    @XmlElement(name = "kpi_name")
    protected String kpiName;
    @XmlElement(name = "kpi_value")
    protected String kpiValue;
    @XmlElement(name = "kv_code")
    protected String kvCode;
    @XmlElement(name = "kv_name")
    protected String kvName;
    @XmlElement(name = "level_type")
    protected String levelType;
    protected String nation;
    @XmlElement(name = "nation_id")
    protected String nationId;
    @XmlElement(name = "node_code")
    protected String nodeCode;
    @XmlElement(name = "occured_time")
    protected String occuredTime;
    @XmlElement(name = "province_code")
    protected String provinceCode;
    @XmlElement(name = "province_name")
    protected String provinceName;
    @XmlElement(name = "reason_final_name")
    protected String reasonFinalName;
    @XmlElement(name = "station_code_nims")
    protected String stationCodeNims;
    protected String target;
    @XmlElement(name = "type_station")
    protected String typeStation;
    @XmlElement(name = "update_time")
    protected String updateTime;
    protected String vendor;
    @XmlElement(name = "year_id")
    protected String yearId;

    /**
     * Gets the value of the alarmId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlarmId() {
        return alarmId;
    }

    /**
     * Sets the value of the alarmId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlarmId(String value) {
        this.alarmId = value;
    }

    /**
     * Gets the value of the cabinetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCabinetName() {
        return cabinetName;
    }

    /**
     * Sets the value of the cabinetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCabinetName(String value) {
        this.cabinetName = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the districtCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistrictCode() {
        return districtCode;
    }

    /**
     * Sets the value of the districtCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistrictCode(String value) {
        this.districtCode = value;
    }

    /**
     * Gets the value of the districtName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistrictName() {
        return districtName;
    }

    /**
     * Sets the value of the districtName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistrictName(String value) {
        this.districtName = value;
    }

    /**
     * Gets the value of the endTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the value of the endTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndTime(String value) {
        this.endTime = value;
    }

    /**
     * Gets the value of the faultName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaultName() {
        return faultName;
    }

    /**
     * Sets the value of the faultName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaultName(String value) {
        this.faultName = value;
    }

    /**
     * Gets the value of the kpiName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKpiName() {
        return kpiName;
    }

    /**
     * Sets the value of the kpiName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKpiName(String value) {
        this.kpiName = value;
    }

    /**
     * Gets the value of the kpiValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKpiValue() {
        return kpiValue;
    }

    /**
     * Sets the value of the kpiValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKpiValue(String value) {
        this.kpiValue = value;
    }

    /**
     * Gets the value of the kvCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKvCode() {
        return kvCode;
    }

    /**
     * Sets the value of the kvCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKvCode(String value) {
        this.kvCode = value;
    }

    /**
     * Gets the value of the kvName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKvName() {
        return kvName;
    }

    /**
     * Sets the value of the kvName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKvName(String value) {
        this.kvName = value;
    }

    /**
     * Gets the value of the levelType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLevelType() {
        return levelType;
    }

    /**
     * Sets the value of the levelType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLevelType(String value) {
        this.levelType = value;
    }

    /**
     * Gets the value of the nation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNation() {
        return nation;
    }

    /**
     * Sets the value of the nation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNation(String value) {
        this.nation = value;
    }

    /**
     * Gets the value of the nationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNationId() {
        return nationId;
    }

    /**
     * Sets the value of the nationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNationId(String value) {
        this.nationId = value;
    }

    /**
     * Gets the value of the nodeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeCode() {
        return nodeCode;
    }

    /**
     * Sets the value of the nodeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeCode(String value) {
        this.nodeCode = value;
    }

    /**
     * Gets the value of the occuredTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOccuredTime() {
        return occuredTime;
    }

    /**
     * Sets the value of the occuredTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOccuredTime(String value) {
        this.occuredTime = value;
    }

    /**
     * Gets the value of the provinceCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinceCode() {
        return provinceCode;
    }

    /**
     * Sets the value of the provinceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinceCode(String value) {
        this.provinceCode = value;
    }

    /**
     * Gets the value of the provinceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinceName() {
        return provinceName;
    }

    /**
     * Sets the value of the provinceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinceName(String value) {
        this.provinceName = value;
    }

    /**
     * Gets the value of the reasonFinalName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReasonFinalName() {
        return reasonFinalName;
    }

    /**
     * Sets the value of the reasonFinalName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReasonFinalName(String value) {
        this.reasonFinalName = value;
    }

    /**
     * Gets the value of the stationCodeNims property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStationCodeNims() {
        return stationCodeNims;
    }

    /**
     * Sets the value of the stationCodeNims property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStationCodeNims(String value) {
        this.stationCodeNims = value;
    }

    /**
     * Gets the value of the target property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTarget() {
        return target;
    }

    /**
     * Sets the value of the target property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTarget(String value) {
        this.target = value;
    }

    /**
     * Gets the value of the typeStation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeStation() {
        return typeStation;
    }

    /**
     * Sets the value of the typeStation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeStation(String value) {
        this.typeStation = value;
    }

    /**
     * Gets the value of the updateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * Sets the value of the updateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdateTime(String value) {
        this.updateTime = value;
    }

    /**
     * Gets the value of the vendor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Sets the value of the vendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendor(String value) {
        this.vendor = value;
    }

    /**
     * Gets the value of the yearId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYearId() {
        return yearId;
    }

    /**
     * Sets the value of the yearId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYearId(String value) {
        this.yearId = value;
    }

}
