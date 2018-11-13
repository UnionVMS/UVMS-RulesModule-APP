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
package eu.europa.ec.fisheries.uvms.rules.service.bean.movement;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.io.InputStream;
import java.util.List;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.customrule.v1.SanityRuleType;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesFaultException;
import eu.europa.ec.fisheries.uvms.rules.service.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.SalesRulesService;
import eu.europa.ec.fisheries.uvms.rules.service.ValidationService;
import eu.europa.ec.fisheries.uvms.rules.service.business.CustomRuleDto;
import eu.europa.ec.fisheries.uvms.rules.service.business.MovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.RawMovementFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.CustomRuleParser;
import lombok.extern.slf4j.Slf4j;
import org.drools.template.parser.DefaultTemplateContainer;
import org.drools.template.parser.TemplateContainer;
import org.drools.template.parser.TemplateDataListener;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Slf4j
public class MovementsRulesValidator {
    //    private static final String SANITY_RESOURCE_DRL_FILE = "/rules/SanityRules.drl";
    private static final String CUSTOM_RULE_DRL_FILE = "src/main/resources/rules/CustomRules.drl";
    private static final String CUSTOM_RULE_TEMPLATE = "/templates/CustomRulesTemplate." +
            "drt";

    private static final String SANITY_RULES_DRL_FILE = "src/main/resources/rules/SanityRules.drl";
    private static final String SANITY_RULES_TEMPLATE = "/templates/SanityRulesTemplate.drt";

    private static final String VALIDATION_SERVICE_STR =  "validationService";
    public static final String LOGGER_STR = "logger";

    @EJB
    private ValidationService validationService;

    @EJB
    private SalesRulesService salesService;

    @EJB
    private MDRCacheRuleService mdrService;


    private KieFileSystem sanityKfs;
    private KieContainer sanityKcontainer;
    private List<SanityRuleType> currentSanityRules;

    private KieFileSystem customKfs;
    private KieContainer customKcontainer;



    @PostConstruct
    public void initMovementRules(){
        log.info("Initializing SanityRules for Movemement.");
        updateSanityRules();
    }

    @Lock(LockType.WRITE)
    public String getSanityRuleDrlFile() {
        log.debug("Updating sanity rules");
        try {
            // Fetch sanity rules from DB
            List<SanityRuleType> sanityRules = validationService.getSanityRules();
            if (sanityRules != null && !sanityRules.isEmpty()) {
              //  if (checkForChanges(sanityRules)) {
                    currentSanityRules = sanityRules;
                    // Add sanity rules
                    String drl = generateSanityRuleDrl(SANITY_RULES_TEMPLATE, sanityRules);
                    sanityKfs = KieServices.Factory.get().newKieFileSystem();
                    return drl;
              //  }
            }
        } catch (RulesServiceException | RulesFaultException  e) {
            log.error("[ Error when getting sanity rules ]");
            // TODO: Throw exception???
        }
        return null;
    }

    @Lock(LockType.WRITE)
    public void updateSanityRules() {
        log.info("Updating sanity rules");
        try {
            // Fetch sanity rules from DB
            List<SanityRuleType> sanityRules = validationService.getSanityRules();
            if (sanityRules != null && !sanityRules.isEmpty()) {
                if (checkForChanges(sanityRules)) {
                    currentSanityRules = sanityRules;
                    // Add sanity rules
                    String drl = generateSanityRuleDrl(SANITY_RULES_TEMPLATE, sanityRules);
                    KieServices kieServices = KieServices.Factory.get();
                    sanityKfs = kieServices.newKieFileSystem();
                    sanityKfs.write(SANITY_RULES_DRL_FILE, drl);
                    kieServices.newKieBuilder(sanityKfs).buildAll();
                    sanityKcontainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
                }
            } else {
                sanityKfs = null;
                sanityKcontainer = null;
            }
        } catch (RulesServiceException | RulesFaultException  e) {
            log.error("[ Error when getting sanity rules ]");
            // TODO: Throw exception???
        }
    }

    @Lock(LockType.WRITE)
    public void updateCustomRules() {
        log.info("Updating custom rules");
        try {
            // Fetch custom rules from DB
            List<CustomRuleType> customRules = validationService.getRunnableCustomRules();
            if (customRules != null && !customRules.isEmpty()) {
                // Add custom rules
                List<CustomRuleDto> rules = CustomRuleParser.parseRules(customRules);
                String drl = generateCustomRuleDrl(CUSTOM_RULE_TEMPLATE, rules);
                KieServices kieServices = KieServices.Factory.get();
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
            log.error("[ Error when getting custom rules ]");
            // TODO: Throw exception???
        }
    }

    @Lock(LockType.READ)
    public void evaluate(RawMovementFact fact) {
        KieSession ksession = getKieSession();
        // TODO : decomment as soon as the "Unexpected global [validationService]" is resolved
/*        ksession.setGlobal(VALIDATION_SERVICE_STR, validationService);
        ksession.setGlobal(LOGGER_STR, log);*/
        ksession.insert(fact);
        ksession.fireAllRules();
    }

    @Lock(LockType.READ)
    public void evaluate(MovementFact fact) {
        log.info("Verifying user defined rules");
        KieSession ksession = getKieSession();
        // TODO : decomment as soon as the "Unexpected global [validationService]" is resolved
/*        ksession.setGlobal(VALIDATION_SERVICE_STR, validationService);
        ksession.setGlobal(LOGGER_STR, log);*/
        ksession.insert(fact);
        ksession.fireAllRules();
    }

    @Lock(LockType.READ)
    public void evaluate(List<MovementFact> factList, boolean justToAvoidErasure) {
        log.info("Verifying user defined rules");
        KieSession ksession = getKieSession();
        // TODO : decomment as soon as the "Unexpected global [validationService]" is resolved
/*        ksession.setGlobal(VALIDATION_SERVICE_STR, validationService);
        ksession.setGlobal(LOGGER_STR, log);*/
        for (MovementFact movementFact : factList) {
            ksession.insert(movementFact);
        }
        ksession.fireAllRules();
    }


    @Lock(LockType.READ)
    public void evaluate(List<RawMovementFact> facts) {
        KieSession ksession = getKieSession();
        ksession.setGlobal(LOGGER_STR, log);
        for (RawMovementFact fact : facts) {
            ksession.insert(fact);
        }
        ksession.fireAllRules();
    }



    private KieSession getKieSession() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        return kieContainer.newKieSession();
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
        log.debug("Custom rule file:\n{}", drl);
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
        log.debug("Sanity rule file:\n{}", drl);
        return drl;
    }

    private boolean checkForChanges(List<SanityRuleType> sanityRules) {
        if (currentSanityRules == null || sanityRules.size() != currentSanityRules.size()) {
            return true;
        } else {
            for (int i = 0; i < sanityRules.size(); i++) {
                SanityRuleType sanityA = sanityRules.get(i);
                SanityRuleType sanityB = currentSanityRules.get(i);
                String description = sanityA.getDescription();
                String name = sanityA.getName();
                String updated = sanityA.getUpdated();
                String updatedBy = sanityA.getUpdatedBy();
                if (description != null && !description.equalsIgnoreCase(sanityB.getDescription())) {
                    return true;
                }
                if (description == null && sanityB.getDescription() != null) {
                    return true;
                }
                if (sanityA.getExpression() != null && !sanityA.getExpression().equalsIgnoreCase(sanityB.getExpression())) {
                    return true;
                }
                if (sanityA.getExpression() == null && sanityB.getExpression() != null) {
                    return true;
                }
                if (name != null && !name.equalsIgnoreCase(sanityB.getName())) {
                    return true;
                }
                if (name == null && sanityB.getName() != null) {
                    return true;
                }
                if (updated != null && !updated.equalsIgnoreCase(sanityB.getUpdated())) {
                    return true;
                }
                if (updated == null && sanityB.getUpdated() != null) {
                    return true;
                }
                if (updatedBy != null && !updatedBy.equalsIgnoreCase(sanityB.getUpdatedBy())) {
                    return true;
                }
                if (updatedBy == null && sanityB.getUpdatedBy() != null) {
                    return true;
                }
            }
            return false;
        }
    }

}