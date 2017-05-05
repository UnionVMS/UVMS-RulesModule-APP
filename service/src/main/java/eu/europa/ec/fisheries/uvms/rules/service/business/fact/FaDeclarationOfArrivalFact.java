package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

import java.util.List;

/**
 * Created by sanera on 05/05/2017.
 */
public class FaDeclarationOfArrivalFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;
    private CodeType faReportTypeCode;
    private DateTimeType occurrenceDateTime;
    private CodeType reasonCode;
    private List<FLUXLocation> relatedFLUXLocations;
    private List<CodeType> fluxLocationTypeCodes;
    private List<CodeType> fishingGearRoleCodes;
    private List<IdType> fishingTripIds;

    public CodeType getFishingActivityTypeCode() {
        return fishingActivityTypeCode;
    }

    public void setFishingActivityTypeCode(CodeType fishingActivityTypeCode) {
        this.fishingActivityTypeCode = fishingActivityTypeCode;
    }

    public CodeType getFaReportTypeCode() {
        return faReportTypeCode;
    }

    public void setFaReportTypeCode(CodeType faReportTypeCode) {
        this.faReportTypeCode = faReportTypeCode;
    }

    public DateTimeType getOccurrenceDateTime() {
        return occurrenceDateTime;
    }

    public void setOccurrenceDateTime(DateTimeType occurrenceDateTime) {
        this.occurrenceDateTime = occurrenceDateTime;
    }

    public CodeType getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(CodeType reasonCode) {
        this.reasonCode = reasonCode;
    }

    public List<FLUXLocation> getRelatedFLUXLocations() {
        return relatedFLUXLocations;
    }

    public void setRelatedFLUXLocations(List<FLUXLocation> relatedFLUXLocations) {
        this.relatedFLUXLocations = relatedFLUXLocations;
    }

    public List<CodeType> getFluxLocationTypeCodes() {
        return fluxLocationTypeCodes;
    }

    public void setFluxLocationTypeCodes(List<CodeType> fluxLocationTypeCodes) {
        this.fluxLocationTypeCodes = fluxLocationTypeCodes;
    }

    public List<CodeType> getFishingGearRoleCodes() {
        return fishingGearRoleCodes;
    }

    public void setFishingGearRoleCodes(List<CodeType> fishingGearRoleCodes) {
        this.fishingGearRoleCodes = fishingGearRoleCodes;
    }

    public List<IdType> getFishingTripIds() {
        return fishingTripIds;
    }

    public void setFishingTripIds(List<IdType> fishingTripIds) {
        this.fishingTripIds = fishingTripIds;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_DECLARATION_OF_ARRIVAL;
    }
}
