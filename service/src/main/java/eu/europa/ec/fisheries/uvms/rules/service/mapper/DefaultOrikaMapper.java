package eu.europa.ec.fisheries.uvms.rules.service.mapper;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;

import eu.europa.ec.fisheries.schema.sales.CodeType;
import eu.europa.ec.fisheries.schema.sales.IDType;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

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

        factory.classMap(IDType.class, eu.europa.ec.fisheries.uvms.rules.service.business.fact.IdType.class)
                .byDefault()
                .field("schemeID", "schemeId")
                .register();

        mapperFacade = factory.getMapperFacade();
    }

    @Produces
    public MapperFacade getMapper() {
        return mapperFacade;
    }


}
