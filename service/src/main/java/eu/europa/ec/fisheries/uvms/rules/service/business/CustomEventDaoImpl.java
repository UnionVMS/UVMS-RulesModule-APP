package eu.europa.ec.fisheries.uvms.rules.service.business;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class CustomEventDaoImpl implements CustomEventDao {

    private static final String ACTION_1 = "Action 1";
    private static final String ACTION_2 = "Action 2";
    private static final String ACTION_3 = "Action 3";

    final static Logger LOG = LoggerFactory.getLogger(CustomEventDaoImpl.class);

    @PersistenceContext(unitName = "internalPU")
    EntityManager em;

    @Override
    public void createCustomEvent(PositionEvent pe, String action) {
        LOG.info("Creating custom event. NOT IMPLEMENTED");

        switch (action) {
        case ACTION_1:
            LOG.info("Reached action '{}' regarding movement guid '{}'", ACTION_1, pe.getGuid());
            break;
        case ACTION_2:
            LOG.info("Reached action '{}' regarding movement guid '{}'", ACTION_2, pe.getGuid());
            break;
        case ACTION_3:
            LOG.info("Reached action '{}' regarding movement guid '{}'", ACTION_3, pe.getGuid());
            break;
        default:
            LOG.info("The action '{}' is not defined", action);
            break;
        }

    }

}
