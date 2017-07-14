package eu.europa.ec.fisheries.uvms.rules.service.constants;

/**
 * Created by dordevdr on 7/5/2017.
 */
public enum FishingGearTypeCode {
    OTB("OTB"), TBN("TBN"), TBS("TBS"), TB("TB"), TBB("TBB"), OTT("OTT"),
    PTB("PTB"), OTM("OTM"), PTM("PTM"), SDN("SDN"), SSC("SSC"), SPR("SPR"),
    SX("SX"), SV("SV"), PS("PS"), PS1("PS1"), PS2("PS2"), LA("LA"), DRB("DRB"),
    GN("GN"), GNS("GNS"), GND("GND"), GNC("GNC"), GTN("GTN"), GTR("GTR"),
    FPO("FPO"), FIX("FIX"), LHP("LHP"), LHM("LHM"), LLS("LLS"), LLD("LLD"),
    LL("LL"), LTL("LTL"), LX("LX"), HMD("HMD"), MIS("MIS"), RG("RG"), NK("NK");

    private String value;

    private FishingGearTypeCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }
}
