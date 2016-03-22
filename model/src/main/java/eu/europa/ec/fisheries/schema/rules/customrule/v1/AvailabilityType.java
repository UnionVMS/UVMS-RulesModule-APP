
package eu.europa.ec.fisheries.schema.rules.customrule.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AvailabilityType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AvailabilityType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GLOBAL"/>
 *     &lt;enumeration value="PUBLIC"/>
 *     &lt;enumeration value="PRIVATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AvailabilityType")
@XmlEnum
public enum AvailabilityType {

    GLOBAL,
    PUBLIC,
    PRIVATE;

    public String value() {
        return name();
    }

    public static AvailabilityType fromValue(String v) {
        return valueOf(v);
    }

}
