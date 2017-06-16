package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.SalesObjectsHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import ma.glasnost.orika.MapperFacade;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
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
        salesResponseFactGenerator = new SalesResponseFactGenerator(factGeneratorHelper, mapper);
    }

    @Test
    public void getAllFactsWhenChainDoesntContainNull() throws Exception {
        FLUXResponseDocumentType fluxResponseDocumentType = new FLUXResponseDocumentType()
                .withRespondentFLUXParty(new FLUXPartyType())
                .withRelatedValidationResultDocuments(new ValidationResultDocumentType().withRelatedValidationQualityAnalysises(new ValidationQualityAnalysisType()));

        FLUXSalesResponseMessage fluxSalesResponseMessage = new FLUXSalesResponseMessage()
                .withFLUXResponseDocument(fluxResponseDocumentType)
                .withSalesReports(new SalesReportType());

        salesResponseFactGenerator.setBusinessObjectMessage(fluxSalesResponseMessage);
        List<AbstractFact> allFacts = salesResponseFactGenerator.getAllFacts();

        List<Class<? extends AbstractFact>> listOfClassesThatShouldBeCreated =
                Arrays.asList(SalesFLUXSalesResponseMessageFact.class, SalesFLUXResponseDocumentFact.class,
                        SalesFLUXPartyFact.class, SalesValidationResultDocumentFact.class, SalesValidationQualityAnalysisFact.class);
        List<Class> listOfClassesThatWereCreated = newArrayList();

        for (Class clazz : listOfClassesThatShouldBeCreated) {
            boolean testValid = false;

            testValid = helper.checkIfFactsContainClass(allFacts, listOfClassesThatWereCreated, clazz, testValid);

            assertTrue(clazz + " not found while it was expected", testValid);
        }
    }


}