/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;
import java.util.*;
import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.FANamespaceMapper;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.ActivityOutQueueConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.RulesFAResponseServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResult;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesFLUXMessageHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import static eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType.FA_QUERY;
import static eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType.FA_REPORT;
import static eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType.FA_RESPONSE;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.SENDING_FA_RESPONSE_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.XML;

@Stateless
@LocalBean
@Slf4j
public class RulesExchangeServiceBean {

    private RulesFLUXMessageHelper fluxMessageHelper;

    @EJB
    private RulesConfigurationCache ruleModuleCache;

    @EJB
    private RulePostProcessBean ruleService;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private ActivityOutQueueConsumer activityConsumer;

    @EJB
    private RulesFAResponseServiceBean faResponseServiceBean;

    @EJB
    private RulesMessageProducer rulesProducer;

    @EJB
    private RulesDao rulesDaoBean;

    @PostConstruct
    public void init() {
        fluxMessageHelper = new RulesFLUXMessageHelper(ruleModuleCache);
    }

    public void evaluateAndSendToExchange(FLUXResponseMessage fluxResponseMessageObj, RulesBaseRequest request, PluginType pluginType, boolean correctGuidProvided, Map<String, String> copyOfContextMap) {

        String id = fluxMessageHelper.getIDs(fluxResponseMessageObj);
        if (StringUtils.isEmpty(id)){
            throw new IllegalArgumentException("ID is null");
        }
        String logGuid = request.getLogGuid();
        String onValue = request.getOnValue();
        String df = request.getFluxDataFlow(); //e.g. "urn:un:unece:uncefact:fisheries:FLUX:FA:EU:2" // TODO should come from subscription. Also could be a link between DF and AD value
        try {
            MDC.setContextMap(copyOfContextMap);
            log.info("Preparing FLUXResponseMessage to send back to Exchange module.");
            if (!correctGuidProvided) {
                fluxMessageHelper.fillFluxTLOnValue(fluxResponseMessageObj, onValue);
            }

            // Get fluxNationCode (Eg. XEU) from Config Module.
            String fluxNationCode = ruleModuleCache.getSingleConfig(fluxMessageHelper.FLUX_LOCAL_NATION_CODE);

            // Get the actual Response ids and match them with the Response Ids from the DB
            Set<FADocumentID> idsFromIncommingMessage = fluxMessageHelper.mapToResponseToFADocumentID(fluxResponseMessageObj);
            List<FADocumentID> matchingIdsFromDB = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncommingMessage);

            String fluxResponse = JAXBUtils.marshallJaxBObjectToString(fluxResponseMessageObj, "UTF-8", false, new FANamespaceMapper());
            Map<ExtraValueType, Object> extraValues = fluxMessageHelper.populateExtraValuesMap(fluxNationCode, matchingIdsFromDB);
            extraValues.put(XML, fluxResponse);

            Collection<AbstractFact> fluxResponseFacts = rulesEngine.evaluate(SENDING_FA_RESPONSE_MSG, fluxResponseMessageObj, extraValues);
            ValidationResult fluxResponseValidationResult = ruleService.checkAndUpdateValidationResult(fluxResponseFacts, fluxResponse, logGuid, FA_RESPONSE);
            ExchangeLogStatusTypeType status = fluxMessageHelper.calculateMessageValidationStatus(fluxResponseValidationResult);
            //Create Response
            // We need to link the message that came in with the FLUXResponseMessage we're sending... That's the why of the commented line here..
            //String messageGuid = ActivityFactMapper.getUUID(fluxResponseMessageType.getFLUXResponseDocument().getIDS());
            String fluxFAResponseText = ExchangeModuleRequestMapper.createFluxFAResponseRequestWithOnValue(fluxResponse, request.getUsername(), df, logGuid, request.getSenderOrReceiver(), onValue, status, request.getSenderOrReceiver(), getExchangePluginType(pluginType), id);

            sendToExchange(fluxFAResponseText);

            XPathRepository.INSTANCE.clear(fluxResponseFacts);

            idsFromIncommingMessage.removeAll(matchingIdsFromDB); // To avoid duplication in DB.
            rulesDaoBean.createFaDocumentIdEntity(idsFromIncommingMessage);
            log.info("FLUXFAResponse successfully sent back to Exchange.");
        } catch (JAXBException e) {
            log.error(e.getMessage(), e);
            sendFLUXResponseMessageOnException(e.getMessage(), null, request, fluxResponseMessageObj);
        } catch (ExchangeModelMarshallException | MessageException | RulesValidationException | ServiceException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    public void sendFLUXResponseMessageOnException(String errorMessage, String rawMessage, RulesBaseRequest request, Object message) {
        if (request == null) {
            log.error("Could not send FLUXResponseMessage. Request is null.");
            return;
        }
        List<String> xpaths = new ArrayList<>();
        xpaths.add(errorMessage);
        RuleError ruleError = new RuleError(ServiceConstants.INVALID_XML_RULE, ServiceConstants.INVALID_XML_RULE_MESSAGE, "L00", xpaths);
        RawMsgType messageType = fluxMessageHelper.getMessageType(request.getMethod());
        ValidationResult validationResultDto = ruleService.checkAndUpdateValidationResultForGeneralBusinessRules(ruleError, rawMessage, request.getLogGuid(), messageType);
        validationResultDto.setError(true);
        validationResultDto.setOk(false);
        FLUXResponseMessage fluxResponseMessage;
        if (FA_QUERY.equals(messageType)){
            FLUXFAQueryMessage fluxfaQueryMessage = message != null ? (FLUXFAQueryMessage) message : null;
            String onValue = request.getOnValue(); // Is this needed?
            fluxResponseMessage = fluxMessageHelper.generateFluxResponseMessageForFaQuery(validationResultDto, fluxfaQueryMessage, onValue);
        } else if (FA_REPORT.equals(messageType)){
            FLUXFAReportMessage fluxfaReportMessage = message != null ? (FLUXFAReportMessage) message : null;
            fluxResponseMessage = fluxMessageHelper.generateFluxResponseMessageForFaReport(validationResultDto, fluxfaReportMessage);
        } else if (FA_RESPONSE.equals(messageType)){
            FLUXResponseMessage fluxResponsMsg = message != null ? (FLUXResponseMessage) message : null;
            fluxResponseMessage = fluxMessageHelper.generateFluxResponseMessageForFaResponse(validationResultDto, fluxResponsMsg);
        } else {
            fluxResponseMessage = fluxMessageHelper.generateFluxResponseMessage(validationResultDto);
        }
        if (fluxResponseMessage != null) {
            fluxMessageHelper.fillFluxTLOnValue(fluxResponseMessage, request.getOnValue());
            log.debug("FLUXResponseMessage has been generated after exception: " + fluxResponseMessage);
            evaluateAndSendToExchange(fluxResponseMessage, request, PluginType.FLUX, false, MDC.getCopyOfContextMap());
        }
    }

    public void updateExchangeMessage(String logGuid, ExchangeLogStatusTypeType statusType) {
        try {
            String statusMsg = ExchangeModuleRequestMapper.createUpdateLogStatusRequest(logGuid, statusType);
            log.debug("Message to exchange to update status : {}", statusMsg);
            rulesProducer.sendDataSourceMessage(statusMsg, DataSourceQueue.EXCHANGE);
        } catch (ExchangeModelMarshallException | MessageException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    public void sendToExchange(String message) throws MessageException {
        rulesProducer.sendDataSourceMessage(message, DataSourceQueue.EXCHANGE);
    }

    private eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType getExchangePluginType(PluginType pluginType) {
        if (pluginType == PluginType.BELGIAN_ACTIVITY) {
            return eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType.BELGIAN_ACTIVITY;
        } else {
            return eu.europa.ec.fisheries.schema.exchange.plugin.types.v1.PluginType.FLUX;
        }
    }

}
