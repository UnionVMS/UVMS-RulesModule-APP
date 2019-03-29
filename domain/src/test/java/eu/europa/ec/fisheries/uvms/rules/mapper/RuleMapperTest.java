/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.rules.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import eu.europa.ec.fisheries.schema.rules.rule.v1.ErrorType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.uvms.rules.entity.DataFlowAndExpression;
import eu.europa.ec.fisheries.uvms.rules.entity.Rule;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by kovian on 11/05/2017.
 */
public class RuleMapperTest {

    private String testDRL;

    @Before
    @SneakyThrows
    public void before() {
        testDRL = "SomeDRL";
    }

    @Test
    @SneakyThrows
    public void testRuleMapper(){
        Rule ruleEntity = new Rule();
        DataFlowAndExpression dtEpr = new DataFlowAndExpression();
        dtEpr.setExpression("code.id == id");
        dtEpr.setFailureMessage("Result message");
        ruleEntity.getDataFlowAndExpressionList().add(dtEpr);
        ruleEntity.setLevel("001");
        ruleEntity.setBrId("ID_2291");
        ruleEntity.setErrorType(ErrorType.ERROR);
        ruleEntity.setNote("Some Notes");

        List<RuleType> externalRuleTypes = RuleMapper.INSTANCE.mapToAllFactRuleType(Arrays.asList(ruleEntity));

        assertNotNull(externalRuleTypes);

        RuleType ruleType = externalRuleTypes.get(0);

        assertNotNull(ruleType);
        assertEquals("code.id == id", ruleType.getDataFlowAndExpressionList().get(1).getExpression());
        assertEquals("001", ruleType.getLevel());
        assertEquals("Result message", ruleType.getDataFlowAndExpressionList().get(1).getFailureMessage());
        assertEquals("ID_2291", ruleType.getBrId());
        assertEquals("Some Notes", ruleType.getNote());
        assertEquals(ErrorType.ERROR.toString(), ruleType.getErrorType().toString());
    }
}