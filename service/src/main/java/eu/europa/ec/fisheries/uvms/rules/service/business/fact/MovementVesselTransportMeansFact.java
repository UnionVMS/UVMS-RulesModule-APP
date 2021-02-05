package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.Arrays;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselCountryType;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselPositionEventType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.IDType;

@Data
public class MovementVesselTransportMeansFact extends AbstractFact {

    private List<IDType> ids;
    private VesselCountryType registrationVesselCountry;
    private List<VesselPositionEventType> specifiedVesselPositionEvents;
    
    public boolean hasAtLeastTimesXNonEmptyIds(List<IDType> ids, int count) {
        return ids.stream().filter(id -> id.getValue() != null && !id.getValue().isEmpty()).count() >= count;
    }
    
    public boolean hasAtLeastOneOfTheSchemeIds(List<IDType> ids, String... schemeIds) {
        List<String> schemeIdsAsList = Arrays.asList(schemeIds);
        return ids.stream().anyMatch(id -> schemeIdsAsList.contains(id.getSchemeID()));
    }
    
    public boolean hasBothSchemeIdsOrNone(List<IDType> ids, String schemeId1, String schemeId2) {
        return ids.stream().anyMatch(id -> schemeId1.equals(id.getSchemeID())) == ids.stream().anyMatch(id -> schemeId2.equals(id.getSchemeID()));
    }

    public boolean hasAtLeastTimesXSpecifiedVesselPositionEvents(List<VesselPositionEventType> specifiedVesselPositionEvents, int count) {
        return specifiedVesselPositionEvents.size() >= count;
    }
    @Override
    public void setFactType() {
        this.factType = FactType.MOVEMENT_VESSEL_TRANSPORT_MEANS;
    }
}
