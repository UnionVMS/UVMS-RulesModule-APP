package eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper;

import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.FactCandidate;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FactGeneratorHelperTest {

    private FactGeneratorHelper factGeneratorHelper;

    @Before
    public void init() {
        XPathStringWrapper xPathStringWrapper = new XPathStringWrapper();
        factGeneratorHelper = new FactGeneratorHelper(xPathStringWrapper);
    }

    @Test
    public void findAllObjectsWithOneOfTheFollowingClasses() throws Exception {
        //data set
        TextType purpose = new TextType().withValue("5");
        IDType id6 = new IDType().withValue("6");
        IDType id7 = new IDType().withValue("7");
        SalesReportType salesReport1 = new SalesReportType().withID(id6);
        SalesReportType salesReport2 = new SalesReportType().withID(id7);
        FLUXSalesReportMessage fluxSalesReportMessage = new FLUXSalesReportMessage()
                .withFLUXReportDocument(new FLUXReportDocumentType().withPurpose(purpose))
                .withSalesReports(salesReport1, salesReport2);

        // Added an object that contains an enum from the sales/uncefact namespace to check it doesn't cause a stack overflow
        AuctionSaleType auctionSaleType = new AuctionSaleType()
                .withSalesCategory(SalesCategoryType.VARIOUS_SUPPLY);

        Report report = new Report()
                .withAuctionSale(auctionSaleType)
                .withFLUXSalesReportMessage(fluxSalesReportMessage);


        List<Class<?>> classesToFind = new ArrayList<>();
        classesToFind.addAll(Arrays.asList(FLUXSalesReportMessage.class, TextType.class, SalesReportType.class, IDType.class, AuctionSaleType.class, Report.class));

        //execute
        List<FactCandidate> foundObjects = factGeneratorHelper.findAllObjectsWithOneOfTheFollowingClasses(report, classesToFind);

        //assert
        assertEquals(8, foundObjects.size());

        assertEquals(report, foundObjects.get(0).getObject());

        assertEquals(fluxSalesReportMessage, foundObjects.get(1).getObject());
        assertEquals("//*[local-name()='fluxSalesReportMessage']//*[local-name()='fluxReportDocument']", foundObjects.get(1).getPropertiesAndTheirXPaths().get("fluxReportDocument"));
        assertEquals("//*[local-name()='fluxSalesReportMessage']//*[local-name()='salesReports']", foundObjects.get(1).getPropertiesAndTheirXPaths().get("salesReports"));

        assertEquals(purpose, foundObjects.get(2).getObject());
        assertEquals("//*[local-name()='fluxSalesReportMessage']//*[local-name()='fluxReportDocument']//*[local-name()='purpose']//*[local-name()='value']", foundObjects.get(2).getPropertiesAndTheirXPaths().get("value"));

        assertEquals(salesReport1, foundObjects.get(3).getObject());
        assertEquals("(//*[local-name()='fluxSalesReportMessage']//*[local-name()='salesReports'])[0]//*[local-name()='id']", foundObjects.get(3).getPropertiesAndTheirXPaths().get("id"));

        assertEquals(id6, foundObjects.get(4).getObject());
        assertEquals("(//*[local-name()='fluxSalesReportMessage']//*[local-name()='salesReports'])[0]//*[local-name()='id']//*[local-name()='value']", foundObjects.get(4).getPropertiesAndTheirXPaths().get("value"));

        assertEquals(salesReport2, foundObjects.get(5).getObject());
        assertEquals(id7, foundObjects.get(6).getObject());
        assertEquals(auctionSaleType, foundObjects.get(7).getObject());
    }

}