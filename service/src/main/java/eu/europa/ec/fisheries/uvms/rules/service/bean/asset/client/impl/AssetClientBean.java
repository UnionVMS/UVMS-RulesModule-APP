package eu.europa.ec.fisheries.uvms.rules.service.bean.asset.client.impl;

import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.asset.ejb.client.IAssetFacade;
import eu.europa.ec.fisheries.uvms.commons.date.DateUtils;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.bean.RulesResponseConsumerBean;
import eu.europa.ec.fisheries.uvms.rules.message.producer.bean.RulesAssetProducerBean;
import eu.europa.ec.fisheries.uvms.rules.service.bean.asset.client.IAssetClient;
import eu.europa.ec.fisheries.uvms.rules.service.business.VesselTransportMeansDto;
import eu.europa.ec.fisheries.wsdl.asset.module.AssetModuleMethod;
import eu.europa.ec.fisheries.wsdl.asset.module.FindHistoryOfAssetByCfrFacadeRequest;
import eu.europa.ec.fisheries.wsdl.asset.module.FindHistoryOfAssetFacadeRequest;
import eu.europa.ec.fisheries.wsdl.asset.module.FindHistoryOfAssetFacadeResponse;
import eu.europa.ec.fisheries.wsdl.asset.module.FindVesselIdsByAssetHistGuidResponse;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import eu.europa.ec.fisheries.wsdl.asset.types.FindHistoryOfAssetFacadeCriteria;
import eu.europa.ec.fisheries.wsdl.asset.types.FindHistoryOfAssetFacadeRequestElement;
import eu.europa.ec.fisheries.wsdl.asset.types.FindHistoryOfAssetFacadeResponseElement;
import lombok.Getter;
import lombok.Setter;
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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.TextMessage;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Stateless
public class AssetClientBean implements IAssetClient {

    @Inject
    private RulesAssetProducerBean rulesAssetProducerBean;

    @Inject
    private RulesResponseConsumerBean rulesResponseConsumerBean;

    public boolean isCFRInFleetUnderFlagStateOnLandingDate(String cfr, String flagState, DateTime landingDate) {
        try {
            log.info("Find history of asset by CFR: {} ", cfr);
            List<Asset> assetHistories = getHistoryOfAssetByCfrFromAssetModule(cfr);
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

    private List<Asset> getHistoryOfAssetByCfrFromAssetModule(String cfr) {
        FindHistoryOfAssetByCfrFacadeRequest request = new FindHistoryOfAssetByCfrFacadeRequest();
        request.setMethod(AssetModuleMethod.FIND_HISTORY_OF_ASSET_BY_CFR_FACADE);
        request.getCfrIds().add(cfr);
        List<Asset> assets = new ArrayList<>();
        try {
            String correlationId = rulesAssetProducerBean.sendMessageToSpecificQueue(JAXBMarshaller.marshallJaxBObjectToString(request), rulesAssetProducerBean.getDestination(), rulesResponseConsumerBean.getDestination());
            if(correlationId != null) {
                TextMessage textMessage = rulesResponseConsumerBean.getMessage(correlationId, TextMessage.class);
                FindHistoryOfAssetFacadeResponse response = JAXBMarshaller.unmarshallTextMessage(textMessage, FindHistoryOfAssetFacadeResponse.class);
                if(response != null) {
                    FindHistoryOfAssetFacadeResponseElement result = response.getResults().stream().filter(r -> cfr.equals(r.getIdentifier())).findAny().orElse(null);
                    if(result != null) {
                        assets.addAll(result.getAssets());
                    }
                }
            }
        } catch (MessageException | ActivityModelMarshallException e) {
            e.printStackTrace();
        }
        return assets;
    }

    public List<VesselTransportMeansDto> findHistoryOfAssetBy(List<FAReportDocument> faReportDocuments) {
        List<VesselTransportMeansDto> vesselTransportMeansFactCollectedList = new ArrayList<>();
        for (FAReportDocument faReportDocument : faReportDocuments) {
            collectAndMap(vesselTransportMeansFactCollectedList, faReportDocument);
        }
        Map<String, String> vesselKeyToGeneratedUuidMap = vesselTransportMeansFactCollectedList.stream()
                .distinct()
                .collect(Collectors.toMap(this::makeVesselTransportMeansDtoKey, x -> UUID.randomUUID().toString()));
        List<FindHistoryOfAssetFacadeRequestElement> requestElements = vesselTransportMeansFactCollectedList.stream()
                .distinct()
                .map(dto -> {
                    String reportDate = StringUtils.isNotEmpty(dto.getReportCreationDateTime()) ?
                            dto.getReportCreationDateTime()
                            : DateUtils.END_OF_TIME.toString();
                    return createFindHistoryOfAssetFacadeRequestElement(
                            reportDate,
                            dto.getRegistrationVesselCountry(),
                            dto.getIds().get("CFR"),
                            dto.getIds().get("IRCS"),
                            dto.getIds().get("EXT_MARK"),
                            dto.getIds().get("ICCAT"),
                            vesselKeyToGeneratedUuidMap.get(makeVesselTransportMeansDtoKey(dto))
                    );
                })
                .collect(Collectors.toList());

        Map<String, List<Asset>> resultsByUuid = getHistoryOfAssetsFromAssetModule(requestElements).stream()
                .collect(Collectors.toMap(FindHistoryOfAssetFacadeResponseElement::getIdentifier, FindHistoryOfAssetFacadeResponseElement::getAssets));

        for (VesselTransportMeansDto dto : vesselTransportMeansFactCollectedList) {
            Optional.ofNullable(resultsByUuid.get(vesselKeyToGeneratedUuidMap.get(makeVesselTransportMeansDtoKey(dto))))
                    .map(Collection::stream)
                    .flatMap(Stream::findFirst)
                    .ifPresent(dto::setAsset);

        }
        return vesselTransportMeansFactCollectedList;
    }

    /**
     * Used by {@link #findHistoryOfAssetBy(List)} to create a key from a {@code VesselTransportMeansDto}
     * that will not change after assigning the {@code Asset}.
     */
    private String makeVesselTransportMeansDtoKey(VesselTransportMeansDto dto) {
        return "{reportCreationDateTime='" + dto.getReportCreationDateTime() + '\'' + ", ids=" + dto.getIds() + ", registrationVesselCountry='" + dto.getRegistrationVesselCountry() + "'}";
    }

    private FindHistoryOfAssetFacadeRequestElement createFindHistoryOfAssetFacadeRequestElement(String reportDate, String regCountry, String cfr, String ircs, String extMark, String iccat, String uuid) {
        FindHistoryOfAssetFacadeRequestElement element = new FindHistoryOfAssetFacadeRequestElement();
        element.setId(uuid);
        FindHistoryOfAssetFacadeCriteria criteria = new FindHistoryOfAssetFacadeCriteria();
        criteria.setReportDate(reportDate);
        criteria.setCfr(cfr);
        criteria.setRegCountry(regCountry);
        criteria.setIrcs(ircs);
        criteria.setExtMark(extMark);
        criteria.setIccat(iccat);
        element.setCriteria(criteria);
        return element;
    }

    private List<FindHistoryOfAssetFacadeResponseElement> getHistoryOfAssetsFromAssetModule( List<FindHistoryOfAssetFacadeRequestElement> requestElements) {
        FindHistoryOfAssetFacadeRequest request = new FindHistoryOfAssetFacadeRequest();
        request.setMethod(AssetModuleMethod.FIND_HISTORY_OF_ASSET_FACADE);
        request.getCriteria().addAll(requestElements);
        List<FindHistoryOfAssetFacadeResponseElement> results = new ArrayList<>();
        try {
            String correlationId = rulesAssetProducerBean.sendMessageToSpecificQueue(JAXBMarshaller.marshallJaxBObjectToString(request), rulesAssetProducerBean.getDestination(), rulesResponseConsumerBean.getDestination());
            if(correlationId != null) {
                TextMessage textMessage = rulesResponseConsumerBean.getMessage(correlationId, TextMessage.class);
                FindHistoryOfAssetFacadeResponse response = JAXBMarshaller.unmarshallTextMessage(textMessage, FindHistoryOfAssetFacadeResponse.class);
                if(response != null) {
                    results = response.getResults();
                }
            }
        } catch (MessageException | ActivityModelMarshallException e) {
            e.printStackTrace();
        }
        return results;
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
}
