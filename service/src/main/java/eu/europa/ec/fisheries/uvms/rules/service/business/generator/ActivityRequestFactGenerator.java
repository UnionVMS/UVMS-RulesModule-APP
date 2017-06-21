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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingActivityFact;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FaReportDocumentType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingActivityType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapperPOC;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
        facts.add(ActivityFactMapperPOC.INSTANCE.generateFactForFluxReportMessage(fluxfaReportMessage));
        List<FAReportDocument> faReportDocuments = fluxfaReportMessage.getFAReportDocuments();
        if (CollectionUtils.isNotEmpty(faReportDocuments)) {
            facts.addAll(ActivityFactMapperPOC.INSTANCE.generateFactForFaReportDocuments(faReportDocuments));
            int index = 1;
            for (FAReportDocument faReportDocument : faReportDocuments) {

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index);
                facts.addAll(addFacts(faReportDocument.getSpecifiedFishingActivities(), faReportDocument));

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index);
                facts.add(ActivityFactMapperPOC.INSTANCE.generateFactForVesselTransportMean(faReportDocument.getSpecifiedVesselTransportMeans(), true));

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
                facts.add(ActivityFactMapperPOC.INSTANCE.generateFactForFishingActivity(activity));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<VesselTransportMeans> vesselTransportMeanses = activity.getRelatedVesselTransportMeans();
                facts.addAll(ActivityFactMapperPOC.INSTANCE.generateFactForVesselTransportMeans(vesselTransportMeanses));
                addFactsForVesselTransportMeans(facts, vesselTransportMeanses);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<FACatch> faCatches = activity.getSpecifiedFACatches();
                for (FACatch faCatch : faCatches) {
                    facts.add(ActivityFactMapper.INSTANCE.generateFactsForFaCatch(faCatch, activity.getRelatedFLUXLocations()));
                }
                addFactsForFaCatches(facts, faCatches);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<FishingGear> fishingGears = activity.getSpecifiedFishingGears();
                facts.addAll(ActivityFactMapperPOC.INSTANCE.generateFactsForFishingGears(fishingGears));
                addFactsForFishingGearAndCharacteristics(facts, fishingGears);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<GearProblem> gearProblems = activity.getSpecifiedGearProblems();
                facts.addAll(ActivityFactMapperPOC.INSTANCE.generateFactsForGearProblems(gearProblems));
                addFactsForGearProblems(facts, gearProblems);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<FLUXLocation> fluxLocations = activity.getRelatedFLUXLocations();
                facts.addAll(ActivityFactMapperPOC.INSTANCE.generateFactsForFluxLocations(fluxLocations));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForFLUXLocation(facts, fluxLocations);

                // Until here
                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.add(ActivityFactMapper.INSTANCE.generateFactForFishingTrip(activity.getSpecifiedFishingTrip()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.add(addAdditionalValidationFact(activity, faReportDocument));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(addAdditionalValidationfactForSubActivities(activity.getRelatedFishingActivities()));
            }
        }

        // If specifiedFishingActivities is empty we need to manually clear the buffer.
        xPathUtil.clear();
        return facts;
    }

    private void addFactsForGearProblems(List<AbstractFact> facts, List<GearProblem> gearProblems) {
        for (GearProblem gearProblem : gearProblems) {
            List<FishingGear> relatedfishingGears = gearProblem.getRelatedFishingGears();
            addFactsForFishingGearAndCharacteristics(facts, relatedfishingGears);
            addFactsForFLUXLocation(facts, gearProblem.getSpecifiedFLUXLocations());
        }
    }

    private void addFactsForFaCatches(List<AbstractFact> facts, List<FACatch> faCatches) {
        for (FACatch faCatch : faCatches) {
            List<FishingGear> fishingGears = faCatch.getUsedFishingGears();
            addFactsForFishingGearAndCharacteristics(facts, fishingGears);
            addFactsForFLUXLocation(facts, faCatch.getSpecifiedFLUXLocations());
            addFactsForFLUXLocation(facts, faCatch.getDestinationFLUXLocations());
            facts.addAll(ActivityFactMapper.INSTANCE.generateFactForFishingTrips(faCatch.getRelatedFishingTrips()));
        }
    }

    private void addFactsForVesselTransportMeans(List<AbstractFact> facts, List<VesselTransportMeans> vesselTransportMeanses) {
        for (VesselTransportMeans vesselTransportMeans : vesselTransportMeanses) {
            if (CollectionUtils.isNotEmpty(vesselTransportMeans.getSpecifiedContactParties())) {
                for (ContactParty contactParty : vesselTransportMeans.getSpecifiedContactParties()) {
                    List<StructuredAddress> structuredAddresses = contactParty.getSpecifiedStructuredAddresses();
                    addFactsForStructuredAddress(facts, structuredAddresses, SPECIFIED_STRUCTURED_ADDRESS);
                }
            }
        }
    }

    private void addFactsForFLUXLocation(List<AbstractFact> facts, List<FLUXLocation> fluxLocations) {
        final String partialXpath = xPathUtil.getValue();
        int index = 1;
        for (FLUXLocation fluxLocation : fluxLocations) {

            List<StructuredAddress> structuredAddresses = fluxLocation.getPostalStructuredAddresses();
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_FLUX_LOCATION, index);
            addFactsForStructuredAddress(facts, structuredAddresses, POSTAL_STRUCTURED_ADDRESS);
            // Sopped here
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_FLUX_LOCATION, index).append(PHYSICAL_STRUCTURED_ADDRESS);
            facts.add(ActivityFactMapper.INSTANCE.generateFactsForStructureAddress(fluxLocation.getPhysicalStructuredAddress()));
        }
    }

    private void addFactsForStructuredAddress(List<AbstractFact> facts, List<StructuredAddress> structuredAddresses, String adressType) {
        if (CollectionUtils.isNotEmpty(structuredAddresses)) {
            facts.addAll(ActivityFactMapperPOC.INSTANCE.generateFactsForStructureAddresses(structuredAddresses, adressType));
        }
        xPathUtil.clear();
    }

    private void addFactsForFishingGearAndCharacteristics(List<AbstractFact> facts, List<FishingGear> fishingGears) {
        if (CollectionUtils.isNotEmpty(fishingGears)) {
            facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForFishingGears(fishingGears));
            for (FishingGear fishingGear : fishingGears) {
                List<GearCharacteristic> gearCharacteristics = fishingGear.getApplicableGearCharacteristics();
                if (CollectionUtils.isNotEmpty(gearCharacteristics)) {
                    facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForGearCharacteristics(gearCharacteristics));
                }
            }
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
            log.error("No such Fishing activity type", e);
        }

        return abstractFact;
    }

    private Collection<AbstractFact> addAdditionalValidationfactForSubActivities(List<FishingActivity> fishingActivities) {
        List<AbstractFact> facts = new ArrayList<>();
        if (fishingActivities != null) {
            for (FishingActivity activity : fishingActivities) {
                FishingActivityFact fishingActivityFact = ActivityFactMapper.INSTANCE.generateFactForFishingActivity(activity, true);
                fishingActivityFact.setIsSubActivity(true);
                facts.add(fishingActivityFact);
            }
        }
        return facts;
    }
}
