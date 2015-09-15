package eu.europa.ec.fisheries.uvms.rules.service.business;

import javax.ejb.Stateless;

@Stateless
public interface CustomEventDao {

    public void createCustomEvent(PositionEvent pe, String action);

}
