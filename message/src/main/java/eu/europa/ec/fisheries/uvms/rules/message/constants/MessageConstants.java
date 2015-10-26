package eu.europa.ec.fisheries.uvms.rules.message.constants;

public class MessageConstants {

    public static final String CONNECTION_FACTORY = "java:/ConnectionFactory";
    public static final String CONNECTION_TYPE = "javax.jms.MessageListener";
    public static final String DESTINATION_TYPE_QUEUE = "javax.jms.Queue";
    public static final String RULES_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSRulesEvent";
    public static final String RULES_MESSAGE_IN_QUEUE_NAME = "UVMSRulesEvent";
    public static final String RULES_RESPONSE_QUEUE = "java:/jms/queue/UVMSRules";
    public static final String QUEUE_DATASOURCE_INTERNAL = "java:/jms/queue/UVMSRulesModel";

    public static final String MOVEMENT_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSMovementEvent";
    public static final String VESSEL_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSVesselEvent";
    public static final String MOBILE_TERMINAL_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSMobileTerminalEvent";

    public static final String MODULE_NAME = "rules";
}
