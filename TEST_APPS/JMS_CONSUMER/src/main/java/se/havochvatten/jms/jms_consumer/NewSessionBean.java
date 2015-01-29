package se.havochvatten.jms.jms_consumer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

@Singleton
@Startup
public class NewSessionBean {

    private static final String connectionFactory = "java:jboss/DefaultJMSConnectionFactory";
    private static final String queueName = "java:/jms/queue/ExpiryQueue";

    @Resource(mappedName = queueName)
    private Queue queueExample;

    @Inject
    @JMSConnectionFactory(connectionFactory)
    JMSContext context;

    @PostConstruct
    public void init() {
        System.out.println("JMS consumer bean started");
    }

    @Schedule(second = "*/10", minute = "*", hour = "*")
    public void doIt() {
        String message = context.createConsumer(queueExample).receiveBody(String.class);
        System.out.println(" Message recieved from producer SCHEDULE [ 1 ] " + message);
    }

    @Schedule(second = "*/10", minute = "*", hour = "*")
    public void doItAgain() {
        String message = context.createConsumer(queueExample).receiveBody(String.class);
        System.out.println(" Message recieved from producer SCHEDULE [ 2 ] " + message);
    }

}
