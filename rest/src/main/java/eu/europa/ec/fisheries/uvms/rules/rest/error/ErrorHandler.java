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
package eu.europa.ec.fisheries.uvms.rules.rest.error;

import eu.europa.ec.fisheries.schema.rules.common.v1.RulesFault;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

import java.nio.file.AccessDeniedException;

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

        if(ex instanceof AccessDeniedException){
            return new ResponseDto<String>(ex.getMessage(), ResponseCode.FORBIDDEN);
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