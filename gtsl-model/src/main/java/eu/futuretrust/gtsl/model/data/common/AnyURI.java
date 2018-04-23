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

import eu.futuretrust.gtsl.model.constraints.ValidURI;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;

import javax.validation.constraints.NotNull;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.1.2 Use of Uniform Resource Identifiers
 *
 * Where fields are defined as being of or using the type URI, implementers shall use general syntax
 * as specified by IETF RFC 3986 [8].
 *
 * @see <a href="https://www.w3.org/TR/xmlschema-2/#anyURI">w3.org - anyURI</a>
 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">IETF RFC 3986</a>
 */
public class AnyURI extends StringType {

  public AnyURI() {}

  public AnyURI(
      @NotNull(message = "{NotNull.string.value}", payload = {
          Severity.Error.class, Impact.Legal.class}) String value) {
    super(value);
  }

  @ValidURI(payload = {Severity.Error.class, Impact.Legal.class})
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
