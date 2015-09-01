package eu.europa.ec.fisheries.wsdl.types;

import java.io.Serializable;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="fault" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "code",
    "fault"
})
@XmlRootElement(name = "ModuleObjectException")
public class ModuleObjectException
        implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected BigInteger code;
    @XmlElement(required = true)
    protected String fault;

    /**
     * Gets the value of the code property.
     *
     * @return possible object is {@link BigInteger }
     *
     */
    public BigInteger getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value allowed object is {@link BigInteger }
     *
     */
    public void setCode(BigInteger value) {
        this.code = value;
    }

    /**
     * Gets the value of the fault property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getFault() {
        return fault;
    }

    /**
     * Sets the value of the fault property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setFault(String value) {
        this.fault = value;
    }

}
