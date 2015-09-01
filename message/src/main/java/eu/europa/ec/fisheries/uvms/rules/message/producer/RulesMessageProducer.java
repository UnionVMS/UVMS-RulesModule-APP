package eu.europa.ec.fisheries.uvms.rules.message.producer;

import javax.ejb.Local;

import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;

@Local
public interface RulesMessageProducer {

    public String sendDataSourceMessage(String text, DataSourceQueue queue) throws MessageException;

}
