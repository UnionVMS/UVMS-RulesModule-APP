package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.AAPProductType;
import eu.europa.ec.fisheries.schema.sales.FACatchType;
import eu.europa.ec.fisheries.schema.sales.NumericType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

import java.util.List;

public class SalesAAPProcessFact extends AbstractFact {

    private List<CodeType> typeCodes;
    private NumericType conversionFactorNumeric;
    private List<FACatchType> usedFACatches;
    private List<AAPProductType> resultAAPProducts;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_AAP_PROCESS;
    }

    public List<CodeType> getTypeCodes() {
        return this.typeCodes;
    }

    public NumericType getConversionFactorNumeric() {
        return this.conversionFactorNumeric;
    }

    public List<FACatchType> getUsedFACatches() {
        return this.usedFACatches;
    }

    public List<AAPProductType> getResultAAPProducts() {
        return this.resultAAPProducts;
    }

    public void setTypeCodes(List<CodeType> typeCodes) {
        this.typeCodes = typeCodes;
    }

    public void setConversionFactorNumeric(NumericType conversionFactorNumeric) {
        this.conversionFactorNumeric = conversionFactorNumeric;
    }

    public void setUsedFACatches(List<FACatchType> usedFACatches) {
        this.usedFACatches = usedFACatches;
    }

    public void setResultAAPProducts(List<AAPProductType> resultAAPProducts) {
        this.resultAAPProducts = resultAAPProducts;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SalesAAPProcessFact)) return false;
        final SalesAAPProcessFact other = (SalesAAPProcessFact) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$typeCodes = this.getTypeCodes();
        final Object other$typeCodes = other.getTypeCodes();
        if (this$typeCodes == null ? other$typeCodes != null : !this$typeCodes.equals(other$typeCodes)) return false;
        final Object this$conversionFactorNumeric = this.getConversionFactorNumeric();
        final Object other$conversionFactorNumeric = other.getConversionFactorNumeric();
        if (this$conversionFactorNumeric == null ? other$conversionFactorNumeric != null : !this$conversionFactorNumeric.equals(other$conversionFactorNumeric))
            return false;
        final Object this$usedFACatches = this.getUsedFACatches();
        final Object other$usedFACatches = other.getUsedFACatches();
        if (this$usedFACatches == null ? other$usedFACatches != null : !this$usedFACatches.equals(other$usedFACatches))
            return false;
        final Object this$resultAAPProducts = this.getResultAAPProducts();
        final Object other$resultAAPProducts = other.getResultAAPProducts();
        if (this$resultAAPProducts == null ? other$resultAAPProducts != null : !this$resultAAPProducts.equals(other$resultAAPProducts))
            return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $typeCodes = this.getTypeCodes();
        result = result * PRIME + ($typeCodes == null ? 43 : $typeCodes.hashCode());
        final Object $conversionFactorNumeric = this.getConversionFactorNumeric();
        result = result * PRIME + ($conversionFactorNumeric == null ? 43 : $conversionFactorNumeric.hashCode());
        final Object $usedFACatches = this.getUsedFACatches();
        result = result * PRIME + ($usedFACatches == null ? 43 : $usedFACatches.hashCode());
        final Object $resultAAPProducts = this.getResultAAPProducts();
        result = result * PRIME + ($resultAAPProducts == null ? 43 : $resultAAPProducts.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SalesAAPProcessFact;
    }
}
