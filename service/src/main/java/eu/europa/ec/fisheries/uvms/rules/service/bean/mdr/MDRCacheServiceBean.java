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
import eu.europa.ec.fisheries.uvms.rules.service.business.EnrichedBRMessage;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.helper.ObjectRepresentationHelper;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Stateless
@LocalBean
@Slf4j
public class MDRCacheServiceBean implements MDRCacheService, MDRCacheRuleService {

    private static final String TERRITORY = "TERRITORY";
    private static final String MANAGEMENT_AREA = "MANAGEMENT_AREA";
    private static final String FISH_PRESENTATION = "FISH_PRESENTATION";
    private static final String FISH_PRESERVATION = "FISH_PRESERVATION";
    private static final String XEU = "XEU";

    @EJB
    private MDRCache cache;

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
            return true;
        }
        MDRAcronymType mdrAcronymType = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if (mdrAcronymType == null) {
            return false;
        }
        List<String> values = getValues(mdrAcronymType, validityDate);
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
    public boolean combinationExistsInConversionFactorList(List<FLUXLocation> specifiedFLUXLocations, List<CodeType> appliedAAPProcessTypeCodes,
                                                           CodeType speciesCode, DateTime validityDate, IdType farepDocSpecVesselTrpmRegVesselCountryId) {
        Iterables.removeIf(specifiedFLUXLocations, Objects::isNull);
        Iterables.removeIf(appliedAAPProcessTypeCodes, Objects::isNull);

        String speciesCodeVal = speciesCode != null ? speciesCode.getValue() : StringUtils.EMPTY;
        String presentation = StringUtils.EMPTY;
        String preservationState = StringUtils.EMPTY;
        String country = StringUtils.EMPTY;
        String flagState = farepDocSpecVesselTrpmRegVesselCountryId.getValue();
        String flagStateBis = XEU;

        // 1. If no MANAGEMENT_AREA and no TERRITORY is provided => fail
        // 2. If more then one MANAGEMENT_AREA or TERRITORY are provided => fail
        // 3. If MANAGEMENT_AREA and TERRITORY are provided than MANAGEMENT_AREA takes precedence
        //   3.1 If MANAGEMENT_AREA doesn't exist (in MDR) than take TERRITORY
        //   3.2 If also TERRITORY doesn't exist (in MDR) => fail
        //   3.3 If TERRITORY has been chosen :
        //       3.3.1 If value XEU is provided => fail
        //       3.3.2 If value is in MEMBER_STATE list => fail
        //       3.3.3 If value is not in MEMBER_STATE list => OK
        if (CollectionUtils.isNotEmpty(specifiedFLUXLocations)) {  // Country is the location ID (first one that has value in the schemdID="TERRITORY" or  schemdID="MANAGEMENT_AREA")
            List<IDType> allLocationIds = new ArrayList<>();
            for (FLUXLocation location : specifiedFLUXLocations) {
                if (location != null && location.getID() != null) {
                    allLocationIds.add(location.getID());
                }
            }
            if (CollectionUtils.isEmpty(allLocationIds)) {
                return false; // Case 1
            }
            IDType managArea = null;
            IDType territory = null;
            if (moreThenOneTerritoryOrManagementAreas(allLocationIds)) { // Case 2
                return false;
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
            boolean valueExistsInMDR = false;
            if (managArea != null && isPresentInMDRList(MANAGEMENT_AREA, managArea.getValue(), validityDate)) {
                String value = managArea.getValue();
                country = value.substring(0, value.indexOf("_"));
                valueExistsInMDR = true;
            } else if (territory != null && isPresentInMDRList(TERRITORY, territory.getValue(), validityDate)) {
                String value = territory.getValue();
                if (XEU.equals(value) || isPresentInMDRList("MEMBER_STATE", value, validityDate)) {
                    return false; // Case 3.3.1 + 3.3.2
                }
                country = value;
                valueExistsInMDR = true;
            }
            if (!valueExistsInMDR) {
                return false; // Case 3.1 + 3.2
            }
        } else { // Case 1
            return false;
        }

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

        // Pre-filtering (species,presentation,preservation) - this is common to all scenarios.
        List<ObjectRepresentation> preFilteredList;
        if (!(StringUtils.isBlank(speciesCodeVal) || StringUtils.isBlank(presentation) || StringUtils.isBlank(preservationState))) {
            List<ObjectRepresentation> conversionFactorList = cache.getEntry(MDRAcronymType.CONVERSION_FACTOR);
            List<ObjectRepresentation> filtered_1_list = filterEntriesByColumn(conversionFactorList, "code", speciesCode != null ? speciesCode.getValue() : StringUtils.EMPTY);
            List<ObjectRepresentation> filtered_2_list = filterEntriesByColumn(filtered_1_list, "presentation", presentation);
            preFilteredList = filterEntriesByColumn(filtered_2_list, "state", preservationState);
        } else {
            return false; // Case : some of the filters are empty
        }

        if (!StringUtils.isEmpty(country) && CollectionUtils.isNotEmpty(filterEntriesByColumn(preFilteredList, "placesCode", country))) {
            return true; // Case : "CF RFMO/SFPA/3rd party" exists
        } else if (CollectionUtils.isNotEmpty(filterEntriesByColumn(preFilteredList, "placesCode", flagStateBis))) {
            return true; // Case : "CF FLAG STATE" exists
        } else {
            return !StringUtils.isEmpty(flagState) && CollectionUtils.isNotEmpty(filterEntriesByColumn(preFilteredList, "placesCode", flagState)); // Case : "CF EU" exists
        }
    }


    public boolean combinationExistsInConversionFactorListAndIsGreaterOrEqualToOne(List<FLUXLocation> specifiedFLUXLocations, List<AAPProcess> appliedAAPProceses,
                                                                                   CodeType speciesCode, DateTime validityDate, IdType farepDocSpecVesselTrpmRegVesselCountryId) {
        Iterables.removeIf(specifiedFLUXLocations, Objects::isNull);
        Iterables.removeIf(appliedAAPProceses, Objects::isNull);

        String speciesCodeVal = speciesCode != null ? speciesCode.getValue() : StringUtils.EMPTY;
        AtomicReference<String> presentation = new AtomicReference<>(StringUtils.EMPTY);
        AtomicReference<String> preservationState = new AtomicReference<>(StringUtils.EMPTY);
        String country = StringUtils.EMPTY;
        String flagState = farepDocSpecVesselTrpmRegVesselCountryId.getValue();
        String flagStateBis = XEU;

        // 1. If no MANAGEMENT_AREA and no TERRITORY is provided => fail
        // 2. If more then one MANAGEMENT_AREA or TERRITORY are provided => fail
        // 3. If MANAGEMENT_AREA and TERRITORY are provided than MANAGEMENT_AREA takes precedence
        //   3.1 If MANAGEMENT_AREA doesn't exist (in MDR) than take TERRITORY
        //   3.2 If also TERRITORY doesn't exist (in MDR) => fail
        //   3.3 If TERRITORY has been chosen :
        //       3.3.1 If value XEU is provided => fail
        //       3.3.2 If value is in MEMBER_STATE list => fail
        //       3.3.3 If value is not in MEMBER_STATE list => OK
        if (CollectionUtils.isNotEmpty(specifiedFLUXLocations)) {  // Country is the location ID (first one that has value in the schemdID="TERRITORY" or  schemdID="MANAGEMENT_AREA")
            List<IDType> allLocationIds = new ArrayList<>();
            for (FLUXLocation location : specifiedFLUXLocations) {
                if (location != null && location.getID() != null) {
                    allLocationIds.add(location.getID());
                }
            }
            if (CollectionUtils.isEmpty(allLocationIds)) {
                return false; // Case 1
            }
            IDType managArea = null;
            IDType territory = null;
            if (moreThenOneTerritoryOrManagementAreas(allLocationIds)) { // Case 2
                return false;
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
            boolean valueExistsInMDR = false;
            if (managArea != null && isPresentInMDRList(MANAGEMENT_AREA, managArea.getValue(), validityDate)) {
                String value = managArea.getValue();
                country = value.substring(0, value.indexOf("_"));
                valueExistsInMDR = true;
            } else if (territory != null && isPresentInMDRList(TERRITORY, territory.getValue(), validityDate)) {
                String value = territory.getValue();
                if (XEU.equals(value) || isPresentInMDRList("MEMBER_STATE", value, validityDate)) {
                    return false; // Case 3.3.1 + 3.3.2
                }
                country = value;
                valueExistsInMDR = true;
            }
            if (!valueExistsInMDR) {
                return false; // Case 3.1 + 3.2
            }
        } else { // Case 1
            return false;
        }

        // Determine FISH_PRESENTATION and FISH_PRESERVATION
        AtomicReference<Boolean> greaterEqualsToOneCFExists = new AtomicReference<>(false);
        List<ObjectRepresentation> conversionFactorList = cache.getEntry(MDRAcronymType.CONVERSION_FACTOR);
        if (CollectionUtils.isNotEmpty(appliedAAPProceses)) {
            final String FACTOR_COLUMN = "factor";
            final BigInteger one = new BigInteger("1");
            String finalCountry = country;
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
                    List<ObjectRepresentation> filtered_1_list = filterEntriesByColumn(conversionFactorList, "code", speciesCode != null ? speciesCode.getValue() : StringUtils.EMPTY);
                    List<ObjectRepresentation> filtered_2_list = filterEntriesByColumn(filtered_1_list, "presentation", presentation.get());
                    preFilteredList = filterEntriesByColumn(filtered_2_list, "state", preservationState.get());
                    if (!StringUtils.isEmpty(finalCountry)) { // Case : "CF RFMO/SFPA/3rd party" exists
                        List<ObjectRepresentation> filteredOr = filterEntriesByColumn(preFilteredList, "placesCode", finalCountry);
                        findMatchAndSetFlagIfExistsWantedValue(greaterEqualsToOneCFExists, FACTOR_COLUMN, one, filteredOr);
                    }
                    if (!greaterEqualsToOneCFExists.get()) { // Case : "CF FLAG STATE" exists
                        List<ObjectRepresentation> filteredOr = filterEntriesByColumn(preFilteredList, "placesCode", flagStateBis);
                        findMatchAndSetFlagIfExistsWantedValue(greaterEqualsToOneCFExists, FACTOR_COLUMN, one, filteredOr);
                    }
                    if(!greaterEqualsToOneCFExists.get() && !StringUtils.isEmpty(flagState)) {  // Case : "CF EU" exists
                        List<ObjectRepresentation> filteredOr = filterEntriesByColumn(preFilteredList, "placesCode", flagState);
                        findMatchAndSetFlagIfExistsWantedValue(greaterEqualsToOneCFExists, FACTOR_COLUMN, one, filteredOr);
                    }
                }
            });
        }
        return greaterEqualsToOneCFExists.get();
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
            Optional<String> code = ObjectRepresentationHelper.getValueOfColumn("code", representation);
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


    @Override
    public boolean isLocationNotInCountry(IdType id, IdType countryID, DateTime creationDateOfMessage) {
        List<ObjectRepresentation> locations = cache.getEntry(MDRAcronymType.LOCATION);
        return !ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn(
                id.getValue(), "placesCode", countryID.getValue(), locations, creationDateOfMessage);
    }


    @Override
    public List<ObjectRepresentation> getObjectRepresentationList(MDRAcronymType mdrAcronym) {
        return cache.getEntry(mdrAcronym);
    }


    @Override
    public EnrichedBRMessage getErrorMessageForBrId(String brId) {
        return cache.getErrorMessage(brId);
    }


    @Override
    public String getErrorMessageStrForBrId(String brid) {
        EnrichedBRMessage errorMessage = cache.getErrorMessage(brid);
        return errorMessage != null ? errorMessage.getMessage() : StringUtils.EMPTY;
    }

    @Override
    public String getErrorTypeStrForBrId(String brid) {
        EnrichedBRMessage errorMessage = cache.getErrorMessage(brid);
        return errorMessage != null ? errorMessage.getType() : StringUtils.EMPTY;
    }

    @Override
    public boolean codeListExistsInMdr(String listName) {
        return cache.getAllCodeLists().stream().anyMatch(listName::equals);
    }


}
