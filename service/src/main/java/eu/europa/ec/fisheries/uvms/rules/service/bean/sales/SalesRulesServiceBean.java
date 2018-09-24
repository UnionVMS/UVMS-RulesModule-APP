package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;


import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingActivitySummary;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripResponse;
import eu.europa.ec.fisheries.uvms.rules.service.*;
import eu.europa.ec.fisheries.uvms.rules.service.bean.asset.client.IAssetClient;
import eu.europa.ec.fisheries.uvms.rules.service.bean.asset.client.impl.AssetClientBean;
import eu.europa.ec.fisheries.uvms.rules.service.business.FactWithReferencedId;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType;
import eu.europa.ec.fisheries.uvms.rules.service.business.fact.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.helper.ObjectRepresentationHelper;
import eu.europa.ec.fisheries.uvms.rules.service.constants.MDRAcronymType;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.mdr.communication.ObjectRepresentation;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Singleton
@Slf4j
public class SalesRulesServiceBean implements SalesRulesService {

    @EJB
    private SalesService salesService;

    @EJB
    private MDRCacheRuleService mdrService;

    @EJB
    private ActivityService activityService;

    @EJB
    private IAssetClient assetClientBean;

    @Inject
    private MapperFacade mapper;

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

        return !salesService.isIdNotUnique(fact.getIDS().get(0).getValue(), SalesMessageIdType.SALES_REPORT);
    }

    @Override
    public boolean isReceptionDate24hAfterSaleDate(SalesFLUXSalesReportMessageFact fact) {
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
                if (salesEventType.getOccurrenceDateTime() != null && salesEventType.getOccurrenceDateTime().getDateTime() != null
                        && isMoreThan24HoursLater(receptionDate, salesEventType.getOccurrenceDateTime().getDateTime())) {
                    return true;
                }
            }
        }


        return false;
    }

    private boolean isMoreThan24HoursLater(DateTime receptionDate, DateTime saleDate) {
        // Add 24 hours and 1 minute to salesDate to determine the latest acceptable date.
        // Adding 24 hours isn't enough, a report sent exactly 24h after the event is still valid.
        // This is why there's a buffer of 1 minute. Not sure if this is enough: time will tell (pun intended)
        DateTime latestAcceptableDate = saleDate.plusHours(24).plusMinutes(1);

        // If receptionDate is after latestAcceptableDate, return true
        return receptionDate.isAfter(latestAcceptableDate);
    }

    @Override
    public boolean isReceptionDate24hAfterLandingDeclaration(SalesFLUXSalesReportMessageFact fact) {
        try {
            String fishingTripID = fact.getSalesReports().get(0).getIncludedSalesDocuments().get(0).getSpecifiedFishingActivities().get(0).getSpecifiedFishingTrip().getIDS().get(0).getValue();

            Optional<FishingTripResponse> fishingTripResponse = activityService.getFishingTrip(fishingTripID);
            if (!fishingTripResponse.isPresent()) {
                return true;
            }

            FishingTripResponse fishingTrip = fishingTripResponse.get();
            Optional<DateTime> dateTimeFromLandingActivity = findAcceptanceDateOfLanding(fishingTrip);
            if (!dateTimeFromLandingActivity.isPresent()) {
                return false;
            }

            DateTime receptionDate = fact.getFLUXReportDocument().getCreationDateTime().getDateTime();

            return isMoreThan24HoursLater(receptionDate, dateTimeFromLandingActivity.get());
        } catch (NullPointerException | IndexOutOfBoundsException ex) {
            // if anything is not filled in, this rule cannot be evaluated properly
            return false;
        }
    }

    @Override
    public boolean isIdNotUnique(SalesDocumentFact fact) {
        if (fact == null || isEmpty(fact.getIDS()) || fact.getIDS().get(0) == null || isBlank(fact.getIDS().get(0).getValue())) {
            return false;
        }

        return salesService.isIdNotUnique(fact.getIDS().get(0).getValue(), SalesMessageIdType.SALES_DOCUMENT);
    }

    @Override
    public boolean isIdNotUnique(SalesQueryFact fact) {
        if (fact == null || fact.getID() == null || isBlank(fact.getID().getValue())) {
            return false;
        }

        return salesService.isIdNotUnique(fact.getID().getValue(), SalesMessageIdType.SALES_QUERY);
    }

    @Override
    public boolean isIdNotUnique(SalesFLUXResponseDocumentFact fact) {
        if (fact == null || fact.getIDS() == null ||
                isEmpty(fact.getIDS()) || fact.getIDS().get(0) == null
                || isBlank(fact.getIDS().get(0).getValue())) {
            return false;
        }

        return salesService.isIdNotUnique(fact.getIDS().get(0).getValue(), SalesMessageIdType.SALES_RESPONSE);
    }

    @Override
    public boolean doesReferencedIdNotExist(FactWithReferencedId fact) {
        //We've seen that under extreme load, it's possible that a response is sent and validated, before the
        // report gets saved. Then, we get a false positive: the referenced id does not exist yet, while a few seconds
        // later it will be in the database. To work around this problem, if the referenced id does not exist,
        // we retry a few seconds later.
        return doesReferencedIdNotExist(fact, 0);
    }

    private boolean doesReferencedIdNotExist(FactWithReferencedId fact, int numberOfTries) {
        if (fact == null || fact.getReferencedID() == null || isBlank(fact.getReferencedID().getValue())) {
            return false;
        }

        boolean doesReferencedIdExist = salesService.isIdNotUnique(fact.getReferencedID().getValue(), SalesMessageIdType.SALES_REFERENCED_ID);
        if (numberOfTries > 0 || doesReferencedIdExist) {
            return !doesReferencedIdExist;
        } else {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                log.error("Thread.sleep failed?", e);
                Thread.currentThread().interrupt();
            }
            return doesReferencedIdNotExist(fact, ++numberOfTries);
        }
    }

    @Override
    public boolean doesTakeOverDocumentIdExist(SalesDocumentFact fact) {
        if (fact == null || isEmpty(fact.getTakeoverDocumentIDs())) {
            return true;
        }

        List<String> takeOverDocumentIds = Lists.newArrayList();

        for (IdType takeoverDocumentIDType : fact.getTakeoverDocumentIDs()) {
            if (takeoverDocumentIDType != null && !isBlank(takeoverDocumentIDType.getValue())) {
                takeOverDocumentIds.add(takeoverDocumentIDType.getValue());
            }
        }
        return salesService.areAnyOfTheseIdsNotUnique(takeOverDocumentIds, SalesMessageIdType.SALES_REPORT);
    }

    @Override
    public boolean doesSalesNoteIdExist(SalesDocumentFact fact) {
        if (fact == null || isEmpty(fact.getSalesNoteIDs())) {
            return true;
        }

        List<String> salesNoteIds = Lists.newArrayList();

        for (IdType salesNoteIDType : fact.getSalesNoteIDs()) {
            if (salesNoteIDType != null && !isBlank(salesNoteIDType.getValue())) {
                salesNoteIds.add(salesNoteIDType.getValue());
            }
        }
        return salesService.areAnyOfTheseIdsNotUnique(salesNoteIds, SalesMessageIdType.SALES_REPORT);
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

    @Override
    public boolean isSalesQueryParameterValueNotValid(CodeType typeCode, CodeType valueCode){
        if (typeCode == null || valueCode == null){
            return true;
        }

        switch (typeCode.getValue()){
            case "ROLE":
                return !mdrService.isPresentInMDRList("FLUX_SALES_QUERY_PARAM_ROLE", valueCode.getValue(), DateTime.now());
            case "FLAG":
                return !mdrService.isPresentInMDRList("TERRITORY", valueCode.getValue(), DateTime.now());
            case "PLACE":
                return !mdrService.isPresentInMDRList("LOCATION", valueCode.getValue(), DateTime.now());
            default:
                return true;
        }
    }

    @Override
    public boolean isTheUsedCurrencyAnOfficialCurrencyOfTheCountryAtTheDateOfTheSales(SalesDocumentFact fact) {
        Optional<String> currency = fact.getCurrencyCodeIfExists();
        Optional<String> country = fact.getCountryIfExists();
        Optional<DateTime> occurrence = fact.getOccurrenceIfPresent();

        if (currency.isPresent() && country.isPresent() && occurrence.isPresent()) {
            List<ObjectRepresentation> territoriesAndTheirCurrencies = mdrService.getObjectRepresentationList(MDRAcronymType.TERRITORY_CURR);
            return ObjectRepresentationHelper.doesObjectRepresentationExistWithTheGivenCodeAndWithTheGivenValueForTheGivenColumn(currency.get(), "placesCode", country.get(), territoriesAndTheirCurrencies, fact.getCreationDateOfMessage());
        } else {
            return false;
        }
    }

    @Override
    public boolean isOriginalAndIsIdNotUnique(SalesFLUXSalesReportMessageFact fact) {
        if (fact == null || isEmpty(fact.getSalesReports()) || fact.getSalesReports().get(0) == null
                || fact.getFLUXReportDocument() == null || fact.getFLUXReportDocument().getPurposeCode() == null
                || isBlank(fact.getFLUXReportDocument().getPurposeCode().getValue())
                || isEmpty(fact.getSalesReports().get(0).getIncludedSalesDocuments())
                || isEmpty(fact.getSalesReports().get(0).getIncludedSalesDocuments().get(0).getIDS())
                || isBlank(fact.getSalesReports().get(0).getIncludedSalesDocuments().get(0).getIDS().get(0).getValue())
                ) {
            return false;
        }

        // If the report is not an original, return false
        if (!fact.getFLUXReportDocument().getPurposeCode().getValue().equals("9")) {
            return false;
        }

        return salesService.isIdNotUnique(fact.getSalesReports().get(0).getIncludedSalesDocuments().get(0).getIDS().get(0).getValue(), SalesMessageIdType.SALES_DOCUMENT);
    }

    @Override
    public boolean isCFRInFleetUnderFlagStateOnLandingDate(SalesFishingActivityFact fact) {
        try {

            List<IDType> ids = fact.getRelatedVesselTransportMeans().get(0).getIDS();

            Optional<String> cfr = findCfrFromIdTypes(ids);

            // vessel country
            String flagState = fact.getRelatedVesselTransportMeans().get(0)
                    .getRegistrationVesselCountry().getID().getValue();

            // get landing date from sales document or get it from activity module?
            DateTime landingDate = fact.getSpecifiedDelimitedPeriods().get(0).getStartDateTime().getDateTime();

            if (!cfr.isPresent() || isBlank(cfr.get())
                    || isBlank(flagState) || landingDate == null) {
                // not enough data available to evaluate this rule
                return false;
            }

            return !assetClientBean.isCFRInFleetUnderFlagStateOnLandingDate(cfr.get(), flagState, landingDate);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private Optional<String> findCfrFromIdTypes(List<IDType> ids) {
        for (IDType id : ids) {
            if (id.getSchemeID().equals("CFR")) {
                return Optional.of(id.getValue());
            }
        }

        return Optional.absent();
    }

    private Optional<DateTime> findAcceptanceDateOfLanding(FishingTripResponse fishingTrip) {
        for (FishingActivitySummary activitySummary : fishingTrip.getFishingActivityLists()) {
            if (activitySummary.getActivityType().equals("LANDING")) {
                return Optional.of(new DateTime(activitySummary.getAcceptedDateTime().toGregorianCalendar()));
            }
        }
        return Optional.absent();
    }


}
