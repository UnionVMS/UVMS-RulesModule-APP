package eu.europa.ec.fisheries.uvms.rules.model.mapper;
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

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by osdjup on 2016-09-01.
 */
public class XsdDateTimeConverter {
    public static Date unmarshal(String dateTime) {
        return DatatypeConverter.parseDate(dateTime).getTime();
    }

    public static String marshalDate(Date date) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return DatatypeConverter.printDate(calendar);
    }

    public static String marshalDateTime(Date dateTime) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(dateTime);
        return DatatypeConverter.printDateTime(calendar);
    }
}
