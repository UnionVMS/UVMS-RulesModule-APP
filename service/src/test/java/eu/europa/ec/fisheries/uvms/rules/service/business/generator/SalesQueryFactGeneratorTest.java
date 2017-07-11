package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import eu.europa.ec.fisheries.schema.sales.DelimitedPeriodType;
import eu.europa.ec.fisheries.schema.sales.FLUXPartyType;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesQueryMessage;
import eu.europa.ec.fisheries.schema.sales.IDType;
import eu.europa.ec.fisheries.schema.sales.SalesQueryParameterType;
import eu.europa.ec.fisheries.schema.sales.SalesQueryType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesDelimitedPeriodFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXPartyFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXSalesQueryMessageFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesQueryFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesQueryParameterFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.SalesObjectsHelper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import ma.glasnost.orika.MapperFacade;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by MATBUL on 22/05/2017.
 */
public class SalesQueryFactGeneratorTest {

    private SalesQueryFactGenerator salesQueryFactGenerator;

    private MapperFacade mapper;

    private SalesObjectsHelper helper;

    @Before
    public void setUp() throws Exception {
        FactGeneratorHelper factGeneratorHelper = new FactGeneratorHelper();
        DefaultOrikaMapper defaultOrikaMapper = new DefaultOrikaMapper();
        helper = new SalesObjectsHelper();

        mapper = defaultOrikaMapper.getMapper();
        salesQueryFactGenerator = new SalesQueryFactGenerator(factGeneratorHelper, mapper);
    }

    @Test
    public void getAllFactsWhenChainDoesntContainNull() throws Exception {
        FLUXSalesQueryMessage fluxSalesQueryMessage = new FLUXSalesQueryMessage();

        SalesQueryType query = new SalesQueryType()
                .withID(new IDType().withValue("bla"))
                .withSpecifiedDelimitedPeriod(new DelimitedPeriodType().withStartDateTime(new DateTimeType().withDateTime(DateTime.now())))
                .withSubmittedDateTime(new DateTimeType().withDateTime(DateTime.now()))
                .withSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("This is my ID")))
                .withTypeCode(new CodeType().withValue("My codeType"))
                .withSimpleSalesQueryParameters(new SalesQueryParameterType());


        fluxSalesQueryMessage.setSalesQuery(query);

        salesQueryFactGenerator.setBusinessObjectMessage(fluxSalesQueryMessage);
        List<AbstractFact> allFacts = salesQueryFactGenerator.generateAllFacts();

        List<Class<? extends AbstractFact>> listOfClassesThatShouldBeCreated =
                Arrays.asList(SalesFLUXSalesQueryMessageFact.class, SalesFLUXPartyFact.class,
                        SalesDelimitedPeriodFact.class, SalesQueryFact.class, SalesQueryParameterFact.class);
        List<Class> listOfClassesThatWereCreated = newArrayList();

        for (Class clazz : listOfClassesThatShouldBeCreated) {
            boolean testValid = false;

            testValid = helper.checkIfFactsContainClass(allFacts, listOfClassesThatWereCreated, clazz, testValid);

            assertTrue(clazz + " not found while it was expected", testValid);
        }
    }


}