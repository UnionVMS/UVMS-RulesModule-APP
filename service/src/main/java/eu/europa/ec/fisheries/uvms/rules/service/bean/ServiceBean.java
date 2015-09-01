package eu.europa.ec.fisheries.uvms.rules.service.bean;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.rules.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.rules.message.consumer.RulesResponseConsumer;
import eu.europa.ec.fisheries.uvms.rules.message.exception.MessageException;
import eu.europa.ec.fisheries.uvms.rules.message.producer.RulesMessageProducer;
import eu.europa.ec.fisheries.uvms.rules.service.RulesParameterService;
import eu.europa.ec.fisheries.uvms.rules.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.DataSourceRequestMapper;
import eu.europa.ec.fisheries.uvms.rules.model.mapper.DataSourceResponseMapper;
import eu.europa.ec.fisheries.uvms.rules.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rules.service.Service;
import eu.europa.ec.fisheries.wsdl.types.ModuleObject;

@Stateless
public class ServiceBean implements Service {

    final static Logger LOG = LoggerFactory.getLogger(ServiceBean.class);

    @EJB
    RulesParameterService parameterService;

    @EJB
    RulesResponseConsumer consumer;

    @EJB
    RulesMessageProducer producer;

    /**
     * {@inheritDoc}
     *
     * @param data
     * @throws ServiceException
     */
    @Override
    public ModuleObject create(ModuleObject data) throws ServiceException {
        LOG.info("Create invoked in service layer");
        try {
            String request = DataSourceRequestMapper.mapObjectToString(data);
            String messageId = producer.sendDataSourceMessage(request, DataSourceQueue.INTERNAL);
            TextMessage response = consumer.getMessage(messageId, TextMessage.class);
            return DataSourceResponseMapper.mapToModuleObjectFromResponse(response);
        } catch (ModelMapperException | MessageException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     * @throws ServiceException
     */
    @Override
    public List<ModuleObject> getList() throws ServiceException {
        LOG.info("Get list invoked in service layer");
        throw new ServiceException("Get list logic not implemented in service layer");
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    @Override
    public ModuleObject getById(Long id) throws ServiceException {
        LOG.info("Get by id invoked in service layer");
        throw new ServiceException("Get by id not implemented in service layer");
    }

    /**
     * {@inheritDoc}
     *
     * @param data
     * @throws ServiceException
     */
    @Override
    public ModuleObject update(ModuleObject data) throws ServiceException {
        LOG.info("Update invoked in service layer");
        throw new ServiceException("Update not implemented in service layer");
    }

}
