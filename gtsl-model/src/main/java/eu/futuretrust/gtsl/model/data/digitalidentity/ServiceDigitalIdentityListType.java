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

package eu.futuretrust.gtsl.model.data.digitalidentity;

import eu.futuretrust.gtsl.jaxb.tsl.ServiceDigitalIdentityListTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.collections.CollectionUtils;

/**
 * <xsd:complexType name="ServiceDigitalIdentityListType"> <xsd:sequence> <!-- Specified in TS 119
 * 612 v2.1.1 clause 5.5.3 Service digital identity --> <xsd:element
 * ref="tsl:ServiceDigitalIdentity" maxOccurs="unbounded"/> </xsd:sequence> </xsd:complexType>
 */
public class ServiceDigitalIdentityListType extends ListModel<ServiceDigitalIdentityType> {

  public ServiceDigitalIdentityListType() {
  }

  public ServiceDigitalIdentityListType(List<ServiceDigitalIdentityType> values) {
    super(values);
  }

  public ServiceDigitalIdentityListType(
      ServiceDigitalIdentityListTypeJAXB serviceDigitalIdentities) {
    super(CollectionUtils.isNotEmpty(serviceDigitalIdentities.getServiceDigitalIdentity())
        ? serviceDigitalIdentities.getServiceDigitalIdentity().stream()
        .map(ServiceDigitalIdentityType::new).collect(Collectors.toList())
        : new ArrayList<>());
  }

  @Override
  @NotEmpty(message = "{NotEmpty.serviceDigitalIdentityList.values}", payload = {
      Severity.Error.class, Impact.Legal.class})
  public List<ServiceDigitalIdentityType> getValues() {
    return super.getValues();
  }

  public ServiceDigitalIdentityListTypeJAXB asJAXB() {
    ServiceDigitalIdentityListTypeJAXB serviceDigitalIdentityList = new ServiceDigitalIdentityListTypeJAXB();
    if (CollectionUtils.isNotEmpty(values)) {
      serviceDigitalIdentityList.getServiceDigitalIdentity()
          .addAll(values.stream()
              .map(ServiceDigitalIdentityType::asJAXB)
              .filter(Objects::nonNull)
              .collect(Collectors.toList()));
    }

    if (CollectionUtils.isNotEmpty(serviceDigitalIdentityList.getServiceDigitalIdentity())) {
      return serviceDigitalIdentityList;
    } else {
      return null;
    }
  }
}

