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

package eu.futuretrust.gtsl.business.services.validator.rules.tsl;

import eu.futuretrust.gtsl.business.services.validator.rules.RulesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.AddressValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.CommonAttributesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.CountriesValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.AddressType;
import eu.futuretrust.gtsl.model.data.common.CountryCode;
import eu.futuretrust.gtsl.model.data.common.ExtensionsListType;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
import eu.futuretrust.gtsl.model.data.common.MultiLangNormStringType;
import eu.futuretrust.gtsl.model.data.common.MultiLangStringType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIListType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyNormalizedString;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import eu.futuretrust.gtsl.model.data.tsl.OtherTSLPointersType;
import eu.futuretrust.gtsl.model.data.tsl.PolicyOrLegalnoticeType;
import eu.futuretrust.gtsl.model.data.tsl.TSLSchemeInformationType;
import eu.futuretrust.gtsl.properties.RulesProperties;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchemeInformationValidatorImpl implements
    RulesValidator<TSLSchemeInformationType> {

  private final CountriesValidator countriesValidator;
  private final CommonAttributesValidator commonAttributesValidator;
  private final AddressValidator addressValidator;
  private final RulesValidator<OtherTSLPointersType> pointersValidator;

  @Autowired
  public SchemeInformationValidatorImpl(
      CountriesValidator countriesValidator,
      CommonAttributesValidator commonAttributesValidator,
      AddressValidator addressValidator,
      RulesValidator<OtherTSLPointersType> pointersValidator) {
    this.countriesValidator = countriesValidator;
    this.commonAttributesValidator = commonAttributesValidator;
    this.addressValidator = addressValidator;
    this.pointersValidator = pointersValidator;
  }

  @Override
  public void validate(ValidationContext validationContext,
      TSLSchemeInformationType schemeInformation) {

    isSchemeTerritoryCorrectValue(validationContext, schemeInformation.getSchemeTerritory());

    isTslTypeCorrectValue(validationContext, schemeInformation.getTslType());

    isSequenceNumberIncremented(validationContext, schemeInformation.getTslSequenceNumber());

    isTslVersionIdentiferCorrectValue(validationContext,
        schemeInformation.getTslVersionIdentifier());

    isHistoricalInformationPeriodCorrectValue(validationContext,
        schemeInformation.getHistoricalInformationPeriod());

    isIssueDateInThePast(validationContext, schemeInformation.getListIssueDateTime());

    isSchemeNameStartsWithCountryCode(validationContext, schemeInformation.getSchemeName());
    isSchemeNameEnglishCorrectValue(validationContext, schemeInformation.getSchemeName());
    isSchemeNameContainLangEn(validationContext, schemeInformation.getSchemeName());
    isSchemeNameLangAllowed(validationContext, schemeInformation.getSchemeName());

    isPolicyOrLegalNoticeNotEmpty(validationContext,
        schemeInformation.getPolicyOrLegalNotice());
    isPolicyOrLegalNoticeContainLangEn(validationContext,
        schemeInformation.getPolicyOrLegalNotice());
    isPolicyOrLegalNoticeLangAllowed(validationContext, schemeInformation.getPolicyOrLegalNotice());
    isLegalNoticeEnglishCorrectValue(validationContext, schemeInformation.getPolicyOrLegalNotice());

    isStatusDeterminationApproachCorrectValue(validationContext,
        schemeInformation.getStatusDeterminationApproach());

    isSchemeExtensionsNotPresentForEuMs(validationContext, schemeInformation.getSchemeExtensions());

    isSchemeInformationURIContainLangEn(validationContext,
        schemeInformation.getSchemeInformationURI());
    isSchemeInformationURILangAllowed(validationContext,
        schemeInformation.getSchemeInformationURI());

    isSchemeTypeCommunityRulesCorrectValues(validationContext,
        schemeInformation.getSchemeTypeCommunityRules());
    isSchemeTypeCommunityRulesContainLangEn(validationContext,
        schemeInformation.getSchemeTypeCommunityRules());
    isSchemeTypeCommunityRulesLangAllowed(validationContext,
        schemeInformation.getSchemeTypeCommunityRules());

    isSchemeOperatorNameContainLangEn(validationContext, schemeInformation.getSchemeOperatorName());
    isSchemeOperatorNameLangAllowed(validationContext, schemeInformation.getSchemeOperatorName());

    isElectronicAddressContainLangEn(validationContext,
        schemeInformation.getSchemeOperatorAddress());
    isPostalAddressesContainLangEn(validationContext, schemeInformation.getSchemeOperatorAddress());
    isElectronicAddressLangAllowed(validationContext, schemeInformation.getSchemeOperatorAddress());
    isPostalAddressesLangAllowed(validationContext, schemeInformation.getSchemeOperatorAddress());
    isPostalAddressesCountryAllowed(validationContext,
        schemeInformation.getSchemeOperatorAddress());

    isNextUpdateInTheFuture(validationContext, schemeInformation.getListIssueDateTime(),
        schemeInformation.getNextUpdate());
    isNextUpdateMax6MonthsAfterIssueDate(validationContext,
        schemeInformation.getListIssueDateTime(),
        schemeInformation.getNextUpdate());

    pointersValidator.validate(validationContext, schemeInformation.getPointersToOtherTSL());
  }

  private void isPolicyOrLegalNoticeNotEmpty(ValidationContext validationContext,
      PolicyOrLegalnoticeType policyOrLegalNotice) {
    if (policyOrLegalNotice == null) {
      validationContext.addViolation(
          new Violation(ViolationConstant.IS_POLICY_OR_LEGAL_NOTICE_EMPTY));
    } else {
      if (policyOrLegalNotice.getPolicy() == null && policyOrLegalNotice.getLegalNotice() == null) {
        validationContext.addViolation(
            new Violation(ViolationConstant.IS_POLICY_OR_LEGAL_NOTICE_EMPTY));
      } else {
        if (policyOrLegalNotice.getPolicy() == null) {
          if (CollectionUtils.isEmpty(policyOrLegalNotice.getLegalNotice().getValues())) {
            validationContext.addViolation(
                new Violation(ViolationConstant.IS_POLICY_OR_LEGAL_NOTICE_EMPTY));
          }
        } else if (policyOrLegalNotice.getLegalNotice() == null) {
          if (CollectionUtils.isEmpty(policyOrLegalNotice.getPolicy().getValues())) {
            validationContext.addViolation(
                new Violation(ViolationConstant.IS_POLICY_OR_LEGAL_NOTICE_EMPTY));
          }
        } else {
          if (CollectionUtils.isEmpty(policyOrLegalNotice.getPolicy().getValues())
              && CollectionUtils.isEmpty(policyOrLegalNotice.getLegalNotice().getValues())) {
            validationContext.addViolation(
                new Violation(ViolationConstant.IS_POLICY_OR_LEGAL_NOTICE_EMPTY));
          }
        }
      }
    }
  }

  private void isIssueDateInThePast(ValidationContext validationContext,
      LocalDateTime listIssueDateTime) {
    if (listIssueDateTime.isAfter(LocalDateTime.now())) {
      validationContext.addViolation(
          new Violation(ViolationConstant.IS_ISSUE_DATE_IN_THE_PAST, listIssueDateTime));
    }
  }

  private void isNextUpdateInTheFuture(ValidationContext validationContext, LocalDateTime issueDate,
      LocalDateTime nextUpdate) {
    if (!nextUpdate.isAfter(issueDate)) {
      validationContext
          .addViolation(new Violation(ViolationConstant.IS_NEXT_UPDATE_IN_THE_FUTURE, nextUpdate));
    }
  }

  private void isNextUpdateMax6MonthsAfterIssueDate(ValidationContext validationContext,
      LocalDateTime issueDate, LocalDateTime nextUpdate) {
    if (nextUpdate.isAfter(issueDate.plusMonths(6))) {
      validationContext
          .addViolation(
              new Violation(ViolationConstant.IS_NEXT_UPDATE_MAX_6_MONTHS_AFTER_ISSUE_DATE,
                  nextUpdate));
    }
  }

  private void isSequenceNumberIncremented(ValidationContext validationContext, BigInteger number) {
    if (validationContext.getAttributes().getCurrentSequenceNumber() != null) {
      BigInteger currentSequenceNumber = validationContext.getAttributes()
          .getCurrentSequenceNumber();
      int comparison = currentSequenceNumber.add(BigInteger.ONE).compareTo(number);
      if (comparison != 0) {
        // sequence number provided by the user is not strictly equal to the current sequence number +1
        if (comparison > 0) {
          // sequence number provided by the user is lower than or equal to the current sequence number
          validationContext
              .addViolation(
                  new Violation(ViolationConstant.IS_SEQUENCE_NUMBER_INCREMENTED, number));
        } else {
          // sequence number provided by the user is greater than the current sequence number but it is not +1
          validationContext
              .addViolation(
                  new Violation(ViolationConstant.IS_SEQUENCE_NUMBER_WELL_INCREMENTED, number));
        }
      }
    }
  }

  private void isSchemeExtensionsNotPresentForEuMs(ValidationContext validationContext,
      ExtensionsListType schemeExtensions) {
    if (schemeExtensions != null && CollectionUtils.isNotEmpty(schemeExtensions.getValues())) {
      validationContext.addViolation(
          new Violation(ViolationConstant.IS_SCHEME_EXTENSIONS_NOT_PRESENT_FOR_EU_MS));
    }
  }

  private void isPostalAddressesCountryAllowed(ValidationContext validationContext,
      AddressType schemeOperatorAddress) {
    addressValidator.isPostalAddressesCountryAllowed(validationContext,
        ViolationConstant.IS_SCHEME_POSTAL_ADDRESSES_COUNTRY_ALLOWED, schemeOperatorAddress);
  }

  private void isPostalAddressesLangAllowed(ValidationContext validationContext,
      AddressType schemeOperatorAddress) {
    addressValidator.isPostalAddressesLangAllowed(validationContext,
        ViolationConstant.IS_SCHEME_POSTAL_ADDRESSES_LANG_ALLOWED, schemeOperatorAddress);
  }

  private void isElectronicAddressLangAllowed(ValidationContext validationContext,
      AddressType schemeOperatorAddress) {
    addressValidator.isElectronicAddressLangAllowed(validationContext,
        ViolationConstant.IS_SCHEME_ELECTRONIC_ADDRESS_LANG_ALLOWED, schemeOperatorAddress);
  }

  private void isPostalAddressesContainLangEn(ValidationContext validationContext,
      AddressType schemeOperatorAddress) {
    addressValidator.isPostalAddressesContainLangEn(validationContext,
        ViolationConstant.IS_SCHEME_POSTAL_ADDRESSES_CONTAIN_LANG_EN, schemeOperatorAddress);
  }

  private void isElectronicAddressContainLangEn(ValidationContext validationContext,
      AddressType schemeOperatorAddress) {
    addressValidator.isElectronicAddressContainLangEn(validationContext,
        ViolationConstant.IS_SCHEME_ELECTRONIC_ADDRESS_CONTAIN_LANG_EN, schemeOperatorAddress);
  }

  private void isSchemeTerritoryCorrectValue(ValidationContext validationContext,
      CountryCode schemeTerritory) {
    countriesValidator
        .isCountryExist(validationContext, ViolationConstant.IS_SCHEME_TERRITORY_CORRECT_VALUE,
            schemeTerritory);
  }

  private void isTslTypeCorrectValue(ValidationContext validationContext, NonEmptyURIType tslType) {
    commonAttributesValidator
        .isTslTypeCorrectValue(validationContext, ViolationConstant.IS_TSL_TYPE_CORRECT_VALUE,
            tslType);
  }

  private void isTslVersionIdentiferCorrectValue(ValidationContext validationContext,
      BigInteger tslVersionIdentifier) {
    BigInteger tslVersionExpected = validationContext.getProperties().getConstant().getVersion();
    if (tslVersionIdentifier == null || tslVersionIdentifier.compareTo(tslVersionExpected) != 0) {
      validationContext
          .addViolation(new Violation(ViolationConstant.IS_TSL_VERSION_IDENTIFIER_CORRECT_VALUE,
              tslVersionIdentifier));
    }
  }

  private void isHistoricalInformationPeriodCorrectValue(ValidationContext validationContext,
      BigInteger historicalInformationPeriod) {
    BigInteger historicalInformationPeriodExpected = validationContext.getProperties().getConstant()
        .getHistoricalPeriod();
    if (historicalInformationPeriod == null
        || historicalInformationPeriod.compareTo(historicalInformationPeriodExpected) != 0) {
      validationContext.addViolation(
          new Violation(ViolationConstant.IS_HISTORICAL_PERIOD_CORRECT_VALUE,
              historicalInformationPeriod));
    }
  }

  private void isSchemeNameStartsWithCountryCode(ValidationContext validationContext,
      InternationalNamesType schemeName) {
    String schemeTerritory = validationContext.getAttributes().getSchemeTerritory().getValue();
    List<MultiLangNormStringType> invalidSchemeNames = schemeName.getValues().stream()
        .filter(name -> !name.getValue().startsWith(schemeTerritory + ":"))
        .collect(Collectors.toList());
    if (!invalidSchemeNames.isEmpty()) {
      validationContext.addViolation(
          new Violation(ViolationConstant.IS_SCHEME_NAME_START_WITH_COUNTRY_CODE,
              invalidSchemeNames.stream().map(NonEmptyNormalizedString::getValue)
                  .collect(Collectors.joining(" , "))));
    }
  }

  private void isSchemeNameEnglishCorrectValue(ValidationContext validationContext,
      InternationalNamesType schemeName) {
    if (validationContext.isEuTsl()) {
      String schemeTerritory = validationContext.getAttributes().getSchemeTerritory().getValue();
      List<MultiLangNormStringType> invalidEnglishSchemeName = schemeName.getValues().stream()
          .filter(name -> name.getLang().getValue().equals("en"))
          .filter(name -> !name.getValue()
              .equals(schemeTerritory + ":" + validationContext.getProperties().getConstant()
                  .getEnglishSchemeName()))
          .collect(Collectors.toList());
      if (!invalidEnglishSchemeName.isEmpty()) {
        validationContext.addViolation(
            new Violation(ViolationConstant.IS_SCHEME_NAME_ENGLISH_CORRECT_VALUE,
                invalidEnglishSchemeName.stream().map(NonEmptyNormalizedString::getValue)
                    .collect(Collectors.joining(" , "))));
      }
    }
  }

  private void isLegalNoticeEnglishCorrectValue(ValidationContext validationContext,
      PolicyOrLegalnoticeType policyOrLegalnotice) {
    if (validationContext.isEuTsl()) {
      if (policyOrLegalnotice != null && policyOrLegalnotice.getLegalNotice() != null
          && CollectionUtils.isNotEmpty(policyOrLegalnotice.getLegalNotice().getValues())) {
        List<MultiLangStringType> invalidEnglishLegalNotice = policyOrLegalnotice.getLegalNotice()
            .getValues()
            .stream()
            .filter(value -> value.getLang().getValue().equals("en"))
            .filter(value -> !value.getValue()
                .equals(validationContext.getProperties().getConstant().getEnglishLegalNotice()))
            .collect(Collectors.toList());
        if (!invalidEnglishLegalNotice.isEmpty()) {
          validationContext.addViolation(
              new Violation(ViolationConstant.IS_LEGAL_NOTICE_ENGLISH_CORRECT_VALUE,
                  invalidEnglishLegalNotice.stream().map(MultiLangStringType::getValue)
                      .collect(Collectors.joining(" , "))));
        }
      }
    }
  }

  private void isSchemeTypeCommunityRulesCorrectValues(ValidationContext validationContext,
      NonEmptyMultiLangURIListType schemeRules) {
    commonAttributesValidator.isSchemeTypeCommunityRulesCorrectValue(validationContext,
        ViolationConstant.IS_SCHEME_TYPE_COMMUNITY_RULES_CORRECT_VALUES, schemeRules);
  }

  private void isStatusDeterminationApproachCorrectValue(ValidationContext validationContext,
      NonEmptyURIType statusDeterminationApproach) {
    String schemeTerritory = validationContext.getAttributes().getSchemeTerritory().getValue();
    RulesProperties properties = validationContext.getProperties();
    // replace 'CC' argument with schemeTerritory
    String ccDetermination = MessageFormat
        .format(properties.getStatusDetn().getCcDetermination(),
            schemeTerritory);
    String statusDetnValue = statusDeterminationApproach.getValue();
    if (!statusDetnValue.equals(ccDetermination) && !statusDetnValue
        .equals(properties.getStatusDetn().getEuAppropriate()) && !statusDetnValue
        .equals(properties.getStatusDetn().getEuListofthelists())) {
      validationContext.addViolation(
          new Violation(ViolationConstant.IS_STATUS_DETERMINATION_APPROACH_CORRECT_VALUE,
              statusDetnValue));
    }
  }

  private void isSchemeOperatorNameContainLangEn(ValidationContext validationContext,
      InternationalNamesType schemeOperatorName) {
    countriesValidator
        .isListContainLangEn(validationContext,
            ViolationConstant.IS_SCHEME_OPERATOR_NAME_CONTAIN_LANG_EN,
            schemeOperatorName.getValues());
  }

  private void isSchemeNameContainLangEn(ValidationContext validationContext,
      InternationalNamesType schemeName) {
    countriesValidator
        .isListContainLangEn(validationContext, ViolationConstant.IS_SCHEME_NAME_CONTAIN_LANG_EN,
            schemeName.getValues());
  }

  private void isPolicyOrLegalNoticeContainLangEn(ValidationContext validationContext,
      PolicyOrLegalnoticeType policyOrLegalNotice) {
    if (policyOrLegalNotice != null && policyOrLegalNotice.getLegalNotice() != null
        && CollectionUtils.isNotEmpty(policyOrLegalNotice.getLegalNotice().getValues())) {
      countriesValidator.isListContainLangEn(validationContext,
          ViolationConstant.IS_POLICY_OR_LEGAL_NOTICE_CONTAIN_LANG_EN,
          policyOrLegalNotice.getLegalNotice().getValues());
    } else if (policyOrLegalNotice != null && policyOrLegalNotice.getPolicy() != null
        && CollectionUtils.isNotEmpty(policyOrLegalNotice.getPolicy().getValues())) {
      countriesValidator.isListContainLangEn(validationContext,
          ViolationConstant.IS_POLICY_OR_LEGAL_NOTICE_CONTAIN_LANG_EN,
          policyOrLegalNotice.getPolicy().getValues());
    } else {
      validationContext
          .addViolation(new Violation(ViolationConstant.IS_POLICY_OR_LEGAL_NOTICE_CONTAIN_LANG_EN));
    }
  }

  private void isSchemeInformationURIContainLangEn(ValidationContext validationContext,
      NonEmptyMultiLangURIListType schemeInformationURI) {
    countriesValidator
        .isListContainLangEn(validationContext,
            ViolationConstant.IS_SCHEME_INFORMATION_URI_CONTAIN_LANG_EN,
            schemeInformationURI.getValues());
  }

  private void isSchemeTypeCommunityRulesContainLangEn(ValidationContext validationContext,
      NonEmptyMultiLangURIListType schemeTypeCommunityRules) {
    countriesValidator
        .isListContainLangEn(validationContext,
            ViolationConstant.IS_SCHEME_TYPE_COMMUNITY_RULES_CONTAIN_LANG_EN,
            schemeTypeCommunityRules.getValues());
  }

  private void isSchemeOperatorNameLangAllowed(ValidationContext validationContext,
      InternationalNamesType schemeOperatorName) {
    countriesValidator.isListAttributeLangAllowed(validationContext,
        ViolationConstant.IS_SCHEME_OPERATOR_NAME_LANG_ALLOWED, schemeOperatorName.getValues());
  }

  private void isSchemeNameLangAllowed(ValidationContext validationContext,
      InternationalNamesType schemeName) {
    countriesValidator
        .isListAttributeLangAllowed(validationContext,
            ViolationConstant.IS_SCHEME_NAME_LANG_ALLOWED,
            schemeName.getValues());
  }

  private void isPolicyOrLegalNoticeLangAllowed(ValidationContext validationContext,
      PolicyOrLegalnoticeType policyOrLegalnotice) {
    if (policyOrLegalnotice != null && policyOrLegalnotice.getLegalNotice() != null
        && CollectionUtils.isNotEmpty(policyOrLegalnotice.getLegalNotice().getValues())) {
      countriesValidator.isListAttributeLangAllowed(validationContext,
          ViolationConstant.IS_POLICY_OR_LEGAL_NOTICE_LANG_ALLOWED,
          policyOrLegalnotice.getLegalNotice().getValues());
    } else if (policyOrLegalnotice != null && policyOrLegalnotice.getPolicy() != null
        && CollectionUtils.isNotEmpty(policyOrLegalnotice.getPolicy().getValues())) {
      countriesValidator.isListAttributeLangAllowed(validationContext,
          ViolationConstant.IS_POLICY_OR_LEGAL_NOTICE_LANG_ALLOWED,
          policyOrLegalnotice.getPolicy().getValues());
    } else {
      validationContext
          .addViolation(new Violation(ViolationConstant.IS_POLICY_OR_LEGAL_NOTICE_LANG_ALLOWED));
    }
  }

  private void isSchemeInformationURILangAllowed(ValidationContext validationContext,
      NonEmptyMultiLangURIListType schemeInformationURI) {
    countriesValidator.isListAttributeLangAllowed(validationContext,
        ViolationConstant.IS_SCHEME_INFORMATION_URI_LANG_ALLOWED, schemeInformationURI.getValues());
  }

  private void isSchemeTypeCommunityRulesLangAllowed(ValidationContext validationContext,
      NonEmptyMultiLangURIListType schemeTypeCommunityRules) {
    countriesValidator.isListAttributeLangAllowed(validationContext,
        ViolationConstant.IS_SCHEME_TYPE_COMMUNITY_RULES_LANG_ALLOWED,
        schemeTypeCommunityRules.getValues());
  }

}
