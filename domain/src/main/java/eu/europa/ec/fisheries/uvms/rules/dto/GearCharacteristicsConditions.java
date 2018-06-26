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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Singleton
@Startup
public class FishingGearTypeCharacteristics {

    @Getter
    private List<FishingGearTypeCharacteristic> characteristicList = new ArrayList<>();

    @Getter
    private Map<String, List<Attribute>> characteristicMap = new HashMap<>();

    @PostConstruct
    public void init(){

        characteristicMap.put("OTB", Arrays.asList(
                new Attribute("ME", false),
                new Attribute("GM", true),
                new Attribute("MT", true))
        );
        characteristicMap.put("SV", Arrays.asList(
                new Attribute("GM", false),
                new Attribute("ME", false))
        );
        characteristicMap.put("OTM", Arrays.asList(
                new Attribute("MT", true),
                new Attribute("ME", true))
        );
        characteristicMap.put("PTM", Arrays.asList(
                new Attribute("ME", true),
                new Attribute("MT", true))
        );
        characteristicMap.put("PTB", Arrays.asList(
                new Attribute("GM", true),
                new Attribute("MT", true),
                new Attribute("ME", true))
        );
        characteristicMap.put("TBN", Arrays.asList(
                new Attribute("GM", false),
                new Attribute("ME", true),
                new Attribute("MT", false))
        );
        characteristicMap.put("TBS", Arrays.asList(
                new Attribute("GM", false),
                new Attribute("MT", false),
                new Attribute("ME", true))
        );
        characteristicMap.put("TB", Arrays.asList(
                new Attribute("GM", false),
                new Attribute("ME", true),
                new Attribute("MT", false))
        );
        characteristicMap.put("SX", Arrays.asList(
                new Attribute("GM", true),
                new Attribute("ME", true))
        );
        characteristicMap.put("TBB", Arrays.asList(
                new Attribute("GM", true),
                new Attribute("ME", true),
                new Attribute("GN", true))
        );
        characteristicMap.put("OTT", Arrays.asList(
                new Attribute("GM", false),
                new Attribute("ME", true),
                new Attribute("MT", false),
                new Attribute("GN", true))
        );
        characteristicMap.put("SDN", Arrays.asList(
                new Attribute("ME", true),
                new Attribute("GM", true))
        );
        characteristicMap.put("SPR", Arrays.asList(
                new Attribute("GM", true),
                new Attribute("ME", true))
        );
        characteristicMap.put("SSC", Arrays.asList(
                new Attribute("GM", true),
                new Attribute("ME", true))
        );
        characteristicMap.put("PS", Arrays.asList(
                new Attribute("GM", true),
                new Attribute("HE", true),
                new Attribute("ME", true))
        );
        characteristicMap.put("PS1", Arrays.asList(
                new Attribute("ME", true),
                new Attribute("HE", true),
                new Attribute("GM", true))
        );
        characteristicMap.put("PS2", Arrays.asList(
                new Attribute("ME", true),
                new Attribute("HE", true),
                new Attribute("GM", true))
        );

        characteristicList.add(new FishingGearTypeCharacteristic("LA","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("LA","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("LA","HE", true));
        characteristicList.add(new FishingGearTypeCharacteristic("DRB","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("DRB","GN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GN","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GN","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GN","HE", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GN","NL", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GN","NN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GN","QG", false));
        characteristicList.add(new FishingGearTypeCharacteristic("GNS","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GNS","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GNS","HE", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GNS","NL", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GNS","NN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GNS","QG", false));
        characteristicList.add(new FishingGearTypeCharacteristic("GND","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GND","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GND","HE", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GND","NL", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GND","NN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GND","QG", false));
        characteristicList.add(new FishingGearTypeCharacteristic("GNC","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GNC","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GNC","HE", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GNC","NL", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GNC","NN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GNC","QG", false));
        characteristicList.add(new FishingGearTypeCharacteristic("GTN","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GTN","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GTN","HE", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GTN","NL", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GTN","NN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GTN","QG", false));
        characteristicList.add(new FishingGearTypeCharacteristic("GTR","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GTR","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GTR","HE", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GTR","NL", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GTR","NN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("GTR","QG", false));
        characteristicList.add(new FishingGearTypeCharacteristic("LHP","NI", true));
        characteristicList.add(new FishingGearTypeCharacteristic("FPO","GN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("LHP","GN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("LHM","GN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("LHM","NI", true));
        characteristicList.add(new FishingGearTypeCharacteristic("LLS","GN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("LLS","NI", true));
        characteristicList.add(new FishingGearTypeCharacteristic("LLD","GN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("LLD","NI", true));
        characteristicList.add(new FishingGearTypeCharacteristic("LL","GN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("LL","NI", true));
        characteristicList.add(new FishingGearTypeCharacteristic("RG","GD", false));
    }

    @Data
    @AllArgsConstructor
    public class Attribute {
        private String value;
        private boolean optional;
    }
}
