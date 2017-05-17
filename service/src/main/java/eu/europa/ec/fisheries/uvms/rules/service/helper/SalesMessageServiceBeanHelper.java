package eu.europa.ec.fisheries.uvms.rules.service.helper;

import eu.europa.ec.fisheries.schema.exchange.module.v1.ExchangeModuleMethod;
import eu.europa.ec.fisheries.schema.rules.module.v1.*;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;

import javax.ejb.Singleton;
import java.util.Date;

@Singleton
public class SalesMessageServiceBeanHelper {

    public void handleReceiveSalesQueryRequest(ReceiveSalesQueryRequest request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException {

//        List<AbstractFact> salesReportFacts = rulesEngine.evaluate(BusinessObjectType.FLUX_ACTIVITY_REQUEST_MSG, fluxSalesQueryMessage);
//        ValidationResultDto faReportValidationResult = rulePostprocessBean.checkAndUpdateValidationResult(faReportFacts, fluxFAReportMessage);
    }

    public void handleReceiveSalesReportRequest(ReceiveSalesReportRequest request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException {

    }

    public void handleReceiveSalesResponseRequest(ReceiveSalesResponseRequest request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException, ExchangeModelMarshallException {
        //TODO: validate response with rules
    }

    public String handleSendSalesResponseRequest(String request, RulesEngineBean rulesEngine) throws RulesValidationException, ExchangeModelMarshallException, SalesMarshallException {
        SendSalesResponseRequest rulesRequest = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(request, SendSalesResponseRequest.class);

        //TODO: validate response with rules

        return ExchangeModuleRequestMapper.createSendSalesResponseRequest(rulesRequest.getRequest(),
                ExchangeModuleMethod.SEND_SALES_RESPONSE,
                "guid",
                "df",
                "senderOrReceiver",
                new Date()); //TODO: actual values from Sales module
    }

    public String handleSendSalesReportRequest(String request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException, ExchangeModelMarshallException {
        SendSalesReportRequest rulesReport = eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller.unmarshallString(request, SendSalesReportRequest.class);

        //TODO: validate response with rules

        return ExchangeModuleRequestMapper.createSendSalesReportRequest(request,
                ExchangeModuleMethod.SEND_SALES_REPORT,
                "guid",
                "dataFlow",
                "senderOrReceiver",
                new Date()); //TODO: actual values from Sales module
    }


}