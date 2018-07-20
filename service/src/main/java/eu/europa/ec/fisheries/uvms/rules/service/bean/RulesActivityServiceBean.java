/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMapperException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityIDType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityTableType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityUniquinessList;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FaIdsListWithTripIdMap;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivityForTripIds;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivityWithIdentifiers;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.GetFishingActivitiesForTripResponse;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.GetNonUniqueIdsResponse;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.wsdl.subscription.module.SubscriptionPermissionAnswer;
import eu.europa.ec.fisheries.wsdl.subscription.module.SubscriptionPermissionResponse;
import eu.europa.fisheries.uvms.subscription.model.mapper.SubscriptionModuleResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingTrip;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

/**
 * Created by kovian on 17/07/2016.
 */
@Stateless
@LocalBean
@Slf4j
public class RulesActivityServiceBean {

    @EJB
    private RulesResponseConsumer consumer;

    @EJB
    private RulesMessageProducer producer;


    public boolean checkSubscriptionPermissions(String request, MessageType type) {
        try {
            String requestStr = ActivityModuleRequestMapper.mapToSubscriptionRequest(request, type);
            log.debug("Send MapToSubscriptionRequest to Activity");
            String jmsCorrelationId = producer.sendDataSourceMessage(requestStr, DataSourceQueue.ACTIVITY);
            TextMessage message = consumer.getMessage(jmsCorrelationId, TextMessage.class, 240000L);
            log.debug("Received response message from Subscription.");
            SubscriptionPermissionResponse subscriptionPermissionResponse = SubscriptionModuleResponseMapper.mapToSubscriptionPermissionResponse(message.getText());
            SubscriptionPermissionAnswer subscriptionCheck = subscriptionPermissionResponse.getSubscriptionCheck();
            return SubscriptionPermissionAnswer.YES.equals(subscriptionCheck);
        } catch (ActivityModelMapperException | JMSException | JAXBException | MessageException e) {
            log.error("[ERROR] while trying to check subscription permissions (Is [[[- Subscriptions -]]] module Deployed?).." +
                    "Going to assume the request doesn't have permissions!!", e);
        }
        return false;
    }

    public Map<ActivityTableType, List<IdType>> getNonUniqueIdsList(Object requestMessage) {
        Map<ActivityTableType, List<IdType>> nonUniqueIdsMap = new EnumMap<>(ActivityTableType.class);
        GetNonUniqueIdsResponse getNonUniqueIdsResponse = null;
        FLUXFAReportMessage fluxFaRepMessage;
        if (requestMessage != null && requestMessage instanceof FLUXFAReportMessage) {
            fluxFaRepMessage = (FLUXFAReportMessage) requestMessage;
        } else {
            log.error("Either FLUXFAReportMessage is null or is not of the right type!");
            return nonUniqueIdsMap;
        }
        try {
            String strReq = ActivityModuleRequestMapper.mapToGetNonUniqueIdRequest(collectAllIdsFromMessage(fluxFaRepMessage));
            if (StringUtils.isEmpty(strReq)) {
                log.warn("No IDs were found to issue request for in method RulesActivityServiceBean.getNonUniqueIdsList(..){...}. Empty list will be returned");
                return nonUniqueIdsMap;
            }
            log.debug("Send GetNonUniqueIdsRequest message to Activity");
            String jmsCorrelationId = producer.sendDataSourceMessage(strReq, DataSourceQueue.ACTIVITY);
            TextMessage message = consumer.getMessage(jmsCorrelationId, TextMessage.class);
            log.debug("Received response message");
            getNonUniqueIdsResponse = ActivityModuleResponseMapper.mapToGetUniqueIdResponseFromResponse(message, jmsCorrelationId);
        } catch (ActivityModelMapperException | MessageException e) {
            log.error("ERROR when sending/consuming message from ACTIVITY module. Service : RulesActivityServiceBean.getNonUniqueIdsList(Object requestMessage){...}", e);
        }

        if (getNonUniqueIdsResponse != null && CollectionUtils.isNotEmpty(getNonUniqueIdsResponse.getActivityUniquinessLists())) {
            mapGetUniqueIdResponseToIdsMap(nonUniqueIdsMap, getNonUniqueIdsResponse.getActivityUniquinessLists());
        }

        return nonUniqueIdsMap;
    }

    public Map<String, List<FishingActivityWithIdentifiers>> getFishingActivitiesForTrips(Object requestMessage) {
        GetFishingActivitiesForTripResponse response = null;
        FLUXFAReportMessage fluxFaRepMessage;
        if (requestMessage instanceof FLUXFAReportMessage) {
            fluxFaRepMessage = (FLUXFAReportMessage) requestMessage;
        } else {
            log.error("Either FLUXFAReportMessage is null or is not of the right type!");
            return MapUtils.EMPTY_MAP;
        }
        try {
            String strReq = ActivityModuleRequestMapper.mapToGetFishingActivitiesForTripRequest(collectFaIdsAndTripIdsFromMessage(fluxFaRepMessage));
            if (StringUtils.isEmpty(strReq)) {
                log.warn("The request resulted empty in method RulesActivityServiceBean.getFishingActivitiesForTrips(..){...}. Empty list will be returned");
                return MapUtils.EMPTY_MAP;
            }
            log.debug("Send GetFishingActivitiesForTripRequest message to Activity");
            String jmsCorrelationId = producer.sendDataSourceMessage(strReq, DataSourceQueue.ACTIVITY);
            TextMessage message = consumer.getMessage(jmsCorrelationId, TextMessage.class);
            log.debug("Received response message");
            response = ActivityModuleResponseMapper.mapToGetFishingActivitiesForTripResponse(message, jmsCorrelationId);
        } catch (ActivityModelMapperException | MessageException e) {
            log.error("when sending/consuming message from ACTIVITY module. Service : RulesActivityServiceBean.getNonUniqueIdsList(Object requestMessage){...}", e);
        }

        return transformResponse(response);
    }

    private Map<String, List<FishingActivityWithIdentifiers>> transformResponse(GetFishingActivitiesForTripResponse response) {
        Map<String, List<FishingActivityWithIdentifiers>> trsfResponse = new HashMap<>();
        if (response == null) {
            return trsfResponse;
        }
        List<FaIdsListWithTripIdMap> faWithIdentifiersList = response.getFaWithIdentifiers();
        if (CollectionUtils.isNotEmpty(faWithIdentifiersList)) {
            for (FaIdsListWithTripIdMap ident : faWithIdentifiersList) {
                trsfResponse.put(ident.getTripId(), ident.getFaIdentifierLists());
            }
        }
        return trsfResponse;
    }

    private void mapGetUniqueIdResponseToIdsMap(Map<ActivityTableType, List<IdType>> nonUniqueIdsMap, List<ActivityUniquinessList> activityUniquinessLists) {
        for (ActivityUniquinessList uniquenessListType : activityUniquinessLists) {
            List<IdType> idTypeList = new ArrayList<>();
            nonUniqueIdsMap.put(uniquenessListType.getActivityTableType(), idTypeList);
            if (CollectionUtils.isNotEmpty(uniquenessListType.getIds())) {
                mapActivityIdTypesToIdType(idTypeList, uniquenessListType.getIds());
            }
        }
    }

    private void mapActivityIdTypesToIdType(List<IdType> idTypeList, List<ActivityIDType> activityIdTypes) {
        for (ActivityIDType activityIdType : activityIdTypes) {
            idTypeList.add(new IdType(activityIdType.getValue(), activityIdType.getIdentifierSchemeId()));
        }
    }

    private Map<ActivityTableType, List<IDType>> collectAllIdsFromMessage(FLUXFAReportMessage request) {
        Map<ActivityTableType, List<IDType>> idsmap = new EnumMap<>(ActivityTableType.class);
        idsmap.put(ActivityTableType.RELATED_FLUX_REPORT_DOCUMENT_ENTITY, new ArrayList<IDType>());
        if (request == null) {
            return idsmap;
        }
        // FLUXReportDocument IDs
        FLUXReportDocument fluxReportDocument = request.getFLUXReportDocument();
        if (fluxReportDocument != null && CollectionUtils.isNotEmpty(fluxReportDocument.getIDS())) {
            idsmap.put(ActivityTableType.FLUX_REPORT_DOCUMENT_ENTITY, fluxReportDocument.getIDS());
        }
        // FAReportDocument.RelatedFLUXReportDocument IDs and ReferencedID
        List<FAReportDocument> faReportDocuments = request.getFAReportDocuments();
        if (CollectionUtils.isNotEmpty(faReportDocuments)) {
            for (FAReportDocument faRepDoc : faReportDocuments) {
                FLUXReportDocument relatedFLUXReportDocument = faRepDoc.getRelatedFLUXReportDocument();
                if (relatedFLUXReportDocument != null) {
                    List<IDType> idTypes = new ArrayList<>();
                    idTypes.addAll(relatedFLUXReportDocument.getIDS());
                    idTypes.add(relatedFLUXReportDocument.getReferencedID());
                    idTypes.removeAll(Collections.singletonList(null));
                    idsmap.get(ActivityTableType.RELATED_FLUX_REPORT_DOCUMENT_ENTITY).addAll(idTypes);
                }
            }
        }
        return idsmap;
    }

    private List<FishingActivityForTripIds> collectFaIdsAndTripIdsFromMessage(FLUXFAReportMessage fluxFaRepMessage) {
        List<FishingActivityForTripIds> idsReqList = new ArrayList<>();
        FLUXReportDocument fluxReportDocument = fluxFaRepMessage.getFLUXReportDocument();
        List<FAReportDocument> faReportDocuments = fluxFaRepMessage.getFAReportDocuments();
        if (fluxReportDocument == null || CollectionUtils.isEmpty(faReportDocuments)) {
            return idsReqList;
        }

        // Purpose code
        CodeType purposeCode = fluxReportDocument.getPurposeCode();
        List<String> purposeCodes = purposeCode != null && StringUtils.isNotEmpty(purposeCode.getValue()) ?
                Arrays.asList(purposeCode.getValue()) : Arrays.asList("1", "3", "5", "9");

        // FishinActivity type, tripId, tripSchemeId
        for (FAReportDocument faRepDoc : faReportDocuments) {
            collectFromActivityList(idsReqList, purposeCodes, faRepDoc.getSpecifiedFishingActivities());
        }
        return idsReqList;
    }

    private void collectFromActivityList(List<FishingActivityForTripIds> idsReqList, List<String> purposeCodes, List<FishingActivity> specifiedFishingActivities) {
        if (CollectionUtils.isEmpty(specifiedFishingActivities)) {
            return;
        }
        for (FishingActivity fishAct : specifiedFishingActivities) {
            CodeType typeCode = fishAct.getTypeCode();
            FishingTrip fishTrip = fishAct.getSpecifiedFishingTrip();
            if (typeCode == null || StringUtils.isEmpty(typeCode.getValue()) || fishTrip == null || CollectionUtils.isEmpty(fishTrip.getIDS())) {
                continue;
            }
            for (IDType tripId : fishTrip.getIDS()) {
                idsReqList.add(new FishingActivityForTripIds(typeCode.getValue(), tripId.getValue(), tripId.getSchemeID(), purposeCodes));
            }
        }
    }
}
