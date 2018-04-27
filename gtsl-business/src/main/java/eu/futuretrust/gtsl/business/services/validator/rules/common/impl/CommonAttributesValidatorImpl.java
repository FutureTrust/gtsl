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

package eu.futuretrust.gtsl.business.services.validator.rules.common.impl;

import eu.futuretrust.gtsl.business.services.validator.rules.common.CommonAttributesValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIListType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CommonAttributesValidatorImpl implements CommonAttributesValidator {

  @Override
  public void isTslTypeCorrectValue(ValidationContext validationContext,
      ViolationConstant violation,
      NonEmptyURIType tslType) {
    String schemeTerritory = validationContext.getAttributes().getSchemeTerritory().getValue();
    // replace 'CC' argument with schemeTerritory
    String ccList = MessageFormat
        .format(validationContext.getProperties().getTslType().getCcList(), schemeTerritory);
    String ccListOfTheLists = MessageFormat
        .format(validationContext.getProperties().getTslType().getCcListofthelists(),
            schemeTerritory);

    String tslTypeValue = tslType.getValue();
    if (!tslTypeValue.equals(ccList) && !tslTypeValue.equals(ccListOfTheLists) && !tslTypeValue
        .equals(validationContext.getProperties().getTslType().getEuGeneric()) && !tslTypeValue
        .equals(validationContext.getProperties().getTslType().getEuListofthelists())) {
      validationContext.addViolation(new Violation(violation, tslTypeValue));
    }
  }

  @Override
  public void isSchemeTypeCommunityRulesCorrectValue(ValidationContext validationContext,
      ViolationConstant violation, NonEmptyMultiLangURIListType schemeTypeCommunityRules) {
    if (validationContext.isEuTsl() || validationContext.isNonEuTsl()) {
      String schemeTerritory = validationContext.getAttributes().getSchemeTerritory().getValue();
      // replace 'CC' argument with schemeTerritory
      String ccUri = MessageFormat
          .format(validationContext.getProperties().getSchemeRules().getCc(), schemeTerritory);
      List<NonEmptyMultiLangURIType> invalidSchemeTypeCommunityRules = schemeTypeCommunityRules
          .getValues().stream().filter(uri -> !uri.getValue().equals(ccUri) && !uri.getValue()
              .equals(validationContext.getProperties().getSchemeRules().getEuCommon()) && !uri
              .getValue()
              .equals(validationContext.getProperties().getSchemeRules().getEuListofthelists()))
          .collect(Collectors.toList());
      if (!invalidSchemeTypeCommunityRules.isEmpty()) {
        validationContext.addViolation(new Violation(violation,
            invalidSchemeTypeCommunityRules.stream().map(NonEmptyMultiLangURIType::getValue)
                .collect(Collectors.joining(" , "))));
      }
    }
  }

  @Override
  public void isServiceTypeIdentifierRegistered(ValidationContext validationContext,
      ViolationConstant violation, NonEmptyURIType serviceTypeIdentifier) {
    if (!isQualifiedService(validationContext, serviceTypeIdentifier) && !isNonQualifiedService(
        validationContext, serviceTypeIdentifier) && !isNationalService(validationContext,
        serviceTypeIdentifier)) {
      validationContext
          .addViolation(new Violation(violation, serviceTypeIdentifier.getValue()));
    }
  }

  @Override
  public void isServiceStatusCorrectValueForEuMs(ValidationContext validationContext,
      ViolationConstant violation, NonEmptyURIType serviceTypeIdentifier, NonEmptyURIType status) {
    if (validationContext.isEuTsl()) {
      if (isQualifiedService(validationContext, serviceTypeIdentifier)) {
        if (!status.getValue()
            .equals(validationContext.getProperties().getServiceStatus().getGranted())
            && !status.getValue()
            .equals(validationContext.getProperties().getServiceStatus().getWithdrawn())) {
          validationContext.addViolation(new Violation(violation, status.getValue()));
        }
      } else if (isNonQualifiedService(validationContext, serviceTypeIdentifier)
          || isNationalService(validationContext, serviceTypeIdentifier)) {
        if (!status.getValue()
            .equals(
                validationContext.getProperties().getServiceStatus().getRecognisedatnationallevel())
            && !status.getValue()
            .equals(validationContext.getProperties().getServiceStatus()
                .getDeprecatedatnationallevel())) {
          validationContext.addViolation(new Violation(violation, status.getValue()));
        }
      }
    }
  }

  @Override
  public void isServiceStatusCorrectValueForNonEu(ValidationContext validationContext,
      ViolationConstant violation, NonEmptyURIType serviceTypeIdentifier, NonEmptyURIType status) {
    if (validationContext.isNonEuTsl()) {
      if (validationContext.getAttributes().getSchemeInformationURI().getValues().stream()
          .noneMatch(uri -> uri.getValue().equals(status.getValue()))) {
        validationContext.addViolation(new Violation(violation, status.getValue()));
      }
    }
  }

  @Override
  public void isServiceStatusByNationalLawAllowed(ValidationContext validationContext,
      ViolationConstant violation, NonEmptyURIType serviceTypeIdentifier,
      NonEmptyURIType serviceStatus) {
    // set/deprecated by national law are only allowed on NationalRootCA-QC
    if (serviceStatus.getValue()
        .equals(validationContext.getProperties().getServiceStatus().getDeprecatedbynationallaw())
        || serviceStatus.getValue()
        .equals(validationContext.getProperties().getServiceStatus().getSetbynationallaw())) {
      if (!serviceTypeIdentifier.getValue().equals(
          validationContext.getProperties().getNationalServiceType().getNationalRootCAQC())) {
        validationContext.addViolation(
            new Violation(violation));
      }
    }
  }

  @Override
  public boolean isNonEmptyURIRegistered(Object properties, NonEmptyURIType serviceTypeIdentifier) {
    for (Field field : properties.getClass().getDeclaredFields()) {
      String value = getPropertyAsString(properties, field);
      if (value != null) {
        if (serviceTypeIdentifier.getValue().equals(value)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public List<String> propertiesAsList(Object properties) {
    List<String> listProperties = new ArrayList<>();
    for (Field field : properties.getClass().getDeclaredFields()) {
      String value = getPropertyAsString(properties, field);
      if (value != null) {
        listProperties.add(value);
      }
    }
    return listProperties;
  }

  private String getPropertyAsString(Object properties, Field field) {
    try {
      field.setAccessible(true);
      Object value = field.get(properties);
      if (value instanceof String) {
        return (String) value;
      }
    } catch (Exception ignored) {
      return null;
    }
    return null;
  }

}
