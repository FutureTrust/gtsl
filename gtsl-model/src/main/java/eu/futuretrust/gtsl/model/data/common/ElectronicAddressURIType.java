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

import eu.futuretrust.gtsl.jaxb.tsl.NonEmptyMultiLangURITypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;

import javax.validation.constraints.NotNull;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.5.2 Scheme operator electronic address
 */
public class ElectronicAddressURIType extends NonEmptyMultiLangURIType {

  public ElectronicAddressURIType() {
  }

  public ElectronicAddressURIType(NonEmptyMultiLangURITypeJAXB multiLangURI) {
    super(multiLangURI);
  }

  public ElectronicAddressURIType(
      @NotNull(message = "{NotNull.string.value}", payload = {
          Severity.Error.class, Impact.Legal.class}) String value, Lang lang) {
    super(value, lang);
  }

  @Override
  //@ValidElectronicAddress(payload = {Severity.Warning.class, Impact.Legal.class})
  public @NotNull String getValue() {
    return super.getValue();
  }

}
