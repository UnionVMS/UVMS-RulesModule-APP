/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.mapstruct.ap.internal.util.Collections;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactPerson;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
@Slf4j
public class CustomMapper {

    private CustomMapper(){

    }

    public static List<CodeType> mapToIdTypeList(List<ContactParty> contactPartyList) {
        List<CodeType> codeTypes = null;

        if (!CollectionUtils.isEmpty(contactPartyList)) {

            codeTypes = Collections.newArrayList();

            for(ContactParty contactParty: contactPartyList){
                codeTypes.addAll(ActivityFactMapper.INSTANCE.mapToCodeType(contactParty.getRoleCodes()));
            }
        }

        return codeTypes;
    }

    public static List<ContactPerson> mapToContactPersonList(List<ContactParty> contactPartyList) {
        List<ContactPerson> contactPersonList = null;

        if (CollectionUtils.isEmpty(contactPartyList)) {

            contactPersonList = Collections.newArrayList();

            for (ContactParty contactParty : contactPartyList) {
                contactPersonList.addAll(contactParty.getSpecifiedContactPersons());
            }
        }

        return contactPersonList;
    }

    public static Date getDate(DateTimeType dateTimeType) {
        Date date = null;

        if (dateTimeType != null) {
            try {
                if (dateTimeType.getDateTime() != null) {
                    date = dateTimeType.getDateTime().toGregorianCalendar().getTime();
                } else {
                    String format = dateTimeType.getDateTimeString().getFormat();
                    String value = dateTimeType.getDateTimeString().getValue();
                    date = new SimpleDateFormat(format).parse(value);
                }
            } catch (ParseException e) {
                log.debug("Error while trying to parse dateTimeType", e);
            }
        }

        return date;
    }

}
