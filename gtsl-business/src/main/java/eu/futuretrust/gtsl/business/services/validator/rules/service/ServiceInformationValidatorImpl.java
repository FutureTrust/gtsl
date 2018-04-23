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

package eu.futuretrust.gtsl.business.services.validator.rules.service;

import eu.futuretrust.gtsl.business.services.validator.rules.RulesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.CommonAttributesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.CountriesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.DigitalIdentitiesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.ServiceExtensionValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIListType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIOptionalListType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import eu.futuretrust.gtsl.model.data.digitalidentity.ServiceDigitalIdentityType;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceInformationType;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceInformationValidatorImpl implements RulesValidator<TSPServiceInformationType> {

  private final CommonAttributesValidator commonAttributesValidator;
  private final CountriesValidator countriesValidator;
  private final DigitalIdentitiesValidator digitalIdentitiesValidator;
  private final ServiceExtensionValidator serviceExtensionValidator;


  @Autowired
  public ServiceInformationValidatorImpl(
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
      TSPServiceInformationType serviceInformation) {
    isServiceNameContainLangEn(validationContext, serviceInformation.getServiceName());
    isServiceNameLangAllowed(validationContext, serviceInformation.getServiceName());

    isServiceTypeIdentifierRegistered(validationContext,
        serviceInformation.getServiceTypeIdentifier());

    isServiceStatusCorrectValue(validationContext, serviceInformation.getServiceTypeIdentifier(),
        serviceInformation.getServiceStatus());
    isServiceStatusByNationalLawAllowed(validationContext,
        serviceInformation.getServiceTypeIdentifier(), serviceInformation.getServiceStatus());

    isServiceDefinitionURIPresentAndNotEmptyForNationalRootCAQC(validationContext,
        serviceInformation.getTspServiceDefinitionURI(),
        serviceInformation.getServiceTypeIdentifier());
    if (serviceInformation.getTspServiceDefinitionURI() != null
        && CollectionUtils.isNotEmpty(serviceInformation.getTspServiceDefinitionURI().getValues())) {
      isTspServiceDefinitionURIContainLangEn(validationContext,
          serviceInformation.getTspServiceDefinitionURI());
      isTspServiceDefinitionURILangAllowed(validationContext,
          serviceInformation.getTspServiceDefinitionURI());
    }

    if (serviceInformation.getSchemeServiceDefinitionURI() != null
        && CollectionUtils.isNotEmpty(serviceInformation.getSchemeServiceDefinitionURI().getValues())) {
      isSchemeServiceDefinitionURIContainLangEn(validationContext,
          serviceInformation.getSchemeServiceDefinitionURI());
      isSchemeServiceDefinitionURILangAllowed(validationContext,
          serviceInformation.getSchemeServiceDefinitionURI());
    }

    isCertificateExpired(validationContext, serviceInformation.getServiceDigitalIdentity());
    isValidCertificate(validationContext, serviceInformation.getServiceDigitalIdentity());
    isSubjectNameMatch(validationContext, serviceInformation.getServiceDigitalIdentity());
    isX509SkiMatch(validationContext, serviceInformation.getServiceDigitalIdentity());
    isServiceDigitalIdentityCorrect(validationContext,
        serviceInformation.getServiceDigitalIdentity(),
        serviceInformation.getServiceTypeIdentifier());
    isServiceDigitalIdentityOrganizationMatch(validationContext,
        serviceInformation.getServiceDigitalIdentity());

    if (serviceInformation.getServiceInformationExtensions() != null) {
      serviceExtensionValidator
          .validateExtension(validationContext,
              serviceInformation.getServiceInformationExtensions(),
              serviceInformation.getServiceTypeIdentifier(),
              ViolationConstant.IS_QUALIFICATION_ELEMENT_PRESENT,
              ViolationConstant.IS_QUALIFICATION_EXTENSION_ALLOWED,
              ViolationConstant.IS_QUALIFIERS_PRESENT,
              ViolationConstant.IS_CRITERIA_ASSERTS_NOT_NULL,
              ViolationConstant.IS_QUALIFIER_URI_CORRECT_VALUE,
              ViolationConstant.IS_EXPIRED_CERTS_REVOCATION_NOT_CRITICAL,
              ViolationConstant.IS_EXPIRED_CERTS_REVOCATION_ALLOWED,
              ViolationConstant.IS_SERVICE_EXTENSION_ASI_ALLOWED,
              ViolationConstant.IS_SERVICE_EXTENSION_ASI_SIG_ALLOWED,
              ViolationConstant.IS_SERVICE_EXTENSION_ASI_SIG_SEAL_ALLOWED,
              ViolationConstant.IS_SERVICE_EXTENSION_ASI_LANG_ALLOWED);
    }
  }

  private void isServiceStatusByNationalLawAllowed(ValidationContext validationContext,
      NonEmptyURIType serviceTypeIdentifier, NonEmptyURIType serviceStatus) {
    commonAttributesValidator.isServiceStatusByNationalLawAllowed(validationContext,
        ViolationConstant.IS_SERVICE_STATUS_BY_NATIONAL_LAW_ALLOWED, serviceTypeIdentifier,
        serviceStatus);
  }

  private void isServiceDefinitionURIPresentAndNotEmptyForNationalRootCAQC(
      ValidationContext validationContext, NonEmptyMultiLangURIOptionalListType serviceDefinitionURI,
      NonEmptyURIType serviceTypeIdentifier) {
    if (serviceTypeIdentifier.getValue()
        .equals(validationContext.getProperties().getNationalServiceType().getNationalRootCAQC())) {
      if (serviceDefinitionURI == null) {
        validationContext.addViolation(new Violation(
            ViolationConstant.IS_SERVICE_DEFINITION_URI_PRESENT_FOR_NATIONAL_ROOT_CA_QC));
      } else if (serviceDefinitionURI.getValues() == null || serviceDefinitionURI.getValues()
          .isEmpty()) {
        validationContext.addViolation(new Violation(
            ViolationConstant.IS_SERVICE_DEFINITION_URI_LIST_NOT_EMPTY_FOR_NATIONAL_ROOT_CA_QC));
      }
    }
  }

  private void isServiceNameContainLangEn(
      ValidationContext validationContext, InternationalNamesType serviceName) {
    countriesValidator
        .isListContainLangEn(validationContext, ViolationConstant.IS_SERVICE_NAME_CONTAIN_LANG_EN,
            serviceName.getValues());
  }

  private void isServiceNameLangAllowed(ValidationContext validationContext,
      InternationalNamesType serviceName) {
    countriesValidator.isListAttributeLangAllowed(validationContext,
        ViolationConstant.IS_SERVICE_NAME_LANG_ALLOWED, serviceName.getValues());
  }

  private void isTspServiceDefinitionURIContainLangEn(
      ValidationContext validationContext, NonEmptyMultiLangURIOptionalListType serviceDefinitionURI) {
    countriesValidator
        .isListContainLangEn(validationContext,
            ViolationConstant.IS_SERVICE_DEFINITION_URI_CONTAIN_LANG_EN,
            serviceDefinitionURI.getValues());
  }

  private void isTspServiceDefinitionURILangAllowed(
      ValidationContext validationContext,
      NonEmptyMultiLangURIOptionalListType serviceDefinitionURI) {
    countriesValidator.isListAttributeLangAllowed(validationContext,
        ViolationConstant.IS_SERVICE_DEFINITION_URI_LANG_ALLOWED, serviceDefinitionURI.getValues());
  }

  private void isSchemeServiceDefinitionURIContainLangEn(
      ValidationContext validationContext,
      NonEmptyMultiLangURIOptionalListType schemeServiceDefinitionURI) {
    countriesValidator
        .isListContainLangEn(validationContext,
            ViolationConstant.IS_SCHEME_SERVICE_DEFINITION_URI_CONTAIN_LANG_EN,
            schemeServiceDefinitionURI.getValues());
  }

  private void isSchemeServiceDefinitionURILangAllowed(
      ValidationContext validationContext,
      NonEmptyMultiLangURIOptionalListType schemeServiceDefinitionURI) {
    countriesValidator.isListAttributeLangAllowed(validationContext,
        ViolationConstant.IS_SCHEME_SERVICE_DEFINITION_URI_LANG_ALLOWED,
        schemeServiceDefinitionURI.getValues());
  }

  private void isServiceTypeIdentifierRegistered(ValidationContext validationContext,
      NonEmptyURIType serviceTypeIdentifier) {
    commonAttributesValidator.isServiceTypeIdentifierRegistered(validationContext,
        ViolationConstant.IS_SERVICE_TYPE_IDENTIFIER_REGISTERED, serviceTypeIdentifier);
  }

  private void isServiceStatusCorrectValue(ValidationContext validationContext,
      NonEmptyURIType serviceTypeIdentifier, NonEmptyURIType status) {
    commonAttributesValidator.isServiceStatusCorrectValueForEuMs(validationContext,
        ViolationConstant.IS_SERVICE_STATUS_CORRECT_VALUE_FOR_EU_MS, serviceTypeIdentifier, status);
    commonAttributesValidator.isServiceStatusCorrectValueForNonEu(validationContext,
        ViolationConstant.IS_SERVICE_STATUS_CORRECT_VALUE_FOR_NON_EU, serviceTypeIdentifier,
        status);
  }

  private void isCertificateExpired(ValidationContext validationContext,
      ServiceDigitalIdentityType serviceDigitalIdentity) {
    digitalIdentitiesValidator.isListCertificateExpired(validationContext,
        ViolationConstant.IS_SERVICE_DIGITAL_IDENTITY_CERTIFICATE_EXPIRED, serviceDigitalIdentity);
  }

  private void isValidCertificate(ValidationContext validationContext,
      ServiceDigitalIdentityType serviceDigitalIdentity) {
    digitalIdentitiesValidator
        .isListValidCertificate(validationContext,
            ViolationConstant.IS_SERVICE_DIGITAL_IDENTITY_VALID_CERTIFICATE,
            serviceDigitalIdentity);
  }

  private void isSubjectNameMatch(ValidationContext validationContext,
      ServiceDigitalIdentityType serviceDigitalIdentity) {
    digitalIdentitiesValidator
        .isListSubjectNameMatch(validationContext,
            ViolationConstant.IS_SERVICE_DIGITAL_IDENTITY_SUBJECT_NAME_MATCH,
            serviceDigitalIdentity);
  }

  private void isX509SkiMatch(ValidationContext validationContext,
      ServiceDigitalIdentityType serviceDigitalIdentity) {
    digitalIdentitiesValidator
        .isListX509SkiMatch(validationContext,
            ViolationConstant.IS_SERVICE_DIGITAL_IDENTITY_X509SKI_MATCH,
            serviceDigitalIdentity);
  }

  private void isServiceDigitalIdentityCorrect(ValidationContext validationContext,
      ServiceDigitalIdentityType serviceDigitalIdentity, NonEmptyURIType serviceTypeIdentifier) {
    digitalIdentitiesValidator.isListServiceDigitalIdentityCorrect(validationContext,
        ViolationConstant.IS_SERVICE_DIGITAL_IDENTITY_CORRECT, serviceDigitalIdentity,
        serviceTypeIdentifier);
  }

  private void isServiceDigitalIdentityOrganizationMatch(ValidationContext validationContext,
      ServiceDigitalIdentityType serviceDigitalIdentity) {
    digitalIdentitiesValidator.isListServiceDigitalIdentityOrganizationMatch(validationContext,
        ViolationConstant.IS_SERVICE_DIGITAL_IDENTITY_X509CERTIFICATE_ORGANIZATION_MATCH,
        serviceDigitalIdentity);
  }
}
