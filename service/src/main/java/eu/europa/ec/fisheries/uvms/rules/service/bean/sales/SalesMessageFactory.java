package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.schema.sales.ValidationQualityAnalysisType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.SalesModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.ValidationQualityAnalysisMapper;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Stateless
public class SalesMessageFactory {

    public String createSalesQueryRequest(String request, ValidationResultDto validationResult, String pluginType) throws SalesMarshallException {
        List<ValidationQualityAnalysisType> validationQualityAnalysis = mapToValidationQualityAnalysis(validationResult);
        String messageStatus = getMessageStatus(validationResult);
        return SalesModuleRequestMapper.createSalesQueryRequest(request, messageStatus, validationQualityAnalysis, pluginType);
    }


    public String createRespondToInvalidMessageRequest(String messageGuid, ValidationResultDto validationResult, String pluginType, String sender) throws SalesMarshallException {
        List<ValidationQualityAnalysisType> validationQualityAnalysis = mapToValidationQualityAnalysis(validationResult);
        return SalesModuleRequestMapper.createRespondToInvalidMessageRequest(messageGuid, validationQualityAnalysis, pluginType, sender);
    }

    public String createSalesReportRequest(String request, ValidationResultDto validationResult, String pluginType) throws SalesMarshallException {
        List<ValidationQualityAnalysisType> validationQualityAnalysis = mapToValidationQualityAnalysis(validationResult);
        String messageStatus = getMessageStatus(validationResult);
        return SalesModuleRequestMapper.createSalesReportRequest(request, messageStatus, validationQualityAnalysis, pluginType);
    }


    protected List<ValidationQualityAnalysisType> mapToValidationQualityAnalysis(ValidationResultDto validationResult) {
        List<ValidationQualityAnalysisType> validationQualityAnalysisTypes = new ArrayList<>();
        if (isNotEmpty(validationResult.getValidationMessages())) {
            for (ValidationMessageType validationMessageType : validationResult.getValidationMessages()) {
                String errorType = getErrorType(validationMessageType.getErrorType());

                validationQualityAnalysisTypes.add(ValidationQualityAnalysisMapper.map(validationMessageType.getBrId(),
                        validationMessageType.getLevel(), errorType, validationMessageType.getMessage(),
                        validationMessageType.getXpaths()));
            }
        }
        return validationQualityAnalysisTypes;
    }

    protected String getErrorType(ErrorType errorType) {
        switch (errorType) {
            case ERROR: return "ERR";
            case WARNING: return "WAR";
            default: throw new UnsupportedOperationException("No mapping provided for a validation message with error type " + errorType);
        }
    }

    protected String getMessageStatus(ValidationResultDto validationResultDto) {
        if (validationResultDto.isError()) {
            return "NOK";
        } else if (validationResultDto.isWarning()) {
            return "WOK";
        } else {
            return "OK";
        }
    }

}
