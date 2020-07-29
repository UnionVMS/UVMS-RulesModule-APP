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

import eu.europa.ec.fisheries.uvms.commons.xml.AbstractJAXBMarshaller;
import eu.europa.ec.fisheries.uvms.commons.xml.JAXBRuntimeException;
import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.util.HashMap;
import java.util.Map;

public class JAXBMarshaller extends AbstractJAXBMarshaller {

    private final static Logger LOG = LoggerFactory.getLogger(JAXBMarshaller.class);

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
            Map<String,Object> properties = new HashMap<>();
            properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            return marshallToString(data, properties);
        } catch (JAXBException | JAXBRuntimeException ex) {
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
    public static <R> R unmarshallTextMessage(TextMessage textMessage, Class<R> clazz) throws RulesModelMarshallException {
        try {
            return unMarshallMessage(textMessage.getText(),clazz);
        } catch (JMSException ex) {
            throw new RulesModelMarshallException("[Error when unmarshalling response in ResponseMapper. Expected class was " + clazz.getName() + " ]", ex);
        }
    }

    public static <R> R unMarshallMessage(String textMessage, Class<R> clazz) throws RulesModelMarshallException {
        return unMarshallMessage(textMessage, clazz,null);
    }

    public static <R> R unMarshallMessage(String textMessage, Class<R> clazz, Schema schema) throws RulesModelMarshallException {
        try{
            return unmarshallTo(textMessage, clazz, schema);
        } catch (JAXBException | JAXBRuntimeException ex) {
            throw new RulesModelMarshallException("[Error when unmarshalling response in ResponseMapper. Expected class was " + clazz.getName() + " ]", ex);
        }
    }
}
