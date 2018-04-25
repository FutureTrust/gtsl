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

package eu.futuretrust.gtsl.business.services.validator.rules.pointers;

import eu.futuretrust.gtsl.business.services.validator.rules.RulesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.CommonAttributesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.CountriesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.DigitalIdentitiesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.signature.CertificateValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.signature.X500NameValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.CountryCode;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIListType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import eu.futuretrust.gtsl.model.data.digitalidentity.DigitalIdentityType;
import eu.futuretrust.gtsl.model.data.digitalidentity.ServiceDigitalIdentityListType;
import eu.futuretrust.gtsl.model.data.digitalidentity.ServiceDigitalIdentityType;
import eu.futuretrust.gtsl.model.data.enums.MimeType;
import eu.futuretrust.gtsl.model.data.ts.CertificateType;
import eu.futuretrust.gtsl.model.data.tsl.OtherTSLPointerType;
import java.io.IOException;
import java.security.cert.X509Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.x509.X500Name;

@Service
public class PointerToOtherTslValidatorImpl implements RulesValidator<OtherTSLPointerType> {

  private final DigitalIdentitiesValidator digitalIdentitiesValidator;
  private final CountriesValidator countriesValidator;
  private final CommonAttributesValidator commonAttributesValidator;
  private final CertificateValidator certificateValidator;
  private final X500NameValidator x500NameValidator;

  @Autowired
  public PointerToOtherTslValidatorImpl(
      DigitalIdentitiesValidator digitalIdentitiesValidator,
      CountriesValidator countriesValidator,
      CommonAttributesValidator commonAttributesValidator,
      CertificateValidator certificateValidator,
      X500NameValidator x500NameValidator) {
    this.digitalIdentitiesValidator = digitalIdentitiesValidator;
    this.countriesValidator = countriesValidator;
    this.commonAttributesValidator = commonAttributesValidator;
    this.certificateValidator = certificateValidator;
    this.x500NameValidator = x500NameValidator;
  }

  @Override
  public void validate(ValidationContext validationContext, OtherTSLPointerType pointer) {

    isServiceDigitalIdentitiesValid(validationContext, pointer.getServiceDigitalIdentities());

    isTslTypeCorrectValue(validationContext, pointer.getTslType());

    isSchemeTypeCommunityRulesCorrectValue(validationContext, pointer.getSchemeRules());

    isSchemeTerritoryCorrectValue(validationContext, pointer.getSchemeTerritory());

    isCertificateExpired(validationContext, pointer.getServiceDigitalIdentities());
    isValidCertificate(validationContext, pointer.getServiceDigitalIdentities());
    isSubjectNameMatch(validationContext, pointer.getServiceDigitalIdentities());
    isX509SkiMatch(validationContext, pointer.getServiceDigitalIdentities());

    if (validationContext.isLotl()) {
      checkX509Certificate(validationContext, pointer);
    }
  }

  private void isServiceDigitalIdentitiesValid(ValidationContext validationContext,
      ServiceDigitalIdentityListType serviceDigitalIdentities) {
    digitalIdentitiesValidator
        .isServiceDigitalIdentitiesValid(validationContext, serviceDigitalIdentities);
  }


  private void isCertificateExpired(ValidationContext validationContext,
      ServiceDigitalIdentityListType serviceDigitalIdentities) {
    serviceDigitalIdentities.getValues().forEach(serviceDigitalId -> digitalIdentitiesValidator
        .isListCertificateExpired(validationContext,
            ViolationConstant.IS_POINTER_CERTIFICATE_EXPIRED, serviceDigitalId));
  }

  private void isValidCertificate(ValidationContext validationContext,
      ServiceDigitalIdentityListType serviceDigitalIdentities) {
    serviceDigitalIdentities.getValues().forEach(serviceDigitalId -> digitalIdentitiesValidator
        .isListValidCertificate(validationContext, ViolationConstant.IS_POINTER_VALID_CERTIFICATE,
            serviceDigitalId));
  }

  private void isSubjectNameMatch(ValidationContext validationContext,
      ServiceDigitalIdentityListType serviceDigitalIdentities) {
    serviceDigitalIdentities.getValues().forEach(serviceDigitalId -> digitalIdentitiesValidator
        .isListSubjectNameMatch(validationContext, ViolationConstant.IS_POINTER_SUBJECT_NAME_MATCH,
            serviceDigitalId));
  }

  private void isX509SkiMatch(ValidationContext validationContext,
      ServiceDigitalIdentityListType serviceDigitalIdentities) {
    serviceDigitalIdentities.getValues().forEach(serviceDigitalId -> digitalIdentitiesValidator
        .isListX509SkiMatch(validationContext, ViolationConstant.IS_POINTER_X509SKI_MATCH,
            serviceDigitalId));
  }

  private void isSchemeTypeCommunityRulesCorrectValue(ValidationContext validationContext,
      NonEmptyMultiLangURIListType schemeRules) {
    commonAttributesValidator.isSchemeTypeCommunityRulesCorrectValue(validationContext,
        ViolationConstant.IS_POINTER_SCHEME_RULES_CORRECT_VALUES, schemeRules);
  }

  private void isTslTypeCorrectValue(ValidationContext validationContext, NonEmptyURIType tslType) {
    commonAttributesValidator.isTslTypeCorrectValue(validationContext,
        ViolationConstant.IS_POINTER_TSL_TYPE_CORRECT_VALUE, tslType);
  }

  private void isSchemeTerritoryCorrectValue(ValidationContext validationContext,
      CountryCode schemeTerritory) {
    countriesValidator
        .isCountryExist(validationContext,
            ViolationConstant.IS_POINTER_SCHEME_TERRITORY_CORRECT_VALUE,
            schemeTerritory);
  }

  // from TL-Manager
  private void checkX509Certificate(ValidationContext validationContext,
      OtherTSLPointerType pointer) {
    if (!pointer.getSchemeTerritory().getValue()
        .equals(validationContext.getProperties().getConstant().getLotlTerritory()) &&
        pointer.getMimeType() != null && MimeType.XML.equals(pointer.getMimeType())) {
      for (ServiceDigitalIdentityType serviceDigitalIdentity : pointer.getServiceDigitalIdentities()
          .getValues()) {
        for (DigitalIdentityType digitalIdentification : serviceDigitalIdentity.getValues())
        {
          if (digitalIdentification.getCertificateList() == null)
          {
            if (digitalIdentification.getSubjectName() != null && !digitalIdentification.getSubjectName().isEmpty())
            {
              try
              {
                X500Name x500Name = new X500Name(digitalIdentification.getSubjectName());
                checkX509SubjectName(validationContext, x500Name, pointer);
              } catch (final IOException e)
              {
                validationContext.addViolation(new Violation(ViolationConstant.IS_POINTER_X509CERTIFICATE_COUNTRY_CODE_MATCH));
                validationContext.addViolation(new Violation(ViolationConstant.IS_POINTER_X509CERTIFICATE_ORGANIZATION_MATCH));
              }
            }
          } else
          {
            for (CertificateType cert : digitalIdentification.getCertificateList())
            {
              if (cert.getToken() == null)
              {
                cert.setTokenFromEncoded();
              }

              X509Certificate certificate = cert.getToken().getCertificate();
              certificateValidator.isSigningCertificateKeyUsageConsistent(validationContext,
                      ViolationConstant.IS_POINTER_X509CERTIFICATE_CONTAINS_CORRECT_KEY_USAGES, certificate);
              certificateValidator.isSigningCertificateBasicConstraintsExtensionValid(validationContext,
                      ViolationConstant.IS_POINTER_X509CERTIFICATE_CONTAINS_BASIC_CONSTRAINT_CA_FALSE,
                      certificate);
              certificateValidator
                      .hasSigningCertificateExtendedKeyUsageTslSigning(validationContext,
                              ViolationConstant.IS_POINTER_X509CERTIFICATE_CONTAINS_TSLSIGNING_EXT_KEY_USAGE,
                              certificate);
              certificateValidator
                      .isSigningCertificateSKIExtensionPresent(validationContext,
                              ViolationConstant.IS_POINTER_X509CERTIFICATE_CONTAINS_SUBJECT_KEY_IDENTIFIER,
                              certificate);
              certificateValidator.isSigningCertificateCountryCodeValid(validationContext,
                      ViolationConstant.IS_POINTER_X509CERTIFICATE_COUNTRY_CODE_MATCH, certificate,
                      pointer.getSchemeTerritory().getValue());
              certificateValidator.isSigningCertificateOrganizationValid(validationContext,
                      ViolationConstant.IS_POINTER_X509CERTIFICATE_ORGANIZATION_MATCH, certificate,
                      pointer.getSchemeOperatorName().getValues());
            }
          }
        }
      }
    }
  }

  private void checkX509SubjectName(ValidationContext validationContext, final X500Name subjectName, final OtherTSLPointerType pointer) {

    x500NameValidator.isX500NameCountryCodeValid(validationContext, ViolationConstant.IS_POINTER_X509CERTIFICATE_COUNTRY_CODE_MATCH,
            subjectName, pointer.getSchemeTerritory().getValue());
    x500NameValidator.isX500NameOrganizationValid(validationContext, ViolationConstant.IS_POINTER_X509CERTIFICATE_ORGANIZATION_MATCH,
            subjectName, pointer.getSchemeOperatorName().getValues());

  }

}