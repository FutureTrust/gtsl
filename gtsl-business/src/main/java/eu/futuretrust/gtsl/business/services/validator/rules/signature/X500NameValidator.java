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
import java.io.IOException;
import java.util.List;
import sun.security.x509.X500Name;

public interface X500NameValidator {

  /**
   * Verifies whether a certificate's country name matches a provided country name
   * @param context the validation context
   * @param violationConstant the violation constant to add to the report
   * @param certificate the certificate for which the verification is performed
   * @param schemeTerritory the country name
   * @return true if there is a match, false otherwise
   * @throws IOException thrown if a {@link X500Name} cannot be retrieved from the certificate.
   */
  void isX500NameCountryCodeValid(final ValidationContext context,
                                  final ViolationConstant violationConstant,
                                  final X500Name certificate,
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
  void isX500NameOrganizationValid(final ValidationContext context,
                                   final ViolationConstant violationConstant,
                                   final X500Name certificate,
                                   final List<MultiLangNormStringType> orgNames);

}
