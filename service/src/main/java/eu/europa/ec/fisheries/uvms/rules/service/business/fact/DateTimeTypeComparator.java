/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.Comparator;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

public class DateTimeTypeComparator implements Comparator<DateTimeType> {

    @Override
    public int compare(DateTimeType o1, DateTimeType o2) {
        DateTime dateTime1 = new DateTime(o1.getDateTime().getYear(), o1.getDateTime().getMonth(), o1.getDateTime().getDay(), 0, 0);
        DateTime dateTime2 = new DateTime(o2.getDateTime().getYear(), o2.getDateTime().getMonth(), o2.getDateTime().getDay(), 0, 0);
        if (dateTime1.isAfter(dateTime2)) {
            return 1;
        }
        return -1;
    }
}
