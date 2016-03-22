
package eu.europa.ec.fisheries.schema.rules.ticket.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TicketType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TicketType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="guid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="status" type="{urn:ticket.rules.schema.fisheries.ec.europa.eu:v1}TicketStatusType"/>
 *         &lt;element name="openDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="assetGuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mobileTerminalGuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="channelGuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ruleGuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ruleName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="movementGuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="recipient" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "TicketType", propOrder = {
    "guid",
    "status",
    "openDate",
    "assetGuid",
    "mobileTerminalGuid",
    "channelGuid",
    "ruleGuid",
    "ruleName",
    "movementGuid",
    "recipient",
    "updated",
    "updatedBy"
})
public class TicketType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String guid;
    @XmlElement(required = true)
    protected TicketStatusType status;
    @XmlElement(required = true)
    protected String openDate;
    @XmlElement(required = true)
    protected String assetGuid;
    @XmlElement(required = true)
    protected String mobileTerminalGuid;
    @XmlElement(required = true)
    protected String channelGuid;
    @XmlElement(required = true)
    protected String ruleGuid;
    @XmlElement(required = true)
    protected String ruleName;
    @XmlElement(required = true)
    protected String movementGuid;
    @XmlElement(required = true)
    protected String recipient;
    @XmlElement(required = true)
    protected String updated;
    @XmlElement(required = true)
    protected String updatedBy;

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
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link TicketStatusType }
     *     
     */
    public TicketStatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link TicketStatusType }
     *     
     */
    public void setStatus(TicketStatusType value) {
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
     * Gets the value of the mobileTerminalGuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileTerminalGuid() {
        return mobileTerminalGuid;
    }

    /**
     * Sets the value of the mobileTerminalGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileTerminalGuid(String value) {
        this.mobileTerminalGuid = value;
    }

    /**
     * Gets the value of the channelGuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelGuid() {
        return channelGuid;
    }

    /**
     * Sets the value of the channelGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelGuid(String value) {
        this.channelGuid = value;
    }

    /**
     * Gets the value of the ruleGuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleGuid() {
        return ruleGuid;
    }

    /**
     * Sets the value of the ruleGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleGuid(String value) {
        this.ruleGuid = value;
    }

    /**
     * Gets the value of the ruleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * Sets the value of the ruleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleName(String value) {
        this.ruleName = value;
    }

    /**
     * Gets the value of the movementGuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMovementGuid() {
        return movementGuid;
    }

    /**
     * Sets the value of the movementGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMovementGuid(String value) {
        this.movementGuid = value;
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
