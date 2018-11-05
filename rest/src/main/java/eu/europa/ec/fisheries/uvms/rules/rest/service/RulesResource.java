/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.rest.service;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RawMsgType;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.dto.GearMatrix;
import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.rules.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulePostProcessBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesConfigurationCache;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesEngineBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesKieContainerInitializer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.asset.client.IAssetClient;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResult;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FAReportQueryResponseIdsMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FAReportQueryResponseIdsMapperImpl;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.RulesFLUXMessageHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.fluxresponsemessage._6.FLUXResponseMessage;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_QUERY_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.RECEIVING_FA_REPORT_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType.SENDING_FA_REPORT_MSG;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ASSET;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.FA_QUERY_AND_REPORT_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.FISHING_GEAR_TYPE_CHARACTERISTICS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.TRIP_ID;

@Path("/rules")
@Slf4j
public class RulesResource {

    private static final String SS_OO_MME_GUID = "ss-oo-mme-guid";

    @HeaderParam("FR")
    private String fr;

    @HeaderParam("ON")
    private String on;

    @HeaderParam("MESSAGE_TYPE")
    private String msgType;

    @EJB
    private RulePostProcessBean rulePostProcessBean;

    @EJB
    private RulesEngineBean rulesEngine;

    @EJB
    private RulesKieContainerInitializer drlInitializer;

    @EJB
    private GearMatrix gearMatrix;

    private RulesFLUXMessageHelper helper;

    @EJB
    private IAssetClient assetClientBean;

    @EJB
    private RulesConfigurationCache cache;

    private FAReportQueryResponseIdsMapper faIdsMapper;

    @PostConstruct
    public void init() {
        helper = new RulesFLUXMessageHelper(cache);
        faIdsMapper = new FAReportQueryResponseIdsMapperImpl();
    }

    @EJB
    private RulesDao rulesDaoBean;

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
            extraValues.put(ASSET, assetClientBean.findHistoryOfAssetBy(request.getFAReportDocuments()));
            Set<FADocumentID> idsFromIncomingMessage = helper.mapToFADocumentID(request);
            List<FADocumentID> faDocumentIDS = rulesDaoBean.loadFADocumentIDByIdsByIds(idsFromIncomingMessage);
            extraValues.put(FA_QUERY_AND_REPORT_IDS, faIdsMapper.mapToFishingActivityIdDto(faDocumentIDS));

            List<String> faIdsPerTripsFromMessage = helper.collectFaIdsAndTripIds(request);
            List<String> faIdsPerTripsListFromDb = rulesDaoBean.loadExistingFaIdsPerTrip(faIdsPerTripsFromMessage);

            if (msgType == null || "PULL".equals(msgType)) {
                extraValues.put(TRIP_ID, faIdsPerTripsListFromDb);
            }

            BusinessObjectType objectType = (msgType == null || "PULL".equals(msgType)) ? RECEIVING_FA_REPORT_MSG : SENDING_FA_REPORT_MSG;
            Collection<AbstractFact> facts = rulesEngine.evaluate(objectType, request, extraValues, String.valueOf(request.getFLUXReportDocument().getIDS()));
            String s = JAXBMarshaller.marshallJaxBObjectToString(request);
            ValidationResult validationResultDto = rulePostProcessBean.checkAndUpdateValidationResult(facts, s, SS_OO_MME_GUID, RawMsgType.FA_REPORT);
            fluxResponseMessage = helper.generateFluxResponseMessageForFaReport(validationResultDto, request);
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
            ValidationResult validationResultDto = rulePostProcessBean.checkAndUpdateValidationResult(facts, s, "ss-oo-mme-guid", RawMsgType.FA_QUERY);
            fluxResponseMessage = helper.generateFluxResponseMessageForFaQuery(validationResultDto, request, fr);
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
            ValidationResult validationResultDto = rulePostProcessBean.checkAndUpdateValidationResult(facts, s, "ss-oo-mme-guid", RawMsgType.FA_RESPONSE);
            fluxResponseMessage = helper.generateFluxResponseMessageForFaResponse(validationResultDto, request);
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