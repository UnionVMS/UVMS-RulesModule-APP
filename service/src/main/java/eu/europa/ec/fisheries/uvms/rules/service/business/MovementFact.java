package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.Date;

import eu.europa.ec.fisheries.schema.movement.asset.v1.AssetIdType;
import eu.europa.ec.fisheries.schema.movement.asset.v1.AssetType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;

public class MovementFact {

    private MovementType movementType;

    // TODO: Not necessary???
    private boolean ok = true;

    // Helper
    public Date getTimestamp() {
        return this.getMovementType().getPositionTime().toGregorianCalendar().getTime();
    }

    // Helper
    public String getVesselCfr() {
        if (this.movementType.getAssetId().getAssetType() == AssetType.VESSEL && this.movementType.getAssetId().getIdType() == AssetIdType.CFR) {
            return this.movementType.getAssetId().getValue();
        }
        return null;
    }

    // Helper
    public String getVesselIrcs() {
        if (this.movementType.getAssetId().getAssetType() == AssetType.VESSEL && this.movementType.getAssetId().getIdType() == AssetIdType.IRCS) {
            return this.movementType.getAssetId().getValue();
        }
        return null;
    }

    // Helper
    public String getVesselName() {
        return null;
    }

    // Helper
    public String getMobileTerminalMemberNumber() {
        return "ABC99";
        // return null;
    }

    // Helper
    // TODO: Where do I get mobile terminal serial number??????????????????
    public String getMobileTerminalSerialNumber() {
        return null;
    }

    // Helper
    public String getMobileTerminalDnid() {
        return null;
    }

    // Helper
    public String getGeoAreaAreaId() {
        return null;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    @Override
    public String toString() {
        return "MovementFact [getTimestamp()=" + getTimestamp() + ", getVesselCfr()=" + getVesselCfr() + ", getVesselIrcs()=" + getVesselIrcs()
                + ", getVesselName()=" + getVesselName() + ", getMobileTerminalMemberNumber()=" + getMobileTerminalMemberNumber()
                + ", getMobileTerminalSerialNumber()=" + getMobileTerminalSerialNumber() + ", getMobileTerminalDnid()=" + getMobileTerminalDnid()
                + ", getGeoAreaAreaId()=" + getGeoAreaAreaId() + ", getMovementType()=" + getMovementType() + ", isOk()=" + isOk() + "]";
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

}
