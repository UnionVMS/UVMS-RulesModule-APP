/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.activity;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFaQueryMessageRequest;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FluxEnvProperties;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.MessageType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.SyncAsyncRequestType;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.constants.Rule9998Or9999ErrorType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

@Slf4j
abstract class BaseFaRulesMessageServiceBean {

    boolean isCorrectUUID(List<IDType> ids) {
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
                    log.debug("The given UUID is not in a correct format {}", uuidString);
                }
            }
        } catch (IllegalArgumentException exception) {
            log.debug("The given UUID is not in a correct format {}", uuidString);
        }
        return uuidIsCorrect;
    }

    ValidationResultDto generateValidationResultDtoForFailure() {
        return new ValidationResultDto(true, false, false, null);
    }

    void updateRequestMessageStatusInExchange(String logGuid, ValidationResultDto validationResult) {
        updateRequestMessageStatusInExchange(logGuid, validationResult, false);
    }

    void updateRequestMessageStatusInExchange(String logGuid, ValidationResultDto validationResult, Boolean duplicate) {
        updateRequestMessageStatusInExchange(logGuid, calculateMessageValidationStatus(validationResult), duplicate);
    }

    void updateRequestMessageStatusInExchange(String logGuid, ExchangeLogStatusTypeType statusType) {
        updateRequestMessageStatusInExchange(logGuid, statusType, false);
    }

    private void updateRequestMessageStatusInExchange(String logGuid, ExchangeLogStatusTypeType statusType, Boolean duplicate) {
        try {
            String statusMsg = ExchangeModuleRequestMapper.createUpdateLogStatusRequest(logGuid, statusType, duplicate);
            log.debug("Message to exchange to update status : {}", statusMsg);
            getRulesProducer().sendDataSourceMessage(statusMsg, DataSourceQueue.EXCHANGE);
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

    SetFLUXFAReportMessageRequest sendSyncQueryRequestToActivity(String activityQueryMsgStr, String username, PluginType pluginType, List<IDType> fluxReportDocumentId) {
        try {
            String activityRequest = ActivityModuleRequestMapper.mapToSetFLUXFAReportOrQueryMessageRequest(activityQueryMsgStr, pluginType.toString(), MessageType.FLUX_FA_QUERY_MESSAGE, SyncAsyncRequestType.SYNC, FluxEnvProperties.builder().build(), fluxReportDocumentId);
            final String corrId = getRulesProducer().sendDataSourceMessage(activityRequest, DataSourceQueue.ACTIVITY);
            final TextMessage message = getActivityConsumer().getMessage(corrId, TextMessage.class);
            return JAXBUtils.unMarshallMessage(message.getText(), SetFLUXFAReportMessageRequest.class);
        } catch (ActivityModelMarshallException | MessageException | JAXBException | JMSException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    boolean sendToExchangeOnEmptyReport(SetFaQueryMessageRequest request, String requestStr, String logGuid, String onValue, FLUXFAQueryMessage faQueryMessage, ValidationResultDto faQueryValidationReport) {
        log.info("[WARN] The report generated from Activity doesn't contain data (Empty report)!");
        updateRequestMessageStatusInExchange(logGuid, ExchangeLogStatusTypeType.SUCCESSFUL_WITH_WARNINGS);
        getResponseValidator().sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(requestStr, request, faQueryMessage, Rule9998Or9999ErrorType.EMPTY_REPORT, onValue, faQueryValidationReport);
        return false;
    }

    void sendToExchange(String message) throws MessageException {
        getRulesProducer().sendDataSourceMessage(message, DataSourceQueue.EXCHANGE);
    }

    List<IDType> collectFaQueryId(FLUXFAQueryMessage faQueryMessage) {
        List<IDType> idTypeList = new ArrayList<>();
        if (faQueryMessage.getFAQuery() != null) {
            idTypeList.add(faQueryMessage.getFAQuery().getID());
        }
        return idTypeList;
    }

    abstract RulesMessageProducer getRulesProducer();

    abstract AbstractConsumer getActivityConsumer();

    abstract FaResponseRulesMessageServiceBean getResponseValidator();
}
