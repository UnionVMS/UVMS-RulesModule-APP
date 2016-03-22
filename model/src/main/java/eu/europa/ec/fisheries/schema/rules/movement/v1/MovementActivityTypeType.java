
package eu.europa.ec.fisheries.schema.rules.movement.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MovementActivityTypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MovementActivityTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="COE"/>
 *     &lt;enumeration value="COX"/>
 *     &lt;enumeration value="TRA"/>
 *     &lt;enumeration value="POR"/>
 *     &lt;enumeration value="CAN"/>
 *     &lt;enumeration value="NOT"/>
 *     &lt;enumeration value="AUT"/>
 *     &lt;enumeration value="SUS"/>
 *     &lt;enumeration value="COB"/>
 *     &lt;enumeration value="CAT"/>
 *     &lt;enumeration value="RET"/>
 *     &lt;enumeration value="OBR"/>
 *     &lt;enumeration value="ANC"/>
 *     &lt;enumeration value="DRI"/>
 *     &lt;enumeration value="FIS"/>
 *     &lt;enumeration value="HAU"/>
 *     &lt;enumeration value="PRO"/>
 *     &lt;enumeration value="STE"/>
 *     &lt;enumeration value="TRX"/>
 *     &lt;enumeration value="OTH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MovementActivityTypeType")
@XmlEnum
public enum MovementActivityTypeType {

    COE,
    COX,
    TRA,
    POR,
    CAN,
    NOT,
    AUT,
    SUS,
    COB,
    CAT,
    RET,
    OBR,
    ANC,
    DRI,
    FIS,
    HAU,
    PRO,
    STE,
    TRX,
    OTH;

    public String value() {
        return name();
    }

    public static MovementActivityTypeType fromValue(String v) {
        return valueOf(v);
    }

}
