/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.fisheries.uvms.rules.service.business;

//import static org.junit.Assert.assertEquals;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaArrivalFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.*;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RulesValidatorTest {
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
    }

    /**
     * @throws Exception
     */
    @Test
    public void testResourceDrl() throws Exception {

    }


    @Test
    public void testSome(){
        int i =0;
        while (i < 10) {
            testi();
            i++;
        }
    }

    private void testi(){

        boolean fail = RandomUtils.nextInt(1, 100) % 2 == 0;

        String strToAddToFail = fail ? "abc" : StringUtils.EMPTY;

        FaArrivalFact abstrFact = new FaArrivalFact();

        int addTo = RandomUtils.nextInt(1, 5);

        List<IdType> idList = new ArrayList<>();

        IdType idType_1 = new IdType();
        idType_1.setValue("ATEU0MAR00000");
        idType_1.setSchemeId("ICCAT");
        idList.add(idType_1);

        IdType idType_2 = new IdType();
        idType_2.setValue("MA201504");
        idType_2.setSchemeId("EXT_MARK");
        idList.add(idType_2);

        IdType idType_3 = new IdType();
        idType_3.setValue("FW20150");
        idType_3.setSchemeId("IRCS");
        idList.add(idType_3);

        IdType idType_ = new IdType();
        idType_.setValue("FRA201504172");
        idType_.setSchemeId("CFR");
        idList.add(idType_);

        IdType idType_4 = new IdType();
        idType_4.setValue(UUID.randomUUID().toString());
        idType_4.setSchemeId("UUID");
        idList.add(idType_4);

        IdType idType_5 = new IdType();
        idType_5.setValue(UUID.randomUUID().toString().toUpperCase());
        idType_5.setSchemeId("UUID");
        idList.add(idType_5);

        switch(addTo){
            case 0 :
                idType_.setValue(idType_.getValue() + strToAddToFail);
            case 1 :
                idType_1.setValue(idType_1.getValue() + strToAddToFail);
            case 2 :
                idType_2.setValue(idType_2.getValue() + strToAddToFail);
            case 3 :
                idType_3.setValue(idType_3.getValue() + strToAddToFail);
            case 4 :
                idType_4.setValue(idType_4.getValue() + strToAddToFail);
            case 5 :
                idType_5.setValue(idType_5.getValue() + strToAddToFail);
        }

        final boolean trueFase = abstrFact.validateFormat(idList);

        if(fail){
            assertTrue(trueFase);
            System.out.print("\n(It should fail) Result failed : " + trueFase);
        } else {
            assertFalse(trueFase);
            System.out.print("\n(It shouldn't fail) Result didn't fail : " + !trueFase);
        }

    }

}