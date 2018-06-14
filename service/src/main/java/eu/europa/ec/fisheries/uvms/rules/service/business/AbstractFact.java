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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesPartyFact;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.xpath.util.XPathRepository;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.ContactPerson;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.DelimitedPeriod;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FACatch;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

@Slf4j
@ToString
public abstract class AbstractFact {

    private static final String COLON = ":";
    private static volatile int counter = 0;

    protected FactType factType;

    protected String senderOrReceiver;

    protected List<RuleWarning> warnings;

    protected List<RuleError> errors;

    protected List<String> uniqueIds;

    protected boolean ok = true;

    protected DateTime creationDateOfMessage;

    private Integer sequence = 0;


    public boolean hasWarOrErr(){
        return CollectionUtils.isNotEmpty(warnings) || CollectionUtils.isNotEmpty(errors);
    }

    @Override
    protected void finalize() throws Throwable {
        counter--;
        super.finalize();
    }

    public AbstractFact() {
        counter++;
        this.uniqueIds = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public static int getNumOfInstances() {
        return counter;
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
        if (StringUtils.isNotEmpty(propertyNames)) {
            String propNamesTrimmed = StringUtils.deleteWhitespace(propertyNames);
            String[] propNames = propNamesTrimmed.split(",");
            for (String propName : propNames) {
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

    public boolean idListContainsValue(List<IdType> idTypes, String valueToMatch, String schemeIdToSearchFor) {
        if (StringUtils.isEmpty(valueToMatch) || StringUtils.isEmpty(schemeIdToSearchFor)) {
            return false;
        }
        String flagStateToMatch = StringUtils.EMPTY;
        for (IdType idType : idTypes) {
            if (schemeIdToSearchFor.equals(idType.getSchemeId())) {
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

    public boolean valueStartsWith(List<IdType> idTypes, String... valuesToMatch) {
        if (isEmpty(idTypes) || ArrayUtils.isEmpty(valuesToMatch)) {
            return false;
        }
        int hits = 0;
        for (String valueToMatch : valuesToMatch) {
            for (IdType idType : idTypes) {
                if (valueStartsWith(idType, valueToMatch)) {
                    hits++;
                }
            }
        }
        return valuesToMatch.length <= hits;
    }

    public boolean valueStartsWith(IdType idType, String valueToMatch) {
        if (valueToMatch == null || idType == null) {
            return false;
        }
        String idValue = idType.getValue();
        return idValue != null && idValue.startsWith(valueToMatch);
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
     * Checks if one of the String... array elements exists in the idType.
     *
     * @param idType
     * @param values
     * @return
     */
    public boolean schemeIdContainsAny(IdType idType, String... values) {
        return schemeIdContainsAny(Arrays.asList(idType), values);
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
        idTypes = new ArrayList<>(idTypes);
        CollectionUtils.filter(idTypes, PredicateUtils.notNullPredicate());
        for (String val : values) {
            for (IdType IdType : idTypes) {
                if (val.equals(IdType.getSchemeId())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSchemeIdPresent(IdType idType) {
        return idType == null || StringUtils.isNotBlank(idType.getSchemeId());
    }

    public boolean isAllSchemeIdsPresent(List<IdType> idTypes) {
        if (CollectionUtils.isEmpty(idTypes)) {
            return false;
        }
        idTypes = new ArrayList<>(idTypes);
        CollectionUtils.filter(idTypes, PredicateUtils.notNullPredicate());
        for (IdType idType : idTypes) {
            if (!isSchemeIdPresent(idType)) {
                return true;
            }
        }
        return false;
    }

    /**
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
            TextType givenName = contPers.getGivenName();
            TextType familyName = contPers.getFamilyName();
            TextType nameToConsider = isGivenName ? givenName : familyName;
            TextType alias = contPers.getAlias();
            if (checkWithEmptyness(checkEmptyness, nameToConsider, alias) || checkWithoutEmptyness(nameToConsider, alias)) {
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

    public boolean checkAliasFromContactList(List<ContactPerson> contactPersons, boolean checkAliasEmptyness) {
        if (CollectionUtils.isEmpty(contactPersons)) {
            return true;
        }
        for (ContactPerson contPers : contactPersons) {
            TextType givenName = contPers.getGivenName();
            TextType familyName = contPers.getFamilyName();
            TextType alias = contPers.getAlias();
            if (givenName == null || familyName == null) {
                if (alias == null || (checkAliasEmptyness && StringUtils.isEmpty(alias.getValue()))) {
                    return true;
                }
            } else if (checkAliasEmptyness && alias != null && StringUtils.isEmpty(alias.getValue())) {
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
     * Validate the format of the value depending on the schemeId for List<CodeType>
     *
     * @param codeTypes
     * @return boolean
     */
    public boolean validateFormatCodeTypes(List<CodeType> codeTypes) {
        if (CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        for (CodeType id : codeTypes) {
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
        if (id == null || id.getSchemeId() == null) {
            return true;
        }
        try {
            if (!validateFormat(id.getValue(), FORMATS.valueOf(id.getSchemeId()).getFormatStr())) {
                return true;
            }
        } catch (IllegalArgumentException ex) {
            log.debug("The SchemeId : '" + id.getSchemeId() + "' is not mapped in the AbstractFact.validateFormat(List<IdType> ids) method.", ex.getMessage());
            return true;
        }
        return false;
    }

    /**
     * Validate the format of the value depending on the codeType for single CodeType
     *
     * @param codeType CodeType
     * @return
     */
    public boolean validateFormat(CodeType codeType) {
        if (codeType == null) {
            return true;
        }
        try {
            if (!validateFormat(codeType.getValue(), FORMATS.valueOf(codeType.getListId()).getFormatStr())) {
                return true;
            }
        } catch (IllegalArgumentException ex) {
            log.debug("The codeType : '" + codeType.getListId() + "' is not mapped in the AbstractFact.validateFormat(List<CodeType> codeTypes) method.", ex.getMessage());
            return true;
        }
        return false;
    }

    /**
     * If controlList contains at leat one of the elements of the elementsToMatchList returns true;
     *
     * @param controlList
     * @param elementsToMatchList
     * @return
     */
    public boolean listContainsAtLeastOneFromTheOtherList(List<IdType> controlList, List<IdType> elementsToMatchList) {
        if (CollectionUtils.isEmpty(controlList)) {
            return false;
        }
        if (CollectionUtils.isNotEmpty(elementsToMatchList)) {
            for (IdType idToMatch : elementsToMatchList) {
                if (controlList.contains(idToMatch)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the list size is the expected one [listSize].
     *
     * @param list
     * @param listSize
     * @return
     */
    public boolean listSizeIs(List<?> list, int listSize) {
        return !(isEmpty(list) || list.size() != listSize);
    }

    /**
     * This method returns true if activityTypes list contains other elements then the ones contained in permitedElements.
     *
     * @param activityTypes
     * @return
     */
    public boolean listContainsEitherThen(List<String> activityTypes, String... permitedElements) {
        if (CollectionUtils.isEmpty(activityTypes) || permitedElements == null || permitedElements.length == 0) {
            return false;
        }
        List<String> permitedElementsList = Arrays.asList(permitedElements);
        boolean containsEitherThen = false;
        for (String type : activityTypes) {
            if (!permitedElementsList.contains(type)) {
                containsEitherThen = true;
                break;
            }
        }
        return containsEitherThen;
    }

    public boolean validateFormat(String value, String format) {
        return !StringUtils.isEmpty(value) && !StringUtils.isEmpty(format) && value.matches(format);
    }

    public boolean isIsoDateStringValidFormat(List<String> datesToCheck) {
        if (CollectionUtils.isEmpty(datesToCheck)) {
            return true;
        }
        for (String date : datesToCheck) {
            if (!isIsoDateStringValidFormat(date)) {
                return false;
            }
        }
        return true;
    }

    public boolean isIsoDateStringValidFormat(String value) {
        return !StringUtils.isBlank(value) && value.matches(FORMATS.ISO_8601_WITH_OPT_MILLIS.getFormatStr());
    }

    public boolean isIdTypeValidFormat(String requiredSchemeId, IdType idType) {
        if (idType == null || isEmpty(requiredSchemeId) || isEmpty(idType.getSchemeId()) || isEmpty(idType.getValue()) || !idType.getSchemeId().equals(requiredSchemeId)) {
            return false;
        }
        try {
            return validateFormat(idType.getValue(), FORMATS.valueOf(requiredSchemeId).getFormatStr());
        } catch (IllegalArgumentException ex) {
            log.error("The SchemeId : '" + requiredSchemeId + "' is not mapped in the AbstractFact.FORMATS enum.", ex.getMessage());
            return false;
        }
    }

    public boolean isCodeTypeValidFormat(String requiredListId, CodeType codeType) {
        if (codeType == null || isEmpty(requiredListId) || isEmpty(codeType.getListId()) || isEmpty(codeType.getValue()) || !codeType.getListId().equals(requiredListId)) {
            return false;
        }
        try {
            return validateFormat(codeType.getValue(), FORMATS.valueOf(requiredListId).getFormatStr());
        } catch (IllegalArgumentException ex) {
            log.error("The ListId : '" + requiredListId + "' is not mapped in the AbstractFact.FORMATS enum.", ex.getMessage());
            return false;
        }
    }

    public boolean listIdContainsAll(List<CodeType> codeTypes, String... valuesToMatch) {
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        codeTypes.removeAll(Collections.singleton(null));
        List<String> valueList = Arrays.asList(valuesToMatch);
        valueList.removeAll(Collections.singleton(null));

        for (String val : valueList) {
            for (CodeType IdType : codeTypes) {
                if (!val.equals(IdType.getListId())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean codeTypeValueContainsOnly(List<CodeType> codeTypes, String valueToMatch){
        if(CollectionUtils.isEmpty(codeTypes) || StringUtils.isEmpty(valueToMatch)){
            return false;
        }
        for (CodeType codeType : codeTypes) {
          if(!valueToMatch.equals(codeType.getValue())){
              return false;
          }
        }
        return true;
    }

    public boolean codeTypeValueEquals(CodeType codeType, String valueToMatch){
        return (codeType != null && StringUtils.isNotEmpty(codeType.getValue())) && (codeType.getValue().equals(valueToMatch));
    }

    public boolean idTypeValueEquals(IdType codeType, String valueToMatch){
        return (codeType != null && StringUtils.isNotEmpty(codeType.getValue())) && (codeType.getValue().equals(valueToMatch));
    }

    /**
     * Checks if valuesToMatch strings are ALL present in list of measureTypes
     *
     * @param codeType
     * @param valuesToMatch
     * @return
     */
    public boolean listIdDoesNotContainAll(CodeType codeType, String... valuesToMatch) {
        return listIdDoesNotContainAll(Collections.singletonList(codeType), valuesToMatch);
    }


    public boolean salesPartiesValueDoesNotContainAny(List<SalesPartyFact> salesPartyTypes, String... valuesToMatch) {
        List<CodeType> codeTypes = new ArrayList<>();
        HashSet<String> valuesToBeFound = new HashSet<>(Arrays.asList(valuesToMatch));
        for (SalesPartyFact salesPartyFact : salesPartyTypes) {
            codeTypes.addAll(salesPartyFact.getRoleCodes());
        }
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        for (CodeType codeType : codeTypes) {
            String value = codeType.getValue();

            if (valuesToBeFound.contains(value)) {
                return false;
            }
        }
        return true;
    }

    public boolean listIdDoesNotContainAll(List<CodeType> codeTypes, String... valuesToMatch) {
        HashSet<String> valuesFoundInListOfCodeTypes = new HashSet<>();
        HashSet<String> valuesToBeFound = new HashSet<>(Arrays.asList(valuesToMatch));
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        for (CodeType codeType : codeTypes) {
            if (codeType != null) {
                String listId = codeType.getListId();

                if (valuesToBeFound.contains(listId)) {
                    valuesFoundInListOfCodeTypes.add(listId);
                }
            }
        }
        return !valuesFoundInListOfCodeTypes.equals(valuesToBeFound);
    }

    public boolean valueDoesNotContainAll(List<CodeType> codeTypes, String... valuesToMatch) {
        HashSet<String> valuesFoundInListOfCodeTypes = new HashSet<>();
        HashSet<String> valuesToBeFound = new HashSet<>(Arrays.asList(valuesToMatch));
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        for (CodeType codeType : codeTypes) {
            String value = codeType.getValue();

            if (valuesToBeFound.contains(value)) {
                valuesFoundInListOfCodeTypes.add(value);
            }
        }
        return !valuesFoundInListOfCodeTypes.equals(valuesToBeFound);
    }

    /**
     * This method will return false if any codeType does not have matching value from the list valuesToBe matched
     *
     * @param codeTypes
     * @param valuesToMatch
     * @return
     */
    public boolean codeTypeValueContainsMatch(List<CodeType> codeTypes, String... valuesToMatch) {
        if (CollectionUtils.isEmpty(codeTypes) || valuesToMatch == null) {
            return false;
        }
        HashSet<String> valuesToBeFound = new HashSet<>(Arrays.asList(valuesToMatch));
        for (CodeType codeType : codeTypes) {
            if (codeType == null || StringUtils.isEmpty(codeType.getValue()) || !valuesToBeFound.contains(codeType.getValue())) {
                return false;
            }
        }
        return true;
    }

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
        if (CollectionUtils.isEmpty(delimitedPeriods)) {
            return true;
        }
        for (DelimitedPeriod delimitedPeriod : delimitedPeriods) {
            if ((start && end && (delimitedPeriod.getStartDateTime() == null || delimitedPeriod.getEndDateTime() == null))
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
        return eu.europa.ec.fisheries.uvms.commons.date.DateUtils.nowUTC().toDate();
    }

    public boolean dateNotInPast(Date creationDate) {
        return dateNotInPast(creationDate, 0);
    }

    /**
     * Acceptance date/time not before report creation date/time.
     *
     * @param creationDate   The report creation date/time.
     * @param acceptanceDate The acceptance date/time.
     * @param minutes        A threshold in minutes to compensate for incorrect clock synchronization of the exchanging systems.
     */
    public boolean acceptanceDateNotBeforeCreationDate(Date creationDate, Date acceptanceDate, int minutes) {

        boolean acceptanceDateNotAfterCreationDate = true;
        if (creationDate != null && acceptanceDate != null) {
            DateTime creationDateTime = new DateTime(creationDate).toDateTime(DateTimeZone.UTC).plusMinutes(minutes);
            DateTime acceptanceDateTime = new DateTime(acceptanceDate).toDateTime(DateTimeZone.UTC);
            log.debug("creationDate is {}", creationDateTime.toString());
            log.debug("acceptanceDateTime is {}", acceptanceDateTime.toString());
            acceptanceDateNotAfterCreationDate = acceptanceDateTime.toDate().before(creationDateTime.toDate());

        }
        return acceptanceDateNotAfterCreationDate;
    }

    /**
     * Message creation date/time not in the past.
     *
     * @param creationDate The Message creation date/time to be verified.
     * @param minutes      A threshold in minutes to compensate for incorrect clock synchronization of the exchanging systems.
     */
    public boolean dateNotInPast(Date creationDate, int minutes) {

        boolean notInPast = true;
        if (creationDate != null) {
            DateTime now = eu.europa.ec.fisheries.uvms.commons.date.DateUtils.nowUTC();
            log.debug("now is {}", now.toString());
            now = now.plusMinutes(minutes);
            DateTime creationDateUTC = new DateTime(creationDate).toDateTime(DateTimeZone.UTC);
            log.debug("creationDate is {}", creationDateUTC.toString());
            notInPast = !creationDateUTC.toDate().before(now.toDate());
        }
        return notInPast;
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

    public boolean listIdNotContains(CodeType codeType, String... values) {
        return listIdNotContains(Collections.singletonList(codeType), values);
    }

    public boolean listIdNotContains(List<CodeType> codeTypes, String... values) {
        if (values == null || values.length == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        for (String val : values) {
            for (CodeType codeType : codeTypes) {
                if (val.equals(codeType.getListId())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean valueNotContains(List<CodeType> codeTypes, String value, int hits) {
        if (value == null || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        int found = 0;
        for (CodeType codeType : codeTypes) {
            if (value.equals(codeType.getValue())) {
                found++;
            }
        }
        return hits != found;
    }

    public int count(List<CodeType> codeTypes, String value) {
        if (value == null || CollectionUtils.isEmpty(codeTypes)) {
            return 0;
        }
        int found = 0;
        for (CodeType codeType : codeTypes) {
            if (value.equals(codeType.getValue())) {
                found++;
            }
        }
        return found;
    }

    public boolean valueContainsAny(CodeType codeType, String... valuesToMatch) {
        return codeType == null || valueContainsAny(Collections.singletonList(codeType), valuesToMatch);
    }

    /**
     * Returns true when value is not found.
     *
     * @param codeTypes
     * @param valuesToMatch
     * @return
     */
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

    public boolean isPositive(MeasureType value) {
        return isPositive(Collections.singletonList(value));
    }

    public boolean isPositive(BigDecimal value) {
        return value == null || value.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isPositive(List<MeasureType> value) {
        if (CollectionUtils.isEmpty(value)) {
            return false;
        }
        value.removeAll(Collections.singleton(null));
        for (MeasureType type : value) {
            BigDecimal val = type.getValue();
            if (val == null || BigDecimal.ZERO.compareTo(val) <= 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isPositiveNumeric(List<NumericType> numericList) {
        if (CollectionUtils.isEmpty(numericList)) {
            return false;
        }
        ImmutableList<NumericType> removeNull = ImmutableList.copyOf(Iterables.filter(numericList, Predicates.notNull()));
        for (NumericType type : removeNull) {
            BigDecimal val = type.getValue();
            if (val == null || BigDecimal.ZERO.compareTo(val) > 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isPositiveInteger(List<MeasureType> value) {
        if (CollectionUtils.isEmpty(value)) {
            return true;
        }
        ImmutableList<MeasureType> removeNull = ImmutableList.copyOf(Iterables.filter(value, Predicates.notNull()));
        for (MeasureType type : removeNull) {
            if (!isPositiveIntegerValue(type.getValue())) {
                return false;
            }
        }
        return true;
    }

    private boolean isIntegerValue(BigDecimal bigDecimal) {
        return bigDecimal != null && (bigDecimal.signum() == 0 || bigDecimal.scale() <= 0 || bigDecimal.stripTrailingZeros().scale() <= 0)
                && !(bigDecimal.toPlainString().indexOf(".") > 0);
    }

    public boolean isPositiveIntegerValue(BigDecimal bigDecimal) {
        return bigDecimal != null && bigDecimal.signum() != -1 && isIntegerValue(bigDecimal);
    }

    public boolean isGreaterThanZero(MeasureType value){
        return isGreaterThanZero(Collections.singletonList(value));
    }

    /**
     * This method will check if all values passed  to this method are greater than zero.
     *
     * @param values
     * @return TRUE : If all values are greater than zero
     * FALSE: If any one value is null OR less than OR equal to zero
     */
    public boolean isGreaterThanZero(List<MeasureType> values) {
        if (CollectionUtils.isEmpty(values)) {
            return false;
        }
        ImmutableList<MeasureType> removeNull = ImmutableList.copyOf(Iterables.filter(values, Predicates.notNull()));
        for (MeasureType type : removeNull) {
            BigDecimal val = type.getValue();
            if (val == null || BigDecimal.ZERO.compareTo(val) > -1) {
                return false;
            }
        }
        return true;
    }

    /*
    public boolean valueIdTypeContainsAny(String value, String... valuesToMatch) {
        IdType idType = new IdType();
        idType.setValue(value);
        return valueIdTypeContainsAny(Collections.singletonList(idType), valuesToMatch);
    }

    public boolean valueIdTypeContainsAny(IdType idType, String... valuesToMatch) {
        return valueIdTypeContainsAny(Collections.singletonList(idType), valuesToMatch);
    }

    public boolean valueIdTypeContainsAny(List<IdType> idTypes, String... valuesToMatch) {
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(idTypes)) {
            return true;
        }
        boolean isMatchFound = false;
        for (String val : valuesToMatch) {
            for (IdType idType : idTypes) {
                if (val.equals(idType.getValue())) {
                    isMatchFound = true;
                    break;
                }
            }
        }
        return !isMatchFound;
    }
    */

    public boolean codeTypeValuesUnique(List<CodeType> codeTypes) {
        if (CollectionUtils.isEmpty(codeTypes)) {
            return false;
        }
        Set<String> stringSet = new HashSet<>();

        for (CodeType codeType : codeTypes) {
            stringSet.add(codeType.getValue());
        }

        return codeTypes.size() == stringSet.size();
    }

    public boolean valueCodeTypeContainsAny(List<CodeType> codeTypes, String... valuesToMatch) {
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        boolean isMatchFound = false;
        for (String val : valuesToMatch) {
            for (CodeType codeType : codeTypes) {
                if (val.equals(codeType.getValue())) {
                    isMatchFound = true;
                    break;
                }
            }
        }
        return !isMatchFound;
    }

    public boolean anyValueContainsAll(List<CodeType> codeTypes, String... valuesToMatch) {
        if (valuesToMatch == null || valuesToMatch.length == 0 || CollectionUtils.isEmpty(codeTypes)) {
            return true;
        }
        ImmutableList<CodeType> removeNull = ImmutableList.copyOf(Iterables.filter(codeTypes, Predicates.notNull()));
        boolean isMatchFound = false;

        outer:
        for (String val : valuesToMatch) {
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
     * This method return true if the list contains at least one or more SpecifiedFLUXLocations list.
     * @param faCatches
     * @return true | false
     */
    public boolean validateFluxLocationsForFaCatch(List<FACatch> faCatches) {
        boolean isValid = true;
        for (FACatch faCatch : faCatches) {
            List<FLUXLocation> checkList = faCatch.getSpecifiedFLUXLocations();
            if (checkList == null || checkList.isEmpty()) {
                isValid = false;
            }
        }
        return !isValid;
    }

    /**
     * This method returns true if the collection is null or is empty.
	 * @param collection
	 * @return true | false
     */
    public boolean isEmpty(Collection<?> collection){
        if(collection != null){
            collection.removeAll(Collections.singleton(null));
        }
        if(CollectionUtils.isEmpty(collection)){
            return true;
        }
        final Object object = collection.toArray()[0];
        boolean hasOnlyEmptyElements = true;
        if(object instanceof CodeType){
            for (CodeType code : (List<CodeType>) collection)
                if (StringUtils.isNotEmpty(code.getValue())) {
                    hasOnlyEmptyElements = false;
                }
        } else if(object instanceof IdType){
            for (IdType id : (List<IdType>) collection)
                if (StringUtils.isNotEmpty(id.getValue())) {
                    hasOnlyEmptyElements = false;
                }
        } else if (object instanceof TextType){
            for (TextType id : (List<TextType>) collection)
                if (StringUtils.isNotEmpty(id.getValue())) {
                    hasOnlyEmptyElements = false;
                }
        }
        else {
            for (Object obj :  collection)
                if (!allFieldsAreNullOrEmptyList(obj)){
                    hasOnlyEmptyElements = false;
                }
        }
        return hasOnlyEmptyElements;
    }

    private boolean allFieldsAreNullOrEmptyList(Object obj) {
        if(obj == null){
            return true;
        }
        for (Field field : obj.getClass().getDeclaredFields()) {
            if(!Modifier.isStatic(field.getModifiers())){
                final Object fieldsObj;
                try {
                    field.setAccessible(true);
                    fieldsObj = field.get(obj);
                } catch (IllegalAccessException e) {
                    log.warn("[WARN] Couldn't access field (even forcing accessible to true) {}", field);
                    return false;
                }
                if (fieldsObj instanceof List) {
                    final List listField = (List) fieldsObj;
                    if(CollectionUtils.isNotEmpty(listField) && !isEmpty(listField)){
                        return false;
                    }
                } else if(fieldsObj != null) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * This method returns true of the map is null or is empty.
     * @param map
     * @return true | false
     */
    public boolean isEmpty( Map<?, ?> map ){
        return MapUtils.isEmpty(map);
    }

    /**
     * This method returns true if the objet is null.
     * @param object
     * @return true | false
     */
    public boolean isEmpty( Object object ){
        if(object instanceof Collection){
            return isEmpty((Collection)object);
        }
        return object == null;
    }

    /**
     * This method returns true if the input array is null or its length is zero.
     * @param array
     * @return true | false
     */
    public boolean isEmpty( Object[] array ){
        return array == null || array.length == 0;
    }

    /**
     * This method returns true if the input string is null or its length is zero.
     * @param string
     * @return true | false
     */
    public boolean isEmpty( String string ){
        return string == null || string.trim().length() == 0;
    }

    /**
     * Checks if the list of strings contains empty (null / "") elements.
     *
     * @param stringsList
     * @return true / false
     */
    public boolean containsEmptyStrings(List<String> stringsList) {
        return isEmpty(stringsList) || stringsList.contains(null) || stringsList.contains("");
    }

    public boolean containsOnlyEmptyStrings(List<String> stringsList) {
        if (!isEmpty(stringsList)) {
            for (String str : stringsList) {
                if (StringUtils.isNotEmpty(str)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isNumeric(List<NumericType> list) {
        if (CollectionUtils.isEmpty(list)){
            return false;
        }
        for (NumericType type : list) {
            if (type.getValue() == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isListEmptyOrBetweenNumberOfItems(List sourceList, int minNumberOfItems, int maxNumberOfItems) {
        compareMinimumToMaximum(minNumberOfItems, maxNumberOfItems);

        return sourceList != null && (sourceList.isEmpty() || (sourceList.size() <= maxNumberOfItems && sourceList.size() >= minNumberOfItems));
    }

    public boolean isListNotEmptyAndBetweenNumberOfItems(List sourceList, int minNumberOfItems, int maxNumberOfItems) {
        compareMinimumToMaximum(minNumberOfItems, maxNumberOfItems);

        return sourceList != null && (!sourceList.isEmpty()) && sourceList.size() <= maxNumberOfItems && sourceList.size() >= minNumberOfItems;
    }

    private void compareMinimumToMaximum(int minNumberOfItems, int maxNumberOfItems) {
        if (minNumberOfItems > maxNumberOfItems) {
            throw new IllegalArgumentException("minNumberOfItems '" + minNumberOfItems + "' can't be bigger than '" + maxNumberOfItems + "'.");
        }
    }

    public boolean isListEmptyOrAllValuesUnique(List<CodeType> sourceList) {
        if (isEmpty(sourceList)) {
            return true;
        }
        List<String> values = new ArrayList<>();
        for (CodeType codeType : sourceList) {
            if (codeType == null) {
                continue;
            }
            if (values.contains(codeType.getValue())) {
                return false;
            }
            values.add(codeType.getValue());
        }
        return true;
    }

    public boolean isListEmptyOrAllListIdsUnique(List<CodeType> sourceList) {
        if (isEmpty(sourceList)) {
            return true;
        }
        List<String> listIds = new ArrayList<>();
        for (CodeType codeType : sourceList) {
            if (codeType == null) {
                continue;
            }
            if (listIds.contains(codeType.getListId())) {
                return false;
            }
            listIds.add(codeType.getListId());
        }
        return true;
    }

    public boolean isListEmptyOrValuesMatchPassedArguments(List<CodeType> sourceList, String... valuesToMatch) {
        if (isEmpty(sourceList)) {
            return true;
        }
        List<String> matchList = Arrays.asList(valuesToMatch);
        for (CodeType codeType : sourceList) {
            if (codeType == null) {
                return false;
            }

            if (!matchList.contains(codeType.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Deprecated //numberOfDecimals(BigDecimal value)
    public int getNumberOfDecimalPlaces(BigDecimal bigDecimal) {
        String string = bigDecimal.stripTrailingZeros().toPlainString();
        int index = string.indexOf('.');
        return index < 0 ? 0 : string.length() - index - 1;
    }

    public int numberOfDecimals(BigDecimal value) {
        if (value == null) {
            return -1;
        }
        int i = value.subtract(value.setScale(0, RoundingMode.FLOOR)).movePointRight(value.scale()).intValue();
        return Integer.toString(i).length();
    }

    @Deprecated // use isInRange
    public boolean isBigDecimalBetween(BigDecimal value, BigDecimal lowBound, BigDecimal upperBound) {
        return value.compareTo(lowBound) > 0 && value.compareTo(upperBound) < 0;
    }

    public boolean isInRange(BigDecimal value, int lowBound, int upperBound) {
        return value == null || !((value.compareTo(new BigDecimal(lowBound)) > 0) && (value.compareTo(new BigDecimal(upperBound)) < 0));
    }

    public enum FORMATS {
        // TODO : ICCAT and CFR have Territory characters reppresented [a-zA-Z]{3} which is not correct, cause it is matching not existing combinations also (Like ABC
        // TODO : which is not an existing country code). This happens with ICCAT -second sequence- and CFR -first sequence-!

        UUID("[a-fA-F0-9]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"),
        EXT_MARK(".{1,14}"),
        IRCS("[a-zA-Z0-9]{1,7}"),
        CFR("[a-zA-Z]{3}[a-zA-Z0-9]{9}"),
        UVI("[a-zA-Z0-9]{7}"),
        ICCAT("AT[a-zA-Z0-9]{3}[a-zA-Z0-9]{3}[a-zA-Z0-9]{5}"),
        GFCM("[a-zA-Z0-9]{1,13}"),
        JFO("^(19|20)[0-9][0-9]-\\d{3}"),
        //EU_TRIP_ID("[a-zA-Z]{3}-TRP-[a-zA-Z0-9]{0,20}"),
        EU_SALES_ID_COMMON("[A-Z]{3}-(SN|TOD|TRD|SN+TOD)-.*"),
        EU_SALES_ID_SPECIFIC(".*-.*-[A-Za-z0-9\\-]{1,20}"),
        EU_SALES_TAKE_OVER_DOCUMENT_ID("[A-Z]{3}-TOD-[A-Za-z0-9\\-]{1,20}"),
        EU_SALES_SALES_NOTE_ID("[A-Z]{3}-SN-[A-Za-z0-9\\-]{1,20}"),
        EU_TRIP_ID("[A-Z]{3}-TRP-[A-Za-z0-9\\-]{1,20}"),
        FLUX_SALES_TYPE("(SN\\+TOD|SN|TOD|TRD)"),
        FLUX_SALES_QUERY_PARAM("(VESSEL|FLAG|ROLE|PLACE|SALES_ID|TRIP_ID)"),
        FLUX_GP_RESPONSE("(OK|NOK|WOK)"),
        ISO_8601_WITH_OPT_MILLIS("\\d{4}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[1-2]\\d|3[0-1])T(?:[0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d([\\.]\\d{3})?Z"),
        FLUXTL_ON("[a-zA-Z]{3}[a-zA-Z0-9]{17}");

        String formatStr;

        FORMATS(String someFormat) {
            setFormatStr(someFormat);
        }

        public String getFormatStr() {
            return formatStr;
        }

        void setFormatStr(String formatStr) {
            this.formatStr = formatStr;
        }
    }

    public boolean matchWithFluxTL(List<IdType> idTypes) {
        boolean isMatch = false;
        if (idTypes == null){
            return false;
        }
        for (IdType idType : idTypes) {
            isMatch = matchWithFluxTL(idType);
            if (isMatch) {
                break;
            }
        }
        return isMatch;
    }

    private boolean matchWithFluxTL(IdType idType) {
        boolean match = false;
        if (idType != null) {
            String[] idValueArray = split(idType.getValue(), COLON);
            if (ArrayUtils.isNotEmpty(idValueArray)) {
                String[] split = split(senderOrReceiver, COLON);
                if (ArrayUtils.isNotEmpty(split)) {
                    match = StringUtils.equals(idValueArray[0], split[0]);
                }
            }
        }
        return match;
    }

    public String[] split(String value, String separator) {
        if (StringUtils.isBlank(separator)) {
            return new String[0];
        }
        String[] strings = new String[0];
        if (StringUtils.isNotEmpty(value)) {
            strings = value.split(separator);
        }
        return strings;
    }


    public boolean isSameReportedVesselFlagState(IdType vesselCountryId, List<IdTypeWithFlagState> assetList) {
        if (CollectionUtils.isEmpty(assetList)) {
            return false;
        }
        String vesselCountryIdValue = vesselCountryId.getValue();
        for (IdTypeWithFlagState asset : assetList){
            if (isSameFlagState(vesselCountryIdValue, asset)) {
                return true;
            }
        }
        return false;
    }

    private Boolean isSameFlagState(String vesselCountryIdValue, IdTypeWithFlagState asset) {
        if (asset != null){
            String flagState = asset.getFlagState();
            return flagState != null && flagState.equals(vesselCountryIdValue);
        }
        return false;
    }

    public boolean vesselIdsMatch(List<IdType> vesselIds, IdType vesselCountryId, List<IdTypeWithFlagState> assetList) {
        if (CollectionUtils.isEmpty(assetList)) {
            return false;
        }
        List<IdTypeWithFlagState> listToBeMatched = new ArrayList<>();
        for (IdType idType : vesselIds) {
            listToBeMatched.add(new IdTypeWithFlagState(idType.getSchemeId(), idType.getValue(), vesselCountryId.getValue()));
        }
        for (IdTypeWithFlagState elemFromListToBeMatched : listToBeMatched) {
            if (!assetList.contains(elemFromListToBeMatched)) {
                return false;
            }
        }
        return true;
    }

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

    public boolean stringEquals(String str1, String str2){
        return StringUtils.equals(str1, str2);
    }

    public boolean isBlank(eu.europa.ec.fisheries.schema.sales.TextType textType) {
        return textType == null || StringUtils.isBlank(textType.getValue());
    }

    public boolean isBlank(MeasureType measureType) {
        return measureType == null || measureType.getValue() == null;
    }

    public boolean isBlank(TextType textType) {
        return textType == null || StringUtils.isBlank(textType.getValue());
    }

    public boolean isBlank(eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType codeType) {
        return codeType == null || StringUtils.isBlank(codeType.getValue());
    }

    public boolean isBlank(IdType id) {
        return id == null || StringUtils.isBlank(id.getValue());
    }

    public String getValueForSchemeId(String schemeId, List<IdType> ids) {
        if (StringUtils.isBlank(schemeId) || CollectionUtils.isEmpty(ids)) {
            return null;
        }
        for (IdType id : ids) {
            String idsSchemeId = id.getSchemeId();
            if (StringUtils.isNotBlank(idsSchemeId) && idsSchemeId.equals(schemeId)) {
                return id.getValue();
            }
        }
        return null;
    }

    public boolean anyFluxLocationTypeCodeContainsValue(List<FLUXLocation> fluxLocations, String value) {
        if (CollectionUtils.isEmpty(fluxLocations) || StringUtils.isBlank(value)) {
            return false;
        }
        for (FLUXLocation fluxLocation : fluxLocations) {
            un.unece.uncefact.data.standard.unqualifieddatatype._20.CodeType typeCode = fluxLocation.getTypeCode();

            if (typeCode != null && value.equals(typeCode.getValue())) {
                return true;
            }
        }
        return false;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    /**
     * This method checks if atleast one FACatch from specifiedFACatches has matching speciesCode and typeCode value
     *
     * @param specifiedFACatches FACatches from this list would be matched against
     * @param speciesCode        FACatch speciesCode value to be matched
     * @param typeCode           FACatch typeCode value to be matched
     * @return TRUE : Atleast one FACatch with matching criteria found
     * FALSE :  No FACatch with matching criteria found
     */
    public boolean containsAnyFaCatch(List<FACatch> specifiedFACatches, String speciesCode, String typeCode) {
        if (CollectionUtils.isEmpty(specifiedFACatches) || speciesCode == null || typeCode == null) {
            return false;
        }
        for (FACatch faCatch : specifiedFACatches) {
            if (faCatch.getSpeciesCode() != null && faCatch.getTypeCode() != null && speciesCode.equals(faCatch.getSpeciesCode().getValue()) && typeCode.equals(faCatch.getTypeCode().getValue())) {
                return true;
            }
        }
        return false;
    }

    public String getSenderOrReceiver() {
        return senderOrReceiver;
    }

    public void setSenderOrReceiver(String senderOrReceiver) {
        this.senderOrReceiver = senderOrReceiver;
    }

    public DateTime getCreationDateOfMessage() {
        return creationDateOfMessage;
    }

    public void setCreationDateOfMessage(DateTime creationDateOfMessage) {
        this.creationDateOfMessage = creationDateOfMessage;
    }
}
