package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.sales.TextType;
import eu.europa.ec.fisheries.schema.sales.ValidationQualityAnalysisType;
import eu.europa.ec.fisheries.schema.sales.ValidationResultDocumentType;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.ap.internal.util.Collections;

import java.util.ArrayList;
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
                .withIgnoredFields("factType", "warnings", "errors", "uniqueIds", "ok", "sequence", "source", "senderOrReceiver", "salesCategoryType", "rulesDomainModel")
                .verify();
    }

    @Test
    public void hasIDInvalidFormatWhenTrue() {
        fact.setIDS(Lists.newArrayList(new IdType("abc")));
        assertTrue(fact.hasIDInvalidFormat());
    }

    @Test
    public void hasIDInvalidFormatWhenFalseBecauseIDIsNull() {
        fact.setIDS(null);
        assertFalse(fact.hasIDInvalidFormat());
    }

    @Test
    public void hasIDInvalidFormatWhenFalseBecauseIDsIsEmpty() {
        fact.setIDS(new ArrayList<IdType>());
        assertFalse(fact.hasIDInvalidFormat());
    }

    @Test
    public void hasIDInvalidFormatWhenFalseBecauseOfAnInvalidFormat() {
        fact.setIDS(Lists.newArrayList(new IdType("c2731113-9e77-4e42-9c10-821575b72115")));
        assertFalse(fact.hasIDInvalidFormat());
    }

    @Test
    public void hasReferencedIDInvalidFormatWhenTrue() {
        fact.setReferencedID(new IdType("abc"));
        assertTrue(fact.hasReferencedIDInvalidFormat());
    }

    @Test
    public void hasReferencedIDInvalidFormatWhenFalseBecauseReferencedIDIsNull() {
        fact.setReferencedID(null);
        assertFalse(fact.hasReferencedIDInvalidFormat());
    }

    @Test
    public void hasReferencedIDInvalidFormatWhenFalseBecauseOfAnInvalidFormat() {
        fact.setReferencedID(new IdType("c2731113-9e77-4e42-9c10-821575b72115"));
        assertFalse(fact.hasReferencedIDInvalidFormat());
    }

}