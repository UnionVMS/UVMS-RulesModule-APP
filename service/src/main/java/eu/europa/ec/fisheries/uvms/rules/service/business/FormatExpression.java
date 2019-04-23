package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.Date;

public class FormatExpression {

    private String expression;
    private Date startDate;
    private Date endDate;

    public FormatExpression(String expression, Date startDate, Date endDate) {
        this.expression = expression;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
