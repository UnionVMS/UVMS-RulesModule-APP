package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.schema.sales.ValidationQualityAnalysisType;
import eu.europa.ec.fisheries.schema.sales.ValidationResultDocumentType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.ap.internal.util.Collections;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SalesFLUXResponseDocumentFactTest {

    SalesFLUXResponseDocumentFact fact;

    @Before
    public void setUp() throws Exception {
        fact = new SalesFLUXResponseDocumentFact();
    }

    @Test
    public void anyValidationResultDocumentsWithEmptyValidationQualityAnalysesWhenValid() throws Exception {
        ValidationResultDocumentType validationResultDocumentType = new ValidationResultDocumentType()
                .withRelatedValidationQualityAnalysises(new ValidationQualityAnalysisType());
        fact.setRelatedValidationResultDocuments(Arrays.asList(validationResultDocumentType));

        assertFalse(fact.anyValidationResultDocumentsWithEmptyValidationQualityAnalyses());
    }

    @Test
    public void anyValidationResultDocumentsWithEmptyValidationQualityAnalysesWhenInvalid() throws Exception {
        fact.setRelatedValidationResultDocuments(Collections.<ValidationResultDocumentType>newArrayList());

        assertTrue(fact.anyValidationResultDocumentsWithEmptyValidationQualityAnalyses());
    }

    @Test
    public void anyValidationQualityAnalysisWithoutReferencedTextItemsWhenItContainsReferencedItems() throws Exception {
        ValidationResultDocumentType validationResultDocumentType = new ValidationResultDocumentType()
                .withRelatedValidationQualityAnalysises(
                        new ValidationQualityAnalysisType()
                                .withReferencedItems(Arrays.asList(new TextType().withValue("value"))));
        fact.setRelatedValidationResultDocuments(Arrays.asList(validationResultDocumentType));


        assertFalse(fact.anyValidationQualityAnalysisWithoutReferencedTextItems());
    }

    @Test
    public void anyValidationQualityAnalysisWithoutReferencedTextItemsWhenItDoesNotContainReferencedItems() throws Exception {
        ValidationResultDocumentType validationResultDocumentType = new ValidationResultDocumentType()
                .withRelatedValidationQualityAnalysises(
                        new ValidationQualityAnalysisType()
                                .withReferencedItems(Collections.<TextType>newArrayList()));
        fact.setRelatedValidationResultDocuments(Arrays.asList(validationResultDocumentType));


        assertTrue(fact.anyValidationQualityAnalysisWithoutReferencedTextItems());
    }

    @Test
    public void equalsAndHashCode() {
        EqualsVerifier.forClass(SalesFLUXResponseDocumentFact.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .withRedefinedSuperclass()
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "source", "sequence")
                .verify();
    }

}