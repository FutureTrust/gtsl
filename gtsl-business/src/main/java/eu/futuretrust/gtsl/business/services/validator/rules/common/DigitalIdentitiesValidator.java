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

import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import eu.futuretrust.gtsl.model.data.digitalidentity.DigitalIdentityType;
import eu.futuretrust.gtsl.model.data.digitalidentity.ServiceDigitalIdentityListType;
import eu.futuretrust.gtsl.model.data.digitalidentity.ServiceDigitalIdentityType;

public interface DigitalIdentitiesValidator {

  void isServiceDigitalIdentitiesValid(ValidationContext validationContext,
      ServiceDigitalIdentityListType serviceDigitalIdentities);

  void isCertificateExpired(ValidationContext validationContext, ViolationConstant violation,
      DigitalIdentityType digitalIdentity);

  void isSubjectNameMatch(ValidationContext validationContext, ViolationConstant violation,
      DigitalIdentityType digitalIdentity);

  void isValidCertificate(ValidationContext validationContext, ViolationConstant violation,
      DigitalIdentityType digitalIdentity);

  void isX509SkiMatch(ValidationContext validationContext, ViolationConstant violation,
      DigitalIdentityType digitalIdentity);

  void isServiceDigitalIdentityOrganizationMatch(ValidationContext validationContext,
      ViolationConstant violation, DigitalIdentityType digitalIdentity);

  default void isListCertificateExpired(ValidationContext validationContext,
      ViolationConstant violation, ServiceDigitalIdentityType serviceDigitalIdentity) {
    serviceDigitalIdentity.getValues()
        .forEach(digitalId -> isCertificateExpired(validationContext, violation, digitalId));
  }

  default void isListSubjectNameMatch(ValidationContext validationContext,
      ViolationConstant violation, ServiceDigitalIdentityType serviceDigitalIdentity) {
    serviceDigitalIdentity.getValues()
        .forEach(digitalId -> isSubjectNameMatch(validationContext, violation, digitalId));
  }

  default void isListValidCertificate(ValidationContext validationContext,
      ViolationConstant violation, ServiceDigitalIdentityType serviceDigitalIdentity) {
    serviceDigitalIdentity.getValues()
        .forEach(digitalId -> isValidCertificate(validationContext, violation, digitalId));
  }

  default void isListX509SkiMatch(ValidationContext validationContext, ViolationConstant violation,
      ServiceDigitalIdentityType serviceDigitalIdentity) {
    serviceDigitalIdentity.getValues()
        .forEach(digitalId -> isX509SkiMatch(validationContext, violation, digitalId));
  }

  void isListServiceDigitalIdentityCorrect(ValidationContext validationContext,
      ViolationConstant violation, ServiceDigitalIdentityType serviceDigitalIdentity,
      NonEmptyURIType serviceTypeIdentifier);

  default void isListServiceDigitalIdentityOrganizationMatch(ValidationContext validationContext,
      ViolationConstant violation, ServiceDigitalIdentityType serviceDigitalIdentity) {
    serviceDigitalIdentity.getValues()
        .forEach(
            digitalId -> isServiceDigitalIdentityOrganizationMatch(validationContext, violation,
                digitalId));
  }

}
