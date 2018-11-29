package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import com.google.common.base.Stopwatch;
import eu.europa.ec.fisheries.uvms.asset.client.AssetClient;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetDTO;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetIdentifier;
import eu.europa.ec.fisheries.uvms.asset.client.model.AssetQuery;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.rules.service.AssetService;
import eu.europa.ec.fisheries.uvms.rules.service.business.VesselTransportMeansDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FishingActivity;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselCountry;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.VesselTransportMeans;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
@Slf4j
public class RulesAssetServiceBean implements AssetService {

    @EJB
    private AssetClient assetClient;

    @Override
    public boolean isCFRInFleetUnderFlagStateOnLandingDate(String cfr, String flagState, DateTime landingDate) {
        OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(landingDate.toDate().toInstant(), ZoneId.systemDefault());
        AssetDTO asset = assetClient.getAssetFromAssetIdAndDate(AssetIdentifier.CFR, cfr, offsetDateTime);
        return asset != null && flagState.equals(asset.getFlagStateCode());
    }

    public List<VesselTransportMeansDto> findHistoryOfAssetBy(List<FAReportDocument> faReportDocuments) {
        log.info("-->>>> Calling Assets....");
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<VesselTransportMeansDto> vesselTransportMeansFactCollectedList = new ArrayList<>();
        for (FAReportDocument faReportDocument : faReportDocuments) {
            collectAndMap(vesselTransportMeansFactCollectedList, faReportDocument);
        }
        for (VesselTransportMeansDto vesselTransportMeansDto : vesselTransportMeansFactCollectedList) {
            Map<String, String> ids = vesselTransportMeansDto.getIds();
            AssetQuery assetQuery = createAssetQuery(vesselTransportMeansDto.getReportCreationDateTime(), ids.get("CFR"),
                    vesselTransportMeansDto.getRegistrationVesselCountry(), ids.get("IRCS"), ids.get("EXT_MARK"), ids.get("ICCAT"));
            List<AssetDTO> assetList = assetQuery != null ? assetClient.getAssetList(assetQuery) : null;
            if (CollectionUtils.isNotEmpty(assetList)) {
                // FIXME Instead of newAsset we should change the asset list in vesselTransportMeansDto!!!
                vesselTransportMeansDto.setAsset(assetList.get(0));
            }
        }
        log.info("-->>>> Assets responded in [{}] milliseconds!", stopwatch.stop().elapsed(TimeUnit.MILLISECONDS));
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

    Optional<AssetDTO> findAssetHistoryByDate(Date landingDate, List<AssetDTO> assetHistories) {
        // Asset module sorts this list already, but we do it again here just to be sure
        assetHistories.sort(assetComparator());
        AssetDTO historyOnDate = null;
        // Because our list is ordered from newest event to oldest, we can simply check if the date of the event is before the landing date
        for (AssetDTO asset : assetHistories) {
            if (asset.getUpdateTime() != null && asset.getUpdateTime().toInstant().isBefore(landingDate.toInstant())) {
                historyOnDate = asset;
                break;
            }
        }
        return Optional.ofNullable(historyOnDate);
    }


    private static AssetQuery createAssetQuery(String reportDate, String cfr, String flagState, String ircs, String extMark, String iccat) {
        AssetQuery assetQuery = new AssetQuery();
        if (StringUtils.isNotEmpty(reportDate)) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withOffsetParsed();
            Date dateTime = formatter.withZoneUTC().parseDateTime(reportDate).toGregorianCalendar().getTime();
            assetQuery.setDate(dateTime.toInstant());
        } else {
            return null;
        }
        if (StringUtils.isNotEmpty(cfr)) {
            assetQuery.setCfr(Collections.singletonList(cfr));
        } else {
            if (StringUtils.isNotEmpty(flagState)) {
                assetQuery.setFlagState(Collections.singletonList(flagState));
            } else {
                return null;
            }
            if (StringUtils.isNotEmpty(ircs)) {
                assetQuery.setIrcs(Collections.singletonList(ircs));
            } else if (StringUtils.isNotEmpty(extMark)) {
                assetQuery.setExternalMarking(Collections.singletonList(extMark));
            } else if (StringUtils.isNotEmpty(iccat)) {
                assetQuery.setIccat(Collections.singletonList(iccat));
            } else {
                return null;
            }
        }
        return assetQuery;
    }

    Comparator<AssetDTO> assetComparator() {
        return (o1, o2) -> {
            if (o1.getUpdateTime() == null && o2.getUpdateTime() == null) {
                return 0;
            } else if (o2.getUpdateTime() == null) {
                return 1;
            } else if (o1.getUpdateTime() == null) {
                return -1;
            } else {
                return (o1.getUpdateTime().compareTo(o2.getUpdateTime()) > 0 ? -1 : 1);
            }
        };
    }

}
