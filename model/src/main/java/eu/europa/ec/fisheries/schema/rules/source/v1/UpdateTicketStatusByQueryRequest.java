
package eu.europa.ec.fisheries.schema.rules.source.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;


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
 *         &lt;element name="loggedInUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="query" type="{urn:search.rules.schema.fisheries.ec.europa.eu:v1}TicketQuery"/>
 *         &lt;element name="status" type="{urn:ticket.rules.schema.fisheries.ec.europa.eu:v1}TicketStatusType"/>
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
    "loggedInUser",
    "query",
    "status"
})
@XmlRootElement(name = "UpdateTicketStatusByQueryRequest")
public class UpdateTicketStatusByQueryRequest
    extends RulesBaseRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String loggedInUser;
    @XmlElement(required = true)
    protected TicketQuery query;
    @XmlElement(required = true)
    protected TicketStatusType status;

    /**
     * Gets the value of the loggedInUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Sets the value of the loggedInUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoggedInUser(String value) {
        this.loggedInUser = value;
    }

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link TicketQuery }
     *     
     */
    public TicketQuery getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link TicketQuery }
     *     
     */
    public void setQuery(TicketQuery value) {
        this.query = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link TicketStatusType }
     *     
     */
    public TicketStatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link TicketStatusType }
     *     
     */
    public void setStatus(TicketStatusType value) {
        this.status = value;
    }

}
