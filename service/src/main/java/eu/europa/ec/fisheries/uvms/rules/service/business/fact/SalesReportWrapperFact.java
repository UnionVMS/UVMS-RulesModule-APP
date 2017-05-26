package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

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
        if (o == null || getClass() != o.getClass()) return false;

        SalesReportWrapperFact that = (SalesReportWrapperFact) o;

        if (fluxSalesReportMessage != null ? !fluxSalesReportMessage.equals(that.fluxSalesReportMessage) : that.fluxSalesReportMessage != null)
            return false;
        return auctionSale != null ? auctionSale.equals(that.auctionSale) : that.auctionSale == null;
    }

    @Override
    public int hashCode() {
        int result = fluxSalesReportMessage != null ? fluxSalesReportMessage.hashCode() : 0;
        result = 31 * result + (auctionSale != null ? auctionSale.hashCode() : 0);
        return result;
    }
}
