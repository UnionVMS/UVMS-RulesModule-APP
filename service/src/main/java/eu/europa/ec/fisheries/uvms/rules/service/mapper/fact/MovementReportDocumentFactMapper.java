/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/

package eu.europa.ec.fisheries.uvms.rules.service.mapper.fact;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MovementReportDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathStringWrapper;
import lombok.extern.slf4j.Slf4j;
import un.unece.uncefact.data.standard.fluxvesselpositionmessage._4.FLUXVesselPositionMessage;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.DateTimeType;

import java.util.Date;

import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.CREATION_DATE_TIME;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.FLUX_REPORT_DOCUMENT;
import static eu.europa.ec.fisheries.uvms.rules.service.constants.XPathConstants.MOVEMENT_REPORT_DOCUMENT;

@Slf4j
public class MovementReportDocumentFactMapper {

    private XPathStringWrapper xPathUtil;

    public MovementReportDocumentFactMapper() {
        xPathUtil = new XPathStringWrapper();
    }
    public MovementReportDocumentFactMapper(XPathStringWrapper strUtil1) {
        this.xPathUtil = strUtil1;
    }

    public AbstractFact generateFactForMovementReportDocument(FLUXVesselPositionMessage vesselPositionMessage){

        if(vesselPositionMessage == null){
            return null;
        }

        MovementReportDocumentFact fact = new MovementReportDocumentFact();
        String partialXpath = xPathUtil.append(MOVEMENT_REPORT_DOCUMENT).getValue();

        DateTimeType creationDateTime = vesselPositionMessage.getFLUXReportDocument().getCreationDateTime();
        fact.setCreationDateTimeString(dateTimeAsString(creationDateTime));
        fact.setCreationDateTime(getDate(creationDateTime));
        xPathUtil.appendWithoutWrapping(partialXpath).append(FLUX_REPORT_DOCUMENT, XPathConstants.CREATION_DATE_TIME).storeInRepo(fact, CREATION_DATE_TIME);

        return fact;
    }

    public static MovementReportDocumentFact mapToMovementReportDocumentFact(FLUXVesselPositionMessage vesselPositionMessage){

        if(vesselPositionMessage == null || vesselPositionMessage.getFLUXReportDocument() == null){
            throw new IllegalArgumentException("FLUXVesselPositionMessage and FLUXReportDocument cannot be null");
        }

        MovementReportDocumentFact fact = new MovementReportDocumentFact();
        DateTimeType creationDateTime = vesselPositionMessage.getFLUXReportDocument().getCreationDateTime();
        fact.setCreationDateTime(getDate(creationDateTime));
        fact.setCreationDateTimeString(dateTimeAsString(creationDateTime));
        return fact;
    }

    private static String dateTimeAsString(DateTimeType dateTimeType) {
        String dateAsString = null;

        if (dateTimeType != null) {
            try {
                if (dateTimeType.getDateTime() != null) {
                    dateAsString = dateTimeType.getDateTime().toString();
                }
            } catch (Exception e) {
                log.debug("Error while trying to parse dateTimeType", e);
            }
        }
        return dateAsString;
    }

    private static Date getDate(DateTimeType dateTimeType) {
        Date date = null;
        if (dateTimeType != null) {
            try {
                if (dateTimeType.getDateTime() != null) {
                    date = dateTimeType.getDateTime().toGregorianCalendar().getTime();
                }
            } catch (Exception e) {
                log.debug("Error while trying to parse dateTimeType", e);
            }
        }

        return date;
    }



}
