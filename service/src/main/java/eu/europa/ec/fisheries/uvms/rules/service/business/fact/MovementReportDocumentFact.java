/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/

package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.FLUXPartyType;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._18.VesselCountryType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.CodeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._18.IDType;

@Data
public class MovementReportDocumentFact extends AbstractFact {

    private boolean ok = true;
    private Date creationDateTime;
    private String creationDateTimeString;
    private List<IDType> ids;
    private CodeType purposeCode;
    private FLUXPartyType ownerFLUXParty;
    private VesselCountryType registrationVesselCountry;
    private IDType registrationVesselCountryIdType;
    private DateTime dateTime;
    private List<String> existingIds;

    public boolean hasDuplicateId(List<IDType> ids, List<String> existingIds) {
        return ids.stream().map(IDType::getValue).anyMatch(existingIds::contains);
    }
    
    public boolean containsTypesOfIdXTimes(List<IDType> ids,String schemaType,int count){

        int counter = 0;
        for(IDType idType:ids){

            if(schemaType.equals(idType.getSchemeID())){
                counter ++;
            }
        }

        return counter == count;
    }
    
    public boolean hasValidCreationDateTime(String creationDateTimeString) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseStrict()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                .optionalStart()
                .appendFraction(ChronoField.MICRO_OF_SECOND, 1, 6, true)
                .optionalEnd()
                .appendLiteral('Z')//timezone must always be utc, thus the literal Z
                .parseStrict().toFormatter();
        try {
            formatter.parse(creationDateTimeString);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public boolean isDateInThePast(Date creationDateTime){

        Calendar serverDate = Calendar.getInstance();
        long t= serverDate.getTimeInMillis();
        Date startDate = new Date(t - 10 * 60000);
        new Date();

        return creationDateTime.after(startDate) && creationDateTime.before(serverDate.getTime());
    }

    public boolean hasValidPurposeCodeListId(CodeType purposeCode, String defaultValue) {
        return defaultValue.equals(purposeCode.getListID());
    }
    
    public boolean hasValidPurposeCodeValue(CodeType purposeCode, String defaultValue) {
        return defaultValue.equals(purposeCode.getValue());
    }

    public boolean hasValidSchemeId(String schemeId, String expectedValue) {
        return schemeId != null && !schemeId.isEmpty() && expectedValue.equals(schemeId);
    }

    public boolean hasValidIdType(IDType idType) {
        return idType != null && idType.getValue() != null && !idType.getValue().isEmpty();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.MOVEMENT_REPORT_DOCUMENT;
    }
}
