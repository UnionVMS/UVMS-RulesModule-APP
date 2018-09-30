package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.schema.sales.SalesIdType;
import eu.europa.ec.fisheries.schema.sales.ValidationQualityAnalysisType;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResult;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.SalesModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.ValidationQualityAnalysisMapper;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@Stateless
public class SalesMessageFactory {

    private static final String FLUX_GP_VALIDATION_TYPE_ERR = "ERR";
    private static final String FLUX_GP_VALIDATION_TYPE_WAR = "WAR";
    private static final String FLUX_GP_RESPONSE_NOK = "NOK";
    private static final String FLUX_GP_RESPONSE_WOK = "WOK";
    private static final String FLUX_GP_RESPONSE_OK = "OK";

    public String createSalesQueryRequest(String request, ValidationResult validationResult, String pluginType) throws SalesMarshallException {
        List<ValidationQualityAnalysisType> validationQualityAnalysis = mapToValidationQualityAnalysis(validationResult);
        String messageStatus = getMessageStatus(validationResult);
        return SalesModuleRequestMapper.createSalesQueryRequest(request, messageStatus, validationQualityAnalysis, pluginType);
    }


    public String createRespondToInvalidMessageRequest(String messageGuid, ValidationResult validationResult, String pluginType, String sender, SalesIdType salesIdType) throws SalesMarshallException {
        List<ValidationQualityAnalysisType> validationQualityAnalysis = mapToValidationQualityAnalysis(validationResult);
        return SalesModuleRequestMapper.createRespondToInvalidMessageRequest(messageGuid, validationQualityAnalysis, pluginType, sender, salesIdType);
    }

    public String createSalesReportRequest(String request, ValidationResult validationResult, String pluginType) throws SalesMarshallException {
        List<ValidationQualityAnalysisType> validationQualityAnalysis = mapToValidationQualityAnalysis(validationResult);
        String messageStatus = getMessageStatus(validationResult);
        return SalesModuleRequestMapper.createSalesReportRequest(request, messageStatus, validationQualityAnalysis, pluginType);
    }


    protected List<ValidationQualityAnalysisType> mapToValidationQualityAnalysis(ValidationResult validationResult) {
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
            case ERROR: return FLUX_GP_VALIDATION_TYPE_ERR;
            case WARNING: return FLUX_GP_VALIDATION_TYPE_WAR;
            default: throw new UnsupportedOperationException("No mapping provided for a validation message with error type " + errorType);
        }
    }

    protected String getMessageStatus(ValidationResult validationResultDto) {
        if (validationResultDto.isError()) {
            return FLUX_GP_RESPONSE_NOK;
        } else if (validationResultDto.isWarning()) {
            return FLUX_GP_RESPONSE_WOK;
        } else {
            return FLUX_GP_RESPONSE_OK;
        }
    }

}
