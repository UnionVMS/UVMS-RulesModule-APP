/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.rest.service;

import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMarshallException;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.dto.GearMatrix;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulePostProcessBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesKieContainerInitializer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.activity.FaResponseRulesMessageServiceBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.asset.client.impl.AssetClientBean;
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

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_QUERY_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_REPORT_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.*;

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
    private RulesKieContainerInitializer drlInitializer;

    @EJB
    private GearMatrix gearMatrix;

    @EJB
    private FaResponseRulesMessageServiceBean faResponseValidatorAndSender;

    @EJB
    private AssetClientBean assetClientBean;

    @POST
    @Consumes(value = {MediaType.APPLICATION_XML})
    @Produces(value = {MediaType.APPLICATION_XML})
    @Path("/evaluate/fluxfareportmessage")
    public Response evaluate(FLUXFAReportMessage request) throws AssetModelMarshallException, MessageException {
        FLUXResponseMessage fluxResponseMessage = null;
        try {
            Map<ExtraValueType, Object> extraValues = new EnumMap<>(ExtraValueType.class);
            extraValues.put(SENDER_RECEIVER, fr);
            extraValues.put(FISHING_GEAR_TYPE_CHARACTERISTICS, gearMatrix.getMatrix());
            extraValues.put(ASSET, assetClientBean.findHistoryOfAssetBy(request.getFAReportDocuments()));
            Collection<AbstractFact> facts = rulesEngine.evaluate(RECEIVING_FA_REPORT_MSG, request, extraValues, String.valueOf(request.getFLUXReportDocument().getIDS()));
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

            Collection<AbstractFact> facts = rulesEngine.evaluate(RECEIVING_FA_QUERY_MSG, request, extraValues, String.valueOf(request.getFAQuery().getID()));
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

            Collection<AbstractFact> facts = rulesEngine.evaluate(BusinessObjectType.SENDING_FA_RESPONSE_MSG, request, extraValues,  String.valueOf(request.getFLUXResponseDocument().getIDS()));
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