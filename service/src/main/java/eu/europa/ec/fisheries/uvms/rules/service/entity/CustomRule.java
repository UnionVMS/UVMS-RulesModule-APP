package eu.europa.ec.fisheries.uvms.rules.service.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import eu.europa.ec.fisheries.uvms.rules.service.constants.ServiceConstants;

//@formatter:off
@Entity
@Table(name = "customrule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = ServiceConstants.FIND_ALL_CUSTOM_RULES, query = "SELECT r FROM CustomRule r"),
    @NamedQuery(name = ServiceConstants.FIND_CUSTOM_RULE_BY_ID, query = "SELECT r FROM CustomRule r WHERE r.id = :id")
})
//@formatter:on
public class CustomRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Integer id;

    @Column(name = "rule_attribute")
    private String attribute;

    @Column(name = "rule_operator")
    private String operator;

    @Column(name = "rule_value")
    private String value;

    @Column(name = "rule_action")
    private String action;

    @Column(name = "rule_owner")
    private String owner;

    public CustomRule() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
