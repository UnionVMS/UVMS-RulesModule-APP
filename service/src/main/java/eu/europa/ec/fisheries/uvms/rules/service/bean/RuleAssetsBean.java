/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static java.util.Collections.singleton;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMapperException;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.asset.model.mapper.AssetModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdTypeWithFlagState;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteria;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListCriteriaPair;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListPagination;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;
import eu.europa.ec.fisheries.wsdl.asset.types.ConfigSearchField;
import eu.europa.ec.fisheries.wsdl.user.types.UserFault;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

/**
 * Created by kovian on 03/07/2017.
 */
@Stateless
@LocalBean
@Slf4j
public class RuleAssetsBean {

    @EJB
    private RulesResponseConsumer consumer;

    @EJB
    private RulesMessageProducer producer;

    private List<String> supportedIds = Arrays.asList("IRCS","CFR","EXT_MARK","NAME", "ICCAT");


    public List<IdTypeWithFlagState> getAssetList(Object message){
        FLUXFAReportMessage fluxFaRepMessage;
        if(message instanceof FLUXFAReportMessage){
            fluxFaRepMessage = (FLUXFAReportMessage) message;
        } else {
            return Collections.emptyList();
        }
        List<VesselTransportMeans> vessTranspMeans = extractVesselTransportMeans(fluxFaRepMessage);
        if (CollectionUtils.isEmpty(vessTranspMeans)) {
            return Collections.emptyList();
        }
        AssetListQuery assetListQuery = createAssetListQuery(vessTranspMeans);
        if(CollectionUtils.isEmpty(assetListQuery.getAssetSearchCriteria().getCriterias())){
            log.debug("No compatibile VesselTransportMeans IDs were found so the call to Assets will be avoided! Check your XML!");
            return Collections.emptyList();
        }
        List<Asset> assetsRespList = new ArrayList<>();
        try {
            String request = AssetModuleRequestMapper.createAssetListModuleRequest(assetListQuery);
            log.debug("Send AssetListModuleRequest message to Asset");
            String correlationId = producer.sendDataSourceMessage(request, DataSourceQueue.ASSET);
            TextMessage response = consumer.getMessage(correlationId, TextMessage.class);
            log.debug("Received response message");
            if (response != null && !isUserFault(response)) {
                assetsRespList = AssetModuleResponseMapper.mapToAssetListFromResponse(response, correlationId);
            } else {
                return Collections.emptyList();
            }
        } catch (AssetModelMapperException | MessageException e) {
            log.error("Something went wrong during unmarshalling of the Assets Response!", e);
        }
        return extractAvailableIDsFromAssets(assetsRespList != null ? assetsRespList : Collections.<Asset>emptyList());
    }

    private List<VesselTransportMeans> extractVesselTransportMeans(FLUXFAReportMessage message) {
        List<VesselTransportMeans> vessTranspMeans = new ArrayList<>();
        List<FAReportDocument> faReportDocuments = message.getFAReportDocuments() != null ? message.getFAReportDocuments() : Collections.<FAReportDocument>emptyList();
        for(FAReportDocument faRepDoc : faReportDocuments){
            vessTranspMeans.add(faRepDoc.getSpecifiedVesselTransportMeans());
            List<FishingActivity> specifiedFishingActivities = faRepDoc.getSpecifiedFishingActivities()!= null ? faRepDoc.getSpecifiedFishingActivities() : Collections.<FishingActivity>emptyList();
            for(FishingActivity fishActivity : specifiedFishingActivities){
                vessTranspMeans.addAll(fishActivity.getRelatedVesselTransportMeans());
            }
        }
        vessTranspMeans.removeAll(singleton(null));
        return vessTranspMeans;
    }

    private List<IdTypeWithFlagState> extractAvailableIDsFromAssets(List<Asset> assetsRespList) {
        List<IdTypeWithFlagState> assetTypes = new ArrayList<>();
        for(Asset asset : assetsRespList){
            String countryCode = asset.getCountryCode();
            String ircs = asset.getIrcs();
            if(StringUtils.isNotEmpty(ircs)){
                assetTypes.add(new IdTypeWithFlagState("IRCS", ircs, countryCode));
            }
            String cfr = asset.getCfr();
            if(StringUtils.isNotEmpty(cfr)){
                assetTypes.add(new IdTypeWithFlagState("CFR", cfr, countryCode));
            }
            String externalMarking = asset.getExternalMarking();
            if(StringUtils.isNotEmpty(externalMarking)){
                assetTypes.add(new IdTypeWithFlagState("EXT_MARK", externalMarking, countryCode));
            }
            String name = asset.getName();
            if(StringUtils.isNotEmpty(name)){
                assetTypes.add(new IdTypeWithFlagState("NAME", name, countryCode));
            }
            String iccat = asset.getIccat();
            if(StringUtils.isNotEmpty(iccat)){
                assetTypes.add(new IdTypeWithFlagState("ICCAT", iccat, countryCode));
            }
        }
        return assetTypes;
    }

    private AssetListQuery createAssetListQuery(List<VesselTransportMeans> vessTranspMeans) {
        AssetListQuery assetListQuery = new AssetListQuery();
        AssetListCriteria assetListCriteria = new AssetListCriteria();
        List<AssetListCriteriaPair> criterias = assetListCriteria.getCriterias();
        for (VesselTransportMeans vessMean : vessTranspMeans) {
            List<IDType> ids = vessMean.getIDS();
            for (IDType idType : ids) { // Avoid imo, uvi
                String schemeID = idType.getSchemeID();
                if(!isInSupportedList(schemeID)){
                    log.warn("[WARN] Found not compatibile VesselTransportMeans ID : ["+schemeID+"]");
                    continue;
                }
                AssetListCriteriaPair criteriaPair = new AssetListCriteriaPair();
                if("EXT_MARK".equals(schemeID)){
                    schemeID = "EXTERNAL_MARKING";
                }
                ConfigSearchField configSearchField = ConfigSearchField.fromValue(schemeID);
                criteriaPair.setKey(configSearchField);
                criteriaPair.setValue(idType.getValue());
                criterias.add(criteriaPair);
            }
            assetListCriteria.setIsDynamic(false);
            AssetListPagination pagination = new AssetListPagination();

            pagination.setPage(1);
            pagination.setListSize(1000);
            assetListQuery.setPagination(pagination);
            assetListQuery.setAssetSearchCriteria(assetListCriteria);
        }
        return assetListQuery;
    }

    private boolean isInSupportedList(String schemeID) {
        return supportedIds.contains(schemeID);
    }

    private boolean isUserFault(TextMessage message) {
        boolean isErrorResponse = false;
        try {
            UserFault userFault = JAXBMarshaller.unmarshallTextMessage(message, UserFault.class);
            log.error("UserFault error JMS message received with text: " + userFault.getFault());
            isErrorResponse = true;
        } catch (RulesModelMarshallException e) {
            //do nothing  since it's not a UserFault
            log.debug("It is not user fault. Check passed!");
        }
        return isErrorResponse;
    }


}
