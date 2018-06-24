/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.business.generator.helper;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

public class ActivityObjectsHelper {

    private ActivityObjectsHelper(){

    }

    public static FishingActivity generateActivity(String occurrence, String activityType){
        FishingActivity departure = new FishingActivity();
        CodeType codeType = new CodeType();
        codeType.setValue(activityType);
        try {
            DateTimeType dateTimeType = new DateTimeType();
            dateTimeType.setDateTime(toXMLGregorianCalendar(occurrence));
            departure.setTypeCode(codeType);
            departure.setOccurrenceDateTime(dateTimeType);
        } catch (ParseException | DatatypeConfigurationException e){
            e.printStackTrace();
        }
        return departure;
    }

    private static XMLGregorianCalendar toXMLGregorianCalendar(String dateString) throws ParseException, DatatypeConfigurationException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        Date date = sdf.parse(dateString);
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
    }

}
