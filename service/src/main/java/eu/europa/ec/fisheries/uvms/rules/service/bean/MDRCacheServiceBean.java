/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.ejb.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.helper.ObjectRepresentationHelper;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

@Singleton
@Slf4j
public class MDRCacheServiceBean implements MDRCacheService, MDRCacheRuleService {

    @EJB
    private MDRCache cache;

    private Map<String, String> errorMessages;

    @AccessTimeout(value = 180, unit = SECONDS)
    @Lock(LockType.WRITE)
    public void loadMDRCache() {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            List<Callable<List<ObjectRepresentation>>> myCallableList = new ArrayList<>();
            for (final MDRAcronymType type : MDRAcronymType.values()){
                if ((type != MDRAcronymType.FA_BR_DEF)
                        && (type != MDRAcronymType.SALE_BR_DEF)) {
                    myCallableList.add(new Callable<List<ObjectRepresentation>>() {
                        @Override
                        public List<ObjectRepresentation> call() {
                            return cache.getEntry(type);
                        }
                    });
                }
            }
            executorService.invokeAll(myCallableList);
            executorService.shutdown();

            // Blocks until all tasks have completed execution after a shutdown request, or the timeout occurs,
            // or the current thread is interrupted, whichever happens first.
            executorService.awaitTermination(180, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        log.debug(cache.getCache().stats().toString());
        log.info("MDRCache size: " + cache.getCache().asMap().size());
    }

    @Override
    @AccessTimeout(value = 180, unit = SECONDS)
    @Lock(LockType.READ)
    public String getErrorMessageForBrId(String brId){
        return errorMessages.get(brId);
    }

    /**
     * This function mapps all the error messages to the ones defined in MDR;
     *
     */
    @AccessTimeout(value = 180, unit = SECONDS)
    @Lock(LockType.READ)
    public void loadCacheForFailureMessages() {
        if (!MapUtils.isEmpty(errorMessages)) {
            return;
        }
        errorMessages = new HashMap<>();
        final List<ObjectRepresentation> objRapprList = new ArrayList<ObjectRepresentation>(){{
            addAll(cache.getEntry(MDRAcronymType.FA_BR_DEF));
            addAll(cache.getEntry(MDRAcronymType.SALE_BR_DEF));
        }};
        final String MESSAGE_COLUMN = "messageIfFailing";
        final String BR_ID_COLUMN = "code";
        for (ObjectRepresentation objectRepr : objRapprList) {
            String brId = null;
            String errorMessage = null;
            for (ColumnDataType field : objectRepr.getFields()) {
                final String columnName = field.getColumnName();
                if(MESSAGE_COLUMN.equals(columnName)){
                    errorMessage = field.getColumnValue();
                }
                if(BR_ID_COLUMN.equals(columnName)){
                    brId = field.getColumnValue();
                }
            }
            errorMessages.put(brId,errorMessage);
        }
    }


    /**
     * Check if value passed is present in the MDR list speified
     *
     * @param listName  - MDR list name to be ckecked against
     * @param codeValue - This value will be checked in MDR list
     * @return True-> if value is present in MDR list   False-> if value is not present in MDR list
     */
    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isPresentInMDRList(String listName, String codeValue) {
        MDRAcronymType mdrAcronymType = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if (mdrAcronymType == null) {
            return false;
        }
        List<String> values = getValues(mdrAcronymType);
        return CollectionUtils.isNotEmpty(values) && values.contains(codeValue);
    }

    private List<String> getList(List<ObjectRepresentation> entry) {
        List<String> codeColumnValues = new ArrayList<>();
        if (CollectionUtils.isEmpty(entry))
            return Collections.emptyList();
        for (ObjectRepresentation representation : entry) {
            List<ColumnDataType> columnDataTypes = representation.getFields();
            if (CollectionUtils.isEmpty(columnDataTypes)) {
                continue;
            }
            for (ColumnDataType nameVal : columnDataTypes) {
                if ("code".equals(nameVal.getColumnName())) {
                    codeColumnValues.add(nameVal.getColumnValue());
                }
            }
        }
        return codeColumnValues;
    }

    /**
     * This function checks that all the CodeType values passed to the function exist in MDR code list defined by its listId
     *
     * @param valuesToMatch - CodeType list--Values from each instance will be checked agaist ListName
     * @return true -> if all values are found in MDR list specified. false -> if even one value is not matching with MDR list
     */
    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isCodeTypePresentInMDRList(List<CodeType> valuesToMatch) {
        if (CollectionUtils.isEmpty(valuesToMatch)) {
            return false;
        }
        for (CodeType codeType : valuesToMatch) {
            if (codeType == null || codeType.getValue() == null || codeType.getListId() == null) {
                return false;
            }
            MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, codeType.getListId());
            if (anEnum == null) {
                return false;
            }
            List<String> codeListValues = getValues(anEnum);
            if (!codeListValues.contains(codeType.getValue())) {
                return false;
            }
        }
        return true;
    }

    private List<String> getValues(MDRAcronymType anEnum) {
        List<ObjectRepresentation> entry = cache.getEntry(anEnum);
        return getList(entry);
    }

    /**
     * This function checks that all the IdType values passed to the function exist in MDR code list or not
     *
     * @param listName      - Values passed would be checked agaist this MDR list
     * @param valuesToMatch - IdType list--Values from each instance will be checked agaist ListName
     * @return True -> if all values are found in MDR list specified. False -> If even one value is not matching with MDR list
     */
    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isIdTypePresentInMDRList(String listName, List<IdType> valuesToMatch) {
        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if (anEnum == null) {
            return false;
        }
        List<String> codeListValues = getValues(anEnum);
        if (CollectionUtils.isEmpty(valuesToMatch) || CollectionUtils.isEmpty(codeListValues)) {
            return false;
        }
        for (IdType codeType : valuesToMatch) {
            if (!codeListValues.contains(codeType.getValue()))
                return false;
        }
        return true;
    }

    /**
     * This function checks that all the CodeType values passed to the function exist in MDR code list or not
     *
     * @param listName      - Values passed would be checked agaist this MDR list
     * @param valuesToMatch - CodeType list--Values from each instance will be checked agaist ListName
     * @return True -> if all values are found in MDR list specified. False -> If even one value is not matching with MDR list
     */
    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isCodeTypePresentInMDRList(String listName, List<CodeType> valuesToMatch) {
        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if (anEnum == null) {
            return false;
        }
        List<String> codeListValues = getValues(anEnum);
        if (CollectionUtils.isEmpty(valuesToMatch) || CollectionUtils.isEmpty(codeListValues)) {
            return false;
        }
        for (CodeType codeType : valuesToMatch) {
            if (!codeListValues.contains(codeType.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isCodeTypeListIdPresentInMDRList(String listName, List<CodeType> valuesToMatch) {
        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if (anEnum == null) {
            return false;
        }
        List<String> codeListValues = getValues(anEnum);
        if (CollectionUtils.isEmpty(valuesToMatch) || CollectionUtils.isEmpty(codeListValues)) {
            return false;
        }
        for (CodeType codeType : valuesToMatch) {
            if (!codeListValues.contains(codeType.getListId()))
                return false;
        }
        return true;
    }

    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isIdTypePresentInMDRList(List<IdType> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        for (IdType idType : ids) {
            if (!isIdTypePresentInMDRList(idType)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This function checks that the IdType exist in MDR code list or not.
     * The MDR list is defined by the property schemeId from the IdType
     *
     * @param id - IdType that will be checked against ListName
     * @return true when it exists
     */
    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isIdTypePresentInMDRList(IdType id) {
        if (id == null) {
            return false;
        }
        String schemeId = id.getSchemeId();
        String value = id.getValue();
        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, schemeId);
        if (anEnum == null) {
            return false;
        }
        List<String> codeListValues = getValues(anEnum);
        return codeListValues.contains(value);
    }

    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isAllSchemeIdsPresentInMDRList(String listName, List<IdType> idTypes) {
        if (StringUtils.isBlank(listName) || isEmpty(idTypes)) {
            return false;
        }
        for (IdType idType : idTypes) {
            if (!isSchemeIdPresentInMDRList(listName, idType)) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean combinationExistsInConversionFactorList(List<FLUXLocation> specifiedFLUXLocations, List<CodeType> appliedAAPProcessTypeCodes, CodeType speciesCode) {
        // clean lists from nulls
        Iterables.removeIf(specifiedFLUXLocations, Predicates.isNull());
        Iterables.removeIf(appliedAAPProcessTypeCodes, Predicates.isNull());
        // country column
        String country = StringUtils.EMPTY;
        if(CollectionUtils.isNotEmpty(specifiedFLUXLocations)){
            for(FLUXLocation location : specifiedFLUXLocations){
                final IDType locId = location.getID();
                if(locId != null && ("TERRITORY".equals(locId.getSchemeID()) || "MANAGEMENT_AREA".equals(locId.getSchemeID()))){
                    country = locId.getValue();
                }
            }
        }
        if(isPresentInMDRList("MEMBER_STATE", country)){
            country = "XEU";
        }
        // presentation, state columns
        String presentation = StringUtils.EMPTY;
        String state = StringUtils.EMPTY;
        if(CollectionUtils.isNotEmpty(appliedAAPProcessTypeCodes)){
            for(CodeType presPreserv : appliedAAPProcessTypeCodes){
                if("FISH_PRESENTATION".equals(presPreserv.getListId())){
                    presentation = presPreserv.getValue();
                }
                if("FISH_PRESERVATION".equals(presPreserv.getListId())){
                    state = presPreserv.getValue();
                }
            }
        }
        List<ObjectRepresentation> finalList = null;
        if(!(StringUtils.isBlank(country) || StringUtils.isBlank(presentation) || StringUtils.isBlank(state))){
            List<ObjectRepresentation> entry = cache.getEntry(MDRAcronymType.CONVERSION_FACTOR);
            List<ObjectRepresentation> filtered_1_list = filterEntriesByColumn(entry, "placesCode", country);
            List<ObjectRepresentation> filtered_2_list = filterEntriesByColumn(filtered_1_list, "species", speciesCode != null ? speciesCode.getValue() : StringUtils.EMPTY);
            List<ObjectRepresentation> filtered_3_list = filterEntriesByColumn(filtered_2_list, "presentation", presentation);
            finalList = filterEntriesByColumn(filtered_3_list, "state", state);
        }
        return CollectionUtils.isNotEmpty(finalList);
    }

    private List<ObjectRepresentation> filterEntriesByColumn(List<ObjectRepresentation> entries, String columnName, String columnValue){
        if(CollectionUtils.isEmpty(entries) || StringUtils.isEmpty(columnValue) || StringUtils.isEmpty(columnName)){
            return entries;
        }
        List<ObjectRepresentation> matchingList = new ArrayList<>();
        for(ObjectRepresentation entry : entries){
            for(ColumnDataType field : entry.getFields()){
                if(field.getColumnName().equals(columnName) && field.getColumnValue().equals(columnValue)){
                    matchingList.add(entry);
                }
            }
        }
        return matchingList;
    }

    /**
     * This method gets value from DataType Column of MDR list for the matching record. Record will be matched with CODE column
     *
     * @param listName  MDR list name to be matched with
     * @param codeValue value for CODE column to be matched with
     * @return DATATYPE column value for the matching record
     */
    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public String getDataTypeForMDRList(String listName, String codeValue) {
        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if (anEnum == null || codeValue == null) {
            return StringUtils.EMPTY;
        }
        List<ObjectRepresentation> representations = cache.getEntry(anEnum);
        boolean valueFound = false;
        if (CollectionUtils.isNotEmpty(representations)) {
            for (ObjectRepresentation representation : representations) {

                List<ColumnDataType> columnDataTypes = representation.getFields();
                if (CollectionUtils.isEmpty(columnDataTypes)) {
                    continue;
                }
                for (ColumnDataType columnDataType : columnDataTypes) {
                    if ("code".equals(columnDataType.getColumnName()) && columnDataType.getColumnValue().equals(codeValue)) {
                        valueFound = true;
                        break;
                    }
                }
                if (valueFound) {
                    for (ColumnDataType columnDataType : columnDataTypes) {
                        if ("dataType".equals(columnDataType.getColumnName())) {
                            return columnDataType.getColumnValue();
                        }
                    }
                }
            }
        }
        return StringUtils.EMPTY;
    }

    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isSchemeIdPresentInMDRList(String listName, IdType idType) {
        return idType != null && !StringUtils.isBlank(idType.getSchemeId()) && isPresentInMDRList(listName, idType.getSchemeId());
    }

    private boolean isEmpty(List<?> list) {
        return CollectionUtils.isEmpty(list);
    }

    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isTypeCodeValuePresentInList(String listName, CodeType typeCode) {
        return isTypeCodeValuePresentInList(listName, Collections.singletonList(typeCode));
    }

    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isTypeCodeValuePresentInList(String listName, List<CodeType> typeCodes) {
        String typeCodeValue = getValueForListId(listName, typeCodes);
        return typeCodeValue != null && isPresentInMDRList(listName, typeCodeValue);
    }

    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public String getValueForListId(String listId, List<CodeType> typeCodes) {
        if (StringUtils.isBlank(listId) || CollectionUtils.isEmpty(typeCodes)) {
            return null;
        }
        for (CodeType typeCode : typeCodes) {
            String typeCodeListId = typeCode.getListId();

            if (StringUtils.isNotBlank(typeCodeListId) && typeCodeListId.equals(listId)) {
                return typeCode.getValue();
            }
        }
        return null;
    }


    @Override
    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isNotMostPreciseFAOArea(IdType id) {
        List<ObjectRepresentation> faoAreas = cache.getEntry(MDRAcronymType.FAO_AREA);
        return !ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
                (id.getValue(), "terminalInd", "1", faoAreas);
    }


    @Override
    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public boolean isLocationNotInCountry(IdType id, IdType countryID) {
        List<ObjectRepresentation> locations = cache.getEntry(MDRAcronymType.LOCATION);
        return !ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn(
                id.getValue(), "placesCode", countryID.getValue(), locations);
    }

    @Override
    @Lock(LockType.READ)
    @AccessTimeout(value = 180, unit = SECONDS)
    public List<ObjectRepresentation> getObjectRepresentationList(MDRAcronymType mdrAcronym) {
        return cache.getEntry(mdrAcronym);
    }


}
