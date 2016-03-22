
package eu.europa.ec.fisheries.schema.rules.source.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="previousReports" type="{urn:previous.rules.schema.fisheries.ec.europa.eu:v1}PreviousReportType" maxOccurs="unbounded" minOccurs="0"/>
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
    "previousReports"
})
@XmlRootElement(name = "GetPreviousReportsResponse")
public class GetPreviousReportsResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected List<PreviousReportType> previousReports;

    /**
     * Gets the value of the previousReports property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the previousReports property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPreviousReports().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PreviousReportType }
     * 
     * 
     */
    public List<PreviousReportType> getPreviousReports() {
        if (previousReports == null) {
            previousReports = new ArrayList<PreviousReportType>();
        }
        return this.previousReports;
    }

}
