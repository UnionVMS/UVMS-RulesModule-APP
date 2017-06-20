/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.fisheries.uvms.rules.service.mapper.fact;

import static org.junit.Assert.assertNull;

import java.util.Date;

import eu.europa.ec.fisheries.uvms.rules.service.mapper.CustomMapper;
import org.junit.Before;
import org.junit.Test;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

/**
 * TODO create test
 */
public class CustomMapperTest {

    private DateTimeType.DateTimeString dateTimeString = new DateTimeType.DateTimeString();

    private DateTimeType dateTimeType = new DateTimeType();

    @Before
    public void before() {
        dateTimeString.setValue("12/12/2016");
    }

    @Test
    public void testGetDateWithEmptyFormatShouldReturnNull() {

        dateTimeString.setFormat("");
        dateTimeType.setDateTimeString(dateTimeString);
        Date date = CustomMapper.getDate(dateTimeType);
        assertNull(date);

    }

    @Test
    public void testGetDateWithFormatNullShouldReturnNull() {

        dateTimeString.setFormat(null);
        dateTimeType.setDateTimeString(dateTimeString);
        Date date = CustomMapper.getDate(dateTimeType);
        assertNull(date);

    }

    @Test
    public void testGetDateWithNullShouldReturnNull() {

        Date date = CustomMapper.getDate(null);
        assertNull(date);

    }
}
