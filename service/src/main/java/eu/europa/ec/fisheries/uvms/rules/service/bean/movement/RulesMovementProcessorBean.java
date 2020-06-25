/*
 * ﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
 * © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.movement;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.config.module.v1.SettingsListResponse;
import eu.europa.ec.fisheries.schema.config.types.v1.SettingType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementRefType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementRefTypeType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.MovementTypeType;
import eu.europa.ec.fisheries.schema.exchange.movement.v1.SetReportMovementType;
import eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType;
import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.mobileterminal.module.v1.MobileTerminalBatchListElement;
import eu.europa.ec.fisheries.schema.mobileterminal.types.v1.*;
import eu.europa.ec.fisheries.schema.movement.common.v1.SimpleResponse;
import eu.europa.ec.fisheries.schema.movement.module.v1.CreateMovementBatchResponse;
import eu.europa.ec.fisheries.schema.movement.module.v1.CreateMovementResponse;
import eu.europa.ec.fisheries.schema.movement.module.v1.ProcessedMovementAck;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementMapResponseType;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.RangeKeyType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmStatusType;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetId;
import eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdList;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.AvailabilityType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SubscritionOperationType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.UpdateSubscriptionType;
import eu.europa.ec.fisheries.schema.rules.mobileterminal.v1.IdList;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetTicketsAndRulesByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.module.v1.SendFLUXMovementReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXMovementReportRequest;
import eu.europa.ec.fisheries.schema.rules.movement.v1.MovementSourceType;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.schema.rules.previous.v1.PreviousReportType;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmListCriteria;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmSearchKey;
import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetAlarmListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByMovementsResponse;
import eu.europa.ec.fisheries.schema.rules.source.v1.GetTicketListByQueryResponse;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.schema.rules.ticketrule.v1.TicketAndRuleType;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelValidationException;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.audit.model.exception.AuditModelMarshallException;
import eu.europa.ec.fisheries.uvms.audit.model.mapper.AuditLogMapper;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.commons.notifications.NotificationMessage;
import eu.europa.ec.fisheries.uvms.config.model.mapper.ModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMapperException;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.exception.MobileTerminalModelMapperException;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.exception.MobileTerminalUnmarshallException;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.mapper.MobileTerminalModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.mobileterminal.model.mapper.MobileTerminalModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.movement.model.exception.MovementModelException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.MovementModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.*;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditObjectTypeEnum;
import eu.europa.ec.fisheries.uvms.rules.model.constant.AuditOperationEnum;
import eu.europa.ec.fisheries.uvms.rules.model.dto.AlarmListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TicketListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.service.bean.mdr.MDRCache;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.PreviousReportFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RulesUtil;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.AlarmReportEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketCountEvent;
import eu.europa.ec.fisheries.uvms.rules.service.event.TicketUpdateEvent;
import eu.europa.ec.fisheries.uvms.rules.service.exception.InputArgumentException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.*;
import eu.europa.ec.fisheries.uvms.user.model.mapper.UserModuleRequestMapper;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.types.*;
import eu.europa.ec.fisheries.wsdl.user.module.GetContactDetailResponse;
import eu.europa.ec.fisheries.wsdl.user.module.GetUserContextResponse;
import eu.europa.ec.fisheries.wsdl.user.types.Feature;
import eu.europa.ec.fisheries.wsdl.user.types.UserContext;
import eu.europa.ec.fisheries.wsdl.user.types.UserContextId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.fluxvesselpositionmessage._4.FLUXVesselPositionMessage;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static eu.europa.ec.fisheries.uvms.movement.model.exception.ErrorCode.MOVEMENT_DUPLICATE_ERROR;


@Stateless
@LocalBean
@Slf4j
public class RulesMovementProcessorBean {

    private static final long TWENTYFOUR_HOURS_IN_MILLISEC = 86400000;
    private static final String MOVEMENTTYPE_POS = "POS";
    private static final String MOVEMENTTYPE_EXI = "EXIT";
    private static final String MOVEMENTTYPE_ENT = "ENTRY";
    private static final String MOVEMENTTYPE_MAN = "MANUAL";

    @EJB
    private RulesResponseConsumer consumer;

    @EJB
    private MovementOutQueueProducer movOutQueueProducer;

    @EJB
    private RulesDomainModel rulesDomainModel;

    @EJB
    private MovementsRulesValidator movementValidator;

    @EJB
    private RulesMovementProducerBean movementProducer;

    @EJB
    private RulesMobilTerminalProducerBean mobileTerminalProducer;

    @EJB
    private RulesExchangeProducerBean exchangeProducer;

    @EJB
    private RulesAssetProducerBean assetProducer;

    @EJB
    private RulesConfigProducerBean configProducer;

    @EJB
    private RulesUserProducerBean userProducer;

    @EJB
    private RulesAuditProducerBean auditProducer;

    @Inject
    @TicketUpdateEvent
    private Event<NotificationMessage> ticketUpdateEvent;

    @Inject
    @TicketCountEvent
    private Event<NotificationMessage> ticketCountEvent;

    @Inject
    @AlarmReportEvent
    private Event<NotificationMessage> alarmReportEvent;

    @Inject
    @AlarmReportCountEvent
    private Event<NotificationMessage> alarmReportCountEvent;

    private Map<String, MovementTypeType> mapToMovementType;

    @Inject
    void setMdrCache(MDRCache mdrCache) {
        mapToMovementType = new HashMap<>();
        List<ObjectRepresentation> mdrCacheEntries = mdrCache.getEntry(MDRAcronymType.FLUX_VESSEL_POSITION_TYPE);
        mdrCacheEntries.stream().map(ObjectRepresentation::getFields)
                .forEach(t ->  t.forEach( s -> {
                    if ("code".equals(s.getColumnName())) {
                        String columnValue = s.getColumnValue();
                        switch (columnValue) {
                            case MOVEMENTTYPE_POS:
                                mapToMovementType.put(columnValue, MovementTypeType.POS);
                                break;
                            case MOVEMENTTYPE_EXI:
                                mapToMovementType.put(columnValue, MovementTypeType.EXI);
                                break;
                            case MOVEMENTTYPE_ENT:
                                mapToMovementType.put(columnValue, MovementTypeType.ENT);
                                break;
                            case MOVEMENTTYPE_MAN:
                                mapToMovementType.put(columnValue, MovementTypeType.MAN);
                                break;
                            default:
                                log.warn("Movement type couldn't be mapped: "+ columnValue);
                        }
                    }
                }));
    }

    public void sendMovementReport(SendFLUXMovementReportRequest request, String messageGuid) throws RulesServiceException {
        log.info("Sending Movement Report to exchange");
        try {
            String exchangeMessageText = ExchangeMovementMapper.mapToFluxMovementReport(request.getRequest(), request.getUsername(), request.getSenderOrReceiver(), request.getFluxDataFlow(), request.getLogGuid(),request.getAd());
            exchangeProducer.sendModuleMessage(exchangeMessageText, consumer.getDestination());
        } catch (ExchangeModelMapperException | MessageException e) {
            log.error("Error while send movement report to exchange", e);
        }
    }

    public void setMovementReportReceived(SetFLUXMovementReportRequest request, String messageGuid) throws RulesServiceException {
        FLUXVesselPositionMessage fluxVesselPositionMessage;
        String pluginType = request.getType().name();
        String userName = request.getUsername();
        String registeredPluginClassName = request.getRegisteredClassName();
        try {
            fluxVesselPositionMessage = JAXBUtils.unMarshallMessage(request.getRequest(), FLUXVesselPositionMessage.class, null);
            List<RawMovementType> movementReportsList = FLUXVesselPositionMapper.mapToRawMovementTypes(fluxVesselPositionMessage, registeredPluginClassName,pluginType,mapToMovementType);
            // If no movements were received then there is no sense to continue, so just going to update the exchange log status to FAILED!
            if (CollectionUtils.isEmpty(movementReportsList)) {
                log.warn("The list of rawMovements is EMPTY! Not going to proceed neither validation not sending to Movement Module!");
                updateRequestMessageStatusInExchange(request.getLogGuid(), ExchangeLogStatusTypeType.FAILED);
                sendBatchBackToExchange(request.getLogGuid(), movementReportsList, MovementRefTypeType.ALARM, userName);
                return;
            }
            // Decomment this one and comment the other when validation is working! Still work needs to be done after this!
            EnrichedMovementWrapper enrichedWrapper = processReceivedMovementsAsBatch(movementReportsList, pluginType, userName, request.getLogGuid());
//            enrichAndSenMovementsAsBatch(enrichedWrapper, movementReportsList, userName, request.getLogGuid());
//            enrichAndSenMovementsAsBatch(null, movementReportsList, userName, request.getLogGuid());
            // Send some response to Movement, if it originated from there (manual movement)
            if (MovementSourceType.MANUAL.equals(movementReportsList.get(0).getSource())) {// A person has created a position
                ProcessedMovementAck response = MovementModuleResponseMapper.mapProcessedMovementAck(eu.europa.ec.fisheries.schema.movement.common.v1.AcknowledgeTypeType.OK,
                        messageGuid, "Movement successfully processed");
                movOutQueueProducer.sendMessageWithSpecificIds(JAXBMarshaller.marshallJaxBObjectToString(response), movOutQueueProducer.getDestination(), null, messageGuid, messageGuid);
            }
        } catch (JAXBException | RulesModelMarshallException | MessageException e) {
            log.error("Error while processing received movement", e);
        }
    }


    /**
     * This method is just up until the new movement flow is ready from Swe team!
     * It is actually avoiding validation process all together since it doesn't work as of now!
     *
     * @param rawMovements
     * @param username
     * @param exchangeLogGuid
     * @throws RulesServiceException
     */
    private void enrichAndSenMovementsAsBatch(EnrichedMovementWrapper enrichedWrapper1, List<RawMovementType> rawMovements, String username, String exchangeLogGuid) throws RulesServiceException {
        try {
            // Enrich with MobilTerminal and Assets data. Get Mobile Terminal if it exists.
            EnrichedMovementWrapper enrichedWrapper = enrichBatchWithMobileTerminalAndAssets(rawMovements);
            CreateMovementBatchResponse movementBatchResponse = sendBatchToMovement(enrichedWrapper.getAssetList(), rawMovements, username);
            ExchangeLogStatusTypeType status;
            if (movementBatchResponse != null && SimpleResponse.OK.equals(movementBatchResponse.getResponse())) {
                // Here when ready needs to happen the validation with the list returned from movements! movementBatchResponse.getMovements();
                status = ExchangeLogStatusTypeType.SUCCESSFUL;
            } else {
                status = ExchangeLogStatusTypeType.FAILED;
            }
            sendBatchBackToExchange(exchangeLogGuid, rawMovements, MovementRefTypeType.MOVEMENT, username);
            updateRequestMessageStatusInExchange(exchangeLogGuid, status);
        } catch (MessageException | MobileTerminalModelMapperException | MobileTerminalUnmarshallException | JMSException | AssetModelMapperException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    private EnrichedMovementWrapper processReceivedMovementsAsBatch(List<RawMovementType> rawMovements, String pluginType, String username, String exchangeLogGuid) throws RulesServiceException {
        try {
            // Enrich with MobilTerminal and Assets data. Get Mobile Terminal if it exists.
            EnrichedMovementWrapper enrichedWrapper = enrichBatchWithMobileTerminalAndAssets(rawMovements);
            List<RawMovementFact> rawMovementFactList = RawMovementFactMapper.mapRawMovementFacts(rawMovements, enrichedWrapper.getMobileTerminalList(),
                    enrichedWrapper.getAssetList(), pluginType);
            movementValidator.evaluate(rawMovementFactList);
            if (allFactsAreOk(rawMovementFactList)) { // For now it is always OK
                // The collectMovementData actually is the method that sends the movements list to Movements module to be saved!
                List<MovementFact> movementFactList = collectBatchMovementData(enrichedWrapper.getMobileTerminalList(), enrichedWrapper.getAssetList(), rawMovements, username);
                log.info(" Validating movement from Movement Module");
                movementValidator.evaluate(movementFactList, true);
                // Tell Exchange that a movement Batch was persisted in Movement
                ExchangeLogStatusTypeType status;
                if (CollectionUtils.isNotEmpty(movementFactList)) {
                    status = ExchangeLogStatusTypeType.SUCCESSFUL;
                    sendBatchBackToExchange(exchangeLogGuid, rawMovements, MovementRefTypeType.MOVEMENT, username);
                } else {
                    status = ExchangeLogStatusTypeType.ERROR;
                    sendBatchBackToExchange(exchangeLogGuid, rawMovements, MovementRefTypeType.MOVEMENT, username);
                }
                updateRequestMessageStatusInExchange(exchangeLogGuid, status);
            } else {
                // Tell Exchange that the report caused an alarm
                updateRequestMessageStatusInExchange(exchangeLogGuid, ExchangeLogStatusTypeType.FAILED);
                sendBatchBackToExchange(exchangeLogGuid, rawMovements, MovementRefTypeType.ALARM, username);
            }
            return enrichedWrapper;
        } catch (MessageException | MobileTerminalModelMapperException | MobileTerminalUnmarshallException | JMSException | AssetModelMapperException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * Enriches the rawMovementList with Assets and Mobileterminals.
     *
     * @param rawMovementList
     * @throws AssetModelMapperException
     * @throws MessageException
     * @throws MobileTerminalUnmarshallException
     * @throws JMSException
     * @throws MobileTerminalModelMapperException
     */
    private EnrichedMovementWrapper enrichBatchWithMobileTerminalAndAssets(List<RawMovementType> rawMovementList) throws AssetModelMapperException, MessageException, MobileTerminalUnmarshallException, JMSException, MobileTerminalModelMapperException {
        List<Asset> assetList = new ArrayList<>();
        // Get Mobile Terminal if it exists
        List<MobileTerminalType> mobileTerminalList;
        // Get MobileTerminalData
        mobileTerminalList = getMobileTerminalByRawMovementsBatch(rawMovementList);
        if (CollectionUtils.isNotEmpty(mobileTerminalList)) {
            List<String> connectIds = collectConnectIds(mobileTerminalList);
            if (CollectionUtils.isNotEmpty(connectIds)) {
                assetList = getAssetByConnectIdsBatch(connectIds);
            }
        } else {
            // Get Assets data, at this point as of now the loop is unavoidable since getAssetByCfrIrcs()
            for (RawMovementType rawMovementType : rawMovementList) {
                Asset asset = getAssetByCfrIrcs(rawMovementType.getAssetId());
                assetList.add(asset);
                if (isPluginTypeWithoutMobileTerminal(rawMovementType.getPluginType()) && asset != null) {
                    MobileTerminalType mobileTerminal = findMobileTerminalByAsset(asset.getAssetId().getGuid());
                    rawMovementType.setMobileTerminal(MobileTerminalMapper.mapMobileTerminal(mobileTerminal));
                    mobileTerminalList.add(mobileTerminal);
                } else {
                    mobileTerminalList.add(null); // to maintain same indexes with the other 2 lists.
                }
            }
        }
        int index = 0;
        for (RawMovementType rawMovementType : rawMovementList) {
            if (rawMovementType.getAssetId() == null && CollectionUtils.isNotEmpty(assetList)) {
                AssetId assetId = AssetAssetIdMapper.mapAssetToAssetId(assetList.get(index));
                rawMovementType.setAssetId(assetId);
                index++;
            }
        }
        return new EnrichedMovementWrapper(assetList, mobileTerminalList);
    }

    private List<String> collectConnectIds(List<MobileTerminalType> mobileTerminals) {
        List<String> connetctIds = new ArrayList<>();
        for (MobileTerminalType mobileTerminal : mobileTerminals) {
            connetctIds.add(mobileTerminal.getConnectId());
        }
        return connetctIds;
    }

    private boolean allFactsAreOk(List<RawMovementFact> rawMovementFactList) {
        boolean areAllOk = true;
        for (RawMovementFact movementFact : rawMovementFactList) {
            if (movementFact == null || !movementFact.isOk()) {
                areAllOk = false;
                break;
            }
        }
        return areAllOk;
    }

    private List<MobileTerminalType> getMobileTerminalByRawMovementsBatch(List<RawMovementType> rawMovements) throws MessageException, MobileTerminalModelMapperException, MobileTerminalUnmarshallException, JMSException {
        log.info(" Fetch mobile terminal");
        List<MobileTerminalType> responseList = new ArrayList<>();
        List<MobileTerminalListQuery> query = generateMobileTerminalBatchListQuery(rawMovements);
        if (query == null) {
            return null;
        }
        String mobileTerminalBatchListRequestStr = MobileTerminalModuleRequestMapper.createMobileTerminalBatchListRequest(query);
        log.debug("Send MobileTerminalListRequest message to Mobile terminal");
        String getMobileTerminalMessageId = mobileTerminalProducer.sendModuleMessage(mobileTerminalBatchListRequestStr, consumer.getDestination());
        TextMessage getMobileTerminalResponse = consumer.getMessage(getMobileTerminalMessageId, TextMessage.class);
        log.debug("Received response message");
        List<MobileTerminalBatchListElement> resultList = MobileTerminalModuleResponseMapper.mapToMobileTerminalBatchListResponse(getMobileTerminalResponse);
        for (MobileTerminalBatchListElement mobileTerminalBatchListElement : resultList) {
            responseList.add(mobileTerminalBatchListElement.getMobileTerminal().size() != 1 ? null : mobileTerminalBatchListElement.getMobileTerminal().get(0));
        }
        return responseList;
    }

    private List<MobileTerminalListQuery> generateMobileTerminalBatchListQuery(List<RawMovementType> rawMovements) {
        if (CollectionUtils.isEmpty(rawMovements)) {
            return null;
        }
        List<MobileTerminalListQuery> batchQuery = new ArrayList<>();
        for (RawMovementType rawMovement : rawMovements) {
            batchQuery.add(generateMobileTerminalListQuery(rawMovement));
        }
        return batchQuery;
    }

    /**
     * Creates the dynamic query to send to MobileTerminal.
     *
     * @param rawMovement
     * @return
     */
    private MobileTerminalListQuery generateMobileTerminalListQuery(RawMovementType rawMovement) {
        MobileTerminalListQuery query = new MobileTerminalListQuery();
        // If no mobile terminal information exists, don't look for one
        if (rawMovement.getMobileTerminal() == null || rawMovement.getMobileTerminal().getMobileTerminalIdList() == null) {
            return null;
        }
        List<IdList> ids = rawMovement.getMobileTerminal().getMobileTerminalIdList();
        MobileTerminalSearchCriteria criteria = new MobileTerminalSearchCriteria();
        for (IdList id : ids) {
            ListCriteria crit = new ListCriteria();
            switch (id.getType()) {
                case DNID:
                    if (id.getValue() != null) {
                        crit.setKey(SearchKey.DNID);
                        crit.setValue(id.getValue());
                        criteria.getCriterias().add(crit);
                    }
                    break;
                case MEMBER_NUMBER:
                    if (id.getValue() != null) {
                        crit.setKey(SearchKey.MEMBER_NUMBER);
                        crit.setValue(id.getValue());
                        criteria.getCriterias().add(crit);
                    }
                    break;
                case SERIAL_NUMBER:
                    if (id.getValue() != null) {
                        crit.setKey(SearchKey.SERIAL_NUMBER);
                        crit.setValue(id.getValue());
                        criteria.getCriterias().add(crit);
                    }
                    break;
                case LES:
                default:
                    log.error(" Unhandled Mobile Terminal id: {} ]", id.getType());
                    break;
            }
        }
        // If no valid criterias, don't look for a mobile terminal
        if (criteria.getCriterias().isEmpty()) {
            return null;
        }
        // If we know the transponder type from the source, use it in the search criteria
        ListCriteria transponderTypeCrit = new ListCriteria();
        transponderTypeCrit.setKey(SearchKey.TRANSPONDER_TYPE);
        transponderTypeCrit.setValue(rawMovement.getSource().name());
        criteria.getCriterias().add(transponderTypeCrit);

        query.setMobileTerminalSearchCriteria(criteria);
        ListPagination pagination = new ListPagination();
        // To leave room to find erroneous results - it must be only one in the list
        pagination.setListSize(2);
        pagination.setPage(1);
        query.setPagination(pagination);
        return query;
    }

    private void sendBatchBackToExchange(String guid, List<RawMovementType> rawMovement, MovementRefTypeType status, String username) throws MessageException {
        log.info("Sending back processed movement BATCH to exchange");
        List<MovementRefType> movTypeList = new ArrayList<>();
        for (RawMovementType rawMovementType : rawMovement) {
            // Map response
            MovementRefType movementRef = new MovementRefType();
            movementRef.setMovementRefGuid(guid);
            movementRef.setType(status);
            movementRef.setAckResponseMessageID(rawMovementType.getAckResponseMessageID());
            movTypeList.add(movementRef);
        }
        // Map movement
        List<SetReportMovementType> setReportMovementType = ExchangeMovementMapper.mapExchangeMovementBatch(rawMovement);
        try {
            String exchangeResponseText = ExchangeMovementMapper.mapToProcessedMovementResponseBatch(setReportMovementType, movTypeList, username);
            exchangeProducer.sendModuleMessage(exchangeResponseText, consumer.getDestination());
        } catch (ExchangeModelMapperException e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<Asset> getAssetByConnectIdsBatch(List<String> connectIds) throws AssetModelMapperException, MessageException {
        log.info(" Fetch asset by connectId '{}'", connectIds);
        if (CollectionUtils.isEmpty(connectIds)) {
            return null;
        }
        List<AssetListQuery> assetListBatch = new ArrayList<>();
        for (String connectId : connectIds) {
            AssetListQuery query = new AssetListQuery();
            AssetListCriteria criteria = new AssetListCriteria();
            AssetListCriteriaPair criteriaPair = new AssetListCriteriaPair();
            criteriaPair.setKey(ConfigSearchField.GUID);
            criteriaPair.setValue(connectId);
            criteria.getCriterias().add(criteriaPair);
            criteria.setIsDynamic(true);

            query.setAssetSearchCriteria(criteria);

            AssetListPagination pagination = new AssetListPagination();
            // To leave room to find erroneous results - it must be only one in the list
            pagination.setListSize(2);
            pagination.setPage(1);
            query.setPagination(pagination);
            assetListBatch.add(query);
        }
        String getAssetRequest = AssetModuleRequestMapper.createBatchAssetListModuleRequest(assetListBatch);
        log.debug("Send AssetListModuleRequest message to Asset");
        String getAssetMessageId = assetProducer.sendModuleMessage(getAssetRequest, consumer.getDestination());
        TextMessage getAssetResponse = consumer.getMessage(getAssetMessageId, TextMessage.class);
        log.debug("Received response message");
        List<BatchAssetListResponseElement> resultList = AssetModuleResponseMapper.mapToBatchAssetListFromResponse(getAssetResponse, getAssetMessageId);
        List<Asset> assetRespList = new ArrayList<>();
        for (BatchAssetListResponseElement batchAssetListResponseElement : resultList) {
            List<Asset> asset = batchAssetListResponseElement.getAsset();
            assetRespList.add(asset.size() != 1 ? null : asset.get(0));
        }
        return assetRespList;
    }

    private Asset getAssetByCfrIrcs(AssetId assetId) {
        log.info(" Fetch asset by assetId");
        Asset asset;
        try {
            // If no asset information exists, don't look for one
            if (assetId == null || assetId.getAssetIdList() == null) {
                log.warn("No asset information exists!");
                return null;
            }
            List<AssetIdList> ids = assetId.getAssetIdList();
            String cfr = null;
            String ircs = null;
            String mmsi = null;
            // Get possible search parameters
            for (AssetIdList id : ids) {
                if (eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdType.CFR.equals(id.getIdType())) {
                    cfr = id.getValue();
                }
                if (eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdType.IRCS.equals(id.getIdType())) {
                    ircs = id.getValue();
                }
                if (eu.europa.ec.fisheries.schema.rules.asset.v1.AssetIdType.MMSI.equals(id.getIdType())) {
                    mmsi = id.getValue();
                }

            }
            if (ircs != null && cfr != null && mmsi != null) {
                try {
                    asset = getAsset(AssetIdType.CFR, cfr);
                    // If the asset matches on ircs as well we have a winner
                    if (asset != null && asset.getIrcs().equals(ircs)) {
                        return asset;
                    }
                    // If asset is null, try fetching by IRCS (cfr will fail for SE national db)
                    if (asset == null) {
                        asset = getAsset(AssetIdType.IRCS, ircs);
                        // If asset is still null, try mmsi (this should be the case for movement coming from AIS)
                        if (asset == null) {
                            return getAsset(AssetIdType.MMSI, mmsi);
                        }
                    }
                } catch (AssetModelValidationException e) {
                    return getAsset(AssetIdType.IRCS, ircs);
                }
            } else if (cfr != null) {
                return getAsset(AssetIdType.CFR, cfr);
            } else if (ircs != null) {
                return getAsset(AssetIdType.IRCS, ircs);
            } else if (mmsi != null) {
                return getAsset(AssetIdType.MMSI, mmsi);
            }
        } catch (Exception e) {
            // Log and continue validation
            log.warn("Could not find asset!");
        }
        return null;
    }

    private Asset getAsset(AssetIdType type, String value) throws AssetModelMapperException, MessageException {
        String getAssetListRequest = AssetModuleRequestMapper.createGetAssetModuleRequest(value, type);
        log.debug("Send GetAssetModuleRequest message to Asset");
        String getAssetMessageId = assetProducer.sendModuleMessage(getAssetListRequest, consumer.getDestination());
        TextMessage getAssetResponse = consumer.getMessage(getAssetMessageId, TextMessage.class);
        log.debug("Received response message");

        return AssetModuleResponseMapper.mapToAssetFromResponse(getAssetResponse, getAssetMessageId);
    }

    private boolean isPluginTypeWithoutMobileTerminal(String pluginType) {
        if (pluginType == null) {
            return true;
        }
        try {
            PluginType type = PluginType.valueOf(pluginType);
            switch (type) {
                case MANUAL:
                case NAF:
                case OTHER:
                    return true;
                default:
                    return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private MobileTerminalType findMobileTerminalByAsset(String assetGuid) throws MessageException, MobileTerminalModelMapperException, MobileTerminalUnmarshallException, JMSException {
        MobileTerminalListQuery query = new MobileTerminalListQuery();
        eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ListPagination pagination = new eu.europa.ec.fisheries.schema.mobileterminal.types.v1.ListPagination();
        pagination.setListSize(2);
        pagination.setPage(1);
        MobileTerminalSearchCriteria criteria = new MobileTerminalSearchCriteria();
        ListCriteria guidCriteria = new ListCriteria();
        guidCriteria.setKey(SearchKey.CONNECT_ID);
        guidCriteria.setValue(assetGuid);
        criteria.getCriterias().add(guidCriteria);
        query.setMobileTerminalSearchCriteria(criteria);
        query.setPagination(pagination);

        String request = MobileTerminalModuleRequestMapper.createMobileTerminalListRequest(query);
        log.debug("Send MobileTerminalListRequest message to Mobile terminal");
        String messageId = mobileTerminalProducer.sendModuleMessage(request, consumer.getDestination());
        TextMessage getMobileTerminalResponse = consumer.getMessage(messageId, TextMessage.class);
        log.debug("Received response message");
        List<MobileTerminalType> resultList = MobileTerminalModuleResponseMapper.mapToMobileTerminalListResponse(getMobileTerminalResponse);
        MobileTerminalType mobileTerminal = null;
        for (MobileTerminalType mobileTerminalType : resultList) {
            if (mobileTerminalType.getConnectId() != null) {
                mobileTerminal = mobileTerminalType;
                break;
            }
        }
        return mobileTerminal;
    }

    /**
     * TODO : This method simplifies the things up until the movement validation is ready again!
     * TODO : When it is ready please use collectMovementData(...){...} method instead!.. modifying it in order to support batches of movements!
     *
     * @param asset
     * @param rawMovements
     * @param username
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws RulesServiceException
     */
    private CreateMovementBatchResponse sendBatchToMovement(List<Asset> asset, List<RawMovementType> rawMovements, final String username) throws RulesServiceException {
        List<String> connectIds = collectConnectIdsFromAssetList(asset, rawMovements);
        // Get data from parallel tasks
        try {
            log.info(" Send the validated raw position to Movement..");
            Date auditTimestamp = new Date();
            CreateMovementBatchResponse movementSimpleResponse = null;
            List<MovementBaseType> movementBatch = new ArrayList<>();
            int index = 0;
            for (RawMovementType rawMovement : rawMovements) {
                MovementBaseType movementBaseType = MovementBaseTypeMapper.mapRawMovementFact(rawMovement);
                movementBaseType.setConnectId(connectIds.get(index));
                movementBatch.add(movementBaseType);
                index++;
            }
            try {
                String createMovementRequest = MovementModuleRequestMapper.mapToCreateMovementBatchRequest(movementBatch, username);
                log.debug("Send CreateMovementRequest message to Movement");
                warnOnMultipleConnectIds(connectIds);
                String connectId = connectIds.get(0);
                String messageId = movementProducer.sendModuleMessageInGroup(createMovementRequest, consumer.getDestination(), connectId);
                TextMessage movJmsResponse = consumer.getMessage(messageId, TextMessage.class, 2400000L);
                log.debug("Received response message");
                movementSimpleResponse = MovementModuleResponseMapper.mapToCreateMovementBatchResponse(movJmsResponse);
            } catch (JMSException | MessageException e) {
                log.error(" Error when getting movementResponse from Movement , movementResponse from JMS Queue is null..", e);
            } catch (MovementModelException e) {
                if (e.getCode() == MOVEMENT_DUPLICATE_ERROR) {
                    log.error(" Error when getting movementResponse from Movement, tried to create duplicate movement..", e);
                } else {
                    log.error(" Other Movement error", e);
                }
            }
            auditLog("Time to get movement from Movement Module:", auditTimestamp);
            return movementSimpleResponse;
        } catch (RulesServiceException | NullPointerException e) {
            throw new RulesServiceException("Error likely caused by a duplicate movement.", e);
        }
    }

    private void warnOnMultipleConnectIds(List<String> connectIds) {
        if (connectIds.size() == 1) {
            return;
        }
        Set<String> uniqueConnectIds = new HashSet<>(connectIds);
        if (uniqueConnectIds.size() > 1) {
            log.warn("*** connectIds with {} different elements, the JMSXGroupId will be wrong! The first two elements are: {} ***", uniqueConnectIds.size(), uniqueConnectIds.stream().limit(2).collect(Collectors.joining(",","[","]")));
        }
    }

    private List<String> collectConnectIdsFromAssetList(List<Asset> assets, List<RawMovementType> rawMovements) {
        List<String> connectIdsList = new ArrayList<>();
        int index = 0;
        for (Asset asset : assets) {
            if (asset != null && asset.getAssetId() != null && asset.getEventHistory() != null) {
                connectIdsList.add(asset.getEventHistory().getEventId());
            } else {
                connectIdsList.add(null);
                log.warn("[WARN] Asset was null for {} ", rawMovements.get(index).getAssetId());
            }
            index++;
        }
        return connectIdsList;
    }

    private MovementFact collectMovementData(MobileTerminalType mobileTerminal, Asset asset, final RawMovementType rawMovement, final String username) throws ExecutionException, InterruptedException, RulesServiceException {
        int threadNum = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadNum);
        Integer numberOfReportsLast24Hours;
        final String assetGuid;
        final String assetHistGuid;
        final String assetFlagState;
        if (asset != null && asset.getAssetId() != null && asset.getEventHistory() != null) {
            assetGuid = asset.getAssetId().getGuid();
            assetHistGuid = asset.getEventHistory().getEventId();
            assetFlagState = asset.getCountryCode();
        } else {
            log.warn("[WARN] Asset was null for {} ", rawMovement.getAssetId());
            assetGuid = null;
            assetHistGuid = null;
            assetFlagState = null;
        }

        final Date positionTime = rawMovement.getPositionTime();

        FutureTask<Long> timeDiffAndPersistMovementTask = new FutureTask<>(new Callable<Long>() {
            @Override
            public Long call() {
                return timeDiffAndPersistMovement(rawMovement.getSource(), assetGuid, assetFlagState, positionTime);
            }
        });
        executor.execute(timeDiffAndPersistMovementTask);

        FutureTask<Integer> numberOfReportsLast24HoursTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() {
                return numberOfReportsLast24Hours(assetHistGuid, positionTime);
            }
        });
        executor.execute(numberOfReportsLast24HoursTask);

        FutureTask<MovementType> sendToMovementTask = new FutureTask<>(new Callable<MovementType>() {
            @Override
            public MovementType call() {
                return sendToMovement(assetHistGuid, rawMovement, username);
            }
        });
        executor.execute(sendToMovementTask);

        FutureTask<List<AssetGroup>> assetGroupTask = new FutureTask<>(new Callable<List<AssetGroup>>() {
            @Override
            public List<AssetGroup> call() {
                return getAssetGroup(assetGuid);
            }
        });
        executor.execute(assetGroupTask);

        FutureTask<List<String>> vicinityOfTask = new FutureTask<>(new Callable<List<String>>() {
            @Override
            public List<String> call() {
                return getVicinityOf(rawMovement);
            }
        });
        executor.execute(vicinityOfTask);

        // Get channel guid
        String channelGuid = "";
        if (mobileTerminal != null) {
            channelGuid = getChannelGuid(mobileTerminal, rawMovement);
        }

        // Get channel type
        String comChannelType = null;
        if (rawMovement.getComChannelType() != null) {
            comChannelType = rawMovement.getComChannelType().name();
        }

        // Get data from parallel tasks
        try {
            Long timeDiffInSeconds = timeDiffAndPersistMovementTask.get();
            List<AssetGroup> assetGroups = assetGroupTask.get();
            numberOfReportsLast24Hours = numberOfReportsLast24HoursTask.get();
            MovementType createdMovement = sendToMovementTask.get();
            List<String> vicinityOf = vicinityOfTask.get();
            MovementFact movementFact = MovementFactMapper.mapMovementFact(createdMovement, mobileTerminal, asset, comChannelType, assetGroups, timeDiffInSeconds, numberOfReportsLast24Hours, channelGuid, vicinityOf);
            executor.shutdown();
            return movementFact;
        } catch (RulesServiceException | NullPointerException e) {
            executor.shutdown();
            throw new RulesServiceException("Error likely caused by a duplicate movement.", e);
        }
    }

    // Todo ; When possible remove loop!
    private List<MovementFact> collectBatchMovementData(List<MobileTerminalType> mobileTerminal, List<Asset> asset, List<RawMovementType> rawMovement, String username) {
        List<MovementFact> movFactList = new ArrayList<>();
        int index = 0;
        for (RawMovementType rawMovementType : rawMovement) {
            try {
                movFactList.add(collectMovementData(mobileTerminal.get(index), asset.get(index), rawMovementType, username));
            } catch (ExecutionException | InterruptedException | RulesServiceException e) {
                movFactList.add(null);
                Thread.currentThread().interrupt();
            }
        }
        return movFactList;
    }

    private Long timeDiffAndPersistMovement(MovementSourceType movementSource, String assetGuid, String assetFlagState, Date positionTime) {
        Date auditTimestamp = new Date();
        // This needs to be done before persisting last report
        Long timeDiffInSeconds;
        Long timeDiff = timeDiffFromLastCommunication(assetGuid, positionTime);
        timeDiffInSeconds = timeDiff != null ? timeDiff / 1000 : null;

        // We only persist our own last communications that were not from AIS.
        if (isLocalFlagstate(assetFlagState) && !movementSource.equals(MovementSourceType.AIS)) {
            persistLastCommunication(assetGuid, positionTime);
        }
        return timeDiffInSeconds;
    }


    private Date auditLog(String msg, Date lastTimestamp) {
        Date newTimestamp = new Date();
        long duration = newTimestamp.getTime() - lastTimestamp.getTime();
        log.debug(" --> AUDIT - {} {} ms", msg, duration);
        return newTimestamp;
    }

    private Long timeDiffFromLastCommunication(String assetGuid, Date thisTime) {
        log.info(" Fetching time difference to previous movement report");
        Long timeDiff = null;
        try {
            PreviousReportType previousReport = rulesDomainModel.getPreviousReportByAssetGuid(assetGuid);
            Date previousTime = previousReport.getPositionTime();
            timeDiff = thisTime.getTime() - previousTime.getTime();
        } catch (Exception e) {
            // If something goes wrong, continue with the other validation
            log.warn("[WARN] Error when fetching time difference of previous movement reports..");
        }
        return timeDiff;
    }

    private void persistLastCommunication(String assetGuid, Date positionTime) {
        PreviousReportType thisReport = new PreviousReportType();
        thisReport.setPositionTime(positionTime);
        thisReport.setAssetGuid(assetGuid);
        String upsertPreviousReportequest = null;
        try {
            rulesDomainModel.upsertPreviousReport(thisReport);
        } catch (RulesModelException e) {
            log.error(" Error persisting report. ] {}", e.getMessage());
        }
    }

    private boolean isLocalFlagstate(String assetFlagState) {
        if (assetFlagState == null) {
            return false;
        }
        try {
            String settingsRequest = ModuleRequestMapper.toListSettingsRequest("asset");
            log.debug("Send ListSettingsRequest message to Config");
            String messageId = configProducer.sendModuleMessage(settingsRequest, consumer.getDestination());
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            log.debug("Received response message");
            SettingsListResponse settings = eu.europa.ec.fisheries.uvms.config.model.mapper.JAXBMarshaller.unmarshallTextMessage(response, SettingsListResponse.class);
            for (SettingType setting : settings.getSettings()) {
                if (setting.getKey().equals("asset.default.flagstate")) {
                    return assetFlagState.equalsIgnoreCase(setting.getValue());
                }
            }
        } catch (eu.europa.ec.fisheries.uvms.config.model.exception.ModelMapperException | MessageException e) {
            return false;
        }
        return false;
    }

    private Integer numberOfReportsLast24Hours(String connectId, Date thisTime) {
        log.info(" Fetching number of reports last 24 hours");
        Date auditTimestamp = new Date();
        Integer numberOfMovements = null;
        MovementQuery query = new MovementQuery();

        // Range
        RangeCriteria dateRangeCriteria = new RangeCriteria();
        dateRangeCriteria.setKey(RangeKeyType.DATE);
        Date twentyFourHoursAgo = new Date(thisTime.getTime() - TWENTYFOUR_HOURS_IN_MILLISEC);
        dateRangeCriteria.setFrom(twentyFourHoursAgo.toString());
        //String to = RulesUtil.xmlGregorianToString(thisTime);
        dateRangeCriteria.setTo(thisTime.toString());
        query.getMovementRangeSearchCriteria().add(dateRangeCriteria);

        // Id
        eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria idCriteria = new eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria();
        idCriteria.setKey(eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey.CONNECT_ID);
        idCriteria.setValue(connectId);
        query.getMovementSearchCriteria().add(idCriteria);

        try {
            String request = MovementModuleRequestMapper.mapToGetMovementMapByQueryRequest(query);
            log.debug("Send GetMovementMapByQueryRequest message to Movement");
            String messageId = movementProducer.sendModuleMessage(request, consumer.getDestination());
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            log.debug("Received response message");

            List<MovementMapResponseType> result = MovementModuleResponseMapper.mapToMovementMapResponse(response);

            List<MovementType> movements;

            if (result == null || result.isEmpty()) {
                log.warn("[WARN] Error when fetching sum of previous movement reports : No result found");
                return null;
            } else if (result.size() != 1) {
                log.warn("[WARN] Error when fetching sum of previous movement reports: Duplicate assets found ({})", result.size());
                return null;
            } else if (!connectId.equals(result.get(0).getKey())) {
                log.warn("[WARN] Error when fetching sum of previous movement reports: Wrong asset found ({})", result.get(0).getKey());
                return null;
            } else {
                movements = result.get(0).getMovements();
            }
            numberOfMovements = movements != null ? movements.size() : 0;
        } catch (Exception e) {
            // If something goes wrong, continue with the other validation
            log.warn(" Error when fetching sum of previous movement reports:{} ]", e.getMessage());
        }
        auditLog("Time to fetch number of reports last 24 hours:", auditTimestamp);
        return numberOfMovements;
    }

    private MovementType sendToMovement(String connectId, RawMovementType rawMovement, String username) {
        log.info(" Send the validated raw position to Movement..");
        Date auditTimestamp = new Date();
        MovementType createdMovement = null;
        try {
            MovementBaseType movementBaseType = MovementBaseTypeMapper.mapRawMovementFact(rawMovement);
            movementBaseType.setConnectId(connectId);
            String createMovementRequest = MovementModuleRequestMapper.mapToCreateMovementRequest(movementBaseType, username);
            log.debug("Send CreateMovementRequest message to Movement");
            String messageId = movementProducer.sendModuleMessage(createMovementRequest, consumer.getDestination());
            TextMessage movementResponse = consumer.getMessage(messageId, TextMessage.class, 10000L);
            log.debug("Received response message");
            CreateMovementResponse createMovementResponse = MovementModuleResponseMapper.mapToCreateMovementResponseFromMovementResponse(movementResponse);
            createdMovement = createMovementResponse.getMovement();
        } catch (JMSException | MessageException e) {
            log.error(" Error when getting movementResponse from Movement , movementResponse from JMS Queue is null..", e);
        } catch (MovementModelException e) {
            if (e.getCode() == MOVEMENT_DUPLICATE_ERROR) {
                log.error(" Error when getting movementResponse from Movement, tried to create duplicate movement..", e);
            } else {
                log.error(" Other Movement error", e);
            }
        }
        auditLog("Time to get movement from Movement Module:", auditTimestamp);
        return createdMovement;
    }

    private List<String> getVicinityOf(RawMovementType rawMovement) {
        long start = System.currentTimeMillis();
        List<String> vicinityOf = new ArrayList<>();
        /*
        try {
            MovementQuery query = new MovementQuery();
            query.setExcludeFirstAndLastSegment(true);

            RangeCriteria time = new RangeCriteria();
            //GregorianCalendar from = rawMovement.getPositionTime().toGregorianCalendar();
            //from.add(Calendar.HOUR_OF_DAY, -1);
            Date fromDate = new Date(rawMovement.getPositionTime().getTime() - TWENTYFOUR_HOURS_IN_MILLISEC);
            time.setKey(RangeKeyType.DATE);
            time.setFrom(fromDate.toString());
            time.setTo(rawMovement.getPositionTime().toString());
            query.getMovementRangeSearchCriteria().add(time);

            eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination pagination = new eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination();
            pagination.setListSize(BigInteger.valueOf(1000L));
            pagination.setPage(BigInteger.ONE);
            query.setPagination(pagination);

            String request = MovementModuleRequestMapper.mapToGetMovementListByQueryRequest(query);
            String messageId = rulesProducer.sendDataSourceMessage(request, DataSourceQueue.MOVEMENT);
            TextMessage movementResponse = consumer.getMessage(messageId, TextMessage.class);
            List<MovementType> movements = MovementModuleResponseMapper.mapToMovementListResponse(movementResponse);
            double centerX = rawMovement.getPosition().getLongitude();
            double centerY = rawMovement.getPosition().getLatitude();
            List<String> guidList = new ArrayList<>();
            for (MovementType movement : movements) {
                if (guidList.contains(movement.getConnectId())) {
                    continue;
                }
                double x = movement.getPosition().getLongitude();
                double y = movement.getPosition().getLatitude();
                double distance = Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2);
                if (distance < VICINITY_RADIUS) {
                    guidList.add(movement.getConnectId());
                    Asset asset = getAssetByConnectId(movement.getConnectId());
                    vicinityOf.add(asset.getIrcs());
                }
                //(x - center_x)^2 + (y - center_y)^2 < radius^2
            }
        } catch (AssetModelMapperException | JMSException | MessageException | ModelMapperException | MovementFaultException | MovementDuplicateException e) {
            log.warn("Could not fetch movements for vicinity of.");
        }

        log.debug("[ Get nearby vessels: {} ms ]", (System.currentTimeMillis() - start));
        */
        return vicinityOf;
    }

    private List<AssetGroup> getAssetGroup(String assetGuid) {
        log.info("Fetch asset groups from Asset");
        TextMessage getAssetResponse;
        String getAssetMessageId;
        List<AssetGroup> assetGroups = null;
        try {
            String getAssetRequest = AssetModuleRequestMapper.createAssetGroupListByAssetGuidRequest(assetGuid);
            log.debug("Send GetAssetGroupListByAssetGuidRequest message to Asset");
            getAssetMessageId = assetProducer.sendModuleMessage(getAssetRequest, consumer.getDestination());
            getAssetResponse = consumer.getMessage(getAssetMessageId, TextMessage.class);
            log.debug("Received response message");
            assetGroups = AssetModuleResponseMapper.mapToAssetGroupListFromResponse(getAssetResponse, getAssetMessageId);
        } catch (AssetModelMapperException | MessageException e) {
            log.warn("[ Failed while fetching asset groups ]", e.getMessage());
        }
        return assetGroups;
    }

    // TODO: Implement for IRIDIUM as well (if needed)
    private String getChannelGuid(MobileTerminalType mobileTerminal, RawMovementType rawMovement) {
        String dnid = StringUtils.EMPTY;
        String memberNumber = StringUtils.EMPTY;
        String channelGuid = StringUtils.EMPTY;
        List<IdList> ids = rawMovement.getMobileTerminal().getMobileTerminalIdList();
        for (IdList id : ids) {
            switch (id.getType()) {
                case DNID:
                    if (id.getValue() != null) {
                        dnid = id.getValue();
                    }
                    break;
                case MEMBER_NUMBER:
                    if (id.getValue() != null) {
                        memberNumber = id.getValue();
                    }
                    break;
                case SERIAL_NUMBER:
                    // IRIDIUM
                case LES:
                default:
                    log.error(" Unhandled Mobile Terminal id: {} ]", id.getType());
                    break;
            }
        }

        // Get the channel guid
        boolean correctDnid = false;
        boolean correctMemberNumber = false;
        List<ComChannelType> channels = mobileTerminal.getChannels();
        for (ComChannelType channel : channels) {
            List<ComChannelAttribute> attributes = channel.getAttributes();
            for (ComChannelAttribute attribute : attributes) {
                String type = attribute.getType();
                String value = attribute.getValue();
                if ("DNID".equals(type)) {
                    correctDnid = value.equals(dnid);
                }
                if ("MEMBER_NUMBER".equals(type)) {
                    correctMemberNumber = value.equals(memberNumber);
                }
            }
            if (correctDnid && correctMemberNumber) {
                channelGuid = channel.getGuid();
            }
        }
        return channelGuid;
    }

    public String reprocessAlarm(List<String> alarmGuids, String username) throws RulesServiceException {
        log.info(" Reprocess alarms invoked in service layer");
        try {
            AlarmQuery query = mapToOpenAlarmQuery(alarmGuids);
            AlarmListResponseDto alarms = rulesDomainModel.getAlarmListByQuery(query);

            for (AlarmReportType alarm : alarms.getAlarmList()) {
                // Cannot reprocess without a movement (i.e. "Asset not sending" alarm)
                if (alarm.getRawMovement() == null) {
                    continue;
                }

                if(alarm.getAlarmItem().stream().map(a -> a.getRuleGuid() == null).findAny().orElse(false)) {
                    throw new RulesServiceException("Cannot reprocess an alarm that is not rule originated");
                }
                // Mark the alarm as REPROCESSED before reprocessing. That will create a new alarm (if still wrong) with the items remaining.
                alarm.setStatus(AlarmStatusType.REPROCESSED);
                alarm = updateAlarmStatus(alarm);
                sendAuditMessage(AuditObjectTypeEnum.ALARM, AuditOperationEnum.UPDATE, alarm.getGuid(), null, username);
                RawMovementType rawMovementType = alarm.getRawMovement();
                // TODO: Use better type (some variation of PluginType...)
                String pluginType = alarm.getPluginType();
                processReceivedMovementsAsBatch(Collections.singletonList(rawMovementType), pluginType, pluginType, username);
            }
//            return RulesDataSourceResponseMapper.mapToAlarmListFromResponse(response, messageId);
            // TODO: Better
            return "OK";
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }


    private AlarmQuery mapToOpenAlarmQuery(List<String> alarmGuids) {
        AlarmQuery query = new AlarmQuery();
        eu.europa.ec.fisheries.schema.rules.search.v1.ListPagination pagination = new eu.europa.ec.fisheries.schema.rules.search.v1.ListPagination();
        pagination.setListSize(alarmGuids.size());
        pagination.setPage(1);
        query.setPagination(pagination);

        for (String alarmGuid : alarmGuids) {
            AlarmListCriteria criteria = new AlarmListCriteria();
            criteria.setKey(AlarmSearchKey.ALARM_GUID);
            criteria.setValue(alarmGuid);
            query.getAlarmSearchCriteria().add(criteria);
        }

        // We only want open alarms
        AlarmListCriteria openCrit = new AlarmListCriteria();
        openCrit.setKey(AlarmSearchKey.STATUS);
        openCrit.setValue(AlarmStatusType.OPEN.name());
        query.getAlarmSearchCriteria().add(openCrit);
        query.setDynamic(true);
        return query;
    }

    public AlarmReportType updateAlarmStatus(AlarmReportType alarm) throws RulesServiceException {
        log.info(" Update alarm status invoked in service layer");
        try {
            AlarmReportType updatedAlarm = rulesDomainModel.setAlarmStatus(alarm);
            // Notify long-polling clients of the change
            alarmReportEvent.fire(new NotificationMessage("guid", updatedAlarm.getGuid()));
            // Notify long-polling clients of the change (no vlaue since FE will need to fetch it)
            alarmReportCountEvent.fire(new NotificationMessage("alarmCount", null));
            sendAuditMessage(AuditObjectTypeEnum.ALARM, AuditOperationEnum.UPDATE, updatedAlarm.getGuid(), null, alarm.getUpdatedBy());
            return updatedAlarm;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    private String getOrganisationName(String userName) throws eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException, MessageException, RulesModelMarshallException {
        String userRequest = UserModuleRequestMapper.mapToGetContactDetailsRequest(userName);
        log.debug("Send GetContactDetailsRequest message to User");
        String userMessageId = userProducer.sendModuleMessage(userRequest, consumer.getDestination());
        TextMessage userMessage = consumer.getMessage(userMessageId, TextMessage.class);
        log.debug("Received response message");
        GetContactDetailResponse userResponse = JAXBMarshaller.unmarshallTextMessage(userMessage, GetContactDetailResponse.class);

        if (userResponse != null && userResponse.getContactDetails() != null) {
            return userResponse.getContactDetails().getOrganisationName();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param customRule
     * @throws RulesServiceException
     * @throws RulesFaultException
     */
    public CustomRuleType createCustomRule(CustomRuleType customRule, String featureName, String applicationName) throws RulesServiceException, RulesFaultException, AccessDeniedException {
        log.info(" Create invoked in service layer");
        try {
            // Get organisation of user
            String organisationName = getOrganisationName(customRule.getUpdatedBy());
            if (organisationName != null) {
                customRule.setOrganisation(organisationName);
            } else {
                log.warn("User {} is not connected to any organisation!", customRule.getUpdatedBy());
            }
            if (customRule.getAvailability().equals(AvailabilityType.GLOBAL)) {
                UserContext userContext = getFullUserContext(customRule.getUpdatedBy(), applicationName);
                if (!hasFeature(userContext, featureName)) {
                    throw new AccessDeniedException("Forbidden access");
                }
            }
            CustomRuleType createdRule = rulesDomainModel.createCustomRule(customRule);
            // TODO: Rewrite so rules are loaded when changed
            movementValidator.updateCustomRules();
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.CREATE, createdRule.getGuid(), null, customRule.getUpdatedBy());
            return createdRule;

        } catch (RulesModelMapperException | MessageException | RulesModelException | eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param guid
     * @return
     * @throws RulesServiceException
     */
    public CustomRuleType getCustomRuleByGuid(String guid) throws RulesServiceException {
        log.info(" Get Custom Rule by guid invoked in service layer");
        try {
            CustomRuleType customRule = rulesDomainModel.getByGuid(guid);
            return customRule;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param oldCustomRule
     * @throws RulesServiceException
     */
    public CustomRuleType updateCustomRule(CustomRuleType oldCustomRule, String featureName, String applicationName) throws RulesServiceException, RulesFaultException, AccessDeniedException {
        log.info(" Update custom rule invoked in service layer");
        try {
            // Get organisation of user
            String organisationName = getOrganisationName(oldCustomRule.getUpdatedBy());
            if (organisationName != null) {
                oldCustomRule.setOrganisation(organisationName);
            } else {
                log.warn("User {} is not connected to any organisation!", oldCustomRule.getUpdatedBy());
            }
            if (oldCustomRule.getAvailability().equals(AvailabilityType.GLOBAL)) {
                UserContext userContext = getFullUserContext(oldCustomRule.getUpdatedBy(), applicationName);
                if (!hasFeature(userContext, featureName)) {
                    throw new AccessDeniedException("Forbidden access");
                }
            }
            CustomRuleType customRule = rulesDomainModel.updateCustomRule(oldCustomRule);
            movementValidator.updateCustomRules();
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.UPDATE, customRule.getGuid(), null, oldCustomRule.getUpdatedBy());
            return customRule;
        } catch (RulesModelMapperException | MessageException | eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException | RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param oldCustomRule
     * @throws RulesServiceException
     */
    public CustomRuleType updateCustomRule(CustomRuleType oldCustomRule) throws RulesServiceException, RulesFaultException {
        log.info(" Update custom rule invoked in service layer by timer");
        try {
            CustomRuleType updatedCustomRule = rulesDomainModel.updateCustomRule(oldCustomRule);
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.UPDATE, updatedCustomRule.getGuid(), null, oldCustomRule.getUpdatedBy());
            return updatedCustomRule;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param updateSubscriptionType
     */
    public CustomRuleType updateSubscription(UpdateSubscriptionType updateSubscriptionType, String username) throws RulesServiceException, RulesFaultException {
        log.info(" Update subscription invoked in service layer");
        try {
            boolean validRequest = updateSubscriptionType.getSubscription().getType() != null && updateSubscriptionType.getSubscription().getOwner() != null;
            if (!validRequest) {
                throw new RulesServiceException("Not a valid subscription!");
            }
            CustomRuleType updateCustomRule = rulesDomainModel.updateCustomRuleSubscription(updateSubscriptionType);
            if (SubscritionOperationType.ADD.equals(updateSubscriptionType.getOperation())) {
                // TODO: Don't log rule guid, log subscription guid?
                sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE_SUBSCRIPTION, AuditOperationEnum.CREATE, updateSubscriptionType.getRuleGuid(), updateSubscriptionType.getSubscription().getOwner() + "/" + updateSubscriptionType.getSubscription().getType(), username);
            } else if (SubscritionOperationType.REMOVE.equals(updateSubscriptionType.getOperation())) {
                // TODO: Don't log rule guid, log subscription guid?
                sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE_SUBSCRIPTION, AuditOperationEnum.DELETE, updateSubscriptionType.getRuleGuid(), updateSubscriptionType.getSubscription().getOwner() + "/" + updateSubscriptionType.getSubscription().getType(), username);
            }
            return updateCustomRule;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param guid
     * @throws RulesServiceException
     */
    public CustomRuleType deleteCustomRule(String guid, String username, String featureName, String applicationName) throws RulesServiceException, RulesFaultException, AccessDeniedException {
        log.info(" Deleting custom rule by guid: {}.", guid);
        if (guid == null) {
            throw new InputArgumentException("No custom rule to remove");
        }

        try {
            CustomRuleType customRuleFromDb = getCustomRuleByGuid(guid);
            if (customRuleFromDb.getAvailability().equals(AvailabilityType.GLOBAL)) {
                UserContext userContext = getFullUserContext(username, applicationName);
                if (!hasFeature(userContext, featureName)) {
                    throw new AccessDeniedException("Forbidden access");
                }
            }

            CustomRuleType deletedRule = rulesDomainModel.deleteCustomRule(guid);
            movementValidator.updateCustomRules();
            sendAuditMessage(AuditObjectTypeEnum.CUSTOM_RULE, AuditOperationEnum.DELETE, deletedRule.getGuid(), null, username);
            return deletedRule;
        } catch (RulesModelMapperException | RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     * @throws RulesServiceException
     */
    public GetAlarmListByQueryResponse getAlarmList(AlarmQuery query) throws RulesServiceException, RulesFaultException {
        log.info(" Get alarm list invoked in service layer");
        try {
            AlarmListResponseDto alarmList = rulesDomainModel.getAlarmListByQuery(query);
            GetAlarmListByQueryResponse response = new GetAlarmListByQueryResponse();
            response.getAlarms().addAll(alarmList.getAlarmList());
            response.setTotalNumberOfPages(alarmList.getTotalNumberOfPages());
            response.setCurrentPage(alarmList.getCurrentPage());
            return response;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    public GetTicketListByQueryResponse getTicketList(String loggedInUser, TicketQuery query) throws RulesServiceException, RulesFaultException {
        log.info(" Get ticket list invoked in service layer");
        try {
            TicketListResponseDto ticketList = rulesDomainModel.getTicketListByQuery(loggedInUser, query);
            GetTicketListByQueryResponse response = new GetTicketListByQueryResponse();
            response.setCurrentPage(ticketList.getCurrentPage());
            response.setTotalNumberOfPages(ticketList.getTotalNumberOfPages());
            response.getTickets().addAll(ticketList.getTicketList());
            return response;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    public GetTicketListByMovementsResponse getTicketsByMovements(List<String> movements) throws RulesServiceException, RulesFaultException {
        log.info(" Get tickets by movements invoked in service layer");
        try {
            TicketListResponseDto ticketListByMovements = rulesDomainModel.getTicketListByMovements(movements);
            GetTicketListByMovementsResponse response = new GetTicketListByMovementsResponse();
            response.getTickets().addAll(ticketListByMovements.getTicketList());
            return response;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    public GetTicketsAndRulesByMovementsResponse getTicketsAndRulesByMovements(List<String> movements) throws RulesServiceException {
        log.info(" Get tickets and rules by movements invoked in service layer");
        try {
            List<TicketAndRuleType> ticketsAndRulesByMovements = rulesDomainModel.getTicketsAndRulesByMovements(movements);
            GetTicketsAndRulesByMovementsResponse response = new GetTicketsAndRulesByMovementsResponse();
            response.getTicketsAndRules().addAll(ticketsAndRulesByMovements);
            return response;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    public long countTicketsByMovements(List<String> movements) throws RulesServiceException, RulesFaultException {
        log.info(" Get number of tickets by movements invoked in service layer");
        try {
            return rulesDomainModel.countTicketListByMovements(movements);
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    public TicketType updateTicketStatus(TicketType ticket) throws RulesServiceException {
        log.info(" Update ticket status invoked in service layer");
        try {
            TicketType updatedTicket = rulesDomainModel.setTicketStatus(ticket);
            // Notify long-polling clients of the update
            ticketUpdateEvent.fire(new NotificationMessage("guid", updatedTicket.getGuid()));
            // Notify long-polling clients of the change (no value since FE will need to fetch it)
            ticketCountEvent.fire(new NotificationMessage("ticketCount", null));
            sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.UPDATE, updatedTicket.getGuid(), ticket.getComment(), ticket.getUpdatedBy());
            return updatedTicket;


        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    public List<TicketType> updateTicketStatusByQuery(String loggedInUser, TicketQuery query, TicketStatusType status) throws RulesServiceException {
        log.info(" Update all ticket status invoked in service layer");
        try {
            List<TicketType> updatedTickets = rulesDomainModel.updateTicketStatusByQuery(loggedInUser, query, status);
            // Notify long-polling clients of the update
            for (TicketType updatedTicket : updatedTickets) {
                ticketUpdateEvent.fire(new NotificationMessage("guid", updatedTicket.getGuid()));
                sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.UPDATE, updatedTicket.getGuid(), null, loggedInUser);
            }
            // Notify long-polling clients of the change (no value since FE will need to fetch it)
            ticketCountEvent.fire(new NotificationMessage("ticketCount", null));
            return updatedTickets;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    public long getNumberOfAssetsNotSending() throws RulesServiceException {
        try {
            long numberOfAssetsNotSending = rulesDomainModel.getNumberOfAssetsNotSending();
            return numberOfAssetsNotSending;
        } catch (RulesModelException e) {
            throw new RulesServiceException("[ Error when getting number of open alarms. ]");
        }
    }

    // Triggered by RulesTimerBean
    public List<PreviousReportType> getPreviousMovementReports() throws RulesServiceException, RulesFaultException {
        log.info(" Get previous movement reports invoked in service layer");
        try {
            return rulesDomainModel.getPreviousReports();
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    // Triggered by timer rule
    public void timerRuleTriggered(String ruleName, PreviousReportFact fact) throws RulesServiceException, RulesFaultException {
        log.info(" Timer rule triggered invoked in service layer");
        try {
            // Check if ticket already is created for this asset
            TicketType ticket = rulesDomainModel.getTicketByAssetGuid(fact.getAssetGuid(), ruleName);
            if (ticket == null) {
                createAssetNotSendingTicket(ruleName, fact);
            } else if (ticket.getTicketCount() != null) {
                ticket.setTicketCount(ticket.getTicketCount() + 1);
                updateTicketCount(ticket);
            } else {
                ticket.setTicketCount(2L);
                updateTicketCount(ticket);
            }
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    private void createAssetNotSendingTicket(String ruleName, PreviousReportFact fact) throws RulesModelException {
        TicketType ticket = new TicketType();

        ticket.setAssetGuid(fact.getAssetGuid());
        ticket.setOpenDate(RulesUtil.dateToString(new Date()));
        ticket.setRuleName(ruleName);
        ticket.setRuleGuid(ruleName);
        ticket.setUpdatedBy("UVMS");
        ticket.setStatus(TicketStatusType.OPEN);
        ticket.setMovementGuid(fact.getMovementGuid());
        ticket.setGuid(UUID.randomUUID().toString());
        TicketType createdTicket = rulesDomainModel.createTicket(ticket);
        sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.CREATE, createdTicket.getGuid(), null, ticket.getUpdatedBy());
        // Notify long-polling clients of the change
        ticketCountEvent.fire(new NotificationMessage("ticketCount", null));
    }

    public TicketType updateTicketCount(TicketType ticket) throws RulesServiceException {
        log.info(" Update ticket count invoked in service layer");
        try {
            TicketType updatedTicket = rulesDomainModel.updateTicketCount(ticket);
            // Notify long-polling clients of the update
            ticketUpdateEvent.fire(new NotificationMessage("guid", updatedTicket.getGuid()));
            // Notify long-polling clients of the change (no value since FE will need to fetch it)
            ticketCountEvent.fire(new NotificationMessage("ticketCount", null));
            sendAuditMessage(AuditObjectTypeEnum.TICKET, AuditOperationEnum.UPDATE, updatedTicket.getGuid(), null, ticket.getUpdatedBy());
            return updatedTicket;
        } catch (RulesModelException e) {
            throw new RulesServiceException(e.getMessage());
        }
    }

    public AlarmReportType getAlarmReportByGuid(String guid) throws RulesServiceException {
        try {
            AlarmReportType alarmReport = rulesDomainModel.getAlarmReportByGuid(guid);
            return alarmReport;
        } catch (RulesModelException e) {
            throw new RulesServiceException("[ Error when getting alarm by GUID. ]");
        }
    }

    public TicketType getTicketByGuid(String guid) throws RulesServiceException {
        try {
            TicketType ticket = rulesDomainModel.getTicketByGuid(guid);
            return ticket;
        } catch (RulesModelException e) {
            throw new RulesServiceException("[ Error when getting ticket by GUID. ]");
        }
    }

    private void sendAuditMessage(AuditObjectTypeEnum type, AuditOperationEnum operation, String affectedObject, String comment, String username) {
        try {
            String message = AuditLogMapper.mapToAuditLog(type.getValue(), operation.getValue(), affectedObject, comment, username);
            auditProducer.sendModuleMessage(message, consumer.getDestination());
        } catch (AuditModelMarshallException | MessageException e) {
            log.error(" Error when sending message to Audit. ] {}", e.getMessage());
        }
    }

    private UserContext getFullUserContext(String remoteUser, String applicationName) throws RulesServiceException, RulesModelMarshallException {
        log.debug("Request getFullUserContext({}, {})", remoteUser, applicationName);
        UserContext userContext = null;
        UserContextId contextId = new UserContextId();
        contextId.setApplicationName(applicationName);
        contextId.setUserName(remoteUser);
        String userRequest;
        try {
            userRequest = UserModuleRequestMapper.mapToGetUserContextRequest(contextId);
            String messageId = userProducer.sendModuleMessage(userRequest, consumer.getDestination());
            log.debug("JMS message with ID: {} is sent to USM.", messageId);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            log.debug("Received response message");
            if (response != null) {
                GetUserContextResponse userContextResponse = JAXBMarshaller.unmarshallTextMessage(response, GetUserContextResponse.class);
                log.debug("Response concerning message with ID: {} is received.", messageId);
                userContext = userContextResponse.getContext();
            } else {
                log.error("Error occurred while receiving JMS response for message ID: {}", messageId);
                throw new RulesServiceException("Unable to receive a response from USM.");
            }
        } catch (eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException e) {
            throw new RulesModelMarshallException("Unexpected exception while trying to get user context.", e);
        } catch (MessageException e) {
            log.error("Unable to receive a response from USM.");
            throw new RulesServiceException("Unable to receive a response from USM.");
        }
        return userContext;
    }

    private boolean hasFeature(UserContext userContext, String featureName) {
        for (eu.europa.ec.fisheries.wsdl.user.types.Context c : userContext.getContextSet().getContexts()) {
            for (Feature f : c.getRole().getFeature()) {
                if (featureName.equals(f.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateRequestMessageStatusInExchange(String logGuid, ExchangeLogStatusTypeType statusType) {
        try {
            String statusMsg = ExchangeModuleRequestMapper.createUpdateLogStatusRequest(logGuid, statusType);
            log.debug("Message to exchange to update status : {}", statusMsg);
            exchangeProducer.sendModuleMessage(statusMsg, consumer.getDestination());
        } catch (ExchangeModelMarshallException | MessageException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }
}
