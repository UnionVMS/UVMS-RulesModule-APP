
package eu.europa.ec.fisheries.schema.rules.source.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="assetGuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ruleGuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "assetGuid",
    "ruleGuid"
})
@XmlRootElement(name = "GetTicketByAssetAndRuleRequest")
public class GetTicketByAssetAndRuleRequest
    extends RulesBaseRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String assetGuid;
    @XmlElement(required = true)
    protected String ruleGuid;

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

}
