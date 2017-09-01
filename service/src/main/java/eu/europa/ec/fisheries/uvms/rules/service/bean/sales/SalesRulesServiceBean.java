package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;


import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.rules.service.SalesRulesService;
import eu.europa.ec.fisheries.uvms.rules.service.SalesService;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXReportDocumentFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.SalesFLUXSalesReportMessageFact;
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
                    DateTime saleDate = salesEventType.getOccurrenceDateTime().getDateTime();
                    // Add 49 hours to salesDate to determine the latest acceptable date
                    DateTime latestAcceptableDate = saleDate.plusHours(48).plusMinutes(1);

                    // If receptionDate is after latestAcceptableDate, return true
                    if (receptionDate.isAfter(latestAcceptableDate)) {
                        return true;
                    }
                }
            }
        }


        return false;
    }

    @Override
    public boolean isReceptionDate48hAfterLandingDeclaration(SalesFLUXSalesReportMessageFact fact) {
        if (fact == null ||
                isEmpty(fact.getSalesReports()) ||
                fact.getSalesReports().get(0) == null ||
                isEmpty(fact.getSalesReports().get(0).getIncludedSalesDocuments()) ||
                fact.getSalesReports().get(0).getIncludedSalesDocuments()
                        .get(0) == null||
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
            for (FishingActivityType fishingActivityType: includedSalesDocument.getSpecifiedFishingActivities()) {
                for (DelimitedPeriodType delimitedPeriodType : fishingActivityType.getSpecifiedDelimitedPeriods()) {
                    if (delimitedPeriodType.getStartDateTime() != null && delimitedPeriodType.getStartDateTime().getDateTime() != null) {
                        DateTime dateTime = delimitedPeriodType.getStartDateTime().getDateTime();
                        DateTime latestAcceptableDateTime = dateTime.plusHours(48).plusMinutes(1);

                        if (receptionDate.isAfter(latestAcceptableDateTime)) {
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
}
