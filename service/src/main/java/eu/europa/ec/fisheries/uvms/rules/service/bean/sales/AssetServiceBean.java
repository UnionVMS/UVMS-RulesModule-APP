package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMarshallException;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.service.AssetService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper.AssetServiceBeanHelper;
import eu.europa.ec.fisheries.uvms.rules.service.business.VesselTransportMeansDto;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselCountry;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

@Singleton
@Slf4j
public class AssetServiceBean implements AssetService {

    @EJB
    private AssetServiceBeanHelper helper;

    @Override
    public boolean isCFRInFleetUnderFlagStateOnLandingDate(String cfr, String flagState, DateTime landingDate) {
        try {
            List<Asset> assetHistories = helper.findHistoryOfAssetByCfr(cfr);

            Optional<Asset> historyOnDate = findAssetHistoryByDate(landingDate.toDate(), assetHistories);

            if (!historyOnDate.isPresent()) {
                // when there's no entry for the specified date, return false
                return false;
            }

            Asset asset = historyOnDate.get();
            return asset.getCountryCode().equals(flagState);

        } catch (MessageException | AssetModelMarshallException e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    public List<VesselTransportMeansDto> findHistoryOfAssetBy(List<FAReportDocument> faReportDocuments){
        List<VesselTransportMeansDto> vesselTransportMeansFactCollectedList = new ArrayList<>();

        for (FAReportDocument faReportDocument : faReportDocuments) {
            collectAndMap(vesselTransportMeansFactCollectedList, faReportDocument);
        }

        Iterator<VesselTransportMeansDto> iterator = vesselTransportMeansFactCollectedList.iterator();

        while (iterator.hasNext()){
            VesselTransportMeansDto vesselTransportMeansDto = iterator.next();
            String reportDate = vesselTransportMeansDto.getReportCreationDateTime();
            String regCountry = vesselTransportMeansDto.getRegistrationVesselCountry();
            Map<String, String> ids = vesselTransportMeansDto.getIds();

            String cfr = ids.get("CFR");
            String ircs = ids.get("IRCS");
            String extMark = ids.get("EXT_MARK");
            String iccat = ids.get("ICCAT");
            List<Asset> assets = helper.findHistoryOfAssetBy(reportDate, cfr, regCountry, ircs, extMark, iccat);

            if (CollectionUtils.isNotEmpty(assets)){
                vesselTransportMeansDto.setAsset(assets.get(0));
            }
        }

        return vesselTransportMeansFactCollectedList;
    }

    private void collectAndMap(List<VesselTransportMeansDto> vesselTransportMeansFactCollectedList, FAReportDocument faReportDocument) {
        List<FishingActivity> specifiedFishingActivities = faReportDocument.getSpecifiedFishingActivities();
        for (FishingActivity specifiedFishingActivity : specifiedFishingActivities) {
            List<FishingActivity> relatedFishingActivities = specifiedFishingActivity.getRelatedFishingActivities();
            if (CollectionUtils.isNotEmpty(relatedFishingActivities)){
                for (FishingActivity relatedFishingActivity : relatedFishingActivities) {
                    List<VesselTransportMeans> relatedVesselTransportMeans2 = relatedFishingActivity.getRelatedVesselTransportMeans();
                    if (CollectionUtils.isNotEmpty(relatedVesselTransportMeans2)){
                        for (VesselTransportMeans vesselTransportMeans : relatedVesselTransportMeans2) {
                            mapVesselTransportMeansToDto(vesselTransportMeansFactCollectedList, faReportDocument, vesselTransportMeans);

                        }
                    }
                }
            }
            List<VesselTransportMeans> relatedVesselTransportMeans = specifiedFishingActivity.getRelatedVesselTransportMeans();
            for (VesselTransportMeans relatedMeans : relatedVesselTransportMeans) {
                mapVesselTransportMeansToDto(vesselTransportMeansFactCollectedList, faReportDocument, relatedMeans);
            }
        }
        mapVesselTransportMeansToDto(vesselTransportMeansFactCollectedList, faReportDocument, faReportDocument.getSpecifiedVesselTransportMeans());
    }

    private void mapVesselTransportMeansToDto(List<VesselTransportMeansDto> vesselTransportMeansFactCollectedList, FAReportDocument faReportDocument, VesselTransportMeans transportMeans) {
        if (transportMeans != null){
            VesselTransportMeansDto relatedMeansDto = new VesselTransportMeansDto();
            setReportCreationDateTime(faReportDocument, relatedMeansDto);

            List<IDType> relatedIds = transportMeans.getIDS();
            for (IDType id : relatedIds) {
                relatedMeansDto.getIds().put(id.getSchemeID(), id.getValue());
            }
            VesselCountry country = transportMeans.getRegistrationVesselCountry();
            if (country != null && country.getID() != null){
                relatedMeansDto.setRegistrationVesselCountry(country.getID().getValue());
            }
            vesselTransportMeansFactCollectedList.add(relatedMeansDto);
        }
    }

    private void setReportCreationDateTime(FAReportDocument faReportDocument, VesselTransportMeansDto vesselTransportMeansFactCollected) {
        if (faReportDocument != null && faReportDocument.getRelatedFLUXReportDocument() != null){
            DateTimeType creationDateTime = faReportDocument.getRelatedFLUXReportDocument().getCreationDateTime();
            XMLGregorianCalendar dateTime = creationDateTime.getDateTime();
            if (dateTime != null){
                vesselTransportMeansFactCollected.setReportCreationDateTime(dateTime.toString());
            }
        }
    }

    protected Optional<Asset> findAssetHistoryByDate(Date landingDate, List<Asset> assetHistories) {
        // Asset module sorts this list already, but we do it again here just to be sure
        Collections.sort(assetHistories, assetComparator());

        Asset historyOnDate = null;

        // Because our list is ordered from newest event to oldest,
        // we can simply check if the date of the event is before the landing date
        for (Asset assetHistory : assetHistories) {
            if (assetHistory.getEventHistory().getEventDate().before(landingDate)) {
                historyOnDate = assetHistory;
                break;
            }
        }

        return Optional.fromNullable(historyOnDate);
    }


    protected Comparator<Asset> assetComparator() {
        return new Comparator<Asset>() {
            @Override
            public int compare(Asset o1, Asset o2) {
                if (o1.getEventHistory().getEventDate() == null && o2.getEventHistory().getEventDate() == null) {
                    return 0;
                } else if (o2.getEventHistory().getEventDate() == null) {
                    return 1;
                } else if (o1.getEventHistory().getEventDate() == null) {
                    return -1;
                } else {
                    return (o1.getEventHistory().getEventDate().compareTo(o2.getEventHistory().getEventDate()) > 0 ? -1 : 1);
                }
            }
        };
    }

}
