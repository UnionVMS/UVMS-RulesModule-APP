package eu.europa.ec.fisheries.uvms.rules.message.constants;

public class MessageConstants {

    public static final String CONNECTION_FACTORY = "java:/ConnectionFactory";
    public static final String CONNECTION_TYPE = "javax.jms.MessageListener";
    public static final String DESTINATION_TYPE_QUEUE = "javax.jms.Queue";
    public static final String RULES_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSRulesEvent";
    public static final String RULES_MESSAGE_IN_QUEUE_NAME = "UVMSRulesEvent";
    public static final String COMPONENT_RESPONSE_QUEUE = "java:/jms/queue/UVMSRules";
    public static final String QUEUE_DATASOURCE_INTERNAL = "java:/jms/queue/UVMSRulesModel";

}
