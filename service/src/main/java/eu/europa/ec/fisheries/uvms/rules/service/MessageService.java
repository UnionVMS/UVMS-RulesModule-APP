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

import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.uvms.rules.model.dto.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;

/**
 * Created by padhyad on 5/9/2017.
 */
@Local
public interface MessageService {

    void setFLUXFAReportMessageReceived(SetFLUXFAReportMessageRequest request) throws RulesServiceException, RulesModelMarshallException;

    FLUXResponseMessage generateFluxResponseMessage(ValidationResultDto faReportValidationResult, FLUXFAReportMessage fluxfaReportMessage);

    void sendResponseToExchange(FLUXResponseMessage fluxResponseMessageType, String username) throws RulesServiceException;

    void mapAndSendFLUXMdrRequestToExchange(String request);

    void mapAndSendFLUXMdrResponseToMdrModule(String request);
}
