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
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIListType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyURIType;
import java.util.List;

public interface CommonAttributesValidator {

  void isTslTypeCorrectValue(ValidationContext validationContext, ViolationConstant violation,
      NonEmptyURIType tslType);

  void isSchemeTypeCommunityRulesCorrectValue(ValidationContext validationContext,
      ViolationConstant violation, NonEmptyMultiLangURIListType schemeTypeCommunityRules);

  void isServiceTypeIdentifierRegistered(ValidationContext validationContext,
      ViolationConstant violation, NonEmptyURIType serviceTypeIdentifier);

  void isServiceStatusCorrectValueForEuMs(ValidationContext validationContext,
      ViolationConstant violation,
      NonEmptyURIType serviceTypeIdentifier, NonEmptyURIType status);

  void isServiceStatusCorrectValueForNonEu(ValidationContext validationContext,
      ViolationConstant violation,
      NonEmptyURIType serviceTypeIdentifier, NonEmptyURIType status);

  void isServiceStatusByNationalLawAllowed(ValidationContext validationContext,
      ViolationConstant violation, NonEmptyURIType serviceTypeIdentifier,
      NonEmptyURIType serviceStatus);

  boolean isNonEmptyURIRegistered(Object properties, NonEmptyURIType serviceTypeIdentifier);

  default boolean isQualifiedService(ValidationContext validationContext,
      NonEmptyURIType serviceTypeIdentifier) {
    return isNonEmptyURIRegistered(validationContext.getProperties().getQualifiedServiceType(),
        serviceTypeIdentifier);
  }

  default boolean isNonQualifiedService(ValidationContext validationContext,
      NonEmptyURIType serviceTypeIdentifier) {
    return isNonEmptyURIRegistered(validationContext.getProperties().getNonQualifiedServiceType(),
        serviceTypeIdentifier);
  }

  default boolean isNationalService(ValidationContext validationContext,
      NonEmptyURIType serviceTypeIdentifier) {
    return isNonEmptyURIRegistered(validationContext.getProperties().getNationalServiceType(),
        serviceTypeIdentifier);
  }

  default boolean isValidServiceStatus(ValidationContext validationContext,
      NonEmptyURIType serviceStatus) {
    return isNonEmptyURIRegistered(validationContext.getProperties().getServiceStatus(),
        serviceStatus);
  }

  default boolean isValidServicePreviousStatus(ValidationContext validationContext,
      NonEmptyURIType serviceStatus) {
    return isNonEmptyURIRegistered(validationContext.getProperties().getServicePreviousStatus(),
        serviceStatus);
  }

  List<String> propertiesAsList(Object properties);
}
