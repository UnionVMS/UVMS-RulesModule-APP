/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.uvms.rules.service.bean.RuleTestHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.FaArrivalFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesPartyFact;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Gregory Rinaldi
 */
public class AbstractFactTest {

    private AbstractFact fact = new FaArrivalFact();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testCheckDateNowHappy() {
        Date date = new DateTime(2005, 3, 26, 12, 0, 0, 0).toDate();
        assertTrue(date.before(fact.dateNow(1)));
    }

    @Test
    public void testListIdContainsAll() {
        List<CodeType> codeTypes = Arrays.asList(RuleTestHelper.getCodeType("val1", "AREA"), RuleTestHelper.getCodeType("val2", "AREA1"));
        assertTrue(fact.listIdContainsAll(codeTypes, "AREA", "AREA1", "BLA"));
    }

    @Test
    public void testValidateIDTypeHappy() {
        IdType idType = new IdType();
        idType.setSchemeId("53e3a36a-d6fa-4ac8-b061-7088327c7d81");
        IdType idType2 = new IdType();
        idType2.setSchemeId("53e36fab361-7338327c7d81");
        List<IdType> idTypes = Arrays.asList(idType, idType2);
        assertTrue(fact.schemeIdContainsAll(idTypes, "UUID"));
    }

    @Test
    public void testValidateIDType() {
        IdType idType = new IdType();
        idType.setSchemeId("53e3a36a-d6fa-4ac8-b061-7088327c7d81");
        IdType idType2 = new IdType();
        idType2.setSchemeId("53e3a36a-d6fa-4ac8-b061-7088327c7d81");
        List<IdType> idTypes = Arrays.asList(idType, idType2);
        assertTrue(fact.schemeIdContainsAll(idTypes, "UUID"));
    }

    @Test
    public void testContainsSchemeIdHappy() {
        IdType idType = new IdType();
        idType.setSchemeId("CFR");
        IdType idType2 = new IdType();
        idType2.setSchemeId("IRCS");
        IdType idType3 = new IdType();
        idType3.setSchemeId("EXT_MARK");
        List<IdType> idTypes = Arrays.asList(idType, idType2, idType3);
        boolean result = fact.schemeIdContainsAll(idTypes, "IRCS", "CFR");
        assertFalse(result);
    }

    @Test
    public void testContainsSchemeIdSad() {

        IdType idType = new IdType();
        idType.setSchemeId("CFR");
        IdType idType2 = new IdType();
        idType2.setSchemeId("IRCS");
        IdType idType3 = new IdType();
        idType3.setSchemeId("UUID");
        List<IdType> idTypes = Arrays.asList(idType, idType2, idType3);
        boolean result = fact.schemeIdContainsAll(idTypes, "UUID");
        assertFalse(result);
    }

    @Test
    public void testValidateFormatUUID_OK(){
        IdType uuidIdType = new IdType();
        uuidIdType.setSchemeId("UUID");
        uuidIdType.setValue(UUID.randomUUID().toString());
        List<IdType> idTypes = Arrays.asList(uuidIdType);
        boolean result = fact.validateFormat(idTypes);
        assertFalse(result);
    }

    @Test
    public void testValidateFormatUUID_NOT_OK(){
        IdType uuidIdType = new IdType();
        uuidIdType.setSchemeId("UUID");
        uuidIdType.setValue("ballshjshdhdfhsgfd");
        List<IdType> idTypes = Arrays.asList(uuidIdType);
        boolean result = fact.validateFormat(idTypes);
        assertTrue(result);
    }

    @Test
    public void testValidateFormatWhenPassingAStringAndResultIsOK(){
        boolean b = fact.validateFormat("aaa", "aaa");
        assertTrue(b);
    }

    @Test
    public void testValidateFormatWhenPassingAStringAndResultIsNOKBecauseArgumentIsNull(){
        boolean b = fact.validateFormat(null, null);
        assertFalse(b);
    }

    @Test
    public void testValidateFormatWhenPassingAStringAndResultIsNOKBecauseArgumentDoesNotApplyToTheFormat(){
        boolean b = fact.validateFormat("aap", "paa");
        assertFalse(b);
    }


    @Test
    public void testValidateFormatWhenSalesSpecificIDAndResultIsOK() {
        boolean b = fact.validateFormat("BEL-SN-2017-123456", AbstractFact.FORMATS.EU_SALES_ID_SPECIFIC.getFormatStr());
        assertTrue(b);
    }

    @Test
    public void testListIdDoesNotContainAllWhenListIdDoesNotContainAllValues() {
        CodeType codeType1 = getCodeTypeWithListID("bla");
        CodeType codeType2 = getCodeTypeWithListID("alb");

        List<CodeType> codeTypeList = Arrays.asList(codeType1, codeType2);

        assertTrue(fact.listIdDoesNotContainAll(codeTypeList, "bla", "notbla"));
    }

    @Test
    public void testListIdDoesNotContainAllWhenListIdDoesNotContainAllValuesAndValueIsNull() {
        CodeType codeType = getCodeTypeWithListID("alb");

        List<CodeType> codeTypeList = Arrays.asList(null, codeType, null);

        assertTrue(fact.listIdDoesNotContainAll(codeTypeList, "bla", "notbla"));
    }

    @Test
    public void testListIdDoesNotContainAllWhenListIdDoesContainAllValues() {
        CodeType codeType1 = getCodeTypeWithListID("bla");
        CodeType codeType2 = getCodeTypeWithListID("alb");

        List<CodeType> codeTypeList = Arrays.asList(codeType1, codeType2);

        assertFalse(fact.listIdDoesNotContainAll(codeTypeList, "bla", "alb"));
    }


    @Test
    public void testAnyValueDoesNotContainAllWhenValueDoesNotContainAnyValue() {
        CodeType codeType1 = getCodeTypeWithValue("BUYER");
        CodeType codeType2 = getCodeTypeWithValue("SELLER");

        SalesPartyFact salesPartyType1 = new SalesPartyFact();
        salesPartyType1.setRoleCodes(Arrays.asList(codeType1));

        SalesPartyFact salesPartyType2 = new SalesPartyFact();
        salesPartyType2.setRoleCodes(Arrays.asList(codeType2));


        assertTrue(fact.salesPartiesValueDoesNotContainAny(Arrays.asList(salesPartyType1, salesPartyType2),"SENDER"));
    }

    @Test
    public void testAnyValueDoesNotContainAllWhenValueContainsAnyValue() {
        CodeType codeType1 = getCodeTypeWithValue("BUYER");
        CodeType codeType2 = getCodeTypeWithValue("SELLER");
        CodeType codeType3 = getCodeTypeWithValue("SENDER");

        SalesPartyFact salesPartyType1 = new SalesPartyFact();
        salesPartyType1.setRoleCodes(Arrays.asList(codeType1));

        SalesPartyFact salesPartyType2 = new SalesPartyFact();
        salesPartyType2.setRoleCodes(Arrays.asList(codeType2));

        SalesPartyFact salesPartyType3 = new SalesPartyFact();
        salesPartyType3.setRoleCodes(Arrays.asList(codeType3));

        assertFalse(fact.salesPartiesValueDoesNotContainAny(Arrays.asList(salesPartyType1, salesPartyType2, salesPartyType3),"SENDER"));
    }

    @Test
    public void testValueIdTypeContainsAnyWhenValueIsPresent() {
        IdType idType1 = new IdType();
        idType1.setValue("value");
        IdType idType2 = new IdType();
        idType2.setValue("MASTER");

        List<IdType> idTypes = Arrays.asList(idType1, idType2);

        assertFalse(fact.valueIdTypeContainsAny(idTypes, "MASTER", "AGENT", "OWNER", "OPERATOR"));
    }

    @Test
    public void testValueIdTypeContainsAnyWhenValueIsNotPresent() {
        IdType idType1 = new IdType();
        idType1.setValue("value");
        IdType idType2 = new IdType();
        idType2.setValue("eulav");

        List<IdType> idTypes = Arrays.asList(idType1, idType2);

        assertTrue(fact.valueIdTypeContainsAny(idTypes, "MASTER", "AGENT", "OWNER", "OPERATOR"));
    }

    @Test
    public void testIsListEmptyOrBetweenNumberOfItemsWhenListSizeIs2AndShouldBeBetween1And1() throws Exception {
        List<String> list = Arrays.asList("", "");

        boolean listEmptyOrBetweenNumberOfItems = fact.isListEmptyOrBetweenNumberOfItems(list, 1, 1);
        assertFalse(listEmptyOrBetweenNumberOfItems);
    }

    @Test
    public void testIsListEmptyOrBetweenNumberOfItemsWhenListSizeIs1AndShouldBeBetween1And1() throws Exception {
        List<String> list = Arrays.asList("");

        boolean listEmptyOrBetweenNumberOfItems = fact.isListEmptyOrBetweenNumberOfItems(list, 1, 1);
        assertTrue(listEmptyOrBetweenNumberOfItems);
    }

    @Test
    public void testIsListEmptyOrBetweenNumberOfItemsWhenListSizeIs2AndShouldBeBetween1And5() throws Exception {
        List<String> list = Arrays.asList("", "");

        boolean listEmptyOrBetweenNumberOfItems = fact.isListEmptyOrBetweenNumberOfItems(list, 1, 5);
        assertTrue(listEmptyOrBetweenNumberOfItems);
    }

    @Test
    public void testIsListEmptyOrBetweenNumberOfItemsWhenListIsEmpty() throws Exception {
        List<String> list = Collections.emptyList();

        boolean listEmptyOrBetweenNumberOfItems = fact.isListEmptyOrBetweenNumberOfItems(list, 1, 5);
        assertTrue(listEmptyOrBetweenNumberOfItems);
    }

    @Test
    public void testIsListEmptyOrBetweenNumberOfItemsWhenMinNumberOfItemsIsBiggerThanMaxNumberOfItems() throws Exception {
        expectedException.expectMessage("minNumberOfItems '5' can't be bigger than '1'");
        expectedException.expect(IllegalArgumentException.class);

        List<String> list = Collections.emptyList();

        boolean listEmptyOrBetweenNumberOfItems = fact.isListEmptyOrBetweenNumberOfItems(list, 5, 1);
        assertTrue(listEmptyOrBetweenNumberOfItems);
    }

    @Test
    public void testIsListNotEmptyAndBetweenNumberOfItemsWhenListSizeIs2AndShouldBeBetween1And5() throws Exception {
        List<String> list = Arrays.asList("", "");

        boolean listNotEmptyAndBetweenNumberOfItems = fact.isListNotEmptyAndBetweenNumberOfItems(list, 1, 5);
        assertTrue(listNotEmptyAndBetweenNumberOfItems);
    }

    @Test
    public void testIsListNotEmptyAndBetweenNumberOfItemsWhenListSizeIs2AndShouldBeBetween3And5() throws Exception {
        List<String> list = Arrays.asList("", "");

        boolean listNotEmptyAndBetweenNumberOfItems = fact.isListNotEmptyAndBetweenNumberOfItems(list, 3, 5);
        assertFalse(listNotEmptyAndBetweenNumberOfItems);
    }

    @Test
    public void testIsListNotEmptyAndBetweenNumberOfItemsWhenListSizeIs1AndShouldBeBetween1And1() throws Exception {
        List<String> list = Arrays.asList("");

        boolean listNotEmptyAndBetweenNumberOfItems = fact.isListNotEmptyAndBetweenNumberOfItems(list, 1, 1);
        assertTrue(listNotEmptyAndBetweenNumberOfItems);
    }

    @Test
    public void testIsListEmptyOrAllValuesUniqueWhenListContainsUniqueValues() throws Exception {
        List<CodeType> uniqueValues = Arrays.asList(new CodeType("a"), new CodeType("b"), new CodeType("c"));

        boolean listIsUnique = fact.isListEmptyOrAllValuesUnique(uniqueValues);
        assertTrue(listIsUnique);
    }

    @Test
    public void testIsListEmptyOrAllValuesUniqueWhenListContainsDuplicateValues() throws Exception {
        List<CodeType> uniqueValues = Arrays.asList(new CodeType("a"), new CodeType("a"), new CodeType("c"));

        boolean listIsUnique = fact.isListEmptyOrAllValuesUnique(uniqueValues);
        assertFalse(listIsUnique);
    }

    @Test
    public void testIsListEmptyOrAllValuesUniqueWhenListIsEmpty() throws Exception {
        List<CodeType> uniqueValues = Collections.emptyList();

        boolean listIsUnique = fact.isListEmptyOrAllValuesUnique(uniqueValues);
        assertTrue(listIsUnique);
    }

    @Test
    public void testIsListEmptyOrValuesMatchPassedArgumentsWhenPassedArgumentsAreAllFound() throws Exception {
        List<CodeType> list = Arrays.asList(new CodeType("a"), new CodeType("a"), new CodeType("b"), new CodeType("c"), new CodeType("d"), new CodeType("e"));

        boolean listEmptyOrValuesMatchPassedArguments = fact.isListEmptyOrValuesMatchPassedArguments(list, "a", "b", "c", "d", "e");
        assertTrue(listEmptyOrValuesMatchPassedArguments);
    }

    @Test
    public void testIsListEmptyOrValuesMatchPassedArgumentsWhenNotAllPassedArgumentsAreFound() throws Exception {
        List<CodeType> list = Arrays.asList(new CodeType("a"), new CodeType("b"), new CodeType("c"), new CodeType("d"), new CodeType("e"));

        boolean listEmptyOrValuesMatchPassedArguments = fact.isListEmptyOrValuesMatchPassedArguments(list, "a", "b", "c", "d");
        assertFalse(listEmptyOrValuesMatchPassedArguments);
    }

    @Test
    public void isIdTypeValidFormatWhenInvalidSchemeID() {
        IdType idType = new IdType();
        idType.setValue("bla");
        idType.setSchemeId("bla");

        assertFalse(fact.isIdTypeValidFormat("bla", idType));
    }

    @Test
    public void isIdTypeValidFormatWhenValidSchemeID() {
        IdType idType = new IdType();
        idType.setValue("aaa123456789");
        idType.setSchemeId("CFR");

        assertTrue(fact.isIdTypeValidFormat("CFR", idType));
    }

    @Test
    public void isIdTypeValidFormatWhenValidSchemeIDAndInvalidValue() {
        IdType idType = new IdType();
        idType.setValue("bla");
        idType.setSchemeId("CFR");

        assertFalse(fact.isIdTypeValidFormat("CFR", idType));
    }

    @Test
    public void isCodeTypeValidFormatWhenListIDIsInvalid() {
        CodeType codeType = new CodeType();
        codeType.setListId("notvalid");
        codeType.setValue("notvalid");

        assertFalse(fact.isCodeTypeValidFormat("invalid", codeType));
    }

    @Test
    public void isCodeTypeValidFormatWhenListIDIsValid() {
        CodeType codeType = new CodeType();
        codeType.setListId("CFR");
        codeType.setValue("aaa123456789");

        assertTrue(fact.isCodeTypeValidFormat("CFR", codeType));
    }


    private CodeType getCodeTypeWithListID(String listId) {
        CodeType codeType = new CodeType();
        codeType.setListId(listId);
        return codeType;
    }

    private CodeType getCodeTypeWithValue(String value) {
        CodeType codeType = new CodeType();
        codeType.setValue(value);
        return codeType;
    }

}
