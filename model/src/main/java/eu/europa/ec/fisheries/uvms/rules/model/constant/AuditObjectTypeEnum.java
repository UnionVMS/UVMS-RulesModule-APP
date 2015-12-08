package eu.europa.ec.fisheries.uvms.rules.model.constant;

public enum AuditObjectTypeEnum {

    CUSTOM_RULE("Custom Rule"),
    TICKET("Ticket"),
    ALARM("Alarm"),
    CUSTOM_RULE_ACTION("Custom Rule Action Triggered");

    private String value;

    AuditObjectTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
