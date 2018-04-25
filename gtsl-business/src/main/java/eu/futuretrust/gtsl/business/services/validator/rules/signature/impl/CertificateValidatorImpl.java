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

package eu.futuretrust.gtsl.business.services.validator.rules.signature.impl;

import eu.europa.esig.dss.SignatureAlgorithm;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.futuretrust.gtsl.business.services.tsl.SignersService;
import eu.futuretrust.gtsl.business.services.validator.rules.signature.CertificateValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.signature.CryptographicSuite;
import eu.futuretrust.gtsl.business.utils.SignatureUtils;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.MultiLangNormStringType;
import eu.futuretrust.gtsl.model.data.digitalidentity.DigitalIdentityType;
import eu.futuretrust.gtsl.model.data.signature.SignatureType;
import eu.futuretrust.gtsl.model.data.ts.CertificateType;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.x509.RDN;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertInfo;

@Service
public class CertificateValidatorImpl implements CertificateValidator
{

  private final static String OID_TSL_SIGNING = "0.4.0.2231.3.0";
  private final static String OID_SUBJECT_KEY_IDENTIFIER = "2.5.29.14";

  @Autowired
  private SignersService signersService;

  @Override
  public void isSigningCertificateKeyUsageConsistent(final ValidationContext context,
                                                     final ViolationConstant violationConstant,
                                                     final X509Certificate certificate) {

    final boolean[] keyUsage = certificate.getKeyUsage();

    if (null == keyUsage || (! keyUsage[0] && ! keyUsage[1])) {
      context.addViolation(new Violation(violationConstant));
      return;
    }

    for (int i = 2; i < keyUsage.length; i++) {
      if (keyUsage[i]) {
        context.addViolation(new Violation(violationConstant));
        return;
      }
    }
  }

  @Override
  public void isSigningCertificateBasicConstraintsExtensionValid(final ValidationContext context,
                                                                 final ViolationConstant violationConstant,
                                                                 final X509Certificate certificate) {

    if (certificate.getBasicConstraints() != -1) {
      context.addViolation(new Violation(violationConstant));
    }
  }

  @Override
  public void isSigningCertificateIssuerTlsoOrTsp(final ValidationContext context,
                                                  final ViolationConstant violationConstant,
                                                  final X509Certificate certificate,
                                                  final TrustStatusListType tsl) {

    final String territory = tsl.getSchemeInformation().getSchemeTerritory().getValue();

    try
    {
      if (!isSigningCertificateIssuerTlso(certificate, territory)
              && !isSigningCertificateIssuerTsp(certificate, tsl)
              && !isSigningCertificateSelfSigned(certificate))
      {
        context.addViolation(new Violation(violationConstant));
      }
    } catch (final CertificateException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
      context.addViolation(new Violation(ViolationConstant.IS_SIGNING_CERTIFICATE_VALID));
    }
  }

  /**
   * Checks whether the signing certificate is self-signed
   * @param certificate the certificate to check
   * @return true if the certificate is self-signed, false otherwise
   * @throws CertificateException if there is an error in the certificate encoding
   * @throws NoSuchAlgorithmException if there is an unsupported algorithm
   * @throws NoSuchProviderException if there is no default provider
   */
  private boolean isSigningCertificateSelfSigned(final X509Certificate certificate)
          throws CertificateException, NoSuchAlgorithmException, NoSuchProviderException
  {
    try {
      final PublicKey pk = certificate.getPublicKey();
      certificate.verify(pk);
      return true;
    } catch (final SignatureException | InvalidKeyException e) {
      return false;
    }
  }

  /**
   * Checks whether the signing certificate matches one of the TSP certificates provided in the TSL
   * @param certificate the certificate to check
   * @param tsl the Trust List from which the TSPs have to be retrieved
   * @return true if a match is found, false otherwise
   */
  private boolean isSigningCertificateIssuerTsp(final X509Certificate certificate, final TrustStatusListType tsl) {

    for (final TSPServiceType tsp : SignatureUtils.getTSPServiceTypesFromTSL(tsl)) {
      final List<DigitalIdentityType> values = tsp.getServiceInformation()
              .getServiceDigitalIdentity().getValues();
      if (values != null) {

        for (final DigitalIdentityType identity : values) {
          List<CertificateType> certificateList = identity.getCertificateList();

          if (certificateList != null) {
            for (final CertificateType cert : certificateList) {
              if (cert.getToken().getSubjectX500Principal().equals(certificate.getSubjectX500Principal())) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  /**
   * Checks whether the signing certificate matches one of the certificates listed on the EU Lotl
   * @param certificate the certificate to check
   * @param territory the value of the TSL Scheme Territory
   * @return true if a match is found, false otherwise
   */
  private boolean isSigningCertificateIssuerTlso(final X509Certificate certificate,
                                                 final String territory) throws IOException, CertificateException {

    final List<CertificateToken> potentialSigners = signersService.getTLPotentialSigners(
              territory);

    final List<RDN> issuerRDNs = new X500Name(certificate.getIssuerX500Principal().getName()).rdns();

    for (final CertificateToken potentialSigner : potentialSigners) {
      final X509CertInfo info = new X509CertInfo(potentialSigner.getCertificate().getTBSCertificate());
      final List<RDN> rdns = ((X500Name) info.get("issuer")).rdns();

      if (issuerRDNs.containsAll(rdns)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void isSigningCertificateCountryCodeValid(final ValidationContext context,
                                                   final ViolationConstant violationConstant,
                                                   final X509Certificate certificate,
                                                   final String schemeTerritory)
  {
    try {
        final X500Name name = new X500Name(certificate.getSubjectX500Principal().getName());

        if (schemeTerritory.equalsIgnoreCase("EU")) {
          return;
        }
        else if (!schemeTerritory.equalsIgnoreCase(name.getCountry())) {
          context.addViolation(new Violation(violationConstant));
        }
    } catch (final IOException e) {
      context.addViolation(new Violation(ViolationConstant.IS_SIGNING_CERTIFICATE_VALID));
    }
  }

  @Override
  public void isSigningCertificateOrganizationValid (final ValidationContext context,
                                                        final ViolationConstant violationConstant,
                                                        final X509Certificate certificate,
                                                        final List<MultiLangNormStringType> orgNames) {
    try
    {
        final X500Name name = new X500Name(certificate.getSubjectX500Principal().getName());

        for (final MultiLangNormStringType value : orgNames) {
            if (value.getValue().equalsIgnoreCase(name.getOrganization())) {
                return;
            }
        }
        context.addViolation(new Violation(violationConstant));
    } catch (final IOException e) {
        context.addViolation(new Violation(ViolationConstant.IS_SIGNING_CERTIFICATE_VALID));
    }
  }

  @Override
  public void hasSigningCertificateExtendedKeyUsageTslSigning(final ValidationContext context,
                                                              final ViolationConstant violationConstant,
                                                              final X509Certificate certificate) {
    try
    {
        final List<String> extendedKeyUsage = certificate.getExtendedKeyUsage();
        if (extendedKeyUsage == null || !extendedKeyUsage.contains(OID_TSL_SIGNING))
        {
            context.addViolation(new Violation(violationConstant));
        }
    } catch (final CertificateParsingException e)
    {
        context.addViolation(new Violation(ViolationConstant.IS_SIGNING_CERTIFICATE_VALID));
    }
  }

  @Override
  public void isSigningCertificateSKIExtensionPresent(final ValidationContext context,
                                                      final ViolationConstant violationConstant,
                                                      final X509Certificate certificate) {
    final byte[] ski = certificate.getExtensionValue(OID_SUBJECT_KEY_IDENTIFIER);
    if (ski == null || ski.length == 0) {
      context.addViolation(new Violation(violationConstant));
    }
  }

  @Override
  public void isSigningCertificateKeyLengthAcceptable(final ValidationContext context,
                                                      final ViolationConstant violationConstant,
                                                      final SignatureType signature,
                                                      final X509Certificate certificate) {

    final int keyLength = SignatureUtils.getKeyLength(certificate.getPublicKey());
    SignatureAlgorithm algorithm = SignatureAlgorithm.forXML(signature.getSignedInfo().getSignatureMethod().getAlgorithm());

    CryptographicSuite cryptoSuite = CryptographicSuite.valueOf(algorithm.getJCEId());

    if (keyLength < cryptoSuite.getKeyLengthThreeYears()) {
        context.addViolation(new Violation(violationConstant));
    }
  }
}
