
package eu.europa.ec.fisheries.schema.rules.module.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RulesModuleMethod.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RulesModuleMethod">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SET_MOVEMENT_REPORT"/>
 *     &lt;enumeration value="VALIDATE_MOVEMENT_REPORT"/>
 *     &lt;enumeration value="PING"/>
 *     &lt;enumeration value="GET_CUSTOM_RULE"/>
 *     &lt;enumeration value="GET_TICKETS_BY_MOVEMENTS"/>
 *     &lt;enumeration value="COUNT_TICKETS_BY_MOVEMENTS"/>
 *     &lt;enumeration value="GET_TICKETS_AND_RULES_BY_MOVEMENTS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RulesModuleMethod")
@XmlEnum
public enum RulesModuleMethod {

    SET_MOVEMENT_REPORT,
    VALIDATE_MOVEMENT_REPORT,
    PING,
    GET_CUSTOM_RULE,
    GET_TICKETS_BY_MOVEMENTS,
    COUNT_TICKETS_BY_MOVEMENTS,
    GET_TICKETS_AND_RULES_BY_MOVEMENTS;

    public String value() {
        return name();
    }

    public static RulesModuleMethod fromValue(String v) {
        return valueOf(v);
    }

}
