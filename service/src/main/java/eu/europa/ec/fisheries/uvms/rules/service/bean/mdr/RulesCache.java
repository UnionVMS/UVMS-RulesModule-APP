package eu.europa.ec.fisheries.uvms.rules.service.bean.mdr;


import eu.europa.ec.fisheries.uvms.rules.dao.RulesDao;
import eu.europa.ec.fisheries.uvms.rules.entity.Template;
import eu.europa.ec.fisheries.uvms.rules.exception.DaoException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Startup
@Slf4j
public class RulesCache {

    @EJB
    private RulesDao rulesDao;

    // Each rule (BR_ID) has a mapping to one or more contexts.. Map<BR_ID, List<Context>>
    private Map<String, List<String>> rulesContexts;

    @PostConstruct
    public void loadAllRuleContexts() {
        log.info("Loading rules contexts..");
        rulesContexts = new ConcurrentHashMap<>();
        List<Template> allTemplates;
        try {
            allTemplates = rulesDao.getAllFactTemplates();
        } catch (DaoException e) {
            log.error("Error while trying to fetch the rules from the db!!!");
            throw new RuntimeException("Failure to load rules! I rules table accessible?!");
        }
        allTemplates.forEach(template -> {
            template.getFactRules().forEach(rule -> {
                String brId = rule.getBrId();
                rule.getRuleContextExpressionList().forEach(ruleContextExpression -> {
                    String context = ruleContextExpression.getContext();
                    if(rulesContexts.get(brId) != null){
                        rulesContexts.get(brId).add(context);
                    } else {
                        List<String> newContextList = new ArrayList<>();
                        newContextList.add(context);
                        rulesContexts.put(brId, newContextList);
                    }
                });
            });
        });
        log.info("Finished Loading rules contexts..");
    }

    public Map<String, List<String>> getRulesContexts() {
        return rulesContexts;
    }
}
