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
public class GearMatrix {

    @Getter
    private Map<String, List<Condition>> matrix = new HashMap<>();

    @PostConstruct
    public void init(){

        // Trawl nets

        matrix.put("OTB", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("MT", true))
        );
        matrix.put("TBN", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("MT", true))
        );
        matrix.put("TBS", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("MT", true))
        );
        matrix.put("TB", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("MT", true))
        );
        matrix.put("TBB", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("GN", false))
        );
        matrix.put("OTT", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("GN", false),
                new Condition("MT", true))
        );
        matrix.put("PTB", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("MT", true))
        );
        matrix.put("OTM", Arrays.asList(
                new Condition("MT", false),
                new Condition("ME", false))
        );
        matrix.put("PTM", Arrays.asList(
                new Condition("ME", false),
                new Condition("MT", false))
        );

        // Seine nets
        matrix.put("SDN", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false))
        );
        matrix.put("SSC", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false))
        );
        matrix.put("SPR", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false))
        );
        matrix.put("SX", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false))
        );
        matrix.put("SV", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false))
        );

        // Surrounding nets
        matrix.put("PS", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false))
        );
        matrix.put("PS1", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false))
        );
        matrix.put("PS2", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false))
        );
        matrix.put("LA", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false))
        );

        // Dredges
        matrix.put("DRB", Arrays.asList(
                new Condition("GM", false),
                new Condition("GN", false))
        );

        // Gillnets && entangling nets
        matrix.put("GN", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );
        matrix.put("GNS", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );
        matrix.put("GND", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );
        matrix.put("GNC", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );
        matrix.put("GTN", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );
        matrix.put("GTR", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );

        // Traps
        matrix.put("FPO",
                Collections.singletonList(new Condition("GN", false))
        );
        matrix.put("FIX", Collections.<Condition>emptyList());

        // Hooks & Lines
        matrix.put("LHP", Arrays.asList(
                new Condition("GN", false),
                new Condition("NI", false))
        );
        matrix.put("LHM", Arrays.asList(
                new Condition("GN", false),
                new Condition("NI", false))
        );
        matrix.put("LLS", Arrays.asList(
                new Condition("GN", false),
                new Condition("NI", false))
        );
        matrix.put("LLD", Arrays.asList(
                new Condition("GN", false),
                new Condition("NI", false))
        );
        matrix.put("LL", Arrays.asList(
                new Condition("GN", false),
                new Condition("NI", false))
        );
        matrix.put("LTL", Collections.<Condition>emptyList());
        matrix.put("LX", Collections.<Condition>emptyList());

        // Harvesting machines
        matrix.put("HMD", Collections.<Condition>emptyList());

        // Miscellaneous gear
        matrix.put("MIS", Collections.<Condition>emptyList());

        // Recreational gear
        matrix.put("RG", Collections.<Condition>emptyList());

        // Gear not known or not specified
        matrix.put("NK", Collections.<Condition>emptyList());
    }

    @Data
    @AllArgsConstructor
    public class Condition {
        private String value;
        private boolean optional;
    }
}
