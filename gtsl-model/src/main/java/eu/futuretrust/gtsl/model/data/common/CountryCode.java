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

package eu.futuretrust.gtsl.model.data.common;

import eu.futuretrust.gtsl.model.constraints.Case;
import eu.futuretrust.gtsl.model.constraints.enums.CaseMode;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;

import javax.validation.constraints.NotNull;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.1.5 Value of Country Code fields
 *
 * All fields carrying Country Codes values, denoted by "CC", shall be in capital letters and in
 * accordance with either: a) ISO 3166-1 [15] Alpha 2 codes with the following exceptions: 1) the
 * Country Code for United Kingdom shall be "UK"; 2) the Country Code for Greece shall be "EL"; 3)
 * when the scope of the field is the European Union and/or the European Commission the code "EU"
 * shall be used; or b) commonly used extensions with regional scope (e.g. AP for Asia Pacific,
 * ASIA); or c) another identifier recognized for identifying multi-state grouping and that does not
 * conflict with a), or b) (e.g. GCC, ASEAN).
 *
 * @see <a href="https://www.iso.org/iso-3166-country-codes.html">ISO 3166</a>
 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">IETF RFC 3986</a>
 */
public class CountryCode extends NonEmptyString {

  public CountryCode() {
  }

  public CountryCode(
      @NotNull(message = "{NotNull.string.value}", payload = {
          Severity.Error.class, Impact.Legal.class}) String value) {
    super(value);
  }

  @Override
  @Case(value = CaseMode.UPPER, message = "{UpperCase.countryCode.value}", payload = {Severity.Error.class, Impact.Legal.class})
  public String getValue() {
    return super.getValue();
  }

}
