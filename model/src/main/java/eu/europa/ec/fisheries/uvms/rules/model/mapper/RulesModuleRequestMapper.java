/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.*;
import eu.europa.ec.fisheries.schema.rules.movement.v1.RawMovementType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;

import java.util.Date;
import java.util.List;

public class RulesModuleRequestMapper {

    public static String createSetMovementReportRequest(PluginType type, RawMovementType rawMovementType, String username) throws RulesModelMapperException {
        SetMovementReportRequest request = new SetMovementReportRequest();
        request.setMethod(RulesModuleMethod.SET_MOVEMENT_REPORT);
        request.setType(type);
        request.setUsername(username);
        request.setRequest(rawMovementType);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String createSetFLUXMovementReportRequest(PluginType type, String fluxFAReportMessage, String username,
                                                             String logId, String fluxDataFlow, String senderOrReceiver, String onValue,
                                                            String registeredClassName, String ad, String to, String todt) throws RulesModelMapperException {
        SetFLUXMovementReportRequest request = new SetFLUXMovementReportRequest();
        request.setMethod(RulesModuleMethod.RECEIVE_MOVEMENT_BATCH);
        request.setRequest(fluxFAReportMessage);
        request.setType(type);
        populateCommonProperties(request, username, logId, fluxDataFlow, senderOrReceiver, onValue, registeredClassName, ad, to, todt);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String createGetTicketsAndRulesByMovementsRequest(List<String> movementsGuids) throws RulesModelMarshallException {
        GetTicketsAndRulesByMovementsRequest request = new GetTicketsAndRulesByMovementsRequest();
        request.setMethod(RulesModuleMethod.GET_TICKETS_AND_RULES_BY_MOVEMENTS);
        request.getMovementGuids().addAll(movementsGuids);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String createSetFLUXFAReportMessageRequest(PluginType type, String fluxFAReportMessage, String username,
                                                             String logId, String fluxDataFlow, String senderOrReceiver, String onValue) throws RulesModelMapperException {
        SetFLUXFAReportMessageRequest request = new SetFLUXFAReportMessageRequest();
        request.setMethod(RulesModuleMethod.SET_FLUX_FA_REPORT);
        request.setRequest(fluxFAReportMessage);
        request.setPluginType(type);
        populateCommonProperties(request, username, logId, fluxDataFlow, senderOrReceiver, onValue);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String createSendFLUXFAReportMessageRequest(String fluxFAReportMessage, String username, String logId, String fluxDataFlow, String senderOrReceiver, String onValue, boolean isEmpty) throws RulesModelMapperException {
        SetFLUXFAReportMessageRequest request = new SetFLUXFAReportMessageRequest();
        request.setMethod(RulesModuleMethod.SEND_FLUX_FA_REPORT);
        request.setRequest(fluxFAReportMessage);
        request.setIsEmptyReport(isEmpty);
        request.setAd(senderOrReceiver);
        populateCommonProperties(request, username, logId, fluxDataFlow, senderOrReceiver, onValue);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String createSetFaQueryMessageRequest(PluginType type, String faQueryMessageStr, String username,
                                                             String logId, String fluxDataFlow, String senderOrReceiver, String onValue) throws RulesModelMapperException {
        SetFaQueryMessageRequest request = new SetFaQueryMessageRequest();
        request.setMethod(RulesModuleMethod.SET_FLUX_FA_QUERY);
        request.setRequest(faQueryMessageStr);
        request.setPluginType(type);
        populateCommonProperties(request, username, logId, fluxDataFlow, senderOrReceiver, onValue);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String createRcvFluxFaResponseMessageRequest(PluginType type, String faResponseStr, String username,
                                                        String logId, String fluxDataFlow, String senderOrReceiver, String onValue) throws RulesModelMapperException {
        SetFluxFaResponseMessageRequest request = new SetFluxFaResponseMessageRequest();
        request.setMethod(RulesModuleMethod.RCV_FLUX_RESPONSE);
        request.setRequest(faResponseStr);
        request.setType(type);
        populateCommonProperties(request, username, logId, fluxDataFlow, senderOrReceiver, onValue);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String createSendFaQueryMessageRequest(String faQueryMessageStr, String username, String logId, String fluxDataFlow,
                                                         String senderOrReceiver, String ad) throws RulesModelMapperException {
        SetFaQueryMessageRequest request = new SetFaQueryMessageRequest();
        request.setMethod(RulesModuleMethod.SEND_FLUX_FA_QUERY);
        request.setRequest(faQueryMessageStr);
        request.setAd(ad);
        populateCommonProperties(request, username, logId, fluxDataFlow, senderOrReceiver, null);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String createSetFLUXFAQueryMessageRequest(String faQueryMessageStr, String username, String logId,
                                                            String dataFlow, String localNodeName, String onValue,
                                                            Boolean isEmptyReport, Boolean isPermitted) throws RulesModelMapperException {
        SetFLUXFAReportMessageRequest request = new SetFLUXFAReportMessageRequest();
        request.setMethod(RulesModuleMethod.SEND_FLUX_FA_QUERY);
        request.setRequest(faQueryMessageStr);
        request.setIsPermitted(isPermitted);
        request.setIsEmptyReport(isEmptyReport);
        populateCommonProperties(request, username, logId, dataFlow, localNodeName, onValue);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String createSendFluxMovementReportMessageRequest(PluginType type, String forwardPositionMessageStr, String username, String logId, String fluxDataFlow,
                                                         String senderOrReceiver,String ad) throws RulesModelMapperException {
        SendFLUXMovementReportRequest request = new SendFLUXMovementReportRequest();
        request.setMethod(RulesModuleMethod.SEND_FLUX_MOVEMENT_REPORT);
        request.setRequest(forwardPositionMessageStr);
        request.setType(type);
        request.setAd(ad);
        populateCommonProperties(request, username, logId, fluxDataFlow, senderOrReceiver, null);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    @Deprecated // Use the populateCommonProperties() with 10 parameters!
    private static void populateCommonProperties(RulesBaseRequest request, String username, String logId, String fluxDataFlow, String senderOrReceiver, String onValue) {
        request.setFluxDataFlow(fluxDataFlow);
        request.setSenderOrReceiver(senderOrReceiver);
        request.setUsername(username);
        request.setLogGuid(logId);
        request.setOnValue(onValue);
    }

    private static void populateCommonProperties(RulesBaseRequest request, String username, String logId,
                                                 String fluxDataFlow, String senderOrReceiver, String onValue, String registeredClassName,
                                                 String ad, String to, String todt) {
        request.setFluxDataFlow(fluxDataFlow);
        request.setSenderOrReceiver(senderOrReceiver);
        request.setUsername(username);
        request.setLogGuid(logId);
        request.setOnValue(onValue);
        request.setRegisteredClassName(registeredClassName);
        request.setAd(ad);
        request.setTo(to);
        request.setTodt(todt);
    }

    public static String createReceiveSalesReportRequest(String salesReport, String messageGuid, String pluginType, String logGuid, String sender, String on) throws RulesModelMarshallException {
        ReceiveSalesReportRequest receiveSalesReportRequest = new ReceiveSalesReportRequest();
        receiveSalesReportRequest.setMethod(RulesModuleMethod.RECEIVE_SALES_REPORT);
        receiveSalesReportRequest.setRequest(salesReport);
        receiveSalesReportRequest.setPluginType(pluginType);
        receiveSalesReportRequest.setLogGuid(logGuid);
        receiveSalesReportRequest.setSender(sender);
        receiveSalesReportRequest.setMessageGuid(messageGuid);
        receiveSalesReportRequest.setOnValue(on);
        return JAXBMarshaller.marshallJaxBObjectToString(receiveSalesReportRequest);
    }

    public static String createReceiveSalesQueryRequest(String salesQuery, String messageGuid, String pluginType, String logGuid, String sender, String on) throws RulesModelMarshallException {
        ReceiveSalesQueryRequest receiveSalesQueryRequest = new ReceiveSalesQueryRequest();
        receiveSalesQueryRequest.setMethod(RulesModuleMethod.RECEIVE_SALES_QUERY);
        receiveSalesQueryRequest.setRequest(salesQuery);
        receiveSalesQueryRequest.setPluginType(pluginType);
        receiveSalesQueryRequest.setLogGuid(logGuid);
        receiveSalesQueryRequest.setSender(sender);
        receiveSalesQueryRequest.setMessageGuid(messageGuid);
        receiveSalesQueryRequest.setOnValue(on);
        return JAXBMarshaller.marshallJaxBObjectToString(receiveSalesQueryRequest);
    }

    public static String createReceiveSalesResponseRequest(String salesResponse, String logGuid, String senderOrReceiver) throws RulesModelMarshallException {
        ReceiveSalesResponseRequest receiveSalesResponseRequest = new ReceiveSalesResponseRequest();
        receiveSalesResponseRequest.setMethod(RulesModuleMethod.RECEIVE_SALES_RESPONSE);
        receiveSalesResponseRequest.setRequest(salesResponse);
        receiveSalesResponseRequest.setLogGuid(logGuid);
        receiveSalesResponseRequest.setSenderOrReceiver(senderOrReceiver);
        return JAXBMarshaller.marshallJaxBObjectToString(receiveSalesResponseRequest);
    }


    public static String createSendSalesResponseRequest(String salesResponse, String salesResponseGuid, String recipient, String pluginToSendResponseThrough, String fluxDataFlow, Date dateSent) throws RulesModelMarshallException {
        SendSalesResponseRequest sendSalesResponseRequest = new SendSalesResponseRequest();
        sendSalesResponseRequest.setMethod(RulesModuleMethod.SEND_SALES_RESPONSE);
        sendSalesResponseRequest.setRequest(salesResponse);
        sendSalesResponseRequest.setRecipient(recipient);
        sendSalesResponseRequest.setPluginToSendResponseThrough(pluginToSendResponseThrough);
        sendSalesResponseRequest.setFluxDataFlow(fluxDataFlow);
        sendSalesResponseRequest.setDateSent(dateSent);
        sendSalesResponseRequest.setMessageGuid(salesResponseGuid);
        return JAXBMarshaller.marshallJaxBObjectToString(sendSalesResponseRequest);
    }

    public static String createSendSalesReportRequest(String salesReport, String salesReportGuid, String recipient, String pluginToSendResponseThrough, String fluxDataFlow, Date dateSent) throws RulesModelMarshallException {
        SendSalesReportRequest sendSalesReportRequest = new SendSalesReportRequest();
        sendSalesReportRequest.setMethod(RulesModuleMethod.SEND_SALES_REPORT);
        sendSalesReportRequest.setRequest(salesReport);
        sendSalesReportRequest.setRecipient(recipient);
        sendSalesReportRequest.setPluginToSendResponseThrough(pluginToSendResponseThrough);
        sendSalesReportRequest.setFluxDataFlow(fluxDataFlow);
        sendSalesReportRequest.setDateSent(dateSent);
        sendSalesReportRequest.setMessageGuid(salesReportGuid);
        return JAXBMarshaller.marshallJaxBObjectToString(sendSalesReportRequest);
    }


    public static String createGetValidationsByGuidRequest(String guid, String type, String dataFlow) throws RulesModelMarshallException {
        GetValidationsByRawMsgGuidRequest getValidationsByGuidRequest = new GetValidationsByRawMsgGuidRequest();
        getValidationsByGuidRequest.setMethod(RulesModuleMethod.GET_VALIDATION_RESULT_BY_RAW_GUID_REQUEST);
        getValidationsByGuidRequest.setGuid(guid);
        getValidationsByGuidRequest.setType(type);
        getValidationsByGuidRequest.setDf(dataFlow);
        return JAXBMarshaller.marshallJaxBObjectToString(getValidationsByGuidRequest);
    }
}