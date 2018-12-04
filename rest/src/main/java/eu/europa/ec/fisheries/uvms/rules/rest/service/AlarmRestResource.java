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
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import eu.europa.ec.fisheries.schema.rules.alarm.v1.AlarmReportType;
import eu.europa.ec.fisheries.schema.rules.search.v1.AlarmQuery;
import eu.europa.ec.fisheries.uvms.rest.security.RequiresFeature;
import eu.europa.ec.fisheries.uvms.rest.security.UnionVMSFeature;
import eu.europa.ec.fisheries.uvms.rules.model.dto.AlarmListResponseDto;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.rest.error.ErrorHandler;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.movement.RulesMovementProcessorBean;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/alarms")
@Stateless
public class AlarmRestResource {

    private final static Logger LOG = LoggerFactory.getLogger(AlarmRestResource.class);

    @EJB
    private RulesMovementProcessorBean rulesMovementProcessorBean;

    @EJB
    private ValidationService validationService;

    @Context
    private HttpServletRequest request;

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
    @RequiresFeature(UnionVMSFeature.viewAlarmsHoldingTable)
    public ResponseDto<AlarmListResponseDto> getCustomRuleList(AlarmQuery query) {
        LOG.info("Get alarm list invoked in rest layer");
        try {
            return new ResponseDto(rulesMovementProcessorBean.getAlarmList(query), ResponseCode.OK);
        } catch (RulesServiceException | NullPointerException | RulesFaultException  e) {
            LOG.error("[ Error when getting list. ] {} ", e.getMessage());
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
    @RequiresFeature(UnionVMSFeature.manageAlarmsHoldingTable)
    public ResponseDto updateAlarmStatus(final AlarmReportType alarmReportType) {
        LOG.info("Update alarm status invoked in rest layer");
        try {
            return new ResponseDto(rulesMovementProcessorBean.updateAlarmStatus(alarmReportType), ResponseCode.OK);
        } catch (RulesServiceException | NullPointerException e) {
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
    @RequiresFeature(UnionVMSFeature.viewAlarmsHoldingTable)
    public ResponseDto getAlarmReportByGuid(@PathParam("guid") String guid) {
        try {
            return new ResponseDto(rulesMovementProcessorBean.getAlarmReportByGuid(guid), ResponseCode.OK);
        } catch (RulesServiceException e) {
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
    @RequiresFeature(UnionVMSFeature.manageAlarmsHoldingTable)
    public ResponseDto reprocessAlarm(final List<String> alarmGuidList) {
        LOG.info("Reprocess alarm invoked in rest layer");
        try {
            return new ResponseDto(rulesMovementProcessorBean.reprocessAlarm(alarmGuidList, request.getRemoteUser()), ResponseCode.OK);
        } catch (RulesServiceException e) {
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
    @RequiresFeature(UnionVMSFeature.viewAlarmsHoldingTable)
    public ResponseDto getNumberOfOpenAlarmReports() {
        try {
            return new ResponseDto(validationService.getNumberOfOpenAlarmReports(), ResponseCode.OK);
        } catch (RulesServiceException | RulesFaultException e) {
            LOG.error("[ Error when getting number of open alarms. ] {} ", e.getMessage());
            return ErrorHandler.getFault(e);
        }
    }

}