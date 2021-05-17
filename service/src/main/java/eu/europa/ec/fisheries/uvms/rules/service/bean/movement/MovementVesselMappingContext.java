package eu.europa.ec.fisheries.uvms.rules.service.bean.movement;

import java.util.HashMap;
import java.util.Map;

import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselTransportMeansType;

public class MovementVesselMappingContext {
    
    private Map<VesselTransportMeansType, RawMovementType> vesselTransportMeansToRawMovement = new HashMap<>();
    
    private Map<RawMovementType, Asset> rawMovementToAsset = new HashMap<>();
    
    public void put(VesselTransportMeansType vesselTransportMeansType, RawMovementType rawMovementType) {
        vesselTransportMeansToRawMovement.put(vesselTransportMeansType, rawMovementType);
    }
    
    public void put(RawMovementType rawMovementType, Asset asset) {
        rawMovementToAsset.put(rawMovementType, asset);        
    }
    
    public RawMovementType getRawMovement(VesselTransportMeansType vesselTransportMeansType) {
        return vesselTransportMeansToRawMovement.get(vesselTransportMeansType);
    }
    
    public Asset getAsset(RawMovementType rawMovementType) {
        return rawMovementToAsset.get(rawMovementType);
    }
}
