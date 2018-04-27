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

package eu.futuretrust.gtsl.business.services.validator.rules.servicehistory;

import eu.futuretrust.gtsl.business.services.validator.rules.RulesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.CommonAttributesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.CountriesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.DigitalIdentitiesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.ServiceExtensionValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import eu.futuretrust.gtsl.model.data.digitalidentity.ServiceDigitalIdentityType;
import eu.futuretrust.gtsl.model.data.ts.ServiceHistoryInstanceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceHistoryInstanceValidatorImpl implements
    RulesValidator<ServiceHistoryInstanceType> {

  private final CommonAttributesValidator commonAttributesValidator;
  private final CountriesValidator countriesValidator;
  private final DigitalIdentitiesValidator digitalIdentitiesValidator;
  private final ServiceExtensionValidator serviceExtensionValidator;

  @Autowired
  public ServiceHistoryInstanceValidatorImpl(
      CommonAttributesValidator commonAttributesValidator,
      CountriesValidator countriesValidator,
      DigitalIdentitiesValidator digitalIdentitiesValidator,
      ServiceExtensionValidator serviceExtensionValidator) {
    this.commonAttributesValidator = commonAttributesValidator;
    this.countriesValidator = countriesValidator;
    this.digitalIdentitiesValidator = digitalIdentitiesValidator;
    this.serviceExtensionValidator = serviceExtensionValidator;
  }

  @Override
  public void validate(ValidationContext validationContext,
      ServiceHistoryInstanceType historyInstance) {

    isServiceTypeIdentifierRegistered(validationContext,
        historyInstance.getServiceTypeIdentifier());

    isServiceNameContainLangEn(validationContext, historyInstance.getServiceName());
    isServiceNameLangAllowed(validationContext, historyInstance.getServiceName());

    isServiceStatusCorrectValue(validationContext, historyInstance.getServiceStatus());
    isServiceStatusByNationalLawAllowed(validationContext,
        historyInstance.getServiceTypeIdentifier(), historyInstance.getServiceStatus());

    isCertificateExpired(validationContext, historyInstance.getServiceDigitalIdentity());
    isValidCertificate(validationContext, historyInstance.getServiceDigitalIdentity());
    isSubjectNameMatch(validationContext, historyInstance.getServiceDigitalIdentity());
    isX509SkiMatch(validationContext, historyInstance.getServiceDigitalIdentity());

    if (historyInstance.getServiceInformationExtensions() != null) {
      serviceExtensionValidator
          .validateExtension(validationContext, historyInstance.getServiceInformationExtensions(),
              historyInstance.getServiceTypeIdentifier(),
              ViolationConstant.IS_HISTORY_QUALIFICATION_ELEMENT_PRESENT,
              ViolationConstant.IS_HISTORY_QUALIFICATION_EXTENSION_ALLOWED,
              ViolationConstant.IS_HISTORY_QUALIFIERS_PRESENT,
              ViolationConstant.IS_HISTORY_CRITERIA_ASSERTS_NOT_NULL,
              ViolationConstant.IS_HISTORY_QUALIFIER_URI_CORRECT_VALUE,
              ViolationConstant.IS_HISTORY_EXPIRED_CERTS_REVOCATION_NOT_CRITICAL,
              ViolationConstant.IS_HISTORY_EXPIRED_CERTS_REVOCATION_ALLOWED,
              ViolationConstant.IS_HISTORY_SERVICE_EXTENSION_ASI_ALLOWED,
              ViolationConstant.IS_HISTORY_SERVICE_EXTENSION_ASI_SIG_ALLOWED,
              ViolationConstant.IS_HISTORY_SERVICE_EXTENSION_ASI_SIG_SEAL_ALLOWED,
              ViolationConstant.IS_HISTORY_SERVICE_EXTENSION_ASI_LANG_ALLOWED);
    }
  }

  private void isServiceTypeIdentifierRegistered(ValidationContext validationContext,
      NonEmptyURIType serviceTypeIdentifier) {
    commonAttributesValidator.isServiceTypeIdentifierRegistered(validationContext,
        ViolationConstant.IS_HISTORY_TYPE_IDENTIFIER_REGISTERED, serviceTypeIdentifier);
  }

  private void isServiceNameContainLangEn(
      ValidationContext validationContext, InternationalNamesType serviceName) {
    countriesValidator
        .isListContainLangEn(validationContext,
            ViolationConstant.IS_HISTORY_SERVICE_NAME_CONTAIN_LANG_EN, serviceName.getValues());
  }

  private void isServiceNameLangAllowed(ValidationContext validationContext,
      InternationalNamesType serviceName) {
    countriesValidator.isListAttributeLangAllowed(validationContext,
        ViolationConstant.IS_HISTORY_SERVICE_NAME_LANG_ALLOWED, serviceName.getValues());
  }

  private void isServiceStatusCorrectValue(ValidationContext validationContext,
      NonEmptyURIType status) {
    if (!commonAttributesValidator.isValidServiceStatus(validationContext, status)
        && !commonAttributesValidator.isValidServicePreviousStatus(validationContext, status)) {
      validationContext
          .addViolation(new Violation(ViolationConstant.IS_HISTORY_SERVICE_STATUS_CORRECT_VALUE));
    }
  }

  private void isServiceStatusByNationalLawAllowed(ValidationContext validationContext,
      NonEmptyURIType serviceTypeIdentifier, NonEmptyURIType serviceStatus) {
    commonAttributesValidator.isServiceStatusByNationalLawAllowed(validationContext,
        ViolationConstant.IS_HISTORY_SERVICE_STATUS_BY_NATIONAL_LAW_ALLOWED, serviceTypeIdentifier,
        serviceStatus);
  }

  private void isCertificateExpired(ValidationContext validationContext,
      ServiceDigitalIdentityType serviceDigitalIdentity) {
    digitalIdentitiesValidator.isListCertificateExpired(validationContext,
        ViolationConstant.IS_HISTORY_DIGITAL_IDENTITY_CERTIFICATE_EXPIRED, serviceDigitalIdentity);
  }

  private void isValidCertificate(ValidationContext validationContext,
      ServiceDigitalIdentityType serviceDigitalIdentity) {
    digitalIdentitiesValidator
        .isListValidCertificate(validationContext,
            ViolationConstant.IS_HISTORY_DIGITAL_IDENTITY_VALID_CERTIFICATE,
            serviceDigitalIdentity);
  }

  private void isSubjectNameMatch(ValidationContext validationContext,
      ServiceDigitalIdentityType serviceDigitalIdentity) {
    digitalIdentitiesValidator
        .isListSubjectNameMatch(validationContext,
            ViolationConstant.IS_HISTORY_DIGITAL_IDENTITY_SUBJECT_NAME_MATCH,
            serviceDigitalIdentity);
  }

  private void isX509SkiMatch(ValidationContext validationContext,
      ServiceDigitalIdentityType serviceDigitalIdentity) {
    digitalIdentitiesValidator
        .isListX509SkiMatch(validationContext,
            ViolationConstant.IS_HISTORY_DIGITAL_IDENTITY_X509SKI_MATCH,
            serviceDigitalIdentity);
  }

}
