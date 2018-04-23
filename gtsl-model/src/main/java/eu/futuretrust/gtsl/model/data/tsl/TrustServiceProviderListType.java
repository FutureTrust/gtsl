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

import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.jaxb.tsl.TrustServiceProviderListTypeJAXB;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import eu.futuretrust.gtsl.model.data.tsp.TSPType;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.18 Trust Service Provider List
 *
 * <xsd:complexType name="TrustServiceProviderListType"> <xsd:sequence> <!-- Specified in TS 119 612
 * v2.1.1 clause 5.3.18 Trust Service Provider List --> <xsd:element ref="tsl:TrustServiceProvider"
 * maxOccurs="unbounded"/> </xsd:sequence> </xsd:complexType>
 *
 * <xsd:element name="TrustServiceProvider" type="tsl:TSPType"/>
 */
public class TrustServiceProviderListType extends ListModel<TSPType> {

  public TrustServiceProviderListType() {
  }

  public TrustServiceProviderListType(List<TSPType> values) {
    super(values);
  }

  public TrustServiceProviderListType(TrustServiceProviderListTypeJAXB trustServiceProviderList) {
    super(CollectionUtils.isNotEmpty(trustServiceProviderList.getTrustServiceProvider()) ?
        trustServiceProviderList.getTrustServiceProvider()
            .stream()
            .map(TSPType::new)
            .collect(Collectors.toList())
        : Collections.emptyList());
  }

  @Override
  @NotEmpty(message = "{NotEmpty.trustServiceProviderList.values}", payload = {Severity.Info.class,
      Impact.Legal.class})
  public List<TSPType> getValues() {
    return super.getValues();
  }

  public TrustServiceProviderListTypeJAXB asJAXB() {
    if (CollectionUtils.isNotEmpty(values)) {
      TrustServiceProviderListTypeJAXB trustServiceProviderList = new TrustServiceProviderListTypeJAXB();
      trustServiceProviderList.getTrustServiceProvider()
          .addAll(values.stream().map(TSPType::asJAXB).collect(Collectors.toList()));
      return trustServiceProviderList;
    }
    return null;
  }
}
