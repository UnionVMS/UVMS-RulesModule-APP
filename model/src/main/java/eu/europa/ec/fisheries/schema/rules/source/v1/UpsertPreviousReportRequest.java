
package eu.europa.ec.fisheries.schema.rules.source.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;


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
 *         &lt;element name="previousReport" type="{urn:previous.rules.schema.fisheries.ec.europa.eu:v1}PreviousReportType"/>
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
    "previousReport"
})
@XmlRootElement(name = "UpsertPreviousReportRequest")
public class UpsertPreviousReportRequest
    extends RulesBaseRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected PreviousReportType previousReport;

    /**
     * Gets the value of the previousReport property.
     * 
     * @return
     *     possible object is
     *     {@link PreviousReportType }
     *     
     */
    public PreviousReportType getPreviousReport() {
        return previousReport;
    }

    /**
     * Sets the value of the previousReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link PreviousReportType }
     *     
     */
    public void setPreviousReport(PreviousReportType value) {
        this.previousReport = value;
    }

}
