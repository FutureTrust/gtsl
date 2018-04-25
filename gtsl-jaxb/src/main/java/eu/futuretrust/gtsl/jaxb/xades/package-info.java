/**
 * This package-info file is NOT overwritten when building the application, in order to ensure correct prefix
 * allocation to the elements defined under the XAdES namespace.
 */

@XmlSchema(
        namespace = "http://uri.etsi.org/01903/v1.3.2#",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix = "xades", namespaceURI = "http://uri.etsi.org/01903/v1.3.2#")
        })

package eu.futuretrust.gtsl.jaxb.xades;
import javax.xml.bind.annotation.*;