/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service.mockdata;

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.AvailabilityType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;

public class MockData {

    /**
     * Get mocked data single object
     *
     * @param id
     * @return
     */
    public static CustomRuleType getDto(Long id) {
        CustomRuleType dto = new CustomRuleType();
        dto.setName("DummyRule" + id);
        dto.setAvailability(AvailabilityType.PUBLIC);
        dto.setName("Test");
        return dto;
    }

    /**
     * Get mocked data as a list
     *
     * @param amount
     * @return
     */
    public static List<CustomRuleType> getDtoList(Integer amount) {
        List<CustomRuleType> dtoList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            dtoList.add(getDto(Long.valueOf(i)));
        }
        return dtoList;
    }

}