package eu.europa.ec.fisheries.uvms.rules.service.bean;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.rules.service.business.helper.ObjectRepresentationHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ObjectRepresentationHelperTest {

    @Test
    public void getValueOfColumnWhenSuccess() {
        String column1 = "findHistoryOfAssetBy";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType(column2, value2, String.class.toString());

        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA));

        assertEquals(Optional.of("true"), ObjectRepresentationHelper.getValueOfColumn("terminal_ind", objectRepresentationA));
    }

    @Test
    public void getValueOfColumnWhenColumnDoesNotExist() {
        String column1 = "findHistoryOfAssetBy";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType(column2, value2, String.class.toString());

        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA));

        assertEquals(Optional.absent(), ObjectRepresentationHelper.getValueOfColumn("bla", objectRepresentationA));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenTrueWithOneMatchForColumnOne() throws Exception {
        String column1 = "findHistoryOfAssetBy";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        String column3 = "startDate";
        String value3 = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");
        String column4 = "endDate";
        String value4 = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");

        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType(column2, value2, String.class.toString());
        ColumnDataType columnDataType3ForObjectA = new ColumnDataType(column3, value3, String.class.toString());
        ColumnDataType columnDataType4ForObjectA = new ColumnDataType(column4, value4, String.class.toString());

        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA, columnDataType3ForObjectA, columnDataType4ForObjectA));

        ColumnDataType columnDataType1ForObjectB = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectB = new ColumnDataType(column2, "another value", String.class.toString());
        ColumnDataType columnDataType3ForObjectB = new ColumnDataType(column3, value3, String.class.toString());
        ColumnDataType columnDataType4ForObjectB = new ColumnDataType(column4, value4, String.class.toString());
        ObjectRepresentation objectRepresentationB = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectB, columnDataType2ForObjectB, columnDataType3ForObjectB, columnDataType4ForObjectB));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentationA, objectRepresentationB);

        assertTrue(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations, new DateTime(2018, 2, 3, 0, 0, 0)));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenTrueWithTwoMatchesForColumnOne() throws Exception {
        String column1 = "findHistoryOfAssetBy";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        String column3 = "startDate";
        String value3 = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");
        String column4 = "endDate";
        String value4 = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");

        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType(column2, "another value", String.class.toString());
        ColumnDataType columnDataType3ForObjectA = new ColumnDataType(column3, value3, String.class.toString());
        ColumnDataType columnDataType4ForObjectA = new ColumnDataType(column4, value4, String.class.toString());
        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA, columnDataType3ForObjectA, columnDataType4ForObjectA));

        ColumnDataType columnDataType1ForObjectB = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectB = new ColumnDataType(column2, "another value", String.class.toString());
        ColumnDataType columnDataType3ForObjectB = new ColumnDataType(column3, value3, String.class.toString());
        ColumnDataType columnDataType4ForObjectB = new ColumnDataType(column4, value4, String.class.toString());
        ObjectRepresentation objectRepresentationB = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectB, columnDataType2ForObjectB, columnDataType3ForObjectB, columnDataType4ForObjectB));

        ColumnDataType columnDataType1ForObjectC = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectC = new ColumnDataType(column2, value2, String.class.toString());
        ColumnDataType columnDataType3ForObjectC = new ColumnDataType(column3, value3, String.class.toString());
        ColumnDataType columnDataType4ForObjectC = new ColumnDataType(column4, value4, String.class.toString());
        ObjectRepresentation objectRepresentationC = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectC, columnDataType2ForObjectC, columnDataType3ForObjectC, columnDataType4ForObjectC));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentationA, objectRepresentationB, objectRepresentationC);

        assertTrue(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations, new DateTime(2018, 2, 3, 0, 0, 0)));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenFalseBecauseValue2NotFound() throws Exception {
        String column1 = "findHistoryOfAssetBy";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        String column3 = "startDate";
        String value3 = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");
        String column4 = "endDate";
        String value4 = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");

        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType(column2, "something else", String.class.toString());
        ColumnDataType columnDataType3ForObjectA = new ColumnDataType(column3, value3, String.class.toString());
        ColumnDataType columnDataType4ForObjectA = new ColumnDataType(column4, value4, String.class.toString());
        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA, columnDataType3ForObjectA, columnDataType4ForObjectA));

        ColumnDataType columnDataType1ForObjectB = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectB = new ColumnDataType(column2, "another value", String.class.toString());
        ColumnDataType columnDataType3ForObjectB = new ColumnDataType(column3, value3, String.class.toString());
        ColumnDataType columnDataType4ForObjectB = new ColumnDataType(column4, value4, String.class.toString());
        ObjectRepresentation objectRepresentationB = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectB, columnDataType2ForObjectB, columnDataType3ForObjectB, columnDataType4ForObjectB));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentationA, objectRepresentationB);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations, new DateTime(2018, 2, 3, 0, 0, 0)));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenFalseBecauseColumn2DoesNotExist() throws Exception {
        String column1 = "findHistoryOfAssetBy";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        String column3 = "startDate";
        String value3 = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");
        String column4 = "endDate";
        String value4 = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");

        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType("another column", value2, String.class.toString());
        ColumnDataType columnDataType3ForObjectA = new ColumnDataType(column3, value3, Date.class.toString());
        ColumnDataType columnDataType4ForObjectA = new ColumnDataType(column4, value4, Date.class.toString());
        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA, columnDataType3ForObjectA, columnDataType4ForObjectA));

        ColumnDataType columnDataType1ForObjectB = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectB = new ColumnDataType(column2, "another value", String.class.toString());
        ColumnDataType columnDataType3ForObjectB = new ColumnDataType(column3, value3, String.class.toString());
        ColumnDataType columnDataType4ForObjectB = new ColumnDataType(column4, value4, String.class.toString());
        ObjectRepresentation objectRepresentationB = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectB, columnDataType2ForObjectB, columnDataType3ForObjectB, columnDataType4ForObjectB));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentationA, objectRepresentationB);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations, new DateTime(2018, 2, 3, 0, 0, 0)));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenFalseBecauseColumn1DoesNotExist() throws Exception {
        String column1 = "findHistoryOfAssetBy";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        String column3 = "startDate";
        String value3 = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");
        String column4 = "endDate";
        String value4 = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");

        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType(column2, value2, String.class.toString());
        ColumnDataType columnDataType3ForObjectA = new ColumnDataType(column3, value3, Date.class.toString());
        ColumnDataType columnDataType4ForObjectA = new ColumnDataType(column4, value4, Date.class.toString());
        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA, columnDataType3ForObjectA, columnDataType4ForObjectA));

        ColumnDataType columnDataType1ForObjectB = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectB = new ColumnDataType(column2, "another value", String.class.toString());
        ColumnDataType columnDataType3ForObjectB = new ColumnDataType(column3, value3, Date.class.toString());
        ColumnDataType columnDataType4ForObjectB = new ColumnDataType(column4, value4, Date.class.toString());
        ObjectRepresentation objectRepresentationB = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectB, columnDataType2ForObjectB, columnDataType3ForObjectB, columnDataType4ForObjectB));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentationA, objectRepresentationB);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations, new DateTime(2018, 2, 3, 0, 0, 0)));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenFalseBecauseNoObjectRepresentations() throws Exception {
        String column1 = "findHistoryOfAssetBy";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        List<ObjectRepresentation> objectRepresentations = new ArrayList<>();

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations, new DateTime(2018, 2, 3, 0, 0, 0)));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenFalseBecauseNoObjectRepresentationsForThisDate() throws Exception {
        String column1 = "findHistoryOfAssetBy";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        String column3 = "startDate";
        String value3 = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");
        String column4 = "endDate";
        String value4 = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");

        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType(column2, value2, String.class.toString());
        ColumnDataType columnDataType3ForObjectA = new ColumnDataType(column3, value3, String.class.toString());
        ColumnDataType columnDataType4ForObjectA = new ColumnDataType(column4, value4, String.class.toString());

        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA, columnDataType3ForObjectA, columnDataType4ForObjectA));

        ColumnDataType columnDataType1ForObjectB = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectB = new ColumnDataType(column2, "another value", String.class.toString());
        ColumnDataType columnDataType3ForObjectB = new ColumnDataType(column3, value3, String.class.toString());
        ColumnDataType columnDataType4ForObjectB = new ColumnDataType(column4, value4, String.class.toString());
        ObjectRepresentation objectRepresentationB = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectB, columnDataType2ForObjectB, columnDataType3ForObjectB, columnDataType4ForObjectB));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentationA, objectRepresentationB);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations, new DateTime(2222, 2, 3, 0, 0, 0)));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumnWhenTrue() throws Exception {
        String codeValue = "findHistoryOfAssetBy";
        String columnName = "terminal_ind";
        String columnValue = "true";
        String startDateColumnName = "startDate";
        String startDateColumnValue = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");
        String endDateColumnName = "endDate";
        String endDateColumnValue = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");

        ColumnDataType codeColumn1 = new ColumnDataType("code", codeValue, String.class.toString());
        ColumnDataType columnToSearchFor1 = new ColumnDataType(columnName, columnValue, String.class.toString());
        ColumnDataType startDateColumn = new ColumnDataType(startDateColumnName, startDateColumnValue, Date.class.toString());
        ColumnDataType endDateColumn = new ColumnDataType(endDateColumnName, endDateColumnValue, Date.class.toString());
        ObjectRepresentation objectRepresentation1 = new ObjectRepresentation(Arrays.asList(codeColumn1, columnToSearchFor1, startDateColumn, endDateColumn));

        ColumnDataType codeColumn2 = new ColumnDataType("code", "another code", String.class.toString());
        ColumnDataType columnToSearchFor2 = new ColumnDataType(columnName, "another value", String.class.toString());
        ObjectRepresentation objectRepresentation2 = new ObjectRepresentation(Arrays.asList(codeColumn2, columnToSearchFor2, startDateColumn, endDateColumn));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentation1, objectRepresentation2);

        assertTrue(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
                    (codeValue, columnName, columnValue, objectRepresentations, new DateTime(2018, 2, 3, 0, 0, 0)));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumnWhenFalseBecauseValueNotFound() throws Exception {
        String codeValue = "findHistoryOfAssetBy";
        String columnName = "terminal_ind";
        String columnValue = "true";
        String startDateColumnName = "startDate";
        String startDateColumnValue = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");
        String endDateColumnName = "endDate";
        String endDateColumnValue = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");

        ColumnDataType codeColumn1 = new ColumnDataType("code", codeValue, String.class.toString());
        ColumnDataType columnToSearchFor1 = new ColumnDataType(columnName, "something else", String.class.toString());
        ColumnDataType startDateColumn = new ColumnDataType(startDateColumnName, startDateColumnValue, Date.class.toString());
        ColumnDataType endDateColumn = new ColumnDataType(endDateColumnName, endDateColumnValue, Date.class.toString());
        ObjectRepresentation objectRepresentation1 = new ObjectRepresentation(Arrays.asList(codeColumn1, columnToSearchFor1, startDateColumn, endDateColumn));

        ColumnDataType codeColumn2 = new ColumnDataType("code", "another code", String.class.toString());
        ColumnDataType columnToSearchFor2 = new ColumnDataType(columnName, "another value", String.class.toString());
        ObjectRepresentation objectRepresentation2 = new ObjectRepresentation(Arrays.asList(codeColumn2, columnToSearchFor2, startDateColumn, endDateColumn));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentation1, objectRepresentation2);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
                (codeValue, columnName, columnValue, objectRepresentations, new DateTime(2018, 2, 3, 0, 0, 0)));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumnWhenFalseBecauseTheMDRCodeIsNoLongerValid() throws Exception {
        String codeValue = "findHistoryOfAssetBy";
        String columnName = "terminal_ind";
        String columnValue = "true";
        String startDateColumnName = "startDate";
        String startDateColumnValue = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");
        String endDateColumnName = "endDate";
        String endDateColumnValue = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");

        ColumnDataType codeColumn1 = new ColumnDataType("code", codeValue, String.class.toString());
        ColumnDataType columnToSearchFor1 = new ColumnDataType(columnName, columnValue, String.class.toString());
        ColumnDataType startDateColumn = new ColumnDataType(startDateColumnName, startDateColumnValue, Date.class.toString());
        ColumnDataType endDateColumn = new ColumnDataType(endDateColumnName, endDateColumnValue, Date.class.toString());
        ObjectRepresentation objectRepresentation1 = new ObjectRepresentation(Arrays.asList(codeColumn1, columnToSearchFor1, startDateColumn, endDateColumn));

        ColumnDataType codeColumn2 = new ColumnDataType("code", "another code", String.class.toString());
        ColumnDataType columnToSearchFor2 = new ColumnDataType(columnName, "another value", String.class.toString());
        ObjectRepresentation objectRepresentation2 = new ObjectRepresentation(Arrays.asList(codeColumn2, columnToSearchFor2, startDateColumn, endDateColumn));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentation1, objectRepresentation2);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
                (codeValue, columnName, columnValue, objectRepresentations, new DateTime(2222, 2, 3, 0, 0, 0)));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumnWhenFalseBecauseColumnDoesNotExist() throws Exception {
        String codeValue = "findHistoryOfAssetBy";
        String columnName = "terminal_ind";
        String columnValue = "true";
        String startDateColumnName = "startDate";
        String startDateColumnValue = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");
        String endDateColumnName = "endDate";
        String endDateColumnValue = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");

        ColumnDataType codeColumn1 = new ColumnDataType("code", codeValue, String.class.toString());
        ColumnDataType columnToSearchFor1 = new ColumnDataType("another column", columnValue, String.class.toString());
        ColumnDataType startDateColumn = new ColumnDataType(startDateColumnName, startDateColumnValue, Date.class.toString());
        ColumnDataType endDateColumn = new ColumnDataType(endDateColumnName, endDateColumnValue, Date.class.toString());
        ObjectRepresentation objectRepresentation1 = new ObjectRepresentation(Arrays.asList(codeColumn1, columnToSearchFor1, startDateColumn, endDateColumn));

        ColumnDataType codeColumn2 = new ColumnDataType("code", "another code", String.class.toString());
        ColumnDataType columnToSearchFor2 = new ColumnDataType("another column", "another value", String.class.toString());
        ObjectRepresentation objectRepresentation2 = new ObjectRepresentation(Arrays.asList(codeColumn2, columnToSearchFor2, startDateColumn, endDateColumn));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentation1, objectRepresentation2);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
                (codeValue, columnName, columnValue, objectRepresentations, new DateTime(2018, 2, 3, 0, 0, 0)));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumnWhenFalseBecauseCodeDoesNotExist() throws Exception {
        String codeValue = "findHistoryOfAssetBy";
        String columnName = "terminal_ind";
        String columnValue = "true";
        String startDateColumnName = "startDate";
        String startDateColumnValue = new DateTime(2018, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");
        String endDateColumnName = "endDate";
        String endDateColumnValue = new DateTime(2019, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss.SSS");

        ColumnDataType codeColumn1 = new ColumnDataType("code", "another code", String.class.toString());
        ColumnDataType columnToSearchFor1 = new ColumnDataType(columnName, columnValue, String.class.toString());
        ColumnDataType startDateColumn = new ColumnDataType(startDateColumnName, startDateColumnValue, Date.class.toString());
        ColumnDataType endDateColumn = new ColumnDataType(endDateColumnName, endDateColumnValue, Date.class.toString());
        ObjectRepresentation objectRepresentation1 = new ObjectRepresentation(Arrays.asList(codeColumn1, columnToSearchFor1, startDateColumn, endDateColumn));

        ColumnDataType codeColumn2 = new ColumnDataType("code", "and another one", String.class.toString());
        ColumnDataType columnToSearchFor2 = new ColumnDataType(columnName, "another value", String.class.toString());
        ObjectRepresentation objectRepresentation2 = new ObjectRepresentation(Arrays.asList(codeColumn2, columnToSearchFor2));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentation1, objectRepresentation2);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
                (codeValue, columnName, columnValue, objectRepresentations, new DateTime(2018, 2, 3, 0, 0, 0)));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumnWhenFalseBecauseNoObjectRepresentations() throws Exception {
        String codeValue = "findHistoryOfAssetBy";
        String columnName = "terminal_ind";
        String columnValue = "true";
        List<ObjectRepresentation> objectRepresentations = new ArrayList<>();

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
                (codeValue, columnName, columnValue, objectRepresentations, new DateTime(2018, 2, 3, 0, 0, 0)));
    }

}