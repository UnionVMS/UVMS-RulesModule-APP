package eu.europa.ec.fisheries.uvms.rules.service.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

//@formatter:off
@Entity
@Table(name = "erroritem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ErrorItem.findById", query = "SELECT e FROM ErrorItem e WHERE e.id = :id")
})
//@formatter:on
public class ErrorItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "err_id")
    private Integer id;

    @JoinColumn(name = "err_errep_id", referencedColumnName = "errep_id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ErrorReport errorReport;

    @Column(name = "err_comment")
    private String comment;

    @Column(name = "err_updattim")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Column(name = "err_upuser")
    @NotNull
    private String updatedBy;

    public ErrorItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ErrorReport getErrorReport() {
        return errorReport;
    }

    public void setErrorReport(ErrorReport errorReport) {
        this.errorReport = errorReport;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

}
