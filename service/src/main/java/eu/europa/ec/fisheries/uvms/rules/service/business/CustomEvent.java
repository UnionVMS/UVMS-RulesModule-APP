package eu.europa.ec.fisheries.uvms.rules.service.business;

public class CustomEvent {
    private int id;
    private PositionEvent positionEvent;
    private String action;

    public CustomEvent() {
    }

    public CustomEvent(PositionEvent p) {
        this.positionEvent = p;
    }

    public CustomEvent(PositionEvent p, String action) {
        this.positionEvent = p;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PositionEvent getPositionEvent() {
        return positionEvent;
    }

    public void setPositionEvent(PositionEvent positionEvent) {
        this.positionEvent = positionEvent;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
