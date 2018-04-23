package eu.futuretrust.gtsl.business.services.responder;

import eu.futuretrust.gtsl.business.services.helpers.ResourcesUtils;
import eu.futuretrust.gtsl.jaxb.tsl.TSPServiceTypeJAXB;
import eu.futuretrust.gtsl.jaxb.tsl.TrustStatusListTypeJAXB;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TestResponder extends AbstractTestResponder {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void valid() throws Exception {
    String certificate = CertificateExample.FR.SERVICE_1;
    Optional<TSPServiceType> optionalService = responderService
        .validate(certificate);
    Assert.assertTrue(optionalService.isPresent());

    TSPServiceType service = optionalService.get();
    Assert.assertNotNull(service);
    Assert.assertTrue(contains(service, certificate));

    TrustStatusListTypeJAXB tslJAXB = jaxbService
        .unmarshallTsl(ResourcesUtils.loadInputStream("tsl/TL - FR.xml"));
    assertService(
        tslJAXB.getTrustServiceProviderList().getTrustServiceProvider().get(0).getTSPServices()
            .getTSPService().get(0),
        service.asJAXB());
  }

  @Test
  public void safeValid() throws Exception {
    String certificate = CertificateExample.FR.SERVICE_1;
    String dirtyCertificate = "\n\n" + CertificateExample.PREFIX + "\n\n"
        + certificate
        + "\n\n" + CertificateExample.SUFFIX + "\n\n";
    Optional<TSPServiceType> optionalService = responderService
        .validate(dirtyCertificate);
    Assert.assertTrue(optionalService.isPresent());

    TSPServiceType service = optionalService.get();
    Assert.assertNotNull(service);
    Assert.assertTrue(contains(service, certificate));

    TrustStatusListTypeJAXB tslJAXB = jaxbService
        .unmarshallTsl(ResourcesUtils.loadInputStream("tsl/TL - FR.xml"));
    assertService(
        tslJAXB.getTrustServiceProviderList().getTrustServiceProvider().get(0).getTSPServices()
            .getTSPService().get(0),
        service.asJAXB());
  }

  @Test
  public void invalid() throws Exception {
    thrown.expect(CertificateException.class);

    String certificate = CertificateExample.INVALID;
    responderService.validate(certificate);
  }

  @Test
  public void notFound() throws Exception {
    String certificate = CertificateExample.NOT_PRESENT;
    Optional<TSPServiceType> optionalService = responderService
        .validate(certificate);
    Assert.assertFalse(optionalService.isPresent());
  }

  private boolean contains(TSPServiceType service, String certificate) {
    return service.getServiceInformation().getServiceDigitalIdentity().getValues().stream()
        .flatMap(digitalId -> digitalId.getCertificateList().stream())
        .anyMatch(cert -> certificate.equals(cert.getCertB64()));
  }

  private void assertService(TSPServiceTypeJAXB expected, TSPServiceTypeJAXB actual) {
    Assert.assertEquals(expected.getServiceInformation().getServiceStatus(),
        expected.getServiceInformation().getServiceStatus());
    Assert.assertEquals(expected.getServiceInformation().getServiceTypeIdentifier(),
        expected.getServiceInformation().getServiceTypeIdentifier());
    Assert.assertEquals(expected.getServiceInformation().getStatusStartingTime(),
        expected.getServiceInformation().getStatusStartingTime());
    Assert.assertTrue(
        expected.getServiceInformation().getServiceName().getName().stream()
            .allMatch(
                expectedName ->
                    actual.getServiceInformation().getServiceName().getName().stream()
                        .anyMatch(
                            actualName ->
                                expectedName.getLang().equals(actualName.getLang())
                                    && expectedName.getValue().equals(actualName.getValue())
                        )
            )
    );
    Assert.assertTrue(
        expected.getServiceInformation().getSchemeServiceDefinitionURI().getURI().stream()
            .allMatch(
                expectedUri ->
                    actual.getServiceInformation().getSchemeServiceDefinitionURI().getURI().stream()
                        .anyMatch(
                            actualUri ->
                                expectedUri.getLang().equals(actualUri.getLang())
                                    && expectedUri.getValue().equals(actualUri.getValue())
                        )
            )
    );
    Assert.assertTrue(
        expected.getServiceInformation().getTSPServiceDefinitionURI().getURI().stream()
            .allMatch(
                expectedUri ->
                    actual.getServiceInformation().getTSPServiceDefinitionURI().getURI().stream()
                        .anyMatch(
                            actualUri ->
                                expectedUri.getLang().equals(actualUri.getLang())
                                    && expectedUri.getValue().equals(actualUri.getValue())
                        )
            )
    );
    Assert.assertTrue(
        expected.getServiceInformation().getServiceDigitalIdentity().getDigitalId().stream()
            .allMatch(
                expectedCertificate ->
                    actual.getServiceInformation().getServiceDigitalIdentity().getDigitalId().stream()
                        .anyMatch(
                            actualCertificate ->
                                Arrays.equals(expectedCertificate.getX509Certificate(),
                                    actualCertificate.getX509Certificate())
                        )
            )
    );
  }

}
