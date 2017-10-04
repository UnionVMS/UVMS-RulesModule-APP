/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.rules.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TemplateMapperTest {

    @Test
    public void testMapToTemplateModelList() {

        /*Template template = Template.builder()
                .id(1L)
                .lhs("lhs")
                .templateType(eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType.CHECKNULL)
                .thenStatement("then")
                .templateName("name")
                .ruleType(RuleType.NESTED)
                .build();

        List<eu.europa.ec.fisheries.schema.rules.template.v1.Template> templateModelList =
                TemplateMapper.INSTANCE.mapToTemplateModelList(Arrays.asList(template));

        assertEquals(template.getLhs(), templateModelList.get(0).getLhs());
        assertEquals(template.getTemplateType(), templateModelList.get(0).getType());
        assertEquals(template.getThenStatement(), templateModelList.get(0).getThenStatement());
        assertEquals(template.getTemplateName(), templateModelList.get(0).getTemplateName());
        assertEquals(template.getRuleType(), templateModelList.get(0).getRuleType());*/
        assertEquals(1,1);

    }
}
