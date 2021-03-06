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
import java.util.List;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.MessageType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.fluxfaquerymessage._3.FLUXFAQueryMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQuery;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.FA_QUERY;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.FLUXFA_QUERY_MESSAGE;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
public class ActivityQueryFactGenerator extends AbstractGenerator {

    private FLUXFAQueryMessage fluxfaQueryMessage;
    private ActivityFactMapper activityFactMapper;
    private XPathStringWrapper xPathUtil;

    public ActivityQueryFactGenerator(MessageType messageType) {
        super(messageType);
        xPathUtil = new XPathStringWrapper();
        activityFactMapper = new ActivityFactMapper(xPathUtil);
    }

    public ActivityQueryFactGenerator() {
        super(MessageType.PUSH);
    }

    @Override
    public void setAdditionalValidationObject() {
        String senderReceiver = (String) extraValueMap.get(SENDER_RECEIVER);
        activityFactMapper.setSenderReceiver(senderReceiver);
    }

    @Override
    public List<AbstractFact> generateAllFacts() {
        List<AbstractFact> factList = new ArrayList<>();
        if (fluxfaQueryMessage != null) {
            FAQuery faQuery = fluxfaQueryMessage.getFAQuery();
            if (faQuery != null) {

                xPathUtil.append(FLUXFA_QUERY_MESSAGE).append(FA_QUERY);
                factList.add(activityFactMapper.generateFactsForFaQuery(faQuery));

                xPathUtil.append(FLUXFA_QUERY_MESSAGE).append(FA_QUERY);
                factList.addAll(activityFactMapper.generateFactsForFaQueryParameters(faQuery.getSimpleFAQueryParameters(), faQuery));
            }
        }
        populateCreationDateTime(factList);
        return factList;
    }

    private void populateCreationDateTime(List<AbstractFact> factList) {
        if (fluxfaQueryMessage != null && fluxfaQueryMessage.getFAQuery() != null) {
            DateTime dateTimeOfCreationOfMessage = activityFactMapper.mapToJodaDateTime(fluxfaQueryMessage.getFAQuery().getSubmittedDateTime());
            if (dateTimeOfCreationOfMessage != null) {
                for (AbstractFact fact : factList) {
                    fact.setCreationDateOfMessage(dateTimeOfCreationOfMessage);
                }
            }
        }
    }

    @Override
    public void setBusinessObjectMessage(Object businessObject) throws RulesValidationException {
        if (!(businessObject instanceof FLUXFAQueryMessage)) {
            throw new RulesValidationException("Business object does not match required type");
        }
        this.fluxfaQueryMessage = (FLUXFAQueryMessage) businessObject;
    }

}
