/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.Arrays;
import java.util.Collections;

import eu.europa.ec.fisheries.uvms.rules.dto.GearMatrix;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper.ActivityObjectsHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingGear;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.GearCharacteristic;

public class FishingGearFactTest {

    private GearMatrix matrix = new GearMatrix();

    private FishingGearFact factUnderTest = new FishingGearFact();

    @Before
    public void init(){
        matrix.init();
        factUnderTest.setMatrix(matrix.getMatrix());
    }

    @Test
    public void testWithNullShouldFail(){
        Assert.assertFalse(factUnderTest.valid(null));
    }

    @Test
    public void testWithEmptyListShouldFail(){
        Assert.assertFalse(factUnderTest.valid(new FishingGear()));
    }

    @Test
    public void testWithOTBAndMEOnlyShouldFail(){
        FishingGear otb = ActivityObjectsHelper.generateFishingGear("OTB");
        GearCharacteristic me = ActivityObjectsHelper.generateGearCharacteristic("ME");
        otb.setApplicableGearCharacteristics(Collections.singletonList(me));
        Assert.assertFalse(factUnderTest.valid(otb));
    }

    @Test
    public void testWithOTBMEGMShouldPass(){
        FishingGear otb = ActivityObjectsHelper.generateFishingGear("OTB");
        GearCharacteristic me = ActivityObjectsHelper.generateGearCharacteristic("ME");
        GearCharacteristic gm = ActivityObjectsHelper.generateGearCharacteristic("GM");
        otb.setApplicableGearCharacteristics(Arrays.asList(me, gm));
        Assert.assertTrue(factUnderTest.valid(otb));
    }

    @Test
    public void testWithOTBMEGMGMAShouldPass(){
        FishingGear otb = ActivityObjectsHelper.generateFishingGear("OTB");
        GearCharacteristic me = ActivityObjectsHelper.generateGearCharacteristic("ME");
        GearCharacteristic gm = ActivityObjectsHelper.generateGearCharacteristic("GM");
        GearCharacteristic gm2 = ActivityObjectsHelper.generateGearCharacteristic("GM");
        otb.setApplicableGearCharacteristics(Arrays.asList(me, gm, gm2));
        Assert.assertTrue(factUnderTest.valid(otb));
    }

    @Test
    public void testWithOTBMEMEGMGMAShouldPass(){
        FishingGear otb = ActivityObjectsHelper.generateFishingGear("OTB");
        GearCharacteristic me = ActivityObjectsHelper.generateGearCharacteristic("ME");
        GearCharacteristic me2 = ActivityObjectsHelper.generateGearCharacteristic("ME");
        GearCharacteristic gm = ActivityObjectsHelper.generateGearCharacteristic("GM");
        GearCharacteristic gm2 = ActivityObjectsHelper.generateGearCharacteristic("GM");
        otb.setApplicableGearCharacteristics(Arrays.asList(me, me2, gm, gm2));
        Assert.assertTrue(factUnderTest.valid(otb));
    }

    @Test
    public void testWithSVGMShouldFail(){
        FishingGear sv = ActivityObjectsHelper.generateFishingGear("SV");
        GearCharacteristic gm = ActivityObjectsHelper.generateGearCharacteristic("GM");
        sv.setApplicableGearCharacteristics(Collections.singletonList(gm));
        Assert.assertFalse(factUnderTest.valid(sv));
    }

    @Test
    public void testWithSVGMGMShouldFail(){
        FishingGear sv = ActivityObjectsHelper.generateFishingGear("SV");
        GearCharacteristic gm = ActivityObjectsHelper.generateGearCharacteristic("GM");
        GearCharacteristic gm2 = ActivityObjectsHelper.generateGearCharacteristic("GM");
        sv.setApplicableGearCharacteristics(Arrays.asList(gm, gm2));
        Assert.assertFalse(factUnderTest.valid(sv));
    }

    @Test
    public void testWithSVGMMEShouldPass(){
        FishingGear sv = ActivityObjectsHelper.generateFishingGear("SV");
        GearCharacteristic gm = ActivityObjectsHelper.generateGearCharacteristic("GM");
        GearCharacteristic me = ActivityObjectsHelper.generateGearCharacteristic("ME");
        sv.setApplicableGearCharacteristics(Arrays.asList(gm, me));
        Assert.assertTrue(factUnderTest.valid(sv));
    }

    @Test
    public void testWithSVGMMEMEShouldPass(){
        FishingGear sv = ActivityObjectsHelper.generateFishingGear("SV");
        GearCharacteristic gm = ActivityObjectsHelper.generateGearCharacteristic("GM");
        GearCharacteristic me = ActivityObjectsHelper.generateGearCharacteristic("ME");
        GearCharacteristic me2 = ActivityObjectsHelper.generateGearCharacteristic("ME");
        sv.setApplicableGearCharacteristics(Arrays.asList(gm, me, me2));
        Assert.assertTrue(factUnderTest.valid(sv));
    }

    @Test
    public void testWithTBBMMTShouldFail(){
        FishingGear tbb = ActivityObjectsHelper.generateFishingGear("TBB");
        GearCharacteristic mt = ActivityObjectsHelper.generateGearCharacteristic("MT");
        tbb.setApplicableGearCharacteristics(Collections.singletonList(mt));
        Assert.assertFalse(factUnderTest.valid(tbb));
    }

    @Test
    public void testWithTBBMMTGNShouldFail(){
        FishingGear tbb = ActivityObjectsHelper.generateFishingGear("TBB");
        GearCharacteristic mt = ActivityObjectsHelper.generateGearCharacteristic("MT");
        GearCharacteristic gn = ActivityObjectsHelper.generateGearCharacteristic("GN");
        tbb.setApplicableGearCharacteristics(Arrays.asList(mt, gn));
        Assert.assertFalse(factUnderTest.valid(tbb));
    }

    @Test
    public void testWithTBBMEGMGNShouldPass(){
        FishingGear tbb = ActivityObjectsHelper.generateFishingGear("TBB");
        GearCharacteristic me = ActivityObjectsHelper.generateGearCharacteristic("ME");
        GearCharacteristic gm = ActivityObjectsHelper.generateGearCharacteristic("GM");
        GearCharacteristic gn = ActivityObjectsHelper.generateGearCharacteristic("GN");
        tbb.setApplicableGearCharacteristics(Arrays.asList(me, gn, gm));
        Assert.assertTrue(factUnderTest.valid(tbb));
    }

    @Test
    public void testWithFIXGMShouldPass(){
        FishingGear tbb = ActivityObjectsHelper.generateFishingGear("FIX");
        GearCharacteristic me = ActivityObjectsHelper.generateGearCharacteristic("ME");
        GearCharacteristic gm = ActivityObjectsHelper.generateGearCharacteristic("GM");
        tbb.setApplicableGearCharacteristics(Arrays.asList(me, gm));
        Assert.assertTrue(factUnderTest.valid(tbb));
    }

    @Test
    public void testWithDRBHEShouldFail(){
        FishingGear tbb = ActivityObjectsHelper.generateFishingGear("DRB");
        GearCharacteristic he = ActivityObjectsHelper.generateGearCharacteristic("HE");
        tbb.setApplicableGearCharacteristics(Collections.singletonList(he));
        Assert.assertFalse(factUnderTest.valid(tbb));
    }

    @Test
    public void testWithGNNNQGShouldFail(){
        FishingGear gn = ActivityObjectsHelper.generateFishingGear("GN");
        GearCharacteristic nn = ActivityObjectsHelper.generateGearCharacteristic("NN");
        GearCharacteristic qg = ActivityObjectsHelper.generateGearCharacteristic("QG");
        gn.setApplicableGearCharacteristics(Arrays.asList(nn, qg));
        Assert.assertFalse(factUnderTest.valid(gn));
    }

    @Test
    public void testWithGN_GM_HE_NL_NN_QGShouldPass(){
        FishingGear gn = ActivityObjectsHelper.generateFishingGear("GN");
        GearCharacteristic gm = ActivityObjectsHelper.generateGearCharacteristic("GM");
        GearCharacteristic he = ActivityObjectsHelper.generateGearCharacteristic("HE");
        GearCharacteristic nl = ActivityObjectsHelper.generateGearCharacteristic("NL");
        GearCharacteristic nn = ActivityObjectsHelper.generateGearCharacteristic("NN");
        GearCharacteristic me = ActivityObjectsHelper.generateGearCharacteristic("ME");
        GearCharacteristic qg = ActivityObjectsHelper.generateGearCharacteristic("QG");
        gn.setApplicableGearCharacteristics(Arrays.asList(nn, gm, nl, nl, he, gm, gm, me, qg));
        Assert.assertTrue(factUnderTest.valid(gn));
    }

    @Test
    public void testWithGNNNQGShouldFailTBS(){
        FishingGear gn = ActivityObjectsHelper.generateFishingGear("TBS");
        GearCharacteristic me = ActivityObjectsHelper.generateGearCharacteristic("ME");
        GearCharacteristic gm = ActivityObjectsHelper.generateGearCharacteristic("GM");
        GearCharacteristic gd = ActivityObjectsHelper.generateGearCharacteristic("GD");
        gn.setApplicableGearCharacteristics(Arrays.asList(me, gm, gd));
        Assert.assertTrue(factUnderTest.valid(gn));
    }
}
