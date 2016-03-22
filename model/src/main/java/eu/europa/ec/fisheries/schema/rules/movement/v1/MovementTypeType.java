
package eu.europa.ec.fisheries.schema.rules.movement.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MovementTypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MovementTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="POS"/>
 *     &lt;enumeration value="ENT"/>
 *     &lt;enumeration value="EXI"/>
 *     &lt;enumeration value="MAN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MovementTypeType")
@XmlEnum
public enum MovementTypeType {

    POS,
    ENT,
    EXI,
    MAN;

    public String value() {
        return name();
    }

    public static MovementTypeType fromValue(String v) {
        return valueOf(v);
    }

}
