package eu.europa.ec.fisheries.uvms.rules.message.producer.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;


@Stateless
@LocalBean
public class RulesAuditProducerBean extends AbstractProducer {
    @Override
    public String getDestinationName() {
        return MessageConstants.QUEUE_AUDIT_EVENT;
    }
}