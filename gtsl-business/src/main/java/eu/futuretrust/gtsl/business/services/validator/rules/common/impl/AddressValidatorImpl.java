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

import eu.futuretrust.gtsl.business.services.validator.rules.common.AddressValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.CountriesValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.AddressType;
import eu.futuretrust.gtsl.model.data.common.PostalAddressType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressValidatorImpl implements AddressValidator {

  private final CountriesValidator countriesValidator;

  @Autowired
  protected AddressValidatorImpl(CountriesValidator countriesValidator) {
    this.countriesValidator = countriesValidator;
  }

  @Override
  public void isElectronicAddressLangAllowed(ValidationContext validationContext,
      ViolationConstant violation, AddressType schemeOperatorAddress) {
    countriesValidator.isListAttributeLangAllowed(validationContext, violation,
        schemeOperatorAddress.getElectronicAddress().getValues());
  }

  @Override
  public void isPostalAddressesLangAllowed(ValidationContext validationContext,
      ViolationConstant violation, AddressType schemeOperatorAddress) {
    countriesValidator.isListAttributeLangAllowed(validationContext, violation,
        schemeOperatorAddress.getPostalAddresses().getValues());
  }

  @Override
  public void isElectronicAddressContainLangEn(ValidationContext validationContext,
      ViolationConstant violation, AddressType schemeOperatorAddress) {
    countriesValidator.isListContainLangEn(validationContext, violation,
        schemeOperatorAddress.getElectronicAddress().getValues());
  }

  @Override
  public void isPostalAddressesContainLangEn(ValidationContext validationContext,
      ViolationConstant violation, AddressType schemeOperatorAddress) {
    countriesValidator.isListContainLangEn(validationContext, violation,
        schemeOperatorAddress.getPostalAddresses().getValues());
  }

  @Override
  public void isPostalAddressesCountryAllowed(ValidationContext validationContext,
      ViolationConstant violation, AddressType schemeOperatorAddress) {
    if (validationContext.isEuTsl() || validationContext.isNonEuTsl()) {
      for (PostalAddressType postalAddress : schemeOperatorAddress.getPostalAddresses()
          .getValues()) {
        if (!postalAddress.getCountryName()
            .equals(validationContext.getAttributes().getSchemeTerritory())) {
          validationContext
              .addViolation(new Violation(violation, postalAddress.getCountryName().getValue()));
        } else {
          countriesValidator
              .isCountryExist(validationContext, violation, postalAddress.getCountryName());
        }
      }
    }
  }

}
