package eu.europa.ec.fisheries.uvms.rules.service.business;

public class ErrorEvent {
    private int id;
    private RawMovementFact rawFact;
    private String ruleName;

    public ErrorEvent() {
    }

    public ErrorEvent(RawMovementFact fact) {
        this.rawFact = fact;
    }

    public ErrorEvent(String ruleName, RawMovementFact fact) {
        this.ruleName = ruleName;
        this.rawFact = fact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RawMovementFact getRawFact() {
        return rawFact;
    }

    public void setRawFact(RawMovementFact rawFact) {
        this.rawFact = rawFact;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

}
