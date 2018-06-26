/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.rest.service;

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_QUERY_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_REPORT_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.FISHING_GEAR_TYPE_CHARACTERISTICS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.dto.GearMatrix;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulePostProcessBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.DroolsEngineInitializer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.messageprocessors.fa.FaResponseRulesMessageServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResultDto;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;

@Path("/rules")
@Slf4j
public class RulesResource {

    private static final String SS_OO_MME_GUID = "ss-oo-mme-guid";

    @HeaderParam("FR")
    private String fr;

    @HeaderParam("ON")
    private String on;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private DroolsEngineInitializer drlInitializer;

    @EJB
    private GearMatrix gearMatrix;

    @EJB
    private FaResponseRulesMessageServiceBean faResponseValidatorAndSender;

    @POST
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Produces(value = {MediaType.APPLICATION_XML})
    @Path("/evaluate/fluxfareportmessage")
    public Response evaluate(FLUXFAReportMessage request) {
        FLUXResponseMessage fluxResponseMessage = null;
        try {
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, fr);
            extraValues.put(FISHING_GEAR_TYPE_CHARACTERISTICS, gearMatrix.getMatrix());

            List<AbstractFact> facts = rulesEngine.evaluate(RECEIVING_FA_REPORT_MSG, request, extraValues);
            String s = JAXBMarshaller.marshallJaxBObjectToString(request);
            ValidationResultDto validationResultDto = rulePostProcessBean.checkAndUpdateValidationResult(facts, s, SS_OO_MME_GUID, RawMsgType.FA_REPORT);
            fluxResponseMessage = faResponseValidatorAndSender.generateFluxResponseMessageForFaReport(validationResultDto, request);
            XPathRepository.INSTANCE.clear(facts);
        } catch (ActivityModelMarshallException e) {
            log.error(e.getMessage(), e);
        } catch (RulesServiceException | RulesValidationException e) {
            log.error(e.getMessage(), e);
            return Response.ok(e.getMessage()).build();
        }
        return Response.ok(fluxResponseMessage).build();
    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Produces(value = {MediaType.APPLICATION_XML})
    @Path("/evaluate/fluxfaquerymessage")
    public Response evaluate(FLUXFAQueryMessage request) {
        FLUXResponseMessage fluxResponseMessage = null;
        try {
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, fr);

            List<AbstractFact> facts = rulesEngine.evaluate(RECEIVING_FA_QUERY_MSG, request, extraValues);
            String s = JAXBMarshaller.marshallJaxBObjectToString(request);
            ValidationResultDto validationResultDto = rulePostProcessBean.checkAndUpdateValidationResult(facts, s, "ss-oo-mme-guid", RawMsgType.FA_QUERY);
            fluxResponseMessage = faResponseValidatorAndSender.generateFluxResponseMessageForFaQuery(validationResultDto, request, fr);
            XPathRepository.INSTANCE.clear(facts);
        } catch (ActivityModelMarshallException e) {
            log.error(e.getMessage(), e);

        } catch (RulesServiceException | RulesValidationException e) {
            log.error(e.getMessage(), e);
            return Response.ok(e.getMessage()).build();
        }
        return Response.ok(fluxResponseMessage).build();
    }


    @POST
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Produces(value = {MediaType.APPLICATION_XML})
    @Path("/evaluate/fluxfaResponsemessage")
    public Response evaluate(FLUXResponseMessage request) {
        FLUXResponseMessage fluxResponseMessage = null;
        try {
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, fr);

            List<AbstractFact> facts = rulesEngine.evaluate(BusinessObjectType.SENDING_FA_RESPONSE_MSG, request, extraValues);
            String s = JAXBMarshaller.marshallJaxBObjectToString(request);
            ValidationResultDto validationResultDto = rulePostProcessBean.checkAndUpdateValidationResult(facts, s, "ss-oo-mme-guid", RawMsgType.FA_RESPONSE);
            fluxResponseMessage = faResponseValidatorAndSender.generateFluxResponseMessageForFaResponse(validationResultDto, request);
            XPathRepository.INSTANCE.clear(facts);
        } catch (ActivityModelMarshallException e) {
            log.error(e.getMessage(), e);
        } catch (RulesServiceException | RulesValidationException e) {
            log.error(e.getMessage(), e);
            return Response.ok(e.getMessage()).build();
        }
        return Response.ok(fluxResponseMessage).build();
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/reload")
    public Response reloadRules() {
        drlInitializer.reload();
        return Response.ok(new ResponseDto<>("Rules reloading completed.", ResponseCode.OK)).build();
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    @Path("/validation/{flux_report_message_uuid}")
    public Response getValidation(@PathParam("flux_report_message_uuid") String uuid) { // TODO
        return Response.ok(new ResponseDto<>("Rules initialization completed successfully..", ResponseCode.OK)).build();
    }


}