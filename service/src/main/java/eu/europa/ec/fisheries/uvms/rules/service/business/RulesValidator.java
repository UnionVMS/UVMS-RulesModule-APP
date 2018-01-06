/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.service.business;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Stateless;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SanityRuleType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CustomRuleParser;
import org.drools.template.parser.DefaultTemplateContainer;
import org.drools.template.parser.TemplateContainer;
import org.drools.template.parser.TemplateDataListener;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Startup
// @Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Stateless
public class RulesValidator {
    private final static Logger LOG = LoggerFactory.getLogger(RulesValidator.class);
//    private static final String SANITY_RESOURCE_DRL_FILE = "/rules/SanityRules.drl";
    private static final String CUSTOM_RULE_DRL_FILE = "src/main/resources/rules/CustomRules.drl";
    private static final String CUSTOM_RULE_TEMPLATE = "/templates/CustomRulesTemplate.drt";

    private static final String SANITY_RULES_DRL_FILE = "src/main/resources/rules/SanityRules.drl";
    private static final String SANITY_RULES_TEMPLATE = "/templates/SanityRulesTemplate.drt";

    @EJB
    ValidationService validationService;

    private KieServices kieServices;

    private KieFileSystem sanityKfs;
    private KieContainer sanityKcontainer;
    private List<SanityRuleType> currentSanityRules;

    private KieFileSystem customKfs;
    private KieContainer customKcontainer;

    //@PostConstruct
    public void init() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                initServices();
                updateSanityRules();
                updateCustomRules();
            }
        });
    }

    private void initServices() {
        kieServices = KieServices.Factory.get();
    }

    @Lock(LockType.WRITE)
    public void updateSanityRules() {
        try {
            // Fetch sanity rules from DB
            List<SanityRuleType> sanityRules = validationService.getSanityRules();
            //if (sanityRules != null && !sanityRules.isEmpty()) {
               // if (checkForChanges(sanityRules)) {
                   // currentSanityRules = sanityRules;
                    // Add sanity rules
                   // String drl = generateSanityRuleDrl(SANITY_RULES_TEMPLATE, sanityRules);

                   // sanityKfs = kieServices.newKieFileSystem();

                  //  sanityKfs.write(SANITY_RULES_DRL_FILE, drl);
                  //  kieServices.newKieBuilder(sanityKfs).buildAll();
                  //  sanityKcontainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
               // }
           // } else {
                sanityKfs = null;
                sanityKcontainer = null;

        } catch (RulesServiceException  e) {
            LOG.error("[ Error when getting sanity rules ]");
            // TODO: Throw exception???
        } catch (RulesFaultException e) {
            e.printStackTrace();
        }
    }

    @Lock(LockType.WRITE)
    public void updateCustomRules() {
        try {
            // Fetch custom rules from DB
            List<CustomRuleType> customRules = validationService.getRunnableCustomRules();
            if (customRules != null && !customRules.isEmpty()) {
                // Add custom rules
                List<CustomRuleDto> rules = CustomRuleParser.parseRules(customRules);
                String drl = generateCustomRuleDrl(CUSTOM_RULE_TEMPLATE, rules);

                customKfs = kieServices.newKieFileSystem();

                customKfs.write(CUSTOM_RULE_DRL_FILE, drl);

                // Create session
                kieServices.newKieBuilder(customKfs).buildAll();
                customKcontainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
            } else {
                customKfs = null;
                customKcontainer = null;
            }
        } catch (RulesServiceException | RulesFaultException  e) {
            LOG.error("[ Error when getting custom rules ]");
            // TODO: Throw exception???
        }
    }

    @Lock(LockType.READ)
    public void evaluate(RawMovementFact fact) {
        if (sanityKcontainer != null) {
            LOG.info("Verify sanity rules");

            KieSession ksession = sanityKcontainer.newKieSession();

            // Inject beans
            ksession.setGlobal("validationService", validationService);
            ksession.setGlobal("logger", LOG);

            ksession.insert(fact);
            ksession.fireAllRules();
        }
    }

    @Lock(LockType.READ)
    public void evaluate(MovementFact fact) {
        if (customKcontainer != null) {
            LOG.info("Verify user defined rules");

            KieSession ksession = customKcontainer.newKieSession();

            // Inject beans
            ksession.setGlobal("validationService", validationService);
            ksession.setGlobal("logger", LOG);

            ksession.insert(fact);
            ksession.fireAllRules();
        }
    }

    private String generateCustomRuleDrl(String template, List<CustomRuleDto> ruleDtos) {
        InputStream templateStream = this.getClass().getResourceAsStream(template);
        TemplateContainer tc = new DefaultTemplateContainer(templateStream);
        TemplateDataListener listener = new TemplateDataListener(tc);

        int rowNum = 0;
        for (CustomRuleDto ruleDto : ruleDtos) {
            listener.newRow(rowNum, 0);
            listener.newCell(rowNum, 0, ruleDto.getRuleName(), 0);
            listener.newCell(rowNum, 1, ruleDto.getExpression(), 0);
            listener.newCell(rowNum, 2, ruleDto.getAction(), 0);
            listener.newCell(rowNum, 3, ruleDto.getRuleGuid(), 0);
            rowNum++;
        }
        listener.finishSheet();
        String drl = listener.renderDRL();

        LOG.debug("Custom rule file:\n{}", drl);

        return drl;
    }

    private String generateSanityRuleDrl(String template, List<SanityRuleType> sanityRules) {
        InputStream templateStream = this.getClass().getResourceAsStream(template);
        TemplateContainer tc = new DefaultTemplateContainer(templateStream);
        TemplateDataListener listener = new TemplateDataListener(tc);

        int rowNum = 0;
        for (SanityRuleType sanityRule : sanityRules) {
            listener.newRow(rowNum, 0);
            listener.newCell(rowNum, 0, sanityRule.getName(), 0);
            listener.newCell(rowNum, 1, sanityRule.getExpression(), 0);
            rowNum++;
        }
        listener.finishSheet();
        String drl = listener.renderDRL();

        LOG.debug("Sanity rule file:\n{}", drl);

        return drl;
    }

    private boolean checkForChanges(List<SanityRuleType> sanityRules) {
        if (currentSanityRules == null || sanityRules.size() != currentSanityRules.size()) {
            return true;
        } else {
            for (int i = 0; i < sanityRules.size(); i++) {
                SanityRuleType a = sanityRules.get(i);
                SanityRuleType b = currentSanityRules.get(i);

                if (a.getDescription() != null && !a.getDescription().equalsIgnoreCase(b.getDescription())) {
                    return true;
                }
                if (a.getDescription() == null && b.getDescription() != null) {
                    return true;
                }
                if (a.getExpression() != null && !a.getExpression().equalsIgnoreCase(b.getExpression())) {
                    return true;
                }
                if (a.getExpression() == null && b.getExpression() != null) {
                    return true;
                }
                if (a.getName() != null && !a.getName().equalsIgnoreCase(b.getName())) {
                    return true;
                }
                if (a.getName() == null && b.getName() != null) {
                    return true;
                }
                if (a.getUpdated() != null && !a.getUpdated().equalsIgnoreCase(b.getUpdated())) {
                    return true;
                }
                if (a.getUpdated() == null && b.getUpdated() != null) {
                    return true;
                }
                if (a.getUpdatedBy() != null && !a.getUpdatedBy().equalsIgnoreCase(b.getUpdatedBy())) {
                    return true;
                }
                if (a.getUpdatedBy() == null && b.getUpdatedBy() != null) {
                    return true;
                }
            }
            return false;
        }
    }

}