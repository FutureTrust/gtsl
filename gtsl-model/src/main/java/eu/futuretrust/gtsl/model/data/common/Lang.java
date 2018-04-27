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
 * Specified in TS 119 612 v2.1.1 clause 5.1.4 Language support
 *
 * A multilingual character string shall be a character string as defined in ISO/IEC 10646 encoded
 * in UTF-8. Each multilingual pointer shall consist of two parts: a tag, conformant to IETF RFC
 * 5646, that identifies the language in which the content pointed-to by the URI is expressed, and
 * the URI expressed as a character string with the syntax specified by IETF RFC 3986 , identifying
 * a resource expressed in the given language. The same content may be represented in multiple
 * languages by a sequence of multilingual pointers. Whenever the native terms cannot be represented
 * using the Latin alphabet, as defined in ISO/IEC 10646, one issue of the term in the native
 * language plus one issue with a transliteration to the Latin alphabet shall be used. Implementers
 * should also comply with the UNICODE Standard (available at http://www.unicode.org/standard/standard.html).
 * Further detailed requirements regarding multilingual
 *
 * @see <a href="https://www.iso.org/standard/63182.html">ISO/IEC 10646:2014</a>
 * @see <a href="https://tools.ietf.org/html/rfc5646">IETF RFC 5646</a>
 * @see <a href="http://www.unicode.org/standard/standard.html">UNICODE Standard</a>
 */
public class Lang extends NonEmptyString {

  public Lang() {
  }

  public Lang(
      @NotNull(message = "{NotNull.string.value}", payload = {
          Severity.Error.class, Impact.Legal.class}) String value) {
    super(value);
  }

  @Override
  @Case(value = CaseMode.LOWER, message = "{LowerCase.lang.value}", payload = {Severity.Error.class, Impact.Legal.class})
  public String getValue() {
    return super.getValue();
  }

}
