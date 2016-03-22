
package eu.europa.ec.fisheries.schema.rules.module.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{urn:module.rules.schema.fisheries.ec.europa.eu:v1}RulesBaseRequest">
 *       &lt;sequence>
 *         &lt;element name="type" type="{urn:exchange.rules.schema.fisheries.ec.europa.eu:v1}PluginType"/>
 *         &lt;element name="request" type="{urn:movement.rules.schema.fisheries.ec.europa.eu:v1}RawMovementType"/>
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
    "type",
    "request"
})
@XmlRootElement(name = "SetMovementReportRequest")
public class SetMovementReportRequest
    extends RulesBaseRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected PluginType type;
    @XmlElement(required = true)
    protected RawMovementType request;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link PluginType }
     *     
     */
    public PluginType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link PluginType }
     *     
     */
    public void setType(PluginType value) {
        this.type = value;
    }

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link RawMovementType }
     *     
     */
    public RawMovementType getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link RawMovementType }
     *     
     */
    public void setRequest(RawMovementType value) {
        this.request = value;
    }

}
