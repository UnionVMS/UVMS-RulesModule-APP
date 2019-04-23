package eu.europa.ec.fisheries.uvms.rules;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.junit.Test;
import org.reflections.Reflections;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class HibernateExporter {

    private static final String OUTPUT_FILE = "schema.sql";
    private static final String DIALECT = "org.hibernate.dialect.H2Dialect";

    private List<String> entityPackages;

    @Test
    public void exportJob() {
        final List<String> entityPackages = Collections.singletonList("eu.europa.ec.fisheries.uvms.rules.entity");
        HibernateExporter exporter = new HibernateExporter();
        exporter.setEntityPackages(entityPackages);
        exporter.export();
    }


    private void export() {
        SchemaExport export = new SchemaExport();
        export.setOutputFile(OUTPUT_FILE);
        export.setFormat(true);
        export.setDelimiter(";");
        EnumSet<TargetType> types = EnumSet.of(TargetType.SCRIPT);
        Metadata metadata = createMetadataSources().buildMetadata();
        export.execute(types, SchemaExport.Action.CREATE, metadata);
    }

    private MetadataSources createMetadataSources() {
        MetadataSources metadata = new MetadataSources(
                new StandardServiceRegistryBuilder()
                        .applySetting("hibernate.dialect", DIALECT)
                        .build());

        for (String entityPackage : entityPackages) {
            final Reflections reflections = new Reflections(entityPackage);
            for (Class<?> cl : reflections.getTypesAnnotatedWith(MappedSuperclass.class)) {
                metadata.addAnnotatedClass(cl);
                System.out.println(String.format("Mapped = %s", cl.getName()));
            }
            for (Class<?> cl : reflections.getTypesAnnotatedWith(Entity.class)) {
                metadata.addAnnotatedClass(cl);
                System.out.println(String.format("Mapped = %s", cl.getName()));
            }
        }
        return metadata;
    }

    public void setEntityPackages(List<String> entityPackages) {
        this.entityPackages = entityPackages;
    }
}
