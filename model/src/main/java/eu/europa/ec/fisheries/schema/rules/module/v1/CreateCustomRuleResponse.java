
package eu.europa.ec.fisheries.schema.rules.module.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customRule" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}CustomRuleType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "customRule"
})
@XmlRootElement(name = "CreateCustomRuleResponse")
public class CreateCustomRuleResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected CustomRuleType customRule;

    /**
     * Gets the value of the customRule property.
     * 
     * @return
     *     possible object is
     *     {@link CustomRuleType }
     *     
     */
    public CustomRuleType getCustomRule() {
        return customRule;
    }

    /**
     * Sets the value of the customRule property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomRuleType }
     *     
     */
    public void setCustomRule(CustomRuleType value) {
        this.customRule = value;
    }

}
