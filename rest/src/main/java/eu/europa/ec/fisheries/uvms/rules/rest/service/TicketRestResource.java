package eu.europa.ec.fisheries.uvms.rules.rest.service;

import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketStatusType;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
     * @summary Get number of not sending transponders by user (used by dashboard widget)
     *
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/countAssetsNotSending")
    public ResponseDto getNumberOfAssetsNotSending() {
        try {
            return new ResponseDto(rulesService.getNumberOfAssetsNotSending(), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when getting number of assets not sending. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

}
