/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.message.consumer.bean;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import eu.europa.ec.fisheries.schema.rules.customrule.v1.CustomRuleType;
import eu.europa.ec.fisheries.schema.rules.module.v1.GetCustomRuleRequest;
import eu.europa.ec.fisheries.uvms.rules.message.RulesMessageEvent;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.RulesModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.RulesService;
import lombok.extern.slf4j.Slf4j;

@Stateless
@LocalBean
@Slf4j
public class GetCustomRuleRequestMessageBean extends RulesMessageBase {

    @EJB
    private RulesService rulesService;

    @EJB
    private RulesMessageProducer producer;

    @Override
    RulesMessageProducer getProducer() {
        return producer;
    }

    @Override void processMessage(RulesMessageEvent message, String username) throws Exception {
        log.info("Get custom rule by guid");
        GetCustomRuleRequest request = (GetCustomRuleRequest) message.getRequest();
        String ruleGuid = request.getGuid();
        CustomRuleType response = rulesService.getCustomRuleByGuid(ruleGuid);
        String responseString = RulesModuleResponseMapper.mapToGetCustomRuleResponse(response);
        producer.sendModuleResponseMessage(message.getMessage(), responseString);
    }
}
