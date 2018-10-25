package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.MessageType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.SalesObjectsHelper;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import ma.glasnost.orika.MapperFacade;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.CREATION_DATE_OF_MESSAGE;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by MATBUL on 16/06/2017.
 */
public class SalesResponseFactGeneratorTest {
    private SalesResponseFactGenerator salesResponseFactGenerator;

    private MapperFacade mapper;

    private SalesObjectsHelper helper;

    @Before
    public void setUp() throws Exception {
        FactGeneratorHelper factGeneratorHelper = new FactGeneratorHelper();
        DefaultOrikaMapper defaultOrikaMapper = new DefaultOrikaMapper();
        helper = new SalesObjectsHelper();

        mapper = defaultOrikaMapper.getMapper();
        salesResponseFactGenerator = new SalesResponseFactGenerator(factGeneratorHelper, mapper, MessageType.PULL);
    }

    @Test
    public void getAllFactsWhenChainDoesntContainNull() throws Exception {
        //data set
        FLUXResponseDocumentType fluxResponseDocumentType = new FLUXResponseDocumentType()
                .withRespondentFLUXParty(new FLUXPartyType())
                .withRelatedValidationResultDocuments(new ValidationResultDocumentType().withRelatedValidationQualityAnalysises(new ValidationQualityAnalysisType()));

        FLUXSalesResponseMessage fluxSalesResponseMessage = new FLUXSalesResponseMessage()
                .withFLUXResponseDocument(fluxResponseDocumentType)
                .withSalesReports(new SalesReportType());

        DateTime creationDateOfMessage = DateTime.now();

        Map<ExtraValueType, Object> extraValues = new HashMap<>();
        extraValues.put(SENDER_RECEIVER, "BEL");
        extraValues.put(CREATION_DATE_OF_MESSAGE, creationDateOfMessage);

        //execute
        List<AbstractFact> allFacts = salesResponseFactGenerator.generateAllFacts(fluxSalesResponseMessage, extraValues);

        //verify
        List<Class<? extends SalesAbstractFact>> listOfClassesThatShouldBeCreated =
                Arrays.asList(SalesFLUXSalesResponseMessageFact.class, SalesFLUXResponseDocumentFact.class,
                        SalesFLUXPartyFact.class, SalesValidationResultDocumentFact.class, SalesValidationQualityAnalysisFact.class);

        for (Class clazz : listOfClassesThatShouldBeCreated) {
            assertTrue(clazz + " not found while it was expected", helper.checkIfFactsContainClass(allFacts, clazz));
        }

        for (AbstractFact fact : allFacts) {
            assertEquals("BEL", fact.getSenderOrReceiver());
            assertEquals(creationDateOfMessage, fact.getCreationDateOfMessage());
        }
    }


}