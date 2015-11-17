package eu.europa.ec.fisheries.uvms.rules.model.constant;

public enum FaultCode {
	RULES_MESSAGE(3700),
	RULES_TOPIC_MESSAGE(3701),
	RULES_EVENT_SERVICE(3201),

	RULES_MODEL_EXCEPTION(3800),
	RULES_MAPPER(3810),
	RULES_MARSHALL_EXCEPTION(3811),

	RULES_COMMAND_INVALID(3120),
	RULES_PLUGIN_INVALID(3205);
	
	private final int code;
	
	FaultCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}
