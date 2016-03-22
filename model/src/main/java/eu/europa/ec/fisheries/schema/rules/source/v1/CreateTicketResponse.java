
package eu.europa.ec.fisheries.schema.rules.source.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;


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
 *         &lt;element name="ack" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ticket" type="{urn:ticket.rules.schema.fisheries.ec.europa.eu:v1}TicketType"/>
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
    "ack",
    "ticket"
})
@XmlRootElement(name = "CreateTicketResponse")
public class CreateTicketResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String ack;
    @XmlElement(required = true)
    protected TicketType ticket;

    /**
     * Gets the value of the ack property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAck() {
        return ack;
    }

    /**
     * Sets the value of the ack property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAck(String value) {
        this.ack = value;
    }

    /**
     * Gets the value of the ticket property.
     * 
     * @return
     *     possible object is
     *     {@link TicketType }
     *     
     */
    public TicketType getTicket() {
        return ticket;
    }

    /**
     * Sets the value of the ticket property.
     * 
     * @param value
     *     allowed object is
     *     {@link TicketType }
     *     
     */
    public void setTicket(TicketType value) {
        this.ticket = value;
    }

}
