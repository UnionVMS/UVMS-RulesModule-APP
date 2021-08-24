package eu.europa.ec.fisheries.uvms.rules.dao.bean;

import eu.europa.ec.fisheries.uvms.rules.constant.UvmsConstants;
import eu.europa.ec.fisheries.uvms.rules.dao.ResponseMessageRuleDao;
import eu.europa.ec.fisheries.uvms.rules.entity.ResponseMessageRule;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;


@Stateless
public class ResponseMessageRuleDaoBean  implements ResponseMessageRuleDao {

    @PersistenceContext(unitName = "rulesPostgresPU")
    public EntityManager em;

    @Override
    public List<ResponseMessageRule> findAll() {
       TypedQuery<ResponseMessageRule> query = em.createNamedQuery(UvmsConstants.RESPONSEMESSAGERULE_FIND_ALL, ResponseMessageRule.class);
        return  query.getResultList();
    }
}