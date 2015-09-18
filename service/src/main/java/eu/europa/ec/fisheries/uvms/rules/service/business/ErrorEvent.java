package eu.europa.ec.fisheries.uvms.rules.service.business;

public class ErrorEvent {
    private int id;
    private PositionFact positionFact;
    private String comment;

    public ErrorEvent() {
    }

    public ErrorEvent(PositionFact p) {
        this.positionFact = p;
    }

    public ErrorEvent(PositionFact p, String comment) {
        this.positionFact = p;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
