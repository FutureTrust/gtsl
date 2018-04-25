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

import eu.futuretrust.gtsl.jaxb.tsl.MultiLangNormStringTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.1.4 Language support
 *
 * <xsd:complexType name="MultiLangNormStringType">
 *   <xsd:simpleContent>
 *     <xsd:extension base="tsl:NonEmptyNormalizedString">
 *       <xsd:attribute ref="xml:lang" use="required"/>
 *     </xsd:extension>
 *   </xsd:simpleContent>
 * </xsd:complexType>
 *
 * A multilingual character string shall be a character string as defined in ISO/IEC 10646 [5]
 * encoded in UTF-8. Each multilingual character string shall consist of two parts: a tag,
 * conformant to IETF RFC 5646 [11] and in lower case, that identifies the language in which the
 * string is expressed, and the text in that language. The same content may be represented in
 * multiple languages by a sequence of multilingual character strings.
 */
public class MultiLangNormStringType extends NonEmptyNormalizedString implements MultiLang {

  @NotNull(message = "{NotNull.multiLangNormString.lang}", payload = {Severity.Error.class, Impact.Legal.class})
  @Valid
  protected Lang lang;

  public MultiLangNormStringType() {
  }

  public MultiLangNormStringType(MultiLangNormStringTypeJAXB multiLangJAXB) {
    super(multiLangJAXB != null ? multiLangJAXB.getValue() : null);
    if (multiLangJAXB != null) {
      this.lang = new Lang(multiLangJAXB.getLang());
    }
  }

  public MultiLangNormStringType(
      @NotNull(message = "{NotNull.string.value}", payload = {
          Severity.Error.class, Impact.Legal.class}) String value) {
    super(value);
  }

  public MultiLangNormStringTypeJAXB asJAXB() {
    MultiLangNormStringTypeJAXB multiLang = new MultiLangNormStringTypeJAXB();
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
