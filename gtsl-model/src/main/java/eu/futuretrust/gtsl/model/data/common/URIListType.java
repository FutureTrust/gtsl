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

import eu.futuretrust.gtsl.jaxb.tsl.NonEmptyURIListTypeJAXB;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.1.4 Language support
 *
 * <xsd:complexType name="NonEmptyURIListType"> <xsd:sequence> <xsd:element name="URI"
 * type="tsl:NonEmptyURIType" maxOccurs="unbounded"/> </xsd:sequence> </xsd:complexType>
 */
public class URIListType extends ListModel<NonEmptyURIType> {

  public URIListType() {
    super();
  }

  public URIListType(List<NonEmptyURIType> values) {
    super(values);
  }

  public URIListType(NonEmptyURIListTypeJAXB distributionPoints) {
    super(CollectionUtils.isNotEmpty(distributionPoints.getURI()) ? distributionPoints.getURI()
        .stream().map(NonEmptyURIType::new).collect(Collectors.toList()) : Collections.emptyList());
  }

  @Override
  public List<NonEmptyURIType> getValues() {
    return values;
  }

  public NonEmptyURIListTypeJAXB asJAXB() {
    NonEmptyURIListTypeJAXB nonEmptyList = new NonEmptyURIListTypeJAXB();
    if (CollectionUtils.isNotEmpty(values)) {
      nonEmptyList.getURI()
          .addAll(values.stream().map(NonEmptyURIType::getValue).collect(Collectors.toList()));
    }
    return nonEmptyList;
  }
}
