package eu.europa.ec.fisheries.uvms.rules.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "data_flow_expression",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"data_flow", "expression"})})
@ToString
@Data
public class DataFlowAndExpression {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rule_id", referencedColumnName = "rule_id")
    private Rule rule;

    @Column(name = "data_flow")
    private String dataFlow;

    @Column(name = "expression", nullable = false)
    private String expression;

    @Column(name = "failure_message")
    private String failureMessage;

}
