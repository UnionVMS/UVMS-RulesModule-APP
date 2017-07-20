/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleResponseMapper.mapToGetUniqueIdResponseFromResponse;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMapperException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityIDType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityTableType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityUniquinessList;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.GetNonUniqueIdsResponse;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
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
            String s = producer.sendDataSourceMessage(strReq, DataSourceQueue.ACTIVITY);
            TextMessage message = consumer.getMessage(s, TextMessage.class);
            getNonUniqueIdsResponse = mapToGetUniqueIdResponseFromResponse(message, s);
        } catch (MessageException | ActivityModelMapperException e) {
            log.error("ERROR when sending/consuming message from ACTIVITY module. Service : RulesActivityServiceBean.getNonUniqueIdsList(Object requestMessage){...}", e);
        }

        if (getNonUniqueIdsResponse != null && CollectionUtils.isNotEmpty(getNonUniqueIdsResponse.getActivityUniquinessLists())) {
            mapGetUniqueIdResponseToIdsMap(nonUniqueIdsMap, getNonUniqueIdsResponse.getActivityUniquinessLists());
        }

        return nonUniqueIdsMap;
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
                    idsmap.put(ActivityTableType.RELATED_FLUX_REPORT_DOCUMENT_ENTITY, idTypes);
                }
            }
        }
        return idsmap;
    }
}
