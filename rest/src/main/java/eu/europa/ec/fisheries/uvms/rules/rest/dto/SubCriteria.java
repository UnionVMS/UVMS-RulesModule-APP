package eu.europa.ec.fisheries.uvms.rules.rest.dto;

public enum SubCriteria {

    ALTITUDE(MainCriteria.ROOT),
    ASSET_GROUP(MainCriteria.ROOT),
    CALCULATED_COURSE(MainCriteria.ROOT),
    CALCULATED_SPEED(MainCriteria.ROOT),
    COMCHANNEL_TYPE(MainCriteria.ROOT),
    CONNECT_ID(MainCriteria.ROOT),
    EXTERNAL_MARKING(MainCriteria.ROOT),
    FLAG_STATE(MainCriteria.ROOT),
    LATITUDE(MainCriteria.ROOT),
    LONGITUDE(MainCriteria.ROOT),
    MOVEMENT_GUID(MainCriteria.ROOT),
    MOVEMENT_TYPE(MainCriteria.ROOT),
    POSITION_REPORT_TIME(MainCriteria.ROOT),
    REPORTED_COURSE(MainCriteria.ROOT),
    REPORTED_SPEED(MainCriteria.ROOT),
    SEGMENT_TYPE(MainCriteria.ROOT),
    STATUS_CODE(MainCriteria.ROOT),
    SOURCE(MainCriteria.ROOT),
    WKT(MainCriteria.ROOT),
    VECINITY_OF(MainCriteria.ROOT),

    VESSEL_CFR(MainCriteria.VESSEL),
    VESSEL_IRCS(MainCriteria.VESSEL),
    VESSEL_NAME(MainCriteria.VESSEL),

    MT_SERIAL_NO(MainCriteria.MOBILE_TERMINAL),
    MT_DNID(MainCriteria.MOBILE_TERMINAL),
    MT_MEMBER_ID(MainCriteria.MOBILE_TERMINAL),

    AREA_CODE(MainCriteria.AREA),
    AREA_TYPE(MainCriteria.AREA),
    AREA_ID(MainCriteria.AREA),
    AREA_NAME(MainCriteria.AREA),

    ACTIVITY_CALLBACK(MainCriteria.ACTIVITY),
    ACTIVITY_MESSAGE_ID(MainCriteria.ACTIVITY),
    ACTIVITY_MESSAGE_TYPE(MainCriteria.ACTIVITY),

    ASSET_ID_ASSET_TYPE(MainCriteria.ASSET_ID),
    ASSET_ID_TYPE(MainCriteria.ASSET_ID),
    ASSET_ID_VALUE(MainCriteria.ASSET_ID),

    COUNTRY_CODE(MainCriteria.CLOSEST_COUNTRY),
    COUNTRY_DISTANCE(MainCriteria.CLOSEST_COUNTRY),
    COUNTRY_REMOTE_ID(MainCriteria.CLOSEST_COUNTRY),
    COUNTRY_NAME(MainCriteria.CLOSEST_COUNTRY),

    PORT_CODE(MainCriteria.CLOSEST_PORT),
    PORT_DISTANCE(MainCriteria.CLOSEST_PORT),
    PORT_REMOTE_ID(MainCriteria.CLOSEST_PORT),
    PORT_NAME(MainCriteria.CLOSEST_PORT);

    private final MainCriteria mainCriteria;

    private SubCriteria(MainCriteria mainCriteria) {
        this.mainCriteria = mainCriteria;
    }

    public MainCriteria getMainCriteria() {
        return mainCriteria;
    }

}
