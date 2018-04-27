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

import eu.futuretrust.gtsl.jaxb.tsl.PostalAddressListTypeJAXB;
import eu.futuretrust.gtsl.model.constraints.payload.Impact;
import eu.futuretrust.gtsl.model.constraints.payload.Severity;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;
import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.5.1 Scheme operator postal address
 *
 * <xsd:complexType name="PostalAddressListType"> <xsd:sequence> <xsd:element
 * ref="tsl:PostalAddress" maxOccurs="unbounded"/> </xsd:sequence> </xsd:complexType>
 *
 * <xsd:element name="PostalAddress" type="tsl:PostalAddressType"/>
 */
public class PostalAddressListType extends ListModel<PostalAddressType> {

  public PostalAddressListType() {
    super();
  }

  public PostalAddressListType(List<PostalAddressType> values) {
    super(values);
  }

  public PostalAddressListType(PostalAddressListTypeJAXB postalAddresses) {
    super(CollectionUtils.isNotEmpty(postalAddresses.getPostalAddress()) ? postalAddresses
        .getPostalAddress().stream().map(PostalAddressType::new).collect(Collectors.toList())
        : Collections.emptyList());
  }

  public PostalAddressListTypeJAXB asJAXB() {
    PostalAddressListTypeJAXB postalAddressList = new PostalAddressListTypeJAXB();
    if (CollectionUtils.isNotEmpty(values)) {
      postalAddressList.getPostalAddress()
          .addAll(values.stream().map(PostalAddressType::asJAXB).collect(Collectors.toList()));
    }
    return postalAddressList;
  }

  @Override
  @NotEmpty(message = "{NotEmpty.postalAddressList.values}", payload = {Severity.Error.class,
      Impact.Legal.class})
  public List<PostalAddressType> getValues() {
    return super.getValues();
  }

}
