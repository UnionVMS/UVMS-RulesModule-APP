package eu.europa.ec.fisheries.uvms.rules.service.bean.messageprocessors.fa;

import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.exchange.v1.PluginType;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFaQueryMessageRequest;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.ActivityModuleRequestMapper;
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

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.UUID;

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
                    log.debug("[WARN] The given UUID is not in a correct format {}", uuidString);
                }
            }
        } catch (IllegalArgumentException exception) {
            log.debug("[WARN] The given UUID is not in a correct format {}", uuidString);
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

    SetFLUXFAReportMessageRequest sendSyncQueryRequestToActivity(String activityQueryMsgStr, String username, PluginType pluginType) {
        try {
            String activityRequest = ActivityModuleRequestMapper.mapToSetFLUXFAReportOrQueryMessageRequest(activityQueryMsgStr, username, pluginType.toString(), MessageType.FLUX_FA_QUERY_MESSAGE, SyncAsyncRequestType.SYNC);
            final String corrId = getRulesProducer().sendDataSourceMessage(activityRequest, DataSourceQueue.ACTIVITY);
            final TextMessage message = getActivityConsumer().getMessage(corrId, TextMessage.class);
            return JAXBUtils.unMarshallMessage(message.getText(), SetFLUXFAReportMessageRequest.class);
        } catch (ActivityModelMarshallException | MessageException | JAXBException | JMSException e) {
            throw new RulesServiceException(e.getMessage(), e);
        }
    }

    boolean isNeedToSendToExchange(SetFaQueryMessageRequest request, String requestStr, String logGuid, String onValue, FLUXFAQueryMessage faQueryMessage) {
        log.info("[WARN] The report generated from Activity doesn't contain data (Empty report)!");
        updateRequestMessageStatusInExchange(logGuid, ExchangeLogStatusTypeType.SUCCESSFUL_WITH_WARNINGS);
        getResponseValidator().sendFLUXResponseMessageOnEmptyResultOrPermissionDenied(requestStr, request, faQueryMessage, Rule9998Or9999ErrorType.EMPTY_REPORT, onValue);
        return false;
    }

    void sendToExchange(String message) throws MessageException {
        getRulesProducer().sendDataSourceMessage(message, DataSourceQueue.EXCHANGE);
    }

    IDType collectFaQueryId(FLUXFAQueryMessage faQueryMessage) {
        IDType faQueryGUID = null;
        if (faQueryMessage.getFAQuery() != null) {
            faQueryGUID = faQueryMessage.getFAQuery().getID();
        }
        return faQueryGUID;
    }

    abstract RulesMessageProducer getRulesProducer();

    abstract AbstractConsumer getActivityConsumer();

    abstract FaResponseRulesMessageServiceBean getResponseValidator();
}
