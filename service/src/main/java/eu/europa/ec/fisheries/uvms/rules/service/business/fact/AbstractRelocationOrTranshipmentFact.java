package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractRelocationOrTranshipmentFact extends AbstractFact {

    protected CodeType fishingActivityTypeCode;
    protected CodeType faReportDocumentTypeCode;
    protected List<CodeType> faCatchTypeCode;
    protected List<CodeType> faCatchSpeciesCodes;
    protected List<FLUXLocation> relatedFLUXLocations;
    protected List<CodeType> fluxLocationTypeCode;
    protected List<CodeType> vesselTransportMeansRoleCodes;
    protected List<VesselTransportMeans> relatedVesselTransportMeans;
    protected List<MeasureType> fluxCharacteristicValueQuantity;
    protected List<FLUXCharacteristic> specifiedFLUXCharacteristics;
    protected List<FACatch> specifiedFACatches;
    protected List<CodeType> faCatchSpecifiedFLUXLocationsTypeCodes;
    protected List<CodeType> fluxCharacteristicTypeCodes;
    protected List<FLAPDocument> specifiedFLAPDocuments;
    protected List<IdType> flapDocumentIdTypes;

    /**
     * This method checks if Every FACatch with typeCode LOADED has atleast one AREA FluxLocation
     * IF yes, it returns true, else false.
     * @param specifiedFACatches
     * @return
     */
    public boolean ifFLUXLocationForFACatchIsAREA(List<FACatch> specifiedFACatches){
        if (CollectionUtils.isEmpty(specifiedFACatches)){
            return false;
        }
        boolean isPresent = true;
        for (FACatch faCatch : specifiedFACatches){
            if (faCatch.getTypeCode()!=null && faCatch.getTypeCode().getValue().equals("LOADED") )  {
                isPresent = false;
                if (CollectionUtils.isEmpty(faCatch.getSpecifiedFLUXLocations())){
                    return false;
                }
                for (FLUXLocation fluxLocation : faCatch.getSpecifiedFLUXLocations()){
                    if (fluxLocation.getTypeCode()!=null && fluxLocation.getTypeCode().getValue().equals("AREA")){
                        isPresent = true;
                        break;
                    }
                }
            }
        }
        return isPresent;
    }
}
