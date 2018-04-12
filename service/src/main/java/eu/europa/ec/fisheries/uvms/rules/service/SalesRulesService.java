package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.uvms.rules.service.business.FactWithReferencedId;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;

import javax.ejb.Local;

@Local
public interface SalesRulesService {
    /**
     * Checks if the report is a correction and if the item type
     * (Sales note/Take over document) is equal to the item type of the referenced report
     * @param fluxSalesReportMessage
     * @return
     */
    boolean isCorrectionAndIsItemTypeTheSameAsInTheOriginal(SalesFLUXSalesReportMessageFact fluxSalesReportMessage);

    /**
     * Checks if a report exists that is equal to the id of the incoming report
     * @param fact
     * @return
     */
    boolean doesReportNotExistWithId(SalesFLUXReportDocumentFact fact);

    /**
     * Checks if the reception date is not within 48 hours of the sale date
     * @param fact
     * @return
     */
    boolean isReceptionDate48hAfterSaleDate(SalesFLUXSalesReportMessageFact fact);

    /**
     * Checks if the reception date is not within 48 hours of the landing declaration
     * @param fact
     * @return
     */
    boolean isReceptionDate48hAfterLandingDeclaration(SalesFLUXSalesReportMessageFact fact);

    /**
     * Checks if a sales document exists
     * @param fact
     * @return
     */
    boolean isIdNotUnique(SalesDocumentFact fact);

    /**
     * Checks if a referenced TOD actually exists.
     * If no TOD is referenced, this method returns true.
     * @param fact
     * @return
     */
    boolean doesTakeOverDocumentIdExist(SalesDocumentFact fact);

    /**
     * Checks if a referenced Sales Note actually exists.
     * If no Sales Note is referenced, this method returns true.
     * @param fact
     * @return
     */
    boolean doesSalesNoteIdExist(SalesDocumentFact fact);

    /**
     * Checks if a sales query ID is not unique
     * @param fact
     * @return
     */
    boolean isIdNotUnique(SalesQueryFact fact);

    boolean isStartDateMoreThan3YearsAgo(SalesDelimitedPeriodFact fact);

    boolean isIdNotUnique(SalesFLUXResponseDocumentFact fact);

    /**
     * Checks if a report exists that is equal to the referencedID of the incoming report
     * @param fact fact, containing a document with a referencedID
     * @return true if no report/query exist with an id equals to the given the referencedID
     */
    boolean doesReferencedIdNotExist(FactWithReferencedId fact);

    boolean isDateNotInPast(SalesFLUXResponseDocumentFact fact);

    boolean isDateOfValidationAfterCreationDateOfResponse(SalesFLUXResponseDocumentFact fact);

    boolean isSalesQueryParameterValueNotValid(CodeType typeCode, CodeType valueCode);

    /**
     * @return whether the currency used is this document is an official currency of the sales country at a certain date
     * When one of these parameters (currency, country, occurrence) is missing, this method will return false.
     */
    boolean isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSales(SalesDocumentFact fact);

    /**
     * Checks if the id of an original report is not unique
     * @param fact
     * @return whether the id of an original report is not unique, returns false when report is not original
     */
    boolean isOriginalAndIsIdNotUnique(SalesFLUXSalesReportMessageFact fact);

    /**
     * Checks if the CFR of the ship in a sales note is in Fleet on the landing date
     * @param fact
     * @return
     */
    boolean isCFRInFleetUnderFlagStateOnLandingDate(SalesFishingActivityFact fact);
}
