package eu.europa.ec.fisheries.uvms.rules.rest.dto;

public enum ResponseCode implements RestResponseCode {

    OK("200"),
    ERROR("500");

    private final String code;

    private ResponseCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

}
