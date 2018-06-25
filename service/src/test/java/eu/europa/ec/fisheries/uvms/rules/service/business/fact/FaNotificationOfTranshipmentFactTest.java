/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import static org.junit.Assert.assertTrue;

import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.ActivityObjectsHelper;
import org.junit.Test;

/**
 * Created by sanera on 24/07/2017.
 */
public class FaNotificationOfTranshipmentFactTest {

    FaNotificationOfTranshipmentFact faNotificationOfTranshipmentFact = new FaNotificationOfTranshipmentFact();
    @Test
    public void testIfFLUXLocationForFACatchIsAREA(){

       boolean result= faNotificationOfTranshipmentFact.ifFLUXLocationForFACatchIsAREA(ActivityObjectsHelper.generateFACatchList());
        assertTrue(result);
    }

    @Test
    public void testContainsAnyFaCatch(){

        boolean result= faNotificationOfTranshipmentFact.containsAnyFaCatch(ActivityObjectsHelper.generateFACatchList(),"BFT","LOADED");
        assertTrue(result);
    }
}
