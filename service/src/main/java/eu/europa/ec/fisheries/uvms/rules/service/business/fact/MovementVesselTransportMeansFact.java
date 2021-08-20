package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetIdType;
import lombok.Data;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselCountryType;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselPositionEventType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.IDType;

@Data
public class MovementVesselTransportMeansFact extends AbstractFact {

    private List<IDType> ids;
    private DateTime creationDateTime;
    private VesselCountryType registrationVesselCountry;
    private List<VesselPositionEventType> specifiedVesselPositionEvents;
    private Asset asset;
    
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

    public boolean hasSchemeId(List<IDType> ids, String schemeID){
        return ids.stream().anyMatch(id -> schemeID.equals(id.getSchemeID()));
    }
    
    public boolean hasExistingAsset(Asset asset) {
        return asset != null && asset.getAssetId() != null && AssetIdType.GUID.equals(asset.getAssetId().getType());
    }

    public boolean hasValidGFCM(Asset asset,List<IDType> ids){
         if(ids.stream().noneMatch(id -> "GFCM".equals(id.getSchemeID()))){
             return true;
        }
        List<IDType> collectedIds = ids.stream().filter(id -> "GFCM".equals(id.getSchemeID())).collect(Collectors.toList());

        if(collectedIds.get(0) == null ){
            return  true;
        }

        if(asset.getGfcm() == null){
            return false;
        }
        return  asset.getGfcm().equals(collectedIds.get(0).getValue());
    }

    public boolean hasValidUVI(Asset asset,List<IDType> ids){
        if(ids.stream().noneMatch(id -> "UVI".equals(id.getSchemeID()))){
            return true;
        }
        List<IDType> collectedIds = ids.stream().filter(id -> "UVI".equals(id.getSchemeID())).collect(Collectors.toList());

        if(collectedIds.get(0) == null){
            return  true;
        }

        if (asset.getUvi() == null) {
            return false;
        }
        return  asset.getUvi().equals(collectedIds.get(0).getValue());
    }

    public boolean hasValidICCAT(Asset asset,List<IDType> ids){
        if(ids.stream().noneMatch(id -> "ICCAT".equals(id.getSchemeID()))){
            return true;
        }
        List<IDType> collectedIds = ids.stream().filter(id -> "ICCAT".equals(id.getSchemeID())).collect(Collectors.toList());

        if(collectedIds.get(0) == null ){
            return  true;
        }
        if (asset.getIccat() == null) {
            return false;
        }

        return  asset.getIccat().equals(collectedIds.get(0).getValue());
    }

    public boolean schemeIdMatchesAsset(Asset asset,List<IDType> ids){

        List<IDType> collectedExtMarking = ids.stream().filter(id -> "EXT_MARK".equals(id.getSchemeID())).filter(t -> t.getValue().equals(asset.getExternalMarking())).collect(Collectors.toList());
        List<IDType> collectedIrcs = ids.stream().filter(id -> "IRCS".equals(id.getSchemeID())).filter(t -> t.getValue().equals(asset.getIrcs())).collect(Collectors.toList());
        return !collectedExtMarking.isEmpty() && !collectedIrcs.isEmpty();
    }

    public boolean hasCorrectCountry(Asset asset, String countryCode){
        if(asset == null || asset.getCountryCode() == null ){
            return false;
        }

        return countryCode.equals(asset.getCountryCode());
    }
    @Override
    public void setFactType() {
        this.factType = FactType.MOVEMENT_VESSEL_TRANSPORT_MEANS;
    }
}
