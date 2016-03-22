
package eu.europa.ec.fisheries.schema.rules.alarm.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlarmItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AlarmItemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="guid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ruleGuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ruleName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AlarmItemType", propOrder = {
    "guid",
    "ruleGuid",
    "ruleName"
})
public class AlarmItemType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String guid;
    @XmlElement(required = true)
    protected String ruleGuid;
    @XmlElement(required = true)
    protected String ruleName;

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

}
