
package eu.europa.ec.fisheries.schema.rules.previous.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for PreviousReportType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PreviousReportType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="movementGuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="assetGuid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="positionTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PreviousReportType", propOrder = {
    "movementGuid",
    "assetGuid",
    "positionTime"
})
public class PreviousReportType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String movementGuid;
    @XmlElement(required = true)
    protected String assetGuid;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar positionTime;

    /**
     * Gets the value of the movementGuid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMovementGuid() {
        return movementGuid;
    }

    /**
     * Sets the value of the movementGuid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMovementGuid(String value) {
        this.movementGuid = value;
    }

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
     * Gets the value of the positionTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPositionTime() {
        return positionTime;
    }

    /**
     * Sets the value of the positionTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPositionTime(XMLGregorianCalendar value) {
        this.positionTime = value;
    }

}
