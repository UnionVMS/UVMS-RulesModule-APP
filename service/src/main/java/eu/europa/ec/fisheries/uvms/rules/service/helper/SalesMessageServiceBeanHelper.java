package eu.europa.ec.fisheries.uvms.rules.service.helper;

import eu.europa.ec.fisheries.schema.exchange.module.v1.*;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesQueryMessage;
import eu.europa.ec.fisheries.schema.sales.SalesModuleMethod;
import eu.europa.ec.fisheries.schema.sales.SalesQueryRequest;
import eu.europa.ec.fisheries.schema.sales.SalesReportRequest;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller;

import javax.ejb.Singleton;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
public class SalesMessageServiceBeanHelper {

    public void handleSalesQueryRequest(String request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException {
        FLUXSalesQueryMessage fluxSalesQueryMessage = JAXBMarshaller.unmarshallString(request, FLUXSalesQueryMessage.class);
//        List<AbstractFact> salesReportFacts = rulesEngine.evaluate(BusinessObjectType.FLUX_ACTIVITY_REQUEST_MSG, fluxSalesQueryMessage);
//        ValidationResultDto faReportValidationResult = rulePostprocessBean.checkAndUpdateValidationResult(faReportFacts, fluxFAReportMessage);
    }

    public void handleSalesReportRequest(String request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException {

    }

    public void handleSalesResponseRequest(String request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException {

    }

    public void handleSendSalesResponseRequest(String request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException {

    }

    public void handleSendSalesReportRequest(String request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException {

    }

    public String createSalesReportRequest(String request, SalesModuleMethod method) throws SalesMarshallException {
        SalesReportRequest salesReportRequest = new SalesReportRequest();
        salesReportRequest.setMethod(checkNotNull(method));
        salesReportRequest.setReport(checkNotNull(request));

        return JAXBMarshaller.marshallJaxBObjectToString(salesReportRequest);
    }

    public String createSalesQueryRequest(String query, SalesModuleMethod method) throws SalesMarshallException {
        SalesQueryRequest salesQueryRequest = new SalesQueryRequest();
        salesQueryRequest.setQuery(checkNotNull(query));
        salesQueryRequest.setMethod(checkNotNull(method));

        return JAXBMarshaller.marshallJaxBObjectToString(salesQueryRequest);
    }

    public String createSendSalesResponseRequest(String response, ExchangeModuleMethod method,
                                                 String guid, String dataFlow,
                                                 String senderOrReceiver, Date date) throws SalesMarshallException {
        SendSalesResponseRequest sendSalesResponseRequest = new SendSalesResponseRequest();
        sendSalesResponseRequest.setResponse(checkNotNull(response));

        enrichBaseRequest(sendSalesResponseRequest, method, guid, dataFlow, senderOrReceiver, date);
        return JAXBMarshaller.marshallJaxBObjectToString(sendSalesResponseRequest);
    }

    public String createSendSalesReportRequest(String report, ExchangeModuleMethod method,
                                               String guid, String dataFlow,
                                               String senderOrReceiver, Date date) throws SalesMarshallException {
        SendSalesReportRequest sendSalesReportRequest = new SendSalesReportRequest();
        sendSalesReportRequest.setReport(checkNotNull(report));

        enrichBaseRequest(sendSalesReportRequest, method, guid, dataFlow, senderOrReceiver, date);
        return JAXBMarshaller.marshallJaxBObjectToString(sendSalesReportRequest);
    }

    public String createReceiveResponseRequest(String response, ExchangeModuleMethod method,
                                               String guid, String dataFlow,
                                               String senderOrReceiver, Date date) throws SalesMarshallException {
        ReceiveSalesResponseRequest receiveSalesResponseRequest = new ReceiveSalesResponseRequest();
        receiveSalesResponseRequest.setResponse(response);

        enrichBaseRequest(receiveSalesResponseRequest, method, guid, dataFlow, senderOrReceiver, date);
        return JAXBMarshaller.marshallJaxBObjectToString(receiveSalesResponseRequest);
    }

    private void enrichBaseRequest(ExchangeBaseRequest exchangeBaseRequest, ExchangeModuleMethod method, String guid, String dataFlow, String senderOrReceiver, Date date) {
        exchangeBaseRequest.setMethod(checkNotNull(method));
        exchangeBaseRequest.setDate(checkNotNull(date));
        exchangeBaseRequest.setMessageGuid(checkNotNull(guid));
        exchangeBaseRequest.setFluxDataFlow(checkNotNull(dataFlow));
        exchangeBaseRequest.setSenderOrReceiver(checkNotNull(senderOrReceiver));
    }
}

//        FLUXSalesQueryMessage fluxfaReportMessage = JAXBMarshaller.unMarshallMessage(fluxFAReportMessage, FLUXFAReportMessage.class);
//        if (fluxfaReportMessage != null) {
//            FLUXResponseMessage fluxResponseMessageType;
//            Map<Boolean, ValidationResultDto> validationMap = rulesPreProcessBean.checkDuplicateIdInRequest(fluxfaReportMessage);
//            boolean isContinueValidation = validationMap.entrySet().iterator().next().getKey();
//            log.info("Validation continue : {}", isContinueValidation);
//
//            if (isContinueValidation) {
//                log.info("Trigger rule engine to do validation of incoming message");
//                List<AbstractFact> faReportFacts = rulesEngine.evaluate(BusinessObjectType.FLUX_ACTIVITY_REQUEST_MSG, fluxfaReportMessage);
//                ValidationResultDto faReportValidationResult = rulePostprocessBean.checkAndUpdateValidationResult(faReportFacts, fluxFAReportMessage);
//                updateValidationResultWithExisting(faReportValidationResult, validationMap.get(isContinueValidation));
//
//                // TODO send exchange ack
//
//                if (!faReportValidationResult.isError()) {
//                    log.info("Validation of Report is successful, forwarding message to Activity");
//                    sendRequestToActivity(fluxFAReportMessage, username, pluginType);
//                }
//                fluxResponseMessageType = generateFluxResponseMessage(faReportValidationResult, fluxfaReportMessage.getFLUXReportDocument().getIDS());
//            } else {
//                fluxResponseMessageType = generateFluxResponseMessage(validationMap.get(isContinueValidation), fluxfaReportMessage.getFLUXReportDocument().getIDS());
//            }
//            sendResponseToExchange(fluxResponseMessageType, username);
//        }