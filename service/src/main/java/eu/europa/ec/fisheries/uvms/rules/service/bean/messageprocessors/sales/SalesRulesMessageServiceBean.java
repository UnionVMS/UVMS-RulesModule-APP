package eu.europa.ec.fisheries.uvms.rules.service.bean.messageprocessors.sales;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.module.v1.*;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigServiceException;
import eu.europa.ec.fisheries.uvms.config.service.ParameterService;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.dto.FishingGearTypeCharacteristics;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RuleAssetsBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulePostProcessBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.SalesMessageFactory;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import javax.ejb.*;
import javax.xml.bind.JAXBException;
import java.util.*;

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.*;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.*;

@Slf4j
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@DependsOn({"RulesConfigurationCache"})
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SalesRulesMessageServiceBean {

    private static final List<String> RULES_TO_USE_ON_VALUE = Arrays.asList("SALE-L01-00-0011", "SALE-L01-00-0400", "SALE-L01-00-0010");
    private static final String FLUX_LOCAL_NATION_CODE = "flux_local_nation_code";

    @EJB
    private RulesMessageProducer producer;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private ParameterService parameterService;

    @EJB
    private FishingGearTypeCharacteristics fishingGearTypeCharacteristics;

    @EJB
    private SalesMessageFactory salesMessageFactory;

    @EJB
    private RuleAssetsBean ruleAssetsBean;

    public void receiveSalesQueryRequest(ReceiveSalesQueryRequest receiveSalesQueryRequest) {
        log.info("Received ReceiveSalesQueryRequest message");
        try {
            //get sales query message
            String salesQueryMessageAsString = receiveSalesQueryRequest.getRequest();
            String logGuid = receiveSalesQueryRequest.getLogGuid();

            FLUXSalesQueryMessage salesQueryMessage = JAXBUtils.unMarshallMessage(salesQueryMessageAsString, FLUXSalesQueryMessage.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, receiveSalesQueryRequest.getSender());
            extraValues.put(ORIGINATING_PLUGIN, receiveSalesQueryRequest.getPluginType());
            extraValues.put(CREATION_DATE_OF_MESSAGE, getCreationDate(salesQueryMessage).or(DateTime.now()));

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_QUERY_MSG, salesQueryMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesQueryMessageAsString, logGuid, RawMsgType.SALES_QUERY);

            //send to sales
            if (validationResult.isError()) {
                String requestForSales;
                if (shouldUseFluxOn(validationResult)) {
                    requestForSales = salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesQueryRequest.getOnValue(),
                            validationResult, receiveSalesQueryRequest.getPluginType(), receiveSalesQueryRequest.getSender(), SalesIdType.FLUXTL_ON);
                } else {
                    requestForSales = salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesQueryRequest.getMessageGuid(),
                            validationResult, receiveSalesQueryRequest.getPluginType(), receiveSalesQueryRequest.getSender(), SalesIdType.GUID);
                }
                log.info("Send RespondToInvalidMessageRequest message to Sales");
                sendToSales(requestForSales);
            } else {
                String requestForSales = salesMessageFactory.createSalesQueryRequest(receiveSalesQueryRequest.getRequest(), validationResult, receiveSalesQueryRequest.getPluginType());
                log.info("Send SalesQueryRequest message to Sales");
                sendToSales(requestForSales);
            }
            updateRequestMessageStatusInExchange(logGuid, validationResult);
        } catch (SalesMarshallException | RulesValidationException | MessageException | JAXBException e) {
            throw new RulesServiceException("Couldn't validate sales query", e);
        }
    }

    public void receiveSalesReportRequest(ReceiveSalesReportRequest receiveSalesReportRequest) {
        log.info("Received ReceiveSalesReportRequest message");
        try {
            //get sales report message
            String salesReportMessageAsString = receiveSalesReportRequest.getRequest();
            String logGuid = receiveSalesReportRequest.getLogGuid();
            Report salesReportMessage = JAXBUtils.unMarshallMessage(salesReportMessageAsString, Report.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, receiveSalesReportRequest.getSender());
            extraValues.put(ORIGINATING_PLUGIN, receiveSalesReportRequest.getPluginType());
            extraValues.put(CREATION_DATE_OF_MESSAGE, getCreationDate(salesReportMessage).or(DateTime.now()));

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_REPORT_MSG, salesReportMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesReportMessageAsString, logGuid, RawMsgType.SALES_REPORT);

            //send to sales
            if (validationResult.isError()) {
                String requestForSales = createInvalidSalesResponseMessage(receiveSalesReportRequest, validationResult);
                log.info("Send RespondToInvalidMessageRequest message to Sales");
                sendToSales(requestForSales);
            } else {
                String requestForSales = salesMessageFactory.createSalesReportRequest(receiveSalesReportRequest.getRequest(), validationResult, receiveSalesReportRequest.getPluginType());
                log.info("Send SalesReportRequest message to Sales");
                sendToSales(requestForSales);
            }
            //update log status
            updateRequestMessageStatusInExchange(logGuid, validationResult);
        } catch (SalesMarshallException | RulesValidationException | MessageException | JAXBException e) {
            throw new RulesServiceException("Couldn't validate sales report", e);
        }
    }

    public void receiveSalesResponseRequest(ReceiveSalesResponseRequest rulesRequest) {
        log.info("Received ReceiveSalesResponseRequest message");
        try {
            //get sales response message
            String salesResponseMessageAsString = rulesRequest.getRequest();
            String logGuid = rulesRequest.getLogGuid();
            FLUXSalesResponseMessage salesResponseMessage = JAXBUtils.unMarshallMessage(salesResponseMessageAsString, FLUXSalesResponseMessage.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, parameterService.getStringValue(FLUX_LOCAL_NATION_CODE));
            extraValues.put(CREATION_DATE_OF_MESSAGE, getCreationDate(salesResponseMessage).or(DateTime.now()));

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_RESPONSE_MSG, salesResponseMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesResponseMessageAsString, logGuid, RawMsgType.SALES_RESPONSE);

            updateRequestMessageStatusInExchange(logGuid, validationResult);
        } catch (RulesValidationException | JAXBException e) {
            throw new RulesServiceException("Couldn't validate sales response", e);
        } catch (ConfigServiceException e) {
            throw new RulesServiceException("Couldn't retrieve the FLUX local nation code from the settings", e);
        }
    }

    public void sendSalesResponseRequest(SendSalesResponseRequest rulesRequest) {
        try {
            //get sales response message
            String salesResponseMessageAsString = rulesRequest.getRequest();
            String logGuid = rulesRequest.getLogGuid();
            FLUXSalesResponseMessage salesResponseMessage = JAXBUtils.unMarshallMessage(salesResponseMessageAsString, FLUXSalesResponseMessage.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, parameterService.getStringValue(FLUX_LOCAL_NATION_CODE));
            extraValues.put(ORIGINATING_PLUGIN, rulesRequest.getPluginToSendResponseThrough());
            extraValues.put(CREATION_DATE_OF_MESSAGE, getCreationDate(salesResponseMessage).or(DateTime.now()));

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_RESPONSE_MSG, salesResponseMessage, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesResponseMessageAsString, logGuid, RawMsgType.SALES_RESPONSE);
            ExchangeLogStatusTypeType validationStatus = calculateMessageValidationStatus(validationResult);

            //send to exchange
            String requestForExchange = ExchangeModuleRequestMapper.createSendSalesResponseRequest(rulesRequest.getRequest(),
                    rulesRequest.getMessageGuid(),
                    rulesRequest.getFluxDataFlow(),
                    rulesRequest.getRecipient(),
                    rulesRequest.getDateSent(),
                    validationStatus,
                    rulesRequest.getPluginToSendResponseThrough());

            log.info("Send SendSalesResponseRequest message to Exchange");
            sendToExchange(requestForExchange);
        } catch (ExchangeModelMarshallException | MessageException | RulesValidationException | ConfigServiceException | JAXBException e) {
            throw new RulesServiceException("Couldn't validate sales response", e);
        }
    }

    public void sendSalesReportRequest(SendSalesReportRequest rulesRequest) {
        try {
            //get sales report message
            String salesReportMessageAsString = rulesRequest.getRequest();
            String logGuid = rulesRequest.getLogGuid();
            Report report = JAXBUtils.unMarshallMessage(salesReportMessageAsString, Report.class);

            //create map with extra values
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, parameterService.getStringValue(FLUX_LOCAL_NATION_CODE));
            extraValues.put(ORIGINATING_PLUGIN, rulesRequest.getPluginToSendResponseThrough());
            extraValues.put(CREATION_DATE_OF_MESSAGE, getCreationDate(report).or(DateTime.now()));

            //validate
            List<AbstractFact> facts = rulesEngine.evaluate(FLUX_SALES_REPORT_MSG, report, extraValues);
            ValidationResultDto validationResult = rulePostProcessBean.checkAndUpdateValidationResult(facts, salesReportMessageAsString, logGuid, RawMsgType.SALES_REPORT);
            ExchangeLogStatusTypeType validationStatus = calculateMessageValidationStatus(validationResult);

            //send to exchange
            String requestForExchange = ExchangeModuleRequestMapper.createSendSalesReportRequest(rulesRequest.getRequest(),
                    rulesRequest.getMessageGuid(),
                    rulesRequest.getFluxDataFlow(),
                    rulesRequest.getRecipient(),
                    rulesRequest.getDateSent(),
                    validationStatus,
                    rulesRequest.getPluginToSendResponseThrough());

            log.info("Send SendSalesReportRequest message to Exchange");
            sendToExchange(requestForExchange);
        } catch (ExchangeModelMarshallException | MessageException | RulesValidationException | ConfigServiceException | JAXBException e) {
            throw new RulesServiceException("Couldn't validate sales report", e);
        }
    }

    private boolean isCorrectUUID(List<IDType> ids) {
        boolean uuidIsCorrect = false;
        String uuidString = null;
        try {
            if (CollectionUtils.isNotEmpty(ids)) {
                IDType idType = ids.get(0);
                uuidString = idType.getValue();
                String schemeID = idType.getSchemeID();
                if ("UUID".equals(schemeID)) {
                    uuidIsCorrect = StringUtils.equalsIgnoreCase(UUID.fromString(uuidString).toString(), uuidString);
                }
                if (!uuidIsCorrect) {
                    log.debug("[WARN] The given UUID is not in a correct format {}", uuidString);
                }
            }
        } catch (IllegalArgumentException exception) {
            log.debug("[WARN] The given UUID is not in a correct format {}", uuidString);
        }
        return uuidIsCorrect;
    }

    private ValidationResultDto generateValidationResultDtoForFailure() {
        ValidationResultDto resultDto = new ValidationResultDto();
        resultDto.setOk(false);
        resultDto.setError(true);
        return resultDto;
    }

    private void updateRequestMessageStatusInExchange(String logGuid, ValidationResultDto validationResult) {
        updateRequestMessageStatusInExchange(logGuid, validationResult, false);
    }

    private void updateRequestMessageStatusInExchange(String logGuid, ValidationResultDto validationResult, Boolean duplicate) {
        updateRequestMessageStatusInExchange(logGuid, calculateMessageValidationStatus(validationResult), duplicate);
    }

    private void updateRequestMessageStatusInExchange(String logGuid, ExchangeLogStatusTypeType statusType) {
        updateRequestMessageStatusInExchange(logGuid, statusType, false);
    }

    private void updateRequestMessageStatusInExchange(String logGuid, ExchangeLogStatusTypeType statusType, Boolean duplicate) {
        try {
            String statusMsg = ExchangeModuleRequestMapper.createUpdateLogStatusRequest(logGuid, statusType, duplicate);
            log.debug("Message to exchange to update status : {}", statusMsg);
            producer.sendDataSourceMessage(statusMsg, DataSourceQueue.EXCHANGE);
        } catch (ExchangeModelMarshallException | MessageException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    private ExchangeLogStatusTypeType calculateMessageValidationStatus(ValidationResultDto validationResult) {
        if (validationResult != null) {
            if (validationResult.isError()) {
                return ExchangeLogStatusTypeType.FAILED;
            } else if (validationResult.isWarning()) {
                return ExchangeLogStatusTypeType.SUCCESSFUL_WITH_WARNINGS;
            } else {
                return ExchangeLogStatusTypeType.SUCCESSFUL;
            }
        } else {
            return ExchangeLogStatusTypeType.UNKNOWN;
        }
    }

    private void sendToSales(String message) throws MessageException {
        producer.sendDataSourceMessage(message, DataSourceQueue.SALES);
    }

    private void sendToExchange(String message) throws MessageException {
        producer.sendDataSourceMessage(message, DataSourceQueue.EXCHANGE);
    }

    private String createInvalidSalesResponseMessage(ReceiveSalesReportRequest receiveSalesReportRequest, ValidationResultDto validationResult) throws SalesMarshallException {
        if (shouldUseFluxOn(validationResult)) {
            return salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesReportRequest.getOnValue(), validationResult,
                    receiveSalesReportRequest.getPluginType(), receiveSalesReportRequest.getSender(), SalesIdType.FLUXTL_ON);
        } else {
            return salesMessageFactory.createRespondToInvalidMessageRequest(receiveSalesReportRequest.getMessageGuid(), validationResult,
                    receiveSalesReportRequest.getPluginType(), receiveSalesReportRequest.getSender(), SalesIdType.GUID);
        }
    }

    public boolean shouldUseFluxOn(ValidationResultDto validationResult) {
        for (ValidationMessageType validationMessage : validationResult.getValidationMessages()) {
            if (RULES_TO_USE_ON_VALUE.contains(validationMessage.getBrId())) {
                return true;
            }
        }
        return false;
    }

    private Optional<DateTime> getCreationDate(FLUXSalesQueryMessage salesQueryMessage) {
        if (salesQueryMessage != null && salesQueryMessage.getSalesQuery() != null
                && salesQueryMessage.getSalesQuery().getSubmittedDateTime() != null
                && salesQueryMessage.getSalesQuery().getSubmittedDateTime().getDateTime() != null) {
            return Optional.of(salesQueryMessage.getSalesQuery().getSubmittedDateTime().getDateTime());
        } else {
            return Optional.absent();
        }
    }

    private Optional<DateTime> getCreationDate(Report salesReportMessage) {
        if (salesReportMessage != null) {
            return getCreationDate(salesReportMessage.getFLUXSalesReportMessage());
        } else {
            return Optional.absent();
        }
    }

    private Optional<DateTime> getCreationDate(FLUXSalesReportMessage salesReportMessage) {
        if (salesReportMessage != null
                && salesReportMessage.getFLUXReportDocument() != null
                && salesReportMessage.getFLUXReportDocument().getCreationDateTime() != null
                && salesReportMessage.getFLUXReportDocument().getCreationDateTime().getDateTime() != null) {
            return Optional.of(salesReportMessage.getFLUXReportDocument().getCreationDateTime().getDateTime());
        } else {
            return Optional.absent();
        }
    }

    private Optional<DateTime> getCreationDate(FLUXSalesResponseMessage salesResponseMessage) {
        if (salesResponseMessage != null && salesResponseMessage.getFLUXResponseDocument() != null
                && salesResponseMessage.getFLUXResponseDocument().getCreationDateTime() != null
                && salesResponseMessage.getFLUXResponseDocument().getCreationDateTime().getDateTime() != null) {
            return Optional.of(salesResponseMessage.getFLUXResponseDocument().getCreationDateTime().getDateTime());
        } else {
            return Optional.absent();
        }
    }

}
