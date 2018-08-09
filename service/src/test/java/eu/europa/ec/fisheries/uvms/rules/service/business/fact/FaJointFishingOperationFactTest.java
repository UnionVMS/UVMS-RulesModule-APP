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
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;

/**
 * Created by sanera on 26/07/2017.
 */
public class FaJointFishingOperationFactTest {

    private FaJointFishingOperationFact faJointFishingOperationFact = new FaJointFishingOperationFact();

    @Test
    public void testIfVesselTransportPresent(){

       boolean result= faJointFishingOperationFact.ifVesselTransportPresent(Collections.singletonList(ActivityObjectsHelper.generateActivity()));
        assertTrue(result);
    }

    @Test
    public void testIfFACatchPresent(){
        boolean result= faJointFishingOperationFact.ifFACatchPresent(Collections.singletonList(ActivityObjectsHelper.generateActivity()));
        assertTrue(result);
    }

    @Test
    public void testAtLeastOneFaCatchTypeCodePresentHappy(){
        FishingActivityFact fact = new FishingActivityFact();
        fact.setSpecifiedFaCatch(Collections.singletonList(ActivityObjectsHelper.generateFACatch("ALLOCATED_TO_QUOTA", "BFT")));
        boolean result= fact.atLeastOneFaCatchWithBFTAndAllocatedQuotaPresent();
        assertTrue(result);
    }

    @Test
    public void testAtLeastOneFaCatchTypeCodePresentSad(){
        FishingActivityFact fact = new FishingActivityFact();
        fact.setSpecifiedFaCatch(Collections.singletonList(ActivityObjectsHelper.generateFACatch("ALLBLAQUOTA", "BFT")));
        boolean result= fact.atLeastOneFaCatchWithBFTAndAllocatedQuotaPresent();
        assertFalse(result);
    }

    @Test
    public void testAtLeastOneFaCatchTypeCodeHappy2(){
        FishingActivityFact fact = new FishingActivityFact();
        fact.setSpecifiedFaCatch(Collections.singletonList(ActivityObjectsHelper.generateFACatch("ALLOCATED_TO_QUOTA", "BFTTTTTT")));
        boolean result= fact.atLeastOneFaCatchWithBFTAndAllocatedQuotaPresent();
        assertTrue(result);
    }
}
