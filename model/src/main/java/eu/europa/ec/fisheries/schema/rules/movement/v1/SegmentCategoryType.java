
package eu.europa.ec.fisheries.schema.rules.movement.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SegmentCategoryType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SegmentCategoryType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="GAP"/>
 *     &lt;enumeration value="JUMP"/>
 *     &lt;enumeration value="IN_PORT"/>
 *     &lt;enumeration value="EXIT_PORT"/>
 *     &lt;enumeration value="ENTER_PORT"/>
 *     &lt;enumeration value="NULL_DUR"/>
 *     &lt;enumeration value="ANCHORED"/>
 *     &lt;enumeration value="LOW_SPEED"/>
 *     &lt;enumeration value="OTHER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SegmentCategoryType")
@XmlEnum
public enum SegmentCategoryType {

    GAP,
    JUMP,
    IN_PORT,
    EXIT_PORT,
    ENTER_PORT,
    NULL_DUR,
    ANCHORED,
    LOW_SPEED,
    OTHER;

    public String value() {
        return name();
    }

    public static SegmentCategoryType fromValue(String v) {
        return valueOf(v);
    }

}
