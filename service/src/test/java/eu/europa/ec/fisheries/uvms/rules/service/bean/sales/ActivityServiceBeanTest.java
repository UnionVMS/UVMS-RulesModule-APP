package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripResponse;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper.ActivityServiceBeanHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.google.common.base.Optional;

import javax.jms.JMSException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ActivityServiceBeanTest {

    @InjectMocks
    private ActivityServiceBean service;

    @Mock
    private ActivityServiceBeanHelper helper;

    @Test
    public void getFishingTripRequestWhenNoResultFound() throws Exception {
        String fishingTripID = "fishingTripID";

        doReturn(Optional.absent()).when(helper).findTrip(fishingTripID);

        Optional<FishingTripResponse> response = service.getFishingTrip(fishingTripID);

        assertEquals(Optional.absent(), response);
        verify(helper).findTrip(fishingTripID);
        verifyNoMoreInteractions(helper);
    }

    @Test
    public void getFishingTripRequestWhenAResultWasFound() throws Exception {
        String fishingTripID = "fishingTripID";

        FishingTripResponse fishingTripResponse = new FishingTripResponse();
        doReturn(Optional.of(fishingTripResponse)).when(helper).findTrip(fishingTripID);

        Optional<FishingTripResponse> response = service.getFishingTrip(fishingTripID);

        assertSame(fishingTripResponse, response.get());
        verify(helper).findTrip(fishingTripID);
        verifyNoMoreInteractions(helper);
    }

    @Test
    public void getFishingTripRequestWhenExceptionWasThrown() throws Exception {
        String fishingTripID = "fishingTripID";

        doThrow(JMSException.class).when(helper).findTrip(fishingTripID);

        Optional<FishingTripResponse> response = service.getFishingTrip(fishingTripID);


        verify(helper).findTrip(fishingTripID);
        verifyNoMoreInteractions(helper);
        assertFalse(response.isPresent());
    }

}