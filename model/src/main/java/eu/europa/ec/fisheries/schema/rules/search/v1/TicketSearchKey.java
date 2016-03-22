
package eu.europa.ec.fisheries.schema.rules.search.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TicketSearchKey.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TicketSearchKey">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="TICKET_GUID"/>
 *     &lt;enumeration value="ASSET_GUID"/>
 *     &lt;enumeration value="RULE_GUID"/>
 *     &lt;enumeration value="RULE_NAME"/>
 *     &lt;enumeration value="RULE_RECIPIENT"/>
 *     &lt;enumeration value="STATUS"/>
 *     &lt;enumeration value="FROM_DATE"/>
 *     &lt;enumeration value="TO_DATE"/>
 *     &lt;enumeration value="UPDATED_BY"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TicketSearchKey")
@XmlEnum
public enum TicketSearchKey {

    TICKET_GUID,
    ASSET_GUID,
    RULE_GUID,
    RULE_NAME,
    RULE_RECIPIENT,
    STATUS,
    FROM_DATE,
    TO_DATE,
    UPDATED_BY;

    public String value() {
        return name();
    }

    public static TicketSearchKey fromValue(String v) {
        return valueOf(v);
    }

}
