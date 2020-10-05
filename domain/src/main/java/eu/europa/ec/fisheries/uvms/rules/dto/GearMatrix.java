/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.*;

@Singleton
@Startup
public class GearMatrix {

    @Getter
    private Map<String, List<Condition>> matrix = new HashMap<>();

    @Getter
    private Map<String, List<Condition>> neafcMatrix = new HashMap<>();

    @PostConstruct
    public void init() {
        fillGearMetrix();
        fillNEAFCGearMetrix();
    }

    private void fillNEAFCGearMetrix() {
        ////////// NEAFCT Related Matrix

        // Surrounding Nets
        neafcMatrix.put("PS", Collections.emptyList());
        neafcMatrix.put("PS1", Collections.emptyList());
        neafcMatrix.put("PS2", Collections.emptyList());
        neafcMatrix.put("LA", Collections.emptyList());
        neafcMatrix.put("SUX", Collections.emptyList());

        // SEINES
        neafcMatrix.put("SB", Collections.emptyList());
        neafcMatrix.put("SV", Collections.emptyList());
        neafcMatrix.put("SDN", Collections.emptyList());
        neafcMatrix.put("SSC", Collections.emptyList());
        neafcMatrix.put("SPR", Collections.emptyList());
        neafcMatrix.put("SX", Collections.emptyList());

        // TRAWLS
        neafcMatrix.put("TBB", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("OTB", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("OT", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("OTT", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("OTP", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("PTB", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("PT", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("TB", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("TBN", Arrays.asList(
                new Condition("GN", false),
                new Condition("", false))
        );
        neafcMatrix.put("TBS", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("PUK", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("PUL", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("OTM", Arrays.asList(
                new Condition("GN", false),
                new Condition("ME", false))
        );
        neafcMatrix.put("PTM", Arrays.asList(
                new Condition("GN", false),
                new Condition("ME", false))
        );
        neafcMatrix.put("TM", Arrays.asList(
                new Condition("GN", false),
                new Condition("ME", false))
        );
        neafcMatrix.put("TMS", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("TSP", Arrays.asList(
                new Condition("GN", false),
                new Condition("ME", false))
        );
        neafcMatrix.put("TX", Arrays.asList(
                new Condition("GN", false))
        );

        // BREDGES
        neafcMatrix.put("DRB", Arrays.asList(
                new Condition("GN", false))
        );

        neafcMatrix.put("DRH", Arrays.asList(
                new Condition("GN", false))
        );

        neafcMatrix.put("DRM", Arrays.asList(
                new Condition("GN", false))
        );

        neafcMatrix.put("DRX", Arrays.asList(
                new Condition("GN", false))
        );

        // LIFT NETS
        neafcMatrix.put("LNP", Collections.emptyList());
        neafcMatrix.put("LNB", Collections.emptyList());
        neafcMatrix.put("LNS", Collections.emptyList());
        neafcMatrix.put("LN", Collections.emptyList());

        // FALLING GEAR
        neafcMatrix.put("FCN", Collections.emptyList());
        neafcMatrix.put("FCO", Collections.emptyList());
        neafcMatrix.put("FG", Collections.emptyList());

        // GILLNETS AND ENTANGLING NETS
        neafcMatrix.put("GNS", Arrays.asList(
                new Condition("GM", false))
        );
        neafcMatrix.put("GND", Arrays.asList(
                new Condition("GM", false))
        );
        neafcMatrix.put("GNC", Arrays.asList(
                new Condition("GM", false))
        );
        neafcMatrix.put("GNF", Arrays.asList(
                new Condition("GM", false))
        );
        neafcMatrix.put("GTR", Arrays.asList(
                new Condition("GM", false))
        );
        neafcMatrix.put("GTN", Arrays.asList(
                new Condition("GM", false))
        );
        neafcMatrix.put("GEN", Arrays.asList(
                new Condition("GM", false))
        );
        neafcMatrix.put("GN", Arrays.asList(
                new Condition("GM", false))
        );

        // TRAPS
        neafcMatrix.put("FPN", Collections.emptyList());
        neafcMatrix.put("FPO", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("FYK", Collections.emptyList());
        neafcMatrix.put("FSN", Collections.emptyList());
        neafcMatrix.put("FWR", Collections.emptyList());
        neafcMatrix.put("FAR", Collections.emptyList());
        neafcMatrix.put("FIX", Collections.emptyList());

        // HOOKS AND LINES
        neafcMatrix.put("LHP", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("LHM", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("LLS", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("LLD", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("LL", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("LVT", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("LTL", Arrays.asList(
                new Condition("GN", false))
        );
        neafcMatrix.put("LX", Arrays.asList(
                new Condition("GN", false))
        );

        // MISCELLANEOUS GEAR
        neafcMatrix.put("HAR", Collections.emptyList());
        neafcMatrix.put("MHI", Collections.emptyList());
        neafcMatrix.put("MPM", Collections.emptyList());
        neafcMatrix.put("MEL", Collections.emptyList());
        neafcMatrix.put("MPN", Collections.emptyList());
        neafcMatrix.put("MSP", Collections.emptyList());
        neafcMatrix.put("MDR", Collections.emptyList());
        neafcMatrix.put("MDV", Collections.emptyList());
        neafcMatrix.put("MIS", Collections.emptyList());
        neafcMatrix.put("HMX", Collections.emptyList());
        neafcMatrix.put("RG", Collections.emptyList());

        // GEAR NOT KNOWN
        neafcMatrix.put("NK", Collections.emptyList());
    }

    private void fillGearMetrix() {

        // Trawl nets
        matrix.put("TBB", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("GN", false))
        );
        matrix.put("OTB", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("MT", true))
        );
        matrix.put("OT", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("MT", true))
        );
        matrix.put("OTT", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("GN", false),
                new Condition("MT", true))
        );
        matrix.put("OTP", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("MT", true))
        );
        matrix.put("PTB", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("MT", true))
        );
        matrix.put("PT", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", true),
                new Condition("MT", true))
        );
        matrix.put("TB", Arrays.asList(
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
        matrix.put("PUK", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("GN", false))
        );
        matrix.put("PUL", Arrays.asList(
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
        matrix.put("TM", Arrays.asList(
                new Condition("ME", false),
                new Condition("MT", false))
        );
        matrix.put("TMS", Arrays.asList(
                new Condition("ME", false),
                new Condition("MT", false))
        );
        matrix.put("TSP", Arrays.asList(
                new Condition("ME", false),
                new Condition("MT", false))
        );
        matrix.put("TX", Arrays.asList(
                new Condition("ME", false))
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
        matrix.put("SB", Arrays.asList(
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
        matrix.put("SUX", Arrays.asList(
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
        matrix.put("GNF", Arrays.asList(
                new Condition("ME", false),
                new Condition("GM", false),
                new Condition("HE", false),
                new Condition("NL", false),
                new Condition("NN", false),
                new Condition("QG", true))
        );
        matrix.put("GEN", Arrays.asList(
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
    }

    @Data
    @AllArgsConstructor
    public class Condition {
        private String value;
        private boolean optional;
    }
}
