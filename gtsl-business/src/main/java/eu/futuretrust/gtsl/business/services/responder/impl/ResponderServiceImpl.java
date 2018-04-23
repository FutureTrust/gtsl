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

package eu.futuretrust.gtsl.business.services.responder.impl;

import eu.futuretrust.gtsl.business.services.responder.ResponderService;
import eu.futuretrust.gtsl.business.services.tsl.TslService;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "responderService")
public class ResponderServiceImpl implements ResponderService {

  private static final Logger LOGGER = LoggerFactory.getLogger(
      ResponderServiceImpl.class);

  private final TslService tslService;

  @Autowired
  public ResponderServiceImpl(TslService tslService) {
    this.tslService = tslService;
  }


  @Override
  public Optional<TSPServiceType> validate(String base64Certificate) throws Exception {
    // ensure well-formed base64
    String safeBase64 = safeBase64(base64Certificate);

    // decode base64
    byte[] bytesCertificate;
    try {
      bytesCertificate = Base64.decode(safeBase64);
    } catch (Exception e) {
      return Optional.empty();
    }

    // get certificate
    X509Certificate certificate = getCertificate(bytesCertificate)
        .orElseThrow(() -> new CertificateException("Unable to read the certificate"));

    // extract country ("CC") from certificate
    String countryName = extractCountryName(certificate)
        .orElseThrow(() -> new CertificateException("Country Name not found in certificate"));

    // search into "CC" TSL
    Optional<TrustStatusListType> tsl = tslService.read(countryName);
    if (tsl.isPresent()) {
      // extract services from "CC" TSL
      List<TSPServiceType> services = extractServices(tsl.get());

      // search certificate into "CC" services
      Optional<TSPServiceType> optionalService = searchCertificate(safeBase64, services);
      if (optionalService.isPresent()) {
        if (LOGGER.isInfoEnabled()) {
          LOGGER.info(
              "Certificate found in TSL {}, corresponding to the country extracted in the certificate (cert={})",
              countryName, base64Certificate);
        }
        return optionalService;
      }
    }

    // search into all others TSLs
    List<TrustStatusListType> allTsl = tslService.readAll();
    for (TrustStatusListType otherTsl : allTsl) {
      // avoid to search into "CC" TSL
      if (!countryName.equals(otherTsl.getSchemeInformation().getSchemeTerritory().getValue())) {
        // extract services from TSL
        List<TSPServiceType> otherServices = extractServices(otherTsl);
        // search certificate into services
        Optional<TSPServiceType> optionalOtherService = searchCertificate(safeBase64,
            otherServices);
        if (optionalOtherService.isPresent()) {
          if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Certificate found in TSL {} (cert={})", countryName, base64Certificate);
          }
          return optionalOtherService;
        }
      }
    }

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Certificate not found", base64Certificate);
    }
    return Optional.empty();
  }

  private Optional<TSPServiceType> searchCertificate(String certificate,
      List<TSPServiceType> services) {
    if (CollectionUtils.isEmpty(services)) {
      return Optional.empty();
    }

    for (TSPServiceType service : services) {
      if (service != null
          && service.getServiceInformation() != null
          && service.getServiceInformation().getServiceDigitalIdentity() != null
          && CollectionUtils.isNotEmpty(
          service.getServiceInformation().getServiceDigitalIdentity().getValues())
          && service.getServiceInformation().getServiceDigitalIdentity().getValues()
          .stream()
          .filter(digitalId -> Objects.nonNull(digitalId)
              && CollectionUtils.isNotEmpty(digitalId.getCertificateList()))
          .flatMap(digitalId -> digitalId.getCertificateList().stream())
          .anyMatch(cert -> cert != null && certificate.equals(cert.getCertB64()))) {
        return Optional.of(service);
      }
    }
    return Optional.empty();
  }

  private List<TSPServiceType> extractServices(TrustStatusListType tsl) {
    if (tsl == null
        || tsl.getTrustServiceProviderList() == null
        || CollectionUtils.isEmpty(tsl.getTrustServiceProviderList().getValues())) {
      return Collections.emptyList();
    }

    return tsl.getTrustServiceProviderList().getValues().stream()
        .filter(tsp -> Objects.nonNull(tsp)
            && Objects.nonNull(tsp.getTspServices())
            && CollectionUtils.isNotEmpty(tsp.getTspServices().getValues()))
        .flatMap(tsp -> tsp.getTspServices().getValues().stream())
        .collect(Collectors.toList());
  }

  private Optional<String> extractCountryName(X509Certificate certificate)
      throws CertificateEncodingException {
    X500Name x500name = new JcaX509CertificateHolder(certificate).getSubject();
    RDN[] rdnArray = x500name.getRDNs(BCStyle.C);
    if (rdnArray.length == 0) {
      return Optional.empty();
    }
    return Optional.of(IETFUtils.valueToString(rdnArray[0].getFirst().getValue()));
  }

  private Optional<X509Certificate> getCertificate(byte[] bytesCertificate)
      throws CertificateException {
    CertificateFactory factory = CertificateFactory.getInstance("X.509");
    return Optional.ofNullable((X509Certificate) factory
        .generateCertificate(new ByteArrayInputStream(bytesCertificate)));
  }

  private String safeBase64(String base64Certificate) {
    if (StringUtils.isEmpty(base64Certificate)) {
      throw new IllegalArgumentException("Invalid argument: " + base64Certificate);
    }
    return base64Certificate.replaceAll("-----(BEGIN|END) CERTIFICATE-----\n?", "")
        .replaceAll("\n", "")
        .trim();
  }

}
