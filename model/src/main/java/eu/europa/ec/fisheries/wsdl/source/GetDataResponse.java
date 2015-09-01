package eu.europa.ec.fisheries.wsdl.source;

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
 *         &lt;element name="vessel" type="{types.wsdl.fisheries.ec.europa.eu}ModuleObject"/>
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
    "vessel"
})
@XmlRootElement(name = "GetDataResponse")
public class GetDataResponse
        implements Serializable {

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected ModuleObject vessel;

    /**
     * Gets the value of the vessel property.
     *
     * @return possible object is {@link ModuleObject }
     *
     */
    public ModuleObject getVessel() {
        return vessel;
    }

    /**
     * Sets the value of the vessel property.
     *
     * @param value allowed object is {@link ModuleObject }
     *
     */
    public void setVessel(ModuleObject value) {
        this.vessel = value;
    }

}
