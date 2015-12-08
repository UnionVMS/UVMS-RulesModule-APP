package eu.europa.ec.fisheries.uvms.rules.model.constant;

public enum AuditOperationEnum {
    CREATE("Create"),
    UPDATE("Update"),
    DELETE("Delete"),
    SEND_EMAIL("Send Email"),
    SEND_TO_ENDPOINT("Send To Endpoint");

    private String value;

    AuditOperationEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
