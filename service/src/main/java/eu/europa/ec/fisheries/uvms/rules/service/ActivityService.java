package eu.europa.ec.fisheries.uvms.rules.service;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripResponse;

import javax.ejb.Local;

@Local
public interface ActivityService {

    Optional<FishingTripResponse> getFishingTrip(String fishingTripID);
}
