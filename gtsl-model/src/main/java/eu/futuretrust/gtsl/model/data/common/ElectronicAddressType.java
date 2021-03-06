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

import eu.futuretrust.gtsl.jaxb.tsl.ElectronicAddressTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.5.2 Scheme operator electronic address
 *
 * <xsd:complexType name="ElectronicAddressType"> <xsd:sequence> <xsd:element name="URI"
 * type="tsl:NonEmptyMultiLangURIType" maxOccurs="unbounded"/> </xsd:sequence> </xsd:complexType>
 */
public class ElectronicAddressType extends ListModel<ElectronicAddressURIType> {

  public ElectronicAddressType() {
  }

  public ElectronicAddressType(List<ElectronicAddressURIType> list) {
    super(list);
  }

  public ElectronicAddressType(ElectronicAddressTypeJAXB electronicAddress) {
    super(
        CollectionUtils.isNotEmpty(electronicAddress.getURI()) ? electronicAddress.getURI().stream()
            .map(ElectronicAddressURIType::new).collect(Collectors.toList())
            : Collections.emptyList());
  }

  public ElectronicAddressTypeJAXB asJAXB() {
    ElectronicAddressTypeJAXB electronicAddress = new ElectronicAddressTypeJAXB();
    if (CollectionUtils.isNotEmpty(values)) {
      electronicAddress.getURI().addAll(
          values.stream().map(NonEmptyMultiLangURIType::asJAXB).collect(Collectors.toList()));
    }
    return electronicAddress;
  }

  @Override
  @NotEmpty(message = "{NotEmpty.electronicAddress.values}", payload = {Severity.Error.class,
      Impact.Legal.class})
  public List<ElectronicAddressURIType> getValues() {
    return super.getValues();
  }

}
