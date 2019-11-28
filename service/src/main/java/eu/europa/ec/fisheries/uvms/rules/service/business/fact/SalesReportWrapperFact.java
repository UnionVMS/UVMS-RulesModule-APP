package eu.europa.ec.fisheries.uvms.rules.service.business.fact;

import eu.europa.ec.fisheries.schema.rules.template.v1.FactType;
import eu.europa.ec.fisheries.schema.sales.AuctionSaleType;
import eu.europa.ec.fisheries.schema.sales.FLUXSalesReportMessage;
import eu.europa.ec.fisheries.uvms.rules.service.business.SalesAbstractFact;
import lombok.ToString;

import java.util.Objects;

@ToString
public class SalesReportWrapperFact extends SalesAbstractFact {

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
                Objects.equals(auctionSale, that.auctionSale) &&
                Objects.equals(creationDateOfMessage, that.creationDateOfMessage) &&
                Objects.equals(messageDataFlow, that.messageDataFlow) &&
                Objects.equals(creationJavaDateOfMessage, that.creationJavaDateOfMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fluxSalesReportMessage, auctionSale, creationDateOfMessage, messageDataFlow, creationJavaDateOfMessage);
    }
}
