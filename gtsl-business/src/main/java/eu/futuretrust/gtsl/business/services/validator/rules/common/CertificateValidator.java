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

package eu.futuretrust.gtsl.business.services.validator.rules.common;

import eu.europa.esig.dss.x509.CertificateToken;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.model.data.common.CountryCode;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
import eu.futuretrust.gtsl.model.data.ts.CertificateType;

public interface CertificateValidator {

  // from TL-Manager
  void isX509CertificateContainsCorrectKeyUsages(ValidationContext validationContext,
      ViolationConstant violationConstant, CertificateType cert);

  // from TL-Manager
  void isX509CertificateContainsBasicConstraintCaFalse(ValidationContext validationContext,
      ViolationConstant violationConstant, CertificateType cert);

  // from TL-Manager
  void isX509CertificateContainsTslSigningExtKeyUsage(ValidationContext validationContext,
      ViolationConstant violationConstant, CertificateType cert);

  // from TL-Manager
  void isX509CertificateContainsSubjectKeyIdentifier(ValidationContext validationContext,
      ViolationConstant violationConstant, CertificateType cert);

  // from TL-Manager
  void isX509CertificateCountryCodeMatch(ValidationContext validationContext,
      ViolationConstant violationConstant, CertificateType cert, CountryCode expectedCountryCode);

  // from TL-Manager
  void isX509CertificateOrganizationMatch(ValidationContext validationContext,
      ViolationConstant violationConstant, CertificateType cert,
      InternationalNamesType schemeOperatorNames);

  // from TL-Manager
  String getCountryCode(CertificateToken certificate);

  // from TL-Manager
  String getOrganization(CertificateToken certificate);

  // from TL-Manager
  boolean isMatch(InternationalNamesType names, String organization);
}
