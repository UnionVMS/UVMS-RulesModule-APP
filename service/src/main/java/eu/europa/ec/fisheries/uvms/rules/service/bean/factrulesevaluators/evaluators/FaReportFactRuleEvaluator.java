/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.evaluators;

import eu.europa.ec.fisheries.uvms.rules.model.dto.TemplateRuleMapDto;
import eu.europa.ec.fisheries.uvms.rules.service.bean.ExchangeRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.caches.MDRCacheRuleService;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.ContainerType;
import eu.europa.ec.fisheries.uvms.rules.service.bean.factrulesevaluators.DroolsEngineInitializer;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.kie.api.runtime.KieContainer;

import javax.ejb.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Stateless
@LocalBean
public class FaReportFactRuleEvaluator extends AbstractFactEvaluator {

    @EJB
    private MDRCacheRuleService mdrCacheRuleService;

    @EJB
    private ExchangeRuleService exchangeRuleService;

    @EJB
    private DroolsEngineInitializer initializer;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public KieContainer initializeRules(Collection<TemplateRuleMapDto> templates) {
        return initializeRules(templates, ContainerType.FA_REPORT.getContainerName(), ContainerType.FA_REPORT.getPackageName());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void validateFacts(Collection<AbstractFact> facts) throws RulesValidationException {
        if (CollectionUtils.isEmpty(facts)) {
            throw new RulesValidationException("No facts available for validation");
        }
        setExceptionsList(new ArrayList<AbstractFact>());
        validateFacts(facts, initializer.getContainerByType(ContainerType.FA_REPORT));
        facts.addAll(getExceptionsList());
    }

    @Override
    Map<String, Object> getGlobalsMap() {
        return  new HashMap<String, Object>() {{
            put("mdrService", mdrCacheRuleService);
            put("exchangeService", exchangeRuleService);
        }};
    }
}
