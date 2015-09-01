package eu.europa.ec.fisheries.uvms.rules.model;

import javax.ejb.Local;

import eu.europa.ec.fisheries.uvms.rules.model.exception.ModelException;

@Local
public interface Model {

    public void sendData(Object dto) throws ModelException;

    public Object getData() throws ModelException;

}
