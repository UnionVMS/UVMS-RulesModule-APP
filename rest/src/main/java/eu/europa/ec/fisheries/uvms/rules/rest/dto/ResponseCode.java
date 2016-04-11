package eu.europa.ec.fisheries.uvms.rules.rest.dto;

public enum ResponseCode {

    OK(200),
    UNDEFINED_ERROR(500),

    // RULES_ERROR(501),

    INPUT_ERROR(511),
    MAPPING_ERROR(512),

    SERVICE_ERROR(521),
    MODEL_ERROR(522),
    DOMAIN_ERROR(523),
    FORBIDDEN(403);

    private final int code;

    private ResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
