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

import eu.futuretrust.gtsl.jaxb.tsl.NonEmptyMultiLangURIListTypeJAXB;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.1.4 Language support
 *
 * <xsd:complexType name="NonEmptyMultiLangURIListType">
 *   <xsd:sequence>
 *     <xsd:element name="URI" type="tsl:NonEmptyMultiLangURIType" maxOccurs="unbounded"/>
 *   </xsd:sequence>
 * </xsd:complexType>
 */
public class NonEmptyMultiLangURIOptionalListType extends ListModel<NonEmptyMultiLangURIType> {

  public NonEmptyMultiLangURIOptionalListType() {
  }

  public NonEmptyMultiLangURIOptionalListType(
      List<NonEmptyMultiLangURIType> values) {
    super(values);
  }

  public NonEmptyMultiLangURIOptionalListType(NonEmptyMultiLangURIListTypeJAXB schemeInformationURI) {
    super(schemeInformationURI.getURI().stream().map(NonEmptyMultiLangURIType::new)
        .collect(Collectors.toList()));
  }

  @Override
  public List<NonEmptyMultiLangURIType> getValues() {
    return values;
  }

  public NonEmptyMultiLangURIListTypeJAXB asJAXB() {
    NonEmptyMultiLangURIListTypeJAXB multiLangList = new NonEmptyMultiLangURIListTypeJAXB();
    if (CollectionUtils.isNotEmpty(values)) {
      multiLangList.getURI().addAll(values.stream().map(NonEmptyMultiLangURIType::asJAXB).collect(
          Collectors.toList()));
    }
    return multiLangList;
  }
}
