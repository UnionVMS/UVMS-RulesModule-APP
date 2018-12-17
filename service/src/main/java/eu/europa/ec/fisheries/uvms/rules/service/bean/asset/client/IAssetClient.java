package eu.europa.ec.fisheries.uvms.rules.service.bean.asset.client;

import eu.europa.ec.fisheries.uvms.rules.service.business.VesselTransportMeansDto;
import eu.europa.ec.fisheries.wsdl.asset.types.Asset;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;

import javax.ejb.Local;
import java.util.List;

/**
 * This shouldn't  exist since it call AssetFacade bean, that has the same methods.
 * TODO: CHECK 
 *
 */
@Local
public interface IAssetClient {

    boolean isCFRInFleetUnderFlagStateOnLandingDate(String cfr, String flagState, DateTime landingDate);
    List<VesselTransportMeansDto> findHistoryOfAssetBy(List<FAReportDocument> faReportDocuments);

}
