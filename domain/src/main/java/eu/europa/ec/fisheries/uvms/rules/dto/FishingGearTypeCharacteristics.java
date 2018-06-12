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
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Singleton
@Startup
public class FishingGearTypeCharacteristics {

    @Getter
    private List<FishingGearTypeCharacteristic> characteristicList;

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

        // TODO complete list
    }

    @Data
    @AllArgsConstructor
    private class FishingGearTypeCharacteristic {
        private String fishingGearTypeCode;
        private String fishingGearCharacteristicCode;
        private Boolean mandatory;
    }


}
