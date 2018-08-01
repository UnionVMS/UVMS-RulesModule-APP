/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ASSET;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.FA_QUERY_AND_REPORT_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.FISHING_GEAR_TYPE_CHARACTERISTICS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.TRIP_ID;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.APPLICABLE_GEAR_CHARACTERISTIC;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.DESTINATION_FLUX_LOCATION;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.DESTINATION_VESSEL_STORAGE_CHARACTERISTIC;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.FA_REPORT_DOCUMENT;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.FLUXFA_REPORT_MESSAGE;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.PHYSICAL_STRUCTURED_ADDRESS;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.POSTAL_STRUCTURED_ADDRESS;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.RELATED_FISHING_ACTIVITY;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.RELATED_FISHING_GEAR;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.RELATED_FISHING_TRIP;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.RELATED_FLUX_LOCATION;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.RELATED_VESSEL_TRANSPORT_MEANS;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SOURCE_VESSEL_STORAGE_CHARACTERISTIC;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_FA_CATCH;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_FISHING_ACTIVITY;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_FISHING_GEAR;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_FISHING_TRIP;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_FLUX_CHARACTERISTIC;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_FLUX_LOCATION;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_GEAR_PROBLEM;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_STRUCTURED_ADDRESS;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.SPECIFIED_VESSEL_TRANSPORT_MEANS;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.USED_FISHING_GEAR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.rules.dto.GearMatrix;
import eu.europa.ec.fisheries.uvms.rules.entity.FAUUIDType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.VesselTransportMeansDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselTransportMeansFact;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FaReportDocumentType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingActivityType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingGear;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearProblem;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.StructuredAddress;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

@Slf4j
public class ActivityFaReportFactGenerator extends AbstractGenerator {

    private FLUXFAReportMessage fluxfaReportMessage;

    private XPathStringWrapper xPathUtil;

    private ActivityFactMapper activityFactMapper;

    public ActivityFaReportFactGenerator() {
        xPathUtil = new XPathStringWrapper();
        activityFactMapper = new ActivityFactMapper(xPathUtil);
    }

    @Override
    public void setBusinessObjectMessage(Object businessObject) throws RulesValidationException {
        if (!(businessObject instanceof FLUXFAReportMessage)) {
            throw new RulesValidationException("Business object does not match required type");
        }
        this.fluxfaReportMessage = (FLUXFAReportMessage) businessObject;
    }

    @Override
    public void setAdditionalValidationObject() {
        List<VesselTransportMeansDto> assets = (List<VesselTransportMeansDto>) extraValueMap.get(ASSET);
        activityFactMapper.setTransportMeans(assets);
        List<eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType> idTypeList = (List<eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType>) extraValueMap.get(FA_QUERY_AND_REPORT_IDS);

        if (CollectionUtils.isNotEmpty(idTypeList)){
            for (eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType idType : idTypeList) {
                String type = idType.getSchemeId();
                if (FAUUIDType.FA_REPORT_ID.name().equals(type)){
                    activityFactMapper.getFaRelatedReportIds().add(idType);
                } else if (FAUUIDType.FA_MESSAGE_ID.name().equals(type)) {
                    activityFactMapper.getFaReportMessageIds().add(idType);
                } else if(FAUUIDType.FA_QUERY_ID.name().equals(type)){
                    activityFactMapper.getFaQueryIds().add(idType);
                }
            }
        }

        List<String> stringListMap = (List<String>) extraValueMap.get(TRIP_ID);
        activityFactMapper.setFishingActivitiesWithTripIds(stringListMap);

        String senderReceiver = (String) extraValueMap.get(SENDER_RECEIVER);
        activityFactMapper.setSenderReceiver(senderReceiver);
    }

    @Override
    public List<AbstractFact> generateAllFacts() {
        List<AbstractFact> facts = new ArrayList<>();
        List<FAReportDocument> faReportDocuments = fluxfaReportMessage.getFAReportDocuments();
        if (CollectionUtils.isNotEmpty(faReportDocuments)) {
            facts.addAll(activityFactMapper.generateFactForFaReportDocuments(faReportDocuments));
            int index = 1;
            for (FAReportDocument faReportDocument : faReportDocuments) {

                List<AbstractFact> factsByReport = new ArrayList<>();

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index);
                factsByReport.addAll(addFacts(faReportDocument.getSpecifiedFishingActivities(), faReportDocument,false));

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index).append(SPECIFIED_VESSEL_TRANSPORT_MEANS);
                activityFactMapper.generateFactForVesselTransportMean(faReportDocument.getSpecifiedVesselTransportMeans(), true);
                factsByReport.add(activityFactMapper.generateFactForVesselTransportMean(faReportDocument.getSpecifiedVesselTransportMeans(), true));

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index);
                addFactsForVesselTransportMeansStructuresAddress(factsByReport, Collections.singletonList(faReportDocument.getSpecifiedVesselTransportMeans()), SPECIFIED_VESSEL_TRANSPORT_MEANS);

                FLUXReportDocument relatedFLUXReportDocument = faReportDocument.getRelatedFLUXReportDocument();

                if (relatedFLUXReportDocument != null){
                    populateUniqueIDsAndFaReportDocumentDate(relatedFLUXReportDocument, factsByReport);
                }

                facts.addAll(factsByReport);
                index++;
            }
        }
        facts.add(activityFactMapper.generateFactForFluxFaReportMessage(fluxfaReportMessage));
        List<VesselTransportMeansDto> transportMeans = activityFactMapper.getTransportMeans();
        int index = 0;
        for (AbstractFact fact : facts) {
            if (fact instanceof VesselTransportMeansFact && transportMeans != null){
                try {
                    VesselTransportMeansDto vesselTransportMeansDto = transportMeans.get(index);
                    ((VesselTransportMeansFact) fact).setTransportMeans(vesselTransportMeansDto);
                    index++;
                } catch (ArrayIndexOutOfBoundsException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }

        return facts;
    }

    private Collection<AbstractFact> addFacts(List<FishingActivity> specifiedFishingActivities, FAReportDocument faReportDocument, boolean isSubActivity) {

        List<AbstractFact> facts = new ArrayList<>();

        if (specifiedFishingActivities != null) {
            int index = 1;

            String partialXpath = xPathUtil.getValue();

            for (FishingActivity specifiedActivity : specifiedFishingActivities) {

                String partialSpecFishActXpath;

                if(isSubActivity){
                    partialSpecFishActXpath = xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_FISHING_ACTIVITY, index).getValue();
                } else {
                    partialSpecFishActXpath = xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FISHING_ACTIVITY, index).getValue();
                }

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.add(activityFactMapper.generateFishingActivityFact(specifiedActivity, partialXpath, isSubActivity, faReportDocument.getTypeCode()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(activityFactMapper.generateFactForVesselTransportMeans(specifiedActivity.getRelatedVesselTransportMeans()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForVesselTransportMeansStructuresAddress(facts, specifiedActivity.getRelatedVesselTransportMeans(), RELATED_VESSEL_TRANSPORT_MEANS);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(activityFactMapper.generateFactsForFaCatch(specifiedActivity,isSubActivity, faReportDocument.getTypeCode()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForFaCatches(facts, specifiedActivity.getSpecifiedFACatches());

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<FishingGear> fishingGears = specifiedActivity.getSpecifiedFishingGears();
                addFactsForFishingGearAndCharacteristics(facts, fishingGears, SPECIFIED_FISHING_GEAR);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<GearProblem> gearProblems = specifiedActivity.getSpecifiedGearProblems();
                facts.addAll(activityFactMapper.generateFactsForGearProblems(gearProblems));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForGearProblems(facts, gearProblems);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(activityFactMapper.generateFactsForFluxCharacteristics(specifiedActivity.getSpecifiedFLUXCharacteristics(), SPECIFIED_FLUX_CHARACTERISTIC));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForFLUXLocation(facts, specifiedActivity.getRelatedFLUXLocations(), RELATED_FLUX_LOCATION, false);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(SPECIFIED_FISHING_TRIP);
                facts.add(activityFactMapper.generateFactForFishingTrip(specifiedActivity.getSpecifiedFishingTrip()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.add(addAdditionalValidationFact(specifiedActivity, faReportDocument, isSubActivity));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(SOURCE_VESSEL_STORAGE_CHARACTERISTIC);
                facts.add(activityFactMapper.generateFactsForVesselStorageCharacteristic(specifiedActivity.getSourceVesselStorageCharacteristic()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(DESTINATION_VESSEL_STORAGE_CHARACTERISTIC);
                facts.add(activityFactMapper.generateFactsForVesselStorageCharacteristic(specifiedActivity.getDestinationVesselStorageCharacteristic()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(addFacts(specifiedActivity.getRelatedFishingActivities(), faReportDocument, true));

                index++;
            }
        }

        // If specifiedFishingActivities is empty we need to manually clear the buffer.
        xPathUtil.clear();
        return facts;
    }

    private void addFactsForGearProblems(List<AbstractFact> facts, List<GearProblem> gearProblems) {
        if (CollectionUtils.isEmpty(gearProblems)) {
            xPathUtil.clear();
            return;
        }
        String partialXpath = xPathUtil.getValue();
        int index = 1;
        for (GearProblem gearProblem : gearProblems) {

            String partialCatchXpath = xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_GEAR_PROBLEM, index).getValue();

            List<FishingGear> relatedfishingGears = gearProblem.getRelatedFishingGears();

            xPathUtil.appendWithoutWrapping(partialCatchXpath);
            addFactsForFishingGearAndCharacteristics(facts, relatedfishingGears, RELATED_FISHING_GEAR);

            xPathUtil.appendWithoutWrapping(partialCatchXpath);
            addFactsForFLUXLocation(facts, gearProblem.getSpecifiedFLUXLocations(), SPECIFIED_FLUX_LOCATION, false);
        }
    }

    private void addFactsForFaCatches(List<AbstractFact> facts, List<FACatch> faCatches) {
        String partialXpath = xPathUtil.getValue();
        if (CollectionUtils.isNotEmpty(faCatches)) {
            int index = 1;
            for (FACatch faCatch : faCatches) {

                String partialCatchXpath = xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FA_CATCH, index).getValue();

                xPathUtil.appendWithoutWrapping(partialCatchXpath);
                addFactsForFishingGearAndCharacteristics(facts, faCatch.getUsedFishingGears(), USED_FISHING_GEAR);

                xPathUtil.appendWithoutWrapping(partialCatchXpath);
                addFactsForFLUXLocation(facts, faCatch.getSpecifiedFLUXLocations(), SPECIFIED_FLUX_LOCATION, true);

                xPathUtil.appendWithoutWrapping(partialCatchXpath);
                addFactsForFLUXLocation(facts, faCatch.getDestinationFLUXLocations(), DESTINATION_FLUX_LOCATION, false);

                xPathUtil.appendWithoutWrapping(partialCatchXpath);
                facts.addAll(activityFactMapper.generateFactForFishingTrips(faCatch.getRelatedFishingTrips(), RELATED_FISHING_TRIP));

                index++;
            }
        }
        xPathUtil.clear();
    }

    private void addFactsForVesselTransportMeansStructuresAddress(List<AbstractFact> facts, List<VesselTransportMeans> vesselTransportMeanses, String vesselTransportMeansType) {
        String partialXpath = xPathUtil.getValue();
        int index = 1;
        for (VesselTransportMeans vesselTransportMeans : vesselTransportMeanses) {
            if (vesselTransportMeans != null && CollectionUtils.isNotEmpty(vesselTransportMeans.getSpecifiedContactParties())) {
                String partialXpath2 = xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(vesselTransportMeansType, index).getValue();
                for (ContactParty contactParty : vesselTransportMeans.getSpecifiedContactParties()) {
                    List<StructuredAddress> structuredAddresses = contactParty.getSpecifiedStructuredAddresses();
                    xPathUtil.appendWithoutWrapping(partialXpath2);
                    facts.addAll(activityFactMapper.generateFactsForStructureAddresses(structuredAddresses, SPECIFIED_STRUCTURED_ADDRESS));
                }
            }
            index++;
        }
        xPathUtil.clear();
    }

    private void addFactsForFLUXLocation(List<AbstractFact> facts, List<FLUXLocation> fluxLocations, String fluxLocationType, boolean isSpecifiedFLocationFromFaCatch) {
        final String partialXpath = xPathUtil.getValue();
        int index = 1;
        for (FLUXLocation fluxLocation : fluxLocations) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(fluxLocationType, index);
            facts.addAll(activityFactMapper.generateFactsForStructureAddresses(fluxLocation.getPostalStructuredAddresses(), POSTAL_STRUCTURED_ADDRESS));

            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(fluxLocationType, index).append(PHYSICAL_STRUCTURED_ADDRESS);
            facts.add(activityFactMapper.generateFactsForStructureAddress(fluxLocation.getPhysicalStructuredAddress()));

            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(fluxLocationType, index);
            facts.add(activityFactMapper.generateFactForFluxLocation(fluxLocation, isSpecifiedFLocationFromFaCatch));

            index++;
        }
        xPathUtil.clear();
    }


    private AbstractFact addAdditionalValidationFact(FishingActivity activity, FAReportDocument faReportDocument, boolean isSubActivity) {
        AbstractFact abstractFact = null;
        try {
            if (activity != null && activity.getTypeCode() != null && faReportDocument != null) {
                FishingActivityType fishingActivityType = FishingActivityType.valueOf(activity.getTypeCode().getValue());
                CodeType faRepTypeCode = faReportDocument.getTypeCode();
                String faRepTypeCodeVal = (faRepTypeCode!=null && faRepTypeCode.getValue() != null) ? faRepTypeCode.getValue() : StringUtils.EMPTY;
                String DECLARATION_STR = FaReportDocumentType.DECLARATION.name();
                String NOTIFICATION_STR = FaReportDocumentType.NOTIFICATION.name();
                switch (fishingActivityType) {
                    case DEPARTURE:
                        abstractFact = activityFactMapper.generateFactsForFaDeparture(activity, faReportDocument);
                        break;
                    case ARRIVAL:
                        if (DECLARATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForDeclarationOfArrival(activity, faReportDocument);
                        } else if (NOTIFICATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForPriorNotificationOfArrival(activity, faReportDocument);
                        }
                        break;
                    case AREA_ENTRY:
                        abstractFact = activityFactMapper.generateFactsForEntryIntoSea(activity, faReportDocument);
                        break;
                    case AREA_EXIT:
                        abstractFact = activityFactMapper.generateFactsForExitArea(activity, faReportDocument);
                        break;
                    case JOINT_FISHING_OPERATION:
                        if (DECLARATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForJointFishingOperation(activity, faReportDocument);
                        }
                        break;
                    case LANDING:
                        abstractFact = activityFactMapper.generateFactsForLanding(activity, faReportDocument);
                        break;
                    case DISCARD:
                        if (DECLARATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForDiscard(activity, faReportDocument);
                        }
                        break;
                    case TRANSHIPMENT:
                        if (DECLARATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForTranshipment(activity, faReportDocument);
                        } else if (NOTIFICATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForNotificationOfTranshipment(activity, faReportDocument);
                        }
                        break;
                    case RELOCATION:
                        if (NOTIFICATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForNotificationOfTranshipment(activity, faReportDocument);
                        } else if (DECLARATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForRelocation(activity, faReportDocument, isSubActivity);
                        }
                        break;
                    case FISHING_OPERATION:
                        if (DECLARATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForFishingOperation(activity, faReportDocument);
                        }
                        break;
                    default:
                        log.debug("No rule to be applied for the received activity type : " + fishingActivityType);

                }
            }
        } catch (IllegalArgumentException e) {
            xPathUtil.clear();
            log.debug("No such Fishing activity type", e);
        }
        xPathUtil.clear();
        return abstractFact;
    }

    private void addFactsForFishingGearAndCharacteristics(List<AbstractFact> facts, List<FishingGear> fishingGears, String gearType) {
        if (CollectionUtils.isNotEmpty(fishingGears)) {
            String partialXpath = xPathUtil.getValue();

            xPathUtil.appendWithoutWrapping(partialXpath);
            Map<String, List<GearMatrix.Condition>> matrix = new HashMap<>();
            if (MapUtils.isNotEmpty(extraValueMap)){
                Object o = extraValueMap.get(FISHING_GEAR_TYPE_CHARACTERISTICS);
                if (o != null){
                    matrix = (Map<String, List<GearMatrix.Condition>>) o;
                }
            }
            facts.addAll(activityFactMapper.generateFactsForFishingGears(fishingGears, gearType, matrix));

            int index = 1;
            for (FishingGear fishingGear : fishingGears) {
                List<GearCharacteristic> gearCharacteristics = fishingGear.getApplicableGearCharacteristics();
                if (CollectionUtils.isNotEmpty(gearCharacteristics)) {
                    xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(APPLICABLE_GEAR_CHARACTERISTIC, index);
                    facts.addAll(activityFactMapper.generateFactsForGearCharacteristics(gearCharacteristics, APPLICABLE_GEAR_CHARACTERISTIC));
                }
                index++;
            }
        }

        xPathUtil.clear();
    }

    /**
     * This method is setting unique UUIDs to a list of facts
     * eg. FaReportDocumentIDS, FluxReportMessageIDs, FAResponseMessageIDs
     * @param fluxRepDoc
     * @param facts
     */
    private void populateUniqueIDsAndFaReportDocumentDate(FLUXReportDocument fluxRepDoc, List<AbstractFact> facts) {
        if (fluxRepDoc != null){
            List<String> strIDs = getIds(fluxRepDoc.getIDS());
            facts.removeAll(Collections.singleton(null));
            for (AbstractFact fact : facts) {
                fact.setUniqueIds(strIDs);
                fact.setCreationDateOfMessage(activityFactMapper.extractCreationDateTime(fluxRepDoc));
            }
        }

    }

    private List<String> getIds(List<IDType> idTypes) {
        ArrayList<String> ids = new ArrayList<>();
        if (CollectionUtils.isEmpty(idTypes)) {
            return ids;
        }
        if (CollectionUtils.isNotEmpty(idTypes)){
            ids = new ArrayList<>();
            for (IDType idType : idTypes) {
                String value = idType.getValue();
                String schemeID = idType.getSchemeID();
                if (value != null && schemeID != null) {
                    ids.add(value.concat("_").concat(schemeID));
                }
            }
        }
        return ids;
    }

}
