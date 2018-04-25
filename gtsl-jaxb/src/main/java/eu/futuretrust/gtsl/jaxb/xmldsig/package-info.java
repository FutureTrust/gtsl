/**
 * This package-info file is NOT overwritten when building the application, in order to ensure correct prefix
 * allocation to the elements defined under the XMLDSIG namespace.
 */

@XmlSchema(
        namespace = "http://www.w3.org/2000/09/xmldsig#",
        elementFormDefault = XmlNsForm.QUALIFIED,
        xmlns = {
                @XmlNs(prefix = "ds", namespaceURI = "http://www.w3.org/2000/09/xmldsig#")
        })

package eu.futuretrust.gtsl.jaxb.xmldsig;

import javax.xml.bind.annotation.*;