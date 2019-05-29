/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.mdr;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import eu.europa.ec.fisheries.uvms.rules.service.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.MDRCacheService;
import eu.europa.ec.fisheries.uvms.rules.service.business.FormatExpression;
import eu.europa.ec.fisheries.uvms.rules.service.business.RuleFromMDR;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.helper.ObjectRepresentationHelper;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProcess;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Stateless
@LocalBean
@Slf4j
public class MDRCacheServiceBean implements MDRCacheService, MDRCacheRuleService {

    private static final String TERRITORY = "TERRITORY";
    private static final String MANAGEMENT_AREA = "MANAGEMENT_AREA";
    private static final String FISH_PRESENTATION = "FISH_PRESENTATION";
    private static final String FISH_PRESERVATION = "FISH_PRESERVATION";
    private static final String XEU = "XEU";
    private static final String CODE_COLUMN_NAME = "code";
    private static final String PLACES_CODE_COLUMN = "placesCode";

    @EJB
    private MDRCache cache;

    @EJB
    private RulesCache rulesCache;

    public void loadMDRCache(boolean isFromReport) {
        cache.loadAllMdrCodeLists(isFromReport);
    }

    /**
     * Check if value passed is present in the MDR list speified
     *
     * @param listName  - MDR list name to be ckecked against
     * @param codeValue - This value will be checked in MDR list
     * @return True-> if value is present in MDR list   False-> if value is not present in MDR list
     */
    @Override
    public boolean isPresentInMDRList(String listName, String codeValue, DateTime validityDate) {
        if (validityDate == null) {
            return isPresentInMDRList(listName, codeValue);
        }
        MDRAcronymType mdrAcronymType = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if (mdrAcronymType == null) {
            return false;
        }
        List<String> values = getValues(mdrAcronymType, validityDate);
        return CollectionUtils.isNotEmpty(values) && values.contains(codeValue);
    }

    private boolean isPresentInMDRList(String listName, String codeValue) {
        MDRAcronymType mdrAcronymType = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if (mdrAcronymType == null) {
            return false;
        }
        List<String> values = getValues(mdrAcronymType);
        return CollectionUtils.isNotEmpty(values) && values.contains(codeValue);
    }

    /**
     * This function checks that all the CodeType values passed to the function exist in MDR code list defined by its listId
     *
     * @param valuesToMatch - CodeType list--Values from each instance will be checked agaist ListName
     * @return true -> if all values are found in MDR list specified. false -> if even one value is not matching with MDR list
     */
    @Override
    public boolean isCodeTypePresentInMDRList(List<CodeType> valuesToMatch, DateTime validityDate) {
        if (validityDate == null) {
            return true;
        }
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
            List<String> codeListValues = getValues(anEnum, validityDate);
            if (!codeListValues.contains(codeType.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isCodeTypePresentInMDRList(CodeType valueToMatch, DateTime validityDate) {
        return isCodeTypePresentInMDRList(Collections.singletonList(valueToMatch), validityDate);
    }

    private List<String> getValues(MDRAcronymType anEnum, DateTime date) {
        List<ObjectRepresentation> entry = cache.getEntry(anEnum);
        return getList(entry, date);
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
    @Override
    public boolean isIdTypePresentInMDRList(String listName, List<IdType> valuesToMatch, DateTime validityDate) {
        if (validityDate == null) {
            return true;
        }
        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if (anEnum == null) {
            return false;
        }
        List<String> codeListValues = getValues(anEnum, validityDate);
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
    @Override
    public boolean isCodeTypePresentInMDRList(String listName, List<CodeType> valuesToMatch, DateTime validityDate) {
        if (validityDate == null) {
            return true;
        }
        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if (anEnum == null) {
            return false;
        }
        List<String> codeListValues = getValues(anEnum, validityDate);
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

    @Override
    public boolean isCodeTypeListIdPresentInMDRList(String listName, List<CodeType> valuesToMatch, DateTime validityDate) {
        if (validityDate == null) {
            return true;
        }
        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if (anEnum == null) {
            return false;
        }
        List<String> codeListValues = getValues(anEnum, validityDate);
        if (CollectionUtils.isEmpty(valuesToMatch) || CollectionUtils.isEmpty(codeListValues)) {
            return false;
        }
        for (CodeType codeType : valuesToMatch) {
            if (!codeListValues.contains(codeType.getListId()))
                return false;
        }
        return true;
    }

    @Override
    public boolean isIdTypePresentInMDRList(List<IdType> ids, DateTime validityDate) {
        if (validityDate == null) {
            return true;
        }
        if (CollectionUtils.isEmpty(ids)) {
            return false;
        }
        for (IdType idType : ids) {
            if (!isIdTypePresentInMDRList(idType, validityDate)) {
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
    @Override
    public boolean isIdTypePresentInMDRList(IdType id, DateTime creationDateOfMessage) {
        if (creationDateOfMessage == null) {
            return true;
        }
        if (id == null) {
            return false;
        }
        String schemeId = id.getSchemeId();
        String value = id.getValue();
        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, schemeId);
        if (anEnum == null) {
            return false;
        }
        List<String> codeListValues = getValues(anEnum, creationDateOfMessage);
        return codeListValues.contains(value);
    }

    @Override
    public boolean isAllSchemeIdsPresentInMDRList(String listName, List<IdType> idTypes, DateTime creationDateOfMessage) {
        if (creationDateOfMessage == null) {
            return true;
        }
        if (StringUtils.isBlank(listName) || CollectionUtils.isEmpty(idTypes)) {
            return false;
        }
        for (IdType idType : idTypes) {
            if (!isSchemeIdPresentInMDRList(listName, idType, creationDateOfMessage)) {
                return false;
            }
        }
        return true;
    }

    /**
     * NOTE : You end up in this method only if CF is not provided!
     * <p>
     * The fields that are used on this method pertinent to rule FA-L03-00-0175 are as follows :
     * <p>
     * 1. Country (coming from specifiedFLUXLocations list)
     * 2. Fish Presentation (coming from appliedAAPProcessTypeCodes - schemeId==FISH_PRESENTATION)
     * 3. Fish Preservation (coming from appliedAAPProcessTypeCodes - schemeId==FISH_PRESERVATION)
     * 4. Species Code (coming from speciesCode)
     * 5. Flag State (coming from farepDocSpecVesselTrpmRegVesselCountryId)
     * <p>
     * The list to be filtered by all the above mentioned fields : CONVERSION_FACTOR
     * <p>
     * CodeLists used in the method : MEMBER_STATE, CONVERSION_FACTOR, MEMBER_STATE, MANAGEMENT_AREA
     *
     * @param specifiedFLUXLocations
     * @param appliedAAPProcessTypeCodes
     * @param speciesCode
     * @param validityDate
     * @return
     */
    @Override
    public boolean combinationExistsInConversionFactorList(List<FLUXLocation> specifiedFLUXLocations, List<CodeType> appliedAAPProcessTypeCodes, CodeType speciesCode, DateTime validityDate, IdType farepDocSpecVesselTrpmRegVesselCountryId) {
        Iterables.removeIf(specifiedFLUXLocations, Objects::isNull);
        Iterables.removeIf(appliedAAPProcessTypeCodes, Objects::isNull);
        String territory;
        String rfmo;
        String speciesCodeVal = speciesCode != null ? speciesCode.getValue() : StringUtils.EMPTY;
        String presentation = StringUtils.EMPTY;
        String preservationState = StringUtils.EMPTY;
        String flagState = farepDocSpecVesselTrpmRegVesselCountryId != null ? farepDocSpecVesselTrpmRegVesselCountryId.getValue() : StringUtils.EMPTY;
        String flagStateBis = XEU;

        Map<String, String> terrManagMap = retrieveCountryFromLocationList(specifiedFLUXLocations, validityDate);
        if(MapUtils.isEmpty(terrManagMap)){
            return false; // Case 1 : Country cannot be determined.
        }
        territory = terrManagMap.get("territory");
        rfmo = terrManagMap.get("managarea");

        // Determine FISH_PRESENTATION and FISH_PRESERVATION
        if (CollectionUtils.isNotEmpty(appliedAAPProcessTypeCodes)) {
            for (CodeType presPreserv : appliedAAPProcessTypeCodes) {
                if (FISH_PRESENTATION.equals(presPreserv.getListId())) {
                    presentation = presPreserv.getValue();
                }
                if (FISH_PRESERVATION.equals(presPreserv.getListId())) {
                    preservationState = presPreserv.getValue();
                }
            }
        }

        // Pre-filtering (species, presentation, preservation) - this is common to all scenarios.
        List<ObjectRepresentation> preFilteredList;
        if (!(StringUtils.isBlank(speciesCodeVal) || StringUtils.isBlank(presentation) || StringUtils.isBlank(preservationState))) {
            List<ObjectRepresentation> conversionFactorList = cache.getEntry(MDRAcronymType.CONVERSION_FACTOR);
            List<ObjectRepresentation> filtered_1_list = filterEntriesByColumn(conversionFactorList, CODE_COLUMN_NAME, speciesCodeVal);
            List<ObjectRepresentation> filtered_2_list = filterEntriesByColumn(filtered_1_list, "presentation", presentation);
            preFilteredList = filterEntriesByColumn(filtered_2_list, "state", preservationState);
        } else {
            return false; // Case : some of the filters are empty!
        }

        if(CollectionUtils.isEmpty(preFilteredList)){
            return false;
        }

        return (!StringUtils.isEmpty(territory) && CollectionUtils.isNotEmpty(filterEntriesByColumn(preFilteredList, PLACES_CODE_COLUMN, territory)))      // Case : "CF RFMO/SFPA/3rd party" exists
                || (!StringUtils.isEmpty(rfmo) && CollectionUtils.isNotEmpty(filterEntriesByColumn(preFilteredList, PLACES_CODE_COLUMN, rfmo)))
                || CollectionUtils.isNotEmpty(filterEntriesByColumn(preFilteredList, PLACES_CODE_COLUMN, flagStateBis))                                    // Case : "CF FLAG STATE" exists
                || (!StringUtils.isEmpty(flagState) && CollectionUtils.isNotEmpty(filterEntriesByColumn(preFilteredList, PLACES_CODE_COLUMN, flagState))); // Case : "CF EU" exists
    }


    @Override
    public boolean combinationExistsInConversionFactorListAndIsGreaterOrEqualToOne(List<FLUXLocation> specifiedFLUXLocations, List<AAPProcess> appliedAAPProceses, CodeType speciesCode, DateTime validityDate, IdType farepDocSpecVesselTrpmRegVesselCountryId) {
        Iterables.removeIf(specifiedFLUXLocations, Objects::isNull);
        Iterables.removeIf(appliedAAPProceses, Objects::isNull);
        String speciesCodeVal = speciesCode != null ? speciesCode.getValue() : StringUtils.EMPTY;
        AtomicReference<String> presentation = new AtomicReference<>(StringUtils.EMPTY);
        AtomicReference<String> preservationState = new AtomicReference<>(StringUtils.EMPTY);
        String territory;
        String rfmo;
        String flagState = farepDocSpecVesselTrpmRegVesselCountryId.getValue();
        String flagStateBis = XEU;

        Map<String, String> terrManagMap = retrieveCountryFromLocationList(specifiedFLUXLocations, validityDate);
        if(terrManagMap == null){
            return false; // Case 1 : Country cannot be determined.
        }
        territory = terrManagMap.get("territory");
        rfmo = terrManagMap.get("managarea");

        // Determine FISH_PRESENTATION and FISH_PRESERVATION
        AtomicReference<Boolean> greaterEqualsToOneCFExists = new AtomicReference<>(false);
        List<ObjectRepresentation> conversionFactorList = cache.getEntry(MDRAcronymType.CONVERSION_FACTOR);
        if (CollectionUtils.isNotEmpty(appliedAAPProceses)) {
            final String FACTOR_COLUMN = "factor";
            final BigInteger one = new BigInteger("1");
            appliedAAPProceses.forEach(aapProcess -> {
                List<un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType> aapProcTypeCodes = aapProcess.getTypeCodes();
                if (CollectionUtils.isNotEmpty(aapProcTypeCodes)) {
                    aapProcTypeCodes.forEach(presPreserv -> {
                        String listID = presPreserv.getListID();
                        if (FISH_PRESENTATION.equals(listID)) {
                            presentation.set(presPreserv.getValue());
                        } else if (FISH_PRESERVATION.equals(listID)) {
                            preservationState.set(presPreserv.getValue());
                        }
                    });
                }
                // Pre-filtering (species,presentation,preservation) - this is common to all scenarios.
                List<ObjectRepresentation> preFilteredList;
                if (!(StringUtils.isBlank(speciesCodeVal) || StringUtils.isBlank(presentation.get()) || StringUtils.isBlank(preservationState.get()))) {
                    // Pre-filtering by : speciesCode, presentation, preservation
                    List<ObjectRepresentation> filtered_1_list = filterEntriesByColumn(conversionFactorList, CODE_COLUMN_NAME, speciesCodeVal);
                    List<ObjectRepresentation> filtered_2_list = filterEntriesByColumn(filtered_1_list, "presentation", presentation.get());
                    preFilteredList = filterEntriesByColumn(filtered_2_list, "state", preservationState.get());
                    if (!StringUtils.isEmpty(territory)) { // Case : "CF RFMO/SFPA/3rd party" exists
                        List<ObjectRepresentation> filteredOr = filterEntriesByColumn(preFilteredList, PLACES_CODE_COLUMN, territory);
                        findMatchAndSetFlagIfExistsWantedValue(greaterEqualsToOneCFExists, FACTOR_COLUMN, one, filteredOr);
                    }
                    if (!StringUtils.isEmpty(rfmo)) { // Case : "CF RFMO/SFPA/3rd party" exists
                        List<ObjectRepresentation> filteredOr = filterEntriesByColumn(preFilteredList, PLACES_CODE_COLUMN, rfmo);
                        findMatchAndSetFlagIfExistsWantedValue(greaterEqualsToOneCFExists, FACTOR_COLUMN, one, filteredOr);
                    }
                    if (!greaterEqualsToOneCFExists.get()) { // Case : "CF FLAG STATE" exists
                        List<ObjectRepresentation> filteredOr = filterEntriesByColumn(preFilteredList, PLACES_CODE_COLUMN, flagStateBis);
                        findMatchAndSetFlagIfExistsWantedValue(greaterEqualsToOneCFExists, FACTOR_COLUMN, one, filteredOr);
                    }
                    if (!greaterEqualsToOneCFExists.get() && !StringUtils.isEmpty(flagState)) {  // Case : "CF EU" exists
                        List<ObjectRepresentation> filteredOr = filterEntriesByColumn(preFilteredList, PLACES_CODE_COLUMN, flagState);
                        findMatchAndSetFlagIfExistsWantedValue(greaterEqualsToOneCFExists, FACTOR_COLUMN, one, filteredOr);
                    }
                }
            });
        }
        return greaterEqualsToOneCFExists.get();
    }

   /*   * fail == return null

     1. If no MANAGEMENT_AREA and no TERRITORY is provided => fail
     2. If more then one MANAGEMENT_AREA or TERRITORY are provided => fail
     3. If MANAGEMENT_AREA and TERRITORY are provided than MANAGEMENT_AREA takes precedence
         3.1 If MANAGEMENT_AREA doesn't exist (in MDR) than take TERRITORY
    */
    private Map<String, String> retrieveCountryFromLocationList(List<FLUXLocation> fluxLocations, DateTime validityDate){
        Map<String, String> contryManagAreaMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(fluxLocations)) {  // Country is the location ID (first one that has value in the schemdID="TERRITORY" or  schemdID="MANAGEMENT_AREA")
            List<IDType> allLocationIds = new ArrayList<>();
            for (FLUXLocation location : fluxLocations) {
                if (location != null && location.getID() != null) {
                    allLocationIds.add(location.getID());
                }
            }
            if (CollectionUtils.isEmpty(allLocationIds)) {
                return null; // Case 1
            }
            IDType managArea = null;
            IDType territory = null;
            if (moreThenOneTerritoryOrManagementAreas(allLocationIds)) { // Case 2
                return null;
            } else {
                for (IDType locId : allLocationIds) {
                    String locIdSchemeId = locId.getSchemeID();
                    if (TERRITORY.equals(locIdSchemeId)) {
                        territory = locId;
                    } else if (MANAGEMENT_AREA.equals(locIdSchemeId)) {
                        managArea = locId;
                    }
                }
            }
            String managementAreaRFMOCode = (managArea == null) ? null : retrieveValueForSpecificColumnOfCodeListFilterByCodeValue(MANAGEMENT_AREA, managArea.getValue(), validityDate, PLACES_CODE_COLUMN);
            if (StringUtils.isNotEmpty(managementAreaRFMOCode)) {
                contryManagAreaMap.put("managarea",managementAreaRFMOCode);
            }
            if (territory != null && StringUtils.isNotEmpty(territory.getValue())) {
                contryManagAreaMap.put("territory",territory.getValue());
            }
            if(contryManagAreaMap.isEmpty()){
                return null; // Case 1 : country filter cannot be determined (not provided in the XML)
            }
        }
        return contryManagAreaMap;
    }

    private void findMatchAndSetFlagIfExistsWantedValue(AtomicReference<Boolean> greaterEqualsToOneCFExists, String FACTOR_COLUMN, BigInteger one, List<ObjectRepresentation> filteredOr) {
        if (CollectionUtils.isNotEmpty(filteredOr)) {
            filteredOr.forEach(objectRepresentation -> {
                objectRepresentation.getFields().forEach(columnDataType -> {
                    String columnName = columnDataType.getColumnName();
                    String columnValue = columnDataType.getColumnValue();
                    if (FACTOR_COLUMN.equals(columnName) && StringUtils.isNotBlank(columnValue) && (one.compareTo(new BigInteger(columnValue)) <= 0)) {
                        greaterEqualsToOneCFExists.set(true);
                    }
                });
            });
        }
    }

    private boolean moreThenOneTerritoryOrManagementAreas(List<IDType> allLocationIds) {
        int nrOfTerritories = 0;
        int nrOfManagementAreas = 0;
        for (IDType locId : allLocationIds) {
            String locIdSchemeId = locId.getSchemeID();
            if (TERRITORY.equals(locIdSchemeId)) {
                nrOfTerritories++;
            } else if (MANAGEMENT_AREA.equals(locIdSchemeId)) {
                nrOfManagementAreas++;
            }
        }
        return nrOfTerritories > 1 || nrOfManagementAreas > 1;
    }

    private List<ObjectRepresentation> filterEntriesByColumn(List<ObjectRepresentation> entries, String columnName, String columnValue) {
        if (CollectionUtils.isEmpty(entries) || StringUtils.isEmpty(columnValue) || StringUtils.isEmpty(columnName)) {
            return entries;
        }
        List<ObjectRepresentation> matchingList = new ArrayList<>();
        for (ObjectRepresentation entry : entries) {
            for (ColumnDataType field : entry.getFields()) {
                if (field.getColumnName().equals(columnName) && field.getColumnValue().equals(columnValue)) {
                    matchingList.add(entry);
                }
            }
        }
        return matchingList;
    }

    private String retrieveValueForSpecificColumnOfCodeListFilterByCodeValue(String listName, String codeValue, DateTime validityDate, String columnNameValueOfWhichToBeReturned) {
        if (validityDate == null) {
            return null;
        }
        MDRAcronymType mdrAcronymType = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if (mdrAcronymType == null) {
            return null;
        }
        List<ObjectRepresentation> rows = cache.getEntry(mdrAcronymType);
        List<ObjectRepresentation> filteredByCodeFieldValue = rows.stream()
                .filter(row -> {
                    AtomicBoolean found = new AtomicBoolean(false);
                    row.getFields().forEach(field -> {
                        if (CODE_COLUMN_NAME.equals(field.getColumnName()) && codeValue.equals(field.getColumnValue())) { // Record with that 'codeValue' was found
                            found.set(true);
                        }
                    });
                    return found.get();
                }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(filteredByCodeFieldValue)) {
            List<ColumnDataType> foundColumn = filteredByCodeFieldValue.get(0).getFields().stream().filter(field -> columnNameValueOfWhichToBeReturned.equals(field.getColumnName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(foundColumn)) {
                return foundColumn.get(0).getColumnValue();
            }
        }
        return null;
    }

    /**
     * This method gets value from DataType Column of MDR list for the matching record. Record will be matched with CODE column
     *
     * @param listName  MDR list name to be matched with
     * @param codeValue value for CODE column to be matched with
     * @return DATATYPE column value for the matching record
     */
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
                    if (CODE_COLUMN_NAME.equals(columnDataType.getColumnName()) && columnDataType.getColumnValue().equals(codeValue)) {
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

    @Override
    public boolean isTypeCodeValuePresentInList(String listName, CodeType typeCode, DateTime creationDateOfMessage) {
        return isTypeCodeValuePresentInList(listName, Collections.singletonList(typeCode), creationDateOfMessage);
    }

    @Override
    public boolean isTypeCodeValuePresentInList(String listName, List<CodeType> typeCodes, DateTime creationDateOfMessage) {
        String typeCodeValue = getValueForListId(listName, typeCodes);
        return typeCodeValue != null && isPresentInMDRList(listName, typeCodeValue, creationDateOfMessage);
    }

    private boolean isSchemeIdPresentInMDRList(String listName, IdType idType, DateTime creationDateOfMessage) {
        if (creationDateOfMessage == null) {
            return true;
        }
        return idType != null && !StringUtils.isBlank(idType.getSchemeId()) && isPresentInMDRList(listName, idType.getSchemeId(), creationDateOfMessage);
    }

    private List<String> getList(List<ObjectRepresentation> entry, DateTime date) {
        List<String> codeColumnValues = new ArrayList<>();
        if (CollectionUtils.isEmpty(entry) || date == null) {
            return Collections.emptyList();
        }
        for (ObjectRepresentation representation : entry) {
            List<ColumnDataType> columnDataTypes = representation.getFields();
            if (CollectionUtils.isEmpty(columnDataTypes)) {
                continue;
            }
            Optional<String> code = ObjectRepresentationHelper.getValueOfColumn(CODE_COLUMN_NAME, representation);
            Optional<String> startDate = ObjectRepresentationHelper.getValueOfColumn("startDate", representation);
            Optional<String> endDate = ObjectRepresentationHelper.getValueOfColumn("endDate", representation);
            if (code.isPresent() && startDate.isPresent() && endDate.isPresent()
                    && date.isAfter(ObjectRepresentationHelper.parseDate(startDate.get()))
                    && date.isBefore(ObjectRepresentationHelper.parseDate(endDate.get()))) {
                codeColumnValues.add(code.get());
            }
        }
        return codeColumnValues;
    }

    private List<String> getList(List<ObjectRepresentation> entry) {
        List<String> codeColumnValues = new ArrayList<>();
        if (CollectionUtils.isEmpty(entry)) {
            return Collections.emptyList();
        }
        for (ObjectRepresentation representation : entry) {
            List<ColumnDataType> columnDataTypes = representation.getFields();
            if (CollectionUtils.isEmpty(columnDataTypes)) {
                continue;
            }
            Optional<String> code = ObjectRepresentationHelper.getValueOfColumn(CODE_COLUMN_NAME, representation);
            if (code.isPresent()) {
                codeColumnValues.add(code.get());
            }
        }
        return codeColumnValues;
    }

    private String getValueForListId(String listId, List<CodeType> typeCodes) {
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
    public boolean isNotMostPreciseFAOArea(IdType id, DateTime creationDateOfMessage) {
        List<ObjectRepresentation> faoAreas = cache.getEntry(MDRAcronymType.FAO_AREA);
        return !ObjectRepresentationHelper
                .doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn(id.getValue(), "terminalInd", "1", faoAreas, creationDateOfMessage);
    }


    // ################################ START : FORMAT VALIDATION #################################################

    /**
     * Validate the format of the value depending on the schemeId for List<IdType>
     */
    @Override
    public boolean validateFormat(List<IdType> ids, DateTime creationDateOfMessage) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        for (IdType id : ids) {
            if (validateFormat(id, creationDateOfMessage)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateSchemeIdFormat(List<IdType> ids, String schemeID, DateTime creationDateOfMessage) {
        if (CollectionUtils.isEmpty(ids) || StringUtils.isEmpty(schemeID)) {
            return true;
        }
        for (IdType id : ids) {
            if (schemeID.equals(id.getSchemeId()) && validateFormat(id, creationDateOfMessage)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Validate the format of the value depending on the schemeId for single IdType
     *
     * @param id IdType
     * @return
     */
    @Override
    public boolean validateFormat(IdType id, DateTime creationDateOfMessage) {
        boolean isInvalid = false;
        if (id == null || id.getSchemeId() == null) {
            return false;
        }
        try {
            String schemeId = id.getSchemeId();
            FormatExpression formatExpression = cache.getFormatsByIdentifier().get(id.getSchemeId());
            if(formatExpression == null){
                return false;
            }
            if ("UUID".equalsIgnoreCase(schemeId) && "00000000-0000-0000-0000-000000000000".equals(id.getValue()) || !validateFormatFromExprObject(id.getValue(), formatExpression, creationDateOfMessage)) {
                isInvalid = true;
            }
        } catch (IllegalArgumentException ex) {
            log.debug("The SchemeId : '" + id.getSchemeId() + "' is not mapped in the AbstractFact.validateFormat(List<IdType> ids) method.", ex.getMessage());
            isInvalid = false;
        }
        return isInvalid;
    }

    /**
     * Validate the format of the value depending on the codeType for single CodeType
     *
     * @param codeType CodeType
     * @return true if format is invalid, return false if format is valid
     */
    @Override
    public boolean validateFormat(CodeType codeType, DateTime creationDateOfMessage) {
        boolean isInvalid = false;
        if (codeType == null) {
            return isInvalid;
        }
        try {
            if (!validateFormatFromExprObject(codeType.getValue(), cache.getFormatsByIdentifier().get(codeType.getListId()), creationDateOfMessage)) {
                isInvalid = true;
            }
        } catch (IllegalArgumentException ex) {
            log.debug("The codeType : '" + codeType.getListId() + "' is not mapped in the AbstractFact.validateFormat(List<CodeType> codeTypes) method.", ex.getMessage());
            isInvalid = false;
        }
        return isInvalid;
    }

    private boolean validateFormatFromExprObject(String value, FormatExpression formatExpression, DateTime creationDateOfMessage) {
        return formatExpression != null &&
                !StringUtils.isEmpty(value) &&
                !StringUtils.isEmpty(formatExpression.getExpression()) &&
                value.matches(formatExpression.getExpression()) &&
                isValidDate(creationDateOfMessage, formatExpression.getStartDate(), formatExpression.getEndDate());
    }

    private boolean isValidDate(DateTime creationDateOfMessage, Date startDate, Date endDate) {
        if (creationDateOfMessage == null || startDate == null || endDate == null) {
            return true;
        }
        Date date = creationDateOfMessage.toDate();
        return date.after(startDate) && date.before(endDate);
    }

    // ################################ END : FORMAT VALIDATION #################################################

    @Override
    public boolean isLocationNotInCountry(IdType id, IdType countryID, DateTime creationDateOfMessage) {
        List<ObjectRepresentation> locations = cache.getEntry(MDRAcronymType.LOCATION);
        return !ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn(
                id.getValue(), PLACES_CODE_COLUMN, countryID.getValue(), locations, creationDateOfMessage);
    }


    @Override
    public List<ObjectRepresentation> getObjectRepresentationList(MDRAcronymType mdrAcronym) {
        return cache.getEntry(mdrAcronym);
    }


    @Override
    public List<RuleFromMDR> getFaBrsForBrId(String brId) {
        return cache.geFaBRsByBrId(brId);
    }

    @Override
    public RuleFromMDR getFaBrForBrIdAndContext(String brId, String context) {
        return cache.getFaBrForBrIdAndContext(brId, context);
    }

    @Override
    public List<RuleFromMDR> getFaBrListForBrIdAndContext(String brId, String context) {
        return cache.getFaBrListForBrIdAndContext(brId, context);
    }

    @Override
    public RuleFromMDR getFaBrForBrIdAndDataFlow(String brId, String dataFlow, Date msgDate) {
        String context = findContextForDf(dataFlow);
        return findMdrFaBrForBrIdAndContextAndDate(brId, context, msgDate);
    }

    @Override
    public String getErrorMessageForBrIAndDFAndValidity(String brid, String context, Date msgDate) {
        RuleFromMDR ruleDromMDR = findMdrFaBrForBrIdAndContextAndDate(brid, context, msgDate);
        return ruleDromMDR != null ? ruleDromMDR.getMessage() : StringUtils.EMPTY;
    }

    @Override
    public String getErrorTypeStrForForBrIAndDFAndValidity(String brid, String context, Date msgDate) {
        RuleFromMDR ruleDromMDR = findMdrFaBrForBrIdAndContextAndDate( brid, context, msgDate);
        return ruleDromMDR != null ? ruleDromMDR.getType() : StringUtils.EMPTY;
    }

    private RuleFromMDR findMdrFaBrForBrIdAndContextAndDate(String brId, String contextForDf, Date msgDate) {
        RuleFromMDR mdrRule = null;
        if(msgDate == null){ // Compatibility with old data that didn't have the dates set.
            mdrRule = cache.getFaBrForBrIdAndContext(brId, contextForDf);
        } else {
            List<RuleFromMDR> faBrListForBrIdAndContext = getFaBrListForBrIdAndContext(brId, contextForDf);
            if(CollectionUtils.isNotEmpty(faBrListForBrIdAndContext)){
                for (RuleFromMDR ruleFromMDR : faBrListForBrIdAndContext) {
                    if(isDateInRange(msgDate, ruleFromMDR.getStartDate(), ruleFromMDR.getEndDate())){
                        mdrRule = ruleFromMDR;
                        break;
                    }
                }
            }
        }
        return mdrRule;
    }

    @Override
    public List<String> getDataFlowListForBRId(String brId) {
        List<RuleFromMDR> ruleFromMDRS = cache.geFaBRsByBrId(brId);
        return CollectionUtils.isNotEmpty(ruleFromMDRS) ? ruleFromMDRS.stream().map(RuleFromMDR::getContext).collect(Collectors.toList()) : null;
    }

    @Override
    public boolean codeListExistsInMdr(String listName) {
        return cache.getAllCodeLists().stream().anyMatch(listName::equals);
    }

    @Override
    public Map<String, List<String>> getRulesContexts() {
        return rulesCache.getRulesContexts();
    }

    @Override
    public boolean doesRuleExistInRulesTable(String brId, String context) {
        List<String> rulesContexts = rulesCache.getRulesContexts().get(brId);
        return rulesContexts.contains(context);
    }

    @Override
    public String findContextForDf(String dataFlow) {
        if(StringUtils.isEmpty(dataFlow)){
            return null;
        }
        List<String> contexts = cache.getDataFlowContexts().get(dataFlow);
        return CollectionUtils.isNotEmpty(contexts) ? contexts.get(0) : null;
    }

    private boolean isDateInRange(Date testDate, Date startDate, Date endDate) {
        if (testDate == null || startDate == null || endDate == null) {
            return true;
        }
        return testDate.after(startDate) && testDate.before(endDate);
    }

}
