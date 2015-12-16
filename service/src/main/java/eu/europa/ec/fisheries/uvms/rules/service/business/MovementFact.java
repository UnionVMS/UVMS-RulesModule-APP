package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesDozerMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MovementFact {
    private eu.europa.ec.fisheries.schema.movement.v1.MovementType movementMovement;
    private String movementGuid;

    // ROOT
    private List<String> assetGroups;

    // ACTIVITY
    private String activityCallback;
    private String activityMessageId;
    private String activityMessageType;

    // AREA
    List<String> areaCodes;
    List<String> areaTypes;

    List<String> entAreaCodes;
    List<String> entAreaTypes;
    List<String> extAreaCodes;
    List<String> extAreaTypes;

    // ASSET
    private String assetGuid;
    private String assetIdGearType;
    private String externalMarking;
    private String flagState;
    private String cfr;
    private String ircs;
    private String assetName;
    private String assetStatus;

    // MOBILE_TERMINAL
    private String comChannelType;
    private String mobileTerminalType;
    private String mobileTerminalDnid;
    private String mobileTerminalMemberNumber;
    private String mobileTerminalSerialNumber;
    private String mobileTerminalStatus;

    // POSITION
    private Double altitude;
    private Double latitude;
    private Double longitude;
    private Double calculatedCourse;
    private Double calculatedSpeed;
    private String movementType; // MovementTypeType
    private Date positionTime;
    private Double reportedCourse;
    private Double reportedSpeed;
    private String segmentType;
    private String source;
    private String statusCode;
    private String vicinityOf;
    private String closestCountryCode;
    private String closestPortCode;

    // REPORT
    private Long timeDiffPositionReport;
    private Integer sumPositionReport;

    public MovementType getExchangeMovement() {
        MovementType exchangeMovement = RulesDozerMapper.getInstance().getMapper().map(movementMovement, MovementType.class);
        return exchangeMovement;
    }

    public eu.europa.ec.fisheries.schema.movement.v1.MovementType getMovementMovement() {
        return movementMovement;
    }

    public void setMovementMovement(eu.europa.ec.fisheries.schema.movement.v1.MovementType movementMovement) {
        this.movementMovement = movementMovement;
    }

    public String getMovementGuid() {
        return movementGuid;
    }

    public void setMovementGuid(String movementGuid) {
        this.movementGuid = movementGuid;
    }

    public List<String> getAssetGroups() {
        if (this.assetGroups== null) {
            this.assetGroups = new ArrayList<>();
        }
        return assetGroups;
    }

    public void setAssetGroups(List<String> assetGroups) {
        this.assetGroups = assetGroups;
    }

    public String getActivityCallback() {
        return activityCallback;
    }

    public void setActivityCallback(String activityCallback) {
        this.activityCallback = activityCallback;
    }

    public String getActivityMessageId() {
        return activityMessageId;
    }

    public void setActivityMessageId(String activityMessageId) {
        this.activityMessageId = activityMessageId;
    }

    public String getActivityMessageType() {
        return activityMessageType;
    }

    public void setActivityMessageType(String activityMessageType) {
        this.activityMessageType = activityMessageType;
    }

    public List<String> getAreaCodes() {
        if (this.areaCodes == null) {
            this.areaCodes = new ArrayList<>();
        }
        return areaCodes;
    }

    public void setAreaCodes(List<String> areaCodes) {
        this.areaCodes = areaCodes;
    }

    public List<String> getAreaTypes() {
        if (this.areaTypes == null) {
            this.areaTypes = new ArrayList<>();
        }
        return areaTypes;
    }

    public void setAreaTypes(List<String> areaTypes) {
        this.areaTypes = areaTypes;
    }

    public List<String> getEntAreaCodes() {
        if (this.entAreaCodes== null) {
            this.entAreaCodes = new ArrayList<>();
        }
        return entAreaCodes;
    }

    public void setEntAreaCodes(List<String> entAreaCodes) {
        this.entAreaCodes = entAreaCodes;
    }

    public List<String> getEntAreaTypes() {
        if (this.entAreaTypes== null) {
            this.entAreaTypes = new ArrayList<>();
        }
        return entAreaTypes;
    }

    public void setEntAreaTypes(List<String> entAreaTypes) {
        this.entAreaTypes = entAreaTypes;
    }

    public List<String> getExtAreaCodes() {
        if (this.extAreaCodes== null) {
            this.extAreaCodes = new ArrayList<>();
        }
        return extAreaCodes;
    }

    public void setExtAreaCodes(List<String> extAreaCodes) {
        this.extAreaCodes = extAreaCodes;
    }

    public List<String> getExtAreaTypes() {
        if (this.extAreaTypes== null) {
            this.extAreaTypes = new ArrayList<>();
        }
        return extAreaTypes;
    }

    public void setExtAreaTypes(List<String> extAreaTypes) {
        this.extAreaTypes = extAreaTypes;
    }

    public String getAssetGuid() {
        return assetGuid;
    }

    public void setAssetGuid(String assetGuid) {
        this.assetGuid = assetGuid;
    }

    public String getAssetIdGearType() {
        return assetIdGearType;
    }

    public void setAssetIdGearType(String assetIdGearType) {
        this.assetIdGearType = assetIdGearType;
    }

    public String getExternalMarking() {
        return externalMarking;
    }

    public void setExternalMarking(String externalMarking) {
        this.externalMarking = externalMarking;
    }

    public String getFlagState() {
        return flagState;
    }

    public void setFlagState(String flagState) {
        this.flagState = flagState;
    }

    public String getCfr() {
        return cfr;
    }

    public void setCfr(String cfr) {
        this.cfr = cfr;
    }

    public String getIrcs() {
        return ircs;
    }

    public void setIrcs(String ircs) {
        this.ircs = ircs;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(String assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getComChannelType() {
        return comChannelType;
    }

    public void setComChannelType(String comChannelType) {
        this.comChannelType = comChannelType;
    }

    public String getMobileTerminalType() {
        return mobileTerminalType;
    }

    public void setMobileTerminalType(String mobileTerminalType) {
        this.mobileTerminalType = mobileTerminalType;
    }

    public String getMobileTerminalDnid() {
        return mobileTerminalDnid;
    }

    public void setMobileTerminalDnid(String mobileTerminalDnid) {
        this.mobileTerminalDnid = mobileTerminalDnid;
    }

    public String getMobileTerminalMemberNumber() {
        return mobileTerminalMemberNumber;
    }

    public void setMobileTerminalMemberNumber(String mobileTerminalMemberNumber) {
        this.mobileTerminalMemberNumber = mobileTerminalMemberNumber;
    }

    public String getMobileTerminalSerialNumber() {
        return mobileTerminalSerialNumber;
    }

    public void setMobileTerminalSerialNumber(String mobileTerminalSerialNumber) {
        this.mobileTerminalSerialNumber = mobileTerminalSerialNumber;
    }

    public String getMobileTerminalStatus() {
        return mobileTerminalStatus;
    }

    public void setMobileTerminalStatus(String mobileTerminalStatus) {
        this.mobileTerminalStatus = mobileTerminalStatus;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getCalculatedCourse() {
        return calculatedCourse;
    }

    public void setCalculatedCourse(Double calculatedCourse) {
        this.calculatedCourse = calculatedCourse;
    }

    public Double getCalculatedSpeed() {
        return calculatedSpeed;
    }

    public void setCalculatedSpeed(Double calculatedSpeed) {
        this.calculatedSpeed = calculatedSpeed;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public Date getPositionTime() {
        return positionTime;
    }

    public void setPositionTime(Date positionTime) {
        this.positionTime = positionTime;
    }

    public Double getReportedCourse() {
        return reportedCourse;
    }

    public void setReportedCourse(Double reportedCourse) {
        this.reportedCourse = reportedCourse;
    }

    public Double getReportedSpeed() {
        return reportedSpeed;
    }

    public void setReportedSpeed(Double reportedSpeed) {
        this.reportedSpeed = reportedSpeed;
    }

    public String getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(String segmentType) {
        this.segmentType = segmentType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getVicinityOf() {
        return vicinityOf;
    }

    public void setVicinityOf(String vicinityOf) {
        this.vicinityOf = vicinityOf;
    }

    public String getClosestCountryCode() {
        return closestCountryCode;
    }

    public void setClosestCountryCode(String closestCountryCode) {
        this.closestCountryCode = closestCountryCode;
    }

    public String getClosestPortCode() {
        return closestPortCode;
    }

    public void setClosestPortCode(String closestPortCode) {
        this.closestPortCode = closestPortCode;
    }

    public Integer getSumPositionReport() {
        return sumPositionReport;
    }

    public void setSumPositionReport(Integer sumPositionReport) {
        this.sumPositionReport = sumPositionReport;
    }

    public Long getTimeDiffPositionReport() {
        return timeDiffPositionReport;
    }

    public void setTimeDiffPositionReport(Long timeDiffPositionReport) {
        this.timeDiffPositionReport = timeDiffPositionReport;
    }

    @Override
    public String toString() {
        return "MovementFact{" +
                "movementMovement=" + movementMovement +
                ", movementGuid='" + movementGuid + '\'' +
                ", assetGroups=" + (assetGroups != null ? Arrays.toString(assetGroups.toArray()) : null) +
                ", activityCallback='" + activityCallback + '\'' +
                ", activityMessageId='" + activityMessageId + '\'' +
                ", activityMessageType='" + activityMessageType + '\'' +
                ", areaCodes=" + (areaCodes != null ? Arrays.toString(areaCodes.toArray()) : null) +
                ", areaTypes=" + (areaTypes != null ? Arrays.toString(areaTypes.toArray()) : null) +
                ", entAreaCodes=" + (entAreaCodes != null ? Arrays.toString(entAreaCodes.toArray()) : null) +
                ", entAreaTypes=" + (entAreaTypes != null ? Arrays.toString(entAreaTypes.toArray()) : null) +
                ", extAreaCodes=" + (extAreaCodes != null ? Arrays.toString(extAreaCodes.toArray()) : null) +
                ", extAreaTypes=" + (extAreaTypes != null ? Arrays.toString(extAreaTypes.toArray()) : null) +
                ", assetGuid='" + assetGuid + '\'' +
                ", assetIdGearType='" + assetIdGearType + '\'' +
                ", externalMarking='" + externalMarking + '\'' +
                ", flagState='" + flagState + '\'' +
                ", cfr='" + cfr + '\'' +
                ", ircs='" + ircs + '\'' +
                ", assetName='" + assetName + '\'' +
                ", assetStatus='" + assetStatus + '\'' +
                ", comChannelType='" + comChannelType + '\'' +
                ", mobileTerminalType='" + mobileTerminalType + '\'' +
                ", mobileTerminalDnid='" + mobileTerminalDnid + '\'' +
                ", mobileTerminalMemberNumber='" + mobileTerminalMemberNumber + '\'' +
                ", mobileTerminalSerialNumber='" + mobileTerminalSerialNumber + '\'' +
                ", mobileTerminalStatus='" + mobileTerminalStatus + '\'' +
                ", altitude=" + altitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", calculatedCourse=" + calculatedCourse +
                ", calculatedSpeed=" + calculatedSpeed +
                ", movementType='" + movementType + '\'' +
                ", positionTime=" + positionTime +
                ", reportedCourse=" + reportedCourse +
                ", reportedSpeed=" + reportedSpeed +
                ", segmentType='" + segmentType + '\'' +
                ", source='" + source + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", vicinityOf='" + vicinityOf + '\'' +
                ", closestCountryCode='" + closestCountryCode + '\'' +
                ", closestPortCode='" + closestPortCode + '\'' +
                ", timeDiffPositionReport=" + timeDiffPositionReport +
                ", sumPositionReport=" + sumPositionReport +
                '}';
    }
}
