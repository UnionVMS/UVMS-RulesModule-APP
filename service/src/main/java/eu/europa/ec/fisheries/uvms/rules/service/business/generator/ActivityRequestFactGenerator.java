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
import eu.europa.ec.fisheries.uvms.rules.service.constants.FaReportDocumentType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingActivityType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.*;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
@Slf4j
public class ActivityRequestFactGenerator extends AbstractGenerator {


    private FLUXFAReportMessage fluxfaReportMessage;

    XPathStringWrapper xPathUtil = XPathStringWrapper.INSTANCE;

    @Override
    public void setBusinessObjectMessage(Object businessObject) throws RulesValidationException {
        if (!(businessObject instanceof FLUXFAReportMessage)) {
            throw new RulesValidationException("Business object does not match required type");
        }
        this.fluxfaReportMessage = (FLUXFAReportMessage) businessObject;
    }

    @Override
    public List<AbstractFact> generateAllFacts() {
        List<AbstractFact> facts = new ArrayList<>();
        facts.add(ActivityFactMapper.INSTANCE.generateFactForFluxReportMessage(fluxfaReportMessage));
        List<FAReportDocument> faReportDocuments = fluxfaReportMessage.getFAReportDocuments();
        if (CollectionUtils.isNotEmpty(faReportDocuments)) {
            facts.addAll(ActivityFactMapper.INSTANCE.generateFactForFaReportDocuments(faReportDocuments));
            int index = 1;
            for (FAReportDocument faReportDocument : faReportDocuments) {

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index);
                facts.addAll(addFacts(faReportDocument.getSpecifiedFishingActivities(), faReportDocument));

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index).append(SPECIFIED_VESSEL_TRANSPORT_MEANS);
                facts.add(ActivityFactMapper.INSTANCE.generateFactForVesselTransportMean(faReportDocument.getSpecifiedVesselTransportMeans(), true));

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
            for (FishingActivity activity : specifiedFishingActivities) {

                String partialSpecFishActXpath = xPathUtil.appendWithIndex(SPECIFIED_FISHING_ACTIVITY, index).getValue();

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.add(ActivityFactMapper.INSTANCE.generateFactForFishingActivity(activity));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(ActivityFactMapper.INSTANCE.generateFactForVesselTransportMeans(activity.getRelatedVesselTransportMeans()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForVesselTransportMeans(facts, activity.getRelatedVesselTransportMeans());

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForFaCatch(activity));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForFaCatches(facts, activity.getSpecifiedFACatches());

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<FishingGear> fishingGears = activity.getSpecifiedFishingGears();
                addFactsForFishingGearAndCharacteristics(facts, fishingGears, SPECIFIED_FISHING_GEAR);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<GearProblem> gearProblems = activity.getSpecifiedGearProblems();
                facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForGearProblems(gearProblems));
                addFactsForGearProblems(facts, gearProblems);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForFluxLocations(activity.getRelatedFLUXLocations()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForFLUXLocation(facts, activity.getRelatedFLUXLocations(), RELATED_FLUX_LOCATION);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(SPECIFIED_FISHING_TRIP);
                facts.add(ActivityFactMapper.INSTANCE.generateFactForFishingTrip(activity.getSpecifiedFishingTrip()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.add(addAdditionalValidationFact(activity, faReportDocument));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(addAdditionalValidationfactForSubActivities(activity.getRelatedFishingActivities()));

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
        if(CollectionUtils.isNotEmpty(faCatches)) {
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
                facts.addAll(ActivityFactMapper.INSTANCE.generateFactForFishingTrips(faCatch.getRelatedFishingTrips(), RELATED_FISHING_TRIP));

                index++;
            }
        }
        xPathUtil.clear();
    }

    private void addFactsForVesselTransportMeans(List<AbstractFact> facts, List<VesselTransportMeans> vesselTransportMeanses) {
        String partialXpath = xPathUtil.getValue();
        int index = 1;
        for (VesselTransportMeans vesselTransportMeans : vesselTransportMeanses) {
            if (CollectionUtils.isNotEmpty(vesselTransportMeans.getSpecifiedContactParties())) {
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
            facts.add(ActivityFactMapper.INSTANCE.generateFactsForStructureAddress(fluxLocation.getPhysicalStructuredAddress()));
            index++;
        }
        xPathUtil.clear();
    }

    private void addFactsForStructuredAddress(List<AbstractFact> facts, List<StructuredAddress> structuredAddresses, String adressType) {
        if (CollectionUtils.isNotEmpty(structuredAddresses)) {
            facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForStructureAddresses(structuredAddresses, adressType));
        }
    }

    private AbstractFact addAdditionalValidationFact(FishingActivity activity, FAReportDocument faReportDocument) {
        AbstractFact abstractFact = null;
        try {
            if (activity != null && activity.getTypeCode() != null) {
                FishingActivityType fishingActivityType = FishingActivityType.valueOf(activity.getTypeCode().getValue());
                switch (fishingActivityType) {
                    case DEPARTURE:
                        abstractFact = ActivityFactMapper.INSTANCE.generateFactsForFaDeparture(activity, faReportDocument);
                        break;
                    case ARRIVAL:
                        if (FaReportDocumentType.DECLARATION.equals(faReportDocument.getTypeCode().getValue())) {
                            abstractFact = ActivityFactMapper.INSTANCE.generateFactsForDeclarationOfArrival(activity, faReportDocument);
                        } else if (FaReportDocumentType.NOTIFICATION.equals(faReportDocument.getTypeCode().getValue())) {
                            abstractFact = ActivityFactMapper.INSTANCE.generateFactsForPriorNotificationOfArrival(activity, faReportDocument);
                        }
                        break;
                    case AREA_ENTRY:
                        abstractFact = ActivityFactMapper.INSTANCE.generateFactsForEntryIntoSea(activity, faReportDocument);
                        break;
                    case AREA_EXIT:
                        abstractFact = ActivityFactMapper.INSTANCE.generateFactsForExitArea(activity, faReportDocument);
                        break;
                    case JOINT_FISHING_OPERATION:
                        abstractFact = ActivityFactMapper.INSTANCE.generateFactsForJointFishingOperation(activity, faReportDocument);
                        break;
                    case LANDING:
                        abstractFact = ActivityFactMapper.INSTANCE.generateFactsForLanding(activity, faReportDocument);
                        break;
                    case TRANSHIPMENT:
                        if (FaReportDocumentType.DECLARATION.equals(faReportDocument.getTypeCode().getValue())) {
                            abstractFact = ActivityFactMapper.INSTANCE.generateFactsForTranshipment(activity, faReportDocument);
                        } else if (FaReportDocumentType.NOTIFICATION.equals(faReportDocument.getTypeCode().getValue())) {
                            abstractFact = ActivityFactMapper.INSTANCE.generateFactsForNotificationOfTranshipment(activity, faReportDocument);
                        }
                        break;
                    default:
                        log.info("No rule to be applied for the received activity type : "+fishingActivityType);

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
            facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForFishingGears(fishingGears, gearType));

            int index = 1;
            for (FishingGear fishingGear : fishingGears) {
                List<GearCharacteristic> gearCharacteristics = fishingGear.getApplicableGearCharacteristics();
                if (CollectionUtils.isNotEmpty(gearCharacteristics)) {
                    xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(APPLICABLE_GEAR_CHARACTERISTIC, index);
                    facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForGearCharacteristics(gearCharacteristics, APPLICABLE_GEAR_CHARACTERISTIC));
                }
            }
        }
    }

    private Collection<AbstractFact> addAdditionalValidationfactForSubActivities(List<FishingActivity> fishingActivities) {
        List<AbstractFact> facts = new ArrayList<>();
        String partialXpath = xPathUtil.getValue();
        if (fishingActivities != null) {
            int index = 1;
            for (FishingActivity activity : fishingActivities) {
                xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_FISHING_ACTIVITY ,index);
                FishingActivityFact fishingActivityFact = ActivityFactMapper.INSTANCE.generateFactForFishingActivity(activity, true);
                fishingActivityFact.setIsSubActivity(true);
                facts.add(fishingActivityFact);
            }
        }
        return facts;
    }
}
