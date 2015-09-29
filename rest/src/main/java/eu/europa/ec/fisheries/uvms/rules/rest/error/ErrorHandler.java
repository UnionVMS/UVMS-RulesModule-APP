package eu.europa.ec.fisheries.uvms.rules.rest.error;

import eu.europa.ec.fisheries.schema.rules.common.v1.RulesFault;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

//import eu.europa.ec.fisheries.uvms.rules.service.exception.MobileTerminalServiceMapperException;

public class ErrorHandler {

    public static ResponseDto getFault(Exception ex) {
        if (ex instanceof RulesServiceException) {
            if (ex instanceof eu.europa.ec.fisheries.uvms.rules.service.exception.InputArgumentException) {
                return new ResponseDto<String>(ex.getMessage(), ResponseCode.INPUT_ERROR);
            }

            return new ResponseDto<String>(ex.getMessage(), ResponseCode.SERVICE_ERROR);
        }

        if (ex instanceof RulesModelException) {
            if (ex instanceof eu.europa.ec.fisheries.uvms.rules.model.exception.InputArgumentException) {
                return new ResponseDto<String>(ex.getMessage(), ResponseCode.MAPPING_ERROR);
            }

            if (ex instanceof RulesModelMarshallException) {
                return new ResponseDto<String>(ex.getMessage(), ResponseCode.MAPPING_ERROR);
            }

            if (ex instanceof RulesFaultException) {
                return extractFault((RulesFaultException) ex);
            }

            return new ResponseDto<String>(ex.getMessage(), ResponseCode.MODEL_ERROR);
        }

        if (ex instanceof RulesModelMapperException) {
            if (ex instanceof RulesModelMarshallException) {
                return new ResponseDto<String>(ex.getMessage(), ResponseCode.MAPPING_ERROR);
            }
        }

        return new ResponseDto<String>(ex.getMessage(), ResponseCode.UNDEFINED_ERROR);
    }

    private static ResponseDto<String> extractFault(RulesFaultException ex) {
        RulesFault fault = ex.getRulesFault();

        if (fault != null) {
            return new ResponseDto<String>(fault.getMessage(), fault.getCode());
        }
        return new ResponseDto<String>(ex.getMessage(),
                ResponseCode.DOMAIN_ERROR);
    }

}
