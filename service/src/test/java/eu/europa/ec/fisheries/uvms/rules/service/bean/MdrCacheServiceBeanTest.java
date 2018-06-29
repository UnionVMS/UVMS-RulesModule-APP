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

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class MdrCacheServiceBeanTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    MDRCacheServiceBean mdrCacheServiceBean;

    @Mock
    MDRCache cache;

    @Test
    public void isPresentInMDRListWhenTrue() {
        doReturn(getTestEntriesForMDRListFAO_AREA()).when(cache).getEntry(MDRAcronymType.FAO_AREA);

        assertTrue(mdrCacheServiceBean.isPresentInMDRList("FAO_AREA", "1",
                new DateTime(2018, 4, 4, 0, 0,0, 0, DateTimeZone.UTC)));
        verify(cache).getEntry(MDRAcronymType.FAO_AREA);
    }

    @Test
    public void isPresentInMDRListWhenAcronymIsNull() {
        assertFalse(mdrCacheServiceBean.isPresentInMDRList("INVALID_ACRONYM", "1",
                new DateTime(2018, 4, 4, 0, 0,0, 0, DateTimeZone.UTC)));
    }

    @Test
    public void isPresentInMDRListWhenMDRListIsNoLongerValid() {
        doReturn(getTestEntriesForMDRListFAO_AREA()).when(cache).getEntry(MDRAcronymType.FAO_AREA);

        assertFalse(mdrCacheServiceBean.isPresentInMDRList("FAO_AREA", "1",
                new DateTime(2222, 4, 4, 0, 0,0, 0, DateTimeZone.UTC)));
        verify(cache).getEntry(MDRAcronymType.FAO_AREA);
    }

    @Test
    public void isNotMostPreciseFAOAreaWhenTrue() {
        //data set
        IdType id = new IdType("1");

        //mock
        doReturn(getTestEntriesForMDRListFAO_AREA()).when(cache).getEntry(MDRAcronymType.FAO_AREA);

        //execute and verify
        assertTrue(mdrCacheServiceBean.isNotMostPreciseFAOArea(id, new DateTime(2018, 3, 4, 0, 2, 3)));
        verify(cache).getEntry(MDRAcronymType.FAO_AREA);
    }

    @Test
    public void isNotMostPreciseFAOAreaWhenFalse() {
        //data set
        IdType id = new IdType("1.1");

        //mock
        doReturn(getTestEntriesForMDRListFAO_AREA()).when(cache).getEntry(MDRAcronymType.FAO_AREA);

        //execute and verify
        assertFalse(mdrCacheServiceBean.isNotMostPreciseFAOArea(id, new DateTime(2018, 3, 4, 0, 2, 3)));
        verify(cache).getEntry(MDRAcronymType.FAO_AREA);
    }

    @Test
    public void isLocationNotInCountryWhenTrue() {
        //data set
        IdType id = new IdType("NEAMS");
        IdType country = new IdType("BEL");

        //mock
        doReturn(getTestEntriesForMDRListLOCATION()).when(cache).getEntry(MDRAcronymType.LOCATION);

        //execute and verify
        assertTrue(mdrCacheServiceBean.isLocationNotInCountry(id, country, new DateTime(2018, 3, 4, 0, 2, 3)));
        verify(cache).getEntry(MDRAcronymType.LOCATION);
    }

    @Test
    public void isLocationNotInCountryWhenFalse() {
        //data set
        IdType id = new IdType("BEOST");
        IdType country = new IdType("BEL");

        //mock
        doReturn(getTestEntriesForMDRListLOCATION()).when(cache).getEntry(MDRAcronymType.LOCATION);

        //execute and verify
        assertFalse(mdrCacheServiceBean.isLocationNotInCountry(id, country, new DateTime(2018, 3, 4, 0, 2, 3)));
        verify(cache).getEntry(MDRAcronymType.LOCATION);
    }

    private List<ObjectRepresentation> getTestEntriesForMDRListFAO_AREA() {
        //1
        ColumnDataType code1 = new ColumnDataType();
        code1.setColumnName("code");
        code1.setColumnValue("1.1");

        ColumnDataType placesCode1 = new ColumnDataType();
        placesCode1.setColumnName("terminalInd");
        placesCode1.setColumnValue("1");

        ColumnDataType startDate1 = new ColumnDataType();
        startDate1.setColumnName("startDate");
        startDate1.setColumnValue(new DateTime(2018, 1, 1, 0, 0,0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS"));

        ColumnDataType endDate1 = new ColumnDataType();
        endDate1.setColumnName("endDate");
        endDate1.setColumnValue(new DateTime(2019, 1, 1, 0, 0,0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS"));

        ObjectRepresentation objectRepresentation1 = new ObjectRepresentation();
        objectRepresentation1.setFields(Arrays.asList(code1, placesCode1, startDate1, endDate1));

        //2
        ColumnDataType code2 = new ColumnDataType();
        code2.setColumnName("code");
        code2.setColumnValue("1");

        ColumnDataType placesCode2 = new ColumnDataType();
        placesCode2.setColumnName("terminalInd");
        placesCode2.setColumnValue("0");

        ColumnDataType startDate2 = new ColumnDataType();
        startDate2.setColumnName("startDate");
        startDate2.setColumnValue(new DateTime(2018, 1, 1, 0, 0,0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS"));

        ColumnDataType endDate2 = new ColumnDataType();
        endDate2.setColumnName("endDate");
        endDate2.setColumnValue(new DateTime(2019, 1, 1, 0, 0,0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS"));

        ObjectRepresentation objectRepresentation2 = new ObjectRepresentation();
        objectRepresentation2.setFields(Arrays.asList(code2, placesCode2, startDate2, endDate2));

        return Arrays.asList(objectRepresentation1, objectRepresentation2);
    }

    private List<ObjectRepresentation> getTestEntriesForMDRListLOCATION() {
        ColumnDataType code = new ColumnDataType();
        code.setColumnName("code");
        code.setColumnValue("BEOST");

        ColumnDataType placesCode = new ColumnDataType();
        placesCode.setColumnName("placesCode");
        placesCode.setColumnValue("BEL");

        ColumnDataType startDate = new ColumnDataType();
        startDate.setColumnName("startDate");
        startDate.setColumnValue(new DateTime(2018, 1, 1, 0, 0,0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS"));

        ColumnDataType endDate = new ColumnDataType();
        endDate.setColumnName("endDate");
        endDate.setColumnValue(new DateTime(2019, 1, 1, 0, 0,0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS"));

        ObjectRepresentation objectRepresentation = new ObjectRepresentation();
        objectRepresentation.setFields(Arrays.asList(code, placesCode, startDate, endDate));

        return Arrays.asList(objectRepresentation);
    }
}
