package eu.europa.ec.fisheries.uvms.rules.service.business;

public class CustomEvent {
    private int id;
    private MovementFact movementFact;
    private String action;
    private String ruleName;

    public CustomEvent() {
    }

    public CustomEvent(MovementFact fact) {
        this.movementFact = fact;
    }

    public CustomEvent(String ruleName, MovementFact fact, String action) {
        this.ruleName = ruleName;
        this.movementFact = fact;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MovementFact getMovementFact() {
        return movementFact;
    }

    public void setMovementFact(MovementFact fact) {
        this.movementFact = fact;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

}
