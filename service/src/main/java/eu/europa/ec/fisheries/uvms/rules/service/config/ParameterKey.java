package eu.europa.ec.fisheries.uvms.rules.service.config;

public enum ParameterKey {

    KEY("rules.key.attribute");

    private final String key;

    private ParameterKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
