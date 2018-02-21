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

import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ACTIVITY_NON_UNIQUE_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ACTIVITY_WITH_TRIP_IDS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.ASSET_LIST;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.FISHING_GEAR_TYPE_CHARACTERISTICS;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
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

import eu.europa.ec.fisheries.uvms.activity.model.schemas.ActivityTableType;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivityWithIdentifiers;
import eu.europa.ec.fisheries.uvms.rules.entity.FishingGearTypeCharacteristic;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdTypeWithFlagState;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FaReportDocumentType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingActivityType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingGear;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearCharacteristic;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearProblem;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.StructuredAddress;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 * @author Andi Kovi
 */
@Slf4j
public class ActivityRequestFactGenerator extends AbstractGenerator {

    private FLUXFAReportMessage fluxfaReportMessage;

    private XPathStringWrapper xPathUtil;

    private ActivityFactMapper activityFactMapper;

    public ActivityRequestFactGenerator() {
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
        List<IdTypeWithFlagState> idTypeWithFlagStates = (List<IdTypeWithFlagState>) extraValueMap.get(ASSET_LIST);
        activityFactMapper.setAssetList(idTypeWithFlagStates);

        Map<ActivityTableType, List<IdType>> activityTableTypeListMap = (Map<ActivityTableType, List<IdType>>) extraValueMap.get(ACTIVITY_NON_UNIQUE_IDS);
        activityFactMapper.setNonUniqueIdsMap(activityTableTypeListMap);

        Map<String, List<FishingActivityWithIdentifiers>> stringListMap = (Map<String, List<FishingActivityWithIdentifiers>>) extraValueMap.get(ACTIVITY_WITH_TRIP_IDS);
        activityFactMapper.setFishingActivitiesWithTripIds(stringListMap);

        String senderReceiver = (String) extraValueMap.get(SENDER_RECEIVER);
        activityFactMapper.setSenderReceiver(senderReceiver);

        List<FishingGearTypeCharacteristic> fishingGearTypeCharacteristics = (List<FishingGearTypeCharacteristic>) extraValueMap.get(FISHING_GEAR_TYPE_CHARACTERISTICS);
        activityFactMapper.setFishingGearTypeCharacteristics(fishingGearTypeCharacteristics);
    }

    @Override
    public List<AbstractFact> generateAllFacts() {
        List<AbstractFact> facts = new ArrayList<>();
        facts.add(activityFactMapper.generateFactForFluxFaReportMessage(fluxfaReportMessage));
        List<FAReportDocument> faReportDocuments = fluxfaReportMessage.getFAReportDocuments();
        if (CollectionUtils.isNotEmpty(faReportDocuments)) {
            facts.addAll(activityFactMapper.generateFactForFaReportDocuments(faReportDocuments));
            int index = 1;
            for (FAReportDocument faReportDocument : faReportDocuments) {

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index);
                facts.addAll(addFacts(faReportDocument.getSpecifiedFishingActivities(), faReportDocument,false));

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index).append(SPECIFIED_VESSEL_TRANSPORT_MEANS);
                facts.add(activityFactMapper.generateFactForVesselTransportMean(faReportDocument.getSpecifiedVesselTransportMeans(), true));

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index).append(SPECIFIED_VESSEL_TRANSPORT_MEANS, SPECIFIED_STRUCTURED_ADDRESS);
                addFactsForVesselTransportMeansStructuresAddress(facts, Collections.singletonList(faReportDocument.getSpecifiedVesselTransportMeans()));

                index++;
            }
        }
        facts.removeAll(Collections.singleton(null));
        return facts;
    }

    private Collection<AbstractFact> addFacts(List<FishingActivity> specifiedFishingActivities, FAReportDocument faReportDocument, boolean isSubActivity) {

        List<AbstractFact> facts = new ArrayList<>();

        if (specifiedFishingActivities != null) {
            int index = 1;

            String partialXpath = xPathUtil.getValue();

            for (FishingActivity activity : specifiedFishingActivities) {

                String partialSpecFishActXpath;

                if(isSubActivity){
                    partialSpecFishActXpath = xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_FISHING_ACTIVITY, index).getValue();
                } else {
                    partialSpecFishActXpath = xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FISHING_ACTIVITY, index).getValue();
                }

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.add(activityFactMapper.generateFactForFishingActivity(activity, faReportDocument,isSubActivity));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(activityFactMapper.generateFactForVesselTransportMeans(activity.getRelatedVesselTransportMeans()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForVesselTransportMeansStructuresAddress(facts, activity.getRelatedVesselTransportMeans());

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(activityFactMapper.generateFactsForFaCatch(activity,isSubActivity, faReportDocument.getTypeCode()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForFaCatches(facts, activity.getSpecifiedFACatches());

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<FishingGear> fishingGears = activity.getSpecifiedFishingGears();
                addFactsForFishingGearAndCharacteristics(facts, fishingGears, SPECIFIED_FISHING_GEAR);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<GearProblem> gearProblems = activity.getSpecifiedGearProblems();
                facts.addAll(activityFactMapper.generateFactsForGearProblems(gearProblems));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForGearProblems(facts, gearProblems);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(activityFactMapper.generateFactsForFluxCharacteristics(activity.getSpecifiedFLUXCharacteristics(), SPECIFIED_FLUX_CHARACTERISTIC));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForFLUXLocation(facts, activity.getRelatedFLUXLocations(), RELATED_FLUX_LOCATION, false);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(SPECIFIED_FISHING_TRIP);
                facts.add(activityFactMapper.generateFactForFishingTrip(activity.getSpecifiedFishingTrip()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.add(addAdditionalValidationFact(activity, faReportDocument));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(SOURCE_VESSEL_STORAGE_CHARACTERISTIC);
                facts.add(activityFactMapper.generateFactsForVesselStorageCharacteristic(activity.getSourceVesselStorageCharacteristic()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(DESTINATION_VESSEL_STORAGE_CHARACTERISTIC);
                facts.add(activityFactMapper.generateFactsForVesselStorageCharacteristic(activity.getDestinationVesselStorageCharacteristic()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(addFacts(activity.getRelatedFishingActivities(),faReportDocument,true));

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

    private void addFactsForVesselTransportMeansStructuresAddress(List<AbstractFact> facts, List<VesselTransportMeans> vesselTransportMeanses) {
        String partialXpath = xPathUtil.getValue();
        int index = 1;
        for (VesselTransportMeans vesselTransportMeans : vesselTransportMeanses) {
            if (vesselTransportMeans != null && CollectionUtils.isNotEmpty(vesselTransportMeans.getSpecifiedContactParties())) {
                String partialXpath2 = xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VESSEL_TRANSPORT_MEANS, index).getValue();
                for (ContactParty contactParty : vesselTransportMeans.getSpecifiedContactParties()) {
                    List<StructuredAddress> structuredAddresses = contactParty.getSpecifiedStructuredAddresses();
                    xPathUtil.appendWithoutWrapping(partialXpath2);
                    activityFactMapper.generateFactsForStructureAddresses(structuredAddresses, SPECIFIED_STRUCTURED_ADDRESS);
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


    private AbstractFact addAdditionalValidationFact(FishingActivity activity, FAReportDocument faReportDocument) {
        AbstractFact abstractFact = null;
        try {
            if (activity != null && activity.getTypeCode() != null && faReportDocument != null) {
                FishingActivityType fishingActivityType = FishingActivityType.valueOf(activity.getTypeCode().getValue());
                CodeType faRepTypeCode = faReportDocument.getTypeCode();
                String faRepTypeCodeVal = faRepTypeCode.getValue() != null ? faRepTypeCode.getValue() : StringUtils.EMPTY;
                switch (fishingActivityType) {
                    case DEPARTURE:
                        abstractFact = activityFactMapper.generateFactsForFaDeparture(activity, faReportDocument);
                        break;
                    case ARRIVAL:
                        if (FaReportDocumentType.DECLARATION.name().equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForDeclarationOfArrival(activity, faReportDocument);
                        } else if (FaReportDocumentType.NOTIFICATION.name().equals(faRepTypeCodeVal)) {
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
                        if (FaReportDocumentType.DECLARATION.name().equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForJointFishingOperation(activity, faReportDocument);
                        }
                        break;
                    case LANDING:
                        abstractFact = activityFactMapper.generateFactsForLanding(activity, faReportDocument);
                        break;
                    case DISCARD:
                        if (FaReportDocumentType.DECLARATION.name().equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForDiscard(activity, faReportDocument);
                        }
                        break;
                    case TRANSHIPMENT:
                        if (FaReportDocumentType.DECLARATION.name().equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForTranshipment(activity, faReportDocument);
                        } else if (FaReportDocumentType.NOTIFICATION.name().equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForNotificationOfTranshipment(activity, faReportDocument);
                        }
                        break;
                    case RELOCATION:
                        if (FaReportDocumentType.NOTIFICATION.name().equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForNotificationOfTranshipment(activity, faReportDocument);
                        } else if (FaReportDocumentType.DECLARATION.name().equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForRelocation(activity, faReportDocument);
                        }
                        break;
                    case FISHING_OPERATION:
                        if (FaReportDocumentType.DECLARATION.name().equals(faRepTypeCodeVal)) {
                            abstractFact = activityFactMapper.generateFactsForFishingOperation(activity, faReportDocument);
                        }
                        break;
                    default:
                        log.trace("No rule to be applied for the received activity type : " + fishingActivityType);

                }
            }
        } catch (IllegalArgumentException e) {
            xPathUtil.clear();
            log.warn("No such Fishing activity type", e);
        }
        xPathUtil.clear();
        return abstractFact;
    }

    private void addFactsForFishingGearAndCharacteristics(List<AbstractFact> facts, List<FishingGear> fishingGears, String gearType) {
        if (CollectionUtils.isNotEmpty(fishingGears)) {
            String partialXpath = xPathUtil.getValue();

            xPathUtil.appendWithoutWrapping(partialXpath);
            facts.addAll(activityFactMapper.generateFactsForFishingGears(fishingGears, gearType));

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


}
