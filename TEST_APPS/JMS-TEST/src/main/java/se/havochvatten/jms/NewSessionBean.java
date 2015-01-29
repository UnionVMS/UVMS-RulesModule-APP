/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.havochvatten.jms;

import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

/**
 *
 * @author martin
 */
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

    private AtomicLong sent = new AtomicLong();

    @PostConstruct
    public void init() {
        System.out.println("JMS producer bean started");
        System.out.println("Sending messsage");
        try {
            System.out.println("Checking context ok for queue" + queueExample.getQueueName());
            System.out.println(context.getMetaData().getJMSProviderName());
            System.out.println(context.getMetaData().getJMSMajorVersion());
        } catch (Exception e) {
            System.out.println("Error when sending JMS message [ ERROR ] " + e.getMessage());
        }
    }

    @Schedule(second = "*/10", minute = "*", hour = "*")
    public void doIt() {
        String message = "Hello!! " + sent.addAndGet(1);
        context.createProducer().send(queueExample, message);
        System.out.println(" Message sent from producer: " + message);

    }

}
