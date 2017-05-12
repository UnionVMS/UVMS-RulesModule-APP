/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.rest.service;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.bean.MDRServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;

/**
 * @author Gregory Rinaldi
 */
@Path("/rules")
@Slf4j
public class RulesResource {

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private MDRServiceBean mdrService;

    @POST
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/evaluate/{objectType}")
    public Response evaluate(FLUXFAReportMessage request, @PathParam("objectType") BusinessObjectType objectType) throws ServiceException {

        List<AbstractFact> abstractFacts;

        try {

            abstractFacts = rulesEngine.evaluate(objectType, request);

        } catch (RulesValidationException e) {
            log.error(e.getMessage(), e);
            return Response.ok(e.getMessage()).build();
        }
        return Response.ok(abstractFacts).build();

    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/ispresentlist/{listname}/{codevalue}")
    public Response test(@PathParam("listname") String listName, @PathParam("codevalue") String codeValue) {
        return Response.ok(mdrService.isPresentInList(listName, codeValue)).build();
    }

}