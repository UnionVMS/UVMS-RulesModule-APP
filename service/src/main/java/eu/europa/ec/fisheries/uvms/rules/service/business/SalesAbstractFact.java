/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.business;

import eu.europa.ec.fisheries.schema.sales.AmountType;
import eu.europa.ec.fisheries.schema.sales.DateTimeType;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Collection;

@Slf4j
@ToString
public abstract class SalesAbstractFact extends AbstractFact {
    protected Source source;

    public boolean isQuery() {
        if (source.equals(Source.QUERY)) {
            return true;
        }

        return false;
    }

    public boolean isResponse() {
        if (source.equals(Source.RESPONSE)) {
            return true;
        }

        return false;
    }

    public boolean isReport() {
        if (source.equals(Source.REPORT)) {
            return true;
        }

        return false;
    }

    public boolean nullValuesInAmountTypes(Collection<AmountType> amountTypes) {
        if (amountTypes == null || amountTypes.isEmpty()) {
            return true;
        }
        for (AmountType amountType : amountTypes) {
            if (amountType == null || amountType.getValue() == null) {
                return true;
            }
        }
        return false;
    }

    public boolean isNotUTC(DateTimeType dateTimeType) {
        return isNotUTC(dateTimeType.getDateTime());
    }

    public boolean isNotUTC(DateTime dateTime) {
        return dateTime.getZone() != DateTimeZone.UTC;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
