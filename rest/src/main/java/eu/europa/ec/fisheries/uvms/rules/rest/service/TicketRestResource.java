package eu.europa.ec.fisheries.uvms.rules.rest.service;

import eu.europa.ec.fisheries.schema.rules.search.v1.TicketQuery;
import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;
import eu.europa.ec.fisheries.uvms.rules.model.dto.TicketListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/tickets")
@Stateless
public class TicketRestResource {

    final static Logger LOG = LoggerFactory.getLogger(TicketRestResource.class);

    @EJB
    RulesService serviceLayer;

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
    @Path("/list")
    public ResponseDto<TicketListResponseDto> getTicketList(TicketQuery query) {
        LOG.info("Get tickets list invoked in rest layer");
        try {
            return new ResponseDto(serviceLayer.getTicketList(query), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException  | NullPointerException ex) {
            LOG.error("[ Error when geting list. ] {} ", ex.getStackTrace());
            return ErrorHandler.getFault(ex);
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
    public ResponseDto updateTicketStatus(final TicketType ticketType) {
        LOG.info("Update ticket status invoked in rest layer");
        try {
            return new ResponseDto(serviceLayer.updateTicketStatus(ticketType), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException e) {
            LOG.error("[ Error when updating. ] {} ", e.getStackTrace());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 Selected ticket fetched
     * @responseMessage 500 No tickets fetched
     *
     * @summary Get a ticket by GUID
     *
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/{guid}")
    public ResponseDto getTicketByGuid(@PathParam("guid") String guid) {
        try {
            return new ResponseDto(serviceLayer.getTicketByGuid(guid), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when getting ticket by GUID. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 Number of open tickets
     * @responseMessage 500 No result
     *
     * @summary Get number of open tickets
     *
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/countopen")
    public ResponseDto getNumberOfOpenAlarmReports() {
        try {
            return new ResponseDto(serviceLayer.getNumberOfOpenTickets(), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when getting number of open tickets. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

}
