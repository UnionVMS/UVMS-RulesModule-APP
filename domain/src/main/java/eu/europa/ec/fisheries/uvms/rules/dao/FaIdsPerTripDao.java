package eu.europa.ec.fisheries.uvms.rules.dao;

import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.rules.entity.FaIdsPerTrip;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class FaIdsPerTripDao extends AbstractDAO<FaIdsPerTrip> {

    private EntityManager em;

    public FaIdsPerTripDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<FaIdsPerTrip> loadFADocumentIDByIdsByIds(List<String> inList) {
        Query query = getEntityManager().createNamedQuery(FaIdsPerTrip.BY_TRIPIDSCHEMEIDFATYPEREPORTTYPE);
        query.setParameter("idsList", inList);
        return query.getResultList();
    }

}
