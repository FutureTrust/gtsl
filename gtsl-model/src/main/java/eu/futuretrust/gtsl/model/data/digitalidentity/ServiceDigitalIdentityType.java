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

import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.jaxb.tsl.DigitalIdentityListTypeJAXB;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.5.3 Service digital identity
 *
 * <xsd:complexType name="ServiceDigitalIdentityType"> <xsd:sequence> <xsd:element name="DigitalId"
 * type="tsl:DigitalIdentityType" minOccurs="0" maxOccurs="unbounded"/> </xsd:sequence>
 * </xsd:complexType>
 */
public class ServiceDigitalIdentityType extends ListModel<DigitalIdentityType> {

  public ServiceDigitalIdentityType() {
  }

  public ServiceDigitalIdentityType(List<DigitalIdentityType> values) {
    super(values);
  }

  public ServiceDigitalIdentityType(DigitalIdentityListTypeJAXB digitalIdentityList) {
    super(CollectionUtils.isNotEmpty(digitalIdentityList.getDigitalId()) ? digitalIdentityList
        .getDigitalId().stream().map(DigitalIdentityType::new).collect(Collectors.toList())
        : new ArrayList<>());
  }

  @Override
  @NotEmpty(message = "{NotEmpty.serviceDigitalIdentity.values}", payload = {Severity.Error.class,
      Impact.Legal.class})
  public List<DigitalIdentityType> getValues() {
    return super.getValues();
  }

  public DigitalIdentityListTypeJAXB asJAXB() {
    DigitalIdentityListTypeJAXB serviceDigitalIdentityList = new DigitalIdentityListTypeJAXB();
    if (CollectionUtils.isNotEmpty(values)) {
      for (DigitalIdentityType digitalId : values) {
        DigitalIdentityListTypeJAXB digitalIdJAXB = digitalId.asJAXB();
        if (digitalIdJAXB != null) {
          serviceDigitalIdentityList.getDigitalId().addAll(digitalIdJAXB.getDigitalId());
        }
      }
    }

    if (CollectionUtils.isNotEmpty(serviceDigitalIdentityList.getDigitalId())) {
      return serviceDigitalIdentityList;
    } else {
      return null;
    }
  }
}
