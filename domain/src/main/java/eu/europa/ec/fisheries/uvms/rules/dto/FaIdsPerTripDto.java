package eu.europa.ec.fisheries.uvms.rules.dto;

import lombok.Data;

@Data
public class FaIdsPerTripDto {

    private static final String DASH = "-";

    private String tripIdSchemeidFaTypeReportType;

    public FaIdsPerTripDto(String tripId, String tripSchemeId, String faTypeCode, String reportTypeCode) {
        this.tripIdSchemeidFaTypeReportType = tripId +DASH+ tripSchemeId +DASH+ faTypeCode +DASH+ reportTypeCode;
    }

    public FaIdsPerTripDto(String tripIdSchemeidFaTypeReportType) {
        this.tripIdSchemeidFaTypeReportType = tripIdSchemeidFaTypeReportType;
    }

}
