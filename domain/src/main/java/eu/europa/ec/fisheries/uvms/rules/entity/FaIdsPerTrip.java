package eu.europa.ec.fisheries.uvms.rules.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;


@NamedQueries({
        @NamedQuery(name = FaIdsPerTrip.BY_TRIPIDSCHEMEIDFATYPEREPORTTYPE,
                query = "FROM FaIdsPerTrip faId WHERE faId.tripIdSchemeidFaTypeReportType in (:idsList)")
})
@Entity
@Table(name = "faidspertrip")
@Data
@EqualsAndHashCode(exclude = "id")
public class FaIdsPerTrip implements Serializable {

    public static final String BY_TRIPIDSCHEMEIDFATYPEREPORTTYPE = "by.tripidschemeidfatypereporttype";
    private static final String DASH = "-";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "trip_id_scheme_id_fatype_report_type")
    private String tripIdSchemeidFaTypeReportType;

    public FaIdsPerTrip() {}

    public FaIdsPerTrip(String tripIdSchemeidFaTypeReportType) {
        this.tripIdSchemeidFaTypeReportType = tripIdSchemeidFaTypeReportType;
    }
}
