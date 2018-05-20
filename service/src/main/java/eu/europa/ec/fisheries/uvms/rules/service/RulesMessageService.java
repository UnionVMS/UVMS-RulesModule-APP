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

package eu.europa.ec.fisheries.uvms.rules.service;

import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;

import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesQueryRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.ReceiveSalesResponseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SendSalesReportRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SendSalesResponseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFaQueryMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFluxFaResponseMessageRequest;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;

/**
 * Created by padhyad on 5/9/2017.
 */
@Local
public interface RulesMessageService {

    @Lock(LockType.WRITE)
    void evaluateIncomingFLUXFAReport(SetFLUXFAReportMessageRequest request) throws RulesModelMarshallException;

    void evaluateOutgoingFAQuery(SetFaQueryMessageRequest request);

    void evaluateSetFluxFaResponseRequest(SetFluxFaResponseMessageRequest request);

    FLUXResponseMessage generateFluxResponseMessageForFaReport(ValidationResultDto faReportValidationResult, FLUXFAReportMessage fluxfaReportMessage);

    FLUXResponseMessage generateFluxResponseMessage(ValidationResultDto faReportValidationResult);

    FLUXResponseMessage generateFluxResponseMessageForFaQuery(ValidationResultDto faReportValidationResult, FLUXFAQueryMessage fluxfaQueryMessage, String onValue);

    FLUXResponseMessage generateFluxResponseMessageForFaResponse(ValidationResultDto faReportValidationResult, FLUXResponseMessage fluxResponseMessage);

    void validateAndSendResponseToExchange(FLUXResponseMessage fluxResponseMessageType, RulesBaseRequest request, PluginType pluginType, boolean guidProvided);

    void mapAndSendFLUXMdrRequestToExchange(String request);

    void mapAndSendFLUXMdrResponseToMdrModule(String request);

    void receiveSalesQueryRequest(ReceiveSalesQueryRequest receiveSalesQueryRequest);

    void receiveSalesReportRequest(ReceiveSalesReportRequest receiveSalesReportRequest);

    void sendSalesReportRequest(SendSalesReportRequest rulesRequest);

    void receiveSalesResponseRequest(ReceiveSalesResponseRequest rulesRequest);

    void sendSalesResponseRequest(SendSalesResponseRequest rulesRequest);

    void evaluateIncomingFAQuery(SetFaQueryMessageRequest request);

    void evaluateOutgoingFaReport(SetFLUXFAReportMessageRequest request);
}
