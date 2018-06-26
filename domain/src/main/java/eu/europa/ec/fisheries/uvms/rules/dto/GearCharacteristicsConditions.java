/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.dto;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Singleton
@Startup
public class GearCharacteristicsConditions {

    @Getter
    private List<FishingGearTypeCharacteristic> characteristicList = new ArrayList<>();

    @Getter
    private Map<String, List<Condition>> characteristicMap = new HashMap<>();

    @PostConstruct
    public void init(){

        // Trawl nets

        characteristicMap.put("OTB", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("MT", true))
        );
        characteristicMap.put("TBN", Arrays.asList(
                new Condition("ME", true),
                new Condition("GM", false),
                new Condition("MT", false))
        );
        characteristicMap.put("TBS", Arrays.asList(
                new Condition("ME", true),
                new Condition("GM", false),
                new Condition("MT", false))
        );
        characteristicMap.put("TB", Arrays.asList(
                new Condition("ME", true),
                new Condition("GM", false),
                new Condition("MT", false))
        );
        characteristicMap.put("TBB", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("GN", false))
        );
        characteristicMap.put("OTT", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("GN", false),
                new Condition("MT", true))
        );
        characteristicMap.put("PTB", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("MT", true))
        );
        characteristicMap.put("OTM", Arrays.asList(
                new Condition("MT", false),
                new Condition("ME", false))
        );
        characteristicMap.put("PTM", Arrays.asList(
                new Condition("ME", false),
                new Condition("MT", false))
        );

        // Seine nets
        characteristicMap.put("SDN", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false))
        );
        characteristicMap.put("SSC", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false))
        );
        characteristicMap.put("SPR", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false))
        );
        characteristicMap.put("SX", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false))
        );
        characteristicMap.put("SV", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false))
        );

        // Surrounding nets
        characteristicMap.put("PS", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false))
        );
        characteristicMap.put("PS1", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false))
        );
        characteristicMap.put("PS2", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false))
        );
        characteristicMap.put("LA", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false))
        );

        // Dredges
        characteristicMap.put("DRB", Arrays.asList(
                new Condition("GM", false),
                new Condition("GN", false))
        );

        // Gillnets && entangling nets
        characteristicMap.put("GN", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );
        characteristicMap.put("GNS", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );
        characteristicMap.put("GND", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );
        characteristicMap.put("GNC", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );
        characteristicMap.put("GTN", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );
        characteristicMap.put("GTR", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );

        // Traps
        characteristicMap.put("FPO",
                Collections.singletonList(new Condition("GN", false))
        );
        characteristicMap.put("FIX", Collections.<Condition>emptyList());

        // Hooks & Lines
        characteristicMap.put("LHP", Arrays.asList(
                new Condition("GN", false),
                new Condition("NI", false))
        );
        characteristicMap.put("LHM", Arrays.asList(
                new Condition("GN", false),
                new Condition("NI", false))
        );
        characteristicMap.put("LLS", Arrays.asList(
                new Condition("GN", false),
                new Condition("NI", false))
        );
        characteristicMap.put("LLD", Arrays.asList(
                new Condition("GN", false),
                new Condition("NI", false))
        );
        characteristicMap.put("LL", Arrays.asList(
                new Condition("GN", false),
                new Condition("NI", false))
        );
        characteristicMap.put("LTL", Collections.<Condition>emptyList());
        characteristicMap.put("LX", Collections.<Condition>emptyList());

        // Harvesting machines
        characteristicMap.put("HMD", Collections.<Condition>emptyList());

        // Miscellaneous gear
        characteristicMap.put("MIS", Collections.<Condition>emptyList());

        // Recreational gear
        characteristicMap.put("RG", Collections.<Condition>emptyList());

        // Gear not known or not specified
        characteristicMap.put("NK", Collections.<Condition>emptyList());
    }

    @Data
    @AllArgsConstructor
    public class Condition {
        private String value;
        private boolean optional;
    }
}
