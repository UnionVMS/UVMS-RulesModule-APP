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
package eu.europa.ec.fisheries.uvms.rules.rest.service;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.*;
import eu.europa.ec.fisheries.schema.rules.search.v1.CustomRuleQuery;
import eu.europa.ec.fisheries.uvms.movement.model.util.DateUtil;
import eu.europa.ec.fisheries.uvms.rest.security.RequiresFeature;
import eu.europa.ec.fisheries.uvms.rest.security.UnionVMSFeature;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.nio.file.AccessDeniedException;

@Path("/customrules")
@Stateless
public class CustomRulesRestResource {

    private final static Logger LOG = LoggerFactory.getLogger(CustomRulesRestResource.class);

    private static String UNION_VMS_APPLICATION = "Union-VMS";
    @EJB
    RulesService rulesService;
    @EJB
    ValidationService validationService;
    @Context
    private ServletContext servletContext;
    @Context
    private HttpServletRequest request;
    /**
     *
     * @responseMessage 200 A custom rule successfully created
     * @responseMessage 500 No custom rule was created
     *
     * @summary Create a custom rule
     *
     */
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @RequiresFeature(UnionVMSFeature.manageAlarmRules)
    public ResponseDto createCustomRule(final CustomRuleType customRule) {
        LOG.info("Create invoked in rest layer");
        try {
            if(!validate(customRule)){
                return new ResponseDto<String>("Custom rule data is not correct", ResponseCode.INPUT_ERROR);
            }
            return new ResponseDto(rulesService.createCustomRule(customRule, UnionVMSFeature.manageGlobalAlarmsRules.name(), getApplicationName(servletContext)), ResponseCode.OK);
        } catch (RulesServiceException | NullPointerException | RulesFaultException e) {
            LOG.error("[ Error when creating. ] {} ", e.getStackTrace());
            return ErrorHandler.getFault(e);
        } catch (AccessDeniedException e) {
            LOG.error("[ User has no right to create global alarm rules ] {} ", e.getStackTrace());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary Get a list of all custom rules
     *
     */
    @GET
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path(value = "listAll/{userName}")
    @RequiresFeature(UnionVMSFeature.viewAlarmRules)
    public ResponseDto getCustomRulesByUser(@PathParam(value = "userName") final String userName) {
        LOG.info("Get all custom rules invoked in rest layer");
        try {
            return new ResponseDto(validationService.getCustomRulesByUser(userName), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException ex) {
            LOG.error("[ Error when getting all custom rules. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
        }
    }

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary Get a list of custom rules by query
     *
     */
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("listByQuery")
    @RequiresFeature(UnionVMSFeature.viewAlarmRules)
    public ResponseDto getCustomRulesByQuery(CustomRuleQuery query) {
        LOG.info("Get custom rules by query invoked in rest layer");
        try {
            return new ResponseDto(validationService.getCustomRulesByQuery(query), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException ex) {
            LOG.error("[ Error when getting custom rules by query. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
        }
    }

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary Get a custom rule by GUID
     *
     */
    @GET
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path(value = "{guid}")
    @RequiresFeature(UnionVMSFeature.viewAlarmRules)
    public ResponseDto getCustomRuleByGuid(@PathParam(value = "guid") final String guid) {
        LOG.info("Get custom rule by guid invoked in rest layer");
        try {
            return new ResponseDto(rulesService.getCustomRuleByGuid(guid), ResponseCode.OK);
        } catch (RulesFaultException | RulesModelMapperException | RulesServiceException | NullPointerException ex) {
            LOG.error("[ Error when getting custom rule by guid. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
        }
    }

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary Update a custom rule
     *
     */
    @PUT
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @RequiresFeature(UnionVMSFeature.manageAlarmRules)
    public ResponseDto update(final CustomRuleType customRuleType) {
        LOG.info("Update custom rule invoked in rest layer");
        try {
            return new ResponseDto(rulesService.updateCustomRule(customRuleType, UnionVMSFeature.manageGlobalAlarmsRules.name(), getApplicationName(servletContext)), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException e) {
            LOG.error("[ Error when updating. ] {} ", e.getStackTrace());
            return ErrorHandler.getFault(e);
        } catch (AccessDeniedException e) {
            LOG.error("Forbidden access", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary Update a custom rule
     *
     */
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/subscription")
    @RequiresFeature(UnionVMSFeature.manageAlarmRules)
    public ResponseDto updateSubscription(UpdateSubscriptionType updateSubscriptionType) {
        LOG.info("Update subscription invoked in rest layer");
        try {
            return new ResponseDto(rulesService.updateSubscription(updateSubscriptionType, request.getRemoteUser()), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException e) {
            LOG.error("[ Error when updating subscription. ] {} ", e.getStackTrace());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 [Success]
     * @responseMessage 500 [Error]
     *
     * @summary Archive a custom rule
     *
     */
    @DELETE
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/{guid}")
    @RequiresFeature(UnionVMSFeature.manageAlarmRules)
    public ResponseDto deleteCustomRule(@PathParam(value = "guid") final String guid) {
        LOG.info("Delete custom rule invoked in rest layer");
        try {
            return new ResponseDto(rulesService.deleteCustomRule(guid, request.getRemoteUser(),UnionVMSFeature.manageGlobalAlarmsRules.name(), getApplicationName(servletContext)), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException e) {
            LOG.error("[ Error when deleting custom rule. ] {} ", e.getStackTrace());
            return ErrorHandler.getFault(e);
        } catch (AccessDeniedException e) {
            LOG.error("Forbidden access", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

    public String getApplicationName(ServletContext servletContext) {
        String cfgName = servletContext.getInitParameter("usmApplication");
        if (cfgName == null) {
            cfgName = UNION_VMS_APPLICATION;
        }
        return cfgName;
    }

    private boolean validate(CustomRuleType customRule){
        boolean valid = true;
        if(customRule.getName()==null || customRule.getName().isEmpty()){
            valid = false;
        } else if(customRule.getTimeIntervals()!=null && !customRule.getTimeIntervals().isEmpty()){
            for (CustomRuleIntervalType intervalType : customRule.getTimeIntervals()){
                if(DateUtil.parseToUTCDate(intervalType.getStart()).after(DateUtil.parseToUTCDate(intervalType.getEnd()))){
                    valid = false;
                    break;
                }
            }
        }
        int startOperators = 0;
        int endOperators = 0;
        for (int i = 0; i < customRule.getDefinitions().size(); i++) {
            CustomRuleSegmentType segment = customRule.getDefinitions().get(i);
            startOperators += segment.getStartOperator().length();
            endOperators += segment.getEndOperator().length();
            if (LogicOperatorType.NONE.equals(segment.getLogicBoolOperator()) && i < (customRule.getDefinitions().size() - 1)) {
                valid = false;
                break;
            } else if (!LogicOperatorType.NONE.equals(segment.getLogicBoolOperator()) && i == (customRule.getDefinitions().size() - 1)) {
                valid = false;
                break;
            }
        }
        if (startOperators != endOperators) {
            valid = false;
        }
        return valid;
    }
}