
package eu.europa.ec.fisheries.schema.rules.module.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{urn:module.rules.schema.fisheries.ec.europa.eu:v1}RulesBaseRequest">
 *       &lt;sequence>
 *         &lt;element name="movementGuids" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
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
    "movementGuids"
})
@XmlRootElement(name = "GetTicketsAndRulesByMovementsRequest")
public class GetTicketsAndRulesByMovementsRequest
    extends RulesBaseRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected List<String> movementGuids;

    /**
     * Gets the value of the movementGuids property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the movementGuids property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMovementGuids().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMovementGuids() {
        if (movementGuids == null) {
            movementGuids = new ArrayList<String>();
        }
        return this.movementGuids;
    }

}
