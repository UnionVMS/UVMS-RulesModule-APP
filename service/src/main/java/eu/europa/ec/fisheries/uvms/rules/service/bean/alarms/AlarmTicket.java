package eu.europa.ec.fisheries.uvms.rules.service.bean.alarms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.io.Serializable;

import eu.europa.ec.fisheries.schema.rules.ticket.v1.TicketType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "ticketType"
})
@XmlRootElement(name = "AlarmTicket")
public class AlarmTicket implements Serializable {

    private final static long serialVersionUID = 1L;

    @XmlElement(required = true)
    protected TicketType ticketType;

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

}
