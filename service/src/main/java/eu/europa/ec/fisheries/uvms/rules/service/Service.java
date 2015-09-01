package eu.europa.ec.fisheries.uvms.rules.service;

import javax.ejb.Local;

import eu.europa.ec.fisheries.uvms.rules.service.exception.ServiceException;
import eu.europa.ec.fisheries.wsdl.types.ModuleObject;
import java.util.List;

@Local
public interface Service {

    /**
     * Create/Insert data into database
     *
     * @param data
     * @return
     * @throws ServiceException
     */
    public ModuleObject create(ModuleObject data) throws ServiceException;

    /**
     * Get a list with data
     *
     * @return
     * @throws ServiceException
     */
    public List<ModuleObject> getList() throws ServiceException;

    /**
     * Get an object by id
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public ModuleObject getById(Long id) throws ServiceException;

    /**
     * Update an object
     *
     * @param data
     * @throws ServiceException
     */
    public ModuleObject update(ModuleObject data) throws ServiceException;

}
