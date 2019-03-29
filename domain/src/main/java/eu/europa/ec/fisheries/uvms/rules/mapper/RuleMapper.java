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

package eu.europa.ec.fisheries.uvms.rules.mapper;

import java.util.Collection;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.rule.v1.DataFlowAndExpressionType;
import eu.europa.ec.fisheries.schema.rules.rule.v1.RuleType;
import eu.europa.ec.fisheries.uvms.rules.entity.DataFlowAndExpression;
import eu.europa.ec.fisheries.uvms.rules.entity.Rule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Created by padhyad on 4/10/2017.
 */
@Mapper
public interface RuleMapper {

    RuleMapper INSTANCE = Mappers.getMapper(RuleMapper.class);

    @Mappings({
            @Mapping(target = "createdOn", source = "audit.createdOn")
    })
    RuleType mapToFactRuleType(Rule factRule);

    List<RuleType> mapToAllFactRuleType(Collection<Rule> factRules);

    @Mappings({
            @Mapping(target = "ruleId", source = "rule.id")
    })
    DataFlowAndExpressionType mapToFactDataFlowAndExpressionType(DataFlowAndExpression factRules);

    List<DataFlowAndExpressionType> mapToAllFactDataFlowAndExpressionType(Collection<DataFlowAndExpression> factRules);

}
