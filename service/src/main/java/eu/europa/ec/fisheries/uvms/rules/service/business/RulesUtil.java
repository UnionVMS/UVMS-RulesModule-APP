package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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
