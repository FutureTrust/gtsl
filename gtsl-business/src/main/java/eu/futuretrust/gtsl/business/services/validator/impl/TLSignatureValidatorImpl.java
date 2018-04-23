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

package eu.futuretrust.gtsl.business.services.validator.impl;

import eu.europa.esig.dss.tsl.TSLValidationResult;
import eu.europa.esig.dss.tsl.service.TSLValidator;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;
import eu.futuretrust.gtsl.business.properties.LotlProperties;
import eu.futuretrust.gtsl.business.services.tsl.SignersService;
import eu.futuretrust.gtsl.business.services.validator.TLSignatureValidator;
import eu.futuretrust.gtsl.model.data.enums.TSLType;
import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TLSignatureValidatorImpl implements TLSignatureValidator {


  private SignersService signersService;

  private LotlProperties lotlProperties;

  @Autowired
  public TLSignatureValidatorImpl(final SignersService signersService,
      final LotlProperties lotlProperties) {
    this.signersService = signersService;
    this.lotlProperties = lotlProperties;
  }

  public TSLValidationResult validateTSL(final File tl, final String tslType, final String
      countryCode) throws Exception {

    final List<CertificateToken> tokens;

    if (tslType.equals(TSLType.LOTL_TYPE.getValue())) {
      final File keystoreFile = new File(lotlProperties.getKeystorePath());

      tokens = new KeyStoreCertificateSource(
          keystoreFile,
          lotlProperties.getKeystoreType(),
          lotlProperties.getKeystorePassword())
          .getCertificates();
    } else {
      tokens = signersService.getTLPotentialSigners(countryCode);
    }

    return validateTSL(tl, countryCode, tokens);
  }

  private TSLValidationResult validateTSL(final File tl, final String countryCode,
      final List<CertificateToken> potentialSigners)
      throws Exception {

    TSLValidator validator = new TSLValidator(tl, countryCode, potentialSigners);
    TSLValidationResult validationResult = validator.call();

    return validationResult;
  }

  private CommonTrustedCertificateSource buildTrustedCertificateSource(
      List<CertificateToken> potentialSigners) {
    CommonTrustedCertificateSource commonTrustedCertificateSource = new CommonTrustedCertificateSource();
    if (Utils.isCollectionNotEmpty(potentialSigners)) {
      for (CertificateToken potentialSigner : potentialSigners) {
        commonTrustedCertificateSource.addCertificate(potentialSigner);
      }
    }
    return commonTrustedCertificateSource;
  }

}
