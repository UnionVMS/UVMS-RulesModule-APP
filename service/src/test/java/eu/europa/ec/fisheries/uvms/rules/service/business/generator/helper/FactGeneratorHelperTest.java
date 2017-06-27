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

        List<Class<?>> classes = new ArrayList<>();
        classes.addAll(Arrays.asList(FLUXSalesReportMessage.class, TextType.class, SalesReportType.class, IDType.class));

        //execute
        List<Object> foundObjects = factGeneratorHelper.findAllObjectsWithOneOfTheFollowingClasses(fluxSalesReportMessage, classes);

        //assert
        assertEquals(6, foundObjects.size());
        assertEquals(fluxSalesReportMessage, foundObjects.get(0));
        assertEquals(purpose, foundObjects.get(1));
        assertEquals(salesReport1, foundObjects.get(2));
        assertEquals(id6, foundObjects.get(3));
        assertEquals(salesReport2, foundObjects.get(4));
        assertEquals(id7, foundObjects.get(5));
    }

}