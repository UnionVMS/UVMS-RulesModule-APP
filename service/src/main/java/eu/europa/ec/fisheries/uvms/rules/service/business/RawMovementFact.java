package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;

public class RawMovementFact {

    private RawMovementType rawMovementType;
    private boolean ok = true;

    public RawMovementType getRawMovementType() {
        return rawMovementType;
    }

    public void setRawMovementType(RawMovementType rawMovementType) {
        this.rawMovementType = rawMovementType;
    }

    // Getters
    public Double getLatitude() {
        return this.rawMovementType.getPosition().getLatitude();
    }

    public Double getLongitude() {
        return this.rawMovementType.getPosition().getLongitude();
    }

    public Double getAltitude() {
        return this.rawMovementType.getPosition().getAltitude();
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

}
