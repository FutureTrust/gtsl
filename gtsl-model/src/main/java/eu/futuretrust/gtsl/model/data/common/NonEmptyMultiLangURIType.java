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
import eu.futuretrust.gtsl.model.constraints.payload.Impact.Legal;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.constraints.payload.Severity.Error;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.1.4 Language support
 *
 * <xsd:complexType name="NonEmptyMultiLangURIType">
 *   <xsd:simpleContent>
 *     <xsd:extension base="tsl:NonEmptyURIType">
 *       <xsd:attribute ref="xml:lang" use="required"/>
 *     </xsd:extension>
 *   </xsd:simpleContent>
 * </xsd:complexType>
 */
public class NonEmptyMultiLangURIType extends NonEmptyURIType implements MultiLang {

  @NotNull(message = "{NotNull.nonEmptyMultiLangURI.lang}", payload = {Severity.Error.class, Impact.Legal.class})
  @Valid
  protected Lang lang;

  public NonEmptyMultiLangURIType() {
  }

  public NonEmptyMultiLangURIType(
      @NotNull(message = "{NotNull.string.value}", payload = {
          Error.class, Legal.class}) String value) {
    super(value);
  }

  public NonEmptyMultiLangURIType(NonEmptyMultiLangURITypeJAXB multiLangURI) {
    super(multiLangURI != null ? multiLangURI.getValue() : null);
    if (multiLangURI != null ) {
      this.lang = new Lang(multiLangURI.getLang());
    }
  }

  public NonEmptyMultiLangURIType(
      @NotNull(message = "{NotNull.string.value}", payload = {
          Severity.Error.class, Impact.Legal.class}) String value, Lang lang) {
    super(value);
    this.lang = lang;
  }

  public NonEmptyMultiLangURITypeJAXB asJAXB() {
    NonEmptyMultiLangURITypeJAXB multiLang = new NonEmptyMultiLangURITypeJAXB();
    if (lang != null) {
      multiLang.setLang(lang.getValue());
    }
    multiLang.setValue(value);
    return multiLang;
  }

  @Override
  public Lang getLang() {
    return lang;
  }

  @Override
  public void setLang(Lang lang) {
    this.lang = lang;
  }

}
