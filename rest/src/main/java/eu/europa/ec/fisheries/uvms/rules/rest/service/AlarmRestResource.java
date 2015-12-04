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

import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
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

import java.util.List;

@Path("/alarms")
@Stateless
public class AlarmRestResource {

    private final static Logger LOG = LoggerFactory.getLogger(AlarmRestResource.class);

    @EJB
    RulesService rulesService;

    @EJB
    ValidationService validationService;

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
            return new ResponseDto(rulesService.getAlarmList(query), ResponseCode.OK);
        } catch (RulesServiceException | NullPointerException | RulesFaultException  e) {
            LOG.error("[ Error when geting list. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
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
            return new ResponseDto(rulesService.updateAlarmStatus(alarmReportType), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException e) {
            LOG.error("[ Error when updating. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 Alarm fetched by GUID
     * @responseMessage 500 No alarm fetched
     *
     * @summary Get an alarm by GUID
     *
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/{guid}")
    public ResponseDto getAlarmReportByGuid(@PathParam("guid") String guid) {
        try {
            return new ResponseDto(rulesService.getAlarmReportByGuid(guid), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when getting alarm by GUID. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 Selected alarms processed
     * @responseMessage 500 Reprocessing of alarms failed
     *
     * @summary Reprocess alarms
     *
     */
    @POST
    @Consumes(value = { MediaType.APPLICATION_JSON })
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/reprocess")
    public ResponseDto reprocessAlarm(final List<String> alarmGuidList) {
        LOG.info("Reprocess alarm invoked in rest layer");
        try {
            return new ResponseDto(rulesService.reprocessAlarm(alarmGuidList), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException | NullPointerException e) {
            LOG.error("[ Error when reprocessing. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

    /**
     *
     * @responseMessage 200 Number of open alarms
     * @responseMessage 500 No result
     *
     * @summary Get number of open alarms
     *
     */
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON })
    @Path("/countopen")
    public ResponseDto getNumberOfOpenAlarmReports() {
        try {
            return new ResponseDto(validationService.getNumberOfOpenAlarmReports(), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when getting number of open alarms. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

}
