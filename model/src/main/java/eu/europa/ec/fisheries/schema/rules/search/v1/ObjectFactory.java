
package eu.europa.ec.fisheries.schema.rules.search.v1;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.europa.ec.fisheries.schema.rules.search.v1 package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.europa.ec.fisheries.schema.rules.search.v1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListPagination }
     * 
     */
    public ListPagination createListPagination() {
        return new ListPagination();
    }

    /**
     * Create an instance of {@link AlarmListCriteria }
     * 
     */
    public AlarmListCriteria createAlarmListCriteria() {
        return new AlarmListCriteria();
    }

    /**
     * Create an instance of {@link CustomRuleQuery }
     * 
     */
    public CustomRuleQuery createCustomRuleQuery() {
        return new CustomRuleQuery();
    }

    /**
     * Create an instance of {@link AlarmQuery }
     * 
     */
    public AlarmQuery createAlarmQuery() {
        return new AlarmQuery();
    }

    /**
     * Create an instance of {@link TicketQuery }
     * 
     */
    public TicketQuery createTicketQuery() {
        return new TicketQuery();
    }

    /**
     * Create an instance of {@link CustomRuleListCriteria }
     * 
     */
    public CustomRuleListCriteria createCustomRuleListCriteria() {
        return new CustomRuleListCriteria();
    }

    /**
     * Create an instance of {@link TicketListCriteria }
     * 
     */
    public TicketListCriteria createTicketListCriteria() {
        return new TicketListCriteria();
    }

}
