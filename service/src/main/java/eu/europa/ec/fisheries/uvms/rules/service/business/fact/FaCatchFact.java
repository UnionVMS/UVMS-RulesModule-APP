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

import java.math.BigDecimal;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProcess;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.AAPProduct;
import un.unece.uncefact.data.standard.reusableaggregatebusinessinformationentity._20.FLUXLocation;

@Data
@EqualsAndHashCode(callSuper = false)
public class FaCatchFact extends AbstractFact {

    private CodeType fishingActivityTypeCode;
    private CodeType faReportDocumentTypeCode;
    private IdType farepDocSpecVesselTrpmRegVesselCountryId;
    private CodeType typeCode;
    private CodeType speciesCode;
    private MeasureType unitQuantity;
    private MeasureType weightMeasure;
    private List<CodeType> sizeDistributionClassCode;
    private List<CodeType> appliedAAPProcessTypeCodes;
    private List<AAPProduct> resultAAPProduct;
    private List<MeasureType> resultAAPProductUnitQuantity;
    private List<MeasureType> resultAAPProductWeightMeasure;
    private List<CodeType> resultAAPProductPackagingTypeCode;
    private List<MeasureType> resultAAPProductPackagingUnitAverageWeightMeasure;
    private List<MeasureType> resultAAPProductPackagingUnitQuantity;
    private List<String> testStringList;
    private List<IdType> fluxLocationId;
    private CodeType weighingMeansCode;
    private CodeType categoryCode;
    private List<NumericType> appliedAAPProcessConversionFactorNumber;
    private List<FLUXLocation> specifiedFLUXLocations;
    private List<CodeType> specifiedFluxLocationRFMOCodeList;
    private List<AAPProcess> appliedAAPProcess;
    private List<FLUXLocation> destinationFLUXLocations;
    private List<IdType> faCatchFluxLocationId;
    private List<IdType> fishActRelatedFluxLocationIds;
    private boolean isSubActivity = false;



    public boolean containsAtLeastOneGfcmGsaWithValidValue(List<IdType> ids){
        if(CollectionUtils.isEmpty(ids)){
            return false;
        }
        for (IdType id : ids) {
            if("GFCM_GSA".equals(id.getSchemeId()) && StringUtils.isNotEmpty(id.getValue())){
                return true;
            }
        }
        return false;
    }

    public boolean containsAtLeastOneFaoAreaWithValidValue(List<IdType> ids){
        if(CollectionUtils.isEmpty(ids)){
            return false;
        }
        for (IdType id : ids) {
            if("FAO_AREA".equals(id.getSchemeId()) && StringUtils.startsWith(id.getValue(), "27.3.d")){
                return true;
            }
        }
        return false;
    }

    public boolean containsAtLeastOneCFGreaterThenOne(){
        boolean containsAtLeastOneGreaterThenOne = false;
        final BigDecimal one = new BigDecimal(1);
        if(CollectionUtils.isNotEmpty(appliedAAPProcessConversionFactorNumber)){
            containsAtLeastOneGreaterThenOne = appliedAAPProcessConversionFactorNumber.stream().anyMatch(cf -> cf.getValue() != null && one.compareTo(cf.getValue()) <= 0);
        }
        return containsAtLeastOneGreaterThenOne;
    }

    public FaCatchFact() {
        setFactType();
    }

    @Override
    public void setFactType() {
        this.factType = FactType.FA_CATCH;
    }

    public boolean isSubActivity() {
        return isSubActivity;
    }

}
