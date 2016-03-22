
package eu.europa.ec.fisheries.schema.rules.search.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TicketQuery complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TicketQuery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pagination" type="{urn:search.rules.schema.fisheries.ec.europa.eu:v1}ListPagination"/>
 *         &lt;element name="ticketSearchCriteria" type="{urn:search.rules.schema.fisheries.ec.europa.eu:v1}TicketListCriteria" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TicketQuery", propOrder = {
    "pagination",
    "ticketSearchCriteria"
})
public class TicketQuery
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected ListPagination pagination;
    @XmlElement(required = true)
    protected List<TicketListCriteria> ticketSearchCriteria;

    /**
     * Gets the value of the pagination property.
     * 
     * @return
     *     possible object is
     *     {@link ListPagination }
     *     
     */
    public ListPagination getPagination() {
        return pagination;
    }

    /**
     * Sets the value of the pagination property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListPagination }
     *     
     */
    public void setPagination(ListPagination value) {
        this.pagination = value;
    }

    /**
     * Gets the value of the ticketSearchCriteria property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ticketSearchCriteria property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTicketSearchCriteria().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TicketListCriteria }
     * 
     * 
     */
    public List<TicketListCriteria> getTicketSearchCriteria() {
        if (ticketSearchCriteria == null) {
            ticketSearchCriteria = new ArrayList<TicketListCriteria>();
        }
        return this.ticketSearchCriteria;
    }

}
