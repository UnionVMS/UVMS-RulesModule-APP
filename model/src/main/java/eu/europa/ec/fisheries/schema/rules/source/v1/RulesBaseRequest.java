
package eu.europa.ec.fisheries.schema.rules.source.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RulesBaseRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RulesBaseRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="method" type="{urn:source.rules.schema.fisheries.ec.europa.eu:v1}RulesDataSourceMethod"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RulesBaseRequest", propOrder = {
    "method"
})
@XmlSeeAlso({
    GetTicketByAssetAndRuleRequest.class,
    GetPreviousReportsRequest.class,
    CreateAlarmReportRequest.class,
    CreateTicketRequest.class,
    DeleteCustomRuleRequest.class,
    GetRunnableCustomRulesRequest.class,
    SetAlarmStatusRequest.class,
    GetPreviousReportByAssetGuidRequest.class,
    GetAlarmReportByAssetAndRuleRequest.class,
    UpdateCustomRuleSubscriptionRequest.class,
    GetTicketRequest.class,
    UpdateCustomRuleRequest.class,
    GetTicketListByMovementsRequest.class,
    GetAlarmRequest.class,
    UpsertPreviousReportRequest.class,
    GetNumberOfAssetsNotSendingRequest.class,
    GetCustomRuleRequest.class,
    GetNumberOfOpenAlarmsRequest.class,
    GetCustomRuleListByQueryRequest.class,
    GetAlarmListByQueryRequest.class,
    GetTicketsAndRulesByMovementsRequest.class,
    SetTicketStatusRequest.class,
    GetNumberOfOpenTicketsRequest.class,
    CountTicketListByMovementsRequest.class,
    CreateCustomRuleRequest.class,
    UpdateCustomRuleLastTriggeredRequest.class,
    ReprocessAlarmRequest.class,
    GetTicketListRequest.class,
    UpdateTicketStatusByQueryRequest.class,
    GetSanityRulesRequest.class,
    GetTicketListByQueryRequest.class,
    GetCustomRulesByUserRequest.class,
    PingRequest.class
})
public abstract class RulesBaseRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected RulesDataSourceMethod method;

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link RulesDataSourceMethod }
     *     
     */
    public RulesDataSourceMethod getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link RulesDataSourceMethod }
     *     
     */
    public void setMethod(RulesDataSourceMethod value) {
        this.method = value;
    }

}
