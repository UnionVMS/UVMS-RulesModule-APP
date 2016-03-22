
package eu.europa.ec.fisheries.schema.rules.ticketrule.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;


/**
 * <p>Java class for TicketAndRuleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TicketAndRuleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ticket" type="{urn:ticket.rules.schema.fisheries.ec.europa.eu:v1}TicketType"/>
 *         &lt;element name="rule" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}CustomRuleType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TicketAndRuleType", propOrder = {
    "ticket",
    "rule"
})
public class TicketAndRuleType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected TicketType ticket;
    @XmlElement(required = true)
    protected CustomRuleType rule;

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

    /**
     * Gets the value of the rule property.
     * 
     * @return
     *     possible object is
     *     {@link CustomRuleType }
     *     
     */
    public CustomRuleType getRule() {
        return rule;
    }

    /**
     * Sets the value of the rule property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomRuleType }
     *     
     */
    public void setRule(CustomRuleType value) {
        this.rule = value;
    }

}
