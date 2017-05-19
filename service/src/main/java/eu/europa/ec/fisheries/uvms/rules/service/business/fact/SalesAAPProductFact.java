package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.*;
import eu.europa.ec.fisheries.schema.sales.MeasureType;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
public class SalesAAPProductFact extends AbstractFact {

    private eu.europa.ec.fisheries.schema.sales.CodeType speciesCode;
    private QuantityType unitQuantity;
    private eu.europa.ec.fisheries.schema.sales.MeasureType weightMeasure;
    private eu.europa.ec.fisheries.schema.sales.CodeType weighingMeansCode;
    private eu.europa.ec.fisheries.schema.sales.CodeType usageCode;
    private QuantityType packagingUnitQuantity;
    private eu.europa.ec.fisheries.schema.sales.CodeType packagingTypeCode;
    private MeasureType packagingUnitAverageWeightMeasure;
    private List<AAPProcessType> appliedAAPProcesses;
    private SalesPriceType totalSalesPrice;
    private SizeDistributionType specifiedSizeDistribution;
    private List<FLUXLocationType> originFLUXLocations;
    private FishingActivityType originFishingActivity;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_BATCH;
    }
}
