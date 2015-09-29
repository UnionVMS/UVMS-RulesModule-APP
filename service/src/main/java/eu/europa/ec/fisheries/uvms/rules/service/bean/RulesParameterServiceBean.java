package eu.europa.ec.fisheries.uvms.rules.service.bean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.rules.service.RulesParameterService;
import eu.europa.ec.fisheries.uvms.rules.service.config.ParameterKey;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.entity.Parameter;
import eu.europa.ec.fisheries.uvms.rules.service.exception.InputArgumentException;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;

@Stateless
public class RulesParameterServiceBean implements RulesParameterService {

    final static Logger LOG = LoggerFactory.getLogger(RulesParameterServiceBean.class);

    @PersistenceContext(unitName = "internalPU")
    EntityManager em;

    @Override
    public String getStringValue(ParameterKey key) throws RulesServiceException {
        try {
            Query query = em.createNamedQuery(ServiceConstants.FIND_BY_NAME);
            query.setParameter("key", key.getKey());
            Parameter entity = (Parameter) query.getSingleResult();
            return entity.getParamValue();
        } catch (Exception ex) {
            LOG.error("[ Error when getting String value ]", ex.getMessage());
            throw new RulesServiceException("[ Error when getting String value ]", ex);
        }
    }

    @Override
    public Boolean getBooleanValue(ParameterKey key) throws RulesServiceException {
        try {
            Query query = em.createNamedQuery(ServiceConstants.FIND_BY_NAME);
            query.setParameter("key", key.getKey());
            Parameter entity = (Parameter) query.getSingleResult();
            return parseBooleanValue(entity.getParamValue());
        } catch (RulesServiceException ex) {
            LOG.error("[ Error when getting Boolean value ]", ex.getMessage());
            throw new RulesServiceException("[ Error when getting Boolean value ]", ex);
        }
    }

    private Boolean parseBooleanValue(String value) throws InputArgumentException, RulesServiceException {
        try {
            if (value.equalsIgnoreCase("true")) {
                return Boolean.TRUE;
            } else if (value.equalsIgnoreCase("false")) {
                return Boolean.FALSE;
            } else {
                LOG.error("[ Error when parsing Boolean value from String, The String provided dows not equal 'TRUE' or 'FALSE'. The value is {} ]",
                        value);
                throw new InputArgumentException("The String value provided does not equal boolean value, value provided = " + value);
            }
        } catch (Exception ex) {
            LOG.error("[ Error when parsing Boolean value from String ]", ex.getMessage());
            throw new RulesServiceException("[ Error when parsing Boolean value from String ]", ex);
        }
    }

}
