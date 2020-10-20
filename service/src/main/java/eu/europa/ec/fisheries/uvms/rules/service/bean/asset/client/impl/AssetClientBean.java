package eu.europa.ec.fisheries.uvms.rules.service.bean.asset.client.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.service.bean.asset.gateway.AssetGateway;
import eu.europa.ec.fisheries.uvms.rules.service.business.VesselTransportMeansDto;
import eu.europa.ec.fisheries.wsdl.asset.group.AssetGroup;
import eu.europa.ec.fisheries.wsdl.asset.module.GetAssetModuleRequest;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.AssetListQuery;
import eu.europa.ec.fisheries.wsdl.asset.types.BatchAssetListResponseElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselCountry;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

@Slf4j
@ApplicationScoped
public class AssetClientBean {

    @Inject
    private AssetGateway assetGateway;

    public boolean isCFRInFleetUnderFlagStateOnLandingDate(String cfr, String flagState, DateTime landingDate) {
        try {
            log.info("Find history of asset by CFR: {} ", cfr);
            List<Asset> assetHistories = assetGateway.findHistoryOfAssetByCfr(cfr);
            Optional<Asset> historyOnDate = findAssetHistoryByDate(landingDate.toDate(), assetHistories);

            if (!historyOnDate.isPresent()) {
                // when there's no entry for the specified date, return false
                return false;
            }

            Asset asset = historyOnDate.get();
            return asset.getCountryCode().equals(flagState);

        } catch ( Exception  e) {
            log.error(e.getMessage(), e);
        }
        return true;
    }

    public List<VesselTransportMeansDto> findHistoryOfAssetBy(List<FAReportDocument> faReportDocuments) {
        List<VesselTransportMeansDto> vesselTransportMeansFactCollectedList = new ArrayList<>();
        for (FAReportDocument faReportDocument : faReportDocuments) {
            collectAndMap(vesselTransportMeansFactCollectedList, faReportDocument);
        }
        for (VesselTransportMeansDto vesselTransportMeansDto : vesselTransportMeansFactCollectedList) {
            String reportCreationDateTime = vesselTransportMeansDto.getReportCreationDateTime();
            String reportDate = DateUtils.END_OF_TIME.toString();
            if (StringUtils.isNotEmpty(reportCreationDateTime)) {
                reportDate = reportCreationDateTime;
            }
            String regCountry = vesselTransportMeansDto.getRegistrationVesselCountry();
            Map<String, String> ids = vesselTransportMeansDto.getIds();
            String cfr = ids.get("CFR");
            String ircs = ids.get("IRCS");
            String extMark = ids.get("EXT_MARK");
            String iccat = ids.get("ICCAT");
            String uvi = ids.get("UVI");
            log.debug("Find history of asset by reportDate: {}, cfr: {}, regCountry: {}, ircs: {}, extMark: {}, iccat: {}, uvi: {}", reportDate, cfr, regCountry, ircs, extMark, iccat, uvi);
            List<Asset> assets = assetGateway.findHistoryOfAssetBy(reportDate, cfr, regCountry, ircs, extMark, iccat, uvi);
            if (CollectionUtils.isNotEmpty(assets)) {
                vesselTransportMeansDto.setAsset(assets.get(0));
            }
            if (StringUtils.isNotEmpty(cfr)) {
                List<Asset> assetsByCfr = assetGateway.findHistoryOfAssetBy(reportDate, cfr, regCountry, null,null, null, null);
                if (CollectionUtils.isNotEmpty(assetsByCfr)) {
                    vesselTransportMeansDto.setAssetsByCfr(assetsByCfr.get(0));
                }
            }
            if (StringUtils.isNotEmpty(ircs) && StringUtils.isNotEmpty(extMark)){
                List<Asset> assetsByIrcsAndExtMark = assetGateway.findHistoryOfAssetBy(reportDate, null, regCountry, ircs, extMark, null, null);
                if (CollectionUtils.isNotEmpty(assetsByIrcsAndExtMark)) {
                    vesselTransportMeansDto.setAssetsByIrcsAndExtMark(assetsByIrcsAndExtMark.get(0));
                }
            }
            if (StringUtils.isNotEmpty(uvi)){
                List<Asset> assetsByUvi = assetGateway.findHistoryOfAssetBy(reportDate, null, regCountry, null,null, null, uvi);
                if (CollectionUtils.isNotEmpty(assetsByUvi)) {
                    vesselTransportMeansDto.setAssetsByUvi(assetsByUvi.get(0));
                }
            }
        }
        return vesselTransportMeansFactCollectedList;
    }

    private void collectAndMap(List<VesselTransportMeansDto> vesselTransportMeansFactCollectedList, FAReportDocument faReportDocument) {
        List<FishingActivity> specifiedFishingActivities = faReportDocument.getSpecifiedFishingActivities();
        for (FishingActivity specifiedFishingActivity : specifiedFishingActivities) {
            List<FishingActivity> relatedFishingActivities = specifiedFishingActivity.getRelatedFishingActivities();
            if (CollectionUtils.isNotEmpty(relatedFishingActivities)) {
                for (FishingActivity relatedFishingActivity : relatedFishingActivities) {
                    List<VesselTransportMeans> relatedVesselTransportMeans2 = relatedFishingActivity.getRelatedVesselTransportMeans();
                    if (CollectionUtils.isNotEmpty(relatedVesselTransportMeans2)) {
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
        if (transportMeans != null) {
            VesselTransportMeansDto relatedMeansDto = new VesselTransportMeansDto();
            setReportCreationDateTime(faReportDocument, relatedMeansDto);

            List<IDType> relatedIds = transportMeans.getIDS();
            for (IDType id : relatedIds) {
                relatedMeansDto.getIds().put(id.getSchemeID(), id.getValue());
            }
            VesselCountry country = transportMeans.getRegistrationVesselCountry();
            if (country != null && country.getID() != null) {
                relatedMeansDto.setRegistrationVesselCountry(country.getID().getValue());
            }
            vesselTransportMeansFactCollectedList.add(relatedMeansDto);
        }
    }

    private void setReportCreationDateTime(FAReportDocument faReportDocument, VesselTransportMeansDto vesselTransportMeansFactCollected) {
        if (faReportDocument != null && faReportDocument.getRelatedFLUXReportDocument() != null) {
            DateTimeType creationDateTime = faReportDocument.getRelatedFLUXReportDocument().getCreationDateTime();
            XMLGregorianCalendar dateTime = creationDateTime.getDateTime();
            if (dateTime != null) {
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

        return Optional.ofNullable(historyOnDate);
    }
    
    protected Comparator<Asset> assetComparator() {
        return (o1, o2) -> {
            if (o1.getEventHistory().getEventDate() == null && o2.getEventHistory().getEventDate() == null) {
                return 0;
            } else if (o2.getEventHistory().getEventDate() == null) {
                return 1;
            } else if (o1.getEventHistory().getEventDate() == null) {
                return -1;
            } else {
                return (o1.getEventHistory().getEventDate().compareTo(o2.getEventHistory().getEventDate()) > 0 ? -1 : 1);
            }
        };
    }

    public List<BatchAssetListResponseElement> getAssetListBatch(List<AssetListQuery> assetBatchRequest) {
        return assetGateway.getAssetListBatch(assetBatchRequest);
    }

    public Asset getAsset(GetAssetModuleRequest getAssetModuleRequest) {
        return assetGateway.getAsset(getAssetModuleRequest);
    }

    public List<AssetGroup> getAssetGroupListByAssetGuid(String assetGuid) {
        return assetGateway.getAssetGroupListByAssetGuid(assetGuid);
    }
}
