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

import eu.futuretrust.gtsl.jaxb.tsl.MultiLangStringTypeJAXB;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import eu.futuretrust.gtsl.model.data.common.MultiLangStringType;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

/**
 * Used in the multi-type list in
 *
 * @see PolicyOrLegalnoticeType
 *
 * <xsd:element name="TSLLegalNotice" type="tsl:MultiLangStringType" maxOccurs="unbounded"/>
 */
public class TSLLegalNotice extends ListModel<MultiLangStringType> {

  public TSLLegalNotice() {
    super();
  }

  public TSLLegalNotice(List<MultiLangStringType> values) {
    super(values);
  }

  public List<MultiLangStringTypeJAXB> asJAXB() {
    if (CollectionUtils.isNotEmpty(values)) {
      return values.stream().map(MultiLangStringType::asJAXB).collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

  public List<MultiLangStringType> getValues() {
    return super.getValues();
  }

}
