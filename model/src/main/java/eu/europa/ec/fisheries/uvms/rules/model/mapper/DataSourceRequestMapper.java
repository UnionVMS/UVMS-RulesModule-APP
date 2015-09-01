package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.rules.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.wsdl.source.GetDataRequest;
import eu.europa.ec.fisheries.wsdl.types.ModuleObject;


public class DataSourceRequestMapper {

    final static Logger LOG = LoggerFactory.getLogger(DataSourceRequestMapper.class);

    public static String mapObjectToString(ModuleObject data) throws ModelMapperException {
        try {
            GetDataRequest request = new GetDataRequest();
            request.setId(data);
            return JAXBMarshaller.marshallJaxBObjectToString(request);
        } catch (Exception e) {
            LOG.error("[ Error when mapping Object to String ] {}", e.getMessage());
            throw new ModelMapperException("[ Error when mapping Object to String ]", e);
        }
    }

}
