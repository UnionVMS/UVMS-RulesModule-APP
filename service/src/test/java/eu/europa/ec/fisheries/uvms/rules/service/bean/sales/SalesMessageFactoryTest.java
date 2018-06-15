package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ValidationMessageType;
import eu.europa.ec.fisheries.schema.sales.ValidationQualityAnalysisType;
import eu.europa.ec.fisheries.uvms.rules.service.business.ValidationResultDto;
import org.junit.Before;
import org.junit.Test;

public class SalesMessageFactoryTest {

    private SalesMessageFactory salesMessageFactory;

    @Before
    public void init() {
        salesMessageFactory = new SalesMessageFactory();
    }

    @Test
    public void getMessageStatusWhenErrorsAndWarnings() throws Exception {
        ValidationResultDto validationResultDto = new ValidationResultDto();
        validationResultDto.setError(true);
        validationResultDto.setWarning(true);
        assertEquals("NOK", salesMessageFactory.getMessageStatus(validationResultDto));
    }

    @Test
    public void getMessageStatusWhenErrors() throws Exception {
        ValidationResultDto validationResultDto = new ValidationResultDto();
        validationResultDto.setError(true);
        validationResultDto.setWarning(false);
        assertEquals("NOK", salesMessageFactory.getMessageStatus(validationResultDto));
    }

    @Test
    public void getMessageStatuspWhenWarnings() throws Exception {
        ValidationResultDto validationResultDto = new ValidationResultDto();
        validationResultDto.setError(false);
        validationResultDto.setWarning(true);
        assertEquals("WOK", salesMessageFactory.getMessageStatus(validationResultDto));
    }

    @Test
    public void getMessageStatusWhenOK() throws Exception {
        ValidationResultDto validationResultDto = new ValidationResultDto();
        validationResultDto.setError(false);
        validationResultDto.setWarning(false);
        assertEquals("OK", salesMessageFactory.getMessageStatus(validationResultDto));
    }

    @Test
    public void getErrorTypeWhenWarning() throws Exception {
        assertEquals("WAR", salesMessageFactory.getErrorType(ErrorType.WARNING));
    }

    @Test
    public void getErrorTypeWhenError() throws Exception {
        assertEquals("ERR", salesMessageFactory.getErrorType(ErrorType.ERROR));
    }

    @Test
    public void mapToValidationQualityAnalysisWhenNoValidationMessagesAndListIsNull() {
        ValidationResultDto validationResultDto = new ValidationResultDto();
        validationResultDto.setValidationMessages(null);

        assertEquals(new ArrayList<>(), salesMessageFactory.mapToValidationQualityAnalysis(validationResultDto));
    }

    @Test
    public void mapToValidationQualityAnalysisWhenNoValidationMessagesAndListIsEmpty() {
        ValidationResultDto validationResultDto = new ValidationResultDto();
        validationResultDto.setValidationMessages(new ArrayList<ValidationMessageType>());

        assertEquals(new ArrayList<>(), salesMessageFactory.mapToValidationQualityAnalysis(validationResultDto));
    }

    @Test
    public void mapToValidationQualityAnalysisWhenThereAreValidationMessages() {
        //data set
        ValidationMessageType validationMessage1 = new ValidationMessageType();
        validationMessage1.setBrId("brId1");
        validationMessage1.setErrorType(ErrorType.ERROR);
        validationMessage1.setLevel("L01");
        validationMessage1.setMessage("message1");
        validationMessage1.getXpaths().add("xpath1");
        validationMessage1.getXpaths().add("xpath2");

        ValidationMessageType validationMessage2 = new ValidationMessageType();
        validationMessage2.setBrId("brId2");
        validationMessage2.setErrorType(ErrorType.WARNING);
        validationMessage2.setLevel("L02");
        validationMessage2.setMessage("message2");

        ValidationResultDto validationResultDto = new ValidationResultDto();
        validationResultDto.setValidationMessages(Arrays.asList(validationMessage1, validationMessage2));

        //execute
        List<ValidationQualityAnalysisType> result = salesMessageFactory.mapToValidationQualityAnalysis(validationResultDto);

        //assert and verify
        assertEquals(2, result.size());

        assertEquals("brId1", result.get(0).getID().getValue());
        assertEquals("ERR", result.get(0).getTypeCode().getValue());
        assertEquals("L01", result.get(0).getLevelCode().getValue());
        assertEquals(2, result.get(0).getReferencedItems().size());
        assertEquals("xpath1", result.get(0).getReferencedItems().get(0).getValue());
        assertEquals("xpath2", result.get(0).getReferencedItems().get(1).getValue());

        assertEquals("brId2", result.get(1).getID().getValue());
        assertEquals("WAR", result.get(1).getTypeCode().getValue());
        assertEquals("L02", result.get(1).getLevelCode().getValue());
        assertEquals(0, result.get(1).getReferencedItems().size());
    }

}