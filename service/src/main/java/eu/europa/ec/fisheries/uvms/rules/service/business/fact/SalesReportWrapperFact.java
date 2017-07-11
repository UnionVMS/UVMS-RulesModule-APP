package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import java.util.Objects;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.AuctionSaleType;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.uvms.rules.service.business.AbstractFact;

public class SalesReportWrapperFact extends AbstractFact {

    private FLUXSalesReportMessage fluxSalesReportMessage;
    private AuctionSaleType auctionSale;

    @Override
    public void setFactType() {
        this.factType = FactType.SALES_REPORT_WRAPPER;
    }

    public FLUXSalesReportMessage getFLUXSalesReportMessage() {
        return fluxSalesReportMessage;
    }

    public void setFLUXSalesReportMessage(FLUXSalesReportMessage fluxSalesReportMessage) {
        this.fluxSalesReportMessage = fluxSalesReportMessage;
    }

    public AuctionSaleType getAuctionSale() {
        return auctionSale;
    }

    public void setAuctionSale(AuctionSaleType auctionSale) {
        this.auctionSale = auctionSale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SalesReportWrapperFact)) return false;
        SalesReportWrapperFact that = (SalesReportWrapperFact) o;
        return Objects.equals(fluxSalesReportMessage, that.fluxSalesReportMessage) &&
                Objects.equals(auctionSale, that.auctionSale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fluxSalesReportMessage, auctionSale);
    }
}
