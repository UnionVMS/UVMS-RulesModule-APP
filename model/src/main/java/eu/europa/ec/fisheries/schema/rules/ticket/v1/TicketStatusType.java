
package eu.europa.ec.fisheries.schema.rules.ticket.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TicketStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TicketStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="OPEN"/>
 *     &lt;enumeration value="PENDING"/>
 *     &lt;enumeration value="CLOSED"/>
 *     &lt;enumeration value="NONE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TicketStatusType")
@XmlEnum
public enum TicketStatusType {

    OPEN,
    PENDING,
    CLOSED,
    NONE;

    public String value() {
        return name();
    }

    public static TicketStatusType fromValue(String v) {
        return valueOf(v);
    }

}
