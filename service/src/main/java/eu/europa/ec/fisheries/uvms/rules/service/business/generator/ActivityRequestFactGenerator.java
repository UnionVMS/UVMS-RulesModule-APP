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
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by padhyad on 4/19/2017.
 */
public class ActivityRequestFactGenerator extends AbstractGenerator {

    private FLUXFAReportMessage fluxfaReportMessage;

    @Override
    public void setBusinessObjectMessage(Object businessObject) throws RulesServiceException {
        if (!(businessObject instanceof FLUXFAReportMessage)) {
            throw new RulesServiceException("Business object does not match required type");
        }
        this.fluxfaReportMessage = (FLUXFAReportMessage) businessObject;
    }

    @Override
    public List<AbstractFact> getAllFacts() {
        List<AbstractFact> facts = new ArrayList<>();
        facts.add(ActivityFactMapper.INSTANCE.generateFactForFluxReportMessage(fluxfaReportMessage));
        if (fluxfaReportMessage.getFAReportDocuments() != null) {
            facts.addAll(ActivityFactMapper.INSTANCE.generateFactForFaReportDocuments(fluxfaReportMessage.getFAReportDocuments()));
            for (FAReportDocument faReportDocument : fluxfaReportMessage.getFAReportDocuments()) {
                facts.addAll(addFacts(faReportDocument.getSpecifiedFishingActivities(), faReportDocument));
            }
        }
        return facts;
    }

    private Collection<AbstractFact> addFacts(List<FishingActivity> specifiedFishingActivities, FAReportDocument faReportDocument) {
        List<AbstractFact> facts = new ArrayList<>();
        if (specifiedFishingActivities != null) {
            for (FishingActivity activity : specifiedFishingActivities) {
                facts.add(ActivityFactMapper.INSTANCE.generateFactForFishingActivity(activity));

                List<VesselTransportMeans> vesselTransportMeanses=activity.getRelatedVesselTransportMeans();
                facts.addAll(ActivityFactMapper.INSTANCE.generateFactForVesselTransportMeans(vesselTransportMeanses));
                addFactsForVesselTransportMeans(facts, vesselTransportMeanses);

                List<FACatch> faCatches= activity.getSpecifiedFACatches();
                facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForFaCatchs(faCatches));
                addFactsForFaCatches(facts, faCatches);

                List<FishingGear> fishingGears=  activity.getSpecifiedFishingGears();
                facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForFishingGears(fishingGears));
                addFactsForFishingGearAndCharacteristics(facts,fishingGears);

                List<GearProblem>  gearProblems=activity.getSpecifiedGearProblems();
                facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForGearProblems(gearProblems));
                addFactsForGearProblems(facts, gearProblems);

                List<FLUXLocation> fluxLocations=  activity.getRelatedFLUXLocations();
                facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForFluxLocations(fluxLocations));
                addFactsForFLUXLocation(facts, fluxLocations);

                facts.add(ActivityFactMapper.INSTANCE.generateFactForFishingTrip(activity.getSpecifiedFishingTrip()));

                facts.add(addAdditionalValidationFact(activity, faReportDocument));
                facts.addAll(addAdditionalValidationfactForSubActivities(activity.getRelatedFishingActivities()));

            }
        }
        return facts;
    }

    private void addFactsForGearProblems(List<AbstractFact> facts, List<GearProblem> gearProblems) {
        for(GearProblem gearProblem:gearProblems){
            List<FishingGear> relatedfishingGears= gearProblem.getRelatedFishingGears();
            addFactsForFishingGearAndCharacteristics(facts, relatedfishingGears);
            addFactsForFLUXLocation(facts, gearProblem.getSpecifiedFLUXLocations());
        }
    }

    private void addFactsForFaCatches(List<AbstractFact> facts, List<FACatch> faCatches) {
        for(FACatch faCatch:faCatches){
            List<FishingGear> fishingGears=  faCatch.getUsedFishingGears();
            addFactsForFishingGearAndCharacteristics(facts, fishingGears);
            addFactsForFLUXLocation(facts, faCatch.getSpecifiedFLUXLocations());
            addFactsForFLUXLocation(facts, faCatch.getDestinationFLUXLocations());
            facts.addAll(ActivityFactMapper.INSTANCE.generateFactForFishingTrips(faCatch.getRelatedFishingTrips()));
        }
    }

    private void addFactsForVesselTransportMeans(List<AbstractFact> facts, List<VesselTransportMeans> vesselTransportMeanses) {
        for(VesselTransportMeans vesselTransportMeans:vesselTransportMeanses){
            if(CollectionUtils.isNotEmpty(vesselTransportMeans.getSpecifiedContactParties())){
                for(ContactParty contactParty:vesselTransportMeans.getSpecifiedContactParties()){
                    List<StructuredAddress> structuredAddresses= contactParty.getSpecifiedStructuredAddresses();
                    addFactsForStructuredAddress(facts, structuredAddresses);
                }
            }
        }
    }

    private void addFactsForFLUXLocation(List<AbstractFact> facts, List<FLUXLocation> fluxLocations) {
        for(FLUXLocation fluxLocation:fluxLocations){
            List<StructuredAddress> structuredAddresses= fluxLocation.getPostalStructuredAddresses();
            addFactsForStructuredAddress(facts, structuredAddresses);
            facts.add(ActivityFactMapper.INSTANCE.generateFactsForStructureAddress(fluxLocation.getPhysicalStructuredAddress()));
        }
    }

    private void addFactsForStructuredAddress(List<AbstractFact> facts, List<StructuredAddress> structuredAddresses) {
        if(CollectionUtils.isNotEmpty(structuredAddresses)){
            facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForStructureAddresses(structuredAddresses));
        }
    }

    private void addFactsForFishingGearAndCharacteristics(List<AbstractFact> facts, List<FishingGear> fishingGears) {
        if(CollectionUtils.isNotEmpty(fishingGears)){
            facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForFishingGears(fishingGears));
            for(FishingGear fishingGear: fishingGears){
                List<GearCharacteristic>  gearCharacteristics= fishingGear.getApplicableGearCharacteristics();
                if(CollectionUtils.isNotEmpty(gearCharacteristics)) {
                    facts.addAll(ActivityFactMapper.INSTANCE.generateFactsForGearCharacteristics(gearCharacteristics));
                }
            }
        }
    }

    private AbstractFact addAdditionalValidationFact(FishingActivity activity, FAReportDocument faReportDocument) {
        AbstractFact abstractFact = null;
        if (activity != null) {
            FishingActivityType fishingActivityType = FishingActivityType.valueOf(activity.getTypeCode().getValue());
            switch (fishingActivityType) {
                case DEPARTURE:
                    abstractFact = ActivityFactMapper.INSTANCE.generateFactsForFaDeparture(activity, faReportDocument);
                    break;
                case ARRIVAL:
                    if (FaReportDocumentType.DECLARATION.equals(faReportDocument.getTypeCode().getValue())) {
                        abstractFact = ActivityFactMapper.INSTANCE.generateFactsForDeclarationOfArrival(activity, faReportDocument);
                    }else if(FaReportDocumentType.NOTIFICATION.equals(faReportDocument.getTypeCode().getValue())){
                        abstractFact = ActivityFactMapper.INSTANCE.generateFactsForPriorNotificationOfArrival(activity, faReportDocument);
                    }
                    break;
                case LANDING:

                      abstractFact = ActivityFactMapper.INSTANCE.generateFactsForLanding(activity, faReportDocument);

                    break;
                default:
                    abstractFact = ActivityFactMapper.INSTANCE.generateFactForFishingActivity(activity);
            }
        }
        return abstractFact;
    }

    private Collection<AbstractFact> addAdditionalValidationfactForSubActivities(List<FishingActivity> fishingActivities) {
        List<AbstractFact> facts = new ArrayList<>();
        if (fishingActivities != null) {
            for (FishingActivity activity : fishingActivities) {
                FishingActivityFact fishingActivityFact = ActivityFactMapper.INSTANCE.generateFactForFishingActivity(activity);
                fishingActivityFact.setIsSubActivity(true);
                facts.add(fishingActivityFact);
            }
        }
        return facts;
    }
}
