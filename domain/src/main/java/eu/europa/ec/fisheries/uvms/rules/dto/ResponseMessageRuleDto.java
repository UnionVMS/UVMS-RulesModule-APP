package eu.europa.ec.fisheries.uvms.rules.dto;

import eu.europa.ec.fisheries.uvms.rules.entity.ResponseMessageRule;


public class ResponseMessageRuleDto {

    private String dataFlow;
    private String messageType;
    private String receiver;


    public ResponseMessageRuleDto(String dataFlow, String messageType, String receiver) {
        this.dataFlow = dataFlow;
        this.messageType = messageType;
        this.receiver = receiver;
    }

    public ResponseMessageRuleDto(ResponseMessageRule rule) {
        this.dataFlow = rule.getDataFlow();
        this.messageType = rule.getMessageType();
        this.receiver = rule.getReceiver();
    }

    public String getDataFlow() {
        return dataFlow;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getReceiver() {
        return receiver;
    }

}
