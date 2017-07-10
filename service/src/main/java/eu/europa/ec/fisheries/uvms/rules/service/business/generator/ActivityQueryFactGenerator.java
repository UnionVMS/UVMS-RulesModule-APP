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

package eu.europa.ec.fisheries.uvms.rules.service.business.generator;

import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.config.AdditionalValidationObjectType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FAQuery;

import java.util.Collection;
import java.util.List;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 */
public class ActivityQueryFactGenerator extends AbstractGenerator {

    private FAQuery faQuery;

    @Override
    public List<AbstractFact> generateAllFacts() {
        //TODO facts
        return null;
    }

    @Override
    public void setBusinessObjectMessage(Object businessObject) throws RulesValidationException {
        if (!(businessObject instanceof  FAQuery)) {
            throw new RulesValidationException("Business object does not match required type");
        }
        this.faQuery = (FAQuery) businessObject;
    }

    @Override
    public <T> void setAdditionalValidationObject(Collection<T> additionalObject, AdditionalValidationObjectType validationType) {
        // Set internal Validation Object if needed.
    }
}
