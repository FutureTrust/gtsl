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

package eu.futuretrust.gtsl.business.services.validator.rules.tsp;

import eu.futuretrust.gtsl.business.services.validator.rules.RulesValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.AddressValidator;
import eu.futuretrust.gtsl.business.services.validator.rules.common.CountriesValidator;
import eu.futuretrust.gtsl.business.validator.ViolationConstant;
import eu.futuretrust.gtsl.business.vo.validator.ValidationContext;
import eu.futuretrust.gtsl.business.vo.validator.Violation;
import eu.futuretrust.gtsl.model.data.common.AddressType;
import eu.futuretrust.gtsl.model.data.common.ExtensionType;
import eu.futuretrust.gtsl.model.data.common.ExtensionsListType;
import eu.futuretrust.gtsl.model.data.common.InternationalNamesType;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIListType;
import eu.futuretrust.gtsl.model.data.tsp.TSPInformationType;
import eu.futuretrust.gtsl.model.utils.ModelUtils;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TspInformationValidatorImpl implements RulesValidator<TSPInformationType> {

  private final CountriesValidator countriesValidator;
  private final AddressValidator addressValidator;
  private final VATService vatService;

  @Autowired
  public TspInformationValidatorImpl(
      CountriesValidator countriesValidator,
      AddressValidator addressValidator,
      VATService vatService) {
    this.countriesValidator = countriesValidator;
    this.addressValidator = addressValidator;
    this.vatService = vatService;
  }

  @Override
  public void validate(ValidationContext validationContext, TSPInformationType tspInformation) {

    isTspNameContainLangEn(validationContext, tspInformation.getTspName());
    isTspNameLangAllowed(validationContext, tspInformation.getTspName());

    isTspTradeNameContainLangEn(validationContext, tspInformation.getTspTradeName());
    isTspTradeNameLangAllowed(validationContext, tspInformation.getTspTradeName());
    isTspTradeNameCorrectValue(validationContext, tspInformation.getTspTradeName());
    isTspTradeNameVatNumberValid(validationContext, tspInformation.getTspTradeName());

    isTspInformationURIContainLangEn(validationContext, tspInformation.getTspInformationURI());
    isTspInformationURILangAllowed(validationContext, tspInformation.getTspInformationURI());

    isElectronicAddressContainLangEn(validationContext, tspInformation.getTspAddress());
    isPostalAddressesContainLangEn(validationContext, tspInformation.getTspAddress());
    isElectronicAddressLangAllowed(validationContext, tspInformation.getTspAddress());
    isPostalAddressesLangAllowed(validationContext, tspInformation.getTspAddress());
    isPostalAddressesCountryAllowed(validationContext, tspInformation.getTspAddress());

    if (validationContext.isEuTsl()) {
      if (tspInformation.getTspInformationExtensions() != null) {
        isTspInformationExtensionsNotCritical(validationContext,
            tspInformation.getTspInformationExtensions());
      }
    }
  }

  private void isTspInformationExtensionsNotCritical(ValidationContext validationContext,
      ExtensionsListType tspInformationExtensions) {
    if (CollectionUtils.isNotEmpty(tspInformationExtensions.getValues()) && tspInformationExtensions
        .getValues().stream().anyMatch(ExtensionType::getCritical)) {
      validationContext.addViolation(
          new Violation(ViolationConstant.IS_TSP_INFORMATION_EXTENSIONS_NOT_CRITICAL_FOR_EU_MS));
    }
  }

  private void isTspNameLangAllowed(ValidationContext validationContext,
      InternationalNamesType tspName) {
    countriesValidator
        .isListAttributeLangAllowed(validationContext, ViolationConstant.IS_TSP_NAME_LANG_ALLOWED,
            tspName.getValues());
  }

  private void isTspNameContainLangEn(
      ValidationContext validationContext, InternationalNamesType tspName) {
    countriesValidator
        .isListContainLangEn(validationContext, ViolationConstant.IS_TSP_NAME_CONTAIN_LANG_EN,
            tspName.getValues());
  }

  private void isTspTradeNameLangAllowed(
      ValidationContext validationContext, InternationalNamesType tspTradeName) {
    countriesValidator
        .isListAttributeLangAllowed(validationContext,
            ViolationConstant.IS_TSP_TRADE_NAME_LANG_ALLOWED,
            tspTradeName.getValues());
  }

  private void isTspTradeNameContainLangEn(
      ValidationContext validationContext, InternationalNamesType tspTradeName) {
    countriesValidator
        .isListContainLangEn(validationContext, ViolationConstant.IS_TSP_TRADE_NAME_CONTAIN_LANG_EN,
            tspTradeName.getValues());
  }

  private void isTspTradeNameCorrectValue(ValidationContext validationContext,
      InternationalNamesType tspTradeName) {
    String cc = validationContext.getAttributes().getSchemeTerritory().getValue();
    if (tspTradeName.getValues().stream().noneMatch(
        name -> name.getValue().startsWith(ModelUtils.VAT_PREFIX + cc + "-")
            || name.getValue().startsWith(ModelUtils.NTR_PREFIX + cc + "-")
            || name.getValue().startsWith(ModelUtils.PAS_PREFIX + cc + "-")
            || name.getValue().startsWith(ModelUtils.IDC_PREFIX + cc + "-")
            || name.getValue().startsWith(ModelUtils.PNO_PREFIX + cc + "-")
            || name.getValue().startsWith(ModelUtils.TIN_PREFIX + cc + "-"))) {
      validationContext
          .addViolation(new Violation(ViolationConstant.IS_TSP_TRADE_NAME_CORRECT_VALUE,
              tspTradeName.getValues().stream().map(Object::toString)
                  .collect(Collectors.joining(" , "))));
    }
  }

  private void isTspTradeNameVatNumberValid(ValidationContext validationContext,
      InternationalNamesType tspTradeName) {
    String vatNumber = ModelUtils.getTspTradeNameByPrefix(tspTradeName, ModelUtils.VAT_PREFIX);
    if (vatNumber != null) {
      // from Tl-Manager
      String cleaned = StringUtils.replace(vatNumber, ModelUtils.VAT_PREFIX, "");
      String[] split = cleaned.split("-");
      boolean isValidVat = vatService.isValidVAT(split[1], split[0]);
      if (!isValidVat) {
        validationContext.addViolation(
            new Violation(ViolationConstant.IS_TSP_TRADE_NAME_VAT_NUMBER_VALID, vatNumber));
      }
    }
  }

  private void isTspInformationURILangAllowed(
      ValidationContext validationContext, NonEmptyMultiLangURIListType tspInformationURI) {
    countriesValidator
        .isListAttributeLangAllowed(validationContext,
            ViolationConstant.IS_TSP_INFORMATION_URI_LANG_ALLOWED,
            tspInformationURI.getValues());
  }

  private void isTspInformationURIContainLangEn(
      ValidationContext validationContext, NonEmptyMultiLangURIListType tspInformationURI) {
    countriesValidator
        .isListContainLangEn(validationContext,
            ViolationConstant.IS_TSP_INFORMATION_URI_CONTAIN_LANG_EN,
            tspInformationURI.getValues());
  }

  private void isPostalAddressesCountryAllowed(ValidationContext validationContext,
      AddressType schemeOperatorAddress) {
    addressValidator.isPostalAddressesCountryAllowed(validationContext,
        ViolationConstant.IS_TSP_POSTAL_ADDRESSES_COUNTRY_ALLOWED, schemeOperatorAddress);
  }

  private void isPostalAddressesLangAllowed(ValidationContext validationContext,
      AddressType schemeOperatorAddress) {
    addressValidator
        .isPostalAddressesLangAllowed(validationContext,
            ViolationConstant.IS_TSP_POSTAL_ADDRESSES_LANG_ALLOWED, schemeOperatorAddress);
  }

  private void isPostalAddressesContainLangEn(ValidationContext validationContext,
      AddressType schemeOperatorAddress) {
    addressValidator
        .isPostalAddressesContainLangEn(validationContext,
            ViolationConstant.IS_TSP_POSTAL_ADDRESSES_CONTAIN_LANG_EN, schemeOperatorAddress);
  }

  private void isElectronicAddressLangAllowed(ValidationContext validationContext,
      AddressType schemeOperatorAddress) {
    addressValidator
        .isElectronicAddressLangAllowed(validationContext,
            ViolationConstant.IS_TSP_ELECTRONIC_ADDRESS_LANG_ALLOWED, schemeOperatorAddress);
  }

  private void isElectronicAddressContainLangEn(ValidationContext validationContext,
      AddressType schemeOperatorAddress) {
    addressValidator
        .isElectronicAddressContainLangEn(validationContext,
            ViolationConstant.IS_TSP_ELECTRONIC_ADDRESS_CONTAIN_LANG_EN, schemeOperatorAddress);
  }

}
