/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.uvms.rules.service.bean.RuleTestHelper;
import org.junit.Test;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * Created by sanera on 28/08/2017.
 */
public class FaEntryToSeaFactTest {
    private FaEntryToSeaFact  faEntryToSeaFact = new FaEntryToSeaFact();

    @Test
    public void testVerifyFLUXLocationIDValue(){
        faEntryToSeaFact.setFaReportDocumentTypeCode(RuleTestHelper.getCodeType("DECLARATION",null));
        FLUXLocation fluxLocation= RuleTestHelper.getFLUXLocation(RuleTestHelper.getCodeTypeUNCEFACT("AREA",null),RuleTestHelper.getIdTypeUNCEFACT(null,"EFFORT_ZONE"));
        boolean result= faEntryToSeaFact.verifyFLUXLocationIDValue(Arrays.asList(fluxLocation));
        assertTrue(result);
    }
}
