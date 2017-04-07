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

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.schema.rules.template.v1.Template;
import eu.europa.ec.fisheries.schema.rules.template.v1.TemplateType;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Startup
public class WarningsDatasource extends TemplateDatasource {

	@Override
	public List<String> computeRules(Template template){
		List<String> rules = new ArrayList<String>();
		String rule = "some rule";
		rule  = injectWarningMetadata(rule);
		rules.add(rule);
		return rules;
	}

	@Override
	public TemplateType getTemplateType(){
		return TemplateType.WARNING;
	}

	@Override
	public List<String> getAttributes() {
		return null;
	}

	private String injectWarningMetadata(String rule) {
		rule +="@metadata_key( metadata_value1, metadata_value2, ...  )";
		rule +="@LegalRequirement(\"Section 103 RTA 1988\")";
		return rule;
	}
}
