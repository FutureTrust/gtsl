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

package eu.futuretrust.gtsl.model.data.extension;

import eu.futuretrust.gtsl.jaxb.tsl.ExtensionsListTypeJAXB;
import eu.futuretrust.gtsl.model.data.abstracts.ListModel;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class ServiceInformationExtensionListType extends
    ListModel<ServiceInformationExtensionType> {

  public ServiceInformationExtensionListType() {
    super();
  }

  public ServiceInformationExtensionListType(List<ServiceInformationExtensionType> values) {
    super(values);
  }

  public ServiceInformationExtensionListType(ExtensionsListTypeJAXB serviceInformationExtensions) {
    super(CollectionUtils.isNotEmpty(serviceInformationExtensions.getExtension())
        ? serviceInformationExtensions.getExtension().stream()
        .map(ServiceInformationExtensionType::new).collect(Collectors.toList())
        : Collections.emptyList());
  }

  @Override
  public List<ServiceInformationExtensionType> getValues() {
    return values;
  }

  public ExtensionsListTypeJAXB asJAXB() {
    if (CollectionUtils.isNotEmpty(values)) {
      ExtensionsListTypeJAXB extensions = new ExtensionsListTypeJAXB();
      extensions.getExtension().addAll(
          values.stream().map(ServiceInformationExtensionType::asJAXB)
              .filter(Objects::nonNull)
              .collect(Collectors.toList()));
      if (CollectionUtils.isNotEmpty(extensions.getExtension())) {
        return extensions;
      }
    }
    return null;
  }
}
