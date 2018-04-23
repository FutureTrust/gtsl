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

import javax.validation.constraints.NotNull;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.1.4 Language support
 *
 * <xsd:simpleType name="string" id="string">
 *   <xsd:restriction base="xsd:anySimpleType">
 *     <xsd:whiteSpace value="preserve"/>
 *   </xsd:restriction>
 * </xsd:simpleType>
 */
public class StringType {

  @NotNull(message = "{NotNull.string.value}", payload = {Severity.Error.class, Impact.Legal.class})
  protected String value;

  public StringType() {
    this.value = "";
  }

  public StringType(
      @NotNull(message = "{NotNull.string.value}", payload = {
          Severity.Error.class, Impact.Legal.class}) String value) {
    if (value != null) {
      this.value = value.trim();
    }
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    StringType that = (StringType) o;

    return value != null ? value.equals(that.value) : that.value == null;
  }

  @Override
  public int hashCode() {
    return value != null ? value.hashCode() : 0;
  }

  @Override
  public String toString() {
    return value;
  }

}
