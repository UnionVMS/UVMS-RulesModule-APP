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

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.dto.GearMatrix;
import eu.europa.ec.fisheries.uvms.rules.entity.FAUUIDType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.MessageType;
import eu.europa.ec.fisheries.uvms.rules.service.business.VesselTransportMeansDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.VesselTransportMeansFact;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FaReportDocumentType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.FishingActivityType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.*;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.*;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.FishingActivityType.RELOCATION;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.*;

@Slf4j
public class ActivityFaReportFactGenerator extends AbstractGenerator {

    private FLUXFAReportMessage fluxfaReportMessage;

    private XPathStringWrapper xPathUtil;

    private ActivityFactMapper activityFactMapper;

    public ActivityFaReportFactGenerator(MessageType messageType) {
        super(messageType);
        xPathUtil = new XPathStringWrapper();
        activityFactMapper = new ActivityFactMapper(xPathUtil);
    }

    public ActivityFaReportFactGenerator() {
        super(MessageType.PUSH);
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
        @SuppressWarnings("unchecked")
        List<VesselTransportMeansDto> assets = (List<VesselTransportMeansDto>) extraValueMap.get(ASSET);
        activityFactMapper.setTransportMeans(assets);
        @SuppressWarnings("unchecked")
        List<eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType> idTypeList = (List<eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType>) extraValueMap.get(FA_QUERY_AND_REPORT_IDS);

        if (CollectionUtils.isNotEmpty(idTypeList)) {
            for (eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType idType : idTypeList) {
                String type = idType.getSchemeId();
                if (FAUUIDType.FA_REPORT_ID.name().equals(type)) {
                    activityFactMapper.getFaRelatedReportIds().add(idType);
                } else if (FAUUIDType.FA_MESSAGE_ID.name().equals(type)) {
                    activityFactMapper.getFaReportMessageIds().add(idType);
                } else if (FAUUIDType.FA_QUERY_ID.name().equals(type)) {
                    activityFactMapper.getFaQueryIds().add(idType);
                }
            }
            activityFactMapper.setFaRelatedReportIds(idTypeList);
        }

        @SuppressWarnings("unchecked")
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
            facts.addAll(activityFactMapper.generateFactForFaReportDocuments(faReportDocuments, messageType, getFaMessageOwnerParty(fluxfaReportMessage)));
            int index = 1;
            for (FAReportDocument faReportDocument : faReportDocuments) {

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index);
                List<AbstractFact> factsByReport = new ArrayList<>(addFacts(faReportDocument.getSpecifiedFishingActivities(), faReportDocument, false, null));

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index).append(SPECIFIED_VESSEL_TRANSPORT_MEANS);
                factsByReport.add(activityFactMapper.generateFactForVesselTransportMean(faReportDocument.getSpecifiedVesselTransportMeans(), true, factsByReport));

                xPathUtil.append(FLUXFA_REPORT_MESSAGE).appendWithIndex(FA_REPORT_DOCUMENT, index);
                addFactsForVesselTransportMeansStructuresAddress(factsByReport, Collections.singletonList(faReportDocument.getSpecifiedVesselTransportMeans()), SPECIFIED_VESSEL_TRANSPORT_MEANS);

                FLUXReportDocument relatedFLUXReportDocument = faReportDocument.getRelatedFLUXReportDocument();

                if (relatedFLUXReportDocument != null) { // Set the feReport creation date
                    populateUniqueIDsAndFaReportDocumentDate(relatedFLUXReportDocument, factsByReport);
                }

                facts.addAll(factsByReport);
                index++;
            }
        }
        facts.add(activityFactMapper.generateFactForFluxFaReportMessage(fluxfaReportMessage));
        enrichVesselTransoprtMeanFacts(facts);

        String df = (String) extraValueMap.get(DATA_FLOW);
        List<AbstractFact> nonNullFacts = facts.stream().filter(Objects::nonNull).collect(Collectors.toList());
        nonNullFacts.forEach(fact -> fact.setMessageDataFlow(df));

        return nonNullFacts;
    }

    private void enrichVesselTransoprtMeanFacts(List<AbstractFact> facts) {
        List<VesselTransportMeansDto> transportMeansFromAssets = activityFactMapper.getTransportMeans();
        List<AbstractFact> vessTrpMeansFacts = facts.stream().filter(fact -> fact != null && FactType.VESSEL_TRANSPORT_MEANS.equals(fact.getFactType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(vessTrpMeansFacts) && CollectionUtils.isNotEmpty(transportMeansFromAssets)) {
            vessTrpMeansFacts.forEach(vessAbstrFact -> {
                VesselTransportMeansFact vessFact = (VesselTransportMeansFact) vessAbstrFact;
                List<IdType> vessFactIds = vessFact.getIds();
                transportMeansFromAssets.forEach(assetVessTrpMeans -> {
                    Asset asset = assetVessTrpMeans.getAsset();
                    if (asset != null && CollectionUtils.isNotEmpty(vessFactIds)) { // Check if all the XML Ids match with the ids of this Asset from Assets!
                        AtomicBoolean matches = new AtomicBoolean(true);
                        vessFactIds.forEach(factId -> {
                            String factIdValue = factId.getValue();
                            switch (factId.getSchemeId()) {
                                case "CFR":
                                    if(!StringUtils.equals(factIdValue, asset.getCfr())){
                                        matches.set(false);
                                    }
                                    break;
                                case "IRCS":
                                    if(!StringUtils.equals(factIdValue, asset.getIrcs())){
                                        matches.set(false);
                                    }
                                    break;
                                case "ICCAT":
                                    if(!StringUtils.equals(factIdValue, asset.getIccat())){
                                        matches.set(false);
                                    }
                                    break;
                                case "UVI":
                                    if(!StringUtils.equals(factIdValue, asset.getUvi())){
                                        matches.set(false);
                                    }
                                    break;
                            }
                        });
                        if(matches.get()){
                            vessFact.setTransportMeans(assetVessTrpMeans);
                        }
                    }
                });
            });
        }
    }

    private List<IdType> getFaMessageOwnerParty(FLUXFAReportMessage faReportMessage) {
        if (faReportMessage != null && faReportMessage.getFLUXReportDocument() != null && faReportMessage.getFLUXReportDocument().getOwnerFLUXParty() != null) {
            return activityFactMapper.mapToIdTypes(faReportMessage.getFLUXReportDocument().getOwnerFLUXParty().getIDS());
        }
        return null;
    }

    private Collection<AbstractFact> addFacts(List<FishingActivity> fishingActivities, FAReportDocument faReportDocument, boolean isSubActivity, CodeType mainActivityType) {

        List<AbstractFact> facts = new ArrayList<>();

        if (fishingActivities != null) {
            int index = 1;

            String partialXpath = xPathUtil.getValue();

            for (FishingActivity fishingActivity : fishingActivities) {

                String partialSpecFishActXpath = isSubActivity ? xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(RELATED_FISHING_ACTIVITY, index).getValue() : xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FISHING_ACTIVITY, index).getValue();

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.add(activityFactMapper.generateFishingActivityFact(fishingActivity, isSubActivity, faReportDocument, mainActivityType));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(activityFactMapper.generateFactForVesselTransportMeans(fishingActivity.getRelatedVesselTransportMeans(), facts, fishingActivity.getTypeCode()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForVesselTransportMeansStructuresAddress(facts, fishingActivity.getRelatedVesselTransportMeans(), RELATED_VESSEL_TRANSPORT_MEANS);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(activityFactMapper.generateFactsForFaCatch(fishingActivity, isSubActivity, faReportDocument));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForFaCatches(facts, fishingActivity.getSpecifiedFACatches(), fishingActivity.getTypeCode(), faReportDocument.getTypeCode());

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<FishingGear> fishingGears = fishingActivity.getSpecifiedFishingGears();
                addFactsForFishingGearAndCharacteristics(facts, fishingGears, SPECIFIED_FISHING_GEAR);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                List<GearProblem> gearProblems = fishingActivity.getSpecifiedGearProblems();
                facts.addAll(activityFactMapper.generateFactsForGearProblems(gearProblems));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForGearProblems(facts, gearProblems);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(activityFactMapper.generateFactsForFluxCharacteristics(fishingActivity.getSpecifiedFLUXCharacteristics(), SPECIFIED_FLUX_CHARACTERISTIC));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                addFactsForFLUXLocation(facts, fishingActivity.getRelatedFLUXLocations(), RELATED_FLUX_LOCATION, false, null, null, null);

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(SPECIFIED_FISHING_TRIP);
                facts.add(activityFactMapper.generateFactForFishingTrip(fishingActivity.getSpecifiedFishingTrip()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(addAdditionalValidationFact(fishingActivity, faReportDocument, isSubActivity));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(SOURCE_VESSEL_STORAGE_CHARACTERISTIC);
                facts.add(activityFactMapper.generateFactsForVesselStorageCharacteristic(fishingActivity.getSourceVesselStorageCharacteristic()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath).append(DESTINATION_VESSEL_STORAGE_CHARACTERISTIC);
                facts.add(activityFactMapper.generateFactsForVesselStorageCharacteristic(fishingActivity.getDestinationVesselStorageCharacteristic()));

                xPathUtil.appendWithoutWrapping(partialSpecFishActXpath);
                facts.addAll(addFacts(fishingActivity.getRelatedFishingActivities(), faReportDocument, true, fishingActivity.getTypeCode()));

                index++;
            }
        }

        // If fishingActivities is empty we need to manually clear the buffer.
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
            addFactsForFLUXLocation(facts, gearProblem.getSpecifiedFLUXLocations(), SPECIFIED_FLUX_LOCATION, false, null, null, null);
        }
    }

    private void addFactsForFaCatches(List<AbstractFact> facts, List<FACatch> faCatches, CodeType faActivityType, CodeType faReportType) {
        String partialXpath = xPathUtil.getValue();
        if (CollectionUtils.isNotEmpty(faCatches)) {
            int index = 1;
            for (FACatch faCatch : faCatches) {

                String activityTypeStr = faActivityType != null ? faActivityType.getValue() : StringUtils.EMPTY;
                String faReportTypeStr = faReportType != null ? faReportType.getValue() : StringUtils.EMPTY;

                String partialCatchXpath = xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(SPECIFIED_FA_CATCH, index).getValue();

                xPathUtil.appendWithoutWrapping(partialCatchXpath);
                addFactsForFishingGearAndCharacteristics(facts, faCatch.getUsedFishingGears(), USED_FISHING_GEAR);

                xPathUtil.appendWithoutWrapping(partialCatchXpath);
                addFactsForFLUXLocation(facts, faCatch.getSpecifiedFLUXLocations(), SPECIFIED_FLUX_LOCATION, true, activityTypeStr, faReportTypeStr, faCatch);

                xPathUtil.appendWithoutWrapping(partialCatchXpath);
                addFactsForFLUXLocation(facts, faCatch.getDestinationFLUXLocations(), DESTINATION_FLUX_LOCATION, false, activityTypeStr, faReportTypeStr, faCatch);

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

    private void addFactsForFLUXLocation(List<AbstractFact> facts, List<FLUXLocation> fluxLocations, String fluxLocationType, boolean isSpecifiedFLocationFromFaCatch,
                                         String activityTypeStr, String faReportTypeStr, FACatch factach) {
        final String partialXpath = xPathUtil.getValue();
        int index = 1;
        for (FLUXLocation fluxLocation : fluxLocations) {
            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(fluxLocationType, index);
            facts.addAll(activityFactMapper.generateFactsForStructureAddresses(fluxLocation.getPostalStructuredAddresses(), POSTAL_STRUCTURED_ADDRESS));

            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(fluxLocationType, index).append(PHYSICAL_STRUCTURED_ADDRESS);
            facts.add(activityFactMapper.generateFactsForStructureAddress(fluxLocation.getPhysicalStructuredAddress()));

            xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(fluxLocationType, index);
            facts.add(activityFactMapper.generateFactForFluxLocation(fluxLocation, isSpecifiedFLocationFromFaCatch, activityTypeStr, faReportTypeStr, factach));

            index++;
        }
        xPathUtil.clear();
    }


    private List<AbstractFact> addAdditionalValidationFact(FishingActivity activity, FAReportDocument faReportDocument, boolean isSubActivity) {
        List<AbstractFact> abstractFacts = new ArrayList<>();
        try {
            if (activity != null && activity.getTypeCode() != null && faReportDocument != null) {
                FishingActivityType fishingActivityType = FishingActivityType.valueOf(activity.getTypeCode().getValue());
                CodeType faRepTypeCode = faReportDocument.getTypeCode();
                String faRepTypeCodeVal = (faRepTypeCode != null && faRepTypeCode.getValue() != null) ? faRepTypeCode.getValue() : StringUtils.EMPTY;
                String DECLARATION_STR = FaReportDocumentType.DECLARATION.name();
                String NOTIFICATION_STR = FaReportDocumentType.NOTIFICATION.name();
                switch (fishingActivityType) {
                    case DEPARTURE:
                        abstractFacts.add(activityFactMapper.generateFactsForFaDeparture(activity, faReportDocument));
                        break;
                    case ARRIVAL:
                        if (DECLARATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFacts.add(activityFactMapper.generateFactsForDeclarationOfArrival(activity, faReportDocument));
                        } else if (NOTIFICATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFacts.add(activityFactMapper.generateFactsForPriorNotificationOfArrival(activity, faReportDocument));
                        }
                        break;
                    case AREA_ENTRY:
                        abstractFacts.add(activityFactMapper.generateFactsForEntryIntoSea(activity, faReportDocument));
                        break;
                    case AREA_EXIT:
                        abstractFacts.add(activityFactMapper.generateFactsForExitArea(activity, faReportDocument));
                        break;
                    case JOINT_FISHING_OPERATION:
                        if (DECLARATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFacts.add(activityFactMapper.generateFactsForJointFishingOperation(activity, faReportDocument));
                        }
                        break;
                    case LANDING:
                        abstractFacts.add(activityFactMapper.generateFactsForLanding(activity, faReportDocument));
                        break;
                    case DISCARD:
                        if (DECLARATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFacts.add(activityFactMapper.generateFactsForDiscard(activity, faReportDocument));
                        }
                        break;
                    case TRANSHIPMENT:
                    case RELOCATION:
                        if (DECLARATION_STR.equals(faRepTypeCodeVal)) {
                            if (RELOCATION.equals(fishingActivityType)) {
                                abstractFacts.add(activityFactMapper.generateFactsForRelocation(activity, faReportDocument, isSubActivity));
                            } else {
                                abstractFacts.add(activityFactMapper.generateFactsForTranshipment(activity, faReportDocument));
                            }
                        }
                        // We create also the FaDeclarationOfRelocationOrTranshipmentFact or FaNotificationOfRelocationOrTranshipmentFact for the entity 8.29
                        abstractFacts.add(activityFactMapper.generateFactsForNotificationOrDeclarationOfRelocationOrTranshipment(activity, faReportDocument, isSubActivity));
                        break;
                    case FISHING_OPERATION:
                        if (DECLARATION_STR.equals(faRepTypeCodeVal)) {
                            abstractFacts.add(activityFactMapper.generateFactsForFishingOperation(activity, faReportDocument));
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
        return abstractFacts;
    }

    private void addFactsForFishingGearAndCharacteristics(List<AbstractFact> facts, List<FishingGear> fishingGears, String gearType) {
        if (CollectionUtils.isNotEmpty(fishingGears)) {
            String partialXpath = xPathUtil.getValue();

            xPathUtil.appendWithoutWrapping(partialXpath);
            Map<String, List<GearMatrix.Condition>> matrix = new HashMap<>();
            Map<String, List<GearMatrix.Condition>> matrixNeafc = new HashMap<>();
            if (MapUtils.isNotEmpty(extraValueMap)) {
                Object gearMetrixObj = extraValueMap.get(FISHING_GEAR_TYPE_CHARACTERISTICS);
                if (gearMetrixObj != null) {
                    matrix = (Map<String, List<GearMatrix.Condition>>) gearMetrixObj;
                }
                Object gearMetrixNEAFCObj = extraValueMap.get(FISHING_GEAR_TYPE_NEAFC_CHARACTERISTICS);
                if (gearMetrixObj != null) {
                    matrixNeafc = (Map<String, List<GearMatrix.Condition>>) gearMetrixNEAFCObj;
                }
            }
            facts.addAll(activityFactMapper.generateFactsForFishingGears(fishingGears, gearType, matrix, matrixNeafc));

            int index = 1;
            for (FishingGear fishingGear : fishingGears) {
                List<GearCharacteristic> gearCharacteristics = fishingGear.getApplicableGearCharacteristics();
                if (CollectionUtils.isNotEmpty(gearCharacteristics)) {
                    xPathUtil.appendWithoutWrapping(partialXpath).appendWithIndex(gearType, index);
                    facts.addAll(activityFactMapper.generateFactsForGearCharacteristics(gearCharacteristics, matrix, matrixNeafc));
                }
                index++;
            }
        }

        xPathUtil.clear();
    }

    /**
     * This method is setting unique UUIDs to a list of facts
     * eg. FaReportDocumentIDS, FluxReportMessageIDs, FAResponseMessageIDs
     *
     * @param fluxRepDoc
     * @param facts
     */
    private void populateUniqueIDsAndFaReportDocumentDate(FLUXReportDocument fluxRepDoc, List<AbstractFact> facts) {
        if (fluxRepDoc != null) {
            List<String> strIDs = getIds(fluxRepDoc.getIDS());
            facts.removeAll(Collections.singleton(null));
            DateTime creationDateTime = activityFactMapper.mapToJodaDateTime(fluxRepDoc.getCreationDateTime());
            for (AbstractFact fact : facts) {
                fact.setUniqueIds(strIDs);
                fact.setCreationDateOfMessage(creationDateTime);
            }
        }

    }

    private List<String> getIds(List<IDType> idTypes) {
        ArrayList<String> ids = new ArrayList<>();
        if (CollectionUtils.isEmpty(idTypes)) {
            return ids;
        }
        if (CollectionUtils.isNotEmpty(idTypes)) {
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
