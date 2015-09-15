package eu.europa.ec.fisheries.uvms.rules.service.business;

import java.util.List;

import javax.ejb.Stateless;

import eu.europa.ec.fisheries.uvms.rules.service.entity.CustomRule;

@Stateless
public interface CustomRuleDao {

    public List<CustomRule> getCustomRules();

}
