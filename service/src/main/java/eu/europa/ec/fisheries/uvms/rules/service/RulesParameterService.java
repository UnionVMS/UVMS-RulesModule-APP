package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.uvms.rules.service.config.ParameterKey;
import eu.europa.ec.fisheries.uvms.rules.service.exception.ServiceException;
import javax.ejb.Local;

@Local
public interface RulesParameterService {

    public String getStringValue(ParameterKey key) throws ServiceException;

    public Boolean getBooleanValue(ParameterKey key) throws ServiceException;

}
