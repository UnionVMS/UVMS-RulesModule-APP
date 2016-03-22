
package eu.europa.ec.fisheries.schema.rules.search.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlarmQuery complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AlarmQuery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pagination" type="{urn:search.rules.schema.fisheries.ec.europa.eu:v1}ListPagination"/>
 *         &lt;element name="alarmSearchCriteria" type="{urn:search.rules.schema.fisheries.ec.europa.eu:v1}AlarmListCriteria" maxOccurs="unbounded"/>
 *         &lt;element name="dynamic" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AlarmQuery", propOrder = {
    "pagination",
    "alarmSearchCriteria",
    "dynamic"
})
public class AlarmQuery
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected ListPagination pagination;
    @XmlElement(required = true)
    protected List<AlarmListCriteria> alarmSearchCriteria;
    protected boolean dynamic;

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
     * Gets the value of the alarmSearchCriteria property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alarmSearchCriteria property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlarmSearchCriteria().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AlarmListCriteria }
     * 
     * 
     */
    public List<AlarmListCriteria> getAlarmSearchCriteria() {
        if (alarmSearchCriteria == null) {
            alarmSearchCriteria = new ArrayList<AlarmListCriteria>();
        }
        return this.alarmSearchCriteria;
    }

    /**
     * Gets the value of the dynamic property.
     * 
     */
    public boolean isDynamic() {
        return dynamic;
    }

    /**
     * Sets the value of the dynamic property.
     * 
     */
    public void setDynamic(boolean value) {
        this.dynamic = value;
    }

}
