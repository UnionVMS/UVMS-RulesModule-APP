package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.asset.model.exception.AssetModelMarshallException;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.service.AssetService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper.AssetServiceBeanHelper;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Singleton
@Slf4j
public class AssetServiceBean implements AssetService {

    @EJB
    private AssetServiceBeanHelper helper;

    @Override
    public boolean isCFRInFleetUnderFlagStateOnLandingDate(String cfr, String flagState, DateTime landingDate) {
//        try {
//            List<Asset> assetHistories = helper.findHistoryOfAssetByCfr(cfr);
//
//            Optional<Asset> historyOnDate = findAssetHistoryByDate(landingDate.toDate(), assetHistories);
//
//            if (!historyOnDate.isPresent()) {
//                // when there's no entry for the specified date, return false
//                return false;
//            }
//
//            Asset asset = historyOnDate.get();
//            return asset.getCountryCode().equals(flagState);
//
//        } catch (MessageException | AssetModelMarshallException e) {
//            e.printStackTrace();
//        }
        return true;
    }
//
//    protected Optional<Asset> findAssetHistoryByDate(Date landingDate, List<Asset> assetHistories) {
//        // Asset module sorts this list already, but we do it again here just to be sure
//        assetHistories.sort(assetComparator());
//
//        Asset historyOnDate = null;
//
//        // Because our list is ordered from newest event to oldest,
//        // we can simply check if the date of the event is before the landing date
//        for (Asset assetHistory : assetHistories) {
//            if (assetHistory.getEventHistory().getEventDate().before(landingDate)) {
//                historyOnDate = assetHistory;
//                break;
//            }
//        }
//
//        return Optional.fromNullable(historyOnDate);
//    }
//
//
//    protected Comparator<Asset> assetComparator() {
//        return new Comparator<Asset>() {
//            @Override
//            public int compare(Asset o1, Asset o2) {
//                if (o1.getEventHistory().getEventDate() == null && o2.getEventHistory().getEventDate() == null) {
//                    return 0;
//                } else if (o2.getEventHistory().getEventDate() == null) {
//                    return 1;
//                } else if (o1.getEventHistory().getEventDate() == null) {
//                    return -1;
//                } else {
//                    return (o1.getEventHistory().getEventDate().compareTo(o2.getEventHistory().getEventDate()) > 0 ? -1 : 1);
//                }
//            }
//        };
//    }

}
