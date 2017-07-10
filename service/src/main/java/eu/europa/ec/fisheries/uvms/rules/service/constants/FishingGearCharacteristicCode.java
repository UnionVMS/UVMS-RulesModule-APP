package eu.europa.ec.fisheries.uvms.rules.service.constants;

/**
 * Created by dordevdr on 7/5/2017.
 */
public enum FishingGearCharacteristicCode {
    ME("ME"), GM("GM"), HE("HE"), NL("NL"), GN("GN"), NI("NI"), NN("NN"),
    QG("QG"), MT("MT"), GD("GD");

    private String value;

    private FishingGearCharacteristicCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }
}
