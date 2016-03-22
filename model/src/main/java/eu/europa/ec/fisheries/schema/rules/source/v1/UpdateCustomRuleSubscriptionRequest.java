
package eu.europa.ec.fisheries.schema.rules.source.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.UpdateSubscriptionType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{urn:source.rules.schema.fisheries.ec.europa.eu:v1}RulesBaseRequest">
 *       &lt;sequence>
 *         &lt;element name="subscription" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}UpdateSubscriptionType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "subscription"
})
@XmlRootElement(name = "UpdateCustomRuleSubscriptionRequest")
public class UpdateCustomRuleSubscriptionRequest
    extends RulesBaseRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected UpdateSubscriptionType subscription;

    /**
     * Gets the value of the subscription property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateSubscriptionType }
     *     
     */
    public UpdateSubscriptionType getSubscription() {
        return subscription;
    }

    /**
     * Sets the value of the subscription property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateSubscriptionType }
     *     
     */
    public void setSubscription(UpdateSubscriptionType value) {
        this.subscription = value;
    }

}
