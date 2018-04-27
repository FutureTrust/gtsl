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

import eu.futuretrust.gtsl.jaxb.tsl.InternationalNamesTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.1.4 Language support
 *
 * <xsd:complexType name="InternationalNamesType"> <xsd:sequence> <xsd:element name="Name"
 * common="tsl:MultiLangNormStringType" maxOccurs="unbounded"/> </xsd:sequence> </xsd:complexType>
 */
public class InternationalNamesType extends ListModel<MultiLangNormStringType> {

  public InternationalNamesType() {

  }


  public InternationalNamesType(List<MultiLangNormStringType> list) {
    super(list);
  }

  public InternationalNamesType(InternationalNamesTypeJAXB schemeOperatorName) {
    super(CollectionUtils.isNotEmpty(schemeOperatorName.getName()) ? schemeOperatorName.getName()
        .stream().map(MultiLangNormStringType::new).collect(Collectors.toList())
        : Collections.emptyList());
  }

  public InternationalNamesTypeJAXB asJAXB() {
    InternationalNamesTypeJAXB names = new InternationalNamesTypeJAXB();
    if (CollectionUtils.isNotEmpty(values)) {
      names.getName()
          .addAll(
              values.stream().map(MultiLangNormStringType::asJAXB).collect(Collectors.toList()));
    }
    return names;
  }

  @Override
  @NotEmpty(message = "{NotEmpty.internationalNames.values}", payload = {Severity.Error.class,
      Impact.Legal.class})
  public List<MultiLangNormStringType> getValues() {
    return values;
  }

}
