
package eu.europa.ec.fisheries.schema.rules.search.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomRuleSearchKey.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CustomRuleSearchKey">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NAME"/>
 *     &lt;enumeration value="GUID"/>
 *     &lt;enumeration value="TYPE"/>
 *     &lt;enumeration value="AVAILABILITY"/>
 *     &lt;enumeration value="RULE_USER"/>
 *     &lt;enumeration value="TICKET_ACTION_USER"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CustomRuleSearchKey")
@XmlEnum
public enum CustomRuleSearchKey {

    NAME,
    GUID,
    TYPE,
    AVAILABILITY,
    RULE_USER,
    TICKET_ACTION_USER;

    public String value() {
        return name();
    }

    public static CustomRuleSearchKey fromValue(String v) {
        return valueOf(v);
    }

}
