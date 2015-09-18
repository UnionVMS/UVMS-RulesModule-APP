package eu.europa.ec.fisheries.uvms.rules.service.business;

public class CustomEvent {
    private int id;
    private PositionFact positionFact;
    private String action;

    public CustomEvent() {
    }

    public CustomEvent(PositionFact p) {
        this.positionFact = p;
    }

    public CustomEvent(PositionFact p, String action) {
        this.positionFact = p;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PositionFact getPositionFact() {
        return positionFact;
    }

    public void setPositionFact(PositionFact positionFact) {
        this.positionFact = positionFact;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
