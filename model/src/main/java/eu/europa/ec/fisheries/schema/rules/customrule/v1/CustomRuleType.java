
package eu.europa.ec.fisheries.schema.rules.customrule.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomRuleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomRuleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="guid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="availability" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}AvailabilityType"/>
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="archived" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="timeIntervals" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}CustomRuleIntervalType" maxOccurs="unbounded"/>
 *         &lt;element name="definitions" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}CustomRuleSegmentType" maxOccurs="unbounded"/>
 *         &lt;element name="actions" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}CustomRuleActionType" maxOccurs="unbounded"/>
 *         &lt;element name="subscriptions" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}SubscriptionType" maxOccurs="unbounded"/>
 *         &lt;element name="lastTriggered" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="organisation" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "CustomRuleType", propOrder = {
    "guid",
    "name",
    "availability",
    "active",
    "archived",
    "description",
    "timeIntervals",
    "definitions",
    "actions",
    "subscriptions",
    "lastTriggered",
    "organisation",
    "updated",
    "updatedBy"
})
public class CustomRuleType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String guid;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected AvailabilityType availability;
    protected boolean active;
    protected boolean archived;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected List<CustomRuleIntervalType> timeIntervals;
    @XmlElement(required = true)
    protected List<CustomRuleSegmentType> definitions;
    @XmlElement(required = true)
    protected List<CustomRuleActionType> actions;
    @XmlElement(required = true)
    protected List<SubscriptionType> subscriptions;
    @XmlElement(required = true)
    protected String lastTriggered;
    @XmlElement(required = true)
    protected String organisation;
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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the availability property.
     * 
     * @return
     *     possible object is
     *     {@link AvailabilityType }
     *     
     */
    public AvailabilityType getAvailability() {
        return availability;
    }

    /**
     * Sets the value of the availability property.
     * 
     * @param value
     *     allowed object is
     *     {@link AvailabilityType }
     *     
     */
    public void setAvailability(AvailabilityType value) {
        this.availability = value;
    }

    /**
     * Gets the value of the active property.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the archived property.
     * 
     */
    public boolean isArchived() {
        return archived;
    }

    /**
     * Sets the value of the archived property.
     * 
     */
    public void setArchived(boolean value) {
        this.archived = value;
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
     * Gets the value of the timeIntervals property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timeIntervals property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimeIntervals().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomRuleIntervalType }
     * 
     * 
     */
    public List<CustomRuleIntervalType> getTimeIntervals() {
        if (timeIntervals == null) {
            timeIntervals = new ArrayList<CustomRuleIntervalType>();
        }
        return this.timeIntervals;
    }

    /**
     * Gets the value of the definitions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the definitions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDefinitions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomRuleSegmentType }
     * 
     * 
     */
    public List<CustomRuleSegmentType> getDefinitions() {
        if (definitions == null) {
            definitions = new ArrayList<CustomRuleSegmentType>();
        }
        return this.definitions;
    }

    /**
     * Gets the value of the actions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomRuleActionType }
     * 
     * 
     */
    public List<CustomRuleActionType> getActions() {
        if (actions == null) {
            actions = new ArrayList<CustomRuleActionType>();
        }
        return this.actions;
    }

    /**
     * Gets the value of the subscriptions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subscriptions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubscriptions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubscriptionType }
     * 
     * 
     */
    public List<SubscriptionType> getSubscriptions() {
        if (subscriptions == null) {
            subscriptions = new ArrayList<SubscriptionType>();
        }
        return this.subscriptions;
    }

    /**
     * Gets the value of the lastTriggered property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastTriggered() {
        return lastTriggered;
    }

    /**
     * Sets the value of the lastTriggered property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastTriggered(String value) {
        this.lastTriggered = value;
    }

    /**
     * Gets the value of the organisation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganisation() {
        return organisation;
    }

    /**
     * Sets the value of the organisation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganisation(String value) {
        this.organisation = value;
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
