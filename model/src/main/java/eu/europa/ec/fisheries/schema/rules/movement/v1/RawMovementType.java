
package eu.europa.ec.fisheries.schema.rules.movement.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetId;
import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.MobileTerminalType;


/**
 * <p>Java class for RawMovementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RawMovementType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ackResponseMessageID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dateRecieved" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="pluginType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pluginName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="guid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="connectId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="assetId" type="{urn:asset.rules.schema.fisheries.ec.europa.eu:v1}AssetId"/>
 *         &lt;element name="comChannelType" type="{urn:movement.rules.schema.fisheries.ec.europa.eu:v1}MovementComChannelType"/>
 *         &lt;element name="mobileTerminal" type="{urn:mobileterminal.rules.schema.fisheries.ec.europa.eu:v1}MobileTerminalType"/>
 *         &lt;element name="position" type="{urn:movement.rules.schema.fisheries.ec.europa.eu:v1}MovementPoint"/>
 *         &lt;element name="positionTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="reportedSpeed" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="reportedCourse" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="movementType" type="{urn:movement.rules.schema.fisheries.ec.europa.eu:v1}MovementTypeType"/>
 *         &lt;element name="source" type="{urn:movement.rules.schema.fisheries.ec.europa.eu:v1}MovementSourceType"/>
 *         &lt;element name="activity" type="{urn:movement.rules.schema.fisheries.ec.europa.eu:v1}MovementActivityType"/>
 *         &lt;element name="assetName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="flagState" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="externalMarking" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tripNumber" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="internalReferenceNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RawMovementType", propOrder = {
    "ackResponseMessageID",
    "dateRecieved",
    "pluginType",
    "pluginName",
    "guid",
    "connectId",
    "assetId",
    "comChannelType",
    "mobileTerminal",
    "position",
    "positionTime",
    "status",
    "reportedSpeed",
    "reportedCourse",
    "movementType",
    "source",
    "activity",
    "assetName",
    "flagState",
    "externalMarking",
    "tripNumber",
    "internalReferenceNumber"
})
public class RawMovementType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String ackResponseMessageID;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateRecieved;
    @XmlElement(required = true)
    protected String pluginType;
    @XmlElement(required = true)
    protected String pluginName;
    @XmlElement(required = true)
    protected String guid;
    @XmlElement(required = true)
    protected String connectId;
    @XmlElement(required = true)
    protected AssetId assetId;
    @XmlElement(required = true)
    protected MovementComChannelType comChannelType;
    @XmlElement(required = true)
    protected MobileTerminalType mobileTerminal;
    @XmlElement(required = true)
    protected MovementPoint position;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar positionTime;
    @XmlElement(required = true)
    protected String status;
    @XmlElement(required = true, type = Double.class, nillable = true)
    protected Double reportedSpeed;
    @XmlElement(required = true, type = Double.class, nillable = true)
    protected Double reportedCourse;
    @XmlElement(required = true)
    protected MovementTypeType movementType;
    @XmlElement(required = true)
    protected MovementSourceType source;
    @XmlElement(required = true)
    protected MovementActivityType activity;
    @XmlElement(required = true)
    protected String assetName;
    @XmlElement(required = true)
    protected String flagState;
    @XmlElement(required = true)
    protected String externalMarking;
    @XmlElement(required = true, type = Double.class, nillable = true)
    protected Double tripNumber;
    @XmlElement(required = true)
    protected String internalReferenceNumber;

    /**
     * Gets the value of the ackResponseMessageID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAckResponseMessageID() {
        return ackResponseMessageID;
    }

    /**
     * Sets the value of the ackResponseMessageID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAckResponseMessageID(String value) {
        this.ackResponseMessageID = value;
    }

    /**
     * Gets the value of the dateRecieved property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateRecieved() {
        return dateRecieved;
    }

    /**
     * Sets the value of the dateRecieved property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateRecieved(XMLGregorianCalendar value) {
        this.dateRecieved = value;
    }

    /**
     * Gets the value of the pluginType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPluginType() {
        return pluginType;
    }

    /**
     * Sets the value of the pluginType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPluginType(String value) {
        this.pluginType = value;
    }

    /**
     * Gets the value of the pluginName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPluginName() {
        return pluginName;
    }

    /**
     * Sets the value of the pluginName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPluginName(String value) {
        this.pluginName = value;
    }

    /**
     * Gets the value of the guid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGuid() {
        return guid;
    }

    /**
     * Sets the value of the guid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGuid(String value) {
        this.guid = value;
    }

    /**
     * Gets the value of the connectId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectId() {
        return connectId;
    }

    /**
     * Sets the value of the connectId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectId(String value) {
        this.connectId = value;
    }

    /**
     * Gets the value of the assetId property.
     * 
     * @return
     *     possible object is
     *     {@link AssetId }
     *     
     */
    public AssetId getAssetId() {
        return assetId;
    }

    /**
     * Sets the value of the assetId property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssetId }
     *     
     */
    public void setAssetId(AssetId value) {
        this.assetId = value;
    }

    /**
     * Gets the value of the comChannelType property.
     * 
     * @return
     *     possible object is
     *     {@link MovementComChannelType }
     *     
     */
    public MovementComChannelType getComChannelType() {
        return comChannelType;
    }

    /**
     * Sets the value of the comChannelType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MovementComChannelType }
     *     
     */
    public void setComChannelType(MovementComChannelType value) {
        this.comChannelType = value;
    }

    /**
     * Gets the value of the mobileTerminal property.
     * 
     * @return
     *     possible object is
     *     {@link MobileTerminalType }
     *     
     */
    public MobileTerminalType getMobileTerminal() {
        return mobileTerminal;
    }

    /**
     * Sets the value of the mobileTerminal property.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileTerminalType }
     *     
     */
    public void setMobileTerminal(MobileTerminalType value) {
        this.mobileTerminal = value;
    }

    /**
     * Gets the value of the position property.
     * 
     * @return
     *     possible object is
     *     {@link MovementPoint }
     *     
     */
    public MovementPoint getPosition() {
        return position;
    }

    /**
     * Sets the value of the position property.
     * 
     * @param value
     *     allowed object is
     *     {@link MovementPoint }
     *     
     */
    public void setPosition(MovementPoint value) {
        this.position = value;
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

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the reportedSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getReportedSpeed() {
        return reportedSpeed;
    }

    /**
     * Sets the value of the reportedSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setReportedSpeed(Double value) {
        this.reportedSpeed = value;
    }

    /**
     * Gets the value of the reportedCourse property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getReportedCourse() {
        return reportedCourse;
    }

    /**
     * Sets the value of the reportedCourse property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setReportedCourse(Double value) {
        this.reportedCourse = value;
    }

    /**
     * Gets the value of the movementType property.
     * 
     * @return
     *     possible object is
     *     {@link MovementTypeType }
     *     
     */
    public MovementTypeType getMovementType() {
        return movementType;
    }

    /**
     * Sets the value of the movementType property.
     * 
     * @param value
     *     allowed object is
     *     {@link MovementTypeType }
     *     
     */
    public void setMovementType(MovementTypeType value) {
        this.movementType = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link MovementSourceType }
     *     
     */
    public MovementSourceType getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link MovementSourceType }
     *     
     */
    public void setSource(MovementSourceType value) {
        this.source = value;
    }

    /**
     * Gets the value of the activity property.
     * 
     * @return
     *     possible object is
     *     {@link MovementActivityType }
     *     
     */
    public MovementActivityType getActivity() {
        return activity;
    }

    /**
     * Sets the value of the activity property.
     * 
     * @param value
     *     allowed object is
     *     {@link MovementActivityType }
     *     
     */
    public void setActivity(MovementActivityType value) {
        this.activity = value;
    }

    /**
     * Gets the value of the assetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * Sets the value of the assetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetName(String value) {
        this.assetName = value;
    }

    /**
     * Gets the value of the flagState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlagState() {
        return flagState;
    }

    /**
     * Sets the value of the flagState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlagState(String value) {
        this.flagState = value;
    }

    /**
     * Gets the value of the externalMarking property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalMarking() {
        return externalMarking;
    }

    /**
     * Sets the value of the externalMarking property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalMarking(String value) {
        this.externalMarking = value;
    }

    /**
     * Gets the value of the tripNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getTripNumber() {
        return tripNumber;
    }

    /**
     * Sets the value of the tripNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setTripNumber(Double value) {
        this.tripNumber = value;
    }

    /**
     * Gets the value of the internalReferenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternalReferenceNumber() {
        return internalReferenceNumber;
    }

    /**
     * Sets the value of the internalReferenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternalReferenceNumber(String value) {
        this.internalReferenceNumber = value;
    }

}
