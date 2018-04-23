package eu.futuretrust.gtsl.business.services.responder;

import eu.futuretrust.gtsl.business.dto.report.ReportDTO;
import eu.futuretrust.gtsl.business.services.helpers.DirectoryUtils;
import eu.futuretrust.gtsl.business.services.helpers.ResourcesUtils;
import eu.futuretrust.gtsl.business.services.responder.impl.ResponderServiceImpl;
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.business.services.xml.JaxbService;
import eu.futuretrust.gtsl.business.vo.draft.DraftVO;
import eu.futuretrust.gtsl.jaxb.tsl.TSPServiceTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.TrustStatusListTypeJAXB;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration
public class AbstractTestResponder {

  @Autowired
  protected ResponderService responderService;
  @Autowired
  protected TslService tslService;
  @Autowired
  protected JaxbService jaxbService;

  @Configuration
  static class ResponderContextConfiguration {

    @Bean
    public ResponderService responderService() throws JAXBException {
      return new ResponderServiceImpl(tslService());
    }

    @Bean
    public TslService tslService() throws JAXBException {
      return new TslServiceTest(jaxbService());
    }

    @Bean
    public JaxbService jaxbService() throws JAXBException {
      return new JaxbServiceTest();
    }
  }

  static class TslServiceTest implements TslService {

    private List<TrustStatusListType> tslList;

    public TslServiceTest(JaxbService jaxbService) {
      URL url = ResourcesUtils.loadURL("tsl");
      this.tslList = DirectoryUtils.getFiles(url.getPath())
          .map(file -> {
            try {
              return jaxbService.unmarshallTsl(file);
            } catch (IOException e) {
              return null;
            }
          })
          .filter(Objects::nonNull)
          .map(TrustStatusListType::new)
          .collect(Collectors.toList());
    }

    @Override
    public ReportDTO create(TrustStatusListType tsl) {
      return null;
    }

    @Override
    public ReportDTO update(TrustStatusListType tsl) {
      return null;
    }

    @Override
    public void remove(String tlIdentifier) {
    }

    @Override
    public Optional<byte[]> readBytes(String tlIdentifier) {
      return Optional.empty();
    }

    @Override
    public Optional<TrustStatusListType> read(String tlIdentifier) {
      if (StringUtils.isEmpty(tlIdentifier)) {
        return Optional.empty();
      }

      return tslList.stream().filter(
          tsl -> tlIdentifier.equals(tsl.getSchemeInformation().getSchemeTerritory().getValue()))
          .findFirst();
    }

    @Override
    public List<TrustStatusListType> readAll() {
      return tslList;
    }

    @Override
    public Optional<ReportDTO> validate(String territoryCode) {
      return Optional.empty();
    }

    @Override
    public ReportDTO push(DraftVO draft) {
      return null;
    }
  }

  static class JaxbServiceTest implements JaxbService {

    private Unmarshaller tslUnmarshaller;
    private Marshaller tslMarshaller;

    public JaxbServiceTest() throws JAXBException {
      JAXBContext jaxbContext = JAXBContext.newInstance(
          eu.futuretrust.gtsl.jaxb.tsl.ObjectFactory.class,
          eu.futuretrust.gtsl.jaxb.additionaltypes.ObjectFactory.class,
          eu.futuretrust.gtsl.jaxb.sie.ObjectFactory.class,
          eu.futuretrust.gtsl.jaxb.xades.ObjectFactory.class,
          eu.futuretrust.gtsl.jaxb.xmldsig.ObjectFactory.class);
      tslUnmarshaller = jaxbContext.createUnmarshaller();
      tslMarshaller = jaxbContext.createMarshaller();
    }

    @SuppressWarnings("unchecked")
    @Override
    public TrustStatusListTypeJAXB unmarshallTsl(InputStream is) {
      TrustStatusListTypeJAXB tsl = null;
      try {
        JAXBElement<TrustStatusListTypeJAXB> jaxbElement = (JAXBElement<TrustStatusListTypeJAXB>) tslUnmarshaller
            .unmarshal(new StreamSource(is));
        tsl = jaxbElement.getValue();
      } catch (JAXBException e) {
        e.printStackTrace();
      }
      return tsl;
    }

    @Override
    public TrustStatusListTypeJAXB unmarshallTsl(File file) {
      TrustStatusListTypeJAXB tsl = null;
      InputStream is = null;
      try {
        is = new FileInputStream(file);
        tsl = unmarshallTsl(is);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } finally {
        IOUtils.closeQuietly(is);
      }
      return tsl;
    }

    @Override
    public TrustStatusListTypeJAXB unmarshallTsl(byte[] content) {
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
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      marshallTsl(tsl.asJAXB(), out);
      return out.toByteArray();
    }

    @Override
    public byte[] marshallServiceToBytes(TSPServiceType service) {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      marshallService(service.asJAXB(), out);
      return out.toByteArray();
    }

    private void marshallService(TSPServiceTypeJAXB service, OutputStream os)
        throws XmlMappingException {
      eu.futuretrust.gtsl.jaxb.tsl.ObjectFactory factory = new eu.futuretrust.gtsl.jaxb.tsl.ObjectFactory();
      JAXBElement<TSPServiceTypeJAXB> jaxbElement = factory.createTSPService(service);
      try {
        tslMarshaller.marshal(jaxbElement, new StreamResult(os));
      } catch (JAXBException e) {
        e.printStackTrace();
      }
    }

    private void marshallTsl(TrustStatusListTypeJAXB tsl, OutputStream os)
        throws XmlMappingException {
      eu.futuretrust.gtsl.jaxb.tsl.ObjectFactory factory = new eu.futuretrust.gtsl.jaxb.tsl.ObjectFactory();
      JAXBElement<TrustStatusListTypeJAXB> jaxbElement = factory.createTrustServiceStatusList(tsl);
      try {
        tslMarshaller.marshal(jaxbElement, new StreamResult(os));
      } catch (JAXBException e) {
        e.printStackTrace();
      }
    }

  }

}
