package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;


import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.SalesRulesService;
import eu.europa.ec.fisheries.uvms.rules.service.SalesService;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import ma.glasnost.orika.MapperFacade;
import org.joda.time.DateTime;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isBlank;

@Singleton
public class SalesRulesServiceBean implements SalesRulesService {


    @EJB
    SalesService salesService;

    @Inject
    MapperFacade mapper;

    @Override
    public boolean isCorrectionAndIsItemTypeTheSameAsInTheOriginal(SalesFLUXSalesReportMessageFact fluxSalesReportMessageFact) {
        if (fluxSalesReportMessageFact == null) {
            return false;
        }

        FLUXSalesReportMessage fluxSalesReportMessage = mapper.map(fluxSalesReportMessageFact, FLUXSalesReportMessage.class);
        return salesService.isCorrectionAndIsItemTypeTheSameAsInTheOriginal(fluxSalesReportMessage);
    }

    @Override
    public boolean doesReportNotExistWithId(SalesFLUXReportDocumentFact fact) {
        if (fact == null || isEmpty(fact.getIDS()) || fact.getIDS().get(0) == null || isBlank(fact.getIDS().get(0).getValue())) {
            return false;
        }

        return !salesService.doesReportExistWithId(fact.getIDS().get(0).getValue());
    }

    @Override
    public boolean doesReportNotExistWithReferencedId(SalesFLUXReportDocumentFact fact) {
        if (fact == null || fact.getReferencedID() == null || isBlank(fact.getReferencedID().getValue())) {
            return false;
        }

        return !salesService.doesReportExistWithId(fact.getReferencedID().getValue());
    }

    @Override
    public boolean isReceptionDate48hAfterSaleDate(SalesFLUXSalesReportMessageFact fact) {
        if (fact == null ||
                isEmpty(fact.getSalesReports()) ||
                isEmpty(fact.getSalesReports().get(0).getIncludedSalesDocuments()) ||
                fact.getFLUXReportDocument() == null ||
                fact.getFLUXReportDocument().getCreationDateTime() == null ||
                fact.getFLUXReportDocument().getCreationDateTime().getDateTime() == null) {
            // Can't let this continue, will cause nullpointers
            return false;
        }

        DateTime receptionDate = fact.getFLUXReportDocument().getCreationDateTime().getDateTime();

        List<SalesDocumentType> includedSalesDocuments = fact.getSalesReports().get(0).getIncludedSalesDocuments();
        for (SalesDocumentType includedSalesDocument : includedSalesDocuments) {
            for (SalesEventType salesEventType : includedSalesDocument.getSpecifiedSalesEvents()) {
                if (salesEventType.getOccurrenceDateTime() != null && salesEventType.getOccurrenceDateTime().getDateTime() != null) {
                    if (isMoreThan48HoursLater(receptionDate, salesEventType.getOccurrenceDateTime().getDateTime())) {
                        return true;
                    }
                }
            }
        }


        return false;
    }

    private boolean isMoreThan48HoursLater(DateTime receptionDate, DateTime eventDate) {
        DateTime saleDate = eventDate;

        // Add 48 hours and 1 minute to salesDate to determine the latest acceptable date.
        // Adding 48 hours isn't enough, a report sent exactly 48h after the event is still valid.
        // This is why there's a buffer of 1 minute. Not sure if this is enough: time will tell (pun intended)
        DateTime latestAcceptableDate = saleDate.plusHours(48).plusMinutes(1);

        // If receptionDate is after latestAcceptableDate, return true
        return receptionDate.isAfter(latestAcceptableDate);
    }

    @Override
    public boolean isReceptionDate48hAfterLandingDeclaration(SalesFLUXSalesReportMessageFact fact) {
        if (fact == null ||
                isEmpty(fact.getSalesReports()) ||
                fact.getSalesReports().get(0) == null ||
                isEmpty(fact.getSalesReports().get(0).getIncludedSalesDocuments()) ||
                fact.getSalesReports().get(0).getIncludedSalesDocuments()
                        .get(0) == null ||
                isEmpty(fact.getSalesReports().get(0).getIncludedSalesDocuments()
                        .get(0).getSpecifiedFishingActivities()) ||
                fact.getFLUXReportDocument() == null ||
                fact.getFLUXReportDocument().getCreationDateTime() == null ||
                fact.getFLUXReportDocument().getCreationDateTime().getDateTime() == null) {
            // Can't let this continue, will cause nullpointers
            return false;
        }

        DateTime receptionDate = fact.getFLUXReportDocument().getCreationDateTime().getDateTime();

        List<SalesDocumentType> includedSalesDocuments = fact.getSalesReports().get(0).getIncludedSalesDocuments();
        for (SalesDocumentType includedSalesDocument : includedSalesDocuments) {
            for (FishingActivityType fishingActivityType : includedSalesDocument.getSpecifiedFishingActivities()) {
                for (DelimitedPeriodType delimitedPeriodType : fishingActivityType.getSpecifiedDelimitedPeriods()) {
                    if (delimitedPeriodType.getStartDateTime() != null && delimitedPeriodType.getStartDateTime().getDateTime() != null) {
                        if (isMoreThan48HoursLater(receptionDate, delimitedPeriodType.getStartDateTime().getDateTime())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean isIdNotUnique(SalesDocumentFact fact) {
        if (fact == null || isEmpty(fact.getIDS()) || fact.getIDS().get(0) == null || isBlank(fact.getIDS().get(0).getValue())) {
            return false;
        }

        return salesService.isIdNotUnique(fact.getIDS().get(0).getValue(), UniqueIDType.SALES_DOCUMENT);
    }

    @Override
    public boolean isIdNotUnique(SalesQueryFact fact) {
        if (fact == null || fact.getID() == null || isBlank(fact.getID().getValue())) {
            return false;
        }

        return salesService.isIdNotUnique(fact.getID().getValue(), UniqueIDType.SALES_QUERY);
    }

    @Override
    public boolean isIdNotUnique(SalesFLUXResponseDocumentFact fact) {
        if (fact == null || fact.getIDS() == null ||
                isEmpty(fact.getIDS()) || fact.getIDS().get(0) == null
                || isBlank(fact.getIDS().get(0).getValue())) {
            return false;
        }

        return salesService.isIdNotUnique(fact.getIDS().get(0).getValue(), UniqueIDType.SALES_RESPONSE);
    }

    @Override
    public boolean doesReferencedIdNotExist(SalesFLUXResponseDocumentFact fact) {
        if (fact == null || fact.getReferencedID() == null || isBlank(fact.getReferencedID().getValue())) {
            return false;
        }

        return !salesService.isIdNotUnique(fact.getReferencedID().getValue(), UniqueIDType.SALES_RESPONSE_REFERENCED_ID);
    }

    @Override
    public boolean doesTakeOverDocumentIdExist(SalesDocumentFact fact) {
        if (fact == null || isEmpty(fact.getTakeoverDocumentIDs())) {
            return false;
        }


        List<String> takeOverDocumentIds = Lists.newArrayList();

        for (IdType takeoverDocumentIDType : fact.getTakeoverDocumentIDs()) {
            if (takeoverDocumentIDType != null && !isBlank(takeoverDocumentIDType.getValue())) {
                takeOverDocumentIds.add(takeoverDocumentIDType.getValue());
            }
        }
        return salesService.areAnyOfTheseIdsNotUnique(takeOverDocumentIds, UniqueIDType.TAKEOVER_DOCUMENT);
    }

    @Override
    public boolean isStartDateMoreThan3YearsAgo(SalesDelimitedPeriodFact fact) {
        if (fact == null || fact.getStartDateTime() == null || fact.getStartDateTime().getDateTime() == null) {
            return false;
        }

        return fact.getStartDateTime().getDateTime().isBefore(DateTime.now().minusYears(3).minusMinutes(1));
    }

    @Override
    public boolean isDateNotInPast(SalesFLUXResponseDocumentFact fact) {
        if (fact == null || fact.getCreationDateTime() == null || fact.getCreationDateTime().getDateTime() == null) {
            return false;
        }

        return fact.getCreationDateTime().getDateTime().isAfter(DateTime.now());
    }


    @Override
    public boolean isDateOfValidationAfterCreationDateOfResponse(SalesFLUXResponseDocumentFact fact) {
        if (fact == null || fact.getCreationDateTime() == null || fact.getCreationDateTime().getDateTime() == null
                || fact.getRelatedValidationResultDocuments() == null || isEmpty(fact.getRelatedValidationResultDocuments())) {
            return false;
        }

        DateTime creationDateOfReport = fact.getCreationDateTime().getDateTime();

        for (ValidationResultDocumentType validationResultDocumentType : fact.getRelatedValidationResultDocuments()) {
            if (validationResultDocumentType.getCreationDateTime() != null && validationResultDocumentType.getCreationDateTime().getDateTime() != null) {
                DateTime creationDateOfValidationResult = validationResultDocumentType.getCreationDateTime().getDateTime();

                if (creationDateOfValidationResult.isAfter(creationDateOfReport)) {
                    return true;
                }
            }
        }

        return false;
    }

}
