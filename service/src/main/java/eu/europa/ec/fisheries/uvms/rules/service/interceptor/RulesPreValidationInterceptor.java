/*
 *
 * Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.
 *
 * This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package eu.europa.ec.fisheries.uvms.rules.service.interceptor;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.exchange.v1.ExchangeLogStatusTypeType;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleStatusType;
import eu.europa.ec.fisheries.uvms.exchange.model.exception.ExchangeModelMarshallException;
import eu.europa.ec.fisheries.uvms.exchange.model.mapper.ExchangeModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.bean.RulesConfigurationCache;
import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.mapper.fact.ActivityFactMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Created by padhyad on 7/14/2017.
 */
@Interceptor
@Slf4j
public class RulesPreValidationInterceptor {

    @EJB
    RulesMessageProducer producer;

    @EJB(lookup = ServiceConstants.DB_ACCESS_RULES_DOMAIN_MODEL)
    private RulesDomainModel rulesDomainModel;

    @AroundInvoke
    public Object validateRuleIsInitialized(final InvocationContext ic) throws Exception {
        Object object = null;
        log.info("Validation rules is ready for validation");
        RuleStatusType ruleStatusType = rulesDomainModel.checkRuleStatus();
        if (ruleStatusType.equals(RuleStatusType.SUCCESSFUL)) {
            object = ic.proceed();
        } else {
            RulesBaseRequest request = getRulesBaseRequest(ic); // Get the input parameter
            sendMessageToExchange(request);
        }
        return object;
    }

    private void sendMessageToExchange(RulesBaseRequest request) throws ExchangeModelMarshallException, MessageException {
        if (request != null) {
            ExchangeLogStatusTypeType exchangeLogStatusTypeType = ExchangeLogStatusTypeType.UNPROCESSED;
            String statusMsg = ExchangeModuleRequestMapper.createUpdateLogStatusRequest(request.getLogGuid(), exchangeLogStatusTypeType);
            log.error("Rules validation cannot proceed as rules failed to initialize. Updating status in Exchange");
            log.info("Message to exchange to update status : {}", statusMsg);
            producer.sendDataSourceMessage(statusMsg, DataSourceQueue.EXCHANGE);
        }
    }

    private RulesBaseRequest getRulesBaseRequest(InvocationContext ic) {
        Object[] parameters = ic.getParameters();
        RulesBaseRequest request = null;
        for (Object object : parameters) {
            if (object instanceof  RulesBaseRequest) {
                request = (RulesBaseRequest) object;
                break;
            }
        }
        return request;
    }
}
