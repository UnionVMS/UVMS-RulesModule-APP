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