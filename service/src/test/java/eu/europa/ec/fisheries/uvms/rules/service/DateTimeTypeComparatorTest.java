/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.DateTimeTypeComparator;
import org.junit.Assert;
import org.junit.Test;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

public class DateTimeTypeComparatorTest {

    @Test
    public void testDateOneShouldBeBeforeDate2() throws DatatypeConfigurationException {

        XMLGregorianCalendar date1 = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(2018, 6, 28, 0, 0, 0));
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(2018, 6, 27, 0, 0, 0));
        XMLGregorianCalendar date3 = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar(2018, 6, 15, 0, 0, 0));

        DateTimeType dateType1 = new DateTimeType(date1, null);
        DateTimeType dateType2 = new DateTimeType(date2, null);
        DateTimeType dateType3 = new DateTimeType(date3, null);

        List<DateTimeType> dateTimes = Arrays.asList(dateType1, dateType2, dateType3);


        Assert.assertEquals(dateTimes.get(0).getDateTime().getDay(), 28);
        Assert.assertEquals(dateTimes.get(1).getDateTime().getDay(), 27);
        Assert.assertEquals(dateTimes.get(2).getDateTime().getDay(), 15);

        Collections.sort(dateTimes, new DateTimeTypeComparator());

        Assert.assertEquals(dateTimes.get(0).getDateTime().getDay(), 15);
        Assert.assertEquals(dateTimes.get(1).getDateTime().getDay(), 27);
        Assert.assertEquals(dateTimes.get(2).getDateTime().getDay(), 28);

    }
}
