package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXReportDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXSalesReportMessageFact;

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
     * Checks if a report exists that is equal to the referencedID of the incoming report
     * @param fact
     * @return
     */
    boolean doesReportNotExistWithReferencedId(SalesFLUXReportDocumentFact fact);

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
     * Checks if a referenced TOD actually exists
     * @param fact
     * @return
     */
    boolean doesTakeOverDocumentIdExist(SalesDocumentFact fact);
}
