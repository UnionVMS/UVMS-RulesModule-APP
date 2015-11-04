package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovementFact {
    // Base
    private Double calculatedCourse;
    private Double calculatedSpeed;
    private String comChannelType;
    private String connectId;
    private String externalMarking;
    private String flagState;
    private String mobileTerminalMemberNumber;
    private String mobileTerminalSerialNumber;
    private String mobileTerminalDnid;
    private String movementGuid;
    private String movementType; // MovementTypeType
    private Date positionTime;
    private Double reportedCourse;
    private Double reportedSpeed;
    private String source;
    private String statusCode;
    private String vesselGuid;
    private String vesselName;
    private String wkt;

    // Activity
    private String activityCallback;
    private String activityMessageId;
    private String activityMessageType;

    // AssetId
    private String assetIdAssetType;
    private String assetIdType;
    private String assetIdValue;
    private String vesselCfr;
    private String vesselIrcs;

    // Position
    private Double altitude;
    private Double latitude;
    private Double longitude;

    // Meta
    // private String previousMovementId;
    private String fromSegmentType;

    // Areas
    List<String> areaCodes;
    List<String> areaRemoteIds;
    List<String> areaTypes;
    List<String> areaNames;

    // Closest country
    private String closestCountryCode;
    private Double closestCountryDistance;
    private String closestCountryRemoteId;
    private String closestCountryName;

    // Closets port
    private String closestPortCode;
    private Double closestPortDistance;
    private String closestPortRemoteId;
    private String closestPortName;

    public Date getPositionTime() {
        return positionTime;
    }

    public void setPositionTime(Date positionTime) {
        this.positionTime = positionTime;
    }

    public String getVesselCfr() {
        return vesselCfr;
    }

    public void setVesselCfr(String vesselCfr) {
        this.vesselCfr = vesselCfr;
    }

    public String getVesselIrcs() {
        return vesselIrcs;
    }

    public void setVesselIrcs(String vesselIrcs) {
        this.vesselIrcs = vesselIrcs;
    }

    public String getVesselGuid() {
        return vesselGuid;
    }

    public void setVesselGuid(String vesselGuid) {
        this.vesselGuid = vesselGuid;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
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

    public String getMobileTerminalDnid() {
        return mobileTerminalDnid;
    }

    public void setMobileTerminalDnid(String mobileTerminalDnid) {
        this.mobileTerminalDnid = mobileTerminalDnid;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Double getCalculatedSpeed() {
        return calculatedSpeed;
    }

    public void setCalculatedSpeed(Double calculatedSpeed) {
        this.calculatedSpeed = calculatedSpeed;
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

    public Double getCalculatedCourse() {
        return calculatedCourse;
    }

    public void setCalculatedCourse(Double calculatedCourse) {
        this.calculatedCourse = calculatedCourse;
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

    public String getConnectId() {
        return connectId;
    }

    public void setConnectId(String connectId) {
        this.connectId = connectId;
    }

    public String getMovementGuid() {
        return movementGuid;
    }

    public void setMovementGuid(String movementGuid) {
        this.movementGuid = movementGuid;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    public String getComChannelType() {
        return comChannelType;
    }

    public void setComChannelType(String comChannelType) {
        this.comChannelType = comChannelType;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getFromSegmentType() {
        return fromSegmentType;
    }

    public void setFromSegmentType(String fromSegmentType) {
        this.fromSegmentType = fromSegmentType;
    }

    public String getClosestCountryCode() {
        return closestCountryCode;
    }

    public void setClosestCountryCode(String closestCountryCode) {
        this.closestCountryCode = closestCountryCode;
    }

    public Double getClosestCountryDistance() {
        return closestCountryDistance;
    }

    public void setClosestCountryDistance(Double closestCountryDistance) {
        this.closestCountryDistance = closestCountryDistance;
    }

    public String getClosestCountryRemoteId() {
        return closestCountryRemoteId;
    }

    public void setClosestCountryRemoteId(String closestCountryRemoteId) {
        this.closestCountryRemoteId = closestCountryRemoteId;
    }

    public String getClosestPortCode() {
        return closestPortCode;
    }

    public void setClosestPortCode(String closestPortCode) {
        this.closestPortCode = closestPortCode;
    }

    public Double getClosestPortDistance() {
        return closestPortDistance;
    }

    public void setClosestPortDistance(Double closestPortDistance) {
        this.closestPortDistance = closestPortDistance;
    }

    public String getClosestPortRemoteId() {
        return closestPortRemoteId;
    }

    public void setClosestPortRemoteId(String closestPortRemoteId) {
        this.closestPortRemoteId = closestPortRemoteId;
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

    public String getAssetIdAssetType() {
        return assetIdAssetType;
    }

    public void setAssetIdAssetType(String assetIdAssetType) {
        this.assetIdAssetType = assetIdAssetType;
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

    public List<String> getAreaTypes() {
        if (areaTypes == null) {
            areaTypes = new ArrayList<String>();
        }
        return areaTypes;
    }

    public void setAreaTypes(List<String> areaTypes) {
        this.areaTypes = areaTypes;
    }

    public List<String> getAreaCodes() {
        if (areaCodes == null) {
            areaCodes = new ArrayList<String>();
        }
        return areaCodes;
    }

    public void setAreaCodes(List<String> areaCodes) {
        this.areaCodes = areaCodes;
    }

    public List<String> getAreaRemoteIds() {
        if (areaRemoteIds == null) {
            areaRemoteIds = new ArrayList<String>();
        }
        return areaRemoteIds;
    }

    public void setAreaRemoteIds(List<String> areaRemoteIds) {
        this.areaRemoteIds = areaRemoteIds;
    }

    public List<String> getAreaNames() {
        if (areaNames == null) {
            areaNames = new ArrayList<String>();
        }
        return areaNames;
    }

    public void setAreaNames(List<String> areaNames) {
        this.areaNames = areaNames;
    }

    public String getClosestCountryName() {
        return closestCountryName;
    }

    public void setClosestCountryName(String closestCountryName) {
        this.closestCountryName = closestCountryName;
    }

    public String getClosestPortName() {
        return closestPortName;
    }

    public void setClosestPortName(String closestPortName) {
        this.closestPortName = closestPortName;
    }

    @Override
    public String toString() {
        return "MovementFact{" +
                "calculatedCourse=" + calculatedCourse +
                ", calculatedSpeed=" + calculatedSpeed +
                ", comChannelType='" + comChannelType + '\'' +
                ", connectId='" + connectId + '\'' +
                ", externalMarking='" + externalMarking + '\'' +
                ", flagState='" + flagState + '\'' +
                ", mobileTerminalMemberNumber='" + mobileTerminalMemberNumber + '\'' +
                ", mobileTerminalSerialNumber='" + mobileTerminalSerialNumber + '\'' +
                ", mobileTerminalDnid='" + mobileTerminalDnid + '\'' +
                ", movementGuid='" + movementGuid + '\'' +
                ", movementType='" + movementType + '\'' +
                ", positionTime=" + positionTime +
                ", reportedCourse=" + reportedCourse +
                ", reportedSpeed=" + reportedSpeed +
                ", source='" + source + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", vesselGuid='" + vesselGuid + '\'' +
                ", vesselName='" + vesselName + '\'' +
                ", wkt='" + wkt + '\'' +
                ", activityCallback='" + activityCallback + '\'' +
                ", activityMessageId='" + activityMessageId + '\'' +
                ", activityMessageType='" + activityMessageType + '\'' +
                ", assetIdAssetType='" + assetIdAssetType + '\'' +
                ", assetIdType='" + assetIdType + '\'' +
                ", assetIdValue='" + assetIdValue + '\'' +
                ", vesselCfr='" + vesselCfr + '\'' +
                ", vesselIrcs='" + vesselIrcs + '\'' +
                ", altitude=" + altitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", fromSegmentType='" + fromSegmentType + '\'' +
                ", areaCodes=" + areaCodes +
                ", areaRemoteIds=" + areaRemoteIds +
                ", areaTypes=" + areaTypes +
                ", areaNames=" + areaNames +
                ", closestCountryCode='" + closestCountryCode + '\'' +
                ", closestCountryDistance=" + closestCountryDistance +
                ", closestCountryRemoteId='" + closestCountryRemoteId + '\'' +
                ", closestCountryName='" + closestCountryName + '\'' +
                ", closestPortCode='" + closestPortCode + '\'' +
                ", closestPortDistance=" + closestPortDistance +
                ", closestPortRemoteId='" + closestPortRemoteId + '\'' +
                ", closestPortName='" + closestPortName + '\'' +
                '}';
    }

}
