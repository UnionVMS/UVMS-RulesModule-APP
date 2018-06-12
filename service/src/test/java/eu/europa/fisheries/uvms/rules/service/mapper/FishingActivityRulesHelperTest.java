/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.fisheries.uvms.rules.service.mapper;

import static org.jgroups.util.Util.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import eu.europa.ec.fisheries.uvms.rules.entity.FishingActivityId;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FishingActivityRulesHelper;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

public class FishingActivityRulesHelperTest {

    FishingActivityRulesHelper rulesHelper = new FishingActivityRulesHelper();
    @Test
    public void testWithEmptyFLUXFAReportMessage(){
        FLUXFAReportMessage fluxfaReportMessage = new FLUXFAReportMessage();
        Set<FishingActivityId> ids = rulesHelper.mapToFishingActivityId(fluxfaReportMessage);
        assertTrue(CollectionUtils.isEmpty(ids));
    }

    @Test
    public void testWithNull(){
        Set<FishingActivityId> ids = rulesHelper.mapToFishingActivityId(null);
        assertTrue(CollectionUtils.isEmpty(ids));
    }

    @Test
    public void testFluxMessageIdAndReferenceId(){
        FLUXFAReportMessage fluxfaReportMessage = new FLUXFAReportMessage();
        FLUXReportDocument fluxReportDocument = new FLUXReportDocument();
        IDType idType = new IDType();
        idType.setValue("uuid");
        fluxReportDocument.setIDS(Collections.singletonList(idType));
        IDType ref = new IDType();
        ref.setValue("ref");
        fluxReportDocument.setReferencedID(ref);
        fluxfaReportMessage.setFLUXReportDocument(fluxReportDocument);
        Set<FishingActivityId> ids = rulesHelper.mapToFishingActivityId(fluxfaReportMessage);
    //    List<String> fluxmsg = fishingActivityTypeListMap.get(FishingActivityType.FA_FLUX_MESSAGE_ID);
    //    List<String> refs = fishingActivityTypeListMap.get(FishingActivityType.REF_ID);
    //    assertEquals("uuid", fluxmsg.get(0));
    //    assertEquals("ref", refs.get(0));

    }

    @Test
    public void testRelatedFluxReportIds(){
        FLUXFAReportMessage fluxfaReportMessage = new FLUXFAReportMessage();

        IDType related1 = new IDType();
        related1.setValue("related1");

        IDType related2 = new IDType();
        related2.setValue("related2");

        FAReportDocument faReportDocument1 = new FAReportDocument();
        FLUXReportDocument fluxReportDocument = new FLUXReportDocument();
        fluxReportDocument.setIDS(Collections.singletonList(related1));
        faReportDocument1.setRelatedFLUXReportDocument(fluxReportDocument);

        FAReportDocument faReportDocument2 = new FAReportDocument();
        FLUXReportDocument fluxReportDocument2 = new FLUXReportDocument();
        fluxReportDocument2.setIDS(Collections.singletonList(related2));
        faReportDocument2.setRelatedFLUXReportDocument(fluxReportDocument2);

        fluxfaReportMessage.setFAReportDocuments(Arrays.asList(faReportDocument1, faReportDocument2));

       // Map<FishingActivityType, List<String>> fishingActivityTypeListMap = rulesHelper.mapToFishingActivityId(fluxfaReportMessage);

//        List<String> list = fishingActivityTypeListMap.get(FishingActivityType.FA_FLUX_REPORT_ID);
//        assertEquals("related1", list.get(0));
//        assertEquals("related2", list.get(1));

    }

    @Test
    public void testTripIds(){
        FLUXFAReportMessage fluxfaReportMessage = new FLUXFAReportMessage();
        FAReportDocument faReportDocument = new FAReportDocument();
        FishingActivity fishingActivity = new FishingActivity();
        FishingTrip fishingTrip = new FishingTrip();
        IDType trip1 = new IDType();
        trip1.setValue("trip1");
        trip1.setSchemeID("UUID");
        fishingTrip.setIDS(Collections.singletonList(trip1));
        fishingActivity.setSpecifiedFishingTrip(fishingTrip);
        faReportDocument.setSpecifiedFishingActivities(Collections.singletonList(fishingActivity));
        fluxfaReportMessage.setFAReportDocuments(Collections.singletonList(faReportDocument));

        //Map<FishingActivityType, List<String>> fishingActivityTypeListMap = rulesHelper.mapToFishingActivityId(fluxfaReportMessage);

       // List<String> list = fishingActivityTypeListMap.get(FishingActivityType.TRIP_ID);
       // assertEquals("trip1_UUID", list.get(0));

    }
}
