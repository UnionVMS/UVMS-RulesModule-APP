package eu.europa.ec.fisheries.uvms.rules.service;

import eu.europa.ec.fisheries.uvms.rules.service.config.ParameterKey;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import javax.ejb.Local;

@Local
public interface RulesParameterService {

    public String getStringValue(ParameterKey key) throws RulesServiceException;

    public Boolean getBooleanValue(ParameterKey key) throws RulesServiceException;

}
