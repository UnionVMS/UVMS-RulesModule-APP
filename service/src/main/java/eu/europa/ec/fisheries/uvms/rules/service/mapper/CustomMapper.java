/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.mapstruct.ap.internal.util.Collections;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProcess;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProduct;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactParty;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactPerson;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

   public static List<CodeType> getAppliedProcessTypeCodes(List<AAPProcess> appliedAAPProcesses) {
       if (CollectionUtils.isEmpty(appliedAAPProcesses)) {
           return java.util.Collections.emptyList();
       }
       List<CodeType> codeTypes = new ArrayList<>();

       for (AAPProcess aapProcess : appliedAAPProcesses) {
           if (CollectionUtils.isNotEmpty(aapProcess.getTypeCodes())) {
               codeTypes.addAll(ActivityFactMapper.INSTANCE.mapToCodeType(aapProcess.getTypeCodes()));
           }
       }
       return codeTypes;
   }

    public static List<AAPProduct> getAppliedProcessAAPProducts(List<AAPProcess> appliedAAPProcesses) {
        if (CollectionUtils.isEmpty(appliedAAPProcesses)) {
            return java.util.Collections.emptyList();
        }
        List<AAPProduct> aapProducts = new ArrayList<>();

        for (AAPProcess aapProcess : appliedAAPProcesses) {
            if (CollectionUtils.isNotEmpty(aapProcess.getResultAAPProducts())) {
                aapProducts.addAll(aapProcess.getResultAAPProducts());
            }
        }
        return aapProducts;
    }

    public static List<MeasureType> getAAPProductUnitQuantity(List<AAPProcess> appliedAAPProcesses) {
        if (CollectionUtils.isEmpty(appliedAAPProcesses)) {
            return java.util.Collections.emptyList();
        }
        List<MeasureType> measureTypes = new ArrayList<>();
        for (AAPProcess aapProcess : appliedAAPProcesses) {
            if(CollectionUtils.isNotEmpty(aapProcess.getResultAAPProducts())) {
                for (AAPProduct aapProduct : aapProcess.getResultAAPProducts()) {
                    if (aapProduct.getUnitQuantity() != null) {
                        measureTypes.add(ActivityFactMapper.INSTANCE.mapQuantityTypeToMeasureType(aapProduct.getUnitQuantity()));
                    }
                }
            }
        }
        return measureTypes;
    }

    public static List<MeasureType> getAAPProductWeightMeasure(List<AAPProcess> appliedAAPProcesses) {
        if (CollectionUtils.isEmpty(appliedAAPProcesses)) {
            return java.util.Collections.emptyList();
        }
        List<MeasureType> measureTypes = new ArrayList<>();
        for (AAPProcess aapProcess : appliedAAPProcesses) {
            if(CollectionUtils.isNotEmpty(aapProcess.getResultAAPProducts())) {
                for (AAPProduct aapProduct : aapProcess.getResultAAPProducts()) {
                    if (aapProduct.getWeightMeasure() != null) {
                        measureTypes.add(ActivityFactMapper.INSTANCE.mapToMeasureType(aapProduct.getWeightMeasure()));
                    }
                }
            }
        }
        return measureTypes;
    }


}
