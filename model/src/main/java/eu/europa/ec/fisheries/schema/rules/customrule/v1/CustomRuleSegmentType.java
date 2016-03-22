
package eu.europa.ec.fisheries.schema.rules.customrule.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomRuleSegmentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomRuleSegmentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="startOperator" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}StartOperatorType"/>
 *         &lt;element name="criteria" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}CriteriaType"/>
 *         &lt;element name="subCriteria" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}SubCriteriaType"/>
 *         &lt;element name="condition" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}ConditionType"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="endOperator" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}EndOperatorType"/>
 *         &lt;element name="logicBoolOperator" type="{urn:customrule.rules.schema.fisheries.ec.europa.eu:v1}LogicOperatorType"/>
 *         &lt;element name="order" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomRuleSegmentType", propOrder = {
    "startOperator",
    "criteria",
    "subCriteria",
    "condition",
    "value",
    "endOperator",
    "logicBoolOperator",
    "order"
})
public class CustomRuleSegmentType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected String startOperator;
    @XmlElement(required = true)
    protected CriteriaType criteria;
    @XmlElement(required = true)
    protected SubCriteriaType subCriteria;
    @XmlElement(required = true)
    protected ConditionType condition;
    @XmlElement(required = true)
    protected String value;
    @XmlElement(required = true)
    protected String endOperator;
    @XmlElement(required = true)
    protected LogicOperatorType logicBoolOperator;
    @XmlElement(required = true)
    protected String order;

    /**
     * Gets the value of the startOperator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartOperator() {
        return startOperator;
    }

    /**
     * Sets the value of the startOperator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartOperator(String value) {
        this.startOperator = value;
    }

    /**
     * Gets the value of the criteria property.
     * 
     * @return
     *     possible object is
     *     {@link CriteriaType }
     *     
     */
    public CriteriaType getCriteria() {
        return criteria;
    }

    /**
     * Sets the value of the criteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link CriteriaType }
     *     
     */
    public void setCriteria(CriteriaType value) {
        this.criteria = value;
    }

    /**
     * Gets the value of the subCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link SubCriteriaType }
     *     
     */
    public SubCriteriaType getSubCriteria() {
        return subCriteria;
    }

    /**
     * Sets the value of the subCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubCriteriaType }
     *     
     */
    public void setSubCriteria(SubCriteriaType value) {
        this.subCriteria = value;
    }

    /**
     * Gets the value of the condition property.
     * 
     * @return
     *     possible object is
     *     {@link ConditionType }
     *     
     */
    public ConditionType getCondition() {
        return condition;
    }

    /**
     * Sets the value of the condition property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConditionType }
     *     
     */
    public void setCondition(ConditionType value) {
        this.condition = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the endOperator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndOperator() {
        return endOperator;
    }

    /**
     * Sets the value of the endOperator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndOperator(String value) {
        this.endOperator = value;
    }

    /**
     * Gets the value of the logicBoolOperator property.
     * 
     * @return
     *     possible object is
     *     {@link LogicOperatorType }
     *     
     */
    public LogicOperatorType getLogicBoolOperator() {
        return logicBoolOperator;
    }

    /**
     * Sets the value of the logicBoolOperator property.
     * 
     * @param value
     *     allowed object is
     *     {@link LogicOperatorType }
     *     
     */
    public void setLogicBoolOperator(LogicOperatorType value) {
        this.logicBoolOperator = value;
    }

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrder(String value) {
        this.order = value;
    }

}
