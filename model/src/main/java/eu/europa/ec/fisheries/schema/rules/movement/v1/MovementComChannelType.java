
package eu.europa.ec.fisheries.schema.rules.movement.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MovementComChannelType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MovementComChannelType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FLUX"/>
 *     &lt;enumeration value="MANUAL"/>
 *     &lt;enumeration value="NAF"/>
 *     &lt;enumeration value="MOBILE_TERMINAL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MovementComChannelType")
@XmlEnum
public enum MovementComChannelType {

    FLUX,
    MANUAL,
    NAF,
    MOBILE_TERMINAL;

    public String value() {
        return name();
    }

    public static MovementComChannelType fromValue(String v) {
        return valueOf(v);
    }

}
