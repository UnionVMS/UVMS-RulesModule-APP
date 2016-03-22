
package eu.europa.ec.fisheries.schema.rules.movement.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MovementActivityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MovementActivityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="messageType" type="{urn:movement.rules.schema.fisheries.ec.europa.eu:v1}MovementActivityTypeType"/>
 *         &lt;element name="messageId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="callback" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MovementActivityType", propOrder = {
    "messageType",
    "messageId",
    "callback"
})
public class MovementActivityType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected MovementActivityTypeType messageType;
    @XmlElement(required = true)
    protected String messageId;
    @XmlElement(required = true)
    protected String callback;

    /**
     * Gets the value of the messageType property.
     * 
     * @return
     *     possible object is
     *     {@link MovementActivityTypeType }
     *     
     */
    public MovementActivityTypeType getMessageType() {
        return messageType;
    }

    /**
     * Sets the value of the messageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MovementActivityTypeType }
     *     
     */
    public void setMessageType(MovementActivityTypeType value) {
        this.messageType = value;
    }

    /**
     * Gets the value of the messageId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageId(String value) {
        this.messageId = value;
    }

    /**
     * Gets the value of the callback property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallback() {
        return callback;
    }

    /**
     * Sets the value of the callback property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallback(String value) {
        this.callback = value;
    }

}
