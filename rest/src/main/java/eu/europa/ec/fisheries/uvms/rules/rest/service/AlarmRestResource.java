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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.uvms.rules.model.dto.AlarmListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

@Path("/alarms")
@Stateless
public class AlarmRestResource {

    final static Logger LOG = LoggerFactory.getLogger(AlarmRestResource.class);

    @EJB
    RulesService serviceLayer;

    /**
     *
     * @responseMessage 200 All alarms matching query fetched
     * @responseMessage 500 No alarms fetched
     *
     * @summary Get a list of all alarms by query
     *
     */
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/list")
    public ResponseDto<AlarmListResponseDto> getCustomRuleList(AlarmQuery query) {
        LOG.info("Get alarm list invoked in rest layer");
        try {
            return new ResponseDto(serviceLayer.getAlarmList(query), ResponseCode.OK);
        } catch (RulesServiceException | NullPointerException ex) {
            LOG.error("[ Error when geting list. ] {} ", ex.getMessage());
            return ErrorHandler.getFault(ex);
        }
    }

    /**
     *
     * @responseMessage 200 Selected alarm updated
     * @responseMessage 500 No alarm updated
     *
     * @summary Update an alarm status
     *
     */
    @PUT
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    public ResponseDto updateAlarmStatus(final AlarmReportType alarmReportType) {
        LOG.info("Update alarm status invoked in rest layer");
        try {
            return new ResponseDto(serviceLayer.updateAlarmStatus(alarmReportType), ResponseCode.OK);
        } catch (RulesServiceException | NullPointerException ex) {
            LOG.error("[ Error when updating. ] {} ", ex.getMessage());
            return ErrorHandler.getFault(ex);
        }
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/{guid}")
    public ResponseDto getAlarmReportByGuid(@PathParam("guid") String guid) {
        try {
            return new ResponseDto(serviceLayer.getAlarmReportByGuid(guid), ResponseCode.OK);
        } catch (RulesServiceException e) {
            LOG.error("[ Error when getting alarm by GUID. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

}
