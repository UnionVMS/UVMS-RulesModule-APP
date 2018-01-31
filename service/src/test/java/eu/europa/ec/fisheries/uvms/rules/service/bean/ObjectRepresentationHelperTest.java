package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.uvms.rules.service.business.helper.ObjectRepresentationHelper;
import org.junit.Test;
import un.unece.uncefact.data.standard.mdr.communication.ColumnDataType;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ObjectRepresentationHelperTest {

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenTrueWithOneMatchForColumnOne() throws Exception {
        String column1 = "test";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType(column2, value2, String.class.toString());

        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA));

        ColumnDataType columnDataType1ForObjectB = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectB = new ColumnDataType(column2, "another value", String.class.toString());
        ObjectRepresentation objectRepresentationB = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectB, columnDataType2ForObjectB));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentationA, objectRepresentationB);

        assertTrue(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenTrueWithTwoMatchesForColumnOne() throws Exception {
        String column1 = "test";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";

        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType(column2, "another value", String.class.toString());
        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA));

        ColumnDataType columnDataType1ForObjectB = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectB = new ColumnDataType(column2, "another value", String.class.toString());
        ObjectRepresentation objectRepresentationB = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectB, columnDataType2ForObjectB));

        ColumnDataType columnDataType1ForObjectC = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectC = new ColumnDataType(column2, value2, String.class.toString());
        ObjectRepresentation objectRepresentationC = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectC, columnDataType2ForObjectC));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentationA, objectRepresentationB, objectRepresentationC);

        assertTrue(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenFalseBecauseValue2NotFound() throws Exception {
        String column1 = "test";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType(column2, "something else", String.class.toString());

        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA));

        ColumnDataType columnDataType1ForObjectB = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectB = new ColumnDataType(column2, "another value", String.class.toString());
        ObjectRepresentation objectRepresentationB = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectB, columnDataType2ForObjectB));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentationA, objectRepresentationB);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenFalseBecauseColumn2DoesNotExist() throws Exception {
        String column1 = "test";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, value1, String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType("another column", value2, String.class.toString());

        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA));

        ColumnDataType columnDataType1ForObjectB = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectB = new ColumnDataType(column2, "another value", String.class.toString());
        ObjectRepresentation objectRepresentationB = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectB, columnDataType2ForObjectB));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentationA, objectRepresentationB);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenFalseBecauseColumn1DoesNotExist() throws Exception {
        String column1 = "test";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        ColumnDataType columnDataType1ForObjectA = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectA = new ColumnDataType(column2, value2, String.class.toString());

        ObjectRepresentation objectRepresentationA = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectA, columnDataType2ForObjectA));

        ColumnDataType columnDataType1ForObjectB = new ColumnDataType(column1, "another code", String.class.toString());
        ColumnDataType columnDataType2ForObjectB = new ColumnDataType(column2, "another value", String.class.toString());
        ObjectRepresentation objectRepresentationB = new ObjectRepresentation(Arrays.asList(columnDataType1ForObjectB, columnDataType2ForObjectB));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentationA, objectRepresentationB);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValuesWhenFalseBecauseNoObjectRepresentations() throws Exception {
        String column1 = "test";
        String value1 = "testValue";
        String column2 = "terminal_ind";
        String value2 = "true";
        List<ObjectRepresentation> objectRepresentations = new ArrayList<>();

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenColumnsAndCorrespondingValues
                (column1, value1, column2, value2, objectRepresentations));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumnWhenTrue() throws Exception {
        String codeValue = "test";
        String columnName = "terminal_ind";
        String columnValue = "true";
        ColumnDataType codeColumn1 = new ColumnDataType("code", codeValue, String.class.toString());
        ColumnDataType columnToSearchFor1 = new ColumnDataType(columnName, columnValue, String.class.toString());

        ObjectRepresentation objectRepresentation1 = new ObjectRepresentation(Arrays.asList(codeColumn1, columnToSearchFor1));

        ColumnDataType codeColumn2 = new ColumnDataType("code", "another code", String.class.toString());
        ColumnDataType columnToSearchFor2 = new ColumnDataType(columnName, "another value", String.class.toString());
        ObjectRepresentation objectRepresentation2 = new ObjectRepresentation(Arrays.asList(codeColumn2, columnToSearchFor2));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentation1, objectRepresentation2);

        assertTrue(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
                    (codeValue, columnName, columnValue, objectRepresentations));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumnWhenFalseBecauseValueNotFound() throws Exception {
        String codeValue = "test";
        String columnName = "terminal_ind";
        String columnValue = "true";
        ColumnDataType codeColumn1 = new ColumnDataType("code", codeValue, String.class.toString());
        ColumnDataType columnToSearchFor1 = new ColumnDataType(columnName, "something else", String.class.toString());

        ObjectRepresentation objectRepresentation1 = new ObjectRepresentation(Arrays.asList(codeColumn1, columnToSearchFor1));

        ColumnDataType codeColumn2 = new ColumnDataType("code", "another code", String.class.toString());
        ColumnDataType columnToSearchFor2 = new ColumnDataType(columnName, "another value", String.class.toString());
        ObjectRepresentation objectRepresentation2 = new ObjectRepresentation(Arrays.asList(codeColumn2, columnToSearchFor2));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentation1, objectRepresentation2);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
                (codeValue, columnName, columnValue, objectRepresentations));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumnWhenFalseBecauseColumnDoesNotExist() throws Exception {
        String codeValue = "test";
        String columnName = "terminal_ind";
        String columnValue = "true";
        ColumnDataType codeColumn1 = new ColumnDataType("code", codeValue, String.class.toString());
        ColumnDataType columnToSearchFor1 = new ColumnDataType("another column", columnValue, String.class.toString());

        ObjectRepresentation objectRepresentation1 = new ObjectRepresentation(Arrays.asList(codeColumn1, columnToSearchFor1));

        ColumnDataType codeColumn2 = new ColumnDataType("code", "another code", String.class.toString());
        ColumnDataType columnToSearchFor2 = new ColumnDataType("another column", "another value", String.class.toString());
        ObjectRepresentation objectRepresentation2 = new ObjectRepresentation(Arrays.asList(codeColumn2, columnToSearchFor2));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentation1, objectRepresentation2);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
                (codeValue, columnName, columnValue, objectRepresentations));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumnWhenFalseBecauseCodeDoesNotExist() throws Exception {
        String codeValue = "test";
        String columnName = "terminal_ind";
        String columnValue = "true";
        ColumnDataType codeColumn1 = new ColumnDataType("code", "another code", String.class.toString());
        ColumnDataType columnToSearchFor1 = new ColumnDataType(columnName, columnValue, String.class.toString());

        ObjectRepresentation objectRepresentation1 = new ObjectRepresentation(Arrays.asList(codeColumn1, columnToSearchFor1));

        ColumnDataType codeColumn2 = new ColumnDataType("code", "and another one", String.class.toString());
        ColumnDataType columnToSearchFor2 = new ColumnDataType(columnName, "another value", String.class.toString());
        ObjectRepresentation objectRepresentation2 = new ObjectRepresentation(Arrays.asList(codeColumn2, columnToSearchFor2));

        List<ObjectRepresentation> objectRepresentations = Arrays.asList(objectRepresentation1, objectRepresentation2);

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
                (codeValue, columnName, columnValue, objectRepresentations));
    }

    @Test
    public void doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumnWhenFalseBecauseNoObjectRepresentations() throws Exception {
        String codeValue = "test";
        String columnName = "terminal_ind";
        String columnValue = "true";
        List<ObjectRepresentation> objectRepresentations = new ArrayList<>();

        assertFalse(ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn
                (codeValue, columnName, columnValue, objectRepresentations));
    }

}