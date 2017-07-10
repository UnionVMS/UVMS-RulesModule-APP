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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdTypeWithFlagState;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.NumericType;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactPerson;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

@Slf4j
public abstract class AbstractFact {

    protected FactType factType;

    protected List<RuleWarning> warnings;

    protected List<RuleError> errors;

    protected List<String> uniqueIds = new ArrayList<>();

    protected boolean ok = true;

    private Integer sequence = 0;


    public AbstractFact() {
        this.uniqueIds = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public abstract void setFactType();

    public void addWarningOrError(String type, String msg, String brId, String level, String propertyNames) {
        final List<String> xpathsForProps = getXpathsForProps(propertyNames);
        if (type.equalsIgnoreCase(ErrorType.ERROR.value())) {
            RuleError ruleError = new RuleError(brId, msg, level, xpathsForProps);
            errors.add(ruleError);
        } else {
            RuleWarning ruleWarning = new RuleWarning(brId, msg, level, xpathsForProps);
            warnings.add(ruleWarning);
        }
    }

    private List<String> getXpathsForProps(String propertyNames) {
        List<String> xpathsList = new ArrayList<>();
        if(StringUtils.isNotEmpty(propertyNames)){
            String propNamesTrimmed = StringUtils.deleteWhitespace(propertyNames);
            String[] propNames = propNamesTrimmed.split(",");
            for(String propName : propNames) {
                xpathsList.add(XPathRepository.INSTANCE.getMapForSequence(this.getSequence(), propName));
            }
        }
        return xpathsList;
    }


    public boolean schemeIdContainsAll(List<IdType> idTypes, String... valuesToMatch) {
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(idTypes)) {
            return true;
        }
        int valLength = valuesToMatch.length;
        int hits = 0;
        for (String val : valuesToMatch) {
            for (IdType IdType : idTypes) {
                if (IdType != null && val.equals(IdType.getSchemeId())) {
                    hits++;
                }
            }
        }
        return valLength > hits;
    }

    public boolean idListContainsValue(List<IdType> idTypes, String valueToMatch, String schemeIdToSearchFor){
        if(StringUtils.isEmpty(valueToMatch) || StringUtils.isEmpty(schemeIdToSearchFor)){
            return false;
        }
        String flagStateToMatch = StringUtils.EMPTY;
        for(IdType idType : idTypes){
            if(schemeIdToSearchFor.equals(idType.getSchemeId())){
                flagStateToMatch = idType.getValue();
            }
        }
        return StringUtils.equals(valueToMatch, flagStateToMatch);
    }

    public boolean valueContainsAll(List<IdType> idTypes, String... valuesToMatch) {
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(idTypes)) {
            return true;
        }
        int valLength = valuesToMatch.length;
        int hits = 0;
        for (String val : valuesToMatch) {
            for (IdType IdType : idTypes) {
                if (IdType != null && val.equals(IdType.getValue())) {
                    hits++;
                }
            }
        }
        return valLength > hits;
    }

    /**
     * Checks if the schemeId Contains Any then it checks if it contains all.
     * Otherwise it means that it contains none.
     *
     * @param idTypes
     * @param valuesToMatch
     * @return
     */
    public boolean schemeIdContainsAllOrNone(List<IdType> idTypes, String... valuesToMatch) {
        return !schemeIdContainsAny(idTypes, valuesToMatch) && schemeIdContainsAll(idTypes, valuesToMatch);
    }

    /**
     * Checks if one of the String... array elements exists in the idTypes list.
     *
     * @param idTypes
     * @param values
     * @return
     */
    public boolean schemeIdContainsAny(List<IdType> idTypes, String... values) {
        if (values == null || values.length == 0 || CollectionUtils.isEmpty(idTypes)) {
            return true;
        }
        for (String val : values) {
            for (IdType IdType : idTypes) {
                if (val.equals(IdType.getSchemeId())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     *
     * Checks if one of the String... array elements exists in the idTypes list.
     * Depending on checkEmptyness value it also checks (or not) if the values are empty.
     * Depending on isGivenName value it checks for GivenName or FamilyName.
     *
     * @param contactPersons
     * @param checkEmptyness
     * @return true/false
     */
    public boolean checkContactListContainsAny(List<ContactPerson> contactPersons, boolean checkEmptyness, boolean isGivenName) {
        if (CollectionUtils.isEmpty(contactPersons)) {
            return true;
        }
        for (ContactPerson contPers : contactPersons) {
            TextType givenName      = contPers.getGivenName();
            TextType familyName     = contPers.getFamilyName();
            TextType nameToConsider = isGivenName ? givenName : familyName;
            TextType alias          = contPers.getAlias();
            if(checkWithEmptyness(checkEmptyness, nameToConsider, alias) || checkWithoutEmptyness(nameToConsider, alias)){
                return true;
            }
        }
        return false;
    }

    private boolean checkWithoutEmptyness(TextType nameToConsider, TextType alias) {
        return (nameToConsider == null || nameToConsider.getValue() == null)
                && (alias == null || alias.getValue() == null);
    }

    private boolean checkWithEmptyness(boolean checkEmptyness, TextType nameToConsider, TextType alias) {
        return checkEmptyness && ((nameToConsider == null || StringUtils.isEmpty(nameToConsider.getValue()))
                && (alias == null || StringUtils.isEmpty(alias.getValue())));
    }

    public boolean checkAliasFromContactList(List<ContactPerson> contactPersons, boolean checkAliasEmptyness){
        if (CollectionUtils.isEmpty(contactPersons)) {
            return true;
        }
        for (ContactPerson contPers : contactPersons) {
            TextType givenName      = contPers.getGivenName();
            TextType familyName     = contPers.getFamilyName();
            TextType alias          = contPers.getAlias();
            if(givenName == null && familyName == null){
                if(alias == null || (checkAliasEmptyness && StringUtils.isEmpty(alias.getValue()))){
                    return true;
                }
            } else if(checkAliasEmptyness && alias != null && StringUtils.isEmpty(alias.getValue())){
                return true;
            }
        }
        return false;
    }

    /**
     * Validate the format of the value depending on the schemeId for List<IdType>
     *
     * @param ids
     * @return
     */
    public boolean validateFormat(List<IdType> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return true;
        }
        for (IdType id : ids) {
            if (validateFormat(id)) {
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
    public boolean validateFormat(IdType id) {
        if(id == null){
            return true;
        }
        try {
            if(!validateFormat(id.getValue(), FORMATS.valueOf(id.getSchemeId()).getFormatStr())){
                return true;
            }
        } catch (IllegalArgumentException ex) {
            log.error("The SchemeId : '" + id.getSchemeId() + "' is not mapped in the AbstractFact.validateFormat(List<IdType> ids) method.", ex.getMessage());
            return false;
        }
        return false;
    }

    private boolean validateFormat(String value, String format) {
        if (StringUtils.isEmpty(value) || StringUtils.isEmpty(format)) {
            return false;
        }
        return value.matches(format);
    }

    public boolean listIdContainsAll(List<CodeType> codeTypes, String... valuesToMatch) {
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        for (String val : valuesToMatch) {
            for (CodeType IdType : codeTypes) {
                if (!val.equals(IdType.getListId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if valuesToMatch strings are ALL present in list of measureTypes
     * @param measureTypes
     * @param valuesToMatch
     * @return
     */
    public boolean unitCodeContainsAll(List<MeasureType> measureTypes, String... valuesToMatch) {
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(measureTypes)) {
            return true;
        }
        int hits = 0;
        for (String val : valuesToMatch) {
            for (MeasureType measureType : measureTypes) {
                if (val.equals(measureType.getUnitCode())) {
                    hits++;
                }
            }
        }
        return valuesToMatch.length > hits;
    }

    public boolean validateDelimitedPeriod(List<DelimitedPeriod> delimitedPeriods, boolean start, boolean end) {
        if (delimitedPeriods == null || delimitedPeriods.isEmpty()) {
            return true;
        }
        for (DelimitedPeriod delimitedPeriod : delimitedPeriods) {
            if ((start && end && delimitedPeriod.getStartDateTime() == null && delimitedPeriod.getEndDateTime() == null)
                    || (start && !end && delimitedPeriod.getStartDateTime() == null)
                    || (end && !start && delimitedPeriod.getEndDateTime() == null)) {
                return true;
            }
        }
        return false;
    }

    public boolean schemeIdContainsAll(IdType idType, String... values) {
        return idType == null || schemeIdContainsAll(Collections.singletonList(idType), values);
    }

    public boolean listIdContainsAll(CodeType codeType, String... values) {
        return codeType == null || listIdContainsAll(Collections.singletonList(codeType), values);
    }

    public Date dateNow() {
        return new Date();
    }

    public Date dateNow(int hours) {
        DateTime now = new DateTime(dateNow());
        if (hours > 0) {
            now.plusHours(hours);
        } else if (hours < 0) {
            now.minusHours(hours);
        }
        return now.toDate();
    }

    public List<RuleWarning> getWarnings() {
        return warnings;
    }
    public List<RuleError> getErrors() {
        return errors;
    }
    public boolean isOk() {
        return ok;
    }
    public void setOk(boolean ok) {
        this.ok = ok;
    }
    public FactType getFactType() {
        return factType;
    }
    public List<String> getUniqueIds() {
        return uniqueIds;
    }
    public void setUniqueIds(List<String> uniqueIds) {
        this.uniqueIds = uniqueIds;
    }

    /**
     * Checks if one of the String... array elements exists in the idTypes list.
     *
     * @param codeTypes
     * @param values
     * @return false/true
     */
    public boolean listIdContainsAny(List<CodeType> codeTypes, String... values) {
        if (values == null || values.length == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        for (String val : values) {
            for (CodeType CodeTypes : codeTypes) {
                if (val.equals(CodeTypes.getListId())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean valueContainsAny(CodeType codeType, String... valuesToMatch) {
        return valueContainsAny(Arrays.asList(codeType), valuesToMatch);
    }

    public boolean valueContainsAny(List<CodeType> codeTypes, String... valuesToMatch) {
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        ImmutableList<CodeType> removeNull = ImmutableList.copyOf(Iterables.filter(codeTypes, Predicates.notNull()));
        boolean isMatchFound = false;
        for (String val : valuesToMatch) {
            for (CodeType CodeTypes : removeNull) {
                if (val.equals(CodeTypes.getValue())) {
                    isMatchFound = true;
                    break;
                }
            }
        }
        return !isMatchFound;
    }

    public int numberOfDecimals(BigDecimal value) {
        if (value == null) {
            return -1;
        }

        int i = value.subtract(value.setScale(0, RoundingMode.FLOOR)).movePointRight(value.scale()).intValue();
        return Integer.toString(i).length();
    }

    public boolean isPositive(List<MeasureType> value) {
        if (value == null) {
            return true;
        }
        for (MeasureType type : value) {
            BigDecimal val = type.getValue();
            if (val == null || BigDecimal.ZERO.compareTo(val) < 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isPositive(BigDecimal value) {
        if (value == null) {
            return true;
        }
        return value.compareTo(BigDecimal.ZERO) <= 0;
    }

    public boolean isInRange(BigDecimal value, int min, int max) {
        if (value == null) {
            return true;
        }
        return !((value.compareTo(new BigDecimal(min)) > 0) && (value.compareTo(new BigDecimal(max)) < 0));
    }

    public boolean anyValueContainsAll(List<CodeType> codeTypes, String... valuesToMatch) {
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        ImmutableList<CodeType> removeNull = ImmutableList.copyOf(Iterables.filter(codeTypes, Predicates.notNull()));
        boolean isMatchFound = false;

        outer : for (String val : valuesToMatch) {
            for (CodeType IdType : removeNull) {
                if (val.equals(IdType.getValue())) {
                    isMatchFound = true;
                    continue outer;
                }
            }
            isMatchFound = false;
        }
        return !isMatchFound;
    }

    public boolean allValueContainsMatch(List<CodeType> codeTypes, String valueToMatch) {
        if (valueToMatch == null || valueToMatch.length() == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }

        for (CodeType codeType : codeTypes) {
            if (!valueToMatch.equals(codeType.getValue())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if FaCatch list contains at least one or more SpecifiedFLUXLocations list  .
     *
     * @param  faCatches
     * @return false/true
     */
    public  boolean validateFluxLocationsForFaCatch(List<FACatch> faCatches) {
        boolean isValid = true;
        for (FACatch faCatch : faCatches) {
            List<FLUXLocation> checkList = faCatch.getSpecifiedFLUXLocations();
            if (checkList == null || checkList.isEmpty()) {
                isValid = false;
            }
        }
        return !isValid;
    }

    public boolean isEmpty(List<?> list){
        return CollectionUtils.isEmpty(list);
    }

    public boolean isNumeric(List<NumericType> list) {
        for (NumericType type : list) {
            if (type.getValue() == null) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(String str){
        return StringUtils.isEmpty(str);
    }

    public enum FORMATS {
        // TODO : ICCAT and CFR have Territory characters reppresented [a-zA-Z]{3} which is not correct, cause it is matching not existing combinations also (Like ABC
        // TODO : which is not an existing country code). This happens with ICCAT -second sequence- and CFR -first sequence-!

        UUID("[a-fA-F0-9]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"),
        EXT_MARK("[a-zA-Z0-9]{1,14}"),
        IRCS("[a-zA-Z0-9]{1,7}"),
        CFR("[a-zA-Z]{3}[a-zA-Z0-9]{9}"),
        UVI("[a-zA-Z0-9]{7}"),
        ICCAT("AT[a-zA-Z0-9]{3}[a-zA-Z0-9]{3}[a-zA-Z0-9]{5}"),
        GFCM("[a-zA-Z0-9]{1,13}"),
        EU_TRIP_ID("[a-zA-Z]{3}-TRP-[a-zA-Z0-9]{0,20}");

        String formatStr;

        FORMATS(String someFromat) {
            setFormatStr(someFromat);
        }

        public String getFormatStr() {
            return formatStr;
        }

        public void setFormatStr(String formatStr) {
            this.formatStr = formatStr;
        }
    }

    /**
     *  Check if value passed is present in the MDR list speified
     * @param listName - MDR list name to be ckecked against
     * @param codeValue - This value will be checked in MDR list
     * @return True-> if value is present in MDR list   False-> if value is not present in MDR list
     */
    public boolean isPresentInMDRList(String listName, String codeValue){
        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if(anEnum == null){
            log.error("The list ["+listName+"] doesn't exist in MDR module or in MDRAcronymType class! Check it and try again!");
            return false;
        }
        List<String> values = MDRCacheHolder.getInstance().getList(anEnum);
        if(CollectionUtils.isNotEmpty(values)){
            return values.contains(codeValue);
        }
        return false;
    }

    /**
     *  This function checks that all the CodeType values passed to the function exist in MDR code list or not
     * @param listName - Values passed would be checked agaist this MDR list
     * @param valuesToMatch - CodeType list--Values from each instance will be checked agaist ListName
     * @return True -> if all values are found in MDR list specified. False -> If even one value is not matching with MDR list
     */
     public boolean isCodeTypePresentInMDRList(String listName, List<CodeType> valuesToMatch){

        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, listName);
         if(anEnum == null){
             log.error("The list ["+listName+"] doesn't exist in MDR module or in MDRAcronymType class! Check it and try again!");
             return false;
         }
        List<String> codeListValues = MDRCacheHolder.getInstance().getList(anEnum);

        if(CollectionUtils.isEmpty(valuesToMatch) || CollectionUtils.isEmpty(codeListValues)){
            return false;
        }

        for(CodeType codeType: valuesToMatch){
            if(!codeListValues.contains(codeType.getValue()))
                return false;
        }

        return true;
    }


    /**
     *  This function checks that all the IdType values passed to the function exist in MDR code list or not
     * @param listName - Values passed would be checked agaist this MDR list
     * @param valuesToMatch - IdType list--Values from each instance will be checked agaist ListName
     * @return True -> if all values are found in MDR list specified. False -> If even one value is not matching with MDR list
     */
    public boolean isIdTypePresentInMDRList(String listName, List<IdType> valuesToMatch){

        MDRAcronymType anEnum = EnumUtils.getEnum(MDRAcronymType.class, listName);
        if(anEnum == null){
            log.error("The list ["+listName+"] doesn't exist in MDR module or in MDRAcronymType class! Check it and try again!");
            return false;
        }

        List<String> codeListValues = MDRCacheHolder.getInstance().getList(anEnum);

        if(CollectionUtils.isEmpty(valuesToMatch) || CollectionUtils.isEmpty(codeListValues)){
            return false;
        }

        for(IdType codeType: valuesToMatch){
            if(!codeListValues.contains(codeType.getValue()))
                return false;
        }

        return true;
    }

    public boolean vesselIdsMatch(List<IdType> vesselIds, IdType vesselCountryId, List<IdTypeWithFlagState> additionalObjectList){
        if(CollectionUtils.isEmpty(additionalObjectList)){
            return false;
        }
        List<IdTypeWithFlagState> listToBeMatched = new ArrayList<>();
        for(IdType idType : vesselIds){
            listToBeMatched.add(new IdTypeWithFlagState(idType.getSchemeId(), idType.getValue(), vesselCountryId.getValue()));
        }

        for(IdTypeWithFlagState elemFromListToBeMatched : listToBeMatched){
            if(!additionalObjectList.contains(elemFromListToBeMatched)){
                return false;
            }
        }

        return true;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

}
