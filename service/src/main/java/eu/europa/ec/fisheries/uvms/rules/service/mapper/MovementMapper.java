package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import org.dozer.DozerBeanMapper;

public class MovementMapper {

    private static final DozerBeanMapper mapper = new DozerBeanMapper();

    private MovementMapper() {
    }

    public static DozerBeanMapper getMapper() {
        return mapper;
    }
}
