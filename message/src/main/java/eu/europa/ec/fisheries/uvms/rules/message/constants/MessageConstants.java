package eu.europa.ec.fisheries.uvms.rules.message.constants;

public class MessageConstants {

    private MessageConstants(){}

    public static final String CONNECTION_FACTORY = "java:/ConnectionFactory";
    public static final String CONNECTION_TYPE = "javax.jms.MessageListener";
    public static final String DESTINATION_TYPE_QUEUE = "javax.jms.Queue";
    public static final String RULES_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSRulesEvent";
    public static final String RULES_MESSAGE_IN_QUEUE_NAME = "UVMSRulesEvent";
    public static final String RULES_RESPONSE_QUEUE = "java:/jms/queue/UVMSRules";
    public static final String QUEUE_DATASOURCE_INTERNAL = "java:/jms/queue/UVMSRulesModel";

    public static final String MOVEMENT_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSMovementEvent";
    public static final String ASSET_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSAssetEvent";
    public static final String MOBILE_TERMINAL_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSMobileTerminalEvent";
    public static final String EXCHANGE_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSExchangeEvent";
    public static final String USER_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSUserEvent";
    public static final String AUDIT_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSAuditEvent";
    public static final String ACTIVITY_MESSAGE_IN_QUEUE = "java:/jms/queue/UVMSActivityEvent";

    public static final String MDC_IDENTIFIER = "clientName";

    public static final String MODULE_NAME = "rules";
}
