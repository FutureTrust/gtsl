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

package eu.futuretrust.gtsl.model.data.tsp;

import eu.futuretrust.gtsl.jaxb.tsl.TSPServicesListTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import eu.futuretrust.gtsl.model.data.ts.TSPServiceType;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.4.6 TSP Services (list of services)
 *
 * <xsd:complexType name="TSPServicesListType"> <xsd:sequence> <xsd:element ref="tsl:TSPService"
 * maxOccurs="unbounded"/> </xsd:sequence> </xsd:complexType>
 *
 * <xsd:element name="TSPService" type="tsl:TSPServiceType"/>
 */
public class TSPServicesListType extends ListModel<TSPServiceType> {

  public TSPServicesListType() {
  }

  public TSPServicesListType(TSPServicesListTypeJAXB tspServices) {
    super(CollectionUtils.isNotEmpty(tspServices.getTSPService()) ? tspServices.getTSPService()
        .stream().map(TSPServiceType::new).collect(Collectors.toList()) : Collections.emptyList());
  }

  @Override
  @NotEmpty(message = "{NotEmpty.tspServicesList.values}", payload = {Severity.Error.class,
      Impact.Legal.class})
  public List<TSPServiceType> getValues() {
    return super.getValues();
  }

  public TSPServicesListTypeJAXB asJAXB() {
    TSPServicesListTypeJAXB tspServices = new TSPServicesListTypeJAXB();
    if (CollectionUtils.isNotEmpty(values)) {
      tspServices.getTSPService()
          .addAll(values.stream().map(TSPServiceType::asJAXB).collect(Collectors.toList()));
    }
    return tspServices;
  }
}
