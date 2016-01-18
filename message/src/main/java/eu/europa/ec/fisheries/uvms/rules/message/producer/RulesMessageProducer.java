package eu.europa.ec.fisheries.uvms.rules.message.producer;

import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.rules.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;

import javax.ejb.Local;
import javax.enterprise.event.Observes;
import javax.jms.TextMessage;

@Local
public interface RulesMessageProducer {

    String sendDataSourceMessage(String text, DataSourceQueue queue) throws MessageException;

    void sendModuleResponseMessage(TextMessage message, String text) throws MessageException;

    void sendModuleErrorResponseMessage(@Observes @ErrorEvent EventMessage message);
}
