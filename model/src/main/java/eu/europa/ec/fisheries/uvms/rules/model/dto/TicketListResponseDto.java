package eu.europa.ec.fisheries.uvms.rules.model.dto;

import java.io.Serializable;
import java.util.List;

import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;

public class TicketListResponseDto implements Serializable {

    private static final long serialVersionUID = 1;

    private List<TicketType> ticketList;
    private int totalNumberOfPages;
    private int currentPage;

    public List<TicketType> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<TicketType> ticketList) {
        this.ticketList = ticketList;
    }

    public int getTotalNumberOfPages() {
        return totalNumberOfPages;
    }

    public void setTotalNumberOfPages(int totalNumberOfPages) {
        this.totalNumberOfPages = totalNumberOfPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

}
