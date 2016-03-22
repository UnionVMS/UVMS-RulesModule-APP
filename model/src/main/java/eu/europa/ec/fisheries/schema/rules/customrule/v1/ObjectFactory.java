
package eu.europa.ec.fisheries.schema.rules.customrule.v1;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.europa.ec.fisheries.schema.rules.customrule.v1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.europa.ec.fisheries.schema.rules.customrule.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CustomRuleType }
     * 
     */
    public CustomRuleType createCustomRuleType() {
        return new CustomRuleType();
    }

    /**
     * Create an instance of {@link CustomRuleSegmentType }
     * 
     */
    public CustomRuleSegmentType createCustomRuleSegmentType() {
        return new CustomRuleSegmentType();
    }

    /**
     * Create an instance of {@link CustomRuleActionType }
     * 
     */
    public CustomRuleActionType createCustomRuleActionType() {
        return new CustomRuleActionType();
    }

    /**
     * Create an instance of {@link UpdateSubscriptionType }
     * 
     */
    public UpdateSubscriptionType createUpdateSubscriptionType() {
        return new UpdateSubscriptionType();
    }

    /**
     * Create an instance of {@link CustomRuleIntervalType }
     * 
     */
    public CustomRuleIntervalType createCustomRuleIntervalType() {
        return new CustomRuleIntervalType();
    }

    /**
     * Create an instance of {@link SubscriptionType }
     * 
     */
    public SubscriptionType createSubscriptionType() {
        return new SubscriptionType();
    }

    /**
     * Create an instance of {@link SanityRuleType }
     * 
     */
    public SanityRuleType createSanityRuleType() {
        return new SanityRuleType();
    }

}
