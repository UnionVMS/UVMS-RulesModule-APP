package eu.europa.ec.fisheries.uvms.rules.message.producer.bean;

import eu.europa.ec.fisheries.uvms.rules.message.constants.MessageConstants;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.jms.*;

//@Startup
//@Singleton
@Stateless
public class JMSConnectorBean {
    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(JMSConnectorBean.class);

    @Resource(lookup = MessageConstants.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    private Connection connection;
    private Session session;
    private MessageProducer producer;

    @PostConstruct
    private void connectToQueue() {
        LOG.debug("Trying to connect to JMS broker in Rules module");
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(null);
        } catch (JMSException ex) {
            LOG.debug("Failure when connecting to JMS broker in Rules module");
        }
    }

//        public Session getNewSession() throws JMSException {
//            if (connection == null) {
//                connectToQueue();
//            }
//            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
//            return session;
//        }

    public TextMessage createTextMessage(String message) throws JMSException {
        return session.createTextMessage(message);
    }

    public void sendMessage(Destination destination, TextMessage message) throws JMSException {
        producer.send(destination, message);
    }

    @PreDestroy
    private void closeConnection() {
        try {
            if (connection != null) {
                connection.stop();
                connection.close();
            }
        } catch (JMSException e) {
            LOG.warn("[ Error when stopping or closing JMS queue. ] {}", e.getMessage());
        }
    }

}
