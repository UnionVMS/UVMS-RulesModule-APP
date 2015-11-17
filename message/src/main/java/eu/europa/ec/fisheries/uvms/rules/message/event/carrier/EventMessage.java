package eu.europa.ec.fisheries.uvms.rules.message.event.carrier;

import eu.europa.ec.fisheries.schema.rules.common.v1.RulesFault;

import javax.jms.TextMessage;

public class EventMessage {

    private TextMessage jmsMessage;
    private RulesFault fault;

    public EventMessage(TextMessage jmsMessage) {
        this.jmsMessage = jmsMessage;
    }

    public EventMessage(TextMessage jmsMessage, RulesFault fault) {
        this.jmsMessage = jmsMessage;
        this.fault = fault;
    }

    public RulesFault getFault() {
        return fault;
    }

    public void setFault(RulesFault fault) {
        this.fault = fault;
    }

    public TextMessage getJmsMessage() {
        return jmsMessage;
    }

    public void setJmsMessage(TextMessage jmsMessage) {
        this.jmsMessage = jmsMessage;
    }

}
