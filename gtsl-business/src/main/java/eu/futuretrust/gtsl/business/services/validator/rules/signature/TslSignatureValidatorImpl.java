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

package eu.futuretrust.gtsl.business.services.validator.rules.signature;

import eu.europa.esig.dss.DSSDocument;
import eu.europa.esig.dss.InMemoryDocument;
import eu.europa.esig.dss.tsl.TSLValidationResult;
import eu.europa.esig.dss.tsl.service.TSLValidator;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.validation.executor.ValidationLevel;
import eu.europa.esig.dss.validation.policy.rules.Indication;
import eu.europa.esig.dss.validation.reports.Reports;
import eu.europa.esig.dss.validation.reports.SimpleReport;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.x509.KeyStoreCertificateSource;
import eu.europa.esig.dss.xades.XPathQueryHolder;
import eu.europa.esig.dss.xades.validation.XMLDocumentValidator;
import eu.futuretrust.gtsl.business.properties.LotlProperties;
import eu.futuretrust.gtsl.business.services.tsl.SignersService;
import eu.futuretrust.gtsl.business.services.validator.rules.RulesValidator;
import eu.futuretrust.gtsl.business.services.xml.JaxbService;
import eu.futuretrust.gtsl.business.utils.SignatureUtils;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.enums.TSLType;
import eu.futuretrust.gtsl.model.data.signature.SignatureType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.io.File;
import java.security.cert.X509Certificate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TslSignatureValidatorImpl implements RulesValidator<SignatureType>
{
  private CertificateValidator certificateValidator;
  private TrustStatusListType tsl;

  @Autowired
  private SignersService signersService;
  @Autowired
  private JaxbService jaxbService;
  private LotlProperties lotlProperties;

  @Autowired
  public TslSignatureValidatorImpl(final CertificateValidator certificateValidator) {
    this.certificateValidator = certificateValidator;
  }

  @Override
  public void validate(final ValidationContext validationContext, final SignatureType signature)
  {
    final List<X509Certificate> certificates = SignatureUtils.getCertificatesFromSignature(signature);
    final String countryCode = tsl.getSchemeInformation().getSchemeTerritory().getValue();

    if (certificates == null || certificates.size() == 0) {
      validationContext.addViolation(new Violation(ViolationConstant.HAS_SIGNATURE));
    } else if (certificates.size() == 1) {
      final X509Certificate certificate = SignatureUtils.getCertificatesFromSignature(signature).get(0);

      certificateValidator.isSigningCertificateKeyUsageConsistent(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_KEY_USAGE_CONSISTENT, certificate);
      certificateValidator.isSigningCertificateBasicConstraintsExtensionValid(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_BASIC_CONSTRAINTS_EXTENSION_VALID, certificate);
      certificateValidator.isSigningCertificateIssuerTlsoOrTsp(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_ISSUER_TLSO_OR_TSP, certificate, tsl);
      certificateValidator.isSigningCertificateCountryCodeValid(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_COUNTRY_CODE_VALID, certificate, countryCode);
      certificateValidator.isSigningCertificateOrganizationValid(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_ORGANIZATION_VALID, certificate, tsl.getSchemeInformation().getSchemeOperatorName().getValues());
      certificateValidator.hasSigningCertificateExtendedKeyUsageTslSigning(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_EXTENDED_KEY_USAGE_PRESENT, certificate);
      certificateValidator.isSigningCertificateSKIExtensionPresent(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_SKI_PRESENT, certificate);
      certificateValidator.isSigningCertificateKeyLengthAcceptable(validationContext, ViolationConstant.IS_SIGNING_CERTIFICATE_KEY_RESILIENT, signature, certificate);
    } else {
      validationContext.addViolation(new Violation(ViolationConstant.HAS_SIGNATURE_KEYINFO_SINGLE_CERTIFICATE));
    }

    try {
      TSLValidationResult result = validateTSL(tsl, countryCode);

      if (result.isIndeterminate()) {
        validationContext.addViolation(new Violation(ViolationConstant.IS_SIGNATURE_INDETERMINATE));
      } else if (result.isInvalid()) {
        validationContext.addViolation(new Violation(ViolationConstant.IS_SIGNATURE_INVALID));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * Validates the signature of a Trusted List, based on its type and the authorised certificates
   * published in the List of Trusted Lists
   *
   * @param tsl         The TSL model to validate
   * @param countryCode the country code of the TSL
   * @return a {@link TSLValidationResult}, indicating the validation results
   * @throws Exception
   */
  public TSLValidationResult validateTSL(final TrustStatusListType tsl, final String countryCode) throws Exception
  {

    final List<CertificateToken> tokens;

    if (tsl.getSchemeInformation().getTslType().getValue()
            .equals(TSLType.LOTL_TYPE.getValue())) {

      final File keystoreFile = new File(lotlProperties.getKeystorePath());

      tokens = new KeyStoreCertificateSource(
              keystoreFile,
              lotlProperties.getKeystoreType(),
              lotlProperties.getKeystorePassword())
              .getCertificates();
    } else {
      tokens = signersService.getTLPotentialSigners(countryCode);
    }

    final DSSDocument tslDocument = new InMemoryDocument(jaxbService.marshallTslToBytes(tsl));

    return validateSignature(tslDocument, countryCode, tokens);
  }

  public void setTsl(final TrustStatusListType tsl) {
    this.tsl = tsl;
  }

  /**
   * Taken from the Digit-TSL code base, but adapted to fit a DSSDocument in the method's signature instead of a File
   * @param tslDocument
   * @param countryCode
   * @param potentialSigners
   * @return
   */
  private TSLValidationResult validateSignature(final DSSDocument tslDocument,
                                                final String countryCode,
                                                final List<CertificateToken> potentialSigners) {

    final CertificateVerifier certificateVerifier = new CommonCertificateVerifier(true);
    certificateVerifier.setTrustedCertSource(buildTrustedCertificateSource(potentialSigners));
    final XMLDocumentValidator validator = new XMLDocumentValidator(tslDocument);
    validator.setCertificateVerifier(certificateVerifier);
    validator.setValidationLevel(ValidationLevel.BASIC_SIGNATURES);

    final List<XPathQueryHolder> xPathQueryHolders = validator.getXPathQueryHolder();
    xPathQueryHolders.clear();
    xPathQueryHolders.add(new XPathQueryHolder());

    Reports reports = validator.validateDocument(TSLValidator.class.getResourceAsStream("/tsl-constraint.xml"));
    SimpleReport simpleReport = reports.getSimpleReport();
    Indication indication = simpleReport.getIndication(simpleReport.getFirstSignatureId());

    TSLValidationResult result = new TSLValidationResult();
    result.setCountryCode(countryCode);
    result.setIndication(indication);
    result.setSubIndication(simpleReport.getSubIndication(simpleReport.getFirstSignatureId()));

    return result;
  }

  private CommonTrustedCertificateSource buildTrustedCertificateSource(final List<CertificateToken> potentialSigners) {

    final CommonTrustedCertificateSource source = new CommonTrustedCertificateSource();
    potentialSigners.stream().map(o -> source.addCertificate(o));

    return source;
  }

  public void setLotlProperties(LotlProperties lotlProperties)
  {
    this.lotlProperties = lotlProperties;
  }
}
