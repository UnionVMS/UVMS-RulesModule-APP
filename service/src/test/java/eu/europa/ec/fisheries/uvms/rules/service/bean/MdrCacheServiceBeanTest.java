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

import eu.europa.ec.fisheries.uvms.rules.service.business.MDRCacheHolder;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

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
    public void isPresentInList() {
        Mockito.doReturn(Arrays.asList("abc", "def")).when(cache).getEntry(Mockito.any(MDRAcronymType.class));
        boolean isPresent = mdrCacheServiceBean.isPresentInList("abc", "abc");
        assertTrue(isPresent);
    }

    @Test
    public void testLoadMDRCache() {
        Mockito.doReturn(Arrays.asList("test1", "test2")).when(cache).getEntry(Mockito.any(MDRAcronymType.class));
        mdrCacheServiceBean.loadMDRCache();
        assertNotNull(MDRCacheHolder.getInstance().getList(MDRAcronymType.GEAR_TYPE));
    }
}
