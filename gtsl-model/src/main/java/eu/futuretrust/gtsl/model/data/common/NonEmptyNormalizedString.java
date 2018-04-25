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

import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.1.4 Language support
 *
 * <xsd:simpleType name="NonEmptyNormalizedString">
 *   <xsd:restriction base="xsd:normalizedString">
 *     <xsd:minLength value="1"/>
 *   </xsd:restriction>
 * </xsd:simpleType>
 *
 * The lexical space of xsd:normalizedString is unconstrained (any valid XML character may be used).
 * Its value space is the set of strings after whitespace replacement—i.e., after any occurrence of
 * #x9 (tab), #xA (linefeed), and #xD (carriage return) have been replaced by an occurrence of #x20
 * (space) without any whitespace collapsing.
 * This class is the same as NonEmptyString but the object is normalized when converts in XML.
 */
public class NonEmptyNormalizedString extends StringType {

  public NonEmptyNormalizedString() {
  }

  public NonEmptyNormalizedString(
      @NotNull(message = "{NotNull.string.value}", payload = {
          Severity.Error.class, Impact.Legal.class}) String value) {
    super(value);
  }

  @Override
  @NotBlank(message = "{NotBlank.nonEmptyNormalizedString.value}", payload = {Severity.Error.class, Impact.Legal.class})
  public String getValue() {
    return super.getValue();
  }
}
