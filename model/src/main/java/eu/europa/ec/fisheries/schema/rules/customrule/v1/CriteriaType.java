
package eu.europa.ec.fisheries.schema.rules.customrule.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CriteriaType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CriteriaType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACTIVITY"/>
 *     &lt;enumeration value="AREA"/>
 *     &lt;enumeration value="ASSET"/>
 *     &lt;enumeration value="ASSET_GROUP"/>
 *     &lt;enumeration value="MOBILE_TERMINAL"/>
 *     &lt;enumeration value="POSITION"/>
 *     &lt;enumeration value="REPORT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CriteriaType")
@XmlEnum
public enum CriteriaType {

    ACTIVITY,
    AREA,
    ASSET,
    ASSET_GROUP,
    MOBILE_TERMINAL,
    POSITION,
    REPORT;

    public String value() {
        return name();
    }

    public static CriteriaType fromValue(String v) {
        return valueOf(v);
    }

}
