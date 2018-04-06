/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import static org.junit.Assert.assertTrue;

import eu.europa.ec.fisheries.uvms.rules.service.bean.RuleTestHelper;
import org.junit.Test;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;

/**
 * Created by sanera on 28/08/2017.
 */
public class FaFishingOperationFactTest {
    private FaFishingOperationFact  faFishingOperationFact = new FaFishingOperationFact();

    @Test
    public void testVerifyContactPartyRule(){
        un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType roleCode =RuleTestHelper.getCodeTypeUNCEFACT("PAIR_FISHING_PARTNER",null);
        ContactParty contactParty= RuleTestHelper.getContactParty(roleCode,RuleTestHelper.getStructuredAddress());
        boolean result=faFishingOperationFact.verifyContactPartyRule(RuleTestHelper.getListOfVesselTransportMeans());
        assertTrue(!result);
    }
}
