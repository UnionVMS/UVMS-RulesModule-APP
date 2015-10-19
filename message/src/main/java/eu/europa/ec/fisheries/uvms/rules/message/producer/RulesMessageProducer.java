package eu.europa.ec.fisheries.uvms.rules.message.producer;

import javax.ejb.Local;
import javax.jms.TextMessage;

import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;

@Local
public interface RulesMessageProducer {

    public String sendDataSourceMessage(String text, DataSourceQueue queue) throws MessageException;

    public void sendModuleResponseMessage(TextMessage message, String text) throws MessageException;

}
