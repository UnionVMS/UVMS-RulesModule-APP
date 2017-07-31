package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import eu.europa.ec.fisheries.schema.sales.FLUXPartyType;
import eu.europa.ec.fisheries.schema.sales.FLUXResponseDocumentType;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesResponseMessage;
import eu.europa.ec.fisheries.schema.sales.SalesReportType;
import eu.europa.ec.fisheries.schema.sales.ValidationQualityAnalysisType;
import eu.europa.ec.fisheries.schema.sales.ValidationResultDocumentType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXPartyFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXResponseDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXSalesResponseMessageFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesValidationQualityAnalysisFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesValidationResultDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.SalesObjectsHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import ma.glasnost.orika.MapperFacade;
import org.junit.Before;
import org.junit.Test;

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
        List<AbstractFact> allFacts = salesResponseFactGenerator.generateAllFacts();

        List<Class<? extends SalesAbstractFact>> listOfClassesThatShouldBeCreated =
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