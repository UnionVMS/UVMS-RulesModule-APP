package eu.europa.ec.fisheries.uvms.rules.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.rules.entity.FaIdsPerTrip;
import org.apache.commons.collections.CollectionUtils;

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
        List<FaIdsPerTrip> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(inList)){
            Query query = getEntityManager().createNamedQuery(FaIdsPerTrip.BY_TRIPIDSCHEMEIDFATYPEREPORTTYPE);
            query.setParameter("idsList", inList);
            list = query.getResultList();
        }
        return list;
    }

}
