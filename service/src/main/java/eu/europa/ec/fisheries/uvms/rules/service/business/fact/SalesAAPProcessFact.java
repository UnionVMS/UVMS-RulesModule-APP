package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.AAPProductType;
import eu.europa.ec.fisheries.schema.sales.FACatchType;
import eu.europa.ec.fisheries.schema.sales.NumericType;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@ToString
public class SalesAAPProcessFact extends SalesAbstractFact {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesAAPProcessFact)) return false;
        SalesAAPProcessFact that = (SalesAAPProcessFact) o;
        return Objects.equals(typeCodes, that.typeCodes) &&
                Objects.equals(conversionFactorNumeric, that.conversionFactorNumeric) &&
                Objects.equals(usedFACatches, that.usedFACatches) &&
                Objects.equals(resultAAPProducts, that.resultAAPProducts) &&
                Objects.equals(source, that.source) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeCodes, conversionFactorNumeric, usedFACatches, resultAAPProducts, source, creationDateOfMessage);
    }

}
