package eu.europa.ec.fisheries.uvms.rules.service.helper;

import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesQueryRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesResponseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SendSalesResponseRequest;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;

import javax.ejb.Singleton;

@Singleton
public class SalesMessageServiceBeanHelper {

    public void handleReceiveSalesQueryRequest(ReceiveSalesQueryRequest request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException {
        //TODO: validate response with rules
    }

    public void handleReceiveSalesReportRequest(ReceiveSalesReportRequest request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException {
        //TODO: validate response with rules
    }

    public void handleReceiveSalesResponseRequest(ReceiveSalesResponseRequest request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException, ExchangeModelMarshallException {
        //TODO: validate response with rules
    }

    public void handleSendSalesResponseRequest(SendSalesResponseRequest rulesRequest, RulesEngineBean rulesEngine) throws RulesValidationException, ExchangeModelMarshallException, SalesMarshallException {
        //TODO: validate response with rules
    }

    public void handleSendSalesReportRequest(String request, RulesEngineBean rulesEngine) throws SalesMarshallException, RulesValidationException, ExchangeModelMarshallException {
        //TODO: validate response with rules
    }


}