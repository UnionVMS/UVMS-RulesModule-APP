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

package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.*;
import eu.europa.ec.fisheries.uvms.rules.service.business.*;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.*;

import java.util.*;

/**
 * Created by padhyad on 4/21/2017.
 */
public class FluxCharacteristicsFact extends AbstractFact {

    private CodeType typeCode;

    private List<TextType> values;

    public CodeType getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public List<TextType> getValues() {
        return values;
    }

    public void setValues(List<TextType> values) {
        this.values = values;
    }

    public FluxCharacteristicsFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FLUX_CHARACTERISTIC;
    }
}
