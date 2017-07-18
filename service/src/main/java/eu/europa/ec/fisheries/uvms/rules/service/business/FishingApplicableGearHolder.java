package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingGearCharacteristicCode;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingGearTypeCode;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dordevdr on 7/5/2017.
 */
public class FishingApplicableGearHolder {
    private HashMap<FishingGearTypeCode, List<FishingGearCharacteristicCode>> holderMap = new HashMap<>();

    private FishingApplicableGearHolder() {
        init();
    }

    private void init() {
        holderMap.put(FishingGearTypeCode.OTB, Arrays.asList(FishingGearCharacteristicCode.ME,
                FishingGearCharacteristicCode.GM, FishingGearCharacteristicCode.MT));
        holderMap.put(FishingGearTypeCode.TBN, Arrays.asList(FishingGearCharacteristicCode.ME));
        holderMap.put(FishingGearTypeCode.TBS, Arrays.asList(FishingGearCharacteristicCode.ME));
        holderMap.put(FishingGearTypeCode.TB, Arrays.asList(FishingGearCharacteristicCode.ME));
        holderMap.put(FishingGearTypeCode.TBB, Arrays.asList(FishingGearCharacteristicCode.ME,
                FishingGearCharacteristicCode.GM, FishingGearCharacteristicCode.GN));
        holderMap.put(FishingGearTypeCode.OTT, Arrays.asList(FishingGearCharacteristicCode.ME,
                FishingGearCharacteristicCode.GM, FishingGearCharacteristicCode.GN, FishingGearCharacteristicCode.MT));
        holderMap.put(FishingGearTypeCode.PTB, Arrays.asList(FishingGearCharacteristicCode.ME,
                FishingGearCharacteristicCode.GM, FishingGearCharacteristicCode.MT));
        holderMap.put(FishingGearTypeCode.OTM, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.MT));
        holderMap.put(FishingGearTypeCode.PTM, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.MT));
        holderMap.put(FishingGearTypeCode.SDN, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.GM));
        holderMap.put(FishingGearTypeCode.SSC, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.GM));
        holderMap.put(FishingGearTypeCode.SPR, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.GM));
        holderMap.put(FishingGearTypeCode.SX, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.GM));
        holderMap.put(FishingGearTypeCode.SV, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.GM));
        holderMap.put(FishingGearTypeCode.PS, Arrays.asList(FishingGearCharacteristicCode.ME,
                FishingGearCharacteristicCode.GM, FishingGearCharacteristicCode.HE));
        holderMap.put(FishingGearTypeCode.PS1, Arrays.asList(FishingGearCharacteristicCode.ME,
                FishingGearCharacteristicCode.GM, FishingGearCharacteristicCode.HE));
        holderMap.put(FishingGearTypeCode.PS2, Arrays.asList(FishingGearCharacteristicCode.ME,
                FishingGearCharacteristicCode.GM, FishingGearCharacteristicCode.HE));
        holderMap.put(FishingGearTypeCode.LA, Arrays.asList(FishingGearCharacteristicCode.ME,
                FishingGearCharacteristicCode.GM, FishingGearCharacteristicCode.HE));
        holderMap.put(FishingGearTypeCode.DRB, Arrays.asList(FishingGearCharacteristicCode.GM, FishingGearCharacteristicCode.GN));
        holderMap.put(FishingGearTypeCode.GN, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.GM,
                FishingGearCharacteristicCode.HE, FishingGearCharacteristicCode.NL, FishingGearCharacteristicCode.NN, FishingGearCharacteristicCode.QG));
        holderMap.put(FishingGearTypeCode.GNS, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.GM,
                FishingGearCharacteristicCode.HE, FishingGearCharacteristicCode.NL, FishingGearCharacteristicCode.NN, FishingGearCharacteristicCode.QG));
        holderMap.put(FishingGearTypeCode.GND, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.GM,
                FishingGearCharacteristicCode.HE, FishingGearCharacteristicCode.NL, FishingGearCharacteristicCode.NN, FishingGearCharacteristicCode.QG));
        holderMap.put(FishingGearTypeCode.GNC, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.GM,
                FishingGearCharacteristicCode.HE, FishingGearCharacteristicCode.NL, FishingGearCharacteristicCode.NN, FishingGearCharacteristicCode.QG));
        holderMap.put(FishingGearTypeCode.GTN, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.GM,
                FishingGearCharacteristicCode.HE, FishingGearCharacteristicCode.NL, FishingGearCharacteristicCode.NN, FishingGearCharacteristicCode.QG));
        holderMap.put(FishingGearTypeCode.GTR, Arrays.asList(FishingGearCharacteristicCode.ME, FishingGearCharacteristicCode.GM,
                FishingGearCharacteristicCode.HE, FishingGearCharacteristicCode.NL, FishingGearCharacteristicCode.NN, FishingGearCharacteristicCode.QG));
        holderMap.put(FishingGearTypeCode.FPO, Arrays.asList(FishingGearCharacteristicCode.GN));
        holderMap.put(FishingGearTypeCode.FIX, Collections.<FishingGearCharacteristicCode>emptyList());
        holderMap.put(FishingGearTypeCode.LHP, Arrays.asList(FishingGearCharacteristicCode.GN, FishingGearCharacteristicCode.NI));
        holderMap.put(FishingGearTypeCode.LHM, Arrays.asList(FishingGearCharacteristicCode.GN, FishingGearCharacteristicCode.NI));
        holderMap.put(FishingGearTypeCode.LLS, Arrays.asList(FishingGearCharacteristicCode.GN, FishingGearCharacteristicCode.NI));
        holderMap.put(FishingGearTypeCode.LLD, Arrays.asList(FishingGearCharacteristicCode.GN, FishingGearCharacteristicCode.NI));
        holderMap.put(FishingGearTypeCode.LL, Arrays.asList(FishingGearCharacteristicCode.GN, FishingGearCharacteristicCode.NI));
        holderMap.put(FishingGearTypeCode.LTL, Collections.<FishingGearCharacteristicCode>emptyList());
        holderMap.put(FishingGearTypeCode.LX, Collections.<FishingGearCharacteristicCode>emptyList());
        holderMap.put(FishingGearTypeCode.HMD, Collections.<FishingGearCharacteristicCode>emptyList());
        holderMap.put(FishingGearTypeCode.MIS, Collections.<FishingGearCharacteristicCode>emptyList());
        holderMap.put(FishingGearTypeCode.RG, Collections.<FishingGearCharacteristicCode>emptyList());
        holderMap.put(FishingGearTypeCode.NK, Collections.<FishingGearCharacteristicCode>emptyList());
    }

    public static FishingApplicableGearHolder getInstance() {
        return FishingApplicableGearHelper.FISHING_APPLICABLE_GEAR_HOLDER;
    }

    public List<FishingGearCharacteristicCode> getFishingGearCharacteristicCodes(FishingGearTypeCode fishingGearTypeCode) {
        return holderMap.get(fishingGearTypeCode);
    }

    private static class FishingApplicableGearHelper {
        private static final FishingApplicableGearHolder FISHING_APPLICABLE_GEAR_HOLDER = new FishingApplicableGearHolder();
    }

}
