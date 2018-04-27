
/*
 * Copyright (c) 2017 European Commission.
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence").
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl5
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence
 * is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * See the Licence for the specific language governing permissions and limitations under the
 * Licence.
 */

package eu.futuretrust.gtsl.vat;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.futuretrust.gtsl.vat package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CheckVatResponseName_QNAME = new QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types", "name");
    private final static QName _CheckVatResponseAddress_QNAME = new QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types", "address");
    private final static QName _CheckVatApproxResponseTraderName_QNAME = new QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderName");
    private final static QName _CheckVatApproxResponseTraderCompanyType_QNAME = new QName("urn:ec.europa.eu:taxud:vies:services:checkVat:types", "traderCompanyType");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.futuretrust.gtsl.vat
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CheckVat }
     * 
     */
    public CheckVat createCheckVat() {
        return new CheckVat();
    }

    /**
     * Create an instance of {@link CheckVatResponse }
     * 
     */
    public CheckVatResponse createCheckVatResponse() {
        return new CheckVatResponse();
    }

    /**
     * Create an instance of {@link CheckVatApprox }
     * 
     */
    public CheckVatApprox createCheckVatApprox() {
        return new CheckVatApprox();
    }

    /**
     * Create an instance of {@link CheckVatApproxResponse }
     * 
     */
    public CheckVatApproxResponse createCheckVatApproxResponse() {
        return new CheckVatApproxResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "urn:ec.europa.eu:taxud:vies:services:checkVat:types", name = "name", scope = CheckVatResponse.class)
    public JAXBElement<String> createCheckVatResponseName(String value) {
        return new JAXBElement<String>(_CheckVatResponseName_QNAME, String.class, CheckVatResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "urn:ec.europa.eu:taxud:vies:services:checkVat:types", name = "address", scope = CheckVatResponse.class)
    public JAXBElement<String> createCheckVatResponseAddress(String value) {
        return new JAXBElement<String>(_CheckVatResponseAddress_QNAME, String.class, CheckVatResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "urn:ec.europa.eu:taxud:vies:services:checkVat:types", name = "traderName", scope = CheckVatApproxResponse.class)
    public JAXBElement<String> createCheckVatApproxResponseTraderName(String value) {
        return new JAXBElement<String>(_CheckVatApproxResponseTraderName_QNAME, String.class, CheckVatApproxResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "urn:ec.europa.eu:taxud:vies:services:checkVat:types", name = "traderCompanyType", scope = CheckVatApproxResponse.class)
    public JAXBElement<String> createCheckVatApproxResponseTraderCompanyType(String value) {
        return new JAXBElement<String>(_CheckVatApproxResponseTraderCompanyType_QNAME, String.class, CheckVatApproxResponse.class, value);
    }

}
