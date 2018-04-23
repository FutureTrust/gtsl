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
import eu.futuretrust.gtsl.model.data.extension.ServiceInformationExtensionListType;

public interface ServiceExtensionValidator {

  void validateExtension(ValidationContext validationContext,
      ServiceInformationExtensionListType extensions,
      NonEmptyURIType serviceTypeIdentifier,
      ViolationConstant violationQualificationElementPresent,
      ViolationConstant violationQualificationExtensionAllowed,
      ViolationConstant violationQualifiersPresent,
      ViolationConstant violationCriteriaAssertsNotNull,
      ViolationConstant violationQualifiersURICorrectValue,
      ViolationConstant violationExpiredCertsRevocationNotCritical,
      ViolationConstant violationExpiredCertsRevocationAllowed,
      ViolationConstant violationAsiAllowed,
      ViolationConstant violationAsiSignatureAllowed,
      ViolationConstant violationAsiSignatureOrSealAllowed,
      ViolationConstant violationAsiLangAllowed);
}
