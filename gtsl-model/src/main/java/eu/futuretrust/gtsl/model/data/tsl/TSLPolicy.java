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

package eu.futuretrust.gtsl.model.data.tsl;

import eu.futuretrust.gtsl.jaxb.tsl.NonEmptyMultiLangURITypeJAXB;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import eu.futuretrust.gtsl.model.data.common.NonEmptyMultiLangURIType;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used in the multi-type attribute in
 *
 * @see PolicyOrLegalnoticeType
 *
 * <xsd:element name="TSLPolicy" type="tsl:NonEmptyMultiLangURIType" maxOccurs="unbounded"/>
 */
public class TSLPolicy extends ListModel<NonEmptyMultiLangURIType> {

  public TSLPolicy() {
    super();
  }

  public TSLPolicy(List<NonEmptyMultiLangURIType> values) {
    super(values);
  }

  public List<NonEmptyMultiLangURITypeJAXB> asJAXB() {
    if (CollectionUtils.isNotEmpty(values)) {
      return values.stream().map(NonEmptyMultiLangURIType::asJAXB).collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

  public List<NonEmptyMultiLangURIType> getValues() {
    return super.getValues();
  }
}
