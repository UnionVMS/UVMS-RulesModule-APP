/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import static junit.framework.Assert.assertNotNull;

import eu.europa.ec.fisheries.uvms.rules.service.business.MDRCacheHolder;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Created by padhyad on 6/7/2017.
 */
public class MdrCacheServiceBeanTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    MDRCacheServiceBean mdrCacheServiceBean;

    @Mock
    MDRCache cache;

    @Test
    @Ignore
    public void testLoadMDRCache() {
        Mockito.doReturn(RuleTestHelper.getObjectRepresentationForGEAR_TYPE_CODES()).when(cache).getEntry(Mockito.any(MDRAcronymType.class));
        mdrCacheServiceBean.loadMDRCache();
        assertNotNull(MDRCacheHolder.getInstance().getList(MDRAcronymType.GEAR_TYPE));
    }
}
