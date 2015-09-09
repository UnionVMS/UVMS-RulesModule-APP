package eu.europa.ec.fisheries.uvms.rules.service.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

//@formatter:off
@Entity
@Table(name = "errorreport")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ErrorReport.findById", query = "SELECT er FROM ErrorReport er WHERE er.id = :id"),
    @NamedQuery(name = "ErrorReport.findByGuid", query = "SELECT er FROM ErrorReport er WHERE er.guid = :guid")
})
//@formatter:on
public class ErrorReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "errep_id")
    private Integer id;

    @Column(name = "errep_guid_ref")
    private String guid;

    @Column(name = "errep_updattim")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Column(name = "errep_upuser")
    @NotNull
    private String updatedBy;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "errorReport", fetch = FetchType.LAZY)
    private List<ErrorItem> errorList;

    public ErrorReport() {
    }

    public ErrorReport(String guid) {
        this.guid = guid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<ErrorItem> getErrorList() {
        if (errorList == null) {
            errorList = new ArrayList<ErrorItem>();
        }
        return errorList;
    }

    public void setErrorList(List<ErrorItem> errorList) {
        this.errorList = errorList;
    }

}
