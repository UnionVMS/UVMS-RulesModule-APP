package eu.europa.ec.fisheries.uvms.rules.service.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * The persistent class for the parameter database table.
 *
 */
@Entity
public class Parameter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "param_id")
    private String paramId;

    @Column(name = "param_description")
    private String paramDescription;

    @Column(name = "param_value")
    private String paramValue;

    public Parameter() {
    }

    public String getParamId() {
        return this.paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamDescription() {
        return this.paramDescription;
    }

    public void setParamDescription(String paramDescription) {
        this.paramDescription = paramDescription;
    }

    public String getParamValue() {
        return this.paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

}
