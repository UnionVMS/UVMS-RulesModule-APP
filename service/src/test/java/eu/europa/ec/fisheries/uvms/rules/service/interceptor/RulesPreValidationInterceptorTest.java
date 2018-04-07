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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import javax.interceptor.InvocationContext;

import eu.europa.ec.fisheries.remote.RulesDomainModel;
import eu.europa.ec.fisheries.schema.rules.module.v1.RulesBaseRequest;
import eu.europa.ec.fisheries.schema.rules.module.v1.SetFLUXFAReportMessageRequest;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleStatusType;
import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import lombok.SneakyThrows;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * Created by padhyad on 7/17/2017.
 */

@Ignore
public class RulesPreValidationInterceptorTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    RulesPreValidationInterceptor interceptor;

    @Mock
    RulesMessageProducer producer;

    @Mock
    RulesDomainModel rulesDomainModel;

    @Mock
    InvocationContext invocationContext;

    @Test
    @SneakyThrows
    public void ruleInitializedTest() {
        when(rulesDomainModel.checkRuleStatus()).thenReturn(RuleStatusType.SUCCESSFUL);
        when(invocationContext.proceed()).thenReturn(new Object());
        Object proceed = interceptor.validateRuleIsInitialized(invocationContext);
        assertNotNull(proceed);
    }

    @Test
    @SneakyThrows
    public void ruleNotInitializedTest() {
        when(rulesDomainModel.checkRuleStatus()).thenReturn(RuleStatusType.FAILED);
        doReturn("ID").when(producer).sendDataSourceMessage(any(String.class), any(DataSourceQueue.class));
        RulesBaseRequest request = new SetFLUXFAReportMessageRequest();
        request.setLogGuid("test");
        when(invocationContext.getParameters()).thenReturn(new Object[]{request});
        Object proceed = interceptor.validateRuleIsInitialized(invocationContext);
        assertNull(proceed);
    }

}
