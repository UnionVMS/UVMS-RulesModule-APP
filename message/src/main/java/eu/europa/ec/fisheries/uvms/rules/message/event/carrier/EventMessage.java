package eu.europa.ec.fisheries.uvms.rules.message.event.carrier;

import javax.jms.TextMessage;

public class EventMessage {

    private TextMessage jmsMessage;
    private String errorMessage;

    public EventMessage(TextMessage jmsMessage) {
        this.jmsMessage = jmsMessage;
    }

    public EventMessage(TextMessage jmsMessage, String errorMessage) {
        this.jmsMessage = jmsMessage;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public TextMessage getJmsMessage() {
        return jmsMessage;
    }

    public void setJmsMessage(TextMessage jmsMessage) {
        this.jmsMessage = jmsMessage;
    }

}
