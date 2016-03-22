
package eu.europa.ec.fisheries.schema.rules.source.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleQuery;


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
 *         &lt;element name="query" type="{urn:search.rules.schema.fisheries.ec.europa.eu:v1}CustomRuleQuery"/>
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
    "query"
})
@XmlRootElement(name = "GetCustomRuleListByQueryRequest")
public class GetCustomRuleListByQueryRequest
    extends RulesBaseRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected CustomRuleQuery query;

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link CustomRuleQuery }
     *     
     */
    public CustomRuleQuery getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomRuleQuery }
     *     
     */
    public void setQuery(CustomRuleQuery value) {
        this.query = value;
    }

}
