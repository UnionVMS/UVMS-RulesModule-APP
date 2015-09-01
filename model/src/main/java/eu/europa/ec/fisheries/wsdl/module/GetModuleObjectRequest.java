package eu.europa.ec.fisheries.wsdl.module;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import eu.europa.ec.fisheries.wsdl.types.ModuleObject;

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
 *         &lt;element name="object" type="{types.wsdl.fisheries.ec.europa.eu}ModuleObject"/>
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
    "object"
})
@XmlRootElement(name = "GetModuleObjectRequest")
public class GetModuleObjectRequest
        implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected ModuleObject object;

    /**
     * Gets the value of the object property.
     *
     * @return possible object is {@link ModuleObject }
     *
     */
    public ModuleObject getObject() {
        return object;
    }

    /**
     * Sets the value of the object property.
     *
     * @param value allowed object is {@link ModuleObject }
     *
     */
    public void setObject(ModuleObject value) {
        this.object = value;
    }

}
