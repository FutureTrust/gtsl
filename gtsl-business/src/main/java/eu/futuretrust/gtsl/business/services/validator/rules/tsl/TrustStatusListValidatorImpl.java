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
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.AnyURI;
import eu.futuretrust.gtsl.model.data.tsl.TSLSchemeInformationType;
import eu.futuretrust.gtsl.model.data.tsl.TrustServiceProviderListType;
import eu.futuretrust.gtsl.model.data.tsl.TrustStatusListType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrustStatusListValidatorImpl implements RulesValidator<TrustStatusListType> {

  private final RulesValidator<TSLSchemeInformationType> schemeInformationValidator;
  private final RulesValidator<TrustServiceProviderListType> trustServiceProvidersValidator;

  @Autowired
  public TrustStatusListValidatorImpl(
          RulesValidator<TSLSchemeInformationType> schemeInformationValidator,
          RulesValidator<TrustServiceProviderListType> trustServiceProvidersValidator) {
    this.schemeInformationValidator = schemeInformationValidator;
    this.trustServiceProvidersValidator = trustServiceProvidersValidator;
  }

  @Override
  public void validate(ValidationContext validationContext, TrustStatusListType tsl) {
    // Check attributes of TSL element

    this.isTslTagCorrectValue(validationContext, tsl.getTslTag());

    // Run validation on Scheme Information
    schemeInformationValidator.validate(validationContext, tsl.getSchemeInformation());

    // Trust Service Providers element is optional
    trustServiceProvidersValidator.validate(validationContext, tsl.getTrustServiceProviderList());
  }

  private void isTslTagCorrectValue(ValidationContext validationObject, AnyURI tslTag) {
    String tslTagExpected = validationObject.getProperties().getTsl().getTag();
    if (!tslTag.getValue().equals(tslTagExpected)) {
      validationObject.addViolation(
          new Violation(ViolationConstant.IS_TSL_TAG_CORRECT_VALUE, tslTag.getValue()));
    }
  }

}
