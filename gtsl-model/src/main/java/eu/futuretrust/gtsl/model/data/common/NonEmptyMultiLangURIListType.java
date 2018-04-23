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
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.1.4 Language support
 *
 * <xsd:complexType name="NonEmptyMultiLangURIListType">
 *   <xsd:sequence>
 *     <xsd:element name="URI" type="tsl:NonEmptyMultiLangURIType" maxOccurs="unbounded"/>
 *   </xsd:sequence>
 * </xsd:complexType>
 */
public class NonEmptyMultiLangURIListType extends NonEmptyMultiLangURIOptionalListType {

  public NonEmptyMultiLangURIListType() {
  }

  public NonEmptyMultiLangURIListType(
      List<NonEmptyMultiLangURIType> values) {
    super(values);
  }

  public NonEmptyMultiLangURIListType(NonEmptyMultiLangURIListTypeJAXB schemeInformationURI) {
    super(schemeInformationURI.getURI().stream().map(NonEmptyMultiLangURIType::new)
        .collect(Collectors.toList()));
  }

  @Override
  @NotEmpty(message = "{NotEmpty.nonEmptyMultiLangURIList.values}", payload = {Severity.Error.class, Impact.Legal.class})
  public List<NonEmptyMultiLangURIType> getValues() {
    return super.getValues();
  }

}
