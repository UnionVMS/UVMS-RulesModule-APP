
package eu.europa.ec.fisheries.schema.rules.source.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RulesDataSourceMethod.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RulesDataSourceMethod">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CREATE_CUSTOM_RULE"/>
 *     &lt;enumeration value="UPDATE_CUSTOM_RULE"/>
 *     &lt;enumeration value="DELETE_CUSTOM_RULE"/>
 *     &lt;enumeration value="UPDATE_CUSTOM_RULE_LAST_TRIGGERED"/>
 *     &lt;enumeration value="GET_RUNNABLE_CUSTOM_RULES"/>
 *     &lt;enumeration value="LIST_CUSTOM_RULES_BY_USER"/>
 *     &lt;enumeration value="LIST_CUSTOM_RULES_BY_QUERY"/>
 *     &lt;enumeration value="SET_TICKET_STATUS"/>
 *     &lt;enumeration value="UPDATE_TICKET_STATUS_BY_QUERY"/>
 *     &lt;enumeration value="LIST_TICKETS"/>
 *     &lt;enumeration value="GET_TICKETS_BY_MOVEMENTS"/>
 *     &lt;enumeration value="COUNT_TICKETS_BY_MOVEMENTS"/>
 *     &lt;enumeration value="SET_ALARM_STATUS"/>
 *     &lt;enumeration value="LIST_ALARMS"/>
 *     &lt;enumeration value="GET_CUSTOM_RULE"/>
 *     &lt;enumeration value="GET_ALARM"/>
 *     &lt;enumeration value="GET_TICKET"/>
 *     &lt;enumeration value="GET_NUMBER_OF_OPEN_TICKETS"/>
 *     &lt;enumeration value="GET_NUMBER_OF_OPEN_ALARMS"/>
 *     &lt;enumeration value="UPDATE_CUSTOM_RULE_SUBSCRIPTION"/>
 *     &lt;enumeration value="REPROCESS_ALARMS"/>
 *     &lt;enumeration value="GET_TICKETS_AND_RULES_BY_MOVEMENTS"/>
 *     &lt;enumeration value="CREATE_ALARM_REPORT"/>
 *     &lt;enumeration value="CREATE_TICKET"/>
 *     &lt;enumeration value="GET_TICKET_BY_ASSET_AND_RULE"/>
 *     &lt;enumeration value="GET_ALARM_REPORT_BY_ASSET_AND_RULE"/>
 *     &lt;enumeration value="UPSERT_PREVIOUS_REPORT"/>
 *     &lt;enumeration value="GET_PREVIOUS_REPORT_BY_ASSET_GUID"/>
 *     &lt;enumeration value="GET_SANITY_RULES"/>
 *     &lt;enumeration value="GET_PREVIOUS_REPORTS"/>
 *     &lt;enumeration value="PING"/>
 *     &lt;enumeration value="GET_NUMBER_OF_ASSETS_NOT_SENDING"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RulesDataSourceMethod")
@XmlEnum
public enum RulesDataSourceMethod {

    CREATE_CUSTOM_RULE,
    UPDATE_CUSTOM_RULE,
    DELETE_CUSTOM_RULE,
    UPDATE_CUSTOM_RULE_LAST_TRIGGERED,
    GET_RUNNABLE_CUSTOM_RULES,
    LIST_CUSTOM_RULES_BY_USER,
    LIST_CUSTOM_RULES_BY_QUERY,
    SET_TICKET_STATUS,
    UPDATE_TICKET_STATUS_BY_QUERY,
    LIST_TICKETS,
    GET_TICKETS_BY_MOVEMENTS,
    COUNT_TICKETS_BY_MOVEMENTS,
    SET_ALARM_STATUS,
    LIST_ALARMS,
    GET_CUSTOM_RULE,
    GET_ALARM,
    GET_TICKET,
    GET_NUMBER_OF_OPEN_TICKETS,
    GET_NUMBER_OF_OPEN_ALARMS,
    UPDATE_CUSTOM_RULE_SUBSCRIPTION,
    REPROCESS_ALARMS,
    GET_TICKETS_AND_RULES_BY_MOVEMENTS,
    CREATE_ALARM_REPORT,
    CREATE_TICKET,
    GET_TICKET_BY_ASSET_AND_RULE,
    GET_ALARM_REPORT_BY_ASSET_AND_RULE,
    UPSERT_PREVIOUS_REPORT,
    GET_PREVIOUS_REPORT_BY_ASSET_GUID,
    GET_SANITY_RULES,
    GET_PREVIOUS_REPORTS,
    PING,
    GET_NUMBER_OF_ASSETS_NOT_SENDING;

    public String value() {
        return name();
    }

    public static RulesDataSourceMethod fromValue(String v) {
        return valueOf(v);
    }

}
