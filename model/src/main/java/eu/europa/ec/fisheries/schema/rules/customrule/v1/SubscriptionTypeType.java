
package eu.europa.ec.fisheries.schema.rules.customrule.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubscriptionTypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SubscriptionTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TICKET"/>
 *     &lt;enumeration value="EMAIL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SubscriptionTypeType")
@XmlEnum
public enum SubscriptionTypeType {

    TICKET,
    EMAIL;

    public String value() {
        return name();
    }

    public static SubscriptionTypeType fromValue(String v) {
        return valueOf(v);
    }

}
