
package eu.europa.ec.fisheries.schema.rules.source.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SanityRuleType;


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
 *         &lt;element name="sanityRules" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}SanityRuleType" maxOccurs="unbounded" minOccurs="0"/>
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
    "sanityRules"
})
@XmlRootElement(name = "GetSanityRulesResponse")
public class GetSanityRulesResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected List<SanityRuleType> sanityRules;

    /**
     * Gets the value of the sanityRules property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sanityRules property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSanityRules().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SanityRuleType }
     * 
     * 
     */
    public List<SanityRuleType> getSanityRules() {
        if (sanityRules == null) {
            sanityRules = new ArrayList<SanityRuleType>();
        }
        return this.sanityRules;
    }

}
