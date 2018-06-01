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

import java.util.*;

import eu.europa.ec.fisheries.uvms.commons.date.XMLDateUtils;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import eu.europa.ec.fisheries.uvms.rules.service.config.ExtraValueType;
import eu.europa.ec.fisheries.uvms.rules.service.exception.RulesValidationException;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXReportDocument;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.DateTimeType;
import un.unece.uncefact.data.standard.unqualifieddatatype._20.IDType;

/**
 * @author padhyad
 * @author Gregory Rinaldi
 * @author Andi Kovi
 */
public abstract class AbstractGenerator<T> {

    Map<ExtraValueType, Object> extraValueMap;

    public abstract List<AbstractFact> generateAllFacts();

    public abstract void setBusinessObjectMessage(T businessObject) throws RulesValidationException;

    /**
     * Set internal Validation Object(s) if needed.
     * These objects will be at your disposal to use in the generator or can be transported
     * even in the mapper level if needed, so that you can set them in fact object and use them in drt(s)/drls(s).
     * <p>
     * This way we avoid having to set global EJB objects that sometimes need to be invoked in the drts.
     *
     */
    public void setAdditionalValidationObject() {
        return;
    }

    public void setExtraValueMap(Map<ExtraValueType, Object> map) {
        this.extraValueMap = map;
    }

    /**
     * This method is setting unique UUIDs to a list of facts
     * eg. FaReportDocumentIDS, FluxReportMessageIDs, FAResponseMessageIDs
     * @param fluxRepDoc
     * @param facts
     */
    void populateUniqueIDsAndFaReportDocumentDate(FLUXReportDocument fluxRepDoc, List<AbstractFact> facts) {
        List<String> strIDs = getIds(fluxRepDoc.getIDS());
        DateTimeType creationDateTime = fluxRepDoc.getCreationDateTime();
        DateTime reportDateTime = null;
        if(creationDateTime != null){
            Date repDat = XMLDateUtils.xmlGregorianCalendarToDate(creationDateTime.getDateTime());
            if(repDat != null){
                reportDateTime = new DateTime(repDat);
            }
        }
        facts.removeAll(Collections.singleton(null));
        for (AbstractFact fact : facts) {
            fact.setUniqueIds(strIDs);
            fact.setCreationDateOfMessage(reportDateTime);
        }
    }

    private List<String> getIds(List<IDType> idTypes) {
        ArrayList<String> ids = new ArrayList<>();
        if (CollectionUtils.isEmpty(idTypes)) {
            return ids;
        }
        if (CollectionUtils.isNotEmpty(idTypes)){
            ids = new ArrayList<>();
            for (IDType idType : idTypes) {
                String value = idType.getValue();
                String schemeID = idType.getSchemeID();
                if (value != null && schemeID != null) {
                    ids.add(value.concat("_").concat(schemeID));
                }
            }
        }
        return ids;
    }

}
