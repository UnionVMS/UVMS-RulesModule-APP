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
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Singleton
@Startup
public class FishingGearTypeCharacteristics {

    @Getter
    private List<FishingGearTypeCharacteristic> characteristicList = new ArrayList<>();

    @PostConstruct
    public void init(){
        characteristicList.add(new FishingGearTypeCharacteristic("SV","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("SV","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("OTM","MT", true));
        characteristicList.add(new FishingGearTypeCharacteristic("OTM","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("PTM","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("PTM","MT", true));
        characteristicList.add(new FishingGearTypeCharacteristic("PTB","GM", false));
        characteristicList.add(new FishingGearTypeCharacteristic("PTB","MT", false));
        characteristicList.add(new FishingGearTypeCharacteristic("PTB","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("OTB","GM", false));
        characteristicList.add(new FishingGearTypeCharacteristic("OTB","GE", true));
        characteristicList.add(new FishingGearTypeCharacteristic("OTB","MT", false));
        characteristicList.add(new FishingGearTypeCharacteristic("TBN","GM", false));
        characteristicList.add(new FishingGearTypeCharacteristic("TBN","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("TBN","MT", false));
        characteristicList.add(new FishingGearTypeCharacteristic("TBS","GM", false));
        characteristicList.add(new FishingGearTypeCharacteristic("TBS","MT", false));
        characteristicList.add(new FishingGearTypeCharacteristic("TBS","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("TB","GM", false));
        characteristicList.add(new FishingGearTypeCharacteristic("TB","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("TB","MT", false));
        characteristicList.add(new FishingGearTypeCharacteristic("SX","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("SX","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("TBB","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("TBB","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("TBB","GN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("OTT","GM", false));
        characteristicList.add(new FishingGearTypeCharacteristic("OTT","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("OTT","MT", false));
        characteristicList.add(new FishingGearTypeCharacteristic("OTT","GN", true));
        characteristicList.add(new FishingGearTypeCharacteristic("SDN","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("SDN","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("SPR","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("SPR","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("SSC","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("SSC","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("PS","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("PS","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("PS","HE", true));
        characteristicList.add(new FishingGearTypeCharacteristic("PS1","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("PS1","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("PS1","HE", true));
        characteristicList.add(new FishingGearTypeCharacteristic("PS2","ME", true));
        characteristicList.add(new FishingGearTypeCharacteristic("PS2","GM", true));
        characteristicList.add(new FishingGearTypeCharacteristic("PS2","HE", true));
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
    public class FishingGearTypeCharacteristic {
        private String fishingGearTypeCode;
        private String fishingGearCharacteristicCode;
        private Boolean mandatory;
    }
}
