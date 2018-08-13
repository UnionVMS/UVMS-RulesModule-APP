package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.ActivityObjectsHelper;
import org.junit.Test;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FluxLocationFactTest {


    @Test
    public void testFaCatchContainsSpecifiedFluxLocationOfTypeSAD(){
        FluxLocationFact fact = new FluxLocationFact();
        List<FACatch> faCatches = Collections.singletonList(ActivityObjectsHelper.generateFACatch("ALLOCATED_TO_QUOTA", "BFTTTTTT"));
        fact.setFaCatch(faCatches.get(0));
        assertFalse(fact.faCatchContainsSpecifiedFluxLocationOfType("AREA"));
    }

    @Test
    public void testFaCatchContainsSpecifiedFluxLocationOfTypeHAPPY(){
        FluxLocationFact fact = new FluxLocationFact();
        FACatch faCatch = Collections.singletonList(ActivityObjectsHelper.generateFACatch("ALLOCATED_TO_QUOTA", "BFTTTTTT")).get(0);
        FLUXLocation area = ActivityObjectsHelper.generateFluxLocationWithTypeCodeValue("AREA");
        faCatch.getSpecifiedFLUXLocations().add(area);
        fact.setFaCatch(faCatch);
        assertTrue(fact.faCatchContainsSpecifiedFluxLocationOfType("AREA"));
    }

}
