
package eu.europa.ec.fisheries.schema.rules.customrule.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpdateSubscriptionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateSubscriptionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ruleGuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="operation" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}SubscritionOperationType"/>
 *         &lt;element name="subscription" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}SubscriptionType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateSubscriptionType", propOrder = {
    "ruleGuid",
    "operation",
    "subscription"
})
public class UpdateSubscriptionType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String ruleGuid;
    @XmlElement(required = true)
    protected SubscritionOperationType operation;
    @XmlElement(required = true)
    protected SubscriptionType subscription;

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
     * Gets the value of the operation property.
     * 
     * @return
     *     possible object is
     *     {@link SubscritionOperationType }
     *     
     */
    public SubscritionOperationType getOperation() {
        return operation;
    }

    /**
     * Sets the value of the operation property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubscritionOperationType }
     *     
     */
    public void setOperation(SubscritionOperationType value) {
        this.operation = value;
    }

    /**
     * Gets the value of the subscription property.
     * 
     * @return
     *     possible object is
     *     {@link SubscriptionType }
     *     
     */
    public SubscriptionType getSubscription() {
        return subscription;
    }

    /**
     * Sets the value of the subscription property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubscriptionType }
     *     
     */
    public void setSubscription(SubscriptionType value) {
        this.subscription = value;
    }

}
