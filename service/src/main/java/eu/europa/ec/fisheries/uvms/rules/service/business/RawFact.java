package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;

public class RawFact {

    private MovementBaseType movementBaseType;
    private boolean ok = true;

    public MovementBaseType getMovementBaseType() {
        return movementBaseType;
    }

    public void setMovementBaseType(MovementBaseType movementBaseType) {
        this.movementBaseType = movementBaseType;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

}
