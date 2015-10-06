package eu.europa.ec.fisheries.uvms.rules.service.business;

public class ErrorEvent {
    private int id;
    private RawFact rawFact;
    private String ruleName;

    public ErrorEvent() {
    }

    public ErrorEvent(RawFact fact) {
        this.rawFact = fact;
    }

    public ErrorEvent(String ruleName, RawFact fact) {
        this.ruleName = ruleName;
        this.rawFact = fact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RawFact getRawFact() {
        return rawFact;
    }

    public void setRawFact(RawFact rawFact) {
        this.rawFact = rawFact;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

}
