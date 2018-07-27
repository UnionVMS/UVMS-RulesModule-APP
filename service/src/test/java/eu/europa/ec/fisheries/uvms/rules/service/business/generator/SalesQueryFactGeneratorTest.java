package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.FactGeneratorHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.SalesObjectsHelper;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.DefaultOrikaMapper;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import ma.glasnost.orika.MapperFacade;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.CREATION_DATE_OF_MESSAGE;
import static eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType.SENDER_RECEIVER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        //data set
        FLUXSalesQueryMessage fluxSalesQueryMessage = new FLUXSalesQueryMessage();

        SalesQueryType query = new SalesQueryType()
                .withID(new IDType().withValue("bla"))
                .withSpecifiedDelimitedPeriod(new DelimitedPeriodType().withStartDateTime(new DateTimeType().withDateTime(DateTime.now())))
                .withSubmittedDateTime(new DateTimeType().withDateTime(DateTime.now()))
                .withSubmitterFLUXParty(new FLUXPartyType().withIDS(new IDType().withValue("This is my ID")))
                .withTypeCode(new CodeType().withValue("My codeType"))
                .withSimpleSalesQueryParameters(new SalesQueryParameterType());

        DateTime creationDateOfMessage = DateTime.now();

        Map<ExtraValueType, Object> extraValues = new HashMap<>();
        extraValues.put(SENDER_RECEIVER, "BEL");
        extraValues.put(CREATION_DATE_OF_MESSAGE, creationDateOfMessage);

        fluxSalesQueryMessage.setSalesQuery(query);

        salesQueryFactGenerator.setBusinessObjectMessage(fluxSalesQueryMessage);
        salesQueryFactGenerator.setExtraValueMap(extraValues);

        //execute
        List<AbstractFact> allFacts = salesQueryFactGenerator.generateAllFacts();

        //verify
        List<Class<? extends SalesAbstractFact>> listOfClassesThatShouldBeCreated =
                Arrays.asList(SalesFLUXSalesQueryMessageFact.class, SalesFLUXPartyFact.class,
                        SalesDelimitedPeriodFact.class, SalesQueryFact.class, SalesQueryParameterFact.class);

        for (Class clazz : listOfClassesThatShouldBeCreated) {
            assertTrue(clazz + " not found while it was expected", helper.checkIfFactsContainClass(allFacts, clazz));
        }

        for (AbstractFact fact : allFacts) {
            assertEquals("BEL", fact.getSenderOrReceiver());
            assertEquals(creationDateOfMessage, fact.getCreationDateOfMessage());
        }
    }


}