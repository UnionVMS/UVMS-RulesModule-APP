package eu.europa.ec.fisheries.uvms.rules.service.bean.asset.gateway.client.dto;

public class AssetErrorResponseDto {
    private Integer code;
    private String codeName;
    private String message;

    public AssetErrorResponseDto() {
    }

    public AssetErrorResponseDto(Integer code, String codeName, String message) {
        this.code = code;
        this.codeName = codeName;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
}
