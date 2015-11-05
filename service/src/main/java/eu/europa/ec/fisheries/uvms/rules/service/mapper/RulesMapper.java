package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.jms.TextMessage;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.schema.movement.module.v1.CreateMovementResponse;
import eu.europa.ec.fisheries.schema.movement.v1.MovementType;
import eu.europa.ec.fisheries.uvms.movement.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.movement.model.mapper.JAXBMarshaller;

public class RulesMapper {

    private static final Logger LOG = LoggerFactory.getLogger(RulesMapper.class);

    private static final DozerBeanMapper mapper = new DozerBeanMapper();
    private static final RulesMapper INSTANCE = new RulesMapper();

    public static RulesMapper getInstance() {
        return INSTANCE;
    }

    private RulesMapper() {
        mapper.setMappingFiles(getMapperFiles());
    }

    private List<String> getMapperFiles() {
        List<String> files = new ArrayList<>();
        files.add("movementbasetype.xml");
        files.add("movementtype.xml");
        return files;
    }

    public DozerBeanMapper getMapper() {
        return mapper;
    }

    public static MovementType mapCreateMovementToMovementType(TextMessage message) throws ModelMarshallException {
        CreateMovementResponse response = JAXBMarshaller.unmarshallTextMessage(message, CreateMovementResponse.class);
        return response.getMovement();
    }

}
