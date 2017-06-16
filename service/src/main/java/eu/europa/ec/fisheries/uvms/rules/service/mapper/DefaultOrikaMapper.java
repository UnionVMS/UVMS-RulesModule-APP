package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import eu.europa.ec.fisheries.schema.sales.CodeType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;

@Singleton
public class DefaultOrikaMapper {

    private MapperFacade mapperFacade;

    public DefaultOrikaMapper() {
        MapperFactory factory = new DefaultMapperFactory.Builder()
                .mapNulls(false)
                .build();

        ConverterFactory converterFactory = factory.getConverterFactory();
        converterFactory.registerConverter(new PassThroughConverter(org.joda.time.DateTime.class));

        factory.classMap(CodeType.class, eu.europa.ec.fisheries.uvms.rules.service.business.fact.CodeType.class)
                .byDefault()
                .field("listID", "listId")
                .register();

        mapperFacade = factory.getMapperFacade();
    }

    @Produces
    public MapperFacade getMapper() {
        return mapperFacade;
    }


}
