package eu.europa.ec.fisheries.uvms.rules.rest.dto;

public enum SubCriteria {

    ASSET_GROUP(MainCriteria.ROOT),

    ACTIVITY_CALLBACK(MainCriteria.ACTIVITY),
    ACTIVITY_MESSAGE_ID(MainCriteria.ACTIVITY),
    ACTIVITY_MESSAGE_TYPE(MainCriteria.ACTIVITY),

    AREA_CODE(MainCriteria.AREA),
    AREA_NAME(MainCriteria.AREA),
    AREA_TYPE(MainCriteria.AREA),
    AREA_ID(MainCriteria.AREA),

    ASSET_ID_GEAR_TYPE(MainCriteria.ASSET),
    EXTERNAL_MARKING(MainCriteria.ASSET),
    FLAG_STATE(MainCriteria.ASSET),
    VESSEL_CFR(MainCriteria.ASSET),
    VESSEL_IRCS(MainCriteria.ASSET),
    VESSEL_NAME(MainCriteria.ASSET),

    COMCHANNEL_TYPE(MainCriteria.MOBILE_TERMINAL),
    MT_TYPE(MainCriteria.MOBILE_TERMINAL),
    MT_DNID(MainCriteria.MOBILE_TERMINAL),
    MT_MEMBER_ID(MainCriteria.MOBILE_TERMINAL),
    MT_SERIAL_NO(MainCriteria.MOBILE_TERMINAL),

    ALTITUDE(MainCriteria.POSITION),
    LATITUDE(MainCriteria.POSITION),
    LONGITUDE(MainCriteria.POSITION),
    CALCULATED_COURSE(MainCriteria.POSITION),
    CALCULATED_SPEED(MainCriteria.POSITION),
    MOVEMENT_TYPE(MainCriteria.POSITION),
    POSITION_REPORT_TIME(MainCriteria.POSITION),
    REPORTED_COURSE(MainCriteria.POSITION),
    REPORTED_SPEED(MainCriteria.POSITION),
    SEGMENT_TYPE(MainCriteria.POSITION),
    SOURCE(MainCriteria.POSITION),
    STATUS_CODE(MainCriteria.POSITION),
    VICINITY_OF(MainCriteria.POSITION),
    CLOSEST_COUNTRY_CODE(MainCriteria.POSITION),
    CLOSEST_PORT_CODE(MainCriteria.POSITION);

    private final MainCriteria mainCriteria;

    private SubCriteria(MainCriteria mainCriteria) {
        this.mainCriteria = mainCriteria;
    }

    public MainCriteria getMainCriteria() {
        return mainCriteria;
    }

}
