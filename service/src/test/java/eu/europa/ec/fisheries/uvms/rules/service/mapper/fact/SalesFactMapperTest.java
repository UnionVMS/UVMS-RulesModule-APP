package eu.europa.ec.fisheries.uvms.rules.service.mapper.fact;

import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesAAPProcessFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXPartyFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesReportFact;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class SalesFactMapperTest {

    private SalesFactMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = SalesFactMapper.INSTANCE;
    }

    @Test
    public void test() {
        AAPProcessType aapProcess = new AAPProcessType();
        aapProcess.setConversionFactorNumeric(new NumericType().withValue(BigDecimal.ONE));
        aapProcess.getResultAAPProducts().add(new AAPProductType().withSpeciesCode(new CodeType().withValue("Freddy Fish")));
        aapProcess.getTypeCodes().add(new CodeType().withValue("Loebas"));

        SalesAAPProcessFact salesAAPProcessFact = mapper.generateFactForAAPProcessFact(aapProcess);

        assertEquals(BigDecimal.ONE, salesAAPProcessFact.getConversionFactorNumeric().getValue());
        assertEquals("Freddy Fish", salesAAPProcessFact.getResultAAPProducts().get(0).getSpeciesCode().getValue());
        assertEquals("Loebas", salesAAPProcessFact.getTypeCodes().get(0).getValue());
    }

    @Test
    public void testFLUXPartyTypeWithIDS() {
        FLUXPartyType fluxPartyType = new FLUXPartyType()
                .withIDS(new IDType().withValue("ID"))
                .withNames(new TextType().withValue("NAME"));

        SalesFLUXPartyFact salesFLUXPartyFact = SalesFactMapper.INSTANCE.generateFactForFLUXPartyFact(fluxPartyType);

        assertEquals("ID", salesFLUXPartyFact.getIDS().get(0).getValue());
        assertEquals("NAME", salesFLUXPartyFact.getNames().get(0).getValue());
    }

    @Test
    public void testSalesReportWithID() {
        SalesReportType salesReportType = new SalesReportType();
        salesReportType.withID(new IDType().withValue("ID"));

        SalesReportFact salesReportFact = SalesFactMapper.INSTANCE.generateFactForSalesReportFact(salesReportType);

        assertEquals("ID", salesReportFact.getID().getValue());
    }
}