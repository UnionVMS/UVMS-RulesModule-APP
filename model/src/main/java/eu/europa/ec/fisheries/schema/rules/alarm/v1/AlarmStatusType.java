
package eu.europa.ec.fisheries.schema.rules.alarm.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlarmStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AlarmStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="OPEN"/>
 *     &lt;enumeration value="REJECTED"/>
 *     &lt;enumeration value="REPROCESSED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AlarmStatusType")
@XmlEnum
public enum AlarmStatusType {

    OPEN,
    REJECTED,
    REPROCESSED;

    public String value() {
        return name();
    }

    public static AlarmStatusType fromValue(String v) {
        return valueOf(v);
    }

}
