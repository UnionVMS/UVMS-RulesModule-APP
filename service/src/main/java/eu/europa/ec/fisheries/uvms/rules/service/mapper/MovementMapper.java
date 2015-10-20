package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovementMapper {

    private static final Logger LOG = LoggerFactory.getLogger(MovementMapper.class);

    private static final DozerBeanMapper mapper = new DozerBeanMapper();
    private static final MovementMapper INSTANCE = new MovementMapper();

    public static MovementMapper getInstance() {
        return INSTANCE;
    }

    private MovementMapper() {
        mapper.setMappingFiles(getMapperFiles());
    }

    private List<String> getMapperFiles() {
        List<String> files = new ArrayList<>();
        files.add("movementbasetype.xml");
        return files;
    }

    public DozerBeanMapper getMapper() {
        return mapper;
    }

}
