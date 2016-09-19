/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.message.producer.bean;

import eu.europa.ec.fisheries.uvms.rules.message.constants.MessageConstants;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.*;

@Startup
@Singleton
public class JMSConnectorBean {
    final static org.slf4j.Logger LOG = LoggerFactory.getLogger(JMSConnectorBean.class);

    @Resource(lookup = MessageConstants.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    private Connection connection;

    @PostConstruct
    private void connectToQueue() {
        LOG.debug("Open connection to JMS broker");
        try {
            connection = connectionFactory.createConnection();
            connection.start();
        } catch (JMSException ex) {
            LOG.error("Error when open connection to JMS broker");
        }
    }

    public Session getNewSession() throws JMSException {
        if (connection == null) {
            connectToQueue();
        }
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        return session;
    }

    public TextMessage createTextMessage(Session session, String message) throws JMSException {
        return session.createTextMessage(message);
    }

    @PreDestroy
    private void closeConnection() {
        LOG.debug("Close connection to JMS broker");
        try {
            if (connection != null) {
                //connection.stop();
                connection.close();
            }
        } catch (JMSException e) {
            LOG.warn("[ Error when stopping or closing JMS connection. ] {}", e.getMessage());
        }
    }

}