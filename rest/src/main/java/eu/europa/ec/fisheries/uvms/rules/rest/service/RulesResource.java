/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.rest.service;

import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rules.model.dto.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.MessageService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Gregory Rinaldi
 */
@Path("/rules")
@Slf4j
public class RulesResource {

    @EJB
    private MessageService messageService;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private RulesPreProcessBean rulesPreProcessBean;

    @EJB
    private MDRCacheServiceBean mdrService;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private TemplateEngine templateEngine;


    @POST
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Produces(value = {MediaType.APPLICATION_XML})
    @Path("/evaluate/fluxfareportmessage")
    public Response evaluate(FLUXFAReportMessage request) throws ServiceException {
        FLUXResponseMessage fluxResponseMessage;
        try {
            List<AbstractFact> facts = rulesEngine.evaluate(BusinessObjectType.FLUX_ACTIVITY_REQUEST_MSG, request);
            String s = JAXBMarshaller.marshallJaxBObjectToString(request);
            ValidationResultDto validationResultDto = rulePostProcessBean.checkAndUpdateValidationResult(facts, s);
            fluxResponseMessage = messageService.generateFluxResponseMessage(validationResultDto, request);
            XPathRepository.INSTANCE.clear(facts);
        } catch (RulesServiceException | ActivityModelMarshallException | RulesValidationException e) {
            log.error(e.getMessage(), e);
            return Response.ok(e.getMessage()).build();
        }
        return Response.ok(fluxResponseMessage).build();

    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/ispresentlist/{listname}/{codevalue}")
    public Response checkCodeListValueExists(@PathParam("listname") String listName, @PathParam("codevalue") String codeValue) {
        return Response.ok(mdrService.isPresentInList(listName, codeValue)).build();
    }


    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/reinitialize")
    public Response initializeRules() {
            templateEngine.reInitialize();
        return Response.ok("Initialization successfully finished. The Rules DRLs were reloaded.").build();
    }

}