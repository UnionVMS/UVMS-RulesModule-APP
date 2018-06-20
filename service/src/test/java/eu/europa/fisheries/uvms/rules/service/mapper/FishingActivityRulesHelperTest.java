/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.fisheries.uvms.rules.service.mapper;

import eu.europa.ec.fisheries.uvms.rules.entity.FADocumentID;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.FishingActivityRulesHelper;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;

import java.util.Set;

import static org.jgroups.util.Util.assertTrue;

public class FishingActivityRulesHelperTest {

    FishingActivityRulesHelper rulesHelper = new FishingActivityRulesHelper();
    @Test
    public void testWithEmptyFLUXFAReportMessage(){
        FLUXFAReportMessage fluxfaReportMessage = new FLUXFAReportMessage();
        Set<FADocumentID> ids = rulesHelper.collectReportIds(fluxfaReportMessage);
        assertTrue(CollectionUtils.isEmpty(ids));
    }

    @Test
    public void testWithNull(){
        Set<FADocumentID> ids = rulesHelper.collectReportIds(null);
        assertTrue(CollectionUtils.isEmpty(ids));
    }

}
