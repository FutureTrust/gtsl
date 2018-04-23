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

package eu.futuretrust.gtsl.business.services.xml.impl;


import eu.futuretrust.gtsl.business.services.xml.JaxbService;
import eu.futuretrust.gtsl.jaxb.tsl.ObjectFactory;
import eu.futuretrust.gtsl.jaxb.tsl.TSPServiceTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.TrustStatusListTypeJAXB;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.stereotype.Service;

@Service
public class JaxbServiceImpl implements JaxbService {

  private static final Logger LOGGER = LoggerFactory.getLogger(JaxbServiceImpl.class);

  @Autowired
  @Qualifier(value = "tslMarshaller")
  private Unmarshaller tslUnmarshaller;

  @Autowired
  @Qualifier(value = "tslMarshaller")
  private Marshaller tslMarshaller;

  @SuppressWarnings("unchecked")
  public TrustStatusListTypeJAXB unmarshallTsl(InputStream is)
      throws XmlMappingException, IOException {
    JAXBElement<TrustStatusListTypeJAXB> jaxbElement = (JAXBElement<TrustStatusListTypeJAXB>) tslUnmarshaller
        .unmarshal(new StreamSource(is));
    return jaxbElement.getValue();
  }

  @Override
  public TrustStatusListTypeJAXB unmarshallTsl(File file) throws XmlMappingException, IOException {
    TrustStatusListTypeJAXB tsl;
    InputStream is = null;
    try {
      is = new FileInputStream(file);
      tsl = unmarshallTsl(is);
    } finally {
      IOUtils.closeQuietly(is);
    }
    return tsl;
  }

  @Override
  public TrustStatusListTypeJAXB unmarshallTsl(byte[] content)
      throws XmlMappingException, IOException {
    TrustStatusListTypeJAXB tsl;
    InputStream is = null;
    try {
      is = new ByteArrayInputStream(content);
      tsl = unmarshallTsl(is);
    } finally {
      IOUtils.closeQuietly(is);
    }
    return tsl;
  }

  @Override
  public byte[] marshallTslToBytes(TrustStatusListType tsl) {
    byte[] byteArray = null;
    ByteArrayOutputStream out;
    try {
      out = new ByteArrayOutputStream();
      marshallTsl(tsl.asJAXB(), out);
      byteArray = out.toByteArray();
    } catch (Exception e) {
      LOGGER.error("Unable to marshal Tsl model : " + e.getMessage(), e);
    }
    return byteArray;
  }

  private void marshallTsl(TrustStatusListTypeJAXB tsl, OutputStream os)
      throws XmlMappingException, IOException {
    JAXBElement<TrustStatusListTypeJAXB> jaxbElement = new ObjectFactory().createTrustServiceStatusList(tsl);
    tslMarshaller.marshal(jaxbElement, new StreamResult(os));
  }

  @Override
  public byte[] marshallServiceToBytes(TSPServiceType service) {
    byte[] byteArray = null;
    ByteArrayOutputStream out;
    try {
      out = new ByteArrayOutputStream();
      marshallService(service.asJAXB(), out);
      byteArray = out.toByteArray();
    } catch (Exception e) {
      LOGGER.error("Unable to marshal Tsl model : " + e.getMessage(), e);
    }
    return byteArray;
  }

  private void marshallService(TSPServiceTypeJAXB service, OutputStream os)
      throws XmlMappingException, IOException {
    JAXBElement<TSPServiceTypeJAXB> jaxbElement = new ObjectFactory().createTSPService(service);
    tslMarshaller.marshal(jaxbElement, new StreamResult(os));
  }
  
}
