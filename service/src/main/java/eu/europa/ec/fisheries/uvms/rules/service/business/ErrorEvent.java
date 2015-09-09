package eu.europa.ec.fisheries.uvms.rules.service.business;

public class ErrorEvent {
    private int id;
    private PositionEvent positionEvent;
    private String comment;

    public ErrorEvent() {
    }

    public ErrorEvent(PositionEvent p) {
        this.positionEvent = p;
    }

    public ErrorEvent(PositionEvent p, String comment) {
        this.positionEvent = p;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
