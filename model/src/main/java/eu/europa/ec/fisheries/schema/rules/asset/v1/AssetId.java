
package eu.europa.ec.fisheries.schema.rules.asset.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AssetId complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AssetId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="assetType" type="{urn:asset.rules.schema.fisheries.ec.europa.eu:v1}AssetType"/>
 *         &lt;element name="assetIdList" type="{urn:asset.rules.schema.fisheries.ec.europa.eu:v1}AssetIdList" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssetId", propOrder = {
    "assetType",
    "assetIdList"
})
public class AssetId
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected AssetType assetType;
    @XmlElement(required = true)
    protected List<AssetIdList> assetIdList;

    /**
     * Gets the value of the assetType property.
     * 
     * @return
     *     possible object is
     *     {@link AssetType }
     *     
     */
    public AssetType getAssetType() {
        return assetType;
    }

    /**
     * Sets the value of the assetType property.
     * 
     * @param value
     *     allowed object is
     *     {@link AssetType }
     *     
     */
    public void setAssetType(AssetType value) {
        this.assetType = value;
    }

    /**
     * Gets the value of the assetIdList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assetIdList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssetIdList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssetIdList }
     * 
     * 
     */
    public List<AssetIdList> getAssetIdList() {
        if (assetIdList == null) {
            assetIdList = new ArrayList<AssetIdList>();
        }
        return this.assetIdList;
    }

}
