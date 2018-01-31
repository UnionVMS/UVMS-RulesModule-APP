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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.rest.security.RequiresFeature;
import eu.europa.ec.fisheries.uvms.rest.security.UnionVMSFeature;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TicketListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/tickets")
@Stateless
public class TicketRestResource {

    private final static Logger LOG = LoggerFactory.getLogger(TicketRestResource.class);

    @EJB
    RulesService rulesService;

    @EJB
    ValidationService validationService;

    /**
     *
     * @responseMessage 200 All tickets matching query fetched
     * @responseMessage 500 No tickets fetched
     *
     * @summary Get a list of all tickets by query
     *
     */
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/list/{loggedInUser}")
    @RequiresFeature(UnionVMSFeature.viewAlarmsOpenTickets)
    public ResponseDto<TicketListResponseDto> getTicketList(@PathParam("loggedInUser") String loggedInUser, TicketQuery query) {
        LOG.info("Get tickets list invoked in rest layer");
        try {
            return new ResponseDto(rulesService.getTicketList(loggedInUser, query), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException  | NullPointerException ex) {
            LOG.error("[ Error when getting list. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
        }
    }

    /**
     *
     * @responseMessage 200 All tickets for provided movement guids
     * @responseMessage 500 No tickets fetched
     *
     * @summary Get a list of all tickets for provided movement guids
     *
     */
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/listByMovements")
    @RequiresFeature(UnionVMSFeature.viewAlarmsOpenTickets)
    public ResponseDto<TicketListResponseDto> getTicketsByMovements(List<String> movements) {
        LOG.info("Get tickets by movements invoked in rest layer");
        try {
            return new ResponseDto(rulesService.getTicketsByMovements(movements), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException  | NullPointerException ex) {
            LOG.error("[ Error when getting list by movements. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
        }
    }

    /**
     *
     * @responseMessage 200 Number of tickets for provided movement guids
     * @responseMessage 500 Error
     *
     * @summary Get number of tickets for provided movement guids
     *
     */
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/countByMovements")
    @RequiresFeature(UnionVMSFeature.viewAlarmsOpenTickets)
    public ResponseDto countTicketsByMovements(List<String> movements) {
        try {
            return new ResponseDto(rulesService.countTicketsByMovements(movements), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when getting number of open tickets. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 Selected tickets updated
     * @responseMessage 500 No tickets updated
     *
     * @summary Update ticket status
     *
     */
    @PUT
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/status")
    @RequiresFeature(UnionVMSFeature.manageAlarmsOpenTickets)
    public ResponseDto updateTicketStatus(final TicketType ticketType) {
        LOG.info("Update ticket status invoked in rest layer");
        try {
            return new ResponseDto(rulesService.updateTicketStatus(ticketType), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException e) {
            LOG.error("[ Error when updating. ] {} ", e.getStackTrace());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 Selected tickets updated
     * @responseMessage 500 No tickets updated
     *
     * @summary Update ticket status
     *
     */
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/status/{loggedInUser}/{status}")
    @RequiresFeature(UnionVMSFeature.manageAlarmsOpenTickets)
    public ResponseDto updateTicketStatusByQuery(@PathParam("loggedInUser") String loggedInUser, TicketQuery query, @PathParam("status") TicketStatusType status) {
        LOG.info("Update ticket status invoked in rest layer");
        try {
            return new ResponseDto(rulesService.updateTicketStatusByQuery(loggedInUser, query, status), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException e) {
            LOG.error("[ Error when updating. ] {} ", e.getStackTrace());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 Selected ticket fetched
     * @responseMessage 500 Error
     *
     * @summary Get a ticket by GUID
     *
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/{guid}")
    @RequiresFeature(UnionVMSFeature.viewAlarmsOpenTickets)
    public ResponseDto getTicketByGuid(@PathParam("guid") String guid) {
        try {
            return new ResponseDto(rulesService.getTicketByGuid(guid), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when getting ticket by GUID. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 Number of open tickets for logged in user
     * @responseMessage 500 Error
     *
     * @summary Get number of open tickets
     *
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/countopen/{loggedInUser}")
    @RequiresFeature(UnionVMSFeature.viewAlarmsOpenTickets)
    public ResponseDto getNumberOfOpenTicketReports(@PathParam(value = "loggedInUser") final String loggedInUser) {

        try {
            return new ResponseDto(validationService.getNumberOfOpenTickets(loggedInUser), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when getting number of open tickets. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 Number of open tickets for logged in user
     * @responseMessage 500 Error
     *
     * @summary Get number of not sending transponders (used by dashboard widget)
     *
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/countAssetsNotSending")
    @RequiresFeature(UnionVMSFeature.viewAlarmsOpenTickets)
    public ResponseDto getNumberOfAssetsNotSending() {
        try {
            return new ResponseDto(rulesService.getNumberOfAssetsNotSending(), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when getting number of assets not sending. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

}