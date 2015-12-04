package eu.europa.ec.fisheries.uvms.rules.model.mapper;

import eu.europa.ec.fisheries.uvms.rules.model.exception.RulesModelMarshallException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JAXBMarshaller {

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
            JAXBContext jaxbContext = JAXBContext.newInstance(data.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(data, sw);
            return sw.toString();
        } catch (JAXBException ex) {
            LOG.error("[ Error when marshalling object to string ] {} ", ex.getMessage());
            throw new RulesModelMarshallException("[ Error when marshalling Object to String ]", ex);
        }
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
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader sr = new StringReader(textMessage.getText());
            return (R) unmarshaller.unmarshal(sr);
        } catch (JMSException | JAXBException ex) {
            throw new RulesModelMarshallException("[Error when unmarshalling response in ResponseMapper ]", ex);
        }
    }

}
