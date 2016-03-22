
package eu.europa.ec.fisheries.schema.rules.customrule.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubscritionOperationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SubscritionOperationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ADD"/>
 *     &lt;enumeration value="REMOVE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SubscritionOperationType")
@XmlEnum
public enum SubscritionOperationType {

    ADD,
    REMOVE;

    public String value() {
        return name();
    }

    public static SubscritionOperationType fromValue(String v) {
        return valueOf(v);
    }

}
