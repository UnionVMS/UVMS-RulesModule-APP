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
package eu.europa.ec.fisheries.uvms.rules.service.business;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class RulesUtil {
    final static String FORMAT = "yyyy-MM-dd HH:mm:ss Z";

    public static Date stringToDate(String dateString) throws IllegalArgumentException {
        if (dateString != null) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(FORMAT).withOffsetParsed();
            DateTime dateTime = formatter.withZoneUTC().parseDateTime(dateString);
            GregorianCalendar cal = dateTime.toGregorianCalendar();
            return cal.getTime();
        } else {
            return null;
        }
    }

    public static String dateToString(Date date) {
        String dateString = null;
        if (date != null) {
            DateFormat df = new SimpleDateFormat(FORMAT);
            dateString = df.format(date);
        }
        return dateString;
    }

    public static String xmlGregorianToString(XMLGregorianCalendar timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            return dateToString(timestamp.toGregorianCalendar().getTime());
        }
    }

    public static String gregorianToString(GregorianCalendar timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            return dateToString(timestamp.getTime());
        }
    }

    public static XMLGregorianCalendar dateToXmlGregorian(Date timestamp) throws DatatypeConfigurationException {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(timestamp);
        XMLGregorianCalendar xmlCalendar = null;
        xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        return xmlCalendar;
    }

}