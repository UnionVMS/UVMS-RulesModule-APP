
package eu.europa.ec.fisheries.schema.rules.customrule.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MobileTerminalStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MobileTerminalStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACTIVE"/>
 *     &lt;enumeration value="INACTIVE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MobileTerminalStatus")
@XmlEnum
public enum MobileTerminalStatus {

    ACTIVE,
    INACTIVE;

    public String value() {
        return name();
    }

    public static MobileTerminalStatus fromValue(String v) {
        return valueOf(v);
    }

}
