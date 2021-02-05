/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/

package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.DATA_FLOW;

import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.MessageType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.MovementReportDocumentFactMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import un.unece.uncefact.data.standard.fluxvesselpositionmessage._4.FLUXVesselPositionMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.FLUXReportDocumentType;

public class MovementFactGenerator extends AbstractGenerator {

    private MovementReportDocumentFactMapper movementReportDocumentFactMapper;
    private XPathStringWrapper xPathUtil;
    private FLUXVesselPositionMessage vesselPositionMessage;

    public MovementFactGenerator(MessageType messageType) {
        super(messageType);
        xPathUtil = new XPathStringWrapper();
        movementReportDocumentFactMapper = new MovementReportDocumentFactMapper(xPathUtil);
    }

    public MovementFactGenerator(MessageType messageType,XPathStringWrapper xPathUtil, MovementReportDocumentFactMapper movementReportDocumentFactMapper) {
        super(messageType);
        this.xPathUtil = xPathUtil;
        this.movementReportDocumentFactMapper = movementReportDocumentFactMapper;
    }

    public MovementFactGenerator() {
        super(MessageType.PUSH);
    }

    @Override
    public List<AbstractFact> generateAllFacts() {
        List<AbstractFact> facts = new ArrayList<>();
        FLUXReportDocumentType fluxReportDocument = vesselPositionMessage.getFLUXReportDocument();

        if (fluxReportDocument != null) {
            facts.add(movementReportDocumentFactMapper.generateFactForMovementReportDocument(vesselPositionMessage));
            facts.addAll(movementReportDocumentFactMapper.generateFactForMovementReportDocumentId(vesselPositionMessage));
            facts.addAll(movementReportDocumentFactMapper.generateFactForMovementReportDocOwnerFluxPartyId(vesselPositionMessage));
            facts.addAll(movementReportDocumentFactMapper.generateFactForMovementVesselTransportMeansId(vesselPositionMessage));
            facts.add(movementReportDocumentFactMapper.generateFactForMovementVesselTransportMeans(vesselPositionMessage));
        }

        String df = (String) extraValueMap.get(DATA_FLOW);
        facts.forEach(fact -> fact.setMessageDataFlow(df));
        xPathUtil.clear();
        return facts;
    }

    @Override
    public void setBusinessObjectMessage(Object businessObject) throws RulesValidationException {

        if (!(businessObject instanceof FLUXVesselPositionMessage)) {
            throw new RulesValidationException("Business object does not match required type");
        }
        this.vesselPositionMessage = (FLUXVesselPositionMessage) businessObject;
    }
}
