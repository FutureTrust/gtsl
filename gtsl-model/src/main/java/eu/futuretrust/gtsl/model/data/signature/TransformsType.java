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

package eu.futuretrust.gtsl.model.data.signature;

import eu.futuretrust.gtsl.jaxb.xmldsig.TransformsTypeJAXB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TransformsType {

  private List<TransformType> transforms;

  public TransformsType (final TransformsTypeJAXB transformsTypeJAXB) {

    if (transformsTypeJAXB.getTransform().isEmpty()) {
      transforms = Collections.emptyList();
    }
    else {
      transforms = transformsTypeJAXB.getTransform()
              .stream()
              .map(TransformType::new)
              .collect(Collectors.toList());
    }
  }

  public List<TransformType> getTransforms() {

    if (transforms == null) {
      transforms = new ArrayList<>();
    }
    return transforms;
  }

  public void setTransforms(List<TransformType> transforms) {
    this.transforms = transforms;
  }


  public TransformsTypeJAXB asJAXB() {

    final TransformsTypeJAXB transformsTypeJAXB = new TransformsTypeJAXB();
    transformsTypeJAXB.getTransform()
        .addAll(
            transforms.stream()
              .map(t -> t.asJAXB())
              .collect(Collectors.toList()));

    return transformsTypeJAXB;
  }
}
