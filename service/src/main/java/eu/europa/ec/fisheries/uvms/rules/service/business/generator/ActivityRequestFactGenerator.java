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
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FishingActivityFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;

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
        this.fluxfaReportMessage = (FLUXFAReportMessage)businessObject;
    }

    @Override
    public List<AbstractFact> getAllFacts() {
        List<AbstractFact> facts = new ArrayList<>();
        facts.add(ActivityFactMapper.INSTANCE.generateFactForFluxReportMessage(fluxfaReportMessage));
        if (fluxfaReportMessage.getFAReportDocuments() != null) {
            facts.addAll(ActivityFactMapper.INSTANCE.generateFactForFaReportDocuments(fluxfaReportMessage.getFAReportDocuments()));
            for (FAReportDocument faReportDocument : fluxfaReportMessage.getFAReportDocuments()) {
                facts.addAll(addFacts(faReportDocument.getSpecifiedFishingActivities()));
            }
        }
        return facts;
    }

    private Collection<AbstractFact> addFacts(List<FishingActivity> specifiedFishingActivities) {
        List<AbstractFact> facts = new ArrayList<>();
        if (specifiedFishingActivities != null) {
            for (FishingActivity activity : specifiedFishingActivities) {
                facts.add(ActivityFactMapper.INSTANCE.generateFactForFishingActivity(activity));
                facts.add(addAdditionalValidationFact(activity));
                facts.addAll(addAdditionalValidationfactForSubActivities(activity.getRelatedFishingActivities()));
                //TODO create other facts
            }
        }
        return facts;
    }

    private AbstractFact addAdditionalValidationFact(FishingActivity activity) {
        AbstractFact abstractFact = null;
        if(activity != null) {
            String activityType = activity.getTypeCode().getValue();
            switch (activityType) {
                case "departure" :
                    abstractFact = ActivityFactMapper.INSTANCE.generateFactsForFaDeparture(activity);
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
