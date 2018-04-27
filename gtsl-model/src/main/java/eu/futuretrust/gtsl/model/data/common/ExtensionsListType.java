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

import eu.futuretrust.gtsl.jaxb.tsl.ExtensionsListTypeJAXB;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

/**
 * Specified in TS 119 612 v2.1.1 clause 5.3.17 Scheme extensions
 *
 * <xsd:element name="Extension" type="tsl:ExtensionType"/> <xsd:complexType
 * name="ExtensionsListType"> <xsd:sequence> <xsd:element ref="tsl:Extension"
 * maxOccurs="unbounded"/> </xsd:sequence> </xsd:complexType>
 *
 * <xsd:element name="Extension" type="tsl:ExtensionType"/>
 */
public class ExtensionsListType extends ListModel<ExtensionType> {

  public ExtensionsListType() {
    super();
  }

  public ExtensionsListType(List<ExtensionType> values) {
    super(values);
  }

  public ExtensionsListType(ExtensionsListTypeJAXB schemeExtensions) {
    super(CollectionUtils.isNotEmpty(schemeExtensions.getExtension()) ? schemeExtensions
        .getExtension().stream().map(ExtensionType::new).collect(Collectors.toList())
        : Collections.emptyList());
  }

  public ExtensionsListTypeJAXB asJAXB() {
    if (CollectionUtils.isNotEmpty(values)) {
      ExtensionsListTypeJAXB extensions = new ExtensionsListTypeJAXB();
      extensions.getExtension()
          .addAll(values.stream().map(ExtensionType::asJAXB).collect(Collectors.toList()));
      return extensions;
    }
    return null;
  }
}
