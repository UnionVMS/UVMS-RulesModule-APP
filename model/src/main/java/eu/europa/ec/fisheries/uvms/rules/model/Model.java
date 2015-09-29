package eu.europa.ec.fisheries.uvms.rules.model;

import javax.ejb.Local;

import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelException;

@Local
public interface Model {

    public void sendData(Object dto) throws RulesModelException;

    public Object getData() throws RulesModelException;

}
