/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.uvms.asset.client.AssetClient;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetDTO;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetIdentifier;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.RulesAssetServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.VesselTransportMeansDto;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class AssetServiceBeanTest {

    @InjectMocks
    private RulesAssetServiceBean assetService;

    @Mock
    AssetClient client;

    @Before
    public void before() {

    }

    @Test
    public void testIsCFRInFleetUnderFlagStateOnLandingDateWithNoHistory() {
        assertFalse(assetService.isCFRInFleetUnderFlagStateOnLandingDate("", "", new DateTime()));
    }

    @Test
    public void testIsCFRInFleetUnderFlagStateOnLandingDateWithHappyEnding() {
        OffsetDateTime now = OffsetDateTime.now();
        AssetDTO asset = new AssetDTO();
        asset.setVesselDateOfEntry(now);
        asset.setFlagStateCode("BEL");
        Mockito.when(client.getAssetFromAssetIdAndDate(any(AssetIdentifier.class), anyString(), any(OffsetDateTime.class))).thenReturn(asset);
        assertTrue(assetService.isCFRInFleetUnderFlagStateOnLandingDate("", "BEL", new DateTime(2019, 1, 15, 0, 0, 0, 0)));
    }


    @Test
    public void testFindHistoryOfAssetBy() {

        List<FAReportDocument> faReportDocuments = new ArrayList();
        List<FishingActivity> specifiedFishingActivities = new ArrayList<>();
        FishingActivity fishingActivity = new FishingActivity();

        List<FishingActivity> relatedActivities = new ArrayList<>();
        FishingActivity relatedActivity = new FishingActivity();

        VesselTransportMeans vesselTransportMeans = new VesselTransportMeans();
        List<VesselTransportMeans> vesselTransportMeansList = new ArrayList<>();
        vesselTransportMeansList.add(vesselTransportMeans);

        relatedActivity.setRelatedVesselTransportMeans(vesselTransportMeansList);

        relatedActivities.add(relatedActivity);

        fishingActivity.setRelatedFishingActivities(relatedActivities);
        specifiedFishingActivities.add(fishingActivity);
        FAReportDocument faReportDocument = new FAReportDocument();
        faReportDocument.setSpecifiedFishingActivities(specifiedFishingActivities);
        faReportDocuments.add(faReportDocument);

        List<VesselTransportMeansDto> historyOfAssetBy = assetService.findHistoryOfAssetBy(faReportDocuments);

        assertTrue(CollectionUtils.isNotEmpty(historyOfAssetBy));
    }
}
