package eu.europa.ec.fisheries.uvms.rules.message.producer.bean;

import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class RulesActivitySubsCheckProducer extends AbstractProducer {
    @Override
    public String getDestinationName() {
        return "jms/queue/UVMSActivitySubscriptionsEvent";
    }
}
