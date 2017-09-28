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

    public static String createGetTicketsAndRulesByMovementsRequest(List<String> movementsGuids) throws RulesModelMarshallException {
        GetTicketsAndRulesByMovementsRequest request = new GetTicketsAndRulesByMovementsRequest();
        request.setMethod(RulesModuleMethod.GET_TICKETS_AND_RULES_BY_MOVEMENTS);
        request.getMovementGuids().addAll(movementsGuids);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
        
    }

    public static String createSetFLUXFAReportMessageRequest(PluginType type, String fluxFAReportMessage, String username, String logId, String fluxDataFlow, String senderOrReceiver) throws RulesModelMapperException {
        SetFLUXFAReportMessageRequest request = new SetFLUXFAReportMessageRequest();
        request.setMethod(RulesModuleMethod.SET_FLUX_FA_REPORT);
        request.setFluxDataFlow(fluxDataFlow);
        request.setSenderOrReceiver(senderOrReceiver);
        request.setType(type);
        request.setUsername(username);
        request.setRequest(fluxFAReportMessage);
        request.setLogGuid(logId);
        return JAXBMarshaller.marshallJaxBObjectToString(request);
    }

    public static String createReceiveSalesReportRequest(String salesReport, String messageGuid, String pluginType, String logGuid, String sender) throws RulesModelMarshallException {
        ReceiveSalesReportRequest receiveSalesReportRequest = new ReceiveSalesReportRequest();
        receiveSalesReportRequest.setMethod(RulesModuleMethod.RECEIVE_SALES_REPORT);
        receiveSalesReportRequest.setRequest(salesReport);
        receiveSalesReportRequest.setPluginType(pluginType);
        receiveSalesReportRequest.setLogGuid(logGuid);
        receiveSalesReportRequest.setSender(sender);
        receiveSalesReportRequest.setMessageGuid(messageGuid);
        return JAXBMarshaller.marshallJaxBObjectToString(receiveSalesReportRequest);
    }

    public static String createReceiveSalesQueryRequest(String salesQuery, String messageGuid, String pluginType, String logGuid, String sender) throws RulesModelMarshallException {
        ReceiveSalesQueryRequest receiveSalesQueryRequest = new ReceiveSalesQueryRequest();
        receiveSalesQueryRequest.setMethod(RulesModuleMethod.RECEIVE_SALES_QUERY);
        receiveSalesQueryRequest.setRequest(salesQuery);
        receiveSalesQueryRequest.setPluginType(pluginType);
        receiveSalesQueryRequest.setLogGuid(logGuid);
        receiveSalesQueryRequest.setSender(sender);
        receiveSalesQueryRequest.setMessageGuid(messageGuid);
        return JAXBMarshaller.marshallJaxBObjectToString(receiveSalesQueryRequest);
    }

    public static String createReceiveSalesResponseRequest(String salesResponse, String logGuid) throws RulesModelMarshallException {
        ReceiveSalesResponseRequest receiveSalesResponseRequest = new ReceiveSalesResponseRequest();
        receiveSalesResponseRequest.setMethod(RulesModuleMethod.RECEIVE_SALES_RESPONSE);
        receiveSalesResponseRequest.setRequest(salesResponse);
        receiveSalesResponseRequest.setLogGuid(logGuid);
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


}