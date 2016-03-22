
package eu.europa.ec.fisheries.schema.rules.search.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AlarmSearchKey.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AlarmSearchKey">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ALARM_GUID"/>
 *     &lt;enumeration value="ASSET_GUID"/>
 *     &lt;enumeration value="STATUS"/>
 *     &lt;enumeration value="RULE_RECIPIENT"/>
 *     &lt;enumeration value="FROM_DATE"/>
 *     &lt;enumeration value="TO_DATE"/>
 *     &lt;enumeration value="RULE_GUID"/>
 *     &lt;enumeration value="RULE_NAME"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AlarmSearchKey")
@XmlEnum
public enum AlarmSearchKey {

    ALARM_GUID,
    ASSET_GUID,
    STATUS,
    RULE_RECIPIENT,
    FROM_DATE,
    TO_DATE,
    RULE_GUID,
    RULE_NAME;

    public String value() {
        return name();
    }

    public static AlarmSearchKey fromValue(String v) {
        return valueOf(v);
    }

}
