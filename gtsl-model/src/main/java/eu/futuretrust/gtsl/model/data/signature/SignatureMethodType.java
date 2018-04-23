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

import eu.futuretrust.gtsl.jaxb.xmldsig.SignatureMethodTypeJAXB;

import java.util.ArrayList;
import java.util.List;

public class SignatureMethodType {

  protected List<Object> content;
  protected String algorithm;

  public SignatureMethodType (final SignatureMethodTypeJAXB signatureMethodTypeJAXB) {

    this.content = signatureMethodTypeJAXB.getContent();
    this.algorithm = signatureMethodTypeJAXB.getAlgorithm();
  }

  public List<Object> getContent () {

    if (content == null) {
      content = new ArrayList<Object>();
    }
    return content;
  }

  public void setContent(List<Object> content) {
    this.content = content;
  }

  public String getAlgorithm () {

    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public SignatureMethodTypeJAXB asJAXB() {

    final SignatureMethodTypeJAXB signatureMethodTypeJAXB = new SignatureMethodTypeJAXB();
    signatureMethodTypeJAXB.setAlgorithm(algorithm);
    signatureMethodTypeJAXB.getContent().addAll(content);

    return signatureMethodTypeJAXB;
  }
}
