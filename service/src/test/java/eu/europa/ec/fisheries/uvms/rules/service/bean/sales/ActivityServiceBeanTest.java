package eu.europa.ec.fisheries.uvms.rules.service.bean.sales;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.activity.model.schemas.FishingTripResponse;
import eu.europa.ec.fisheries.uvms.rules.service.bean.sales.helper.ActivityServiceBeanHelper;
import javax.jms.JMSException;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ActivityServiceBeanTest {

    @InjectMocks
    private ActivityServiceBean service;

    @Mock
    private ActivityServiceBeanHelper helper;

    @Test
    @SneakyThrows
    public void getFishingTripRequestWhenNoResultFound() {
        String fishingTripID = "fishingTripID";
        doReturn(Optional.absent()).when(helper).findTrip(fishingTripID);
        Optional<FishingTripResponse> response = service.getFishingTrip(fishingTripID);
        assertEquals(Optional.absent(), response);
        verify(helper).findTrip(fishingTripID);
        verifyNoMoreInteractions(helper);
    }

    @Test
    @SneakyThrows
    public void getFishingTripRequestWhenAResultWasFound() {
        String fishingTripID = "fishingTripID";
        FishingTripResponse fishingTripResponse = new FishingTripResponse();
        doReturn(Optional.of(fishingTripResponse)).when(helper).findTrip(fishingTripID);
        Optional<FishingTripResponse> response = service.getFishingTrip(fishingTripID);
        assertSame(fishingTripResponse, response.get());
        verify(helper).findTrip(fishingTripID);
        verifyNoMoreInteractions(helper);
    }

    @Test
    @SneakyThrows
    public void getFishingTripRequestWhenExceptionWasThrown() {
        String fishingTripID = "fishingTripID";
        doThrow(JMSException.class).when(helper).findTrip(fishingTripID);
        Optional<FishingTripResponse> response = service.getFishingTrip(fishingTripID);
        verify(helper).findTrip(fishingTripID);
        verifyNoMoreInteractions(helper);
        assertFalse(response.isPresent());
    }

}