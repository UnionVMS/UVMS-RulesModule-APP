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
package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JAXBMarshaller {

    private final static Logger LOG = LoggerFactory.getLogger(JAXBMarshaller.class);

    private static Map<String, JAXBContext> contexts = new HashMap<>();

    /**
     * Marshalls a JAXB Object to a XML String representation
     *
     * @param <T>
     * @param data
     * @return
     * @throws
     * eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException
     */
    public static <T> String marshallJaxBObjectToString(T data) throws RulesModelMarshallException {
        try {
            JAXBContext jaxbContext = contexts.get(data.getClass().getName());
            if (jaxbContext == null) {
                long before = System.currentTimeMillis();
                jaxbContext = JAXBContext.newInstance(data.getClass());
                contexts.put(data.getClass().getName(), jaxbContext);
                LOG.debug("Stored contexts: {}", contexts.size());
                LOG.debug("JAXBContext creation time: {}", (System.currentTimeMillis() - before));
            }
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(data, sw);
            long before = System.currentTimeMillis();
            String marshalled = sw.toString();
            LOG.debug("StringWriter time: {}", (System.currentTimeMillis() - before));
            return marshalled;
        } catch (JAXBException ex) {
            LOG.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new RulesModelMarshallException("[ Error when marshalling Object to String ]", ex);        }
    }

    /**
     * Unmarshalls A textMessage to the desired Object. The object must be the
     * root object of the unmarchalled message!
     *
     * @param <R>
     * @param textMessage
     * @param clazz pperException
     * @return
     * @throws
     * eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException
     */
    public static <R> R unmarshallTextMessage(TextMessage textMessage, Class clazz) throws RulesModelMarshallException {
        try {
            JAXBContext jc = contexts.get(clazz.getName());
            if (jc == null) {
                long before = System.currentTimeMillis();
                jc = JAXBContext.newInstance(clazz);
                contexts.put(clazz.getName(), jc);
                LOG.debug("Stored contexts: {}", contexts.size());
                LOG.debug("JAXBContext creation time: {}", (System.currentTimeMillis() - before));
            }
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader sr = new StringReader(textMessage.getText());
            StreamSource source = new StreamSource(sr);
            long before = System.currentTimeMillis();
            R object = (R) unmarshaller.unmarshal(source);
            LOG.debug("Unmarshalling time: {}", (System.currentTimeMillis() - before));
            return object;
        } catch (JMSException | JAXBException ex) {
            throw new RulesModelMarshallException("[Error when unmarshalling response in ResponseMapper. Expected class was " + clazz.getName() + " ]", ex);
        }
    }

    public static <R> R unMarshallMessage(String textMessage, Class clazz, Schema schema) throws RulesModelMarshallException {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            if (schema != null){
                unmarshaller.setSchema(schema);
            }
            StringReader sr = new StringReader(textMessage);
            StreamSource source = new StreamSource(sr);
            long before = System.currentTimeMillis();
            R object = (R) unmarshaller.unmarshal(source);
            LOG.debug("Unmarshalling time: {}", (System.currentTimeMillis() - before));
            return object;
        } catch (JAXBException ex) {
            throw new RulesModelMarshallException("[Error when unmarshalling response in ResponseMapper. Expected class was " + clazz.getName() + " ]", ex);
        }
    }

    public static <R> R unMarshallMessage(String textMessage, Class clazz) throws RulesModelMarshallException {
        return unMarshallMessage(textMessage, clazz, null);
    }

}