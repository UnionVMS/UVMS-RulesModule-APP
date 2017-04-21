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

package eu.europa.ec.fisheries.uvms.rules.service.bean;

import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.business.BusinessObjectFactory;
import eu.europa.ec.fisheries.uvms.rules.service.business.generator.AbstractGenerator;
import eu.europa.ec.fisheries.uvms.rules.service.config.BusinessObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesServiceException;
import un.unece.uncefact.data.standard.fluxfareportmessage._3.FLUXFAReportMessage;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAReportDocument;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
@LocalBean
public class RulesEngine {
 
	@EJB
	private TemplateEngine templateEngine;
	
    public void evaluate(Map<BusinessObjectType, FLUXFAReportMessage> businessObjectEnum) throws RulesServiceException {
		try {
			List<AbstractFact> facts = new ArrayList<>();
			for (Map.Entry<BusinessObjectType, FLUXFAReportMessage> entry : businessObjectEnum.entrySet()) {
				AbstractGenerator generator = BusinessObjectFactory.getBusinessObjFactGenerator(entry.getKey());
				generator.setFluxfaReportMessage(entry.getValue());
				facts.addAll(generator.getAllFacts());
			}
			templateEngine.evaluateFacts(facts);
		} catch (RulesModelException e) {
			throw new RulesServiceException(e.getMessage(), e);
		}

    }
}
