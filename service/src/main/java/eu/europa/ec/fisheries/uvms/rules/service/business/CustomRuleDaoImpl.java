package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.entity.CustomRule;

@Stateless
public class CustomRuleDaoImpl implements CustomRuleDao {

    final static Logger LOG = LoggerFactory.getLogger(CustomRuleDaoImpl.class);

    @PersistenceContext(unitName = "internalPU")
    EntityManager em;

    @Override
    public List<CustomRule> getCustomRules() {
        List<CustomRule> rules = new ArrayList<CustomRule>();

        try {
            TypedQuery<CustomRule> query = em.createNamedQuery(ServiceConstants.FIND_ALL_CUSTOM_RULES, CustomRule.class);
            rules = query.getResultList();
        } catch (NoResultException e) {
            LOG.debug("No rules found");
        }

        return rules;
    }
}
