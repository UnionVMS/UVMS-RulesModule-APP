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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.ActivityObjectsHelper;
import org.junit.Test;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;

public class FAReportDocumentFactTest {

    private ActivityObjectsHelper objectsHelper = new ActivityObjectsHelper();

    @Test
    public void testWithEmptyListShouldFail(){
        List<FishingActivity> fishingActivities = new ArrayList<>();
        FaReportDocumentFact reportDocumentFact = new FaReportDocumentFact();
        assertFalse(reportDocumentFact.isValid(fishingActivities));
    }

    @Test
    public void testWithThreeIdenticalDeparturesOnSameDayShouldFail(){
        FishingActivity departure1 = objectsHelper.generateActivity("31-08-1982 10:20:56","DEPARTURE");
        FishingActivity departure2 = objectsHelper.generateActivity("31-08-1982 10:25:56","DEPARTURE");
        FishingActivity departure3 = objectsHelper.generateActivity("31-08-1982 10:30:56","DEPARTURE");
        FaReportDocumentFact reportDocumentFact = new FaReportDocumentFact();
        assertFalse(reportDocumentFact.isValid(Arrays.asList(departure1, departure2, departure3)));
    }

    @Test
    public void testWithNullOccurrenceThrowExceptionShouldNotBeEvaluated(){
        FishingActivity departure1 = objectsHelper.generateActivity(null,"DEPARTURE");
        FaReportDocumentFact reportDocumentFact = new FaReportDocumentFact();
        assertTrue(reportDocumentFact.isValid(Collections.singletonList(departure1)));
    }

    @Test
    public void testWithOneDeparturesAndOneArrivalShouldPass(){
        FishingActivity departure = objectsHelper.generateActivity("31-08-1982 10:20:56","DEPARTURE");
        FishingActivity arrival = objectsHelper.generateActivity("20-03-1984 10:20:56","ARRIVAL");
        FaReportDocumentFact reportDocumentFact = new FaReportDocumentFact();
        assertTrue(reportDocumentFact.isValid(Arrays.asList(departure, arrival)));
    }

    @Test
    public void testWithSeveralFishingOperationsOnTheSameDayShouldPass(){
        FishingActivity arrival1 = objectsHelper.generateActivity("31-08-1982 10:20:56","FISHING_OPERATION");
        FishingActivity arrival2 = objectsHelper.generateActivity("31-08-1982 10:25:56","FISHING_OPERATION");
        FishingActivity arrival3 = objectsHelper.generateActivity("31-08-1982 10:49:56","FISHING_OPERATION");
        FishingActivity arrival4 = objectsHelper.generateActivity("31-08-1982 10:49:56","FISHING_OPERATION");
        FishingActivity arrival5 = objectsHelper.generateActivity("31-08-1982 10:50:56","JOINED_FISHING_OPERATION");
        FaReportDocumentFact reportDocumentFact = new FaReportDocumentFact();
        assertTrue(reportDocumentFact.isValid(Arrays.asList(arrival1, arrival2, arrival3, arrival4, arrival5)));
    }

    @Test
    public void testWithSeveralFishingOperationsOnDifferentDaysShouldFail(){
        FishingActivity arrival1 = objectsHelper.generateActivity("31-08-1982 10:20:56","FISHING_OPERATION");
        FishingActivity arrival2 = objectsHelper.generateActivity("31-08-1982 10:25:56","FISHING_OPERATION");
        FishingActivity arrival3 = objectsHelper.generateActivity("31-08-1982 10:49:56","FISHING_OPERATION");
        FishingActivity arrival4 = objectsHelper.generateActivity("22-08-1982 10:49:56","FISHING_OPERATION");
        FishingActivity arrival5 = objectsHelper.generateActivity("11-08-1982 10:50:56","JOINED_FISHING_OPERATION");
        FaReportDocumentFact reportDocumentFact = new FaReportDocumentFact();
        assertFalse(reportDocumentFact.isValid(Arrays.asList(arrival1, arrival2, arrival3, arrival4, arrival5)));
    }

    @Test
    public void testWithSeveralFishingOperationsOnTheSameDayAndTwoDeparturesOnSameDayShouldFail(){
        FishingActivity arrival1 = objectsHelper.generateActivity("31-08-1982 10:20:56","FISHING_OPERATION");
        FishingActivity arrival2 = objectsHelper.generateActivity("31-08-1982 10:25:56","FISHING_OPERATION");
        FishingActivity arrival3 = objectsHelper.generateActivity("31-08-1982 10:49:56","FISHING_OPERATION");
        FishingActivity arrival4 = objectsHelper.generateActivity("31-08-1982 10:49:56","FISHING_OPERATION");
        FishingActivity arrival5 = objectsHelper.generateActivity("31-08-1982 10:50:56","JOINED_FISHING_OPERATION");
        FishingActivity departure1 = objectsHelper.generateActivity("31-08-1982 10:20:56","DEPARTURE");
        FishingActivity departure2 = objectsHelper.generateActivity("31-08-1982 10:25:56","DEPARTURE");
        FaReportDocumentFact reportDocumentFact = new FaReportDocumentFact();
        assertFalse(reportDocumentFact.isValid(Arrays.asList(arrival1, arrival2, arrival3, arrival4, arrival5, departure1, departure2)));
    }

    @Test
    public void testWithUnknownTypeShouldNotBeEvaluated(){
        FishingActivity arrival = objectsHelper.generateActivity("31-08-1982 10:20:56","UNKNOWN");
        FaReportDocumentFact reportDocumentFact = new FaReportDocumentFact();
        assertTrue(reportDocumentFact.isValid(Collections.singletonList(arrival)));
    }

    @Test
    public void testWithTwoDifferentDeparturesAndOneArrivalShouldFail(){
        FishingActivity departure1 = objectsHelper.generateActivity("20-08-1982 10:20:56","DEPARTURE");
        FishingActivity departure2= objectsHelper.generateActivity("21-08-1982 10:20:56","DEPARTURE");
        FishingActivity arrival = objectsHelper.generateActivity("20-03-1984 10:20:56","ARRIVAL");
        FaReportDocumentFact reportDocumentFact = new FaReportDocumentFact();
        assertFalse(reportDocumentFact.isValid(Arrays.asList(departure1, departure2, arrival)));
    }

    @Test
    public void testWithOneDeparturesOnSameDayShouldPass(){
        FishingActivity departure1 = objectsHelper.generateActivity("31-08-1982 10:20:56","DEPARTURE");
        FaReportDocumentFact reportDocumentFact = new FaReportDocumentFact();
        assertTrue(reportDocumentFact.isValid(Collections.singletonList(departure1)));
    }
}
