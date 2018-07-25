package eu.europa.ec.fisheries.uvms.rules.service;

import javax.ejb.Local;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripResponse;

@Local
public interface ActivityService {

    Optional<FishingTripResponse> getFishingTrip(String fishingTripID);
}
