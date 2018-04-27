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

import eu.futuretrust.gtsl.business.services.validator.rules.common.CountriesValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.CountryCode;
import eu.futuretrust.gtsl.model.data.common.Lang;
import eu.futuretrust.gtsl.model.data.common.MultiLang;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CountriesValidatorImpl implements CountriesValidator {

  private final static Lang EN_LANG = new Lang("en");

  @Override
  public <T extends MultiLang> void isListContainLangEn(ValidationContext validationContext,
      ViolationConstant violation, List<T> list) {
    if (list.stream().noneMatch(multiLangObject -> multiLangObject.getLang().equals(EN_LANG))) {
      validationContext.addViolation(new Violation(violation,
          list.stream().map(obj -> obj.getLang().getValue()).collect(Collectors.joining(" , "))));
    }
  }

  @Override
  public void isAttributeLangAllowed(ValidationContext validationContext,
      ViolationConstant violation,
      Lang lang) {
    if (validationContext.isEuTsl() || validationContext.isNonEuTsl()) {
      // English is allowed for all countries
      if (lang.equals(EN_LANG)) {
        return;
      }
      CountryCode country = validationContext.getAttributes().getSchemeTerritory();
      if (isCountryExist(validationContext, country) && !validationContext.getProperties()
          .getCountries().get(country.getValue()).getLang().contains(lang.getValue())) {
        validationContext.addViolation(new Violation(violation, lang.getValue()));
      }
    }
  }

  @Override
  public void isCountryExist(ValidationContext validationContext, ViolationConstant violation,
      CountryCode countryCode) {
    if (validationContext.isEuTsl() || validationContext.isNonEuTsl()) {
      if (!isCountryExist(validationContext, countryCode)) {
        validationContext.addViolation(new Violation(violation, countryCode.getValue()));
      }
    }
  }

  private boolean isCountryExist(ValidationContext validationContext, CountryCode countryCode) {
    return validationContext.getProperties().getCountries().containsKey(countryCode.getValue());
  }
}
