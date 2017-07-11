/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.mdr.model.exception.MdrModelMarshallException;
import eu.europa.ec.fisheries.uvms.mdr.model.mapper.MdrModuleMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.dto.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.service.MessageService;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXResponseDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationQualityAnalysis;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ValidationResultDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

/**
 * Created by padhyad on 5/9/2017.
 */
@Stateless
@Slf4j
public class MessageServiceBean implements MessageService {

    @EJB
    RulesResponseConsumer consumer;

    @EJB
    RulesMessageProducer producer;

    @EJB
    RulesEngineBean rulesEngine;

    @EJB
    RulePostProcessBean rulePostprocessBean;

    @EJB
    RulesPreProcessBean rulesPreProcessBean;

    @EJB
    RulesConfigurationCache ruleModuleCache;


    @Override
    public void setFLUXFAReportMessageReceived(SetFLUXFAReportMessageRequest request) throws RulesServiceException {
        log.debug("inside setFLUXFAReportMessageReceived", request.getRequest());
        try {
            FLUXFAReportMessage fluxfaReportMessage = JAXBMarshaller.unMarshallMessage(request.getRequest(), FLUXFAReportMessage.class);
            if (fluxfaReportMessage != null) {
                FLUXResponseMessage fluxResponseMessageType;
                Map<Boolean, ValidationResultDto> validationMap = rulesPreProcessBean.checkDuplicateIdInRequest(fluxfaReportMessage);
                boolean isContinueValidation = validationMap.entrySet().iterator().next().getKey();
                log.info("Validation continue : {}", isContinueValidation);

                if (isContinueValidation) {
                    log.info("Trigger rule engine to do validation of incoming message");
                    List<AbstractFact> faReportFacts = rulesEngine.evaluate(BusinessObjectType.FLUX_ACTIVITY_REQUEST_MSG, fluxfaReportMessage);
                    ValidationResultDto faReportValidationResult = rulePostprocessBean.checkAndUpdateValidationResult(faReportFacts, request.getRequest());
                    updateValidationResultWithExisting(faReportValidationResult, validationMap.get(isContinueValidation));

                    updateRequestMessageStatus(request.getLogGuid(), faReportValidationResult);

                    if (faReportValidationResult!= null && !faReportValidationResult.isError()) {
                        log.info("Validation of Report is successful, forwarding message to Activity");
                        log.debug("message to activity : {}", request.getRequest());
                        sendRequestToActivity(request.getRequest(), request.getUsername(), request.getType());
                    }
                    fluxResponseMessageType = generateFluxResponseMessage(faReportValidationResult, fluxfaReportMessage);
                    XPathRepository.INSTANCE.clear(faReportFacts);
                } else {
                    updateRequestMessageStatus(request.getLogGuid(), validationMap.get(isContinueValidation));
                    fluxResponseMessageType = generateFluxResponseMessage(validationMap.get(isContinueValidation), fluxfaReportMessage);
                    log.info("Validation of FLUXFAReport is complete and FluxResponse is generated");
                }
                sendResponseToExchange(fluxResponseMessageType, request.getUsername());
            }
        } catch (RulesValidationException e) {
            log.error(e.getMessage(), e);
            updateRequestMessageStatus(request.getLogGuid(), null);
        }
        catch (RulesModelMarshallException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    private void updateRequestMessageStatus(String logGuid, ValidationResultDto faReportValidationResult) throws RulesServiceException {
        try {
            ExchangeLogStatusTypeType exchangeLogStatusTypeType;
            if (faReportValidationResult != null) {
                if (faReportValidationResult.isError()) {
                    exchangeLogStatusTypeType = ExchangeLogStatusTypeType.FAILED;
                } else if (faReportValidationResult.isWarning()) {
                    exchangeLogStatusTypeType = ExchangeLogStatusTypeType.SUCCESSFUL_WITH_WARNINGS;
                } else {
                    exchangeLogStatusTypeType = ExchangeLogStatusTypeType.SUCCESSFUL;
                }
            } else {
                exchangeLogStatusTypeType = ExchangeLogStatusTypeType.UNKNOWN;
            }
            String statusMsg = ExchangeModuleRequestMapper.createUpdateLogStatusRequest(logGuid, exchangeLogStatusTypeType);
            log.debug("Message to exchange to update status : {}", statusMsg);
            producer.sendDataSourceMessage(statusMsg, DataSourceQueue.EXCHANGE);
        } catch (ExchangeModelMarshallException | MessageException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    private void updateValidationResultWithExisting(ValidationResultDto faReportValidationResult, ValidationResultDto previousValidationResultDto) {
        if (previousValidationResultDto != null) {
            faReportValidationResult.setIsError(faReportValidationResult.isError() || previousValidationResultDto.isError());
            faReportValidationResult.setIsWarning(faReportValidationResult.isWarning() || previousValidationResultDto.isWarning());
            faReportValidationResult.setIsOk(faReportValidationResult.isOk() || previousValidationResultDto.isOk());
            faReportValidationResult.getValidationMessages().addAll(previousValidationResultDto.getValidationMessages());
        }
    }

    private List<ValidationResultDocument> getValidationResultDocument(ValidationResultDto faReportValidationResult) throws DatatypeConfigurationException {
        ValidationResultDocument validationResultDocument = new ValidationResultDocument();

        GregorianCalendar date = DateTime.now(DateTimeZone.UTC).toGregorianCalendar();
        XMLGregorianCalendar calender = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
        DateTimeType dateTime = new DateTimeType();
        dateTime.setDateTime(calender);
        validationResultDocument.setCreationDateTime(dateTime);

        IDType idType = new IDType();
        idType.setValue("XEU"); // TODO to be received from Global config nation_code
        idType.setSchemeID("FLUX_GP_PARTY");
        validationResultDocument.setValidatorID(idType);

        List<ValidationQualityAnalysis> validationQuality = new ArrayList<>();
        for (ValidationMessageType validationMessage : faReportValidationResult.getValidationMessages()) {
            ValidationQualityAnalysis analysis = new ValidationQualityAnalysis();

            IDType identification = new IDType();
            identification.setValue(validationMessage.getBrId());
            analysis.setID(identification);

            CodeType level = new CodeType();
            level.setValue(validationMessage.getLevel());
            analysis.setLevelCode(level);

            CodeType type = new CodeType();
            type.setValue(validationMessage.getErrorType().value());
            analysis.setTypeCode(type);

            TextType text = new TextType();
            text.setValue(validationMessage.getMessage());
            analysis.getResults().add(text);

            List<String> xpaths = validationMessage.getXpaths();
            if(CollectionUtils.isNotEmpty(xpaths)){
                for(String xpath : xpaths){
                    TextType referenceItem = new TextType();
                    referenceItem.setValue(xpath);
                    analysis.getReferencedItems().add(referenceItem);
                }
            }

            validationQuality.add(analysis);
        }
        validationResultDocument.setRelatedValidationQualityAnalysises(validationQuality);
        return Arrays.asList(validationResultDocument);
    }

    @Override
    public FLUXResponseMessage generateFluxResponseMessage(ValidationResultDto faReportValidationResult, FLUXFAReportMessage fluxfaReportMessage) {
        FLUXResponseMessage responseMessage = new FLUXResponseMessage();
        try {
            FLUXResponseDocument fluxResponseDocument = new FLUXResponseDocument();

            IDType responseId = new IDType();
            responseId.setValue(UUID.randomUUID().toString());
            responseId.setSchemeID("UUID");
            fluxResponseDocument.setIDS(Arrays.asList(responseId)); // Set random ID
            if (fluxfaReportMessage.getFLUXReportDocument() != null) {
                List<IDType> requestId = fluxfaReportMessage.getFLUXReportDocument().getIDS();
                fluxResponseDocument.setReferencedID((requestId != null && !requestId.isEmpty()) ? requestId.get(0) : null); // Set Request Id
            }
            GregorianCalendar date = DateTime.now(DateTimeZone.UTC).toGregorianCalendar();
            XMLGregorianCalendar calender = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
            DateTimeType dateTime = new DateTimeType();
            dateTime.setDateTime(calender);
            fluxResponseDocument.setCreationDateTime(dateTime); // Set creation date time

            CodeType responseCode = new CodeType();
            if (faReportValidationResult.isError()) {
                responseCode.setValue("NOK");
            } else if (faReportValidationResult.isWarning()) {
                responseCode.setValue("WOK");
            } else {
                responseCode.setValue("OK");
            }
            responseCode.setListID("FLUX_GP_RESPONSE");
            fluxResponseDocument.setResponseCode(responseCode); // Set response Code

            if (faReportValidationResult.isError() || faReportValidationResult.isWarning()) {
                TextType rejectionReason = new TextType();
                rejectionReason.setValue("VALIDATION");
                fluxResponseDocument.setRejectionReason(rejectionReason); // Set rejection reason
            }

            fluxResponseDocument.setRelatedValidationResultDocuments(getValidationResultDocument(faReportValidationResult)); // Set validation result

            fluxResponseDocument.setRespondentFLUXParty(getRespondedFluxParty()); // Set flux party in the response

            responseMessage.setFLUXResponseDocument(fluxResponseDocument);
        } catch (DatatypeConfigurationException e) {
            log.error(e.getMessage(), e);
        }
        return responseMessage;
    }

    private FLUXParty getRespondedFluxParty() {
        IDType idType = new IDType();
        String fluxNationCode = ruleModuleCache.getSingleConfig("flux_local_nation_code");
        String nationCode = StringUtils.isNotEmpty(fluxNationCode) ? fluxNationCode : "XEU";
        idType.setValue(nationCode);
        idType.setSchemeID("FLUX_GP_PARTY");
        FLUXParty fluxParty = new FLUXParty();
        fluxParty.setIDS(Arrays.asList(idType));
        return fluxParty;
    }

    public void sendRequestToActivity(String fluxFAReportMessage, String username, eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType pluginType) throws RulesServiceException {
        try {
            String setFLUXFAReportMessageRequest = ActivityModuleRequestMapper.mapToSetFLUXFAReportMessageRequest(fluxFAReportMessage, username, pluginType.toString());
            producer.sendDataSourceMessage(setFLUXFAReportMessageRequest, DataSourceQueue.ACTIVITY);
        } catch (ActivityModelMarshallException | MessageException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void sendResponseToExchange(FLUXResponseMessage fluxResponseMessageType, String username) throws RulesServiceException {
        try {
            //Validate response message
            String fluxResponse = JAXBMarshaller.marshallJaxBObjectToString(fluxResponseMessageType);
            List<AbstractFact> fluxResponseFacts = rulesEngine.evaluate(BusinessObjectType.FLUX_ACTIVITY_RESPONSE_MSG, fluxResponseMessageType);
            ValidationResultDto fluxResponseValidationResult = rulePostprocessBean.checkAndUpdateValidationResult(fluxResponseFacts, fluxResponse);

            ExchangeLogStatusTypeType status;
            if (fluxResponseValidationResult.isError()) {
                status = ExchangeLogStatusTypeType.FAILED;
            } else if (fluxResponseValidationResult.isWarning()) {
                status = ExchangeLogStatusTypeType.SUCCESSFUL_WITH_WARNINGS;
            } else {
                status = ExchangeLogStatusTypeType.SUCCESSFUL;
            }
            //Create Response
            String fluxNationCode = ruleModuleCache.getSingleConfig("flux_local_nation_code");
            String nationCode = StringUtils.isNotEmpty(fluxNationCode) ? fluxNationCode : "XEU";
            String df = "urn:un:unece:uncefact:fisheries:FLUX:FA:EU:2"; // TODO should come from subscription. Also could be a link between DF and AD value
            String destination = "AHR:VMS";
            String messageGuid = ActivityFactMapper.getUUID(fluxResponseMessageType.getFLUXResponseDocument().getIDS());
            String fluxFAReponseText = ExchangeModuleRequestMapper.createFluxFAResponseRequest(fluxResponse, username, df, messageGuid, nationCode, status, destination);
            log.debug("Message to exchange {}", fluxFAReponseText);

            producer.sendDataSourceMessage(fluxFAReponseText, DataSourceQueue.EXCHANGE);
            XPathRepository.INSTANCE.clear(fluxResponseFacts);
            log.info("FLUXFAResponse has been sent back to Exchange.");
        } catch (RulesModelMarshallException | ExchangeModelMarshallException | MessageException | RulesValidationException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }


    /*
	 * Maps a Request String to a eu.europa.ec.fisheries.schema.exchange.module.v1.SetFLUXMDRSyncMessageRequest
	 * to send a message to ExchangeModule
	 *
	 * @see eu.europa.ec.fisheries.uvms.rules.service.RulesService#mapAndSendFLUXMdrRequestToExchange(java.lang.String)
	 */
    @Override
    public void mapAndSendFLUXMdrRequestToExchange(String request) {
        String exchangerStrReq;
        try {
            exchangerStrReq = ExchangeModuleRequestMapper.createFluxMdrSyncEntityRequest(request, StringUtils.EMPTY);
            if(StringUtils.isNotEmpty(exchangerStrReq)){
                producer.sendDataSourceMessage(exchangerStrReq, DataSourceQueue.EXCHANGE);
            } else {
                log.error("ERROR : REQUEST TO BE SENT TO EXCHANGE MODULE RESULTS NULL. NOT SENDING IT!");
            }

        } catch (ExchangeModelMarshallException e) {
            log.error("Unable to marshall SetFLUXMDRSyncMessageRequest in RulesServiceBean.mapAndSendFLUXMdrRequestToExchange(String) : "+e.getMessage());
        } catch (MessageException e) {
            log.error("Unable to send SetFLUXMDRSyncMessageRequest to ExchangeModule : "+e.getMessage());
        }
    }

    @Override
    public void mapAndSendFLUXMdrResponseToMdrModule(String request) {
        String mdrSyncResponseReq;
        try {
            mdrSyncResponseReq = MdrModuleMapper.createFluxMdrSyncEntityRequest(request, StringUtils.EMPTY);
            if(StringUtils.isNotEmpty(mdrSyncResponseReq)){
                producer.sendDataSourceMessage(mdrSyncResponseReq, DataSourceQueue.MDR_EVENT);
            } else {
                log.error("ERROR : REQUEST TO BE SENT TO MDR MODULE RESULTS NULL. NOT SENDING IT!");
            }
        } catch (MdrModelMarshallException e) {
            log.error("Unable to marshall SetFLUXMDRSyncMessageResponse in RulesServiceBean.mapAndSendFLUXMdrResponseToMdrModule(String) : "+e.getMessage());
        } catch (MessageException e) {
            log.error("Unable to send SetFLUXMDRSyncMessageResponse to MDR Module : "+e.getMessage());
        }

    }
}