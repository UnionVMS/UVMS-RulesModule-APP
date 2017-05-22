package eu.europa.ec.fisheries.uvms.rules.service.mapper.fact;

import eu.europa.ec.fisheries.schema.sales.AAPProcessType;
import eu.europa.ec.fisheries.schema.sales.AAPProductType;
import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.NumericType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesAAPProcessFact;
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
}