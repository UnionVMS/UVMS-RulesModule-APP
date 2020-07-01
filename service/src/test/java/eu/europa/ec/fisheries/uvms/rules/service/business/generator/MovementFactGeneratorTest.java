package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import org.junit.Test;
import un.unece.uncefact.data.standard.fluxvesselpositionmessage._4.FLUXVesselPositionMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.FLUXReportDocumentType;

import java.util.HashMap;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.rules.service.business.MessageType.PUSH;
import static org.junit.Assert.*;

public class MovementFactGeneratorTest {

    @Test
    public void testGenerateAllFacts() throws RulesValidationException {

        MovementFactGenerator generator = new MovementFactGenerator(PUSH);
        FLUXVesselPositionMessage vesselPositionMessage = new FLUXVesselPositionMessage();
        vesselPositionMessage.setFLUXReportDocument(new FLUXReportDocumentType());
        generator.setBusinessObjectMessage(vesselPositionMessage);
        generator.setExtraValueMap(new HashMap<>());
        List<AbstractFact> abstractFacts = generator.generateAllFacts();
        assertEquals(abstractFacts.isEmpty(),false);
    }
}