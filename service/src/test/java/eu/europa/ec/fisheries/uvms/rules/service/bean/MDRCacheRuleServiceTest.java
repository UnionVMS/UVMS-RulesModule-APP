/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.uvms.rules.service.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.ActivityObjectsHelper;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

/**
 * TODO create findHistoryOfAssetBy
 */
public class MDRCacheRuleServiceTest {

    @Mock
    private MDRCache mdrCache;

    @InjectMocks
    private MDRCacheRuleService mdrCacheRuleService;

    @Test
    public void testCombinationExistsInConversionFactorList(){

        FLUXLocation code = ActivityObjectsHelper.generateFluxLocationWithTypeCodeValue("code");

        CodeType codeType = new CodeType();

    }
}
