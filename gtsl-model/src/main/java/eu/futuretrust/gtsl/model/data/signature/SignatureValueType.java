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

import eu.futuretrust.gtsl.jaxb.xmldsig.SignatureValueTypeJAXB;

public class SignatureValueType {

  private byte[] value;
  private String id;

  public SignatureValueType (final SignatureValueTypeJAXB signatureValue) {

    this.value = signatureValue.getValue();
    this.id = signatureValue.getId();
  }

  public byte[] getValue() {
    return value;
  }

  public void setValue(byte[] value) {
    this.value = value;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SignatureValueTypeJAXB asJAXB() {

    SignatureValueTypeJAXB signatureValueTypeJAXB = new SignatureValueTypeJAXB();
    signatureValueTypeJAXB.setId(id);
    signatureValueTypeJAXB.setValue(value);
    return signatureValueTypeJAXB;
  }
}
