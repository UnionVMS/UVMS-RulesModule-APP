/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.fisheries.uvms.rules.service.cache;

import eu.europa.ec.fisheries.uvms.rules.service.bean.RuleTestHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.MDRCacheHolder;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by kovian on 27/06/2017.
 */
public class MdrCaheHolderTest {

    MDRCacheHolder mdrCacheHolder;

    @Before
    public void init(){
        mdrCacheHolder = MDRCacheHolder.getInstance();
    }

    @Test
    public void testCacheFillAndGet(){

        mdrCacheHolder.addToCache(MDRAcronymType.GEAR_TYPE, RuleTestHelper.getObjectRepresentationForGEAR_TYPE_CODES());
        mdrCacheHolder.addToCache(MDRAcronymType.FA_CATCH_TYPE,RuleTestHelper.getObjectRepresentationForFA_CATCH());

        final List<String> gearTypeList = mdrCacheHolder.getList(MDRAcronymType.GEAR_TYPE);
        final List<String> faCatchType = mdrCacheHolder.getList(MDRAcronymType.FA_CATCH_TYPE);

        assertTrue(CollectionUtils.isNotEmpty(gearTypeList));
        assertTrue(CollectionUtils.isNotEmpty(faCatchType));

    }


    @Test
    public void testGetObjectRepresntationList(){

        mdrCacheHolder.addToCache(MDRAcronymType.GEAR_TYPE, RuleTestHelper.getObjectRepresentationForGEAR_TYPE_CODES());

        final List<ObjectRepresentation> gearTypeList = mdrCacheHolder.getObjectRepresntationList(MDRAcronymType.GEAR_TYPE);
        assertTrue(CollectionUtils.isNotEmpty(gearTypeList));

    }


}
