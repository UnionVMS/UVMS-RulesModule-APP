package eu.europa.ec.fisheries.uvms.rules.service;

import javax.ejb.Local;
import java.util.List;

import eu.europa.ec.fisheries.uvms.rules.service.business.VesselTransportMeansDto;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;

@Local
public interface AssetService {

    boolean isCFRInFleetUnderFlagStateOnLandingDate(String cfr, String flagState, DateTime date);

    List<VesselTransportMeansDto> findHistoryOfAssetBy(List<FAReportDocument> faReportDocuments);

}
