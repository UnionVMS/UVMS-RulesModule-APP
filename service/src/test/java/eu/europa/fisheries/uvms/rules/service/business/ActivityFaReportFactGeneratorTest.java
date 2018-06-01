/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.fisheries.uvms.rules.service.business;

import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ASSET_LIST;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.powermock.reflect.Whitebox.setInternalState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdTypeWithFlagState;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.ActivityFaReportFactGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import org.junit.Before;
import org.junit.Test;

public class ActivityFaReportFactGeneratorTest {

    private ActivityFaReportFactGenerator generator;

    private ActivityFactMapper mapper = new ActivityFactMapper(new XPathStringWrapper());

    private IdTypeWithFlagState idTypeWithFlagState;

    @Before
    public void before(){
        generator = new ActivityFaReportFactGenerator();
        setInternalState(generator, "activityFactMapper", mapper);
        Map<ExtraValueType, Object> map = new HashMap<>();
        idTypeWithFlagState = new IdTypeWithFlagState();
        idTypeWithFlagState.setFlagState("FLAG");
        map.put(ASSET_LIST, singletonList(idTypeWithFlagState));
        generator.setExtraValueMap(map);
    }

    @Test
    public void testSetAdditionalValidationObject(){
        generator.setAdditionalValidationObject();
        List<IdTypeWithFlagState> assetList = mapper.getAssetList();
        IdTypeWithFlagState flagState = assetList.get(0);
        assertEquals(idTypeWithFlagState.getValue(), flagState.getValue());
    }

}
