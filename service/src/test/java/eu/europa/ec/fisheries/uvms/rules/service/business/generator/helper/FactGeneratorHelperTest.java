package eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper;

import eu.europa.ec.fisheries.schema.sales.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class FactGeneratorHelperTest {

    @InjectMocks
    private FactGeneratorHelper factGeneratorHelper;

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
        List<Object> foundObjects = factGeneratorHelper.findAllObjectsWithOneOfTheFollowingClasses(report, classesToFind);

        //assert
        assertEquals(8, foundObjects.size());
        assertEquals(fluxSalesReportMessage, foundObjects.get(1));
        assertEquals(purpose, foundObjects.get(2));
        assertEquals(salesReport1, foundObjects.get(3));
        assertEquals(id6, foundObjects.get(4));
        assertEquals(salesReport2, foundObjects.get(5));
        assertEquals(id7, foundObjects.get(6));
        assertEquals(auctionSaleType, foundObjects.get(7));
    }

}