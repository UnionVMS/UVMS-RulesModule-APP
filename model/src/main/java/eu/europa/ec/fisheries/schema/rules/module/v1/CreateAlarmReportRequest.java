
package eu.europa.ec.fisheries.schema.rules.module.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;


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
 *         &lt;element name="alarm" type="{urn:alarm.rules.schema.fisheries.ec.europa.eu:v1}AlarmReportType"/>
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
    "alarm"
})
@XmlRootElement(name = "CreateAlarmReportRequest")
public class CreateAlarmReportRequest
    extends RulesBaseRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected AlarmReportType alarm;

    /**
     * Gets the value of the alarm property.
     * 
     * @return
     *     possible object is
     *     {@link AlarmReportType }
     *     
     */
    public AlarmReportType getAlarm() {
        return alarm;
    }

    /**
     * Sets the value of the alarm property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlarmReportType }
     *     
     */
    public void setAlarm(AlarmReportType value) {
        this.alarm = value;
    }

}
