/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.activity;

import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFluxFaResponseMessageRequest;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulePostProcessBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesConfigurationCache;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesExchangeServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleError;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResult;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.Rule9998Or9999ErrorType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CodeTypeMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesFLUXMessageHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.MDC;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.bind.UnmarshalException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import static eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType.FA_QUERY;
import static eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType.FA_RESPONSE;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_RESPONSE_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.DATA_FLOW;

@Stateless
@LocalBean
@Slf4j
public class RulesFAResponseServiceBean {

    private static ValidationResult failure = new ValidationResult(true, false, false, null);

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private RulePostProcessBean ruleService;

    @EJB
    private RulesDao rulesDaoBean;

    @EJB
    private RulesExchangeServiceBean exchangeServiceBean;

    @EJB
    private RulesConfigurationCache ruleModuleCache;

    private RulesFLUXMessageHelper fluxMessageHelper;

    @Inject
    private CodeTypeMapper codeTypeMapper;

    @PostConstruct
    public void init() {
        fluxMessageHelper = new RulesFLUXMessageHelper(ruleModuleCache);
    }

    public void evaluateIncomingFluxResponseRequest(SetFluxFaResponseMessageRequest request) {
        if (request == null){
            throw new IllegalArgumentException("SetFluxFaResponseMessageRequest is null");
        }
        String requestStr = request.getRequest();
        String logGuid = request.getLogGuid();
        String dataFlow = request.getFluxDataFlow();
        log.info("Evaluate FLUXResponseMessage with GUID " + logGuid);
        FLUXResponseMessage fluxResponseMessage;
        try {
            // Validate xsd schema
            fluxResponseMessage = fluxMessageHelper.unMarshallFluxResponseMessage(requestStr);
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(DATA_FLOW, dataFlow);

            Collection<AbstractFact> fluxFaResponseFacts = rulesEngine.evaluate(RECEIVING_FA_RESPONSE_MSG, fluxResponseMessage, extraValues, String.valueOf(fluxResponseMessage.getFLUXResponseDocument().getIDS()));
            ValidationResult fluxResponseValidResults = ruleService.checkAndUpdateValidationResult(fluxFaResponseFacts, requestStr, logGuid, FA_RESPONSE);
            exchangeServiceBean.updateExchangeMessage(logGuid, fluxMessageHelper.calculateMessageValidationStatus(fluxResponseValidResults));
            if (fluxResponseValidResults != null && !fluxResponseValidResults.isError()) {
                log.debug("The Validation of FLUXResponseMessage is successful, forwarding message to Exchange");
            } else {
                log.debug("Validation resulted in errors. Not going to send msg to Exchange module..");
            }
            XPathRepository.INSTANCE.clear(fluxFaResponseFacts);

        } catch (UnmarshalException e) {
            log.debug("Error while trying to parse FLUXResponseMessage received message! It is malformed! Reason : {{}}", e.getMessage());
            exchangeServiceBean.updateExchangeMessage(logGuid, fluxMessageHelper.calculateMessageValidationStatus(failure));
            throw new RulesServiceException(e.getMessage(), e);
        } catch (RulesValidationException e) {
            log.debug("Error during validation of the received FLUXResponseMessage!", e);
            exchangeServiceBean.updateExchangeMessage(logGuid, fluxMessageHelper.calculateMessageValidationStatus(failure));
        }
    }

    public void sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(String rawMessage, RulesBaseRequest request, FLUXFAQueryMessage queryMessage, Rule9998Or9999ErrorType type, String onValue, ValidationResult faQueryValidationReport) {
        if (request == null || type == null) {
            log.error("Could not send FLUXResponseMessage. Request is null or Rule9998Or9999ErrorType not provided.");
            return;
        }
        RuleError ruleWarning;
        if (Rule9998Or9999ErrorType.EMPTY_REPORT.equals(type)) {
            ruleWarning = new RuleError(ServiceConstants.EMPTY_REPORT_RULE, ServiceConstants.EMPTY_REPORT_RULE_MESSAGE, "L03", Collections.<String>singletonList(null));
        } else {
            ruleWarning = new RuleError(ServiceConstants.PERMISSION_DENIED_RULE, ServiceConstants.PERMISSION_DENIED_RULE_MESSAGE, "L00", Collections.<String>singletonList(null));
        }

        ValidationResult validationResultDto = ruleService.checkAndUpdateValidationResultForGeneralBusinessRules(ruleWarning, rawMessage, request.getLogGuid(), FA_QUERY, request.getDate());
        validationResultDto.setError(true);
        validationResultDto.setOk(false);

        if (CollectionUtils.isNotEmpty(faQueryValidationReport.getValidationMessages())){
            validationResultDto.getValidationMessages().addAll(faQueryValidationReport.getValidationMessages());
        }

        FLUXResponseMessage fluxResponseMessage = fluxMessageHelper.generateFluxResponseMessageForFaQuery(validationResultDto, queryMessage, onValue);
        log.debug("FLUXResponseMessage has been generated after exception: " + fluxResponseMessage);
        exchangeServiceBean.evaluateAndSendToExchange(fluxResponseMessage, request, PluginType.FLUX, true, MDC.getCopyOfContextMap());
    }
}