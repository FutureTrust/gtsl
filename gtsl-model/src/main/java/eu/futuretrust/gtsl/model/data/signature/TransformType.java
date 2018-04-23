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

import eu.futuretrust.gtsl.jaxb.xmldsig.TransformTypeJAXB;

import java.util.ArrayList;
import java.util.List;

public class TransformType {

  private String algorithm;
  private List<Object> content;

  public TransformType (final TransformTypeJAXB transformTypeJAXB) {

    this.algorithm = transformTypeJAXB.getAlgorithm();
    this.content = transformTypeJAXB.getContent();
  }

  public List<Object> getContent() {
    if (content == null) {
      content = new ArrayList<Object>();
    }
    return this.content;
  }

  public void setContent (final List<Object> content) {
    this.content = content;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm (final String algorithm) {

    this.algorithm = algorithm;
  }

  public TransformTypeJAXB asJAXB() {

    final TransformTypeJAXB transformTypeJAXB = new TransformTypeJAXB();

    transformTypeJAXB.setAlgorithm(algorithm);
    transformTypeJAXB.getContent().addAll(content);

    return transformTypeJAXB;
  }
}
