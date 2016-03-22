
package eu.europa.ec.fisheries.schema.rules.movement.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MovementSourceType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MovementSourceType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INMARSAT_C"/>
 *     &lt;enumeration value="IRIDIUM"/>
 *     &lt;enumeration value="AIS"/>
 *     &lt;enumeration value="MANUAL"/>
 *     &lt;enumeration value="OTHER"/>
 *     &lt;enumeration value="NAF"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MovementSourceType")
@XmlEnum
public enum MovementSourceType {

    INMARSAT_C,
    IRIDIUM,
    AIS,
    MANUAL,
    OTHER,
    NAF;

    public String value() {
        return name();
    }

    public static MovementSourceType fromValue(String v) {
        return valueOf(v);
    }

}
