
package eu.europa.ec.fisheries.schema.rules.movement.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MovementMetaData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MovementMetaData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="closestPort" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="distanceToClosestPort" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="closestCountryCoast" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="distanceToCountryCoast" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="areas" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MovementMetaData", propOrder = {
    "closestPort",
    "distanceToClosestPort",
    "closestCountryCoast",
    "distanceToCountryCoast",
    "areas"
})
public class MovementMetaData
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String closestPort;
    @XmlElement(required = true, type = Double.class, nillable = true)
    protected Double distanceToClosestPort;
    @XmlElement(required = true)
    protected String closestCountryCoast;
    @XmlElement(required = true, type = Double.class, nillable = true)
    protected Double distanceToCountryCoast;
    @XmlElement(required = true)
    protected List<String> areas;

    /**
     * Gets the value of the closestPort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClosestPort() {
        return closestPort;
    }

    /**
     * Sets the value of the closestPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClosestPort(String value) {
        this.closestPort = value;
    }

    /**
     * Gets the value of the distanceToClosestPort property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDistanceToClosestPort() {
        return distanceToClosestPort;
    }

    /**
     * Sets the value of the distanceToClosestPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDistanceToClosestPort(Double value) {
        this.distanceToClosestPort = value;
    }

    /**
     * Gets the value of the closestCountryCoast property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClosestCountryCoast() {
        return closestCountryCoast;
    }

    /**
     * Sets the value of the closestCountryCoast property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClosestCountryCoast(String value) {
        this.closestCountryCoast = value;
    }

    /**
     * Gets the value of the distanceToCountryCoast property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getDistanceToCountryCoast() {
        return distanceToCountryCoast;
    }

    /**
     * Sets the value of the distanceToCountryCoast property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setDistanceToCountryCoast(Double value) {
        this.distanceToCountryCoast = value;
    }

    /**
     * Gets the value of the areas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the areas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAreas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAreas() {
        if (areas == null) {
            areas = new ArrayList<String>();
        }
        return this.areas;
    }

}
