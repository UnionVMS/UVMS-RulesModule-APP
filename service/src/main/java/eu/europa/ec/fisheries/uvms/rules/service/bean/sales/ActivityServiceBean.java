package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.activity.model.exception.ActivityModelMarshallException;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripResponse;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.rules.service.ActivityService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper.ActivityServiceBeanHelper;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.jms.JMSException;

@Singleton
@Slf4j
public class ActivityServiceBean implements ActivityService {

    @EJB
    private ActivityServiceBeanHelper helper;

    @Override
    public Optional<FishingTripResponse> getFishingTrip(String fishingTripID) {
        Optional<FishingTripResponse> trip = Optional.absent();
        try {
            trip = helper.findTrip(fishingTripID);
        } catch (MessageException | ActivityModelMarshallException | JMSException | SalesMarshallException e) {
            log.warn("Couldn't query FA for fishing trip with id " + fishingTripID, e);
        }
        return trip;
    }
}
