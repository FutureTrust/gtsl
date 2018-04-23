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

import eu.futuretrust.gtsl.vat.CheckVatService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VATService {

  private static final Logger LOGGER = LoggerFactory.getLogger(VATService.class);

  private final CheckVatService checkVatService;

  @Autowired
  public VATService(CheckVatService checkVatService) {
    this.checkVatService = checkVatService;
  }

  private boolean isActive() {
    return checkVatService != null;
  }

  public boolean isValidVAT(String vatNumber, String countryCodeString) {
    if (isActive()) {
      String convertedCountry = convert(countryCodeString);

      Holder<String> countryCodeHolder = new Holder<String>();
      countryCodeHolder.value = convertedCountry;

      Holder<String> vatNumberHolder = new Holder<String>();
      vatNumberHolder.value = vatNumber;

      Holder<XMLGregorianCalendar> requestDateHolder = new Holder<XMLGregorianCalendar>();
      requestDateHolder.value = null;

      Holder<Boolean> validHolder = new Holder<Boolean>();
      validHolder.value = false;

      Holder<String> nameHolder = new Holder<String>();
      nameHolder.value = null;

      Holder<String> addressHolder = new Holder<String>();
      addressHolder.value = null;

      try {
        checkVatService.getCheckVatPort()
            .checkVat(countryCodeHolder, vatNumberHolder, requestDateHolder, validHolder,
                nameHolder,
                addressHolder);
        Boolean valid = validHolder.value;
        if (!valid) {
          LOGGER.debug("VAT '" + convertedCountry + "' '" + vatNumber + "' is not valid");
        }
        return valid;
      } catch (Exception e) {
        LOGGER.error("Unable to call VAT WS : " + e.getMessage(), e);
        return false;
      }
    }
    LOGGER.error("Unable to open connection to CheckVatService");
    return false;
  }

  private String convert(String country) {
    if ("UK".equals(country)) {
      return "GB";
    }
    return country;
  }

}
