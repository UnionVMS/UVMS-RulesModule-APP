package eu.europa.ec.fisheries.uvms.rules.enums;

public enum ResponseMessageType {
    SET_FLUX_FA_REPORT("FLUXFAReportMessage"),
    SET_FLUX_FA_QUERY("FLUXFAQueryMessage"),
    RECEIVE_MOVEMENT_BATCH("FLUXVesselPositionMessage")
    ;

    private String type;

    ResponseMessageType (String type){
        this.type=type;
    }

    public String getType() {
        return type;
    }
}
