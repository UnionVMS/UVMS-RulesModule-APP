
package eu.europa.ec.fisheries.schema.rules.mobileterminal.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MobileTerminalType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MobileTerminalType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="guid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="connectId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mobileTerminalIdList" type="{urn:mobileterminal.rules.schema.fisheries.ec.europa.eu:v1}IdList" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MobileTerminalType", propOrder = {
    "guid",
    "connectId",
    "mobileTerminalIdList"
})
public class MobileTerminalType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String guid;
    @XmlElement(required = true)
    protected String connectId;
    protected List<IdList> mobileTerminalIdList;

    /**
     * Gets the value of the guid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuid(String value) {
        this.guid = value;
    }

    /**
     * Gets the value of the connectId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectId() {
        return connectId;
    }

    /**
     * Sets the value of the connectId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectId(String value) {
        this.connectId = value;
    }

    /**
     * Gets the value of the mobileTerminalIdList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mobileTerminalIdList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMobileTerminalIdList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IdList }
     * 
     * 
     */
    public List<IdList> getMobileTerminalIdList() {
        if (mobileTerminalIdList == null) {
            mobileTerminalIdList = new ArrayList<IdList>();
        }
        return this.mobileTerminalIdList;
    }

}
