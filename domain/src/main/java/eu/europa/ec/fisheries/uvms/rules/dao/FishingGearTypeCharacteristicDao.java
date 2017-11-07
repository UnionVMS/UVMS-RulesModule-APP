package eu.europa.ec.fisheries.uvms.rules.dao;

import eu.europa.ec.fisheries.uvms.rules.entity.FishingGearTypeCharacteristic;
import eu.europa.ec.fisheries.uvms.commons.service.dao.AbstractDAO;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class FishingGearTypeCharacteristicDao extends AbstractDAO<FishingGearTypeCharacteristic> {
    private EntityManager em;

    public FishingGearTypeCharacteristicDao(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<String> getFishingGearCharacteristicCodes(String fishingGearTypeCode) throws ServiceException {
        Query query = getEntityManager().createNamedQuery(FishingGearTypeCharacteristic.GET_ALL_CHARACTERISTIC_CODES_BY_TYPE_CODE);
        query.setParameter("fishingGearTypeCode", fishingGearTypeCode);
        return query.getResultList();
    }

    public List<String> getFishingGearCharacteristicCodes(String fishingGearTypeCode, boolean onlyMandatory) throws ServiceException {
        Query query = getEntityManager().createNamedQuery(FishingGearTypeCharacteristic.GET_ALL_CHARACTERISTIC_CODES_BY_TYPE_CODE_AND_MANDATORY);
        query.setParameter("fishingGearTypeCode", fishingGearTypeCode);
        query.setParameter("mandatory", onlyMandatory);
        return query.getResultList();
    }

    public List<String> getAllFishingGearTypeCodes() throws ServiceException {
        Query query = getEntityManager().createNamedQuery(FishingGearTypeCharacteristic.GET_ALL_TYPE_CODES);
        return query.getResultList();
    }
}
