
package com.viettel.vsaadmin.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for response complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="response">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="errorCode" type="{http://service.vsaadmin.viettel.com/}errorCode" minOccurs="0"/>
 *         &lt;element name="lstApp" type="{http://service.vsaadmin.viettel.com/}applications" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lstEvenLogin" type="{http://service.vsaadmin.viettel.com/}logEventLogin" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lstObj" type="{http://service.vsaadmin.viettel.com/}objects" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lstObjRole" type="{http://service.vsaadmin.viettel.com/}objectRoles" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lstUser" type="{http://service.vsaadmin.viettel.com/}users" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lstUserRole" type="{http://service.vsaadmin.viettel.com/}userRoles" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="values" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "response", propOrder = {
    "code",
    "errorCode",
    "lstApp",
    "lstEvenLogin",
    "lstObj",
    "lstObjRole",
    "lstUser",
    "lstUserRole",
    "values"
})
public class Response {

    protected int code;
    protected ErrorCode errorCode;
    @XmlElement(nillable = true)
    protected List<Applications> lstApp;
    @XmlElement(nillable = true)
    protected List<LogEventLogin> lstEvenLogin;
    @XmlElement(nillable = true)
    protected List<Objects> lstObj;
    @XmlElement(nillable = true)
    protected List<ObjectRoles> lstObjRole;
    @XmlElement(nillable = true)
    protected List<Users> lstUser;
    @XmlElement(nillable = true)
    protected List<UserRoles> lstUserRole;
    @XmlElement(nillable = true)
    protected List<Object> values;

    /**
     * Gets the value of the code property.
     * 
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     */
    public void setCode(int value) {
        this.code = value;
    }

    /**
     * Gets the value of the errorCode property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorCode }
     *     
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorCode }
     *     
     */
    public void setErrorCode(ErrorCode value) {
        this.errorCode = value;
    }

    /**
     * Gets the value of the lstApp property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lstApp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLstApp().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Applications }
     * 
     * 
     */
    public List<Applications> getLstApp() {
        if (lstApp == null) {
            lstApp = new ArrayList<Applications>();
        }
        return this.lstApp;
    }

    /**
     * Gets the value of the lstEvenLogin property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lstEvenLogin property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLstEvenLogin().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LogEventLogin }
     * 
     * 
     */
    public List<LogEventLogin> getLstEvenLogin() {
        if (lstEvenLogin == null) {
            lstEvenLogin = new ArrayList<LogEventLogin>();
        }
        return this.lstEvenLogin;
    }

    /**
     * Gets the value of the lstObj property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lstObj property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLstObj().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Objects }
     * 
     * 
     */
    public List<Objects> getLstObj() {
        if (lstObj == null) {
            lstObj = new ArrayList<Objects>();
        }
        return this.lstObj;
    }

    /**
     * Gets the value of the lstObjRole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lstObjRole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLstObjRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ObjectRoles }
     * 
     * 
     */
    public List<ObjectRoles> getLstObjRole() {
        if (lstObjRole == null) {
            lstObjRole = new ArrayList<ObjectRoles>();
        }
        return this.lstObjRole;
    }

    /**
     * Gets the value of the lstUser property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lstUser property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLstUser().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Users }
     * 
     * 
     */
    public List<Users> getLstUser() {
        if (lstUser == null) {
            lstUser = new ArrayList<Users>();
        }
        return this.lstUser;
    }

    /**
     * Gets the value of the lstUserRole property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lstUserRole property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLstUserRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserRoles }
     * 
     * 
     */
    public List<UserRoles> getLstUserRole() {
        if (lstUserRole == null) {
            lstUserRole = new ArrayList<UserRoles>();
        }
        return this.lstUserRole;
    }

    /**
     * Gets the value of the values property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the values property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getValues() {
        if (values == null) {
            values = new ArrayList<Object>();
        }
        return this.values;
    }

}
