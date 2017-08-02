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

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.TextType;

import java.util.List;

/**
 * Created by sanera on 27/07/2017.
 */
public class ValidationQualityAnalysisFact extends AbstractFact {

    private IdType id;
    private CodeType levelCode;
    private CodeType typeCode;
    private List<TextType> results;
    private List<TextType> referencedItems;

    public IdType getId() {
        return id;
    }

    public void setId(IdType id) {
        this.id = id;
    }

    public CodeType getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(CodeType levelCode) {
        this.levelCode = levelCode;
    }

    public CodeType getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(CodeType typeCode) {
        this.typeCode = typeCode;
    }

    public List<TextType> getResults() {
        return results;
    }

    public void setResults(List<TextType> results) {
        this.results = results;
    }

    public List<TextType> getReferencedItems() {
        return referencedItems;
    }

    public void setReferencedItems(List<TextType> referencedItems) {
        this.referencedItems = referencedItems;
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_VALIDATION_QUALITY_ANALYSIS;
    }
}
