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

package eu.futuretrust.gtsl.business.services.tsl.impl;

import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;
import eu.futuretrust.gtsl.business.properties.LotlProperties;
import eu.futuretrust.gtsl.business.services.storage.LotlCacheService;
import eu.futuretrust.gtsl.business.services.tsl.SignersService;
import eu.futuretrust.gtsl.model.data.digitalidentity.ServiceDigitalIdentityType;
import eu.futuretrust.gtsl.model.data.ts.CertificateType;
import eu.futuretrust.gtsl.model.data.tsl.OtherTSLPointerType;
import eu.futuretrust.gtsl.model.data.tsl.OtherTSLPointersType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class SignersServiceImpl implements SignersService {

  private final LotlCacheService lotlCacheService;
  private final LotlProperties lotlProperties;
  private final CertificateFactory certificateFactory;

  @Autowired
  public SignersServiceImpl(final LotlCacheService lotlCacheService,
      final LotlProperties lotlProperties) throws CertificateException {
    this.lotlCacheService = lotlCacheService;
    this.certificateFactory = CertificateFactory.getInstance("X.509");
    this.lotlProperties = lotlProperties;
  }


  @Override
  public List<CertificateToken> getLotlPotentialSigners() throws IOException {

    final File keystoreFile = new ClassPathResource((lotlProperties.getKeystorePath())).getFile();

    final KeyStoreCertificateSource keystore = new KeyStoreCertificateSource(
        keystoreFile,
        lotlProperties.getKeystoreType(),
        lotlProperties.getKeystorePassword());

    return keystore.getCertificates();
  }

  @Override
  public List<CertificateToken> getTLPotentialSigners(final String countryCode)
      throws IOException {

    TrustStatusListType lotl = lotlCacheService.getLotlAsTSLType();
    OtherTSLPointersType pointersToOtherTSL = lotl.getSchemeInformation().getPointersToOtherTSL();

    Optional<OtherTSLPointerType> otherTSLPointer = pointersToOtherTSL.getValues()
        .stream()
        .filter(o -> o.getSchemeTerritory()
            .getValue()
            .equals(countryCode))
        .findFirst();

    List<CertificateToken> tokens = getDigitalIdentities(otherTSLPointer.get());
    return tokens;
  }

  /**
   * Retrieves the list of certificates mapped to DigitalIdentities in the TSL, for a given OtherTSLPointer element
   * @param otherTSLPointer the element in which the certificates must be retrieved
   * @return the list of {@link CertificateToken} extracted from the OtherTSLPointer element
   */
  private List<CertificateToken> getDigitalIdentities (final OtherTSLPointerType otherTSLPointer) {

    return otherTSLPointer.getServiceDigitalIdentities().getValues()
            .stream()
            .map(ServiceDigitalIdentityType::getValues)
            .flatMap(o -> o.stream())
            .map(o -> o.getCertificateList())
            .map(this::getCertificateTokens)
            .flatMap(l -> l.stream())
            .collect(Collectors.toList());
  }

  /**
   * Retrieves the list of certificates from a provided list of Certificate elements
   * @param certificateList the list of Certificate elements (JAXB models) from which certificates must be extracted
   * @return the list of {@link CertificateToken} extracted from the Certificate elements
   */
  private List<CertificateToken> getCertificateTokens (final List<CertificateType> certificateList) {

      return certificateList.stream()
            .map(CertificateType::getCertEncoded)
            .map(this::getCertificateTokenFromBytes)
            .collect(Collectors.toList());
  }

  /**
   * Builds a {@link CertificateToken} from a provided byte array
   * @param bytes the byte array to use for building a {@link CertificateToken} object
   * @return the {@link CertificateToken} object built from the byte array
   * @throws RuntimeException if there is an issue when building the certificate
   */
  private CertificateToken getCertificateTokenFromBytes (final byte[] bytes) {

    try {
      X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(new
          ByteArrayInputStream(bytes));
      return new CertificateToken(certificate);
    } catch (CertificateException e) {
      throw new RuntimeException(e);
    }
  }
}
