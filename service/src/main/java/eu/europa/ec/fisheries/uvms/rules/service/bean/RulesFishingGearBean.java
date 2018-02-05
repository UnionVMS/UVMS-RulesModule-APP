package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.entity.FishingGearTypeCharacteristic;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
@Slf4j
public class RulesFishingGearBean {
    @EJB
    private RulesDao rulesDao;

    public List<FishingGearTypeCharacteristic> getAllFishingGearTypeCharacteristics() {
        List<FishingGearTypeCharacteristic> fishingGearTypeCharacteristics;
        try {
            fishingGearTypeCharacteristics = rulesDao.getAllFishingGearTypeCharacteristics();
        } catch (DaoException e) {
            log.error("Error retrieving FishingGearTypeCharacteristics!", e);
            return new ArrayList<>();
        }
        return fishingGearTypeCharacteristics;
    }


}
