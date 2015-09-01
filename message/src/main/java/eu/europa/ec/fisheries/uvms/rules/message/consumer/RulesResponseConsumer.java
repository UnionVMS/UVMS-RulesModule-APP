package eu.europa.ec.fisheries.uvms.rules.message.consumer;

import javax.ejb.Local;

import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;

@Local
public interface RulesResponseConsumer {

    public <T> T getMessage(String correlationId, Class type) throws MessageException;

}
