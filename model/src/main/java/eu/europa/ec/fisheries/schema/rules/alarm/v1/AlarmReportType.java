
package eu.europa.ec.fisheries.schema.rules.alarm.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;


/**
 * <p>Java class for AlarmReportType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AlarmReportType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pluginType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="guid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="assetGuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="status" type="{urn:alarm.rules.schema.fisheries.ec.europa.eu:v1}AlarmStatusType"/>
 *         &lt;element name="openDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="recipient" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="alarmItem" type="{urn:alarm.rules.schema.fisheries.ec.europa.eu:v1}AlarmItemType" maxOccurs="unbounded"/>
 *         &lt;element name="rawMovement" type="{urn:movement.rules.schema.fisheries.ec.europa.eu:v1}RawMovementType"/>
 *         &lt;element name="inactivatePosition" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="updated" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="updatedBy" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AlarmReportType", propOrder = {
    "pluginType",
    "guid",
    "assetGuid",
    "status",
    "openDate",
    "recipient",
    "alarmItem",
    "rawMovement",
    "inactivatePosition",
    "updated",
    "updatedBy"
})
public class AlarmReportType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String pluginType;
    @XmlElement(required = true)
    protected String guid;
    @XmlElement(required = true)
    protected String assetGuid;
    @XmlElement(required = true)
    protected AlarmStatusType status;
    @XmlElement(required = true)
    protected String openDate;
    @XmlElement(required = true)
    protected String recipient;
    @XmlElement(required = true)
    protected List<AlarmItemType> alarmItem;
    @XmlElement(required = true)
    protected RawMovementType rawMovement;
    protected boolean inactivatePosition;
    @XmlElement(required = true)
    protected String updated;
    @XmlElement(required = true)
    protected String updatedBy;

    /**
     * Gets the value of the pluginType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPluginType() {
        return pluginType;
    }

    /**
     * Sets the value of the pluginType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPluginType(String value) {
        this.pluginType = value;
    }

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
     * Gets the value of the assetGuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetGuid() {
        return assetGuid;
    }

    /**
     * Sets the value of the assetGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetGuid(String value) {
        this.assetGuid = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link AlarmStatusType }
     *     
     */
    public AlarmStatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlarmStatusType }
     *     
     */
    public void setStatus(AlarmStatusType value) {
        this.status = value;
    }

    /**
     * Gets the value of the openDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenDate() {
        return openDate;
    }

    /**
     * Sets the value of the openDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenDate(String value) {
        this.openDate = value;
    }

    /**
     * Gets the value of the recipient property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Sets the value of the recipient property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipient(String value) {
        this.recipient = value;
    }

    /**
     * Gets the value of the alarmItem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alarmItem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlarmItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AlarmItemType }
     * 
     * 
     */
    public List<AlarmItemType> getAlarmItem() {
        if (alarmItem == null) {
            alarmItem = new ArrayList<AlarmItemType>();
        }
        return this.alarmItem;
    }

    /**
     * Gets the value of the rawMovement property.
     * 
     * @return
     *     possible object is
     *     {@link RawMovementType }
     *     
     */
    public RawMovementType getRawMovement() {
        return rawMovement;
    }

    /**
     * Sets the value of the rawMovement property.
     * 
     * @param value
     *     allowed object is
     *     {@link RawMovementType }
     *     
     */
    public void setRawMovement(RawMovementType value) {
        this.rawMovement = value;
    }

    /**
     * Gets the value of the inactivatePosition property.
     * 
     */
    public boolean isInactivatePosition() {
        return inactivatePosition;
    }

    /**
     * Sets the value of the inactivatePosition property.
     * 
     */
    public void setInactivatePosition(boolean value) {
        this.inactivatePosition = value;
    }

    /**
     * Gets the value of the updated property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdated() {
        return updated;
    }

    /**
     * Sets the value of the updated property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdated(String value) {
        this.updated = value;
    }

    /**
     * Gets the value of the updatedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Sets the value of the updatedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUpdatedBy(String value) {
        this.updatedBy = value;
    }

}
