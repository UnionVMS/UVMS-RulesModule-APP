package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.Date;

public class PreviousReportFact {

    private Date deadline;
    private String assetIdType;
    private String assetIdValue;
    private String movementGuid;
    private String assetGuid;

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getAssetIdType() {
        return assetIdType;
    }

    public void setAssetIdType(String assetIdType) {
        this.assetIdType = assetIdType;
    }

    public String getAssetIdValue() {
        return assetIdValue;
    }

    public void setAssetIdValue(String assetIdValue) {
        this.assetIdValue = assetIdValue;
    }

    public String getMovementGuid() {
        return movementGuid;
    }

    public void setMovementGuid(String movementGuid) {
        this.movementGuid = movementGuid;
    }

    public String getAssetGuid() {
        return assetGuid;
    }

    public void setAssetGuid(String assetGuid) {
        this.assetGuid = assetGuid;
    }

}
