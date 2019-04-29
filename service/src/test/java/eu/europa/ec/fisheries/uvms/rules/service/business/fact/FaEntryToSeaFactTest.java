/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.ActivityObjectsHelper;
import org.junit.Test;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

public class FaEntryToSeaFactTest {
    private FaEntryToSeaFact  faEntryToSeaFact = new FaEntryToSeaFact();
    private ActivityObjectsHelper objectsHelper = new ActivityObjectsHelper();

    @Test
    public void testWithEffortZoneShouldPass(){
        faEntryToSeaFact.setFaReportDocumentTypeCode(objectsHelper.generateCodeType("DECLARATION",null));
        FLUXLocation fluxLocation= ActivityObjectsHelper.generateFLUXLocation(ActivityObjectsHelper.generateCodeTypeUNCEFACT("AREA",null),ActivityObjectsHelper.generateIdTypeUNCEFACT(null,"EFFORT_ZONE"));
        boolean result= faEntryToSeaFact.fluxLocationIDIsValid(Collections.singletonList(fluxLocation), null, null, null);
        assertTrue(result);
    }

    @Test
    public void testWithEffortZoneAndAreaEntryShouldNotFail(){
        faEntryToSeaFact.setFaReportDocumentTypeCode(objectsHelper.generateCodeType("AREA_ENTRY",null));
        FLUXLocation fluxLocation= ActivityObjectsHelper.generateFLUXLocation(ActivityObjectsHelper.generateCodeTypeUNCEFACT("AREA",null),ActivityObjectsHelper.generateIdTypeUNCEFACT(null,"EFFORT_ZONE"));
        boolean result= faEntryToSeaFact.fluxLocationIDIsValid(Collections.singletonList(fluxLocation), null, null, null);
        assertTrue(result);
    }

    @Test
    public void testWithStatRectangleZoneShouldFail(){
        faEntryToSeaFact.setFaReportDocumentTypeCode(objectsHelper.generateCodeType("DECLARATION",null));
        FLUXLocation fluxLocation= ActivityObjectsHelper.generateFLUXLocation(ActivityObjectsHelper.generateCodeTypeUNCEFACT("AREA",null),ActivityObjectsHelper.generateIdTypeUNCEFACT(null,"STAT_RECTANGLE"));
        boolean result= faEntryToSeaFact.fluxLocationIDIsValid(Collections.singletonList(fluxLocation), null, null, null);
        assertFalse(result);
    }

    @Test
    public void testWithNullShouldFail(){
        faEntryToSeaFact.setFaReportDocumentTypeCode(objectsHelper.generateCodeType("DECLARATION",null));
        FLUXLocation fluxLocation= ActivityObjectsHelper.generateFLUXLocation(ActivityObjectsHelper.generateCodeTypeUNCEFACT("AREA",null),ActivityObjectsHelper.generateIdTypeUNCEFACT(null,null));
        boolean result= faEntryToSeaFact.fluxLocationIDIsValid(Collections.singletonList(fluxLocation), null, null, null);
        assertFalse(result);
    }
}
