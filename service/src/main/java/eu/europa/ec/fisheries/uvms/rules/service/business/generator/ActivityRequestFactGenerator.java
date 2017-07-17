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

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingActivityFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdTypeWithFlagState;
import eu.europa.ec.fisheries.uvms.rules.service.config.AdditionalValidationObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FaReportDocumentType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingActivityType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;

import java.util.*;

import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.*;

/**
 * @author padhyad
 * @author Gregory Rinaldi
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
    public void setAdditionalValidationObject(Collection additionalObject, AdditionalValidationObjectType validationType) {
        if (additionalObject != null && AdditionalValidationObjectType.ASSET_LIST.equals(validationType)) {
            activityFactMapper.setAssetList((List<IdTypeWithFlagState>) additionalObject);
        }
    }

    @Override
    public List<AbstractFact> generateAllFacts() {
        List<AbstractFact> facts = new ArrayList<>();
        facts.add(activityFactMapper.generateFactForFluxReportMessage(fluxfaReportMessage));
        List<FAReportDocument> faReportDocuments = fluxfaReportMessage.getFAReportDocuments();
        if (CollectionUtils.isNotEmpty(faReportDocuments)) {
            facts.addAll(activityFactMapper.generateFactForFaReportDocuments(faReportDocuments));
            int index = 1;
            for (FAReportDocument faReportDocument : faReportDocuments) {

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index);
                facts.addAll(addFacts(faReportDocument.getSpecifiedFishingActivities(), faReportDocument));

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index).append(SPECIFIED_VESSEL_TRANSPORT_MEANS);
                facts.add(activityFactMapper.generateFactForVesselTransportMean(faReportDocument.getSpecifiedVesselTransportMeans(), true));

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index).append(SPECIFIED_VESSEL_TRANSPORT_MEANS, SPECIFIED_STRUCTURED_ADDRESS);
                addFactsForVesselTransportMeansStructuresAddress(facts, Arrays.asList(faReportDocument.getSpecifiedVesselTransportMeans()));

                index++;
            }
        }
        facts.removeAll(Collections.singleton(null));
        return facts;
    }

    private Collection<AbstractFact> addFacts(List<FishingActivity> specifiedFishingActivities, FAReportDocument faReportDocument) {

        List<AbstractFact> facts = new ArrayList<>();

        if (specifiedFishingActivities != null) {
            int index = 1;

            String partialXpath = xPathUtil.getValue();

            for (FishingActivity activity : specifiedFishingActivities) {

                String partialSpecFishActXpath = xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FISHING_ACTIVITY, index).getValue();

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.add(activityFactMapper.generateFactForFishingActivity(activity, faReportDocument));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(activityFactMapper.generateFactForVesselTransportMeans(activity.getRelatedVesselTransportMeans()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForVesselTransportMeansStructuresAddress(facts, activity.getRelatedVesselTransportMeans());

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(activityFactMapper.generateFactsForFaCatch(activity));

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
                facts.addAll(activityFactMapper.generateFactsForFluxLocations(activity.getRelatedFLUXLocations()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForFLUXLocation(facts, activity.getRelatedFLUXLocations(), RELATED_FLUX_LOCATION);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(SPECIFIED_FISHING_TRIP);
                facts.add(activityFactMapper.generateFactForFishingTrip(activity.getSpecifiedFishingTrip()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.add(addAdditionalValidationFact(activity, faReportDocument));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(addAdditionalValidationfactForSubActivities(activity.getRelatedFishingActivities()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(SOURCE_VESSEL_STORAGE_CHARACTERISTIC);
                facts.add(activityFactMapper.generateFactsForVesselStorageCharacteristic(activity.getSourceVesselStorageCharacteristic()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(DESTINATION_VESSEL_STORAGE_CHARACTERISTIC);
                facts.add(activityFactMapper.generateFactsForVesselStorageCharacteristic(activity.getDestinationVesselStorageCharacteristic()));

                index++;
            }
        }

        // If specifiedFishingActivities is empty we need to manually clear the buffer.
        xPathUtil.clear();
        return facts;
    }

    private void addFactsForGearProblems(List<AbstractFact> facts, List<GearProblem> gearProblems) {
        for (GearProblem gearProblem : gearProblems) {
            List<FishingGear> relatedfishingGears = gearProblem.getRelatedFishingGears();
            addFactsForFishingGearAndCharacteristics(facts, relatedfishingGears, RELATED_FISHING_GEAR);
            addFactsForFLUXLocation(facts, gearProblem.getSpecifiedFLUXLocations(), SPECIFIED_FLUX_LOCATION);
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
                addFactsForFLUXLocation(facts, faCatch.getSpecifiedFLUXLocations(), SPECIFIED_FLUX_LOCATION);

                xPathUtil.appendWithoutWrapping(partialCatchXpath);
                addFactsForFLUXLocation(facts, faCatch.getDestinationFLUXLocations(), DESTINATION_FLUX_LOCATION);

                xPathUtil.appendWithoutWrapping(partialCatchXpath);
                facts.addAll(activityFactMapper.generateFactForFishingTrips(faCatch.getRelatedFishingTrips(), RELATED_FISHING_TRIP));

                index++;
            }
        }
        xPathUtil.clear();
    }

    public void addFactsForVesselTransportMeansStructuresAddress(List<AbstractFact> facts, List<VesselTransportMeans> vesselTransportMeanses) {
        String partialXpath = xPathUtil.getValue();
        int index = 1;
        for (VesselTransportMeans vesselTransportMeans : vesselTransportMeanses) {
            if (vesselTransportMeans != null && CollectionUtils.isNotEmpty(vesselTransportMeans.getSpecifiedContactParties())) {
                String partialXpath2 = xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_VESSEL_TRANSPORT_MEANS, index).getValue();
                for (ContactParty contactParty : vesselTransportMeans.getSpecifiedContactParties()) {
                    List<StructuredAddress> structuredAddresses = contactParty.getSpecifiedStructuredAddresses();
                    xPathUtil.appendWithoutWrapping(partialXpath2);
                    addFactsForStructuredAddress(facts, structuredAddresses, SPECIFIED_STRUCTURED_ADDRESS);
                }
            }
            index++;
        }
        xPathUtil.clear();
    }

    private void addFactsForFLUXLocation(List<AbstractFact> facts, List<FLUXLocation> fluxLocations, String fluxLocationType) {
        final String partialXpath = xPathUtil.getValue();
        int index = 1;
        for (FLUXLocation fluxLocation : fluxLocations) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(fluxLocationType, index);
            addFactsForStructuredAddress(facts, fluxLocation.getPostalStructuredAddresses(), POSTAL_STRUCTURED_ADDRESS);
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(fluxLocationType, index).append(PHYSICAL_STRUCTURED_ADDRESS);
            facts.add(activityFactMapper.generateFactsForStructureAddress(fluxLocation.getPhysicalStructuredAddress()));
            index++;
        }
        xPathUtil.clear();
    }

    private void addFactsForStructuredAddress(List<AbstractFact> facts, List<StructuredAddress> structuredAddresses, String adressType) {
        if (CollectionUtils.isNotEmpty(structuredAddresses)) {
            facts.addAll(activityFactMapper.generateFactsForStructureAddresses(structuredAddresses, adressType));
        }
    }

    private AbstractFact addAdditionalValidationFact(FishingActivity activity, FAReportDocument faReportDocument) {
        AbstractFact abstractFact = null;
        try {
            if (activity != null && activity.getTypeCode() != null) {
                FishingActivityType fishingActivityType = FishingActivityType.valueOf(activity.getTypeCode().getValue());
                switch (fishingActivityType) {
                    case DEPARTURE:
                        abstractFact = activityFactMapper.generateFactsForFaDeparture(activity, faReportDocument);
                        break;
                    case ARRIVAL:
                        if (FaReportDocumentType.DECLARATION.name().equals(faReportDocument.getTypeCode().getValue())) {
                            abstractFact = activityFactMapper.generateFactsForDeclarationOfArrival(activity, faReportDocument);
                        } else if (FaReportDocumentType.NOTIFICATION.name().equals(faReportDocument.getTypeCode().getValue())) {
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
                        abstractFact = activityFactMapper.generateFactsForJointFishingOperation(activity, faReportDocument);
                        break;
                    case LANDING:
                        abstractFact = activityFactMapper.generateFactsForLanding(activity, faReportDocument);
                        break;
                    case TRANSHIPMENT:
                        if (FaReportDocumentType.DECLARATION.name().equals(faReportDocument.getTypeCode().getValue())) {
                            abstractFact = activityFactMapper.generateFactsForTranshipment(activity, faReportDocument);
                        } else if (FaReportDocumentType.NOTIFICATION.name().equals(faReportDocument.getTypeCode().getValue())) {
                            abstractFact = activityFactMapper.generateFactsForNotificationOfTranshipment(activity, faReportDocument);
                        }
                        break;
                    default:
                        log.info("No rule to be applied for the received activity type : " + fishingActivityType);

                }
            }
        } catch (IllegalArgumentException e) {
            xPathUtil.clear();
            log.error("No such Fishing activity type", e);
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
    }

    private Collection<AbstractFact> addAdditionalValidationfactForSubActivities(List<FishingActivity> fishingActivities) {
        List<AbstractFact> facts = new ArrayList<>();
        String partialXpath = xPathUtil.getValue();
        if (fishingActivities != null) {
            int index = 1;
            for (FishingActivity activity : fishingActivities) {
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_FISHING_ACTIVITY, index);
                FishingActivityFact fishingActivityFact = activityFactMapper.generateFactForFishingActivity(activity, true);
                fishingActivityFact.setIsSubActivity(true);
                facts.add(fishingActivityFact);
                index++;
            }
        }
        return facts;
    }

}
