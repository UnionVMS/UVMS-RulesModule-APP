package eu.europa.ec.fisheries.uvms.rules.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "context_expression",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"context", "expression"})})
@ToString
@Data
public class RuleContextExpression {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rule_id", referencedColumnName = "rule_id")
    private Rule rule;

    @Column(name = "context")
    private String context;

    @Column(name = "expression", nullable = false)
    private String expression;

    @Column(name = "failure_message")
    private String failureMessage;

}
