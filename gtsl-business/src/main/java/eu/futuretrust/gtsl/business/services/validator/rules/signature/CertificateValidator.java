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

import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.model.data.common.MultiLangNormStringType;
import eu.futuretrust.gtsl.model.data.signature.SignatureType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.List;
import sun.security.x509.X500Name;

public interface CertificateValidator {

  /**
   * Verifies that the key usage set for the certificate is set either to "Digital
   * Signature" or to "Non Repudiation".
   * @param context the validation context
   * @param violationConstant the violation constant to add to the report
   * @param certificate the signing certificate of the Trust List
   * @return true if the key usage is set to one of the values mentioned above, false otherwise.
   */
  void isSigningCertificateKeyUsageConsistent(final ValidationContext context,
                                              final ViolationConstant violationConstant,
                                              final X509Certificate certificate);

  /**
   * Verifies whether the signing certificate of the Trust List is an end-entity certificate
   * (i.e. its BasicConstraints attribute has its "ca" field set to false).
   * @param context the validation context
   * @param violationConstant the violation constant to add to the report
   * @param certificate the signing certificate of the Trust List
   * @return true if the certificate is an end-entity certificate, false if it's a CA certificate.
   */
  void isSigningCertificateBasicConstraintsExtensionValid(final ValidationContext context,
                                                                 final ViolationConstant violationConstant,
                                                                 final X509Certificate certificate);

  /**
   * Verifies whether the TLSO's certificate is self-signed or was issued by a TSP listed in the
   * related TSL.
   * @param context the validation context
   * @param violationConstant the violation constant to add to the report
   * @param certificate the TLSO's certificate
   * @param tsl the TSL
   * @return
   */
  void isSigningCertificateIssuerTlsoOrTsp(final ValidationContext context,
                                                  final ViolationConstant violationConstant,
                                                  final X509Certificate certificate,
                                                  final TrustStatusListType tsl);

  /**
   * Verifies whether a certificate's country name matches a provided country name
   * @param context the validation context
   * @param violationConstant the violation constant to add to the report
   * @param certificate the certificate for which the verification is performed
   * @param schemeTerritory the country name
   * @return true if there is a match, false otherwise
   * @throws IOException thrown if a {@link X500Name} cannot be retrieved from the certificate.
   */
  void isSigningCertificateCountryCodeValid(final ValidationContext context,
                                                   final ViolationConstant violationConstant,
                                                   final X509Certificate certificate,
                                                   final String schemeTerritory);

  /**
   * Verifies whether the signing certificate's organization name matches the Scheme Operator name
   * (both English and local language - transliterated to Latin script - are accepted)
   * @param context the validation context
   * @param violationConstant the violation constant to add to the report
   * @param certificate the certificate for which the verification is performed
   * @param orgNames the list of allowed organization names (EN and local language)
   * @return true if there is a match, false otherwise
   * @throws IOException thrown if a {@link X500Name} cannot be retrieved from the certificate.
   */
  void isSigningCertificateOrganizationValid (final ValidationContext context,
                                                     final ViolationConstant violationConstant,
                                                     final X509Certificate certificate,
                                                     final List<MultiLangNormStringType> orgNames);

  /**
   * Verifies whether a certificate's extended key usage matches the specific OID defined for TSL
   * signing.
   * @param context the validation context
   * @param violationConstant the violation constant to add to the report
   * @param certificate the certificate for which the verification is performed
   * @return true if the certificate has an extended key usage attribute matching the specific
   * OID, false otherwise.
   * @throws CertificateParsingException thrown if the extended key usage cannot be retrieved
   * from the certificate.
   */
  void hasSigningCertificateExtendedKeyUsageTslSigning(final ValidationContext context,
                                                              final ViolationConstant violationConstant,
                                                              final X509Certificate certificate);

  /**
   * Verifies whether a certificate has a SubjectKeyIdentifier attribute as part of its extensions.
   * @param context the validation context
   * @param violationConstant the violation constant to add to the report
   * @param certificate the certificate for which the verification is performed
   * @return true if the certificate has a SKI in its extensions, false otherwise.
   */
  void isSigningCertificateSKIExtensionPresent(final ValidationContext context,
                                                      final ViolationConstant violationConstant,
                                                      final X509Certificate certificate);

  /**
   * Verifies whether the key length extracted from a certificate matches the minimal requirements set for the
   * algorithm used for the signature, according to ETSI TS 119 612, section 5.7.1 and ETSI TS 119 312.
   * @param context
   * @param violationConstant
   * @param signature
   * @param certificate
   **/
  void isSigningCertificateKeyLengthAcceptable(final ValidationContext context,
                                               final ViolationConstant violationConstant,
                                               final SignatureType signature,
                                               final X509Certificate certificate);


}
