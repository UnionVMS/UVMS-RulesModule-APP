/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.europa.ec.fisheries.uvms.rules.entity.FishingActivityId;
import eu.europa.ec.fisheries.uvms.rules.entity.IdType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

public class FishingActivityRulesHelper {

    public Set<FishingActivityId> mapToFishingActivityId(FLUXFAReportMessage fluxfaReportMessage) {
        Set<FishingActivityId> ids = new HashSet<>();
        if (fluxfaReportMessage != null){
            FLUXReportDocument fluxReportDocument = fluxfaReportMessage.getFLUXReportDocument();
            if (fluxReportDocument != null){
                mapFluxReportDocumentIDS(ids, fluxReportDocument, IdType.FLUX_FA_REPORT_MESSAGE_ID);
            }
            List<FAReportDocument> faReportDocuments = fluxfaReportMessage.getFAReportDocuments();
            if (CollectionUtils.isNotEmpty(faReportDocuments)){
                mapFaReportDocuments(ids, faReportDocuments);
            }
        }
        return ids;
    }

    private void mapFaReportDocuments(Set<FishingActivityId> ids, List<FAReportDocument> faReportDocuments) {
        if (CollectionUtils.isNotEmpty(faReportDocuments)){
            for (FAReportDocument faReportDocument : faReportDocuments) {
                if (faReportDocument != null){
                    mapFluxReportDocumentIDS(ids, faReportDocument.getRelatedFLUXReportDocument(), IdType.FA_FLUX_REPORT_ID);
                    mapSpecifiedFishingActivities(ids, faReportDocument.getSpecifiedFishingActivities());
                }
            }
        }
    }

    private void mapSpecifiedFishingActivities(Set<FishingActivityId> ids, List<FishingActivity> specifiedFishingActivities) {
        if (CollectionUtils.isNotEmpty(specifiedFishingActivities)){
            for (FishingActivity specifiedFishingActivity : specifiedFishingActivities) {
                if (specifiedFishingActivity != null){
                    FishingTrip specifiedFishingTrip = specifiedFishingActivity.getSpecifiedFishingTrip();
                    mapSpecifiedFishingTrip(ids, specifiedFishingTrip);
                }
            }
        }
    }

    private void mapSpecifiedFishingTrip(Set<FishingActivityId> ids, FishingTrip specifiedFishingTrip) {
        if (specifiedFishingTrip != null){
            List<IDType> ids1 = specifiedFishingTrip.getIDS();
            if (CollectionUtils.isNotEmpty(ids1)){
                for (IDType idType : ids1) {
                    String value = idType.getValue();
                    String schemeID = idType.getSchemeID();
                    if (StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(schemeID)){
                        ids.add(new FishingActivityId(null, value, IdType.TRIP_ID));
                    }
                }
            }
        }
    }

    private void mapFluxReportDocumentIDS(Set<FishingActivityId> ids, FLUXReportDocument fluxReportDocument, IdType faFluxMessageId) {
        if (fluxReportDocument != null){
            List<IDType> fluxReportDocumentIDS = fluxReportDocument.getIDS();
            mapReferencedID(ids, fluxReportDocument);
            if (CollectionUtils.isNotEmpty(fluxReportDocumentIDS)){
                IDType idType = fluxReportDocumentIDS.get(0);
                if (idType != null){
                    ids.add(new FishingActivityId(null, idType.getValue(), faFluxMessageId));
                }
            }
        }
    }

    private void mapReferencedID(Set<FishingActivityId> ids, FLUXReportDocument fluxReportDocument) {
        IDType referencedID = fluxReportDocument.getReferencedID();
        if (referencedID != null){
            ids.add(new FishingActivityId(null, referencedID.getValue(), IdType.REF_ID));
        }
    }

}
